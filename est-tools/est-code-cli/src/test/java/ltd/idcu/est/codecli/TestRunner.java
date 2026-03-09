package ltd.idcu.est.codecli;

import ltd.idcu.est.codecli.config.CliConfigTest;
import ltd.idcu.est.codecli.prompts.PromptLibraryTest;
import ltd.idcu.est.codecli.prompts.PromptTemplateTest;
import ltd.idcu.est.codecli.search.FileIndexTest;
import ltd.idcu.est.codecli.skills.SkillManagerTest;
import ltd.idcu.est.test.Tests;
import ltd.idcu.est.test.result.TestResult;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("EST Code CLI Unit Tests");
        System.out.println("========================================");
        System.out.println();

        List<TestResult> results = Tests.run(
            CliConfigTest.class,
            FileIndexTest.class,
            PromptTemplateTest.class,
            PromptLibraryTest.class,
            SkillManagerTest.class
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println("Test Summary");
        System.out.println("========================================");

        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;

        for (TestResult result : results) {
            totalTests += result.getTotalCount();
            passedTests += result.getPassedCount();
            failedTests += result.getFailedCount();

            System.out.println();
            System.out.println("Test Class: " + result.getTestClass().getSimpleName());
            System.out.println("  Total: " + result.getTotalCount());
            System.out.println("  Passed: " + result.getPassedCount());
            System.out.println("  Failed: " + result.getFailedCount());

            if (result.getFailedCount() > 0) {
                System.out.println("  Failed Tests:");
                for (String failed : result.getFailedTestNames()) {
                    System.out.println("    - " + failed);
                }
            }
        }

        System.out.println();
        System.out.println("========================================");
        System.out.println("Overall: " + passedTests + "/" + totalTests + " tests passed");
        System.out.println("========================================");

        System.exit(failedTests > 0 ? 1 : 0);
    }
}
