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
import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.jwt.JwtSecurity;
import ltd.idcu.est.features.security.jwt.JwtTokenProvider;

import java.io.File;
import java.util.Optional;
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
        
        Logger consoleLogger = ConsoleLogs.getLogger("ConsoleApp", LogLevel.DEBUG);
        
        consoleLogger.trace("This is a trace message");
        consoleLogger.debug("Debugging information");
        consoleLogger.info("Application started");
        consoleLogger.warn("Low memory warning");
        consoleLogger.error("Failed to connect to database", new RuntimeException("Connection timeout"));
        
        String logDir = System.getProperty("java.io.tmpdir") + "/est-logs";
        File logFile = new File(logDir, "app.log");
        logFile.getParentFile().mkdirs();
        
        Logger fileLogger = FileLogs.getLogger("FileApp", logFile);
        fileLogger.info("This goes to file: " + logFile.getAbsolutePath());
        fileLogger.warn("Warning in file log");
        
        System.out.println("Logs written to console and file");
    }
    
    private static void enhancedSecurityExample() {
        System.out.println("\n--- Enhanced Security Example ---");
        
        UserDetailsService userDetailsService = BasicSecurity.inMemoryUserDetailsService();
        
        System.out.println("UserDetailsService created");
        
        System.out.println("\n--- JWT Example ---");
        JwtTokenProvider tokenProvider = JwtSecurity.tokenProvider("my-super-secret-key-12345");
        
        User jwtUser = JwtSecurity.newUser("jane", "password123");
        String token = tokenProvider.generateToken(jwtUser);
        System.out.println("Generated JWT Token: " + token.substring(0, Math.min(30, token.length())) + "...");
        
        Optional<Token> validatedToken = tokenProvider.validateToken(token);
        if (validatedToken.isPresent()) {
            System.out.println("JWT Token valid");
            System.out.println("JWT Subject: " + tokenProvider.getSubject(token));
        }
    }
    
    private static void enhancedSchedulerExample() throws InterruptedException {
        System.out.println("\n--- Enhanced Scheduler Example ---");
        
        CountDownLatch latch = new CountDownLatch(3);
        
        Scheduler fixedRateScheduler = FixedRateSchedulers.create();
        fixedRateScheduler.start();
        
        System.out.println("Starting fixed rate task (every 500ms)...");
        fixedRateScheduler.scheduleAtFixedRate(FixedRateSchedulers.wrap(() -> {
            System.out.println("Fixed rate task executed at: " + System.currentTimeMillis());
            latch.countDown();
        }), 0, 500, TimeUnit.MILLISECONDS);
        
        latch.await(3, TimeUnit.SECONDS);
        fixedRateScheduler.stop();
        System.out.println("Fixed rate scheduler stopped");
        
        System.out.println("\n--- Cron Scheduler Example ---");
        Scheduler cronScheduler = CronSchedulers.create();
        cronScheduler.start();
        
        CountDownLatch cronLatch = new CountDownLatch(1);
        System.out.println("Starting cron task...");
        
        cronScheduler.scheduleCron(CronSchedulers.wrap(() -> {
            System.out.println("Cron task executed at: " + System.currentTimeMillis());
            cronLatch.countDown();
        }), "* * * * * ?");
        
        cronLatch.await(2, TimeUnit.SECONDS);
        cronScheduler.stop();
        System.out.println("Cron scheduler stopped");
    }
    
    private static void enhancedEventExample() throws InterruptedException {
        System.out.println("\n--- Enhanced Event Example ---");
        
        EventBus localEventBus = LocalEvents.newLocalEventBus();
        
        CountDownLatch localLatch = new CountDownLatch(2);
        
        localEventBus.subscribe("test-event", new EventListener<String>() {
            @Override
            public void onEvent(ltd.idcu.est.features.event.api.Event event, String data) {
                System.out.println("Local listener 1 received: " + data);
                localLatch.countDown();
            }
        });
        
        localEventBus.subscribe("test-event", (event, data) -> {
            System.out.println("Local listener 2 received: " + data);
            localLatch.countDown();
        });
        
        localEventBus.publish("test-event", "Hello from local event bus!");
        localLatch.await(1, TimeUnit.SECONDS);
        
        System.out.println("\n--- Async Event Example ---");
        EventBus asyncEventBus = AsyncEvents.newAsyncEventBus();
        
        CountDownLatch asyncLatch = new CountDownLatch(3);
        
        asyncEventBus.subscribe("async-event", (event, data) -> {
            System.out.println("Async listener received: " + data + 
                             " on thread: " + Thread.currentThread().getName());
            asyncLatch.countDown();
        });
        
        for (int i = 0; i < 3; i++) {
            asyncEventBus.publish("async-event", "Async event " + i);
        }
        
        asyncLatch.await(2, TimeUnit.SECONDS);
        System.out.println("All async events processed");
    }
}
