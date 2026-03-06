package ltd.idcu.est.test.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Mockito {

    private static final ThreadLocal<MockContext> currentContext = new ThreadLocal<>();

    private Mockito() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T mock(Class<T> classToMock) {
        if (classToMock.isInterface()) {
            return (T) Proxy.newProxyInstance(
                classToMock.getClassLoader(),
                new Class<?>[]{classToMock},
                new MockInvocationHandler(classToMock)
            );
        } else {
            throw new IllegalArgumentException(
                "Only interfaces can be mocked. For classes, consider extracting an interface.");
        }
    }

    public static <T> OngoingStubbing<T> when(T methodCall) {
        MockContext context = currentContext.get();
        if (context == null) {
            throw new IllegalStateException("No method call recorded. Use when(mock.method()).thenReturn(value)");
        }
        return new OngoingStubbingImpl<>(context);
    }

    public static <T> T verify(T mock) {
        return verify(mock, VerificationMode.times(1));
    }

    public static <T> T verify(T mock, VerificationMode mode) {
        if (!isMock(mock)) {
            throw new IllegalArgumentException("Argument must be a mock object");
        }
        MockInvocationHandler handler = getMockHandler(mock);
        handler.startVerification(mode);
        return mock;
    }

    public static VerificationMode times(int wantedNumberOfInvocations) {
        return VerificationMode.times(wantedNumberOfInvocations);
    }

    public static VerificationMode never() {
        return VerificationMode.times(0);
    }

    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return VerificationMode.atLeast(minNumberOfInvocations);
    }

    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return VerificationMode.atMost(maxNumberOfInvocations);
    }

    public static VerificationMode once() {
        return VerificationMode.times(1);
    }

    static boolean isMock(Object obj) {
        return Proxy.isProxyClass(obj.getClass()) && 
               Proxy.getInvocationHandler(obj) instanceof MockInvocationHandler;
    }

    static MockInvocationHandler getMockHandler(Object mock) {
        return (MockInvocationHandler) Proxy.getInvocationHandler(mock);
    }

    static void setContext(MockContext context) {
        currentContext.set(context);
    }

    static void clearContext() {
        currentContext.remove();
    }

    static MockContext getContext() {
        return currentContext.get();
    }

    static class MockInvocationHandler implements InvocationHandler {
        private final Class<?> mockType;
        private final Map<MethodKey, List<Stubbing>> stubbings = new HashMap<>();
        private final List<Invocation> invocations = new ArrayList<>();
        private VerificationMode verificationMode;
        private boolean verifying = false;

        public MockInvocationHandler(Class<?> mockType) {
            this.mockType = mockType;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return handleObjectMethod(proxy, method, args);
            }

            Invocation invocation = new Invocation(method, args);
            invocations.add(invocation);

            if (verifying) {
                return getDefaultValue(method.getReturnType());
            }

            MockContext context = new MockContext(this, method, args);
            Mockito.setContext(context);

            List<Stubbing> methodStubbings = stubbings.get(new MethodKey(method));
            if (methodStubbings != null) {
                for (Stubbing stubbing : methodStubbings) {
                    if (stubbing.matches(args)) {
                        Mockito.clearContext();
                        return stubbing.getResult();
                    }
                }
            }

            Mockito.clearContext();
            return getDefaultValue(method.getReturnType());
        }

        private Object handleObjectMethod(Object proxy, Method method, Object[] args) {
            String methodName = method.getName();
            return switch (methodName) {
                case "toString" -> "Mock for " + mockType.getSimpleName();
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                default -> null;
            };
        }

        public void addStubbing(Method method, Object[] args, Object result) {
            MethodKey key = new MethodKey(method);
            stubbings.computeIfAbsent(key, k -> new ArrayList<>())
                     .add(new Stubbing(args, result));
        }

        public void startVerification(VerificationMode mode) {
            this.verificationMode = mode;
            this.verifying = true;
        }

        public void verify(Invocation expected) {
            int count = 0;
            for (Invocation invocation : invocations) {
                if (invocation.matches(expected)) {
                    count++;
                }
            }
            verificationMode.verify(count, expected);
            verifying = false;
            verificationMode = null;
        }

        public List<Invocation> getInvocations() {
            return new ArrayList<>(invocations);
        }

        private Object getDefaultValue(Class<?> type) {
            if (type == boolean.class) return false;
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0d;
            if (type == char.class) return '\0';
            return null;
        }
    }

    static class MockContext {
        final MockInvocationHandler handler;
        final Method method;
        final Object[] args;

        MockContext(MockInvocationHandler handler, Method method, Object[] args) {
            this.handler = handler;
            this.method = method;
            this.args = args;
        }
    }

    static class MethodKey {
        private final Method method;

        MethodKey(Method method) {
            this.method = method;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof MethodKey other)) return false;
            return method.equals(other.method);
        }

        @Override
        public int hashCode() {
            return method.hashCode();
        }
    }

    static class Stubbing {
        private final Object[] args;
        private final Object result;

        Stubbing(Object[] args, Object result) {
            this.args = args;
            this.result = result;
        }

        boolean matches(Object[] otherArgs) {
            if (args == null && otherArgs == null) return true;
            if (args == null || otherArgs == null) return false;
            if (args.length != otherArgs.length) return false;
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    if (otherArgs[i] != null) return false;
                } else if (!args[i].equals(otherArgs[i])) {
                    return false;
                }
            }
            return true;
        }

        Object getResult() throws Throwable {
            if (result instanceof ThrowingAnswer) {
                throw ((ThrowingAnswer) result).getThrowable();
            }
            return result;
        }
    }

    static class Invocation {
        private final Method method;
        private final Object[] args;

        Invocation(Method method, Object[] args) {
            this.method = method;
            this.args = args != null ? args.clone() : null;
        }

        boolean matches(Invocation other) {
            if (!method.equals(other.method)) return false;
            if (args == null && other.args == null) return true;
            if (args == null || other.args == null) return false;
            if (args.length != other.args.length) return false;
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    if (other.args[i] != null) return false;
                } else if (!args[i].equals(other.args[i])) {
                    return false;
                }
            }
            return true;
        }

        Method getMethod() { return method; }
        Object[] getArgs() { return args; }
    }

    static class ThrowingAnswer {
        private final Throwable throwable;

        ThrowingAnswer(Throwable throwable) {
            this.throwable = throwable;
        }

        Throwable getThrowable() {
            return throwable;
        }
    }

    static class OngoingStubbingImpl<T> implements OngoingStubbing<T> {
        private final MockContext context;

        OngoingStubbingImpl(MockContext context) {
            this.context = context;
        }

        @Override
        public OngoingStubbing<T> thenReturn(T value) {
            context.handler.addStubbing(context.method, context.args, value);
            Mockito.clearContext();
            return this;
        }

        @Override
        public OngoingStubbing<T> thenReturn(T value, T... values) {
            thenReturn(value);
            for (T v : values) {
                thenReturn(v);
            }
            return this;
        }

        @Override
        public OngoingStubbing<T> thenThrow(Throwable... throwables) {
            for (Throwable t : throwables) {
                context.handler.addStubbing(context.method, context.args, new ThrowingAnswer(t));
            }
            Mockito.clearContext();
            return this;
        }

        @Override
        public OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableClass) {
            try {
                context.handler.addStubbing(context.method, context.args, 
                    new ThrowingAnswer(throwableClass.getDeclaredConstructor().newInstance()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate throwable", e);
            }
            Mockito.clearContext();
            return this;
        }

        @Override
        public OngoingStubbing<T> thenCallRealMethod() {
            throw new UnsupportedOperationException("thenCallRealMethod is not supported for interface mocks");
        }
    }
}
