package ltd.idcu.est.examples.basic.web;

import ltd.idcu.est.web.WebApplication;

public class Web02_Router {
    public static void main(String[] args) {
        System.out.println("=== 路由系统示例 ===");
        System.out.println();

        WebApplication app = WebApplication.create("路由示例", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("首页");
        });
        
        app.get("/about", (req, res) -> {
            res.send("关于我们");
        });
        
        app.get("/contact", (req, res) -> {
            res.send("联系方式");
        });
        
        app.get("/user/:id", (req, res) -> {
            String userId = req.getPathParam("id");
            res.send("用户ID: " + userId);
        });
        
        System.out.println("服务器已启动，访问: http://localhost:8080");
        app.run(8080);
    }
}
