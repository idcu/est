package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class StructuralPatternsTest {

    @Test
    public void testProxyDirectTarget() {
        String target = "test target";
        DefaultProxy<String> proxy = DefaultProxy.of(target);
        
        Assertions.assertTrue(proxy.isInitialized());
        Assertions.assertEquals(target, proxy.getTarget());
    }

    @Test
    public void testProxyLazyInitialization() {
        AtomicInteger createCount = new AtomicInteger(0);
        DefaultProxy<String> proxy = DefaultProxy.lazy(() -> {
            createCount.incrementAndGet();
            return "lazy target";
        });
        
        Assertions.assertFalse(proxy.isInitialized());
        Assertions.assertEquals(0, createCount.get());
        
        String target = proxy.getTarget();
        Assertions.assertTrue(proxy.isInitialized());
        Assertions.assertEquals(1, createCount.get());
        Assertions.assertEquals("lazy target", target);
    }

    @Test
    public void testProxyInitializeOnce() {
        AtomicInteger createCount = new AtomicInteger(0);
        DefaultProxy<String> proxy = DefaultProxy.lazy(() -> {
            createCount.incrementAndGet();
            return "test";
        });
        
        proxy.getTarget();
        proxy.getTarget();
        proxy.getTarget();
        
        Assertions.assertEquals(1, createCount.get());
    }

    @Test
    public void testProxyNullDirectTarget() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultProxy<>((String) null);
        });
    }

    @Test
    public void testProxyNullSupplier() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultProxy<>((java.util.function.Supplier<String>) null);
        });
    }

    @Test
    public void testProxySetTarget() {
        DefaultProxy<String> proxy = DefaultProxy.of("initial");
        Assertions.assertEquals("initial", proxy.getTarget());
        
        proxy.setTarget("updated");
        Assertions.assertTrue(proxy.isInitialized());
        Assertions.assertEquals("updated", proxy.getTarget());
        
        proxy.setTarget(null);
        Assertions.assertFalse(proxy.isInitialized());
        Assertions.assertNull(proxy.getTarget());
    }

    @Test
    public void testProxyLazyThreadSafe() throws InterruptedException {
        AtomicInteger createCount = new AtomicInteger(0);
        DefaultProxy<String> proxy = DefaultProxy.lazy(() -> {
            createCount.incrementAndGet();
            return "thread-safe";
        });
        
        int threadCount = 100;
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threadCount);
        
        String[] results = new String[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                results[index] = proxy.getTarget();
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        
        Assertions.assertEquals(1, createCount.get());
        for (String result : results) {
            Assertions.assertEquals("thread-safe", result);
        }
    }

    @Test
    public void testAdapter() {
        DefaultAdapter<String, Integer> adapter = DefaultAdapter.of(
            String.class,
            Integer.class,
            Integer::parseInt
        );
        
        Integer adapted = adapter.adapt("123");
        Assertions.assertEquals(123, adapted);
        Assertions.assertEquals(String.class, adapter.getSourceType());
        Assertions.assertEquals(Integer.class, adapter.getTargetType());
    }

    @Test
    public void testDecorator() {
        AtomicInteger callCount = new AtomicInteger(0);
        
        class TestDecorator extends AbstractDecorator<Runnable> {
            public void execute() {
                callCount.incrementAndGet();
                decorated.run();
            }
        }
        
        TestDecorator decorator = new TestDecorator();
        AtomicInteger componentCallCount = new AtomicInteger(0);
        Runnable component = componentCallCount::incrementAndGet;
        decorator.setDecorated(component);
        
        decorator.execute();
        
        Assertions.assertEquals(1, callCount.get());
        Assertions.assertEquals(1, componentCallCount.get());
        Assertions.assertEquals(component, decorator.getDecorated());
    }
}
