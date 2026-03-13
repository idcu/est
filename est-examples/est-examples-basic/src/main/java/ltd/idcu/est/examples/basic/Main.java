package ltd.idcu.est.examples.basic;

public class Main {
    public static void main(String[] args) {
        System.out.println("EST Framework Basic Examples");
        System.out.println("==============================");
        
        // Run core feature example
        CoreExample.run();
        
        // Run design pattern example
        PatternExample.run();
        
        // Run utils example
        UtilsExample.run();
        
        System.out.println("\n==============================");
        System.out.println("All examples completed successfully!");
    }
}