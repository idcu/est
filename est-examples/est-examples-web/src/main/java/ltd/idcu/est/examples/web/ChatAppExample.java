package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatAppExample {
    
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    
    static {
        users.put("system", new User("system", "系统", "#007bff"));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("实时聊天应用 - Chat App");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("Chat App", "1.0.0");
        
        app.enableCors();
        
        app.get("/", ChatAppExample::homePage);
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.get("/messages", ChatAppExample::getMessages);
                r.post("/messages", ChatAppExample::sendMessage);
                r.get("/users", ChatAppExample::getUsers);
                r.post("/users", ChatAppExample::joinUser);
                r.delete("/users/:id", ChatAppExample::leaveUser);
                r.get("/poll", ChatAppExample::pollMessages);
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n�?聊天应用服务器启动成功！");
            System.out.println("\n访问地址�?);
            System.out.println("  - http://localhost:8080          (聊天界面)");
            System.out.println("\nAPI 端点�?);
            System.out.println("  - GET    /api/messages           - 获取消息历史");
            System.out.println("  - POST   /api/messages           - 发送消�?);
            System.out.println("  - GET    /api/users              - 获取在线用户");
            System.out.println("  - POST   /api/users              - 用户加入");
            System.out.println("  - DELETE /api/users/:id          - 用户离开");
            System.out.println("  - GET    /api/poll               - 长轮询获取新消息");
            System.out.println("\n�?Ctrl+C 停止服务�?);
            System.out.println("=".repeat(80));
        });
        
        app.run(8080);
    }
    
    public static void main(String[] args) {
        run();
    }
    
    private static void homePage(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>实时聊天</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; height: 100vh; display: flex; background: #f0f2f5; }
                    .sidebar { width: 250px; background: #2c3e50; color: white; padding: 20px; display: flex; flex-direction: column; }
                    .sidebar h2 { margin-bottom: 20px; font-size: 18px; }
                    .user-list { flex: 1; overflow-y: auto; }
                    .user-item { display: flex; align-items: center; padding: 10px; margin-bottom: 5px; border-radius: 5px; background: rgba(255,255,255,0.1); }
                    .user-avatar { width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; margin-right: 10px; }
                    .main { flex: 1; display: flex; flex-direction: column; }
                    .header { background: white; padding: 20px; border-bottom: 1px solid #e0e0e0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .header h1 { font-size: 24px; color: #333; }
                    .messages { flex: 1; padding: 20px; overflow-y: auto; background: #f0f2f5; }
                    .message { margin-bottom: 15px; animation: fadeIn 0.3s; }
                    @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
                    .message-header { display: flex; align-items: center; margin-bottom: 5px; }
                    .message-avatar { width: 30px; height: 30px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 12px; color: white; margin-right: 10px; }
                    .message-author { font-weight: bold; color: #333; margin-right: 10px; }
                    .message-time { font-size: 12px; color: #999; }
                    .message-content { background: white; padding: 12px 16px; border-radius: 18px; display: inline-block; max-width: 70%; box-shadow: 0 1px 2px rgba(0,0,0,0.1); }
                    .message.own .message-content { background: #0084ff; color: white; }
                    .message.own { text-align: right; }
                    .message.own .message-header { flex-direction: row-reverse; }
                    .message.own .message-avatar { margin-right: 0; margin-left: 10px; }
                    .message.own .message-author { margin-right: 0; margin-left: 10px; }
                    .input-area { background: white; padding: 20px; border-top: 1px solid #e0e0e0; display: flex; gap: 10px; }
                    .input-area input { flex: 1; padding: 12px 16px; border: 1px solid #ddd; border-radius: 24px; font-size: 16px; outline: none; }
                    .input-area input:focus { border-color: #0084ff; }
                    .input-area button { padding: 12px 30px; background: #0084ff; color: white; border: none; border-radius: 24px; font-size: 16px; cursor: pointer; }
                    .input-area button:hover { background: #0066cc; }
                    .login-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
                    .login-box { background: white; padding: 40px; border-radius: 10px; text-align: center; box-shadow: 0 4px 20px rgba(0,0,0,0.3); }
                    .login-box h2 { margin-bottom: 20px; color: #333; }
                    .login-box input { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; }
                    .login-box select { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; }
                    .login-box button { width: 100%; padding: 12px; background: #0084ff; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; }
                    .login-box button:hover { background: #0066cc; }
                    .typing { font-size: 12px; color: #999; font-style: italic; padding: 5px 20px; }
                </style>
            </head>
            <body>
                <div id="loginOverlay" class="login-overlay">
                    <div class="login-box">
                        <h2>💬 加入聊天�?/h2>
                        <input type="text" id="username" placeholder="输入你的昵称..." />
                        <select id="color">
                            <option value="#e74c3c">红色</option>
                            <option value="#3498db">蓝色</option>
                            <option value="#2ecc71">绿色</option>
                            <option value="#f39c12">橙色</option>
                            <option value="#9b59b6">紫色</option>
                            <option value="#1abc9c">青色</option>
                        </select>
                        <button onclick="joinChat()">加入</button>
                    </div>
                </div>
                
                <div class="sidebar">
                    <h2>👥 在线用户</h2>
                    <div class="user-list" id="userList"></div>
                </div>
                
                <div class="main">
                    <div class="header">
                        <h1>💬 EST 实时聊天�?/h1>
                    </div>
                    <div class="messages" id="messages"></div>
                    <div class="typing" id="typing"></div>
                    <div class="input-area">
                        <input type="text" id="messageInput" placeholder="输入消息..." onkeypress="handleKeyPress(event)" />
                        <button onclick="sendMessage()">发�?/button>
                    </div>
                </div>
                
                <script>
                    let currentUser = null;
                    let lastMessageId = 0;
                    
                    function getAvatarInitial(name) {
                        return name.charAt(0).toUpperCase();
                    }
                    
                    async function joinChat() {
                        const name = document.getElementById('username').value.trim();
                        const color = document.getElementById('color').value;
                        
                        if (!name) {
                            alert('请输入昵称！');
                            return;
                        }
                        
                        const res = await fetch('/api/users', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                            body: 'name=' + encodeURIComponent(name) + '&color=' + encodeURIComponent(color)
                        });
                        
                        const data = await res.json();
                        if (data.success) {
                            currentUser = data.data;
                            document.getElementById('loginOverlay').style.display = 'none';
                            loadUsers();
                            loadMessages();
                            startPolling();
                        }
                    }
                    
                    async function loadUsers() {
                        const res = await fetch('/api/users');
                        const data = await res.json();
                        if (data.success) {
                            renderUsers(data.data);
                        }
                    }
                    
                    function renderUsers(userList) {
                        const container = document.getElementById('userList');
                        container.innerHTML = userList.map(user => \`
                            <div class="user-item">
                                <div class="user-avatar" style="background: \${user.color}">\${getAvatarInitial(user.name)}</div>
                                <span>\${escapeHtml(user.name)}</span>
                            </div>
                        \`).join('');
                    }
                    
                    async function loadMessages() {
                        const res = await fetch('/api/messages');
                        const data = await res.json();
                        if (data.success) {
                            renderMessages(data.data);
                            if (data.data.length > 0) {
                                lastMessageId = data.data[data.data.length - 1].id;
                            }
                        }
                    }
                    
                    function renderMessages(messageList) {
                        const container = document.getElementById('messages');
                        container.innerHTML = messageList.map(msg => renderMessage(msg)).join('');
                        scrollToBottom();
                    }
                    
                    function renderMessage(msg) {
                        const isOwn = currentUser && msg.userId === currentUser.id;
                        const time = new Date(msg.timestamp).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
                        return \`
                            <div class="message \${isOwn ? 'own' : ''}">
                                <div class="message-header">
                                    <div class="message-avatar" style="background: \${msg.userColor}">\${getAvatarInitial(msg.userName)}</div>
                                    <span class="message-author">\${escapeHtml(msg.userName)}</span>
                                    <span class="message-time">\${time}</span>
                                </div>
                                <div class="message-content">\${escapeHtml(msg.content)}</div>
                            </div>
                        \`;
                    }
                    
                    async function sendMessage() {
                        const input = document.getElementById('messageInput');
                        const content = input.value.trim();
                        
                        if (!content || !currentUser) return;
                        
                        await fetch('/api/messages', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                            body: 'userId=' + encodeURIComponent(currentUser.id) + '&content=' + encodeURIComponent(content)
                        });
                        
                        input.value = '';
                    }
                    
                    function handleKeyPress(e) {
                        if (e.key === 'Enter') {
                            sendMessage();
                        }
                    }
                    
                    async function startPolling() {
                        while (true) {
                            try {
                                const res = await fetch('/api/poll?sinceId=' + lastMessageId);
                                const data = await res.json();
                                if (data.success && data.data.length > 0) {
                                    const container = document.getElementById('messages');
                                    data.data.forEach(msg => {
                                        container.innerHTML += renderMessage(msg);
                                    });
                                    lastMessageId = data.data[data.data.length - 1].id;
                                    scrollToBottom();
                                    loadUsers();
                                }
                            } catch (e) {
                                console.error('Polling error:', e);
                            }
                            await new Promise(r => setTimeout(r, 1000));
                        }
                    }
                    
                    function scrollToBottom() {
                        const container = document.getElementById('messages');
                        container.scrollTop = container.scrollHeight;
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    document.getElementById('username').addEventListener('keypress', e => {
                        if (e.key === 'Enter') joinChat();
                    });
                </script>
            </body>
            </html>
            """);
    }
    
    private static void getMessages(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", new ArrayList<>(messages)
        ));
    }
    
    private static void sendMessage(Request req, Response res) {
        String userId = req.formParam("userId");
        String content = req.formParam("content");
        
        if (userId == null || content == null || content.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "用户ID和内容不能为�?));
            return;
        }
        
        User user = users.get(userId);
        if (user == null) {
            res.status(404).json(Map.of("success", false, "message", "用户不存�?));
            return;
        }
        
        Message message = new Message(
            messages.size() + 1,
            userId,
            user.name,
            user.color,
            content,
            System.currentTimeMillis()
        );
        messages.add(message);
        
        res.json(Map.of(
            "success", true,
            "data", message
        ));
    }
    
    private static void getUsers(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", users.values().stream()
                .filter(u -> !"system".equals(u.id))
                .toList()
        ));
    }
    
    private static void joinUser(Request req, Response res) {
        String name = req.formParam("name");
        String color = req.formParam("color", "#3498db");
        
        if (name == null || name.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "用户名不能为�?));
            return;
        }
        
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, color);
        users.put(id, user);
        
        Message systemMsg = new Message(
            messages.size() + 1,
            "system",
            "系统",
            "#007bff",
            name + " 加入了聊天室",
            System.currentTimeMillis()
        );
        messages.add(systemMsg);
        
        res.json(Map.of(
            "success", true,
            "data", user
        ));
    }
    
    private static void leaveUser(Request req, Response res) {
        String id = req.param("id");
        User user = users.remove(id);
        
        if (user != null) {
            Message systemMsg = new Message(
                messages.size() + 1,
                "system",
                "系统",
                "#007bff",
                user.name + " 离开了聊天室",
                System.currentTimeMillis()
            );
            messages.add(systemMsg);
            
            res.json(Map.of("success", true, "message", "用户已离开"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "用户不存�?));
        }
    }
    
    private static void pollMessages(Request req, Response res) {
        String sinceIdStr = req.queryParam("sinceId");
        int sinceId = sinceIdStr != null ? Integer.parseInt(sinceIdStr) : 0;
        
        List<Message> newMessages = messages.stream()
            .filter(m -> m.id > sinceId)
            .toList();
        
        res.json(Map.of(
            "success", true,
            "data", newMessages
        ));
    }
    
    static class User {
        public String id;
        public String name;
        public String color;
        
        public User(String id, String name, String color) {
            this.id = id;
            this.name = name;
            this.color = color;
        }
    }
    
    static class Message {
        public int id;
        public String userId;
        public String userName;
        public String userColor;
        public String content;
        public long timestamp;
        
        public Message(int id, String userId, String userName, String userColor, String content, long timestamp) {
            this.id = id;
            this.userId = userId;
            this.userName = userName;
            this.userColor = userColor;
            this.content = content;
            this.timestamp = timestamp;
        }
    }
}
