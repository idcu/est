package ltd.idcu.est.core.impl.inject;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.api.exception.CircularDependencyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ConstructorInjector {
    
    private final Container container;
    
    public ConstructorInjector(Container container) {
        this.container = container;
    }
    
    public <T> T createInstance(Class<T> type) {
        try {
            Constructor<T> constructor = findSuitableConstructor(type);
            constructor.setAccessible(true);
            
            Object[] args = resolveConstructorArguments(constructor);
            return constructor.newInstance(args);
        } catch (CircularDependencyException e) {
            throw e;
        } catch (Exception e) {
            if (e.getCause() instanceof CircularDependencyException) {
                throw (CircularDependencyException) e.getCause();
            }
            throw new RuntimeException("Failed to create instance of " + type.getName(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> Constructor<T> findSuitableConstructor(Class<T> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        
        List<Constructor<?>> injectConstructors = new ArrayList<>();
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                injectConstructors.add(constructor);
            }
        }
        
        if (!injectConstructors.isEmpty()) {
            if (injectConstructors.size() > 1) {
                throw new IllegalStateException("Multiple @Inject constructors found in " + type.getName());
            }
            return (Constructor<T>) injectConstructors.get(0);
        }
        
        Arrays.sort(constructors, (c1, c2) -> Integer.compare(c2.getParameterCount(), c1.getParameterCount()));
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return (Constructor<T>) constructor;
            }
        }
        
        return (Constructor<T>) constructors[0];
    }
    
    private Object[] resolveConstructorArguments(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
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
