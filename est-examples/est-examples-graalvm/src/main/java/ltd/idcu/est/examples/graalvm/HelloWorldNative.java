package ltd.idcu.est.examples.graalvm;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;

@Component
class GreetingService {
    public String greet(String name) {
        return "Hello, " + name + " from EST GraalVM Native!";
    }
}

@Component
class Application {
    @Inject
    private GreetingService greetingService;

    public void run() {
        System.out.println(greetingService.greet("World"));
        System.out.println("EST Framework running in GraalVM Native Image mode!");
    }
}

public class HelloWorldNative {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        Container container = new DefaultContainer();
        container.scan("ltd.idcu.est.examples.graalvm");
        
        Application app = container.get(Application.class);
        app.run();
        
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }
}
