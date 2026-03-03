package ltd.idcu.est.test;

import ltd.idcu.est.test.api.Executable;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class Assertions {

    private Assertions() {
    }

    public static void assertTimeout(Duration timeout, Executable executable) {
        assertTimeout(timeout, executable, (String) null);
    }

    public static void assertTimeout(Duration timeout, Executable executable, String message) {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(executable, "executable must not be null");
        
        long timeoutMillis = timeout.toMillis();
        long startTime = System.currentTimeMillis();
        
        try {
            executable.execute();
        } catch (Throwable t) {
            throw new AssertionError(message != null ? message : 
                "Unexpected exception thrown: " + t.getClass().getSimpleName() + ": " + t.getMessage(), t);
        }
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > timeoutMillis) {
            throw new AssertionError(message != null ? message : 
                "Execution took " + elapsedTime + " ms, expected timeout was " + timeoutMillis + " ms");
        }
    }

    public static void assertTimeout(Duration timeout, Executable executable, Supplier<String> messageSupplier) {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(executable, "executable must not be null");
        
        long timeoutMillis = timeout.toMillis();
        long startTime = System.currentTimeMillis();
        
        try {
            executable.execute();
        } catch (Throwable t) {
            throw new AssertionError(nullSafeGet(messageSupplier), t);
        }
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > timeoutMillis) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
    }

    public static <T> T assertTimeout(Duration timeout, Callable<T> callable) {
        return assertTimeout(timeout, callable, (String) null);
    }

    public static <T> T assertTimeout(Duration timeout, Callable<T> callable, String message) {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(callable, "callable must not be null");
        
        long timeoutMillis = timeout.toMillis();
        long startTime = System.currentTimeMillis();
        
        T result;
        try {
            result = callable.call();
        } catch (Throwable t) {
            throw new AssertionError(message != null ? message : 
                "Unexpected exception thrown: " + t.getClass().getSimpleName() + ": " + t.getMessage(), t);
        }
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > timeoutMillis) {
            throw new AssertionError(message != null ? message : 
                "Execution took " + elapsedTime + " ms, expected timeout was " + timeoutMillis + " ms");
        }
        
        return result;
    }

    public static <T> T assertTimeout(Duration timeout, Callable<T> callable, Supplier<String> messageSupplier) {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(callable, "callable must not be null");
        
        long timeoutMillis = timeout.toMillis();
        long startTime = System.currentTimeMillis();
        
        T result;
        try {
            result = callable.call();
        } catch (Throwable t) {
            throw new AssertionError(nullSafeGet(messageSupplier), t);
        }
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > timeoutMillis) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
        
        return result;
    }

    public static void assertTimeoutPreemptively(Duration timeout, Executable executable) {
        assertTimeoutPreemptively(timeout, executable, (String) null);
    }

    public static void assertTimeoutPreemptively(Duration timeout, Executable executable, String message) {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(executable, "executable must not be null");
        
        Thread thread = Thread.currentThread();
        ThreadGroup threadGroup = thread.getThreadGroup();
        
        final Throwable[] exception = new Throwable[1];
        final boolean[] completed = {false};
        
        Thread executionThread = new Thread(threadGroup, () -> {
            try {
                executable.execute();
                completed[0] = true;
            } catch (Throwable t) {
                exception[0] = t;
            }
        }, "assertTimeoutPreemptively-execution");
        
        executionThread.start();
        
        try {
            executionThread.join(timeout.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(message != null ? message : "Thread was interrupted while waiting for execution");
        }
        
        if (executionThread.isAlive()) {
            executionThread.interrupt();
            throw new AssertionError(message != null ? message : 
                "Execution timed out after " + timeout.toMillis() + " ms");
        }
        
        if (exception[0] != null) {
            throw new AssertionError(message != null ? message : 
                "Unexpected exception thrown: " + exception[0].getClass().getSimpleName() + ": " + exception[0].getMessage(), 
                exception[0]);
        }
    }

    public static void assertTimeoutPreemptively(Duration timeout, Executable executable, Supplier<String> messageSupplier) {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(executable, "executable must not be null");
        
        Thread thread = Thread.currentThread();
        ThreadGroup threadGroup = thread.getThreadGroup();
        
        final Throwable[] exception = new Throwable[1];
        final boolean[] completed = {false};
        
        Thread executionThread = new Thread(threadGroup, () -> {
            try {
                executable.execute();
                completed[0] = true;
            } catch (Throwable t) {
                exception[0] = t;
            }
        }, "assertTimeoutPreemptively-execution");
        
        executionThread.start();
        
        try {
            executionThread.join(timeout.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
        
        if (executionThread.isAlive()) {
            executionThread.interrupt();
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
        
        if (exception[0] != null) {
            throw new AssertionError(nullSafeGet(messageSupplier), exception[0]);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, (String) null);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message != null ? message : "Expected true but was false");
        }
    }

    public static void assertTrue(boolean condition, Supplier<String> messageSupplier) {
        if (!condition) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, (String) null);
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message != null ? message : "Expected false but was true");
        }
    }

    public static void assertFalse(boolean condition, Supplier<String> messageSupplier) {
        if (condition) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
    }

    public static void assertNull(Object actual) {
        assertNull(actual, (String) null);
    }

    public static void assertNull(Object actual, String message) {
        if (actual != null) {
            throw new AssertionError(message != null ? message : "Expected null but was: " + actual);
        }
    }

    public static void assertNull(Object actual, Supplier<String> messageSupplier) {
        if (actual != null) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
    }

    public static void assertNotNull(Object actual) {
        assertNotNull(actual, (String) null);
    }

    public static void assertNotNull(Object actual, String message) {
        if (actual == null) {
            throw new AssertionError(message != null ? message : "Expected non-null but was null");
        }
    }

    public static void assertNotNull(Object actual, Supplier<String> messageSupplier) {
        if (actual == null) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(message != null ? message : 
                "Expected: " + expected + " but was: " + actual);
        }
    }

    public static void assertEquals(Object expected, Object actual, Supplier<String> messageSupplier) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
    }

    public static void assertNotEquals(Object unexpected, Object actual) {
        assertNotEquals(unexpected, actual, (String) null);
    }

    public static void assertNotEquals(Object unexpected, Object actual, String message) {
        if (Objects.equals(unexpected, actual)) {
            throw new AssertionError(message != null ? message : 
                "Expected not equal but was: " + actual);
        }
    }

    public static void assertSame(Object expected, Object actual) {
        assertSame(expected, actual, (String) null);
    }

    public static void assertSame(Object expected, Object actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message != null ? message : 
                "Expected same: " + expected + " but was: " + actual);
        }
    }

    public static void assertNotSame(Object unexpected, Object actual) {
        assertNotSame(unexpected, actual, (String) null);
    }

    public static void assertNotSame(Object unexpected, Object actual, String message) {
        if (unexpected == actual) {
            throw new AssertionError(message != null ? message : 
                "Expected not same but was: " + actual);
        }
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        assertArrayEquals(expected, actual, (String) null);
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message != null ? message : 
                "Expected array: " + Arrays.toString(expected) + " but was: " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(int[] expected, int[] actual) {
        assertArrayEquals(expected, actual, (String) null);
    }

    public static void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message != null ? message : 
                "Expected array: " + Arrays.toString(expected) + " but was: " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(long[] expected, long[] actual) {
        assertArrayEquals(expected, actual, (String) null);
    }

    public static void assertArrayEquals(long[] expected, long[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message != null ? message : 
                "Expected array: " + Arrays.toString(expected) + " but was: " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(double[] expected, double[] actual) {
        assertArrayEquals(expected, actual, (String) null);
    }

    public static void assertArrayEquals(double[] expected, double[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message != null ? message : 
                "Expected array: " + Arrays.toString(expected) + " but was: " + Arrays.toString(actual));
        }
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
        return assertThrows(expectedType, executable, (String) null);
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, String message) {
        Objects.requireNonNull(expectedType, "expectedType must not be null");
        Objects.requireNonNull(executable, "executable must not be null");
        
        try {
            executable.execute();
        } catch (Throwable t) {
            if (expectedType.isInstance(t)) {
                return expectedType.cast(t);
            }
            throw new AssertionError(message != null ? message : 
                "Expected " + expectedType.getSimpleName() + " but was " + t.getClass().getSimpleName());
        }
        throw new AssertionError(message != null ? message : 
            "Expected " + expectedType.getSimpleName() + " but no exception was thrown");
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, Supplier<String> messageSupplier) {
        Objects.requireNonNull(expectedType, "expectedType must not be null");
        Objects.requireNonNull(executable, "executable must not be null");
        
        try {
            executable.execute();
        } catch (Throwable t) {
            if (expectedType.isInstance(t)) {
                return expectedType.cast(t);
            }
            throw new AssertionError(nullSafeGet(messageSupplier));
        }
        throw new AssertionError(nullSafeGet(messageSupplier));
    }

    public static void assertDoesNotThrow(Executable executable) {
        assertDoesNotThrow(executable, (String) null);
    }

    public static void assertDoesNotThrow(Executable executable, String message) {
        Objects.requireNonNull(executable, "executable must not be null");
        
        try {
            executable.execute();
        } catch (Throwable t) {
            throw new AssertionError(message != null ? message : 
                "Unexpected exception thrown: " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    public static void fail() {
        fail((String) null);
    }

    public static void fail(String message) {
        throw new AssertionError(message != null ? message : "Test failed");
    }

    public static void fail(Supplier<String> messageSupplier) {
        throw new AssertionError(nullSafeGet(messageSupplier));
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return messageSupplier != null ? messageSupplier.get() : null;
    }
}
