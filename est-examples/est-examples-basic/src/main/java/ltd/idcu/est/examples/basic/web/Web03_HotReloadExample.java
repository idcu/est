package ltd.idcu.est.examples.basic.web;

import ltd.idcu.est.web.WebApplication;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Web03_HotReloadExample {
    public static void main(String[] args) {
        System.out.println("=== EST Web 热重载示例 ===");
        System.out.println();

        WebApplication app = WebApplication.create("热重载示例", "1.0.0");
        
        AtomicInteger counter = new AtomicInteger(0);
        
        app.get("/", (req, res) -> {
            String response = """
                <html>
                <head>
                    <title>EST Hot Reload Demo</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                        h1 { color: #333; }
                        .info { background: #f5f5f5; padding: 15px; border-radius: 5px; margin: 10px 0; }
                        button { padding: 10px 20px; font-size: 16px; }
                    </style>
                </head>
                <body>
                    <h1>EST 热重载演示</h1>
                    <div class=\"info\">
                        <p><strong>访问次数:</strong> %d</p>
                        <p><strong>当前时间:</strong> %s</p>
                    </div>
                    <p>修改此文件后保存，观察热重载功能！</p>
                    <button onclick=\"location.reload()\">刷新页面</button>
                </body>
                </html>
                """.formatted(counter.incrementAndGet(), LocalDateTime.now());
            res.send(response);
        });
        
        app.enableHotReload();
        
        app.addHotReloadListener(() -> {
            System.out.println("[Hot Reload] 检测到文件变化，正在重新加载...");
            System.out.println("[Hot Reload] 重新加载完成！");
        });
        
        System.out.println("服务器已启动，访问: http://localhost:8080");
        System.out.println("修改源代码文件后会自动触发热重载！");
        System.out.println("按 Ctrl+C 停止服务器");
        app.run(8080);
    }
}
