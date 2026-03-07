package ltd.idcu.est.web;

import ltd.idcu.est.test.Tests;

public class WebTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Running EST Web Tests ===");
        System.out.println();
        
        boolean success = Tests.runPackageAndExit("ltd.idcu.est.web");
        
        System.out.println();
        System.out.println("=== Test Run Complete ===");
        System.exit(success ? 0 : 1);
    }
}
