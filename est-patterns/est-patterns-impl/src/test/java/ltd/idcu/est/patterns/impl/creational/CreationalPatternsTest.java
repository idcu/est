package ltd.idcu.est.patterns.impl.creational;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CreationalPatternsTest {

    @Test
    public void testSingletonSameInstance() {
        AtomicInteger counter = new AtomicInteger(0);
        DefaultSingleton<Integer> singleton = DefaultSingleton.of(counter::incrementAndGet);
        
        Integer instance1 = singleton.getInstance();
        Integer instance2 = singleton.getInstance();
        
        Assertions.assertEquals(1, instance1);
        Assertions.assertEquals(instance1, instance2);
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testSingletonNullSupplier() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultSingleton<>(null);
        });
    }

    @Test
    public void testSingletonThreadSafe() throws InterruptedException {
        AtomicInteger createCount = new AtomicInteger(0);
        DefaultSingleton<String> singleton = DefaultSingleton.of(() -> {
            createCount.incrementAndGet();
            return "test";
        });
        
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        String[] results = new String[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                results[index] = singleton.getInstance();
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        
        Assertions.assertEquals(1, createCount.get());
        for (String result : results) {
            Assertions.assertEquals("test", result);
        }
    }

    @Test
    public void testFactoryCreate() {
        AtomicInteger counter = new AtomicInteger(0);
        DefaultFactory<Integer> factory = DefaultFactory.of("counter", counter::incrementAndGet);
        
        Integer instance1 = factory.create();
        Integer instance2 = factory.create();
        
        Assertions.assertEquals(1, instance1);
        Assertions.assertEquals(2, instance2);
        Assertions.assertEquals("counter", factory.getType());
    }

    @Test
    public void testFactoryNullType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultFactory<>(null, () -> "test");
        });
    }

    @Test
    public void testFactoryEmptyType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultFactory<>("", () -> "test");
        });
    }

    @Test
    public void testFactoryNullSupplier() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultFactory<>("test", null);
        });
    }
}
