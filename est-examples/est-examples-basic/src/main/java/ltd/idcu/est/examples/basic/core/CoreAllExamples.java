package ltd.idcu.est.examples.basic.core;

public class CoreAllExamples {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST Core 所有示例");
        System.out.println("=".repeat(60));
        System.out.println();
        
        System.out.println("1. 第一个示例 - 5分钟上手");
        Core01_FirstExample.main(args);
        System.out.println();
        
        System.out.println("2. 基础注册 - 注册和获取组件");
        Core02_BasicRegistration.main(args);
        System.out.println();
        
        System.out.println("3. 构造函数注入");
        Core03_ConstructorInjection.main(args);
        System.out.println();
        
        System.out.println("4. @Inject 注解注入");
        Core04_AnnotationInjection.main(args);
        System.out.println();
        
        System.out.println("5. 作用域（Scope）");
        Core05_ScopeExample.main(args);
        System.out.println();
        
        System.out.println("6. 生命周期管理");
        Core06_LifecycleExample.main(args);
        System.out.println();
        
        System.out.println("7. 配置管理");
        Core07_ConfigExample.main(args);
        System.out.println();
        
        System.out.println("8. 与 EST Collection 集成");
        Core08_CollectionIntegration.main(args);
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("所有示例运行完成！");
        System.out.println("=".repeat(60));
    }
}
