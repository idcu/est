package ltd.idcu.est.core.impl;

import ltd.idcu.est.test.Tests;

public class CoreTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Running EST Core Tests ===");
        System.out.println();
        
        boolean success = Tests.runPackageAndExit("ltd.idcu.est.core.impl");
        
        System.out.println();
        System.out.println("=== Test Run Complete ===");
        System.exit(success ? 0 : 1);
    }
}
