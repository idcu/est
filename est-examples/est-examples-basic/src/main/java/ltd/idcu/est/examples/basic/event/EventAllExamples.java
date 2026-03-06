package ltd.idcu.est.examples.basic.event;

public class EventAllExamples {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EST Event 模块所有示例");
        System.out.println("========================================");
        
        System.out.println("\n--- 示例 1: 第一个事件总线示例 ---");
        Event01_FirstExample.main(args);
        
        System.out.println("\n--- 示例 2: 创建事件总线 ---");
        Event02_CreateEventBus.main(args);
        
        System.out.println("\n--- 示例 3: 订阅事件 ---");
        Event03_Subscribe.main(args);
        
        System.out.println("\n--- 示例 4: 发布事件 ---");
        Event04_Publish.main(args);
        
        System.out.println("\n--- 示例 5: 取消订阅 ---");
        Event05_Unsubscribe.main(args);
        
        System.out.println("\n--- 示例 6: 事件统计 ---");
        Event06_Stats.main(args);
        
        System.out.println("\n--- 示例 7: 电商订单系统 ---");
        Event07_ECommerce.main(args);
        
        System.out.println("\n========================================");
        System.out.println("  所有示例执行完成！");
        System.out.println("========================================");
    }
}
