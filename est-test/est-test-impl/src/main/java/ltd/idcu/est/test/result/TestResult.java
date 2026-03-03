package ltd.idcu.est.test.result;

import java.util.Collections;
import java.util.List;

public class TestResult {

    private final String className;
    private final int passed;
    private final int failed;
    private final int skipped;
    private final List<Throwable> failures;

    public TestResult(String className, int passed, int failed, int skipped, List<Throwable> failures) {
        this.className = className;
        this.passed = passed;
        this.failed = failed;
        this.skipped = skipped;
        this.failures = failures != null ? failures : Collections.emptyList();
    }

    public String getClassName() {
        return className;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getSkipped() {
        return skipped;
    }

    public List<Throwable> getFailures() {
        return failures;
    }

    public int getTotal() {
        return passed + failed + skipped;
    }

    public boolean isSuccessful() {
        return failed == 0;
    }

    @Override
    public String toString() {
        return String.format("TestResult[%s: passed=%d, failed=%d, skipped=%d]", 
            className, passed, failed, skipped);
    }
}
