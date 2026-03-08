package ltd.idcu.est.examples.web;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.*;
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

import java.util.Map;

public class CompleteAdminExample {
    
    public static void main(String[] args) {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("EST Admin Console - 完整管理后台示例");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("EST Complete Admin", "2.0.0");
        
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Admin - 管理后台</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            max-width: 800px;
                            margin: 50px auto;
                            padding: 20px;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                        }
                        .container {
                            background: white;
                            padding: 40px;
                            border-radius: 10px;
                            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                        }
                        h1 {
                            color: #333;
                            text-align: center;
                            margin-bottom: 30px;
                        }
                        .feature {
                            background: #f8f9fa;
                            padding: 15px;
                            margin: 10px 0;
                            border-radius: 5px;
                            border-left: 4px solid #667eea;
                        }
                        .links {
                            margin-top: 30px;
                            text-align: center;
                        }
                        .links a {
                            display: inline-block;
                            margin: 10px;
                            padding: 10px 20px;
                            background: #667eea;
                            color: white;
                            text-decoration: none;
                            border-radius: 5px;
                        }
                        .links a:hover {
                            background: #764ba2;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🚀 EST Admin 管理后台</h1>
                        
                        <h2>✨ 功能特性</h2>
                        <div class="feature">👤 用户管理 - 完整的用户CRUD操作</div>
                        <div class="feature">🎭 角色管理 - 角色创建和权限分配</div>
                        <div class="feature">📋 菜单管理 - 树形菜单结构</div>
                        <div class="feature">🏢 部门管理 - 组织架构管理</div>
                        <div class="feature">🏪 租户管理 - 多租户支持</div>
                        <div class="feature">📝 日志管理 - 操作日志和登录日志</div>
                        <div class="feature">📊 系统监控 - JVM和系统指标</div>
                        <div class="feature">🔌 第三方集成 - 邮件、短信、OSS</div>
                        <div class="feature">🤖 AI助手 - 代码生成和对话</div>
                        
                        <div class="links">
                            <a href="/admin">进入管理后台</a>
                            <a href="/health">健康检查</a>
                            <a href="/api-docs">API文档</a>
                        </div>
                    </div>
                </body>
                </html>
                """);
        });
        
        app.get("/health", (req, res) -> {
            res.json(Map.of(
                "status", "healthy",
                "service", "EST Admin",
                "version", "2.0.0",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        app.get("/api-docs", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Admin API 文档</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 1000px; margin: 50px auto; padding: 20px; }
                        h1 { color: #333; }
                        .endpoint { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 5px; }
                        .method { display: inline-block; padding: 3px 8px; border-radius: 3px; font-weight: bold; color: white; }
                        .get { background: #61affe; }
                        .post { background: #49cc90; }
                        .put { background: #fca130; }
                        .delete { background: #f93e3e; }
                    </style>
                </head>
                <body>
                    <h1>📚 EST Admin API 文档</h1>
                    
                    <h2>认证接口</h2>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/auth/login - 用户登录</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/auth/logout - 用户登出</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/auth/current - 获取当前用户</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/auth/refresh-token - 刷新Token</div>
                    
                    <h2>用户管理</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/users - 获取用户列表</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/users - 创建用户</div>
                    <div class="endpoint"><span class="method put">PUT</span> /admin/api/users/:id - 更新用户</div>
                    <div class="endpoint"><span class="method delete">DELETE</span> /admin/api/users/:id - 删除用户</div>
                    
                    <h2>角色管理</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/roles - 获取角色列表</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/roles - 创建角色</div>
                    <div class="endpoint"><span class="method put">PUT</span> /admin/api/roles/:id - 更新角色</div>
                    <div class="endpoint"><span class="method delete">DELETE</span> /admin/api/roles/:id - 删除角色</div>
                    
                    <h2>菜单管理</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/menus - 获取菜单列表</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/menus - 创建菜单</div>
                    <div class="endpoint"><span class="method put">PUT</span> /admin/api/menus/:id - 更新菜单</div>
                    <div class="endpoint"><span class="method delete">DELETE</span> /admin/api/menus/:id - 删除菜单</div>
                    
                    <h2>日志管理</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/operation-logs - 操作日志</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/login-logs - 登录日志</div>
                    
                    <h2>系统监控</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/monitor/jvm - JVM监控</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/monitor/system - 系统监控</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/online-users - 在线用户</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/cache/statistics - 缓存统计</div>
                    
                    <h2>AI助手</h2>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/chat - AI对话</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/code/generate - 代码生成</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/code/explain - 代码解释</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/code/optimize - 代码优化</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/ai/reference - 开发参考</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/ai/templates - 提示模板</div>
                    
                </body>
                </html>
                """);
        });
        
        AdminApplication adminApp = Admin.create("EST Complete Admin", "2.0.0");
        
        System.out.println("\n✅ Admin 管理后台已启动！");
        System.out.println("📍 访问地址: http://localhost:8080");
        System.out.println("👤 默认账号: admin / admin123");
        System.out.println("=".repeat(80));
        
        app.run(8080);
    }
    
    public static void run() {
        main(new String[]{});
    }
}
