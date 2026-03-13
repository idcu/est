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
        System.out.println("EST Admin Console - Complete Admin Example");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("EST Complete Admin", "2.1.0");
        
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Admin - Admin Console</title>
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
                        <h1>🚀 EST Admin Console</h1>
                        
                        <h2>✨ Features</h2>
                        <div class="feature">👤 User Management - Complete user CRUD operations</div>
                        <div class="feature">🎭 Role Management - Role creation and permission assignment</div>
                        <div class="feature">📋 Menu Management - Tree menu structure</div>
                        <div class="feature">🏢 Department Management - Organization structure</div>
                        <div class="feature">🏪 Tenant Management - Multi-tenant support</div>
                        <div class="feature">📝 Log Management - Operation logs and login logs</div>
                        <div class="feature">📊 System Monitoring - JVM and system metrics</div>
                        <div class="feature">🔌 Third-party Integration - Email, SMS, OSS</div>
                        <div class="feature">🤖 AI Assistant - Code generation and chat</div>
                        
                        <div class="links">
                            <a href="/admin">Enter Admin Console</a>
                            <a href="/health">Health Check</a>
                            <a href="/api-docs">API Documentation</a>
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
                "version", "2.1.0",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        app.get("/api-docs", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Admin API Documentation</title>
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
                    <h1>📚 EST Admin API Documentation</h1>
                    
                    <h2>Authentication APIs</h2>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/auth/login - User login</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/auth/logout - User logout</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/auth/current - Get current user</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/auth/refresh-token - Refresh Token</div>
                    
                    <h2>User Management</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/users - Get user list</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/users - Create user</div>
                    <div class="endpoint"><span class="method put">PUT</span> /admin/api/users/:id - Update user</div>
                    <div class="endpoint"><span class="method delete">DELETE</span> /admin/api/users/:id - Delete user</div>
                    
                    <h2>Role Management</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/roles - Get role list</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/roles - Create role</div>
                    <div class="endpoint"><span class="method put">PUT</span> /admin/api/roles/:id - Update role</div>
                    <div class="endpoint"><span class="method delete">DELETE</span> /admin/api/roles/:id - Delete role</div>
                    
                    <h2>Menu Management</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/menus - Get menu list</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/menus - Create menu</div>
                    <div class="endpoint"><span class="method put">PUT</span> /admin/api/menus/:id - Update menu</div>
                    <div class="endpoint"><span class="method delete">DELETE</span> /admin/api/menus/:id - Delete menu</div>
                    
                    <h2>Log Management</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/operation-logs - Operation logs</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/login-logs - Login logs</div>
                    
                    <h2>System Monitoring</h2>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/monitor/jvm - JVM monitoring</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/monitor/system - System monitoring</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/online-users - Online users</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/cache/statistics - Cache statistics</div>
                    
                    <h2>AI Assistant</h2>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/chat - AI chat</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/code/generate - Code generation</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/code/explain - Code explanation</div>
                    <div class="endpoint"><span class="method post">POST</span> /admin/api/ai/code/optimize - Code optimization</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/ai/reference - Development reference</div>
                    <div class="endpoint"><span class="method get">GET</span> /admin/api/ai/templates - Prompt templates</div>
                    
                </body>
                </html>
                """);
        });
        
        AdminApplication adminApp = Admin.create("EST Complete Admin", "2.1.0");
        
        System.out.println("\n[OK] Admin Console started!");
        System.out.println("📍 Access URL: http://localhost:8080");
        System.out.println("👤 Default account: admin / admin123");
        System.out.println("=".repeat(80));
        
        app.run(8080);
    }
    
    public static void run() {
        main(new String[]{});
    }
}
