package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.monitor.api.Metrics;
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;
import ltd.idcu.est.features.monitor.system.SystemMonitor;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.Map;

public class CompleteMonitorExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteMonitorExample.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST 监控系统模块 - 完整示例");
        System.out.println("=".repeat(70));
        System.out.println("\n本示例将展示 EST 监控系统模块的各种功能：");
        System.out.println("  - JVM 监控（内存、CPU、线程）");
        System.out.println("  - 系统监控（磁盘、网络）");
        System.out.println("  - 健康检查（判断系统是否正常）");
        System.out.println("  - 自定义监控指标");
        System.out.println("  - 与告警系统联动");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("第一部分：理解监控系统的作用");
        System.out.println("=".repeat(70));
        System.out.println("\n【为什么需要监控系统？】");
        System.out.println("  - 实时了解应用运行状态");
        System.out.println("  - 提前发现问题，防患于未然");
        System.out.println("  - 性能分析和优化");
        System.out.println("  - 故障排查和诊断\n");
        
        jvmMonitorExample();
        systemMonitorExample();
        healthCheckExample();
        customMetricsExample();
        alertIntegrationExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("✓ 所有监控系统示例完成！");
        System.out.println("=".repeat(70));
    }
    
    private static void jvmMonitorExample() {
        System.out.println("\n--- 方式一：JVM 监控 ---");
        System.out.println("\n【JVM 监控什么？】");
        System.out.println("  - 堆内存使用情况");
        System.out.println("  - 非堆内存使用情况");
        System.out.println("  - 线程数量");
        System.out.println("  - GC（垃圾回收）情况");
        System.out.println("  - JVM 运行时间\n");
        
        System.out.println("💡 为什么要监控 JVM？");
        System.out.println("  - 内存泄漏会导致 OOM（Out Of Memory）");
        System.out.println("  - 线程过多会导致系统变慢");
        System.out.println("  - GC 频繁会影响性能\n");
        
        JvmMonitor jvmMonitor = JvmMonitor.getInstance();
        
        System.out.println("步骤 1: 获取所有 JVM 指标");
        Map<String, Object> allMetrics = jvmMonitor.getAllMetrics();
        System.out.println("   所有 JVM 指标:");
        for (Map.Entry<String, Object> entry : allMetrics.entrySet()) {
            System.out.println("     - " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\n步骤 2: 获取 JVM 信息");
        String jvmInfo = jvmMonitor.getJvmInfo();
        System.out.println("   JVM 信息: " + jvmInfo);
        
        System.out.println("\n步骤 3: 获取运行时间");
        long uptime = jvmMonitor.getUptime();
        System.out.println("   JVM 已运行: " + uptime + " 毫秒");
        System.out.println("   约等于: " + (uptime / 1000) + " 秒");
        
        System.out.println("\n步骤 4: 关键指标解读");
        System.out.println("   🧠 堆内存：如果使用率持续超过 80%，可能有内存泄漏");
        System.out.println("   🧵 线程数：如果持续增长，可能有线程泄漏");
        System.out.println("   🗑️ GC 次数：如果频繁 Full GC，性能会下降");
        
        logger.info("JVM 监控示例完成");
    }
    
    private static void systemMonitorExample() {
        System.out.println("\n--- 方式二：系统监控 ---");
        System.out.println("\n【系统监控什么？】");
        System.out.println("  - CPU 使用率");
        System.out.println("  - 内存使用率");
        System.out.println("  - 磁盘空间");
        System.out.println("  - 网络流量");
        System.out.println("  - 系统负载\n");
        
        System.out.println("💡 为什么要监控系统？");
        System.out.println("  - 磁盘满了会导致应用崩溃");
        System.out.println("  - CPU 100% 会导致系统卡死");
        System.out.println("  - 内存不足会导致 OOM\n");
        
        SystemMonitor systemMonitor = SystemMonitor.getInstance();
        
        System.out.println("步骤 1: 获取系统指标");
        Map<String, Object> systemMetrics = systemMonitor.getAllMetrics();
        System.out.println("   系统指标:");
        for (Map.Entry<String, Object> entry : systemMetrics.entrySet()) {
            System.out.println("     - " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\n步骤 2: 实际应用场景");
        System.out.println("   🔍 监控磁盘空间：");
        System.out.println("      如果使用率 > 90%，发送告警");
        System.out.println("      自动清理旧日志文件");
        System.out.println();
        System.out.println("   🔍 监控 CPU 使用率：");
        System.out.println("      如果持续 > 80%，分析是什么进程占用");
        System.out.println("      可能需要扩容或优化代码");
        System.out.println();
        System.out.println("   🔍 监控内存使用率：");
        System.out.println("      如果持续 > 90%，检查是否有内存泄漏");
        System.out.println("      可能需要增加服务器内存");
        
        logger.info("系统监控示例完成");
    }
    
    private static void healthCheckExample() {
        System.out.println("\n--- 方式三：健康检查 ---");
        System.out.println("\n【什么是健康检查？】");
        System.out.println("  - 检查系统各个组件是否正常工作");
        System.out.println("  - 返回健康或不健康状态");
        System.out.println("  - 可以包含详细的错误信息\n");
        
        System.out.println("💡 健康检查的用途");
        System.out.println("  - 负载均衡器：只把流量转发给健康的节点");
        System.out.println("  - 容器编排：自动重启不健康的容器");
        System.out.println("  - 告警系统：发现不健康立即通知管理员\n");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        System.out.println("步骤 1: 执行健康检查");
        HealthCheckResult healthResult = monitor.checkHealth();
        
        System.out.println("\n步骤 2: 查看健康状态");
        HealthStatus status = healthResult.getHealthStatus();
        System.out.println("   健康状态: " + status);
        System.out.println("   状态消息: " + healthResult.getMessage());
        
        System.out.println("\n步骤 3: 健康状态解读");
        if (status == HealthStatus.HEALTHY) {
            System.out.println("   ✅ 系统健康，一切正常！");
        } else if (status == HealthStatus.DEGRADED) {
            System.out.println("   ⚠️  系统性能下降，但还能工作");
            System.out.println("   建议：检查一下是什么导致的");
        } else {
            System.out.println("   ❌ 系统不健康，需要立即处理！");
            System.out.println("   建议：查看日志，找出问题原因");
        }
        
        System.out.println("\n步骤 4: 自定义健康检查示例");
        System.out.println("   数据库健康检查：");
        System.out.println("     1. 尝试连接数据库");
        System.out.println("     2. 执行简单的查询（SELECT 1）");
        System.out.println("     3. 如果成功，返回 HEALTHY");
        System.out.println("     4. 如果失败，返回 UNHEALTHY");
        System.out.println();
        System.out.println("   Redis 健康检查：");
        System.out.println("     1. 尝试连接 Redis");
        System.out.println("     2. 执行 PING 命令");
        System.out.println("     3. 如果返回 PONG，返回 HEALTHY");
        System.out.println("     4. 否则返回 UNHEALTHY");
        
        logger.info("健康检查示例完成");
    }
    
    private static void customMetricsExample() {
        System.out.println("\n--- 方式四：自定义监控指标 ---");
        System.out.println("\n【为什么需要自定义指标？】");
        System.out.println("  - JVM 和系统指标是通用的");
        System.out.println("  - 你可能需要监控业务相关的指标");
        System.out.println("  - 比如：订单数、用户数、转化率等\n");
        
        System.out.println("【常见的业务指标】");
        System.out.println("   🛒 电商网站：");
        System.out.println("      - 每分钟订单数");
        System.out.println("      - 每分钟交易额");
        System.out.println("      - 购物车转化率");
        System.out.println();
        System.out.println("   📱 社交应用：");
        System.out.println("      - 每分钟新增用户");
        System.out.println("      - 每分钟消息数");
        System.out.println("      - 日活用户数");
        System.out.println();
        System.out.println("   🎮 游戏应用：");
        System.out.println("      - 在线玩家数");
        System.out.println("      - 每分钟对局数");
        System.out.println("      - 充值金额\n");
        
        System.out.println("【如何使用自定义指标】");
        System.out.println("   1. 定义指标名称和类型（计数器、仪表盘等）");
        System.out.println("   2. 在代码中更新指标值");
        System.out.println("   3. 监控系统采集指标");
        System.out.println("   4. 在监控面板上展示");
        System.out.println("   5. 设置告警规则\n");
        
        System.out.println("【示例：订单计数器】");
        System.out.println("   ```java");
        System.out.println("   // 定义订单计数器");
        System.out.println("   private static AtomicLong orderCount = new AtomicLong(0);");
        System.out.println();
        System.out.println("   // 每创建一个订单，计数器+1");
        System.out.println("   public void createOrder(Order order) {");
        System.out.println("       // ... 保存订单逻辑");
        System.out.println("       orderCount.incrementAndGet();");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // 监控系统获取指标值");
        System.out.println("   public long getOrderCount() {");
        System.out.println("       return orderCount.get();");
        System.out.println("   }");
        System.out.println("   ```");
        
        logger.info("自定义监控指标示例完成");
    }
    
    private static void alertIntegrationExample() {
        System.out.println("\n--- 方式五：与告警系统联动 ---");
        System.out.println("\n【为什么需要告警？】");
        System.out.println("  - 监控只是看，告警才会通知你");
        System.out.println("  - 不能 24 小时盯着监控面板");
        System.out.println("  - 问题发生时需要立即知道\n");
        
        System.out.println("【常见的告警方式】");
        System.out.println("   📧 邮件告警：适合不紧急的通知");
        System.out.println("   💬 短信告警：适合重要通知");
        System.out.println("   🔔 钉钉/企业微信：团队协作常用");
        System.out.println("   📞 电话告警：非常紧急的情况\n");
        
        System.out.println("【告警规则示例】");
        System.out.println("   规则 1: CPU 使用率 > 90% 持续 5 分钟");
        System.out.println("   动作: 发送邮件通知运维");
        System.out.println();
        System.out.println("   规则 2: 内存使用率 > 95%");
        System.out.println("   动作: 发送短信给值班人员");
        System.out.println();
        System.out.println("   规则 3: 磁盘空间 < 10%");
        System.out.println("   动作: 钉钉群告警 + 邮件");
        System.out.println();
        System.out.println("   规则 4: 健康检查失败");
        System.out.println("   动作: 立即电话通知负责人\n");
        
        System.out.println("【告警级别】");
        System.out.println("   P0 (紧急): 系统不可用，立即处理");
        System.out.println("   P1 (高): 严重影响功能，尽快处理");
        System.out.println("   P2 (中): 功能受影响，工作时间处理");
        System.out.println("   P3 (低): 建议优化，有空处理\n");
        
        System.out.println("【最佳实践】");
        System.out.println("   1. 避免告警风暴：不要每秒都发告警");
        System.out.println("   2. 设置静默期：同一问题只发一次");
        System.out.println("   3. 分级处理：不同级别不同方式");
        System.out.println("   4. 告警必处理：每个告警都要有回应");
        System.out.println("   5. 定期回顾：哪些告警是没用的，删掉");
        
        logger.info("告警联动示例完成");
    }
}
