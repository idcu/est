package ltd.idcu.est.event.local;

import ltd.idcu.est.test.Tests;

public class RunEventTests {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  运行 EST Event Local 模块测试");
        System.out.println("========================================");
        
        Tests.run(LocalEventBusTest.class);
    }
}
