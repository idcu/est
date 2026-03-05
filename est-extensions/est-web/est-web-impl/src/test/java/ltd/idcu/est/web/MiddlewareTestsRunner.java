package ltd.idcu.est.web;

import ltd.idcu.est.test.Tests;

public class MiddlewareTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Running EST Web Middleware Tests ===");
        System.out.println();
        
        Tests.run(
            LoggingMiddlewareTest.class,
            SecurityMiddlewareTest.class,
            DefaultCorsMiddlewareTest.class,
            PerformanceMonitorMiddlewareTest.class
        );
        
        System.out.println();
        System.out.println("=== Test Run Complete ===");
    }
}
