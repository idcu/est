package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ComponentScanExample {

    public interface MessageService {
        String getMessage();
    }

    @Service("greeting")
    public static class GreetingService implements MessageService {
        @Override
        public String getMessage() {
            return "Hello from GreetingService!";
        }
    }

    @Component(scope = Scope.PROTOTYPE)
    public static class NotificationService implements MessageService {
        @Override
        public String getMessage() {
            return "Notification from NotificationService!";
        }
    }

    @Component
    public static class MessageProcessor {
        private final MessageService greetingService;

        @Inject
        public MessageProcessor(@ltd.idcu.est.core.api.annotation.Qualifier("greeting") MessageService greetingService) {
            this.greetingService = greetingService;
        }

        public String process() {
            return "Processed: " + greetingService.getMessage();
        }

        @PostConstruct
        public void init() {
            System.out.println("MessageProcessor initialized!");
        }
    }

    public static void main(String[] args) {
        Container container = new DefaultContainer();

        System.out.println("=== Scanning components ===");
        container.scan("ltd.idcu.est.examples.basic.ComponentScanExample");

        System.out.println("\n=== Getting MessageProcessor ===");
        MessageProcessor processor = container.get(MessageProcessor.class);
        System.out.println(processor.process());

        System.out.println("\n=== Getting GreetingService ===");
        MessageService greetingService = container.get(MessageService.class, "greeting");
        System.out.println(greetingService.getMessage());

        System.out.println("\n=== Getting NotificationService (prototype) ===");
        NotificationService notification1 = container.get(NotificationService.class);
        NotificationService notification2 = container.get(NotificationService.class);
        System.out.println("Notification 1: " + notification1.getMessage());
        System.out.println("Notification 2: " + notification2.getMessage());
        System.out.println("Are they the same instance? " + (notification1 == notification2));

        container.close();
    }
}
