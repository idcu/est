package ltd.idcu.est.test.async;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncAssertionsTest {

    @Test
    public void testUntilWithImmediateCondition() {
        AtomicInteger counter = new AtomicInteger(5);
        AsyncAssertions.until(() -> counter.get() > 0);
    }

    @Test
    public void testUntilWithDelayedCondition() {
        AtomicInteger counter = new AtomicInteger(0);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                counter.set(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.until(() -> counter.get() >= 10, Duration.ofMillis(200));
        Assertions.assertEquals(10, counter.get());
    }

    @Test
    public void testUntilAsserted() {
        AtomicInteger counter = new AtomicInteger(0);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                counter.set(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.untilAsserted(() -> {
            Assertions.assertEquals(5, counter.get());
        }, Duration.ofMillis(200));
    }

    @Test
    public void testUntilEquals() {
        AtomicReference<String> value = new AtomicReference<>("initial");
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                value.set("final");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.untilEquals(value::get, "final", Duration.ofMillis(200));
        Assertions.assertEquals("final", value.get());
    }

    @Test
    public void testUntilNotNull() {
        AtomicReference<String> value = new AtomicReference<>(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                value.set("not null");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.untilNotNull(value::get, Duration.ofMillis(200));
        Assertions.assertNotNull(value.get());
    }

    @Test
    public void testAwaitBuilder() {
        AtomicInteger counter = new AtomicInteger(0);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                counter.set(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.await()
            .pollInterval(Duration.ofMillis(10))
            .withMessage("Condition not met")
            .until(() -> counter.get() == 100);
        
        Assertions.assertEquals(100, counter.get());
    }

    @Test
    public void testAtMost() {
        AtomicInteger counter = new AtomicInteger(0);
        counter.set(1);
        
        AsyncAssertions.atMost(Duration.ofMillis(100))
            .until(() -> counter.get() > 0);
    }

    @Test
    public void testAwaitUntilTrue() {
        AtomicBoolean flag = new AtomicBoolean(false);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                flag.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.await(Duration.ofMillis(200))
            .untilTrue(flag::get);
        
        Assertions.assertTrue(flag.get());
    }

    @Test
    public void testAwaitUntilFalse() {
        AtomicBoolean flag = new AtomicBoolean(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                flag.set(false);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.await(Duration.ofMillis(200))
            .untilFalse(flag::get);
        
        Assertions.assertFalse(flag.get());
    }

    @Test
    public void testAwaitUntilNull() {
        AtomicReference<String> value = new AtomicReference<>("temp");
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                value.set(null);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.await(Duration.ofMillis(200))
            .untilNull(value::get);
        
        Assertions.assertNull(value.get());
    }

    @Test
    public void testPollInterval() {
        AtomicInteger counter = new AtomicInteger(0);
        
        new Thread(() -> {
            try {
                Thread.sleep(50);
                counter.set(42);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        AsyncAssertions.pollInterval(Duration.ofMillis(20))
            .until(() -> counter.get() == 42);
        
        Assertions.assertEquals(42, counter.get());
    }

    @Test
    public void testUntilAssertedWithSuccess() {
        AtomicInteger counter = new AtomicInteger(10);
        
        AsyncAssertions.untilAsserted(() -> {
            Assertions.assertTrue(counter.get() > 0);
        });
    }

    @Test
    public void testTimeoutThrowsAssertionError() {
        AtomicInteger counter = new AtomicInteger(0);
        
        Assertions.assertThrows(AssertionError.class, () -> {
            AsyncAssertions.until(() -> counter.get() > 100, Duration.ofMillis(50));
        });
    }
}
