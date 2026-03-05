package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.test.Tests;

public class OrmTestsRunner {
    
    public static void main(String[] args) {
        Tests.run(DialectTest.class);
        System.out.println("\nAll tests completed!");
    }
}
