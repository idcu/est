# EST 框架实际使用示例

本文档提供 EST 框架在实际项目中的使用示例，涵盖常见场景和最佳实践。

---

## 目录

1. [Web应用示例](#1-web应用示例)
2. [RESTful API示例](#2-restful-api示例)
3. [数据访问示例](#3-数据访问示例)
4. [缓存应用示例](#4-缓存应用示例)
5. [事件驱动示例](#5-事件驱动示例)
6. [安全认证示例](#6-安全认证示例)
7. [任务调度示例](#7-任务调度示例)
8. [消息队列示例](#8-消息队列示例)
9. [监控与日志示例](#9-监控与日志示例)
10. [AI集成示例](#10-ai集成示例)

---

## 1. Web应用示例

### 1.1 简单博客系统

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlogApplication {
    
    private static final Map<String, BlogPost> posts = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        WebApplication app = Web.create("My Blog", "1.0.0");
        
        app.get("/", (req, res) -> res.render("index", Map.of("posts", posts.values())));
        
        app.get("/posts/new", (req, res) -> res.render("new-post"));
        
        app.post("/posts", (req, res) -> {
            String title = req.param("title");
            String content = req.param("content");
            String id = UUID.randomUUID().toString();
            posts.put(id, new BlogPost(id, title, content, new Date()));
            res.redirect("/posts/" + id);
        });
        
        app.get("/posts/:id", (req, res) -> {
            String id = req.param("id");
            BlogPost post = posts.get(id);
            if (post != null) {
                res.render("post", Map.of("post", post));
            } else {
                res.status(404).send("Post not found");
            }
        });
        
        app.run(8080);
    }
    
    static class BlogPost {
        String id;
        String title;
        String content;
        Date createdAt;
        
        BlogPost(String id, String title, String content, Date createdAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
```

### 1.2 待办事项应用

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TodoApplication {
    
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        TodoRepository repository = new InMemoryTodoRepository();
        TodoService service = new TodoService(repository);
        container.registerSingleton(TodoService.class, service);
        
        WebApplication app = Web.create("Todo App", "1.0.0", container);
        
        app.get("/", (req, res) -> {
            List<Todo> todos = service.getAllTodos();
            res.render("index", Map.of("todos", todos));
        });
        
        app.post("/todos", (req, res) -> {
            String title = req.param("title");
            String description = req.param("description");
            service.createTodo(title, description);
            res.redirect("/");
        });
        
        app.post("/todos/:id/complete", (req, res) -> {
            String id = req.param("id");
            service.completeTodo(id);
            res.redirect("/");
        });
        
        app.delete("/todos/:id", (req, res) -> {
            String id = req.param("id");
            service.deleteTodo(id);
            res.status(204).send("");
        });
        
        app.run(8080);
    }
}

class Todo {
    String id;
    String title;
    String description;
    boolean completed;
    Date createdAt;
}

interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(String id);
    List<Todo> findAll();
    void deleteById(String id);
}

class InMemoryTodoRepository implements TodoRepository {
    private final Map<String, Todo> todos = new ConcurrentHashMap<>();
    
    @Override
    public Todo save(Todo todo) {
        if (todo.id == null) {
            todo.id = UUID.randomUUID().toString();
            todo.createdAt = new Date();
        }
        todos.put(todo.id, todo);
        return todo;
    }
    
    @Override
    public Optional<Todo> findById(String id) {
        return Optional.ofNullable(todos.get(id));
    }
    
    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }
    
    @Override
    public void deleteById(String id) {
        todos.remove(id);
    }
}

class TodoService {
    private final TodoRepository repository;
    
    TodoService(TodoRepository repository) {
        this.repository = repository;
    }
    
    public Todo createTodo(String title, String description) {
        Todo todo = new Todo();
        todo.title = title;
        todo.description = description;
        todo.completed = false;
        return repository.save(todo);
    }
    
    public void completeTodo(String id) {
        repository.findById(id).ifPresent(todo -> {
            todo.completed = true;
            repository.save(todo);
        });
    }
    
    public void deleteTodo(String id) {
        repository.deleteById(id);
    }
    
    public List<Todo> getAllTodos() {
        return repository.findAll();
    }
}
```

---

## 2. RESTful API示例

### 2.1 用户管理API

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserApiApplication {
    
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        WebApplication app = Web.create("User API", "1.0.0");
        
        app.get("/api/users", (req, res) -> {
            int page = Integer.parseInt(req.param("page", "1"));
            int size = Integer.parseInt(req.param("size", "10"));
            List<User> userList = new ArrayList<>(users.values());
            int start = (page - 1) * size;
            int end = Math.min(start + size, userList.size());
            res.json(Map.of(
                "data", userList.subList(start, end),
                "page", page,
                "size", size,
                "total", userList.size()
            ));
        });
        
        app.get("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            User user = users.get(id);
            if (user != null) {
                res.json(user);
            } else {
                res.status(404).json(Map.of("error", "User not found"));
            }
        });
        
        app.post("/api/users", (req, res) -> {
            User user = req.body(User.class);
            if (user.email == null || user.name == null) {
                res.status(400).json(Map.of("error", "Name and email are required"));
                return;
            }
            user.id = UUID.randomUUID().toString();
            user.createdAt = new Date();
            users.put(user.id, user);
            res.status(201).json(user);
        });
        
        app.put("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            User existingUser = users.get(id);
            if (existingUser == null) {
                res.status(404).json(Map.of("error", "User not found"));
                return;
            }
            User update = req.body(User.class);
            if (update.name != null) existingUser.name = update.name;
            if (update.email != null) existingUser.email = update.email;
            existingUser.updatedAt = new Date();
            res.json(existingUser);
        });
        
        app.delete("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            if (users.remove(id) != null) {
                res.status(204).send("");
            } else {
                res.status(404).json(Map.of("error", "User not found"));
            }
        });
        
        app.run(8080);
    }
    
    static class User {
        String id;
        String name;
        String email;
        Date createdAt;
        Date updatedAt;
    }
}
```

---

## 更多内容...

（文档剩余部分省略，完整文档请参见原始文件）
