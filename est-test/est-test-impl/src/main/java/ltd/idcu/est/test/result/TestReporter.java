package ltd.idcu.est.test.result;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TestReporter {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    private boolean useColors = true;

    public TestReporter() {
    }

    public TestReporter(boolean useColors) {
        this.useColors = useColors;
    }

    public void reportClassStart(String className) {
        System.out.println();
        System.out.println(bold("▶ " + className));
        System.out.println("  " + "─".repeat(50));
    }

    public void reportClassEnd(String className, int passed, int failed, int skipped, long duration) {
        System.out.println("  " + "─".repeat(50));
        StringBuilder summary = new StringBuilder();
        summary.append("  Results: ");
        summary.append(green("✓ " + passed + " passed"));
        if (failed > 0) {
            summary.append(", ").append(red("✗ " + failed + " failed"));
        }
        if (skipped > 0) {
            summary.append(", ").append(yellow("○ " + skipped + " skipped"));
        }
        summary.append(" (" + duration + "ms)");
        System.out.println(summary);
    }

    public void reportTestStart(String testName) {
    }

    public void reportTestPassed(String testName, long duration) {
        System.out.println("  " + green("✓") + " " + testName + " (" + duration + "ms)");
    }

    public void reportTestFailed(String testName, Throwable failure, long duration) {
        System.out.println("  " + red("✗") + " " + testName + " (" + duration + "ms)");
        System.out.println("    " + red(failure.getClass().getSimpleName() + ": " + failure.getMessage()));
        if (failure.getStackTrace().length > 0) {
            System.out.println("        at " + failure.getStackTrace()[0]);
        }
    }

    public void reportTestSkipped(String testName) {
        System.out.println("  " + yellow("○") + " " + testName + " (skipped)");
    }

    public void reportError(String message) {
        System.out.println("  " + red("⚠ ERROR: " + message));
    }

    public void printSummary(List<TestResult> results) {
        System.out.println();
        System.out.println(bold("═══════════════════════════════════════════════════════════════"));
        System.out.println(bold("                           SUMMARY                             "));
        System.out.println(bold("═══════════════════════════════════════════════════════════════"));

        int totalPassed = 0;
        int totalFailed = 0;
        int totalSkipped = 0;

        for (TestResult result : results) {
            totalPassed += result.getPassed();
            totalFailed += result.getFailed();
            totalSkipped += result.getSkipped();
        }

        int total = totalPassed + totalFailed + totalSkipped;

        System.out.println();
        System.out.println("  Total tests: " + total);
        System.out.println("  " + green("Passed: " + totalPassed));
        if (totalFailed > 0) {
            System.out.println("  " + red("Failed: " + totalFailed));
        } else {
            System.out.println("  Failed: " + totalFailed);
        }
        if (totalSkipped > 0) {
            System.out.println("  " + yellow("Skipped: " + totalSkipped));
        }

        System.out.println();
        if (totalFailed == 0) {
            System.out.println(bold(green("  ✓ ALL TESTS PASSED")));
        } else {
            System.out.println(bold(red("  ✗ SOME TESTS FAILED")));
        }
        System.out.println();
    }

    private String green(String text) {
        return useColors ? GREEN + text + RESET : text;
    }

    private String red(String text) {
        return useColors ? RED + text + RESET : text;
    }

    private String yellow(String text) {
        return useColors ? YELLOW + text + RESET : text;
    }

    private String bold(String text) {
        return useColors ? BOLD + text + RESET : text;
    }

    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
