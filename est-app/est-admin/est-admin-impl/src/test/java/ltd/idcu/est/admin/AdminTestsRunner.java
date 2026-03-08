package ltd.idcu.est.admin;

import ltd.idcu.est.test.Tests;

public class AdminTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("  运行 EST Admin 模块测试");
        System.out.println("========================================");
        
        Tests.run(
            DefaultUserServiceTest.class,
            DefaultRoleServiceTest.class,
            DefaultTenantServiceTest.class
        );
    }
}
