package ltd.idcu.est.test.mock;

import ltd.idcu.est.test.annotation.Mock;

import java.lang.reflect.Field;

public final class MockInjector {

    private MockInjector() {
    }

    public static void injectMocks(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        
        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Mock.class)) {
                Object mock = Mockito.mock(field.getType());
                field.setAccessible(true);
                try {
                    field.set(testInstance, mock);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject mock for field: " + field.getName(), e);
                }
            }
        }
    }

    public static void resetMocks(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        
        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Mock.class)) {
                field.setAccessible(true);
                try {
                    field.set(testInstance, null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to reset mock for field: " + field.getName(), e);
                }
            }
        }
    }
}
