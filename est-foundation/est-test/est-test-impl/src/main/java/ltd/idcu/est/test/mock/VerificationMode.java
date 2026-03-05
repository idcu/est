package ltd.idcu.est.test.mock;

import java.util.Objects;

public final class VerificationMode {

    private final VerificationType type;
    private final int count;

    private VerificationMode(VerificationType type, int count) {
        this.type = type;
        this.count = count;
    }

    public static VerificationMode times(int count) {
        return new VerificationMode(VerificationType.EXACT, count);
    }

    public static VerificationMode atLeast(int minCount) {
        return new VerificationMode(VerificationType.AT_LEAST, minCount);
    }

    public static VerificationMode atMost(int maxCount) {
        return new VerificationMode(VerificationType.AT_MOST, maxCount);
    }

    public static VerificationMode never() {
        return times(0);
    }

    public static VerificationMode once() {
        return times(1);
    }

    void verify(int actualCount, Mockito.Invocation expected) {
        String methodName = expected.getMethod().getName();
        
        switch (type) {
            case EXACT -> {
                if (actualCount != count) {
                    throw new AssertionError(String.format(
                        "Verification failed: %s() expected %d invocation(s) but was %d",
                        methodName, count, actualCount));
                }
            }
            case AT_LEAST -> {
                if (actualCount < count) {
                    throw new AssertionError(String.format(
                        "Verification failed: %s() expected at least %d invocation(s) but was %d",
                        methodName, count, actualCount));
                }
            }
            case AT_MOST -> {
                if (actualCount > count) {
                    throw new AssertionError(String.format(
                        "Verification failed: %s() expected at most %d invocation(s) but was %d",
                        methodName, count, actualCount));
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VerificationMode other)) return false;
        return type == other.type && count == other.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, count);
    }

    private enum VerificationType {
        EXACT,
        AT_LEAST,
        AT_MOST
    }
}
