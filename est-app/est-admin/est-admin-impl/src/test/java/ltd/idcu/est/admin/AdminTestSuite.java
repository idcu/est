package ltd.idcu.est.admin;

import ltd.idcu.est.test.Tests;
import ltd.idcu.est.test.result.TestResult;

import java.util.List;

public class AdminTestSuite {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("  EST Admin 模块测试套件");
        System.out.println("========================================");
        System.out.println();

        List<TestResult> results = Tests.run(
            DefaultUserServiceTest.class,
            DefaultRoleServiceTest.class,
            DefaultTenantServiceTest.class
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println("  测试汇总");
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
        System.out.println("总失败: " + totalFailed);
        System.out.println("总跳过: " + totalSkipped);
        System.out.println("总计: " + (totalPassed + totalFailed + totalSkipped));
        System.out.println();
        
        if (totalFailed == 0) {
            System.out.println("✓ 所有测试通过！");
        } else {
            System.out.println("✗ 有测试失败，请检查输出！");
        }
    }
}
