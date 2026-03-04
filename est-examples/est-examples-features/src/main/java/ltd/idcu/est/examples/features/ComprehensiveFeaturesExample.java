package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.async.AsyncEvents;
import ltd.idcu.est.features.event.local.LocalEvents;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;
import ltd.idcu.est.features.logging.file.FileLogs;
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.AuthenticationProvider;
import ltd.idcu.est.features.security.api.Token;
import ltd.idcu.est.features.security.api.TokenProvider;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.api.UserDetailsService;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.jwt.JwtSecurity;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ComprehensiveFeaturesExample {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== Comprehensive Features Example ===");
        
        enhancedLoggingExample();
        enhancedSecurityExample();
        enhancedSchedulerExample();
        enhancedEventExample();
    }
    
    private static void enhancedLoggingExample() {
        System.out.println("\n--- Enhanced Logging Example ---");
        
        Logger consoleLogger = ConsoleLogs.newLogger("ConsoleApp");
        consoleLogger.setLevel(LogLevel.DEBUG);
        
        consoleLogger.trace("This is a trace message");
        consoleLogger.debug("Debugging information");
        consoleLogger.info("Application started");
        consoleLogger.warn("Low memory warning");
        consoleLogger.error("Failed to connect to database", new RuntimeException("Connection timeout"));
        
        String logDir = System.getProperty("java.io.tmpdir") + "/est-logs";
        File logFile = new File(logDir, "app.log");
        logFile.getParentFile().mkdirs();
        
        Logger fileLogger = FileLogs.newLogger(logFile);
        fileLogger.info("This goes to file: " + logFile.getAbsolutePath());
        fileLogger.warn("Warning in file log");
        
        System.out.println("Logs written to console and file");
    }
    
    private static void enhancedSecurityExample() {
        System.out.println("\n--- Enhanced Security Example ---");
        
        UserDetailsService userDetailsService = BasicSecurity.newInMemoryUserDetailsService();
        
        User adminUser = BasicSecurity.newUser("admin", "admin123", "ADMIN", "USER");
        User regularUser = BasicSecurity.newUser("john", "password123", "USER");
        
        userDetailsService.createUser(adminUser);
        userDetailsService.createUser(regularUser);
        System.out.println("Created 2 users: admin and john");
        
        AuthenticationProvider authProvider = BasicSecurity.newBasicAuthenticationProvider(userDetailsService);
        
        Authentication auth1 = authProvider.authenticate("admin", "admin123");
        System.out.println("Admin authenticated: " + auth1.isAuthenticated());
        System.out.println("Admin roles: " + auth1.getUser().getRoles());
        
        Authentication auth2 = authProvider.authenticate("john", "password123");
        System.out.println("John authenticated: " + auth2.isAuthenticated());
        
        try {
            authProvider.authenticate("john", "wrongpass");
        } catch (Exception e) {
            System.out.println("Wrong password correctly rejected: " + e.getMessage());
        }
        
        System.out.println("\n--- JWT Example ---");
        TokenProvider tokenProvider = JwtSecurity.newTokenProvider("my-super-secret-key-12345");
        
        User jwtUser = JwtSecurity.newUser("jane", "USER", "EDITOR");
        Token token = tokenProvider.generateToken(jwtUser);
        System.out.println("Generated JWT Token: " + token.getValue().substring(0, 30) + "...");
        
        Authentication jwtAuth = tokenProvider.validateToken(token.getValue());
        System.out.println("JWT Token valid: " + jwtAuth.isAuthenticated());
        System.out.println("JWT User: " + jwtAuth.getUser().getUsername());
        System.out.println("JWT Roles: " + jwtAuth.getUser().getRoles());
    }
    
    private static void enhancedSchedulerExample() throws InterruptedException {
        System.out.println("\n--- Enhanced Scheduler Example ---");
        
        CountDownLatch latch = new CountDownLatch(3);
        
        Scheduler fixedRateScheduler = FixedRateSchedulers.newScheduler();
        
        System.out.println("Starting fixed rate task (every 500ms)...");
        fixedRateScheduler.scheduleAtFixedRate(() -> {
            System.out.println("Fixed rate task executed at: " + System.currentTimeMillis());
            latch.countDown();
        }, 0, 500);
        
        latch.await(3, TimeUnit.SECONDS);
        fixedRateScheduler.shutdown();
        System.out.println("Fixed rate scheduler stopped");
        
        System.out.println("\n--- Cron Scheduler Example ---");
        Scheduler cronScheduler = CronSchedulers.newScheduler();
        
        CountDownLatch cronLatch = new CountDownLatch(2);
        System.out.println("Starting cron task (every second)...");
        
        cronScheduler.schedule("* * * * * ?", () -> {
            System.out.println("Cron task executed at: " + System.currentTimeMillis());
            cronLatch.countDown();
        });
        
        cronLatch.await(3, TimeUnit.SECONDS);
        cronScheduler.shutdown();
        System.out.println("Cron scheduler stopped");
    }
    
    private static void enhancedEventExample() throws InterruptedException {
        System.out.println("\n--- Enhanced Event Example ---");
        
        EventBus localEventBus = LocalEvents.newEventBus();
        
        CountDownLatch localLatch = new CountDownLatch(2);
        
        localEventBus.register(String.class, new EventListener<String>() {
            @Override
            public void onEvent(String event) {
                System.out.println("Local listener 1 received: " + event);
                localLatch.countDown();
            }
        });
        
        localEventBus.register(String.class, event -> {
            System.out.println("Local listener 2 received: " + event);
            localLatch.countDown();
        });
        
        localEventBus.publish("Hello from local event bus!");
        localLatch.await(1, TimeUnit.SECONDS);
        
        System.out.println("\n--- Async Event Example ---");
        EventBus asyncEventBus = AsyncEvents.newEventBus();
        
        CountDownLatch asyncLatch = new CountDownLatch(3);
        
        asyncEventBus.register(String.class, event -> {
            System.out.println("Async listener received: " + event + 
                             " on thread: " + Thread.currentThread().getName());
            asyncLatch.countDown();
        });
        
        for (int i = 0; i < 3; i++) {
            asyncEventBus.publish("Async event " + i);
        }
        
        asyncLatch.await(2, TimeUnit.SECONDS);
        System.out.println("All async events processed");
    }
}
