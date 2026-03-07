package ltd.idcu.est.examples.microservices.user.controller;

import ltd.idcu.est.examples.microservices.user.model.User;
import ltd.idcu.est.web.api.Get;
import ltd.idcu.est.web.api.Post;
import ltd.idcu.est.web.api.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class UserController {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public UserController() {
        users.put("1", new User("1", "张三", "zhangsan@example.com", 28));
        users.put("2", new User("2", "李四", "lisi@example.com", 32));
        users.put("3", new User("3", "王五", "wangwu@example.com", 25));
    }

    @Get("/users")
    public List<User> getAllUsers() {
        System.out.println("[UserService] GET /users - 返回所有用�?);
        return new ArrayList<>(users.values());
    }

    @Get("/users/{id}")
    public User getUserById(String id) {
        System.out.println("[UserService] GET /users/" + id + " - 获取用户");
        User user = users.get(id);
        if (user == null) {
            throw new RuntimeException("User not found: " + id);
        }
        return user;
    }

    @Post("/users")
    public User createUser(User user) {
        String id = String.valueOf(users.size() + 1);
        user.setId(id);
        users.put(id, user);
        System.out.println("[UserService] POST /users - 创建用户: " + user);
        return user;
    }
}
