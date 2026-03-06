package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class Core08_CollectionIntegration {
    public static void main(String[] args) {
        System.out.println("=== 集成篇：EST Core + EST Collection ===\n");
        
        Container container = new DefaultContainer();
        
        container.register(UserRepository.class, UserRepository.class);
        container.register(UserService.class, UserService.class);
        container.register(UserController.class, UserController.class);
        
        UserController controller = container.get(UserController.class);
        
        System.out.println("--- 所有成年用户 ---");
        controller.printAdultUsers();
        
        System.out.println("\n--- 按年龄排序 ---");
        controller.printUsersSortedByAge();
        
        System.out.println("\n--- 平均年龄 ---");
        controller.printAverageAge();
    }
}

class User {
    private String name;
    private int age;
    private String city;
    
    public User(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    
    @Override
    public String toString() {
        return name + " (" + age + "岁, " + city + ")";
    }
}

@Service
class UserRepository {
    public Seq<User> findAll() {
        return Seqs.of(
            new User("张三", 25, "北京"),
            new User("李四", 17, "上海"),
            new User("王五", 30, "广州"),
            new User("赵六", 16, "深圳"),
            new User("钱七", 28, "北京")
        );
    }
}

@Service
class UserService {
    @Inject
    private UserRepository userRepository;
    
    public Seq<User> getAdultUsers() {
        return userRepository.findAll()
            .where(user -> user.getAge() >= 18);
    }
    
    public Seq<User> getUsersSortedByAge() {
        return userRepository.findAll()
            .sortBy(User::getAge);
    }
    
    public double getAverageAge() {
        return userRepository.findAll()
            .mapToInt(User::getAge)
            .average()
            .orElse(0);
    }
}

@Component
class UserController {
    @Inject
    private UserService userService;
    
    public void printAdultUsers() {
        userService.getAdultUsers().forEach(System.out::println);
    }
    
    public void printUsersSortedByAge() {
        userService.getUsersSortedByAge().forEach(System.out::println);
    }
    
    public void printAverageAge() {
        System.out.println("平均年龄：" + userService.getAverageAge() + " 岁");
    }
}
