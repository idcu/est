package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.impl.DefaultWebApplication;

public class RestApiExample {
    public static void main(String[] args) {
        // 创建Web应用实例
        WebApplication app = DefaultWebApplication.create();
        
        // 模拟用户数据
        class User {
            private int id;
            private String name;
            private String email;
            
            public User(int id, String name, String email) {
                this.id = id;
                this.name = name;
                this.email = email;
            }
            
            @Override
            public String toString() {
                return "{\"id\": " + id + ", \"name\": \"" + name + "\", \"email\": \"" + email + "\"}";
            }
        }
        
        User[] users = {
            new User(1, "John Doe", "john@example.com"),
            new User(2, "Jane Smith", "jane@example.com"),
            new User(3, "Bob Johnson", "bob@example.com")
        };
        
        // 注册RESTful API路由
        app.router()
            // 获取所有用户
            .route(HttpMethod.GET, "/api/users", (request, response) -> {
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < users.length; i++) {
                    json.append(users[i].toString());
                    if (i < users.length - 1) json.append(",");
                }
                json.append("]");
                response.body(json.toString());
                response.header("Content-Type", "application/json");
                response.status(200);
            })
            // 获取单个用户
            .route(HttpMethod.GET, "/api/users/{id}", (request, response) -> {
                String idStr = request.pathParam("id");
                try {
                    int id = Integer.parseInt(idStr);
                    for (User user : users) {
                        if (user.id == id) {
                            response.body(user.toString());
                            response.header("Content-Type", "application/json");
                            response.status(200);
                            return;
                        }
                    }
                    response.body("{\"error\": \"User not found\"}");
                    response.header("Content-Type", "application/json");
                    response.status(404);
                } catch (NumberFormatException e) {
                    response.body("{\"error\": \"Invalid user ID\"}");
                    response.header("Content-Type", "application/json");
                    response.status(400);
                }
            })
            // 创建新用户
            .route(HttpMethod.POST, "/api/users", (request, response) -> {
                response.body("{\"message\": \"User created successfully\"}");
                response.header("Content-Type", "application/json");
                response.status(201);
            })
            // 更新用户
            .route(HttpMethod.PUT, "/api/users/{id}", (request, response) -> {
                response.body("{\"message\": \"User updated successfully\"}");
                response.header("Content-Type", "application/json");
                response.status(200);
            })
            // 删除用户
            .route(HttpMethod.DELETE, "/api/users/{id}", (request, response) -> {
                response.body("{\"message\": \"User deleted successfully\"}");
                response.header("Content-Type", "application/json");
                response.status(200);
            });
        
        // 启动服务器
        app.server(8081).start();
        System.out.println("RESTful API application started on port 8081");
        System.out.println("Try: GET http://localhost:8081/api/users");
        System.out.println("Try: GET http://localhost:8081/api/users/1");
    }
}