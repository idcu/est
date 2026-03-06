package ltd.idcu.est.core.impl.inject;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.api.exception.CircularDependencyException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodInjector {

    private static class MethodMetadata {
        final Method method;
        final Parameter[] parameters;
        final String[] qualifiers;

        MethodMetadata(Method method) {
            this.method = method;
            this.parameters = method.getParameters();
            this.qualifiers = new String[this.parameters.length];

            for (int i = 0; i < this.parameters.length; i++) {
                if (this.parameters[i].isAnnotationPresent(Qualifier.class)) {
                    this.qualifiers[i] = this.parameters[i].getAnnotation(Qualifier.class).value();
                }
            }

            this.method.setAccessible(true);
        }
    }

    private static class ClassMethodMetadata {
        final List<MethodMetadata> methods;

        ClassMethodMetadata(List<MethodMetadata> methods) {
            methods.sort(Comparator.comparing(m -> m.method.getName()));
            this.methods = methods;
        }
    }

    private final Container container;
    private final Map<Class<?>, ClassMethodMetadata> methodCache = new ConcurrentHashMap<>();

    public MethodInjector(Container container) {
        this.container = container;
    }

    public void injectMethods(Object instance) {
        ClassMethodMetadata metadata = methodCache.computeIfAbsent(instance.getClass(), this::buildClassMethodMetadata);
        for (MethodMetadata methodMeta : metadata.methods) {
            injectMethod(instance, methodMeta);
        }
    }

    private ClassMethodMetadata buildClassMethodMetadata(Class<?> type) {
        List<MethodMetadata> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != null && clazz != Object.class) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Inject.class)) {
                    methods.add(new MethodMetadata(method));
                }
            }
            clazz = clazz.getSuperclass();
        }
        return new ClassMethodMetadata(methods);
    }

    private void injectMethod(Object instance, MethodMetadata methodMeta) {
        try {
            Object[] args = resolveMethodArguments(methodMeta);
            methodMeta.method.invoke(instance, args);
        } catch (CircularDependencyException e) {
            throw e;
        } catch (Exception e) {
            if (e.getCause() instanceof CircularDependencyException) {
                throw (CircularDependencyException) e.getCause();
            }
            throw new RuntimeException("Failed to inject method: " + methodMeta.method.getName() +
                " in class: " + instance.getClass().getName(), e);
        }
    }

    private Object[] resolveMethodArguments(MethodMetadata methodMeta) {
        Object[] args = new Object[methodMeta.parameters.length];

        for (int i = 0; i < methodMeta.parameters.length; i++) {
            Class<?> paramType = methodMeta.parameters[i].getType();
            String qualifier = methodMeta.qualifiers[i];

            if (qualifier != null) {
                args[i] = container.get(paramType, qualifier);
            } else {
                args[i] = container.get(paramType);
            }
        }

        return args;
    }
}
