package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultProxyTest {

    @Test
    public void testLazyInitialization() {
        AtomicInteger counter = new AtomicInteger(0);
        DefaultProxy<String> proxy = DefaultProxy.lazy(() -> {
            counter.incrementAndGet();
            return "initialized";
        });
        
        Assertions.assertFalse(proxy.isInitialized());
        Assertions.assertEquals(0, counter.get());
        
        String target = proxy.getTarget();
        
        Assertions.assertEquals("initialized", target);
        Assertions.assertTrue(proxy.isInitialized());
        Assertions.assertEquals(1, counter.get());
        
        String target2 = proxy.getTarget();
        Assertions.assertSame(target, target2);
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testEagerInitialization() {
        DefaultProxy<String> proxy = DefaultProxy.of("eager");
        
        Assertions.assertTrue(proxy.isInitialized());
        Assertions.assertEquals("eager", proxy.getTarget());
    }

    @Test
    public void testSetTarget() {
        DefaultProxy<String> proxy = DefaultProxy.of("initial");
        
        Assertions.assertTrue(proxy.isInitialized());
        Assertions.assertEquals("initial", proxy.getTarget());
        
        proxy.setTarget("new");
        
        Assertions.assertEquals("new", proxy.getTarget());
    }

    @Test
    public void testLazyFactoryMethod() {
        DefaultProxy<String> proxy = DefaultProxy.lazy(() -> "lazy");
        Assertions.assertNotNull(proxy);
        Assertions.assertEquals("lazy", proxy.getTarget());
    }

    @Test
    public void testOfFactoryMethod() {
        DefaultProxy<String> proxy = DefaultProxy.of("eager");
        Assertions.assertNotNull(proxy);
        Assertions.assertTrue(proxy.isInitialized());
    }

    @Test
    public void testNullSupplierThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultProxy<>((java.util.function.Supplier<String>) null);
        });
    }

    @Test
    public void testNullTargetThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultProxy<>((String) null);
        });
    }
}
