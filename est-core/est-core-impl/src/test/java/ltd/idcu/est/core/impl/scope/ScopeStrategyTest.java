package ltd.idcu.est.core.impl.scope;

import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ScopeStrategyTest {

    @Test
    public void testSingletonScope() {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        
        Object instance1 = strategy.get(Scope.SINGLETON, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        Object instance2 = strategy.get(Scope.SINGLETON, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        
        Assertions.assertSame(instance1, instance2);
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testSingletonScopeWithQualifier() {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        
        Object instance1 = strategy.get(Scope.SINGLETON, Object.class, "qual1", 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        Object instance2 = strategy.get(Scope.SINGLETON, Object.class, "qual2", 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        
        Assertions.assertNotSame(instance1, instance2);
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testPrototypeScope() {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        
        Object instance1 = strategy.get(Scope.PROTOTYPE, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        Object instance2 = strategy.get(Scope.PROTOTYPE, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        
        Assertions.assertNotSame(instance1, instance2);
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testRequestScope() {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        
        Object instance1 = strategy.get(Scope.REQUEST, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        Object instance2 = strategy.get(Scope.REQUEST, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        
        Assertions.assertNotSame(instance1, instance2);
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testSessionScope() {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        
        Object instance1 = strategy.get(Scope.SESSION, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        Object instance2 = strategy.get(Scope.SESSION, Object.class, null, 
            () -> { counter.incrementAndGet(); return new Object(); }, instances);
        
        Assertions.assertNotSame(instance1, instance2);
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testUnknownScope() {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        
        Assertions.assertThrows(NullPointerException.class, () -> {
            strategy.get(null, Object.class, null, Object::new, instances);
        });
    }

    @Test
    public void testSingletonThreadSafety() throws InterruptedException {
        ScopeStrategy strategy = new ScopeStrategy();
        Map<String, Object> instances = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        
        Object[] results = new Object[10];
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = strategy.get(Scope.SINGLETON, Object.class, null, 
                    () -> { counter.incrementAndGet(); return new Object(); }, instances);
            });
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        for (int i = 1; i < 10; i++) {
            Assertions.assertSame(results[0], results[i]);
        }
        Assertions.assertEquals(1, counter.get());
    }
}
