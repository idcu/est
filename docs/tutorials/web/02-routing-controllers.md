# Web 教程 02: 路由与控制器

在本教程中，你将学习 EST Web 框架的路由和控制器系统。

## 路由基础

### HTTP 方法

EST 支持所有标准的 HTTP 方法：

```java
package com.example.web;

import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class BasicRoutingExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // GET 请求
        app.getRouter().get("/users", (req, res) -> {
            res.text("List all users");
        });
        
        // POST 请求
        app.getRouter().post("/users", (req, res) -> {
            res.status(201).text("User created");
        });
        
        // PUT 请求
        app.getRouter().put("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("Update user " + id);
        });
        
        // PATCH 请求
        app.getRouter().patch("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("Patch user " + id);
        });
        
        // DELETE 请求
        app.getRouter().delete("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("Delete user " + id);
        });
        
        // HEAD 请求
        app.getRouter().head("/users/:id", (req, res) -> {
            res.status(200);
        });
        
        // OPTIONS 请求
        app.getRouter().options("/users", (req, res) -> {
            res.header("Allow", "GET,POST,OPTIONS");
            res.status(200);
        });
        
        app.run(8080);
    }
}
```

## 路径参数

### 基本路径参数

```java
app.getRouter().get("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    res.text("User ID: " + id);
});
// 匹配: /users/123, /users/abc
```

### 多个路径参数

```java
app.getRouter().get("/users/:userId/posts/:postId", (req, res) -> {
    String userId = req.getPathVariable("userId");
    String postId = req.getPathVariable("postId");
    res.json(Map.of(
        "userId", userId,
        "postId", postId
    ));
});
// 匹配: /users/123/posts/456
```

### 可选路径参数

```java
app.getRouter().get("/users/:id?", (req, res) -> {
    String id = req.getPathVariable("id");
    if (id == null) {
        res.text("All users");
    } else {
        res.text("User: " + id);
    }
});
// 匹配: /users, /users/123
```

## 查询参数

```java
app.getRouter().get("/search", (req, res) -> {
    // 获取查询参数
    String query = req.getParameter("q");
    int page = req.getIntParameter("page", 1);
    int limit = req.getIntParameter("limit", 10);
    String sort = req.getParameter("sort", "id");
    
    res.json(Map.of(
        "query", query,
        "page", page,
        "limit", limit,
        "sort", sort
    ));
});
// 访问: /search?q=test&page=2&limit=20&sort=name
```

## 路由组

### 基本路由组

```java
app.getRouter().group("/api", (group) -> {
    // 这些路由的前缀都是 /api
    group.get("/users", (req, res) -> res.text("API Users"));
    group.post("/users", (req, res) -> res.text("Create User"));
    group.get("/users/:id", (req, res) -> res.text("Get User " + req.getPathVariable("id")));
});
// 路由: /api/users, /api/users/123
```

### 嵌套路由组

```java
app.getRouter().group("/api", (api) -> {
    api.group("/v1", (v1) -> {
        v1.get("/users", (req, res) -> res.text("API v1 Users"));
    });
    
    api.group("/v2", (v2) -> {
        v2.get("/users", (req, res) -> res.text("API v2 Users"));
    });
});
// 路由: /api/v1/users, /api/v2/users
```

### 路由组中间件

```java
app.getRouter().group("/api", (group) -> {
    // 应用到整个组的中间件
    group.use(new AuthMiddleware());
    group.use(new LoggingMiddleware());
    
    group.get("/users", (req, res) -> res.text("Protected Users"));
    group.get("/admin", (req, res) -> res.text("Admin only"));
});
```

## 控制器

### 创建控制器类

```java
package com.example.web;

import ltd.idcu.est.web.api.Controller;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class UserController implements Controller {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void handle(Request request, Response response) {
        String method = request.getMethod().name();
        String path = request.getPath();
        
        switch (method) {
            case "GET":
                if (path.matches("/users/\\d+")) {
                    getUser(request, response);
                } else {
                    listUsers(request, response);
                }
                break;
            case "POST":
                createUser(request, response);
                break;
            case "PUT":
                updateUser(request, response);
                break;
            case "DELETE":
                deleteUser(request, response);
                break;
            default:
                response.status(405).text("Method Not Allowed");
        }
    }
    
    private void listUsers(Request request, Response response) {
        List<User> users = userService.findAll();
        response.json(users);
    }
    
    private void getUser(Request request, Response response) {
        String id = request.getPathVariable("id");
        User user = userService.findById(id);
        if (user != null) {
            response.json(user);
        } else {
            response.status(404).json(Map.of("error", "User not found"));
        }
    }
    
    private void createUser(Request request, Response response) {
        User user = parseUserFromRequest(request);
        userService.create(user);
        response.status(201).json(user);
    }
    
    private void updateUser(Request request, Response response) {
        String id = request.getPathVariable("id");
        User user = parseUserFromRequest(request);
        user.setId(id);
        userService.update(user);
        response.json(user);
    }
    
    private void deleteUser(Request request, Response response) {
        String id = request.getPathVariable("id");
        userService.delete(id);
        response.status(204);
    }
    
    private User parseUserFromRequest(Request request) {
        // 解析请求体
        return new User();
    }
}
```

