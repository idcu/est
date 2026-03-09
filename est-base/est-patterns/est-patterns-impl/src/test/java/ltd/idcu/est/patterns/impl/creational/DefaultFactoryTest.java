package ltd.idcu.est.patterns.impl.creational;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultFactoryTest {

    @Test
    public void testCreate() {
        AtomicInteger counter = new AtomicInteger(0);
        DefaultFactory<String> factory = new DefaultFactory<>("string", () -> {
            counter.incrementAndGet();
            return new String("test");
        });
        
        String result1 = factory.create();
        String result2 = factory.create();
        
        Assertions.assertEquals("test", result1);
        Assertions.assertEquals("test", result2);
        Assertions.assertNotSame(result1, result2);
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testGetType() {
        DefaultFactory<String> factory = DefaultFactory.of("string", () -> "test");
        Assertions.assertEquals("string", factory.getType());
    }

    @Test
    public void testOfFactoryMethod() {
        DefaultFactory<String> factory = DefaultFactory.of("string", () -> "test");
        Assertions.assertNotNull(factory);
        Assertions.assertEquals("string", factory.getType());
        Assertions.assertEquals("test", factory.create());
    }

    @Test
    public void testNullTypeThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultFactory<>(null, () -> "test");
        });
    }

    @Test
    public void testEmptyTypeThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultFactory<>("", () -> "test");
        });
    }

    @Test
    public void testNullSupplierThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultFactory<>("string", null);
        });
    }
}
