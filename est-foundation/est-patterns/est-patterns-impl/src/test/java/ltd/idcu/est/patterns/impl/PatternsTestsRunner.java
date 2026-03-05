package ltd.idcu.est.patterns.impl;

import ltd.idcu.est.patterns.impl.behavioral.BehavioralPatternsTest;
import ltd.idcu.est.patterns.impl.creational.CreationalPatternsTest;
import ltd.idcu.est.patterns.impl.structural.StructuralPatternsTest;
import ltd.idcu.est.test.Tests;

public class PatternsTestsRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Running EST Patterns Tests ===");
        System.out.println();
        
        boolean success = Tests.runAndExit(
            CreationalPatternsTest.class,
            BehavioralPatternsTest.class,
            StructuralPatternsTest.class
        );
        
        System.out.println();
        System.out.println("=== Test Run Complete ===");
        System.exit(success ? 0 : 1);
    }
}
