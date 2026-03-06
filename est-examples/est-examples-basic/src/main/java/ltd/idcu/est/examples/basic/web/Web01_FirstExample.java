package ltd.idcu.est.examples.basic.web;

import ltd.idcu.est.web.WebApplication;

public class Web01_FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Web 5分钟上手 ===");
        System.out.println();

        WebApplication app = WebApplication.create("我的第一个网站", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, EST Web!");
        });
        
        System.out.println("服务器已启动，访问: http://localhost:8080");
        System.out.println("按 Ctrl+C 停止服务器");
        app.run(8080);
    }
}
