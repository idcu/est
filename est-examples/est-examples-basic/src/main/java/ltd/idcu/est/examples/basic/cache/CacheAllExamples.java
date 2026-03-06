package ltd.idcu.est.examples.basic.cache;

public class CacheAllExamples {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST Features Cache 所有示例");
        System.out.println("=".repeat(60));
        System.out.println();
        
        System.out.println("1. 第一个示例 - 5分钟上手");
        Cache01_FirstExample.main(args);
        System.out.println();
        
        System.out.println("2. 基础篇：基本操作");
        Cache02_BasicOperations.main(args);
        System.out.println();
        
        System.out.println("3. 进阶篇：缓存统计");
        Cache03_StatsExample.main(args);
        System.out.println();
        
        System.out.println("4. 进阶篇：LRU 淘汰策略");
        Cache04_LruExample.main(args);
        System.out.println();
        
        System.out.println("5. 集成篇：EST Cache + EST Collection");
        Cache05_CollectionIntegration.main(args);
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("所有示例运行完成！");
        System.out.println("=".repeat(60));
    }
}
