package ltd.idcu.est.logging.console;

import ltd.idcu.est.test.Tests;

public class RunLoggingTests {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  运行 EST Logging Console 模块测试");
        System.out.println("========================================");
        
        Tests.run(ConsoleLoggerTest.class);
    }
}
