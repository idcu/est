package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.DefaultContainer;

public class EnhancedContainerExample {

    interface GreetingService {
        String greet(String name);
    }

    static class EnglishGreetingService implements GreetingService {
        @Override
        public String greet(String name) {
            return "Hello, " + name + "!";
        }
    }

    static class ChineseGreetingService implements GreetingService {
        @Override
        public String greet(String name) {
            return "你好, " + name + "!";
        }
    }

    interface MessageRepository {
        String getMessage();
    }

    static class DefaultMessageRepository implements MessageRepository {
        @Override
        public String getMessage() {
            return "Welcome to EST Framework!";
        }
    }

    static class AppService {
        private final MessageRepository repository;
        private final GreetingService greetingService;

        @Inject
        public AppService(MessageRepository repository, @Qualifier("chinese") GreetingService greetingService) {
            this.repository = repository;
            this.greetingService = greetingService;
        }

        public String getWelcomeMessage(String userName) {
            return greetingService.greet(userName) + " " + repository.getMessage();
        }
    }

    static class PrototypeCounter {
        private static int instanceCount = 0;
        private final int id;

        public PrototypeCounter() {
            this.id = ++instanceCount;
        }

        public int getId() {
            return id;
        }
    }

    public static void run() {
        System.out.println("=== Enhanced Container Example ===\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. Constructor Injection:");
        container.register(MessageRepository.class, DefaultMessageRepository.class);
        container.register(GreetingService.class, EnglishGreetingService.class, "english");
        container.register(GreetingService.class, ChineseGreetingService.class, "chinese");
        container.register(AppService.class, AppService.class);
        
        AppService appService = container.get(AppService.class);
        System.out.println(appService.getWelcomeMessage("张三"));
        System.out.println();
        
        System.out.println("2. Qualifier Support:");
        GreetingService englishService = container.get(GreetingService.class, "english");
        GreetingService chineseService = container.get(GreetingService.class, "chinese");
        System.out.println("English: " + englishService.greet("John"));
        System.out.println("Chinese: " + chineseService.greet("李四"));
        System.out.println();
        
        System.out.println("3. Scope Support - Singleton:");
        container.register(PrototypeCounter.class, PrototypeCounter.class, Scope.SINGLETON);
        PrototypeCounter singleton1 = container.get(PrototypeCounter.class);
        PrototypeCounter singleton2 = container.get(PrototypeCounter.class);
        System.out.println("Singleton instance 1 ID: " + singleton1.getId());
        System.out.println("Singleton instance 2 ID: " + singleton2.getId());
        System.out.println("Are same instance? " + (singleton1 == singleton2));
        System.out.println();
        
        System.out.println("4. Scope Support - Prototype:");
        container.register(PrototypeCounter.class, PrototypeCounter.class, Scope.PROTOTYPE);
        PrototypeCounter prototype1 = container.get(PrototypeCounter.class);
        PrototypeCounter prototype2 = container.get(PrototypeCounter.class);
        System.out.println("Prototype instance 1 ID: " + prototype1.getId());
        System.out.println("Prototype instance 2 ID: " + prototype2.getId());
        System.out.println("Are same instance? " + (prototype1 == prototype2));
        System.out.println();
        
        System.out.println("5. Create Method:");
        AppService createdService = container.create(AppService.class);
        System.out.println(createdService.getWelcomeMessage("王五"));
        System.out.println();
        
        System.out.println("6. getIfPresent and contains:");
        System.out.println("Contains MessageRepository? " + container.contains(MessageRepository.class));
        System.out.println("Contains NonExistentService? " + container.contains(NonExistentService.class));
        System.out.println("getIfPresent MessageRepository: " + container.getIfPresent(MessageRepository.class).isPresent());
        System.out.println("getIfPresent NonExistentService: " + container.getIfPresent(NonExistentService.class).isPresent());
        System.out.println();
        
        System.out.println("7. Clear Container:");
        System.out.println("Before clear - contains MessageRepository? " + container.contains(MessageRepository.class));
        container.clear();
        System.out.println("After clear - contains MessageRepository? " + container.contains(MessageRepository.class));
    }

    interface NonExistentService {}
}
