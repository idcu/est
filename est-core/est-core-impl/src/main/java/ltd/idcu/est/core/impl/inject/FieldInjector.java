package ltd.idcu.est.core.impl.inject;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.api.exception.CircularDependencyException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldInjector {

    private static class FieldMetadata {
        final Field field;
        final Class<?> fieldType;
        final String qualifier;

        FieldMetadata(Field field) {
            this.field = field;
            this.fieldType = field.getType();
            this.qualifier = field.isAnnotationPresent(Qualifier.class)
                ? field.getAnnotation(Qualifier.class).value() : null;
            this.field.setAccessible(true);
        }
    }

    private static class ClassFieldMetadata {
        final List<FieldMetadata> fields;

        ClassFieldMetadata(List<FieldMetadata> fields) {
            this.fields = fields;
        }
    }

    private final Container container;
    private final Map<Class<?>, ClassFieldMetadata> fieldCache = new ConcurrentHashMap<>();

    public FieldInjector(Container container) {
        this.container = container;
    }

    public void injectFields(Object instance) {
        ClassFieldMetadata metadata = fieldCache.computeIfAbsent(instance.getClass(), this::buildClassFieldMetadata);
        for (FieldMetadata fieldMeta : metadata.fields) {
            injectField(instance, fieldMeta);
        }
    }

    private ClassFieldMetadata buildClassFieldMetadata(Class<?> type) {
        List<FieldMetadata> fields = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != null && clazz != Object.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    fields.add(new FieldMetadata(field));
                }
            }
            clazz = clazz.getSuperclass();
        }
        return new ClassFieldMetadata(fields);
    }

    private void injectField(Object instance, FieldMetadata fieldMeta) {
        try {
            Object value;
            if (fieldMeta.qualifier != null) {
                value = container.get(fieldMeta.fieldType, fieldMeta.qualifier);
            } else {
                value = container.get(fieldMeta.fieldType);
            }

            fieldMeta.field.set(instance, value);
        } catch (CircularDependencyException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject field: " + fieldMeta.field.getName() +
                " in class: " + instance.getClass().getName(), e);
        }
    }
}
