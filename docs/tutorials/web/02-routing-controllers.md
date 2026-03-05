# Web 教程 02: 路由与控制器

在本教程中，我们将学习 EST Web 框架的路由和控制器系统，这是构建 Web 应用的核心部分。

## 前置知识

在开始本教程之前，确保你已经：
- 完成了 [第一个应用教程](../beginner/01-first-app.md)
- 了解了 EST 的基本使用方法

## 什么是路由？

想象一下，路由就像是一个"快递员"，它根据 URL（地址）把请求送到正确的"处理点"。

当用户访问 `http://localhost:8080/users` 时，路由系统会找到对应的代码来处理这个请求。

## 路由基础

### 创建你的第一个路由

让我们从一个简单的例子开始：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class BasicRouting {
    public static void main(String[] args) {
        WebApplication app = Web.create("路由演示", "1.0.0");
        
        // GET 请求 - 获取数据
        app.get("/hello", (req, res) -> {
            res.text("你好，欢迎来到 EST！");
        });
        
        app.run(8080);
        System.out.println("应用已启动，请访问: http://localhost:8080/hello");
    }
}
```

运行这个程序，然后在浏览器访问 `http://localhost:8080/hello`，你会看到"你好，欢迎来到 EST！"的消息。

### HTTP 方法

HTTP 方法就像是不同的"操作类型"，EST 支持所有标准的 HTTP 方法：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HttpMethodsDemo {
    public static void main(String[] args) {
        WebApplication app = Web.create("HTTP方法演示", "1.0.0");
        
        // GET - 获取数据（查看）
        app.get("/users", (req, res) -> {
            res.text("获取所有用户列表");
        });
        
        // POST - 创建数据（新增）
        app.post("/users", (req, res) -> {
            res.status(201).text("创建新用户成功");
        });
        
        // PUT - 更新数据（完整更新）
        app.put("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("更新用户 " + id + " 的全部信息");
        });
        
        // PATCH - 部分更新数据（局部更新）
        app.patch("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("更新用户 " + id + " 的部分信息");
        });
        
        // DELETE - 删除数据
        app.delete("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("删除用户 " + id);
        });
        
        app.run(8080);
        System.out.println("应用已启动，端口 8080");
    }
}
```

## 路径参数

路径参数允许你在 URL 中传递变量值，就像给快递员写地址时加上房间号一样。

### 基本路径参数

```java
// 访问 /users/123 时，id 的值就是 "123"
app.get("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    res.text("用户 ID: " + id);
});
```

### 多个路径参数

```java
// 访问 /users/123/posts/456 时
app.get("/users/:userId/posts/:postId", (req, res) -> {
    String userId = req.getPathVariable("userId");
    String postId = req.getPathVariable("postId");
    
    res.json(Map.of(
        "userId", userId,
        "postId", postId
    ));
});
```

## 查询参数

查询参数是 URL 中 `?` 后面的部分，就像给快递员的额外备注。

```java
// 访问 /search?q=java&page=1&limit=10
app.get("/search", (req, res) -> {
    // 获取查询参数
    String query = req.getParameter("q");
    int page = req.getIntParameter("page", 1);  // 第二个参数是默认值
    int limit = req.getIntParameter("limit", 10);
    String sort = req.getParameter("sort", "id");
    
    res.json(Map.of(
        "query", query,
        "page", page,
        "limit", limit,
        "sort", sort
    ));
});
```

## 路由组

路由组可以帮你把相关的路由组织在一起，就像把同一类快递放在同一个篮子里。

### 基本路由组

```java
app.group("/api", (api) -> {
    // 这些路由都会以 /api 开头
    api.get("/users", (req, res) -> res.text("用户列表"));
    api.post("/users", (req, res) -> res.text("创建用户"));
    api.get("/users/:id", (req, res) -> res.text("用户 " + req.getPathVariable("id")));
});

// 实际路由地址：
// /api/users
// /api/users/123
```

### 嵌套路由组

```java
app.group("/api", (api) -> {
    // API v1 版本
    api.group("/v1", (v1) -> {
        v1.get("/users", (req, res) -> res.text("API v1 - 用户列表"));
    });
    
    // API v2 版本
    api.group("/v2", (v2) -> {
        v2.get("/users", (req, res) -> res.text("API v2 - 用户列表"));
    });
});

