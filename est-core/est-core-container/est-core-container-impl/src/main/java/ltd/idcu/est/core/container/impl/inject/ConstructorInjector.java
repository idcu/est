package ltd.idcu.est.core.container.impl.inject;

import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.api.annotation.Inject;
import ltd.idcu.est.core.container.api.annotation.Qualifier;
import ltd.idcu.est.core.container.api.exception.CircularDependencyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConstructorInjector {

    private static class ConstructorMetadata<T> {
        final Constructor<T> constructor;
        final Parameter[] parameters;
        final String[] qualifiers;

        ConstructorMetadata(Constructor<T> constructor, Parameter[] parameters, String[] qualifiers) {
            this.constructor = constructor;
            this.parameters = parameters;
            this.qualifiers = qualifiers;
            this.constructor.setAccessible(true);
        }
    }

    private final Container container;
    private final Map<Class<?>, ConstructorMetadata<?>> constructorCache = new ConcurrentHashMap<>();

    public ConstructorInjector(Container container) {
        this.container = container;
    }

    public <T> T createInstance(Class<T> type) {
        try {
            @SuppressWarnings("unchecked")
            ConstructorMetadata<T> metadata = (ConstructorMetadata<T>) constructorCache.computeIfAbsent(type, this::buildConstructorMetadata);

            Object[] args = resolveConstructorArguments(metadata);
            return metadata.constructor.newInstance(args);
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
    private <T> ConstructorMetadata<T> buildConstructorMetadata(Class<T> type) {
        Constructor<T> constructor = findSuitableConstructor(type);
        Parameter[] parameters = constructor.getParameters();
        String[] qualifiers = new String[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(Qualifier.class)) {
                qualifiers[i] = parameters[i].getAnnotation(Qualifier.class).value();
            }
        }

        return new ConstructorMetadata<>(constructor, parameters, qualifiers);
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

    private Object[] resolveConstructorArguments(ConstructorMetadata<?> metadata) {
        Object[] args = new Object[metadata.parameters.length];

        for (int i = 0; i < metadata.parameters.length; i++) {
            Class<?> paramType = metadata.parameters[i].getType();
            String qualifier = metadata.qualifiers[i];

            if (qualifier != null) {
                args[i] = container.get(paramType, qualifier);
            } else {
                args[i] = container.get(paramType);
            }
        }

        return args;
    }
}
