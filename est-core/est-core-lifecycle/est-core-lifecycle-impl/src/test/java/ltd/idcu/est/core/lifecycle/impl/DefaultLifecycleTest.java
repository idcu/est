package ltd.idcu.est.core.lifecycle.impl;

import ltd.idcu.est.core.lifecycle.api.Lifecycle;
import ltd.idcu.est.core.lifecycle.api.LifecycleListener;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultLifecycleTest {

    @Test
    public void testLifecycleCreation() {
        Lifecycle lifecycle = new DefaultLifecycle();
        Assertions.assertNotNull(lifecycle);
        Assertions.assertFalse(lifecycle.isRunning());
    }

    @Test
    public void testStart() {
        Lifecycle lifecycle = new DefaultLifecycle();
        Assertions.assertFalse(lifecycle.isRunning());
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testStartWhenAlreadyRunning() {
        Lifecycle lifecycle = new DefaultLifecycle();
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testStop() {
        Lifecycle lifecycle = new DefaultLifecycle();
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
        
        lifecycle.stop();
        Assertions.assertFalse(lifecycle.isRunning());
    }

    @Test
    public void testStopWhenNotRunning() {
        Lifecycle lifecycle = new DefaultLifecycle();
        Assertions.assertFalse(lifecycle.isRunning());
        
        lifecycle.stop();
        Assertions.assertFalse(lifecycle.isRunning());
    }

    @Test
    public void testAddListener() {
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        TestListener listener = new TestListener();
        
        lifecycle.addListener(listener);
        Assertions.assertEquals(0, listener.startCount.get());
        Assertions.assertEquals(0, listener.stopCount.get());
        
        lifecycle.start();
        Assertions.assertEquals(1, listener.startCount.get());
        Assertions.assertEquals(0, listener.stopCount.get());
    }

    @Test
    public void testRemoveListener() {
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        TestListener listener = new TestListener();
        
        lifecycle.addListener(listener);
        lifecycle.start();
        Assertions.assertEquals(1, listener.startCount.get());
        
        lifecycle.removeListener(listener);
        lifecycle.stop();
        Assertions.assertEquals(0, listener.stopCount.get());
    }

    @Test
    public void testAddNullListener() {
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        lifecycle.addListener(null);
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testRemoveNullListener() {
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        
        lifecycle.removeListener(null);
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
    }

    @Test
    public void testMultipleListeners() {
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        
        lifecycle.addListener(listener1);
        lifecycle.addListener(listener2);
        
        lifecycle.start();
        Assertions.assertEquals(1, listener1.startCount.get());
        Assertions.assertEquals(1, listener2.startCount.get());
        
        lifecycle.stop();
        Assertions.assertEquals(1, listener1.stopCount.get());
        Assertions.assertEquals(1, listener2.stopCount.get());
    }

    @Test
    public void testListenerNotificationOrder() {
        DefaultLifecycle lifecycle = new DefaultLifecycle();
        StringBuilder order = new StringBuilder();
        
        lifecycle.addListener(new LifecycleListener() {
            @Override
            public void onStart() {
                order.append("1");
            }

            @Override
            public void onStop() {
                order.append("A");
            }
        });
        
        lifecycle.addListener(new LifecycleListener() {
            @Override
            public void onStart() {
                order.append("2");
            }

            @Override
            public void onStop() {
                order.append("B");
            }
        });
        
        lifecycle.start();
        lifecycle.stop();
        
        Assertions.assertEquals("12AB", order.toString());
    }

    @Test
    public void testStartStopCycle() {
        Lifecycle lifecycle = new DefaultLifecycle();
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
        
        lifecycle.stop();
        Assertions.assertFalse(lifecycle.isRunning());
        
        lifecycle.start();
        Assertions.assertTrue(lifecycle.isRunning());
        
        lifecycle.stop();
        Assertions.assertFalse(lifecycle.isRunning());
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