// 实际路由地址：
// /api/v1/users
// /api/v2/users
```

## 什么是控制器？

控制器是一种更有组织的方式来处理请求，就像把快递员分成不同的小组，每个小组专门处理一类快递。

### 创建一个简单的控制器

让我们创建一个用户控制器：

```java
import ltd.idcu.est.web.api.RestController;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class UserController implements RestController {
    
    private final Request request;
    private final Response response;
    
    public UserController(Request request, Response response) {
        this.request = request;
        this.response = response;
    }
    
    @Override
    public Request getRequest() {
        return request;
    }
    
    @Override
    public Response getResponse() {
        return response;
    }
    
    // 获取用户列表
    public String list() {
        return json(Map.of(
            "users", List.of(
                Map.of("id", 1, "name", "张三"),
                Map.of("id", 2, "name", "李四")
            )
        ));
    }
    
    // 获取单个用户
    public String get() {
        String id = getPathVariable("id");
        return json(Map.of(
            "id", id,
            "name", "用户" + id
        ));
    }
    
    // 创建用户
    public String create() {
        Map<String, Object> body = request.getJsonBody();
        return created().json(Map.of(
            "success", true,
            "data", body
        ));
    }
}
```

### 使用控制器

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class ControllerDemo {
    public static void main(String[] args) {
        WebApplication app = Web.create("控制器演示", "1.0.0");
        
        // 注册控制器
        app.controller("/users", UserController.class);
        
        app.run(8080);
        System.out.println("应用已启动，端口 8080");
    }
}
```

## 请求体处理

当你需要发送数据到服务器时（比如提交表单或上传 JSON 数据），就需要处理请求体。

### JSON 请求体

```java
app.post("/users", (req, res) -> {
    // 获取 JSON 请求体
    Map<String, Object> body = req.getJsonBody();
    
    String name = (String) body.get("name");
    String email = (String) body.get("email");
    
    res.status(201).json(Map.of(
        "success", true,
        "message", "用户创建成功",
        "data", Map.of(
            "name", name,
            "email", email
        )
    ));
});
```

### 表单数据

```java
app.post("/submit-form", (req, res) -> {
    // 获取表单数据
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    String email = req.getParameter("email");
    
    res.text("表单提交成功！用户名: " + username);
});
```

## 响应处理

服务器需要把处理结果返回给客户端，EST 提供了多种响应方式。

### JSON 响应（最常用）

```java
app.get("/api/users", (req, res) -> {
    List<Map<String, Object>> users = List.of(
        Map.of("id", 1, "name", "张三"),
        Map.of("id", 2, "name", "李四")
    );
    res.json(users);
});

app.get("/api/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    
    if ("1".equals(id)) {
        res.json(Map.of(
            "success", true,
            "data", Map.of("id", 1, "name", "张三")
        ));
    } else {
        res.status(404).json(Map.of(
            "success", false,
            "error", "用户不存在"
        ));
    }
});
```

### HTML 响应

```java
app.get("/", (req, res) -> {
    res.html("""
        <!DOCTYPE html>
        <html>
        <head>
            <title>首页</title>
            <style>
                body { 
                    font-family: Arial, sans-serif; 
                    max-width: 800px; 
                    margin: 50px auto; 
                    padding: 20px;
                }
                h1 { color: #333; }
                .welcome { 
                    background: #f0f0f0; 
                    padding: 20px; 
                    border-radius: 8px;
                }
            </style>
        </head>
        <body>
            <h1>欢迎使用 EST 框架！</h1>
            <div class="welcome">
                <p>这是一个 HTML 响应示例。</p>
            </div>
        </body>
        </html>
        """);
});
```

### 重定向

```java
// 重定向到新页面
app.get("/old-page", (req, res) -> {
    res.redirect("/new-page");
});

app.get("/new-page", (req, res) -> {
    res.text("这是新页面！");
});
```

## 完整示例：待办事项 API

