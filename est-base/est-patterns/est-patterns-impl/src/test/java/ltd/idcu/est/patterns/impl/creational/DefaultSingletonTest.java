package ltd.idcu.est.patterns.impl.creational;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultSingletonTest {

    @Test
    public void testSingleton() {
        AtomicInteger counter = new AtomicInteger(0);
        DefaultSingleton<String> singleton = DefaultSingleton.of(() -> {
            counter.incrementAndGet();
            return "test";
        });
        
        String instance1 = singleton.getInstance();
        String instance2 = singleton.getInstance();
        
        Assertions.assertEquals("test", instance1);
        Assertions.assertSame(instance1, instance2);
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testOfFactoryMethod() {
        DefaultSingleton<String> singleton = DefaultSingleton.of(() -> "test");
        Assertions.assertNotNull(singleton);
        Assertions.assertEquals("test", singleton.getInstance());
    }

    @Test
    public void testNullSupplierThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultSingleton<>(null);
        });
    }
}
