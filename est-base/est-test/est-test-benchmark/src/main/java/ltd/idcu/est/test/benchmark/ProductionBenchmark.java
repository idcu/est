package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.api.annotation.Component;
import ltd.idcu.est.core.container.api.annotation.Inject;
import ltd.idcu.est.core.container.api.annotation.Repository;
import ltd.idcu.est.core.container.api.annotation.Service;
import ltd.idcu.est.core.container.impl.DefaultContainer;
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 10, time = 2)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G", "-XX:+UseG1GC"})
@State(Scope.Benchmark)
public class ProductionBenchmark {

    public interface UserService {
        User getUserById(String id);
        void saveUser(User user);
    }

    public interface UserRepository {
        User findById(String id);
        void save(User user);
    }

    public static class User {
        private String id;
        private String name;
        private String email;

        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    @Service
    public static class UserServiceImpl implements UserService {
        private final UserRepository userRepository;

        @Inject
        public UserServiceImpl(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public User getUserById(String id) {
            return userRepository.findById(id);
        }

        @Override
        public void saveUser(User user) {
            userRepository.save(user);
        }
    }

    @Repository
    public static class UserRepositoryImpl implements UserRepository {
        @Override
        public User findById(String id) {
            return new User(id, "Test User", "test@example.com");
        }

        @Override
        public void save(User user) {
        }
    }

    public static class UserCreatedData {
        private final String userId;
        private final String userName;

        public UserCreatedData(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }
    }

    private static final String USER_CREATED_EVENT = "user.created";

    private Container container;
    private EventBus localEventBus;
    private AtomicInteger eventCounter;
    private User testUser;

    @Setup(Level.Trial)
    public void setupTrial() {
        container = new DefaultContainer();
        container.register(UserRepository.class, UserRepositoryImpl.class);
        container.register(UserService.class, UserServiceImpl.class);

        localEventBus = LocalEvents.newLocalEventBus();
        eventCounter = new AtomicInteger(0);
        localEventBus.subscribe(USER_CREATED_EVENT, (event, data) -> eventCounter.incrementAndGet());

        testUser = new User("123", "John Doe", "john@example.com");
    }

    @Benchmark
    public UserService container_getService() {
        return container.get(UserService.class);
    }

    @Benchmark
    public User service_getUser() {
        UserService service = container.get(UserService.class);
        return service.getUserById("123");
    }

    @Benchmark
    public void service_saveUser() {
        UserService service = container.get(UserService.class);
        service.saveUser(testUser);
    }

    @Benchmark
    public void eventbus_publish() {
        localEventBus.publish(USER_CREATED_EVENT, new UserCreatedData("456", "Jane Doe"));
    }

    @Benchmark
    public void container_completeWorkflow() {
        UserService service = container.get(UserService.class);
        User user = service.getUserById("789");
        service.saveUser(user);
        localEventBus.publish(USER_CREATED_EVENT, new UserCreatedData(user.getId(), user.getName()));
    }
}
