package ltd.idcu.est.core.module.impl;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class AbstractModuleTest {

    @Test
    public void testModuleCreation() {
        TestModule module = new TestModule("test-module", "1.0.0");
        Assertions.assertNotNull(module);
        Assertions.assertEquals("test-module", module.getName());
        Assertions.assertEquals("1.0.0", module.getVersion());
        Assertions.assertFalse(module.isInitialized());
        Assertions.assertFalse(module.isRunning());
    }

    @Test
    public void testInitialize() {
        TestModule module = new TestModule("test-module", "1.0.0");
        Assertions.assertFalse(module.isInitialized());
        
        module.initialize();
        Assertions.assertTrue(module.isInitialized());
        Assertions.assertTrue(module.doInitializeCalled.get());
    }

    @Test
    public void testInitializeWhenAlreadyInitialized() {
        TestModule module = new TestModule("test-module", "1.0.0");
        module.initialize();
        Assertions.assertTrue(module.isInitialized());
        
        module.doInitializeCalled.set(false);
        
        module.initialize();
        Assertions.assertTrue(module.isInitialized());
        Assertions.assertFalse(module.doInitializeCalled.get());
    }

    @Test
    public void testStart() {
        TestModule module = new TestModule("test-module", "1.0.0");
        Assertions.assertFalse(module.isInitialized());
        Assertions.assertFalse(module.isRunning());
        
        module.start();
        Assertions.assertTrue(module.isInitialized());
        Assertions.assertTrue(module.isRunning());
        Assertions.assertTrue(module.doInitializeCalled.get());
        Assertions.assertTrue(module.doStartCalled.get());
    }

    @Test
    public void testStartWhenAlreadyRunning() {
        TestModule module = new TestModule("test-module", "1.0.0");
        module.start();
        Assertions.assertTrue(module.isRunning());
        
        module.doStartCalled.set(false);
        
        module.start();
        Assertions.assertTrue(module.isRunning());
        Assertions.assertFalse(module.doStartCalled.get());
    }

    @Test
    public void testStartAfterInitialize() {
        TestModule module = new TestModule("test-module", "1.0.0");
        module.initialize();
        Assertions.assertTrue(module.isInitialized());
        Assertions.assertFalse(module.isRunning());
        
        module.start();
        Assertions.assertTrue(module.isRunning());
        Assertions.assertTrue(module.doStartCalled.get());
    }

    @Test
    public void testStop() {
        TestModule module = new TestModule("test-module", "1.0.0");
        module.start();
        Assertions.assertTrue(module.isRunning());
        
        module.stop();
        Assertions.assertFalse(module.isRunning());
        Assertions.assertTrue(module.doStopCalled.get());
    }

    @Test
    public void testStopWhenNotRunning() {
        TestModule module = new TestModule("test-module", "1.0.0");
        Assertions.assertFalse(module.isRunning());
        
        module.stop();
        Assertions.assertFalse(module.isRunning());
        Assertions.assertFalse(module.doStopCalled.get());
    }

    @Test
    public void testLifecycleCycle() {
        TestModule module = new TestModule("test-module", "1.0.0");
        
        module.initialize();
        Assertions.assertTrue(module.isInitialized());
        Assertions.assertFalse(module.isRunning());
        
        module.start();
        Assertions.assertTrue(module.isRunning());
        
        module.stop();
        Assertions.assertFalse(module.isRunning());
        
        module.start();
        Assertions.assertTrue(module.isRunning());
        
        module.stop();
        Assertions.assertFalse(module.isRunning());
    }

    static class TestModule extends AbstractModule {
        final AtomicBoolean doInitializeCalled = new AtomicBoolean(false);
        final AtomicBoolean doStartCalled = new AtomicBoolean(false);
        final AtomicBoolean doStopCalled = new AtomicBoolean(false);

        TestModule(String name, String version) {
            super(name, version);
        }

        @Override
        protected void doInitialize() {
            doInitializeCalled.set(true);
        }

        @Override
        protected void doStart() {
            doStartCalled.set(true);
        }

        @Override
        protected void doStop() {
            doStopCalled.set(true);
        }
    }
}
