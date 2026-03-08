package ltd.idcu.est.admin;

import ltd.idcu.est.test.Tests;
import ltd.idcu.est.test.result.TestResult;

import java.util.List;

public class RunAdminTests {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("  EST Admin 模块测试");
        System.out.println("========================================");
        System.out.println();

        List<TestResult> results = Tests.run(
            DefaultUserServiceTest.class,
            DefaultRoleServiceTest.class,
            DefaultTenantServiceTest.class,
            DefaultMenuServiceTest.class,
            DefaultDepartmentServiceTest.class,
            PhaseThreeTest.class
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println("  测试完成");
        System.out.println("========================================");
    }
}
