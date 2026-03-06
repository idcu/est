package ltd.idcu.est.examples.basic.data;

public class DataAllExamples {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST Features Data 所有示例");
        System.out.println("=".repeat(60));
        System.out.println();
        
        System.out.println("1. 第一个示例 - 5分钟上手");
        Data01_FirstExample.main(args);
        System.out.println();
        
        System.out.println("2. 基础篇：基本 CRUD 操作");
        Data02_BasicCrud.main(args);
        System.out.println();
        
        System.out.println("3. 进阶篇：高级查询");
        Data03_AdvancedQuery.main(args);
        System.out.println();
        
        System.out.println("4. 集成篇：EST Data + EST Collection");
        Data04_CollectionIntegration.main(args);
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("所有示例运行完成！");
        System.out.println("=".repeat(60));
    }
}
