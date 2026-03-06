package ltd.idcu.est.collection.impl;

import ltd.idcu.est.test.Tests;

public class CollectionTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Running EST Collection Tests ===");
        System.out.println();
        
        boolean success = Tests.runPackageAndExit("ltd.idcu.est.collection.impl");
        
        System.out.println();
        System.out.println("=== Test Run Complete ===");
        System.exit(success ? 0 : 1);
    }
}
