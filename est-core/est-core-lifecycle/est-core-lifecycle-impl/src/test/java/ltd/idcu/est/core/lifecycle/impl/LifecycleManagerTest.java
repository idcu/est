package ltd.idcu.est.core.lifecycle.impl;

import ltd.idcu.est.core.lifecycle.api.Lifecycle;
import ltd.idcu.est.core.lifecycle.api.LifecycleListener;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class LifecycleManagerTest {

    @Test
    public void testManagerCreation() {
        LifecycleManager manager = new LifecycleManager();
        Assertions.assertNotNull(manager);
        Assertions.assertEquals(0, manager.size());
    }

    @Test
    public void testRegisterAndGet() {
        LifecycleManager manager = new LifecycleManager();
        Lifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        Assertions.assertEquals(1, manager.size());
        
        Lifecycle retrieved = manager.get("test");
        Assertions.assertNotNull(retrieved);
        Assertions.assertSame(lifecycle, retrieved);
    }

    @Test
    public void testRegisterWithNullName() {
        LifecycleManager manager = new LifecycleManager();
        Lifecycle lifecycle = new DefaultLifecycle();
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            manager.register(null, lifecycle);
        });
    }

    @Test
    public void testRegisterWithNullLifecycle() {
        LifecycleManager manager = new LifecycleManager();
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            manager.register("test", null);
        });
    }

    @Test
    public void testUnregister() {
        LifecycleManager manager = new LifecycleManager();
        Lifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        Assertions.assertEquals(1, manager.size());
        
        manager.unregister("test");
        Assertions.assertEquals(0, manager.size());
        Assertions.assertNull(manager.get("test"));
    }

    @Test
    public void testUnregisterNull() {
        LifecycleManager manager = new LifecycleManager();
        Lifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        Assertions.assertEquals(1, manager.size());
        
        manager.unregister(null);
        Assertions.assertEquals(1, manager.size());
    }

    @Test
    public void testGetNonExistent() {
        LifecycleManager manager = new LifecycleManager();
        
        Assertions.assertNull(manager.get("nonexistent"));
    }

    @Test
    public void testGetNull() {
        LifecycleManager manager = new LifecycleManager();
        
        Assertions.assertNull(manager.get(null));
    }

    @Test
    public void testStart() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        Assertions.assertFalse(lifecycle.isRunning());
        
        manager.start("test");
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testStartNonExistent() {
        LifecycleManager manager = new LifecycleManager();
        
        manager.start("nonexistent");
        Assertions.assertEquals(0, manager.size());
    }

    @Test
    public void testStartWhenAlreadyRunning() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
        
        manager.register("test", lifecycle);
        manager.start("test");
        
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testStop() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        lifecycle.start();
        manager.register("test", lifecycle);
        Assertions.assertTrue(lifecycle.isRunning());
        
        manager.stop("test");
        Assertions.assertFalse(lifecycle.isRunning());
    }

    @Test
    public void testStopNonExistent() {
        LifecycleManager manager = new LifecycleManager();
        
        manager.stop("nonexistent");
        Assertions.assertEquals(0, manager.size());
    }

    @Test
    public void testStopWhenNotRunning() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        Assertions.assertFalse(lifecycle.isRunning());
        
        manager.stop("test");
        Assertions.assertFalse(lifecycle.isRunning());
    }

    @Test
    public void testStartAll() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle1 = new DefaultLifecycle();
        DefaultLifecycle lifecycle2 = new DefaultLifecycle();
        
        manager.register("lifecycle1", lifecycle1);
        manager.register("lifecycle2", lifecycle2);
        
        Assertions.assertFalse(lifecycle1.isRunning());
        Assertions.assertFalse(lifecycle2.isRunning());
        
        manager.startAll();
        
        Assertions.assertTrue(lifecycle1.isRunning());
        Assertions.assertTrue(lifecycle2.isRunning());
    }

    @Test
    public void testStopAll() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle1 = new DefaultLifecycle();
        DefaultLifecycle lifecycle2 = new DefaultLifecycle();
        
        lifecycle1.start();
        lifecycle2.start();
        
        manager.register("lifecycle1", lifecycle1);
        manager.register("lifecycle2", lifecycle2);
        
        Assertions.assertTrue(lifecycle1.isRunning());
        Assertions.assertTrue(lifecycle2.isRunning());
        
        manager.stopAll();
        
        Assertions.assertFalse(lifecycle1.isRunning());
        Assertions.assertFalse(lifecycle2.isRunning());
    }

    @Test
    public void testIsRunning() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        Assertions.assertFalse(manager.isRunning("test"));
        
        lifecycle.start();
        Assertions.assertTrue(manager.isRunning("test"));
    }

    @Test
    public void testIsRunningNonExistent() {
        LifecycleManager manager = new LifecycleManager();
        
        Assertions.assertFalse(manager.isRunning("nonexistent"));
    }

    @Test
    public void testAddGlobalListener() {
        LifecycleManager manager = new LifecycleManager();
        TestListener listener = new TestListener();
        DefaultLifecycle lifecycle1 = new DefaultLifecycle();
        DefaultLifecycle lifecycle2 = new DefaultLifecycle();
        
        manager.register("lifecycle1", lifecycle1);
        manager.register("lifecycle2", lifecycle2);
        manager.addGlobalListener(listener);
        
        manager.startAll();
        Assertions.assertEquals(2, listener.startCount.get());
        
        manager.stopAll();
        Assertions.assertEquals(2, listener.stopCount.get());
    }

    @Test
    public void testRemoveGlobalListener() {
        LifecycleManager manager = new LifecycleManager();
        TestListener listener = new TestListener();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        manager.addGlobalListener(listener);
        
        lifecycle.start();
        Assertions.assertEquals(1, listener.startCount.get());
        
        manager.removeGlobalListener(listener);
        
        lifecycle.stop();
        Assertions.assertEquals(0, listener.stopCount.get());
    }

    @Test
    public void testAddNullGlobalListener() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        manager.addGlobalListener(null);
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testRemoveNullGlobalListener() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        manager.register("test", lifecycle);
        manager.removeGlobalListener(null);
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testClear() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle1 = new DefaultLifecycle();
        DefaultLifecycle lifecycle2 = new DefaultLifecycle();
        
        lifecycle1.start();
        lifecycle2.start();
        
        manager.register("lifecycle1", lifecycle1);
        manager.register("lifecycle2", lifecycle2);
        
        Assertions.assertEquals(2, manager.size());
        Assertions.assertTrue(lifecycle1.isRunning());
        Assertions.assertTrue(lifecycle2.isRunning());
        
        manager.clear();
        
        Assertions.assertEquals(0, manager.size());
        Assertions.assertFalse(lifecycle1.isRunning());
        Assertions.assertFalse(lifecycle2.isRunning());
    }

    @Test
    public void testMultipleRegistrations() {
        LifecycleManager manager = new LifecycleManager();
        DefaultLifecycle lifecycle1 = new DefaultLifecycle();
        DefaultLifecycle lifecycle2 = new DefaultLifecycle();
        
        manager.register("test", lifecycle1);
        Assertions.assertSame(lifecycle1, manager.get("test"));
        
        manager.register("test", lifecycle2);
        Assertions.assertSame(lifecycle2, manager.get("test"));
    }

    static class TestListener implements LifecycleListener {
        final AtomicInteger startCount = new AtomicInteger(0);
        final AtomicInteger stopCount = new AtomicInteger(0);

        @Override
        public void onStart() {
            startCount.incrementAndGet();
        }

        @Override
        public void onStop() {
            stopCount.incrementAndGet();
        }
    }
}
