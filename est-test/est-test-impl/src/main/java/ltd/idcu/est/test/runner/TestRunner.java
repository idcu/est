package ltd.idcu.est.test.runner;

import ltd.idcu.est.test.annotation.*;
import ltd.idcu.est.test.result.TestResult;
import ltd.idcu.est.test.result.TestReporter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private final TestReporter reporter;
    private final TestScanner scanner;
    private final TestExecutor executor;

    public TestRunner() {
        this(new TestReporter());
    }

    public TestRunner(TestReporter reporter) {
        this.reporter = reporter;
        this.scanner = new TestScanner();
        this.executor = new TestExecutor(reporter);
    }

    public TestResult runClass(Class<?> testClass) {
        return executor.execute(testClass);
    }

    public List<TestResult> runClasses(Class<?>... testClasses) {
        List<TestResult> results = new ArrayList<>();
        for (Class<?> testClass : testClasses) {
            if (scanner.isTestClass(testClass)) {
                results.add(runClass(testClass));
            }
        }
        reporter.printSummary(results);
        return results;
    }

    public List<TestResult> runPackage(String packageName) throws Exception {
        List<Class<?>> testClasses = scanner.scanPackageForTests(packageName);
        List<TestResult> results = new ArrayList<>();

        for (Class<?> testClass : testClasses) {
            results.add(runClass(testClass));
        }

        reporter.printSummary(results);
        return results;
    }

    public boolean runPackageAndExit(String packageName) {
        try {
            List<TestResult> results = runPackage(packageName);
            return isAllTestsPassed(results);
        } catch (Exception e) {
            reporter.reportError("Failed to run tests: " + e.getMessage());
            return false;
        }
    }

    public boolean runClassesAndExit(Class<?>... testClasses) {
        List<TestResult> results = runClasses(testClasses);
        return isAllTestsPassed(results);
    }

    private boolean isAllTestsPassed(List<TestResult> results) {
        for (TestResult result : results) {
            if (!result.isSuccessful()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: TestRunner <test-class-name> [test-class-name...]");
            System.out.println("   or: TestRunner --package <package-name>");
            System.exit(1);
        }

        TestRunner runner = new TestRunner();
        boolean success;

        if (args[0].equals("--package") && args.length > 1) {
            success = runner.runPackageAndExit(args[1]);
        } else {
            List<Class<?>> testClasses = new ArrayList<>();
            for (String className : args) {
                try {
                    Class<?> clazz = Class.forName(className);
                    testClasses.add(clazz);
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found: " + className);
                }
            }
            success = runner.runClassesAndExit(testClasses.toArray(new Class<?>[0]));
        }

        System.exit(success ? 0 : 1);
    }
}
