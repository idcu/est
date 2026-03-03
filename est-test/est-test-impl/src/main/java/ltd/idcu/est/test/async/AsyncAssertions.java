package ltd.idcu.est.test.async;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class AsyncAssertions {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(1);
    private static final Duration DEFAULT_POLL_INTERVAL = Duration.ofMillis(100);

    private AsyncAssertions() {
    }

    public static void await() {
        await(DEFAULT_TIMEOUT);
    }

    public static AwaitBuilder await(Duration timeout) {
        return new AwaitBuilder(timeout);
    }

    public static void until(BooleanSupplier condition) {
        await().until(condition);
    }

    public static void until(BooleanSupplier condition, Duration timeout) {
        await(timeout).until(condition);
    }

    public static void untilAsserted(Runnable assertion) {
        await().untilAsserted(assertion);
    }

    public static void untilAsserted(Runnable assertion, Duration timeout) {
        await(timeout).untilAsserted(assertion);
    }

    public static <T> void untilEquals(Supplier<T> actual, T expected) {
        await().untilEquals(actual, expected);
    }

    public static <T> void untilEquals(Supplier<T> actual, T expected, Duration timeout) {
        await(timeout).untilEquals(actual, expected);
    }

    public static <T> void untilNotNull(Supplier<T> supplier) {
        await().untilNotNull(supplier);
    }

    public static <T> void untilNotNull(Supplier<T> supplier, Duration timeout) {
        await(timeout).untilNotNull(supplier);
    }

    public static void atMost(Duration timeout) {
        await(timeout);
    }

    public static void pollInterval(Duration interval) {
        await(DEFAULT_TIMEOUT).pollInterval(interval);
    }

    public static class AwaitBuilder {
        private final Duration timeout;
        private Duration pollInterval = DEFAULT_POLL_INTERVAL;
        private String message;

        public AwaitBuilder(Duration timeout) {
            this.timeout = timeout;
        }

        public AwaitBuilder pollInterval(Duration interval) {
            this.pollInterval = interval;
            return this;
        }

        public AwaitBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public AwaitBuilder withMessage(Supplier<String> messageSupplier) {
            this.message = messageSupplier.get();
            return this;
        }

        public void until(BooleanSupplier condition) {
            long startTime = System.currentTimeMillis();
            long timeoutMillis = timeout.toMillis();
            long pollMillis = pollInterval.toMillis();

            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                if (condition.getAsBoolean()) {
                    return;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pollMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError("Thread was interrupted while waiting", e);
                }
            }

            if (condition.getAsBoolean()) {
                return;
            }

            String errorMessage = message != null ? message : 
                "Condition was not satisfied within " + timeoutMillis + "ms";
            throw new AssertionError(errorMessage);
        }

        public void untilAsserted(Runnable assertion) {
            long startTime = System.currentTimeMillis();
            long timeoutMillis = timeout.toMillis();
            long pollMillis = pollInterval.toMillis();
            Throwable lastException = null;

            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                try {
                    assertion.run();
                    return;
                } catch (AssertionError | Exception e) {
                    lastException = e;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pollMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError("Thread was interrupted while waiting", e);
                }
            }

            try {
                assertion.run();
                return;
            } catch (AssertionError | Exception e) {
                lastException = e;
            }

            String errorMessage = message != null ? message : 
                "Assertion was not satisfied within " + timeoutMillis + "ms";
            throw new AssertionError(errorMessage, lastException);
        }

        public <T> void untilEquals(Supplier<T> actual, T expected) {
            until(() -> {
                T value = actual.get();
                return value != null && value.equals(expected);
            });
        }

        public <T> void untilNotNull(Supplier<T> supplier) {
            until(() -> supplier.get() != null);
        }

        public <T> void untilNull(Supplier<T> supplier) {
            until(() -> supplier.get() == null);
        }

        public <T> void untilTrue(Supplier<Boolean> supplier) {
            until(supplier::get);
        }

        public <T> void untilFalse(Supplier<Boolean> supplier) {
            until(() -> !supplier.get());
        }
    }
}
