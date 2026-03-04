package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.lifecycle.DisposableBean;
import ltd.idcu.est.core.api.lifecycle.InitializingBean;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.api.processor.BeanPostProcessor;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.ArrayList;
import java.util.List;

public class LifecycleContainerExample {

    interface LogService {
        void log(String message);
        List<String> getLogs();
    }

    static class DefaultLogService implements LogService, InitializingBean, DisposableBean {
        private final List<String> logs = new ArrayList<>();
        private boolean initialized = false;
        private boolean destroyed = false;

        @Override
        public void log(String message) {
            logs.add(message);
            System.out.println("[LogService] " + message);
        }

        @Override
        public List<String> getLogs() {
            return new ArrayList<>(logs);
        }

        @PostConstruct
        public void postConstruct() {
            System.out.println("[LogService] @PostConstruct called");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("[LogService] InitializingBean.afterPropertiesSet() called");
            initialized = true;
        }

        @PreDestroy
        public void preDestroy() {
            System.out.println("[LogService] @PreDestroy called");
        }

        @Override
        public void destroy() throws Exception {
            System.out.println("[LogService] DisposableBean.destroy() called");
            destroyed = true;
        }

        public boolean isInitialized() {
            return initialized;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }

    static class UserService {
        private final LogService logService;
        private String status = "created";

        public UserService(LogService logService) {
            this.logService = logService;
            logService.log("UserService instantiated");
        }

        @PostConstruct
        public void init() {
            status = "initialized";
            logService.log("UserService @PostConstruct called");
        }

        @PreDestroy
        public void cleanup() {
            status = "destroyed";
            logService.log("UserService @PreDestroy called");
        }

        public String getStatus() {
            return status;
        }
    }

    static class LoggingBeanPostProcessor implements BeanPostProcessor {
        private final List<String> processedBeans = new ArrayList<>();

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            String message = "BeanPostProcessor.postProcessBeforeInitialization: " + beanName;
            System.out.println("[BeanPostProcessor] " + message);
            processedBeans.add("before:" + beanName);
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            String message = "BeanPostProcessor.postProcessAfterInitialization: " + beanName;
            System.out.println("[BeanPostProcessor] " + message);
            processedBeans.add("after:" + beanName);
            return bean;
        }

        public List<String> getProcessedBeans() {
            return new ArrayList<>(processedBeans);
        }
    }

    public static void run() {
        System.out.println("=== Lifecycle Container Example ===\n");

        Container container = new DefaultContainer();

        LoggingBeanPostProcessor postProcessor = new LoggingBeanPostProcessor();
        container.addBeanPostProcessor(postProcessor);

        System.out.println("1. Registering services...");
        container.register(LogService.class, DefaultLogService.class);
        container.register(UserService.class, UserService.class);
        System.out.println();

        System.out.println("2. Getting LogService...");
        LogService logService = container.get(LogService.class);
        System.out.println();

        System.out.println("3. Getting UserService...");
        UserService userService = container.get(UserService.class);
        System.out.println();

        System.out.println("4. Verifying states:");
        System.out.println("   LogService initialized: " + ((DefaultLogService) logService).isInitialized());
        System.out.println("   UserService status: " + userService.getStatus());
        System.out.println();

        System.out.println("5. BeanPostProcessor processed beans:");
        for (String bean : postProcessor.getProcessedBeans()) {
            System.out.println("   - " + bean);
        }
        System.out.println();

        System.out.println("6. Closing container...");
        container.close();
        System.out.println();

        System.out.println("7. Verifying destroy states:");
        System.out.println("   LogService destroyed: " + ((DefaultLogService) logService).isDestroyed());
        System.out.println("   UserService status: " + userService.getStatus());
        System.out.println();

        System.out.println("8. Logs:");
        for (String log : logService.getLogs()) {
            System.out.println("   - " + log);
        }
    }
}
