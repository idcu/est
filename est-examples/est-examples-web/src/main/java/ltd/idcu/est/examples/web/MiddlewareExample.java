package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.Middleware.Next;
import ltd.idcu.est.web.impl.DefaultWebApplication;
import ltd.idcu.est.web.impl.LoggingMiddleware;
import ltd.idcu.est.web.impl.DefaultCorsMiddleware;

public class MiddlewareExample {
    public static void main(String[] args) {
        // 创建Web应用实例
        WebApplication app = DefaultWebApplication.create();
        
        // 自定义中间件 - 认证中间件
        class AuthMiddleware implements Middleware {
            @Override
            public void handle(Request request, Response response, Next next) {
                String token = request.header("Authorization");
                if (token != null && token.equals("Bearer secret-token")) {
                    next.handle();
                } else {
                    response.body("Unauthorized");
                    response.status(401);
                }
            }
        }
        
        // 自定义中间件 - 计时中间件
        class TimingMiddleware implements Middleware {
            @Override
            public void handle(Request request, Response response, Next next) {
                long start = System.currentTimeMillis();
                next.handle();
                long end = System.currentTimeMillis();
                System.out.println("Request to " + request.path() + " took " + (end - start) + "ms");
            }
        }
        
        // 注册中间件
        app.middleware()
            .add(new LoggingMiddleware())  // 日志中间件
            .add(new DefaultCorsMiddleware())  // CORS中间件
            .add(new TimingMiddleware())  // 计时中间件
            .add(new AuthMiddleware());  // 认证中间件
        
        // 注册路由
        app.router()
            .route(HttpMethod.GET, "/", (request, response) -> {
                response.body("Hello, Middleware Example!");
                response.status(200);
            })
            .route(HttpMethod.GET, "/protected", (request, response) -> {
                response.body("This is a protected route");
                response.status(200);
            });
        
        // 启动服务器
        app.server(8083).start();
        System.out.println("Middleware application started on port 8083");
        System.out.println("Visit http://localhost:8083 (will return 401)");
        System.out.println("Try with Authorization header: Bearer secret-token");
    }
}