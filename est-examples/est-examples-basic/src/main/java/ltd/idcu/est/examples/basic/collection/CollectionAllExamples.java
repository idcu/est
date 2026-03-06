package ltd.idcu.est.examples.basic.collection;

public class CollectionAllExamples {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║           EST Collection 集合模块 - 完整示例演示                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("按顺序运行以下示例：");
        System.out.println("  1. 第一个示例 - 快速入门");
        System.out.println("  2. 创建序列的 7 种方法");
        System.out.println("  3. 基本操作");
        System.out.println("  4. 筛选与映射");
        System.out.println("  5. 排序与截取");
        System.out.println("  6. 对象操作");
        System.out.println("  7. 聚合操作");
        System.out.println("  8. 实战案例：电商数据处理");
        System.out.println("\n─────────────────────────────────────────────────────────────────\n");
        
        try {
            System.out.println("\n【示例 1/8】第一个示例 - 快速入门");
            System.out.println("─────────────────────────────────────────");
            Collection01_FirstExample.main(args);
            
            System.out.println("\n\n【示例 2/8】创建序列的 7 种方法");
            System.out.println("─────────────────────────────────────────");
            Collection02_CreateSeq.main(args);
            
            System.out.println("\n\n【示例 3/8】基本操作");
            System.out.println("─────────────────────────────────────────");
            Collection03_BasicOperations.main(args);
            
            System.out.println("\n\n【示例 4/8】筛选与映射");
            System.out.println("─────────────────────────────────────────");
            Collection04_FilterAndMap.main(args);
            
            System.out.println("\n\n【示例 5/8】排序与截取");
            System.out.println("─────────────────────────────────────────");
            Collection05_SortAndSlice.main(args);
            
            System.out.println("\n\n【示例 6/8】对象操作");
            System.out.println("─────────────────────────────────────────");
            Collection06_ObjectOperations.main(args);
            
            System.out.println("\n\n【示例 7/8】聚合操作");
            System.out.println("─────────────────────────────────────────");
            Collection07_Aggregation.main(args);
            
            System.out.println("\n\n【示例 8/8】实战案例：电商数据处理");
            System.out.println("─────────────────────────────────────────");
            Collection08_RealWorldExample.main(args);
            
            System.out.println("\n\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    所有示例运行完成！🎉                         ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            
        } catch (Exception e) {
            System.err.println("运行示例时出错：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
