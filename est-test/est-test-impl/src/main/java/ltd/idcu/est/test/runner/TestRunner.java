package ltd.idcu.est.test.runner;

import ltd.idcu.est.test.annotation.*;
import ltd.idcu.est.test.result.TestResult;
import ltd.idcu.est.test.result.TestReporter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private final TestReporter reporter;

    public TestRunner() {
        this(new TestReporter());
    }

    public TestRunner(TestReporter reporter) {
        this.reporter = reporter;
    }

    public TestResult runClass(Class<?> testClass) {
        long startTime = System.currentTimeMillis();
        List<Method> testMethods = findTestMethods(testClass);
        List<Method> beforeEachMethods = findMethodsWithAnnotation(testClass, BeforeEach.class);
        List<Method> afterEachMethods = findMethodsWithAnnotation(testClass, AfterEach.class);
        List<Method> beforeAllMethods = findMethodsWithAnnotation(testClass, BeforeAll.class);
        List<Method> afterAllMethods = findMethodsWithAnnotation(testClass, AfterAll.class);

        int passed = 0;
        int failed = 0;
        int skipped = 0;
        List<Throwable> failures = new ArrayList<>();

        String className = getDisplayName(testClass);
        reporter.reportClassStart(className);

        Object instance = null;
        try {
            instance = testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            reporter.reportError("Failed to instantiate test class: " + e.getMessage());
            return new TestResult(testClass.getName(), 0, testMethods.size(), 0, failures);
        }

        try {
            for (Method beforeAllMethod : beforeAllMethods) {
                beforeAllMethod.setAccessible(true);
                beforeAllMethod.invoke(null);
            }
        } catch (Exception e) {
            reporter.reportError("BeforeAll failed: " + e.getMessage());
        }

        for (Method testMethod : testMethods) {
            if (testMethod.isAnnotationPresent(Disabled.class)) {
                skipped++;
                reporter.reportTestSkipped(getDisplayName(testMethod));
                continue;
            }

            String methodName = getDisplayName(testMethod);
            reporter.reportTestStart(methodName);
            long testStartTime = System.currentTimeMillis();

            try {
                for (Method beforeEachMethod : beforeEachMethods) {
                    beforeEachMethod.setAccessible(true);
                    beforeEachMethod.invoke(instance);
                }

                testMethod.setAccessible(true);
                testMethod.invoke(instance);

                for (Method afterEachMethod : afterEachMethods) {
                    afterEachMethod.setAccessible(true);
                    afterEachMethod.invoke(instance);
                }

                long duration = System.currentTimeMillis() - testStartTime;
                passed++;
                reporter.reportTestPassed(methodName, duration);

            } catch (Throwable t) {
                long duration = System.currentTimeMillis() - testStartTime;
                Throwable cause = t.getCause() != null ? t.getCause() : t;
                failed++;
                failures.add(cause);
                reporter.reportTestFailed(methodName, cause, duration);
            }
        }

        try {
            for (Method afterAllMethod : afterAllMethods) {
                afterAllMethod.setAccessible(true);
                afterAllMethod.invoke(null);
            }
        } catch (Exception e) {
            reporter.reportError("AfterAll failed: " + e.getMessage());
        }

        long totalDuration = System.currentTimeMillis() - startTime;
        reporter.reportClassEnd(className, passed, failed, skipped, totalDuration);

        return new TestResult(testClass.getName(), passed, failed, skipped, failures);
    }

    private List<Method> findTestMethods(Class<?> testClass) {
        List<Method> testMethods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    private List<Method> findMethodsWithAnnotation(Class<?> testClass, Class<? extends java.lang.annotation.Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private String getDisplayName(Class<?> testClass) {
        if (testClass.isAnnotationPresent(DisplayName.class)) {
            return testClass.getAnnotation(DisplayName.class).value();
        }
        return testClass.getSimpleName();
    }

    private String getDisplayName(Method method) {
        if (method.isAnnotationPresent(DisplayName.class)) {
            return method.getAnnotation(DisplayName.class).value();
        }
        if (method.isAnnotationPresent(Test.class)) {
            String displayName = method.getAnnotation(Test.class).displayName();
            if (!displayName.isEmpty()) {
                return displayName;
            }
        }
        return method.getName();
    }
}