### 注册控制器

```java
package com.example.web;

public class ControllerExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        UserService userService = new UserServiceImpl();
        UserController userController = new UserController(userService);
        
        // 注册控制器
        app.controller("/users", userController);
        
        app.run(8080);
    }
}
```

## 请求体处理

### JSON 请求体

```java
app.getRouter().post("/users", (req, res) -> {
    // 获取 JSON 请求体
    Map<String, Object> body = req.getJsonBody();
    
    String name = (String) body.get("name");
    String email = (String) body.get("email");
    
    // 创建用户
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    
    res.status(201).json(user);
});
```

### 表单数据

```java
app.getRouter().post("/submit-form", (req, res) -> {
    // 获取表单数据
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    String email = req.getParameter("email");
    
    // 处理表单
    res.text("Form submitted: " + username);
});
```

### 原始请求体

```java
app.getRouter().post("/raw", (req, res) -> {
    // 获取原始请求体
    String body = req.getBody();
    
    // 处理原始数据
    res.text("Received: " + body);
});
```

## 响应处理

### JSON 响应

```java
app.getRouter().get("/api/users", (req, res) -> {
    List<User> users = userService.findAll();
    res.json(users);
});

app.getRouter().get("/api/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    User user = userService.findById(id);
    
    if (user != null) {
        res.json(Map.of(
            "success", true,
            "data", user
        ));
    } else {
        res.status(404).json(Map.of(
            "success", false,
            "error", "User not found"
        ));
    }
});
```

### HTML 响应

```java
app.getRouter().get("/", (req, res) -> {
    res.html("""
        <!DOCTYPE html>
        <html>
        <head>
            <title>Home</title>
        </head>
        <body>
            <h1>Welcome!</h1>
            <p>This is a HTML response.</p>
        </body>
        </html>
        """);
});
```

### 重定向

```java
app.getRouter().get("/old-page", (req, res) -> {
    res.redirect("/new-page");
});

app.getRouter().get("/new-page", (req, res) -> {
    res.text("This is the new page");
});
```

### 设置响应头

```java
app.getRouter().get("/download", (req, res) -> {
    res.header("Content-Type", "application/pdf")
       .header("Content-Disposition", "attachment; filename=\"report.pdf\"")
       .body(pdfContent);
});
```

## RESTful API 示例

```java
package com.example.web;

public class RestApiExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        UserService userService = new UserServiceImpl();
        
        // RESTful API 设计
        app.getRouter().group("/api/v1", (api) -> {
            // 用户资源
            api.get("/users", (req, res) -> {
                List<User> users = userService.findAll();
                res.json(users);
            });
            
            api.get("/users/:id", (req, res) -> {
                String id = req.getPathVariable("id");
                User user = userService.findById(id);
                if (user != null) {
                    res.json(user);
                } else {
                    res.status(404).json(Map.of("error", "User not found"));
                }
            });
            
            api.post("/users", (req, res) -> {
                User user = parseUser(req);
                userService.create(user);
                res.status(201).json(user);
            });
            
            api.put("/users/:id", (req, res) -> {
                String id = req.getPathVariable("id");
                User user = parseUser(req);
                user.setId(id);
                userService.update(user);
                res.json(user);
            });
            
            api.delete("/users/:id", (req, res) -> {
                String id = req.getPathVariable("id");
                userService.delete(id);
                res.status(204);
            });
        });
        
        app.run(8080);
        System.out.println("REST API running at http://localhost:8080/api/v1");
    }
    
    private static User parseUser(Request req) {
        Map<String, Object> body = req.getJsonBody();
        User user = new User();
        user.setName((String) body.get("name"));
        user.setEmail((String) body.get("email"));
        return user;
    }
}
```

## 最佳实践

1. **使用 RESTful 设计**
   - 使用名词表示资源（/users, /products）
   - 使用 HTTP 方法表示动作
   - 使用适当的状态码

2. **组织路由**
   - 使用路由组组织相关路由
   - 版本化 API（/api/v1, /api/v2）

3. **验证输入**
   - 验证路径参数
   - 验证查询参数
   - 验证请求体

4. **统一响应格式**
   - 成功响应：`{ "success": true, "data": ... }`
   - 错误响应：`{ "success": false, "error": ... }`

## 下一步

在下一个教程中，我们将学习 [中间件](./03-middleware.md)。
