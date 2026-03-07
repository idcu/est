package ltd.idcu.est.web.integration;

import ltd.idcu.est.test.Tests;

public class IntegrationTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Running EST Web Integration Tests ===");
        System.out.println();
        
        boolean success = Tests.runPackageAndExit("ltd.idcu.est.web.integration");
        
        System.out.println();
        System.out.println("=== Integration Test Run Complete ===");
        System.exit(success ? 0 : 1);
    }
}