让我们把学到的知识整合起来，创建一个完整的待办事项 API：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoApp {
    
    private static final Map<Integer, Map<String, Object>> todos = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    
    static {
        addTodo("学习 EST 框架", false);
        addTodo("完成第一个项目", false);
        addTodo("写文档", true);
    }
    
    private static void addTodo(String title, boolean completed) {
        int id = idGenerator.getAndIncrement();
        todos.put(id, Map.of(
            "id", id,
            "title", title,
            "completed", completed,
            "createdAt", new Date().toString()
        ));
    }
    
    public static void main(String[] args) {
        WebApplication app = Web.create("待办事项 API", "1.0.0");
        
        app.group("/api", (api) -> {
            // 获取所有待办事项
            api.get("/todos", (req, res) -> {
                res.json(todos.values());
            });
            
            // 获取单个待办事项
            api.get("/todos/:id", (req, res) -> {
                int id = Integer.parseInt(req.getPathVariable("id"));
                Map<String, Object> todo = todos.get(id);
                
                if (todo != null) {
                    res.json(todo);
                } else {
                    res.status(404).json(Map.of(
                        "success", false,
                        "error", "待办事项不存在"
                    ));
                }
            });
            
            // 创建新的待办事项
            api.post("/todos", (req, res) -> {
                Map<String, Object> body = req.getJsonBody();
                String title = (String) body.get("title");
                
                int id = idGenerator.getAndIncrement();
                Map<String, Object> newTodo = Map.of(
                    "id", id,
                    "title", title,
                    "completed", false,
                    "createdAt", new Date().toString()
                );
                todos.put(id, newTodo);
                
                res.status(201).json(newTodo);
            });
            
            // 更新待办事项
            api.put("/todos/:id", (req, res) -> {
                int id = Integer.parseInt(req.getPathVariable("id"));
                Map<String, Object> body = req.getJsonBody();
                
                Map<String, Object> existingTodo = todos.get(id);
                if (existingTodo == null) {
                    res.status(404).json(Map.of(
                        "success", false,
                        "error", "待办事项不存在"
                    ));
                    return;
                }
                
                Map<String, Object> updatedTodo = new HashMap<>(existingTodo);
                if (body.containsKey("title")) {
                    updatedTodo.put("title", body.get("title"));
                }
                if (body.containsKey("completed")) {
                    updatedTodo.put("completed", body.get("completed"));
                }
                
                todos.put(id, updatedTodo);
                res.json(updatedTodo);
            });
            
            // 删除待办事项
            api.delete("/todos/:id", (req, res) -> {
                int id = Integer.parseInt(req.getPathVariable("id"));
                todos.remove(id);
                res.status(204);
            });
        });
        
        app.run(8080);
        System.out.println("待办事项 API 已启动！");
        System.out.println("API 地址: http://localhost:8080/api/todos");
    }
}
```

## 测试 API

你可以使用以下方式测试 API：

1. 使用浏览器测试 GET 请求
2. 使用 curl 命令：
   ```bash
   # 获取所有待办事项
   curl http://localhost:8080/api/todos
   
   # 创建新待办事项
   curl -X POST http://localhost:8080/api/todos \
     -H "Content-Type: application/json" \
     -d "{\"title\": \"新任务\"}"
   
   # 更新待办事项
   curl -X PUT http://localhost:8080/api/todos/1 \
     -H "Content-Type: application/json" \
     -d "{\"completed\": true}"
   
   # 删除待办事项
   curl -X DELETE http://localhost:8080/api/todos/1
   ```

3. 使用 Postman 或其他 API 测试工具

## 最佳实践

1. **使用 RESTful 设计**
   - 使用名词表示资源（/users, /todos）
   - 使用 HTTP 方法表示动作
   - 使用适当的状态码

2. **组织路由**
   - 使用路由组组织相关路由
   - 版本化 API（/api/v1, /api/v2）

3. **统一响应格式**
   - 成功响应：`{ "success": true, "data": ... }`
   - 错误响应：`{ "success": false, "error": ... }`

4. **验证输入**
   - 验证路径参数
   - 验证查询参数
   - 验证请求体

## 小练习

1. 扩展待办事项 API，添加根据完成状态筛选的功能（`/api/todos?completed=true`）
2. 添加优先级字段（low, medium, high）
3. 添加搜索功能（`/api/todos/search?q=关键词`）

## 下一步

恭喜你完成了路由与控制器的学习！现在你已经掌握了 EST Web 开发的核心知识。

接下来，你可以：
- 继续学习其他 Web 开发特性
- 探索 EST 的其他功能模块
- 开始构建你自己的项目！
