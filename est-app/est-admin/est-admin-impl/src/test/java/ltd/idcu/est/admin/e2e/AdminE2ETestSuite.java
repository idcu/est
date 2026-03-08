package ltd.idcu.est.admin.e2e;

import ltd.idcu.est.test.Tests;
import ltd.idcu.est.test.result.TestResult;

import java.util.List;

public class AdminE2ETestSuite {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("  EST Admin E2E 测试套件");
        System.out.println("========================================");
        System.out.println();

        List<TestResult> results = Tests.run(
            AuthE2ETest.class,
            UserManagementE2ETest.class,
            MenuManagementE2ETest.class,
            LogAndMonitorE2ETest.class
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println("  E2E 测试汇�?);
        System.out.println("========================================");
        
        int totalPassed = 0;
        int totalFailed = 0;
        int totalSkipped = 0;
        
        for (TestResult result : results) {
            totalPassed += result.getPassed();
            totalFailed += result.getFailed();
            totalSkipped += result.getSkipped();
        }
        
        System.out.println("总通过: " + totalPassed);
        System.out.println("总失�? " + totalFailed);
        System.out.println("总跳�? " + totalSkipped);
        System.out.println("总计: " + (totalPassed + totalFailed + totalSkipped));
        System.out.println();
        
        if (totalFailed == 0) {
            System.out.println("�?所�?E2E 测试通过�?);
        } else {
            System.out.println("�?�?E2E 测试失败，请检查输出！");
        }
    }
}
