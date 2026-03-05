package ltd.idcu.est.test.runner;

import ltd.idcu.est.test.annotation.*;
import ltd.idcu.est.test.result.TestResult;
import ltd.idcu.est.test.result.TestReporter;
import ltd.idcu.est.test.mock.MockInjector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TestExecutor {

    private final TestReporter reporter;

    public TestExecutor() {
        this(new TestReporter());
    }

    public TestExecutor(TestReporter reporter) {
        this.reporter = reporter;
    }

    public TestResult execute(Class<?> testClass) {
        long startTime = System.currentTimeMillis();
        
        List<TestMethod> testMethods = findTestMethods(testClass);
        List<Method> beforeEachMethods = findMethodsWithAnnotation(testClass, BeforeEach.class);
        List<Method> afterEachMethods = findMethodsWithAnnotation(testClass, AfterEach.class);
        List<Method> beforeAllMethods = findMethodsWithAnnotation(testClass, BeforeAll.class);
        List<Method> afterAllMethods = findMethodsWithAnnotation(testClass, AfterAll.class);

        int passed = 0;
        int failed = 0;
        int skipped = 0;
        List<Throwable> failures = new ArrayList<>();

        String className = getDisplayName(testClass);
        reporter.reportClassStart(className);

        Object instance = null;
        try {
            instance = createTestInstance(testClass);
        } catch (Exception e) {
            reporter.reportError("Failed to instantiate test class: " + e.getMessage());
            return new TestResult(testClass.getName(), 0, testMethods.size(), 0, failures);
        }

        try {
            executeStaticMethods(beforeAllMethods);
        } catch (Exception e) {
            reporter.reportError("BeforeAll failed: " + e.getMessage());
        }

        for (TestMethod testMethod : testMethods) {
            if (testMethod.isDisabled()) {
                skipped++;
                reporter.reportTestSkipped(testMethod.getDisplayName());
                continue;
            }

            if (testMethod.isParameterized()) {
                TestResult paramResult = executeParameterizedTest(instance, testMethod, 
                    beforeEachMethods, afterEachMethods);
                passed += paramResult.getPassed();
                failed += paramResult.getFailed();
                failures.addAll(paramResult.getFailures());
            } else {
                SingleTestResult result = executeSingleTest(instance, testMethod.getMethod(),
                    beforeEachMethods, afterEachMethods);
                if (result.passed) {
                    passed++;
                } else {
                    failed++;
                    if (result.failure != null) {
                        failures.add(result.failure);
                    }
                }
            }
        }

        try {
            executeStaticMethods(afterAllMethods);
        } catch (Exception e) {
            reporter.reportError("AfterAll failed: " + e.getMessage());
        }

        long totalDuration = System.currentTimeMillis() - startTime;
        reporter.reportClassEnd(className, passed, failed, skipped, totalDuration);

        return new TestResult(testClass.getName(), passed, failed, skipped, failures);
    }

    private Object createTestInstance(Class<?> testClass) throws Exception {
        Object instance = testClass.getDeclaredConstructor().newInstance();
        MockInjector.injectMocks(instance);
        return instance;
    }

    private void executeStaticMethods(List<Method> methods) throws Exception {
        for (Method method : methods) {
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                method.invoke(null);
            }
        }
    }

    private TestResult executeParameterizedTest(Object instance, TestMethod testMethod,
            List<Method> beforeEachMethods, List<Method> afterEachMethods) {
        
        List<Object[]> arguments = resolveArguments(instance, testMethod);
        int passed = 0;
        int failed = 0;
        List<Throwable> failures = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            Object[] args = arguments.get(i);
            String testName = formatParameterizedName(testMethod, args, i);
            long testStartTime = System.currentTimeMillis();

            try {
                for (Method beforeEach : beforeEachMethods) {
                    beforeEach.setAccessible(true);
                    beforeEach.invoke(instance);
                }

                testMethod.getMethod().setAccessible(true);
                testMethod.getMethod().invoke(instance, args);

                for (Method afterEach : afterEachMethods) {
                    afterEach.setAccessible(true);
                    afterEach.invoke(instance);
                }

                long duration = System.currentTimeMillis() - testStartTime;
                passed++;
                reporter.reportTestPassed(testName, duration);

            } catch (Throwable t) {
                long duration = System.currentTimeMillis() - testStartTime;
                Throwable cause = t.getCause() != null ? t.getCause() : t;
                failed++;
                failures.add(cause);
                reporter.reportTestFailed(testName, cause, duration);
            }
        }

        return new TestResult(testMethod.getMethod().getName(), passed, failed, 0, failures);
    }

    private List<Object[]> resolveArguments(Object instance, TestMethod testMethod) {
        Method method = testMethod.getMethod();
        List<Object[]> arguments = new ArrayList<>();

        if (method.isAnnotationPresent(ValueSource.class)) {
            ValueSource valueSource = method.getAnnotation(ValueSource.class);
            arguments.addAll(resolveValueSource(valueSource, method.getParameterTypes()));
        }

        if (method.isAnnotationPresent(CsvSource.class)) {
            CsvSource csvSource = method.getAnnotation(CsvSource.class);
            arguments.addAll(resolveCsvSource(csvSource, method.getParameterTypes()));
        }

        if (method.isAnnotationPresent(MethodSource.class)) {
            MethodSource methodSource = method.getAnnotation(MethodSource.class);
            arguments.addAll(resolveMethodSource(instance, methodSource, method.getParameterTypes()));
        }

        return arguments;
    }

    private List<Object[]> resolveValueSource(ValueSource valueSource, Class<?>[] paramTypes) {
        List<Object[]> arguments = new ArrayList<>();

        if (valueSource.strings().length > 0) {
            for (String value : valueSource.strings()) {
                arguments.add(new Object[]{convertValue(value, paramTypes[0])});
            }
        } else if (valueSource.ints().length > 0) {
            for (int value : valueSource.ints()) {
                arguments.add(new Object[]{value});
            }
        } else if (valueSource.longs().length > 0) {
            for (long value : valueSource.longs()) {
                arguments.add(new Object[]{value});
            }
        } else if (valueSource.doubles().length > 0) {
            for (double value : valueSource.doubles()) {
                arguments.add(new Object[]{value});
            }
        } else if (valueSource.booleans().length > 0) {
            for (boolean value : valueSource.booleans()) {
                arguments.add(new Object[]{value});
            }
        }

        return arguments;
    }

    private List<Object[]> resolveCsvSource(CsvSource csvSource, Class<?>[] paramTypes) {
        List<Object[]> arguments = new ArrayList<>();
        char delimiter = csvSource.delimiter();
        String nullValue = csvSource.nullValues();

        for (String line : csvSource.value()) {
            String[] parts = line.split(String.valueOf(delimiter));
            Object[] args = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length && i < parts.length; i++) {
                String part = parts[i].trim();
                if (part.equals(nullValue)) {
                    args[i] = null;
                } else {
                    args[i] = convertValue(part, paramTypes[i]);
                }
            }

            arguments.add(args);
        }

        return arguments;
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> resolveMethodSource(Object instance, MethodSource methodSource, Class<?>[] paramTypes) {
        List<Object[]> arguments = new ArrayList<>();
        String methodName = methodSource.value();

        if (methodName.isEmpty()) {
            methodName = "provideArguments";
        }

        try {
            Method providerMethod;
            Class<?> testClass = instance.getClass();

            try {
                providerMethod = testClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                providerMethod = testClass.getDeclaredMethod(methodName, Class[].class);
            }

            providerMethod.setAccessible(true);

            Object result;
            if (providerMethod.getParameterCount() == 0) {
                result = providerMethod.invoke(Modifier.isStatic(providerMethod.getModifiers()) ? null : instance);
            } else {
                result = providerMethod.invoke(Modifier.isStatic(providerMethod.getModifiers()) ? null : instance, (Object) paramTypes);
            }

            if (result instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof Object[] arr) {
                        arguments.add(arr);
                    } else {
                        arguments.add(new Object[]{item});
                    }
                }
            }

        } catch (Exception e) {
            reporter.reportError("Failed to resolve MethodSource: " + e.getMessage());
        }

        return arguments;
    }

    private Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value);
        } else if (targetType == short.class || targetType == Short.class) {
            return Short.parseShort(value);
        } else if (targetType == byte.class || targetType == Byte.class) {
            return Byte.parseByte(value);
        } else if (targetType == char.class || targetType == Character.class) {
            return value.charAt(0);
        }
        return value;
    }

    private String formatParameterizedName(TestMethod testMethod, Object[] args, int index) {
        ParameterizedTest annotation = testMethod.getMethod().getAnnotation(ParameterizedTest.class);
        String pattern = annotation.name();

        if (pattern.isEmpty()) {
            pattern = testMethod.getDisplayName() + " [" + index + "]";
        } else {
            pattern = pattern.replace("{index}", String.valueOf(index));
            pattern = pattern.replace("{displayName}", testMethod.getDisplayName());
            for (int i = 0; i < args.length; i++) {
                pattern = pattern.replace("{" + i + "}", String.valueOf(args[i]));
            }
            pattern = pattern.replace("{arguments}", formatArguments(args));
            pattern = pattern.replace("{argumentsWithNames}", formatArgumentsWithNames(testMethod.getMethod(), args));
        }

        return pattern;
    }

    private String formatArguments(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i]);
        }
        return sb.toString();
    }

    private String formatArgumentsWithNames(Method method, Object[] args) {
        StringBuilder sb = new StringBuilder();
        java.lang.reflect.Parameter[] params = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            if (i < params.length) {
                sb.append(params[i].getName()).append("=");
            }
            sb.append(args[i]);
        }
        return sb.toString();
    }

    private SingleTestResult executeSingleTest(Object instance, Method testMethod,
            List<Method> beforeEachMethods, List<Method> afterEachMethods) {
        
        String testName = getDisplayName(testMethod);
        long testStartTime = System.currentTimeMillis();

        try {
            for (Method beforeEach : beforeEachMethods) {
                beforeEach.setAccessible(true);
                beforeEach.invoke(instance);
            }

            testMethod.setAccessible(true);
            testMethod.invoke(instance);

            for (Method afterEach : afterEachMethods) {
                afterEach.setAccessible(true);
                afterEach.invoke(instance);
            }

            long duration = System.currentTimeMillis() - testStartTime;
            reporter.reportTestPassed(testName, duration);
            return new SingleTestResult(true, null);

        } catch (Throwable t) {
            long duration = System.currentTimeMillis() - testStartTime;
            Throwable cause = t.getCause() != null ? t.getCause() : t;
            reporter.reportTestFailed(testName, cause, duration);
            return new SingleTestResult(false, cause);
        }
    }

    private List<TestMethod> findTestMethods(Class<?> testClass) {
        List<TestMethod> methods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class) || 
                method.isAnnotationPresent(ParameterizedTest.class)) {
                methods.add(new TestMethod(method));
            }
        }
        return methods;
    }

    private List<Method> findMethodsWithAnnotation(Class<?> testClass, 
            Class<? extends java.lang.annotation.Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private String getDisplayName(Class<?> testClass) {
        if (testClass.isAnnotationPresent(DisplayName.class)) {
            return testClass.getAnnotation(DisplayName.class).value();
        }
        return testClass.getSimpleName();
    }

    private String getDisplayName(Method method) {
        if (method.isAnnotationPresent(DisplayName.class)) {
            return method.getAnnotation(DisplayName.class).value();
        }
        if (method.isAnnotationPresent(Test.class)) {
            String displayName = method.getAnnotation(Test.class).displayName();
            if (!displayName.isEmpty()) {
                return displayName;
            }
        }
        return method.getName();
    }

    private static class TestMethod {
        private final Method method;
        private final boolean parameterized;
        private final boolean disabled;
        private final String displayName;

        public TestMethod(Method method) {
            this.method = method;
            this.parameterized = method.isAnnotationPresent(ParameterizedTest.class);
            this.disabled = method.isAnnotationPresent(Disabled.class);
            this.displayName = resolveDisplayName();
        }

        private String resolveDisplayName() {
            if (method.isAnnotationPresent(DisplayName.class)) {
                return method.getAnnotation(DisplayName.class).value();
            }
            if (method.isAnnotationPresent(Test.class)) {
                String name = method.getAnnotation(Test.class).displayName();
                if (!name.isEmpty()) {
                    return name;
                }
            }
            return method.getName();
        }

        public Method getMethod() { return method; }
        public boolean isParameterized() { return parameterized; }
        public boolean isDisabled() { return disabled; }
        public String getDisplayName() { return displayName; }
    }

    private static class SingleTestResult {
        final boolean passed;
        final Throwable failure;

        SingleTestResult(boolean passed, Throwable failure) {
            this.passed = passed;
            this.failure = failure;
        }
    }
}
