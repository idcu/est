package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.impl.DefaultWebApplication;

public class BasicWebAppExample {
    public static void main(String[] args) {
        // 创建Web应用实例
        WebApplication app = DefaultWebApplication.create();
        
        // 注册路由
        app.router()
            .route(HttpMethod.GET, "/", (request, response) -> {
                response.body("Hello, EST Web!");
                response.status(200);
            })
            .route(HttpMethod.GET, "/about", (request, response) -> {
                response.body("This is a basic web application example using EST framework.");
                response.status(200);
            });
        
        // 启动服务器
        app.server(8080).start();
        System.out.println("Basic web application started on port 8080");
        System.out.println("Visit http://localhost:8080 to see the example");
    }
}