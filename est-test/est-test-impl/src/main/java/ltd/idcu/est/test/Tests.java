package ltd.idcu.est.test;

import ltd.idcu.est.test.runner.TestRunner;
import ltd.idcu.est.test.result.TestResult;

import java.util.List;

public final class Tests {

    private static final TestRunner DEFAULT_RUNNER = new TestRunner();

    private Tests() {
    }

    public static TestResult run(Class<?> testClass) {
        return DEFAULT_RUNNER.runClass(testClass);
    }

    public static List<TestResult> run(Class<?>... testClasses) {
        return DEFAULT_RUNNER.runClasses(testClasses);
    }

    public static List<TestResult> runPackage(String packageName) throws Exception {
        return DEFAULT_RUNNER.runPackage(packageName);
    }

    public static boolean runAndExit(Class<?>... testClasses) {
        return DEFAULT_RUNNER.runClassesAndExit(testClasses);
    }

    public static boolean runPackageAndExit(String packageName) {
        return DEFAULT_RUNNER.runPackageAndExit(packageName);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.main(args);
    }
}
