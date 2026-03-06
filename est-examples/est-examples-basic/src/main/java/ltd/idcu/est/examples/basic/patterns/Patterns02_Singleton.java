package ltd.idcu.est.examples.basic.patterns;

import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

public class Patterns02_Singleton {
    public static void main(String[] args) {
        System.out.println("=== 单例模式示例 ===");
        System.out.println();

        Singleton<ClassMonitor> monitorSingleton = 
            DefaultSingleton.of(ClassMonitor::new);
        
        ClassMonitor monitor1 = monitorSingleton.getInstance();
        ClassMonitor monitor2 = monitorSingleton.getInstance();
        
        monitor1.report("小明");
        monitor2.report("小红");
        
        System.out.println("是同一个班长: " + (monitor1 == monitor2));
    }
}

class ClassMonitor {
    public void report(String studentName) {
        System.out.println(studentName + " 向班长汇报工作");
    }
}
