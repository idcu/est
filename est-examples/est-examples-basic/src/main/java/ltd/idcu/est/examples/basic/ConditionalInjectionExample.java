package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.ConditionalOnProperty;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ConditionalInjectionExample {

    public interface NotificationService {
        void send(String message);
    }

    @Component
    @ConditionalOnProperty(name = "notification.type", havingValue = "email")
    public static class EmailNotificationService implements NotificationService {
        @Override
        public void send(String message) {
            System.out.println("[Email] Sending message: " + message);
        }
    }

    @Component
    @ConditionalOnProperty(name = "notification.type", havingValue = "sms")
    public static class SmsNotificationService implements NotificationService {
        @Override
        public void send(String message) {
            System.out.println("[SMS] Sending message: " + message);
        }
    }

    @Component
    public static class UserService {
        private final NotificationService notificationService;

        @Inject
        public UserService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        public void registerUser(String username) {
            System.out.println("Registering user: " + username);
            notificationService.send("Welcome, " + username + "!");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Conditional Injection Example ===\n");

        Config emailConfig = new DefaultConfig();
        emailConfig.set("notification.type", "email");

        Container emailContainer = new DefaultContainer(emailConfig);
        emailContainer.scan("ltd.idcu.est.examples.basic");

        System.out.println("1. Testing with notification.type=email:");
        UserService emailUserService = emailContainer.get(UserService.class);
        emailUserService.registerUser("Alice");

        emailContainer.close();

        System.out.println("\n2. Testing with notification.type=sms:");

        Config smsConfig = new DefaultConfig();
        smsConfig.set("notification.type", "sms");

        Container smsContainer = new DefaultContainer(smsConfig);
        smsContainer.scan("ltd.idcu.est.examples.basic");

        UserService smsUserService = smsContainer.get(UserService.class);
        smsUserService.registerUser("Bob");

        smsContainer.close();

        System.out.println("\n=== Example completed ===");
    }
}
