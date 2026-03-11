package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.AuthService;
import ltd.idcu.est.admin.api.LoginLogService;
import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.Session;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

public class AdminController {
    
    private final AuthService authService;
    private final LoginLogService loginLogService;
    
    public AdminController() {
        this.authService = Admin.createAuthService();
        this.loginLogService = Admin.createLoginLogService();
    }
    
    public void login(Request req, Response res) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if (username == null || password == null) {
            String body = req.getBody();
            if (body != null && !body.isEmpty()) {
                try {
                    Map<String, Object> jsonMap = JsonUtils.parseObject(body);
                    if (jsonMap != null) {
                        username = JsonUtils.getString(jsonMap, "username");
                        password = JsonUtils.getString(jsonMap, "password");
                    }
                } catch (Exception e) {
                    res.setStatus(400);
                    res.json(ApiResponse.error("Invalid JSON format"));
                    return;
                }
            }
        }
        
        String ip = getClientIp(req);
        String userAgent = getUserAgent(req);
        
        try {
            User user = authService.authenticate(username, password);
            String token = authService.generateToken(user);
            
            Session session = req.getSession(true);
            session.setAttribute("authToken", token);
            session.setAttribute("currentUser", user);
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("roles", user.getRoles());
            userData.put("permissions", user.getPermissions());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", userData);
            
            loginLogService.createLoginLog(user.getId(), user.getUsername(), ip, userAgent, 1, null);
            res.json(ApiResponse.success("Login successful", data));
        } catch (Exception e) {
            loginLogService.createLoginLog(null, username, ip, userAgent, 0, e.getMessage());
            res.setStatus(401);
            res.json(ApiResponse.unauthorized(e.getMessage()));
        }
    }
    
    private String getClientIp(Request req) {
        String xForwardedFor = req.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return "127.0.0.1";
    }
    
    private String getUserAgent(Request req) {
        String userAgent = req.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown";
    }
    
    public void logout(Request req, Response res) {
        Session session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("authToken");
            session.removeAttribute("currentUser");
        }
        
        res.json(ApiResponse.success("Logout successful", null));
    }
    
    public void getCurrentUser(Request req, Response res) {
        String authHeader = req.getHeader("Authorization");
        String token = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            Session session = req.getSession(false);
            if (session != null) {
                token = (String) session.getAttribute("authToken");
            }
        }
        
        if (token == null) {
            res.setStatus(401);
            res.json(ApiResponse.unauthorized("Not authenticated"));
            return;
        }
        
        User user = authService.validateToken(token);
        if (user == null) {
            res.setStatus(401);
            res.json(ApiResponse.unauthorized("Invalid or expired token"));
            return;
        }
        
        req.setAttribute("currentUser", user);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("roles", user.getRoles());
        userData.put("permissions", user.getPermissions());
        userData.put("active", user.isActive());
        
        res.json(ApiResponse.success(userData));
    }
    
    public void refreshToken(Request req, Response res) {
        String authHeader = req.getHeader("Authorization");
        String token = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            Session session = req.getSession(false);
            if (session != null) {
                token = (String) session.getAttribute("authToken");
            }
        }
        
        if (token == null) {
            res.setStatus(401);
            res.json(ApiResponse.unauthorized("Not authenticated"));
            return;
        }
        
        User user = authService.validateToken(token);
        if (user == null) {
            res.setStatus(401);
            res.json(ApiResponse.unauthorized("Invalid or expired token"));
            return;
        }
        
        String newToken = authService.generateToken(user);
        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        
        res.json(ApiResponse.success("Token refreshed", data));
    }
    
    public void dashboard(Request req, Response res) {
        res.html(getDashboardHTML());
    }
    
    public void systemStats(Request req, Response res) {
        Map<String, Object> stats = new HashMap<>();
        
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        
        Map<String, Object> memory = new HashMap<>();
        memory.put("heapUsed", memoryBean.getHeapMemoryUsage().getUsed());
        memory.put("heapMax", memoryBean.getHeapMemoryUsage().getMax());
        memory.put("heapCommitted", memoryBean.getHeapMemoryUsage().getCommitted());
        memory.put("nonHeapUsed", memoryBean.getNonHeapMemoryUsage().getUsed());
        stats.put("memory", memory);
        
        Map<String, Object> system = new HashMap<>();
        system.put("osName", osBean.getName());
        system.put("osVersion", osBean.getVersion());
        system.put("availableProcessors", osBean.getAvailableProcessors());
        system.put("systemLoadAverage", osBean.getSystemLoadAverage());
        stats.put("system", system);
        
        Map<String, Object> threads = new HashMap<>();
        threads.put("threadCount", threadBean.getThreadCount());
        threads.put("peakThreadCount", threadBean.getPeakThreadCount());
        threads.put("daemonThreadCount", threadBean.getDaemonThreadCount());
        stats.put("threads", threads);
        
        Map<String, Object> runtime = new HashMap<>();
        runtime.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        runtime.put("startTime", ManagementFactory.getRuntimeMXBean().getStartTime());
        stats.put("runtime", runtime);
        
        res.json(Map.of(
            "success", true,
            "data", stats
        ));
    }
    
    private String getDashboardHTML() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>EST Admin Console</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        background: #f0f2f5;
                        color: #333;
                    }
                    
                    .header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 20px 30px;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    
                    .header h1 {
                        font-size: 28px;
                        font-weight: 600;
                    }
                    
                    .container {
                        max-width: 1400px;
                        margin: 30px auto;
                        padding: 0 20px;
                    }
                    
                    .stats-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
                        gap: 20px;
                        margin-bottom: 30px;
                    }
                    
                    .stat-card {
                        background: white;
                        border-radius: 12px;
                        padding: 25px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                        transition: transform 0.2s, box-shadow 0.2s;
                    }
                    
                    .stat-card:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 4px 16px rgba(0,0,0,0.12);
                    }
                    
                    .stat-label {
                        font-size: 14px;
                        color: #666;
                        margin-bottom: 8px;
                    }
                    
                    .stat-value {
                        font-size: 32px;
                        font-weight: 700;
                        color: #667eea;
                    }
                    
                    .stat-unit {
                        font-size: 14px;
                        color: #999;
                        margin-left: 5px;
                    }
                    
                    .content-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
                        gap: 20px;
                    }
                    
                    .panel {
                        background: white;
                        border-radius: 12px;
                        padding: 25px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                    }
                    
                    .panel-title {
                        font-size: 18px;
                        font-weight: 600;
                        margin-bottom: 20px;
                        color: #333;
                        padding-bottom: 10px;
                        border-bottom: 2px solid #f0f2f5;
                    }
                    
                    .detail-item {
                        display: flex;
                        justify-content: space-between;
                        padding: 12px 0;
                        border-bottom: 1px solid #f5f5f5;
                    }
                    
                    .detail-item:last-child {
                        border-bottom: none;
                    }
                    
                    .detail-label {
                        color: #666;
                    }
                    
                    .detail-value {
                        font-weight: 500;
                        color: #333;
                    }
                    
                    .refresh-btn {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        border: none;
                        padding: 12px 24px;
                        border-radius: 8px;
                        cursor: pointer;
                        font-size: 14px;
                        font-weight: 500;
                        transition: opacity 0.2s;
                    }
                    
                    .refresh-btn:hover {
                        opacity: 0.9;
                    }
                    
                    .progress-bar {
                        width: 100%;
                        height: 8px;
                        background: #e0e0e0;
                        border-radius: 4px;
                        overflow: hidden;
                        margin-top: 8px;
                    }
                    
                    .progress-fill {
                        height: 100%;
                        background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
                        transition: width 0.5s;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>EST Admin Console</h1>
                </div>
                
                <div class="container">
                    <div style="text-align: right; margin-bottom: 20px;">
                        <button class="refresh-btn" onclick="refreshStats()">Refresh Data</button>
                    </div>
                    
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-label">Heap Memory Used</div>
                            <div>
                                <span class="stat-value" id="heapUsed">0</span>
                                <span class="stat-unit">MB</span>
                            </div>
                            <div class="progress-bar">
                                <div class="progress-fill" id="heapProgress" style="width: 0%"></div>
                            </div>
                        </div>
                        
                        <div class="stat-card">
                            <div class="stat-label">Thread Count</div>
                            <div class="stat-value" id="threadCount">0</div>
                        </div>
                        
                        <div class="stat-card">
                            <div class="stat-label">System Load</div>
                            <div class="stat-value" id="systemLoad">0</div>
                        </div>
                        
                        <div class="stat-card">
                            <div class="stat-label">Uptime</div>
                            <div class="stat-value" id="uptime">0</div>
                            <span class="stat-unit">s</span>
                        </div>
                    </div>
                    
                    <div class="content-grid">
                        <div class="panel">
                            <div class="panel-title">Memory Info</div>
                            <div class="detail-item">
                                <span class="detail-label">Heap Used</span>
                                <span class="detail-value" id="heapUsedDetail">0 MB</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Heap Max</span>
                                <span class="detail-value" id="heapMaxDetail">0 MB</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Heap Committed</span>
                                <span class="detail-value" id="heapCommittedDetail">0 MB</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Non-Heap Used</span>
                                <span class="detail-value" id="nonHeapUsedDetail">0 MB</span>
                            </div>
                        </div>
                        
                        <div class="panel">
                            <div class="panel-title">System Info</div>
                            <div class="detail-item">
                                <span class="detail-label">OS</span>
                                <span class="detail-value" id="osName">-</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">OS Version</span>
                                <span class="detail-value" id="osVersion">-</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Processors</span>
                                <span class="detail-value" id="availableProcessors">0</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Peak Threads</span>
                                <span class="detail-value" id="peakThreadCount">0</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <script>
                    function formatBytes(bytes) {
                        return (bytes / (1024 * 1024)).toFixed(2);
                    }
                    
                    function formatUptime(ms) {
                        return (ms / 1000).toFixed(0);
                    }
                    
                    async function refreshStats() {
                        try {
                            const response = await fetch('/admin/api/stats');
                            const result = await response.json();
                            
                            if (result.success) {
                                const data = result.data;
                                const memory = data.memory;
                                const system = data.system;
                                const threads = data.threads;
                                const runtime = data.runtime;
                                
                                const heapUsedMB = parseFloat(formatBytes(memory.heapUsed));
                                const heapMaxMB = parseFloat(formatBytes(memory.heapMax));
                                const heapPercent = (heapUsedMB / heapMaxMB * 100).toFixed(1);
                                
                                document.getElementById('heapUsed').textContent = heapUsedMB.toFixed(0);
                                document.getElementById('heapProgress').style.width = heapPercent + '%';
                                document.getElementById('threadCount').textContent = threads.threadCount;
                                document.getElementById('systemLoad').textContent = system.systemLoadAverage.toFixed(2);
                                document.getElementById('uptime').textContent = formatUptime(runtime.uptime);
                                
                                document.getElementById('heapUsedDetail').textContent = heapUsedMB + ' MB';
                                document.getElementById('heapMaxDetail').textContent = heapMaxMB + ' MB';
                                document.getElementById('heapCommittedDetail').textContent = formatBytes(memory.heapCommitted) + ' MB';
                                document.getElementById('nonHeapUsedDetail').textContent = formatBytes(memory.nonHeapUsed) + ' MB';
                                
                                document.getElementById('osName').textContent = system.osName;
                                document.getElementById('osVersion').textContent = system.osVersion;
                                document.getElementById('availableProcessors').textContent = system.availableProcessors;
                                document.getElementById('peakThreadCount').textContent = threads.peakThreadCount;
                            }
                        } catch (error) {
                            console.error('Failed to refresh stats:', error);
                        }
                    }
                    
                    refreshStats();
                    setInterval(refreshStats, 5000);
                </script>
            </body>
            </html>
            """;
    }
}
