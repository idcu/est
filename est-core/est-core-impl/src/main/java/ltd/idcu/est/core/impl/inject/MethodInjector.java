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

public class MethodInjector {

    private final Container container;

    public MethodInjector(Container container) {
        this.container = container;
    }

    public void injectMethods(Object instance) {
        Class<?> clazz = instance.getClass();
        List<Method> injectMethods = new ArrayList<>();
        
        while (clazz != null && clazz != Object.class) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Inject.class)) {
                    injectMethods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        
        injectMethods.sort(Comparator.comparing(Method::getName));
        
        for (Method method : injectMethods) {
            injectMethod(instance, method);
        }
    }

    private void injectMethod(Object instance, Method method) {
        try {
            method.setAccessible(true);
            Object[] args = resolveMethodArguments(method);
            method.invoke(instance, args);
        } catch (CircularDependencyException e) {
            throw e;
        } catch (Exception e) {
            if (e.getCause() instanceof CircularDependencyException) {
                throw (CircularDependencyException) e.getCause();
            }
            throw new RuntimeException("Failed to inject method: " + method.getName() + 
                " in class: " + instance.getClass().getName(), e);
        }
    }

    private Object[] resolveMethodArguments(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();
            String qualifier = null;

            if (param.isAnnotationPresent(Qualifier.class)) {
                qualifier = param.getAnnotation(Qualifier.class).value();
            }

            if (qualifier != null) {
                args[i] = container.get(paramType, qualifier);
            } else {
                args[i] = container.get(paramType);
            }
        }

        return args;
    }
}
