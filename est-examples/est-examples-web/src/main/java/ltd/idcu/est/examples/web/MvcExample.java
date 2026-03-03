package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.impl.DefaultWebApplication;

public class MvcExample {
    public static void main(String[] args) {
        // 创建Web应用实例
        WebApplication app = DefaultWebApplication.create();
        
        // 模型 - 模拟用户数据
        class User {
            private int id;
            private String name;
            private String email;
            
            public User(int id, String name, String email) {
                this.id = id;
                this.name = name;
                this.email = email;
            }
            
            public int getId() { return id; }
            public String getName() { return name; }
            public String getEmail() { return email; }
        }
        
        User[] users = {
            new User(1, "John Doe", "john@example.com"),
            new User(2, "Jane Smith", "jane@example.com"),
            new User(3, "Bob Johnson", "bob@example.com")
        };
        
        // 控制器 - 处理请求
        app.router()
            // 首页
            .route(HttpMethod.GET, "/", (request, response) -> {
                String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST MVC Example</title>
                </head>
                <body>
                    <h1>Welcome to EST MVC Example</h1>
                    <p><a href="/users">View Users</a></p>
                </body>
                </html>
                """;
                response.body(html);
                response.header("Content-Type", "text/html");
                response.status(200);
            })
            // 用户列表页
            .route(HttpMethod.GET, "/users", (request, response) -> {
                StringBuilder html = new StringBuilder("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>User List</title>
                </head>
                <body>
                    <h1>User List</h1>
                    <ul>
                """);
                
                for (User user : users) {
                    html.append("<li><a href='/users/" + user.getId() + "'>" + user.getName() + "</a></li>");
                }
                
                html.append("""
                    </ul>
                    <p><a href="/">Back to Home</a></p>
                </body>
                </html>
                """);
                
                response.body(html.toString());
                response.header("Content-Type", "text/html");
                response.status(200);
            })
            // 用户详情页
            .route(HttpMethod.GET, "/users/{id}", (request, response) -> {
                String idStr = request.pathParam("id");
                try {
                    int id = Integer.parseInt(idStr);
                    for (User user : users) {
                        if (user.getId() == id) {
                            String html = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <title>User Details</title>
                            </head>
                            <body>
                                <h1>User Details</h1>
                                <p><strong>ID:</strong> """ + user.getId() + """
                                <p><strong>Name:</strong> """ + user.getName() + """
                                <p><strong>Email:</strong> """ + user.getEmail() + """
                                <p><a href="/users">Back to User List</a></p>
                                <p><a href="/">Back to Home</a></p>
                            </body>
                            </html>
                            """;
                            response.body(html);
                            response.header("Content-Type", "text/html");
                            response.status(200);
                            return;
                        }
                    }
                    response.body("User not found");
                    response.status(404);
                } catch (NumberFormatException e) {
                    response.body("Invalid user ID");
                    response.status(400);
                }
            });
        
        // 启动服务器
        app.server(8082).start();
        System.out.println("MVC application started on port 8082");
        System.out.println("Visit http://localhost:8082 to see the example");
    }
}