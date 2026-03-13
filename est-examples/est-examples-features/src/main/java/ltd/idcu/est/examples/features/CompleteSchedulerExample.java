package ltd.idcu.est.examples.features;

import ltd.idcu.est.scheduler.api.ScheduleConfig;
import ltd.idcu.est.scheduler.api.ScheduleType;
import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.api.TaskState;
import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.scheduler.cron.CronSchedulers;
import ltd.idcu.est.scheduler.fixed.FixedSchedulers;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CompleteSchedulerExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteSchedulerExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(70));
        System.out.println("EST Scheduler Module - Complete Example");
        System.out.println("=".repeat(70));
        System.out.println("\nThis example demonstrates various features of the EST Scheduler module:");
        System.out.println("  - Fixed Rate Scheduling");
        System.out.println("  - Cron Expression Scheduling (Flexible Timing)");
        System.out.println("  - Task Management (Start, Pause, Cancel)");
        System.out.println("  - Practical Scenarios (Backup, Statistics, Cleanup)");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("Part 1: Understanding the Role of Scheduling");
        System.out.println("=".repeat(70));
        System.out.println("\n[Why Do We Need a Scheduler?]");
        System.out.println("  - Some tasks need to run on schedule, not manually triggered");
        System.out.println("  - Example: Database backup at 2 AM every day");
        System.out.println("  - Example: Business statistics every hour");
        System.out.println("  - Example: System health check every minute\n");
        
        fixedRateExample();
        cronExpressionExample();
        taskManagementExample();
        practicalScenariosExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("[X] All scheduler examples completed!");
        System.out.println("=".repeat(70));
    }
    
    private static void fixedRateExample() throws InterruptedException {
        System.out.println("\n--- Approach 1: Fixed Rate Scheduling ---");
        System.out.println("\n[Fixed Rate Scheduling Features]");
        System.out.println("  - Runs at fixed intervals");
        System.out.println("  - Simple, easy to use");
        System.out.println("  - Suitable for: Heartbeat detection, status sync\n");
        
        System.out.println("Step 1: Create fixed rate scheduler");
        Scheduler scheduler = FixedSchedulers.create();
        
        System.out.println("\nStep 2: Create tasks");
        Task heartbeatTask = FixedSchedulers.wrap(() -> {
            System.out.println("   [" + new Date() + "] [Heartbeat] System running normally");
        });
        
        Task syncTask = FixedSchedulers.wrap(() -> {
            System.out.println("   [" + new Date() + "] [Sync] Data sync in progress...");
        });
        
        System.out.println("\nStep 3: Configure scheduling rules");
        System.out.println("   Heartbeat task: Runs every 2 seconds");
        ScheduleConfig heartbeatConfig = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(0)
                .period(2)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        System.out.println("   Sync task: Runs every 5 seconds, first delay 3 seconds");
        ScheduleConfig syncConfig = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(3)
                .period(5)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        System.out.println("\nStep 4: Submit tasks to scheduler");
        String heartbeatTaskId = scheduler.schedule(heartbeatTask, heartbeatConfig);
        String syncTaskId = scheduler.schedule(syncTask, syncConfig);
        
        System.out.println("   Heartbeat task ID: " + heartbeatTaskId);
        System.out.println("   Sync task ID: " + syncTaskId);
        
        System.out.println("\nStep 5: Start scheduler");
        scheduler.start();
        System.out.println("   Scheduler started, observing for 10 seconds...\n");
        
        Thread.sleep(10000);
        
        System.out.println("\nStep 6: Stop scheduler");
        scheduler.stop();
        System.out.println("   Scheduler stopped");
        
        logger.info("Fixed rate scheduling example completed");
    }
    
    private static void cronExpressionExample() {
        System.out.println("\n--- Approach 2: Cron Expression Scheduling (Most Flexible) ---");
        System.out.println("\n[What is a Cron Expression?]");
        System.out.println("  - A string to describe scheduled tasks");
        System.out.println("  - Can be precise to seconds, minutes, hours, days, months, weeks");
        System.out.println("  - Very flexible, suitable for complex timing needs\n");
        
        System.out.println("[Cron Expression Format]");
        System.out.println("   * * * * * *");
        System.out.println("   * * * * * *");
        System.out.println();
        System.out.println("   Each position meaning:");
        System.out.println("   - Second: 0-59");
        System.out.println("   - Minute: 0-59");
        System.out.println("   - Hour: 0-23");
        System.out.println("   - Day: 1-31");
        System.out.println("   - Month: 1-12");
        System.out.println("   - Week: 1-7 (1=Sunday, 7=Saturday)\n");
        
        System.out.println("[Common Cron Expression Examples]");
        System.out.println("   Every second:    * * * * * *");
        System.out.println("   Every minute:    0 * * * * *");
        System.out.println("   Every hour:      0 0 * * * *");
        System.out.println("   Daily at 2 AM:   0 0 2 * * *");
        System.out.println("   Monday 9 AM:     0 0 9 * * 1");
        System.out.println("   Monthly 1st:     0 0 0 1 * *\n");
        
        System.out.println("[Special Symbol Meanings]");
        System.out.println("   * : Any value (e.g., * in minute position means every minute)");
        System.out.println("   , : Multiple values (e.g., 1,3,5 in minute position means 1st, 3rd, 5th minutes)");
        System.out.println("   - : Range (e.g., 9-17 in hour position means 9 AM to 5 PM)");
        System.out.println("   / : Interval (e.g., 0/5 in minute position means every 5 minutes)\n");
        
        System.out.println("Step 1: Create Cron scheduler");
        Scheduler cronScheduler = CronSchedulers.create();
        
        System.out.println("\nStep 2: Create tasks");
        Task backupTask = CronSchedulers.wrap(() -> {
            System.out.println("   [Backup] Database backup task executing...");
        });
        
        Task statsTask = CronSchedulers.wrap(() -> {
            System.out.println("   [Stats] Daily statistics task executing...");
        });
        
        System.out.println("\nStep 3: Configure Cron scheduling");
        System.out.println("   Backup task: Runs every 5 seconds (for demo)");
        ScheduleConfig backupConfig = ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression("0/5 * * * * *")
                .build();
        
        System.out.println("   Stats task: Runs every 10 seconds (for demo)");
        ScheduleConfig statsConfig = ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression("0/10 * * * * *")
                .build();
        
        System.out.println("\n[Tip] In production, use real Cron expressions like:");
        System.out.println("   Daily backup at 2 AM: 0 0 2 * * *");
        System.out.println("   Hourly stats on the hour: 0 0 * * * *");
        
        logger.info("Cron expression example completed");
    }
    
    private static void taskManagementExample() {
        System.out.println("\n--- Approach 3: Task Management ---");
        System.out.println("\n[Task Management Features]");
        System.out.println("  - Check task status");
        System.out.println("  - Pause task");
        System.out.println("  - Resume task");
        System.out.println("  - Cancel task\n");
        
        Scheduler scheduler = FixedSchedulers.create();
        
        System.out.println("Step 1: Create and schedule task");
        Task task = FixedSchedulers.wrap(() -> {
            System.out.println("   Task executing: " + new Date());
        });
        
        ScheduleConfig config = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(0)
                .period(2)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        String taskId = scheduler.schedule(task, config);
        System.out.println("   Task scheduled, ID: " + taskId);
        
        System.out.println("\nStep 2: Start scheduler");
        scheduler.start();
        
        System.out.println("\nStep 3: Check task status");
        TaskState state = scheduler.getTaskState(taskId);
        System.out.println("   Task status: " + state);
        
        System.out.println("\nStep 4: Pause task");
        scheduler.pauseTask(taskId);
        System.out.println("   Task paused");
        
        System.out.println("\nStep 5: Resume task");
        scheduler.resumeTask(taskId);
        System.out.println("   Task resumed");
        
        System.out.println("\nStep 6: Cancel task");
        scheduler.cancelTask(taskId);
        System.out.println("   Task cancelled");
        
        System.out.println("\nStep 7: Stop scheduler");
        scheduler.stop();
        
        logger.info("Task management example completed");
    }
    
    private static void practicalScenariosExample() {
        System.out.println("\n--- Approach 4: Practical Application Scenarios ---");
        
        System.out.println("\n[Scenario 1: Data Backup]");
        System.out.println("   Requirement: Backup database at 2 AM daily");
        System.out.println("   Cron: 0 2 * * *");
        System.out.println("   Tasks:");
        System.out.println("     1. Connect to database");
        System.out.println("     2. Export data to file");
        System.out.println("     3. Compress file");
        System.out.println("     4. Upload to cloud storage");
        System.out.println("     5. Delete backups older than 7 days\n");
        
        System.out.println("[Scenario 2: Log Cleanup]");
        System.out.println("   Requirement: Clean logs older than 30 days at 3 AM daily");
        System.out.println("   Cron: 0 3 * * *");
        System.out.println("   Tasks:");
        System.out.println("     1. Traverse log directory");
        System.out.println("     2. Check file last modified time");
        System.out.println("     3. Delete files older than 30 days\n");
        
        System.out.println("[Scenario 3: Data Statistics]");
        System.out.println("   Requirement: Statistics business data every hour");
        System.out.println("   Cron: 0 * * * *");
        System.out.println("   Tasks:");
        System.out.println("     1. Query orders from last hour");
        System.out.println("     2. Calculate sales volume");
        System.out.println("     3. Update statistics report");
        System.out.println("     4. Send notification to admin\n");
        
        System.out.println("[Scenario 4: Health Check]");
        System.out.println("   Requirement: Check system health status every minute");
        System.out.println("   Cron: * * * * *");
        System.out.println("   Tasks:");
        System.out.println("     1. Check database connection");
        System.out.println("     2. Check Redis connection");
        System.out.println("     3. Check disk space");
        System.out.println("     4. Check memory usage");
        System.out.println("     5. If issues, send alert\n");
        
        System.out.println("[Scenario 5: Cache Warmup]");
        System.out.println("   Requirement: Warm up hot data at 8 AM daily");
        System.out.println("   Cron: 0 8 * * *");
        System.out.println("   Tasks:");
        System.out.println("     1. Query hot products");
        System.out.println("     2. Query hot articles");
        System.out.println("     3. Load into cache");
        System.out.println("     4. Improve user access speed\n");
        
        logger.info("Practical scenarios example completed");
    }
}
