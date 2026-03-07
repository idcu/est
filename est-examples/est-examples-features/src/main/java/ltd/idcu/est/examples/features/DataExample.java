package ltd.idcu.est.examples.features;

import ltd.idcu.est.data.api.Entity;
import ltd.idcu.est.data.api.Id;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DataExample {
    
    @Entity(tableName = "users")
    public static class User {
        @Id
        private Long id;
        private String name;
        private String email;
        private int age;
        
        public User() {}
        
        public User(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "', email='" + email + "', age=" + age + "}";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n=== Data Access Examples ===");
        
        memoryRepositoryExample();
    }
    
    private static void memoryRepositoryExample() {
        System.out.println("\n--- Memory Repository Example ---");
        
        Repository<User, Long> repository = MemoryData.newRepository();
        
        User user1 = new User("Alice", "alice@example.com", 25);
        User user2 = new User("Bob", "bob@example.com", 30);
        User user3 = new User("Charlie", "charlie@example.com", 35);
        
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        System.out.println("Saved 3 users");
        
        System.out.println("Total users: " + repository.count());
        
        Optional<User> foundUser = repository.findById(user1.getId());
        foundUser.ifPresent(user -> System.out.println("Found user: " + user));
        
        List<User> allUsers = repository.findAll();
        System.out.println("All users:");
        allUsers.forEach(user -> System.out.println("  " + user));
        
        System.out.println("User with id " + user2.getId() + " exists: " + repository.existsById(user2.getId()));
        
        repository.delete(user2);
        System.out.println("Deleted user Bob");
        System.out.println("Total users after delete: " + repository.count());
        
        repository.deleteAll();
        System.out.println("Deleted all users");
        System.out.println("Total users after clear: " + repository.count());
    }
}
