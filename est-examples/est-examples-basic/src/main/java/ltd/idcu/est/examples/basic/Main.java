package ltd.idcu.est.examples.basic;

public class Main {
    public static void main(String[] args) {
        System.out.println("EST Framework Basic Examples");
        System.out.println("==============================");
        
        // 运行核心功能示例
        CoreExample.run();
        
        // 运行设计模式示例
        PatternExample.run();
        
        // 运行工具类示例
        UtilsExample.run();
        
        System.out.println("\n==============================");
        System.out.println("All examples completed successfully!");
    }
}