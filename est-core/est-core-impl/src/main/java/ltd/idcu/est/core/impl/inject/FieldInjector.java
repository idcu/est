package ltd.idcu.est.core.impl.inject;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;

import java.lang.reflect.Field;

public class FieldInjector {

    private final Container container;

    public FieldInjector(Container container) {
        this.container = container;
    }

    public void injectFields(Object instance) {
        Class<?> clazz = instance.getClass();
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    injectField(instance, field);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private void injectField(Object instance, Field field) {
        try {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            String qualifier = null;

            if (field.isAnnotationPresent(Qualifier.class)) {
                qualifier = field.getAnnotation(Qualifier.class).value();
            }

            Object value;
            if (qualifier != null) {
                value = container.get(fieldType, qualifier);
            } else {
                value = container.get(fieldType);
            }

            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject field: " + field.getName() + 
                " in class: " + instance.getClass().getName(), e);
        }
    }
}
