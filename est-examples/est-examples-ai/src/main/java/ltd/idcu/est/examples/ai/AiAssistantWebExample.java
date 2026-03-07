package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.PromptTemplateRegistry;
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.api.ProjectScaffold;
import ltd.idcu.est.ai.impl.Ai;
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AiAssistantWebExample {
    
    private static final Map<Integer, ChatSession> chatSessions = new ConcurrentHashMap<>();
    private static final AtomicInteger sessionIdGenerator = new AtomicInteger(1);
    private static AiAssistant aiAssistant;
    private static PromptTemplateRegistry templateRegistry;
    private static CodeGenerator codeGenerator;
    private static ProjectScaffold projectScaffold;
    
    static {
        aiAssistant = Ai.getAssistant();
        templateRegistry = Ai.getTemplateRegistry();
        codeGenerator = Ai.getCodeGenerator();
        projectScaffold = Ai.getProjectScaffold();
        
        PromptTemplate codeReviewTemplate = Ai.createPromptTemplate(
            "code-review",
            "请审查以下代码，分析潜在问题并提供改进建议：\\n\\n${code}",
            Map.of("code", "要审查的代码")
        );
        
        PromptTemplate bugFixTemplate = Ai.createPromptTemplate(
            "bug-fix",
            "请分析以下错误并提供修复方案：\\n\\n错误信息：${error}\\n\\n相关代码：${code}",
            Map.of("error", "错误信息", "code", "相关代码")
        );
        
        PromptTemplate explanationTemplate = Ai.createPromptTemplate(
            "explanation",
            "请用通俗易懂的方式解释以下概念：${concept}",
            Map.of("concept", "要解释的概念")
        );
        
        templateRegistry.register(codeReviewTemplate);
        templateRegistry.register(bugFixTemplate);
        templateRegistry.register(explanationTemplate);
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("AI助手Web应用 - AI Assistant Web");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("AI Assistant", "1.0.0");
        
        app.enableCors();
        
        app.get("/", AiAssistantWebExample::homePage);
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.group("/chat", (chatRouter, chatGroup) -> {
                    chatRouter.get("/sessions", AiAssistantWebExample::listSessions);
                    chatRouter.post("/sessions", AiAssistantWebExample::createSession);
                    chatRouter.get("/sessions/:id", AiAssistantWebExample::getSession);
                    chatRouter.post("/sessions/:id/messages", AiAssistantWebExample::sendMessage);
                    chatRouter.delete("/sessions/:id", AiAssistantWebExample::deleteSession);
                });
                
                r.group("/templates", (templateRouter, templateGroup) -> {
                    templateRouter.get("", AiAssistantWebExample::listTemplates);
                    templateRouter.post("/apply", AiAssistantWebExample::applyTemplate);
                });
                
                r.group("/code", (codeRouter, codeGroup) -> {
                    codeRouter.post("/generate", AiAssistantWebExample::generateCode);
                    codeRouter.post("/review", AiAssistantWebExample::reviewCode);
                });
                
                r.group("/scaffold", (scaffoldRouter, scaffoldGroup) -> {
                    scaffoldRouter.post("/generate", AiAssistantWebExample::generateScaffold);
                });
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ AI助手Web应用启动成功！");
            System.out.println("\n访问地址：");
            System.out.println("  - http://localhost:8080          (AI助手界面)");
            System.out.println("\nAPI 端点：");
            System.out.println("【聊天会话】");
            System.out.println("  - GET    /api/chat/sessions      - 获取会话列表");
            System.out.println("  - POST   /api/chat/sessions      - 创建会话");
            System.out.println("  - GET    /api/chat/sessions/:id  - 获取会话详情");
            System.out.println("  - POST   /api/chat/sessions/:id/messages - 发送消息");
            System.out.println("  - DELETE /api/chat/sessions/:id  - 删除会话");
            System.out.println("\n【提示词模板】");
            System.out.println("  - GET    /api/templates           - 获取模板列表");
            System.out.println("  - POST   /api/templates/apply     - 应用模板");
            System.out.println("\n【代码生成】");
            System.out.println("  - POST   /api/code/generate       - 生成代码");
            System.out.println("  - POST   /api/code/review         - 代码审查");
            System.out.println("\n【项目脚手架】");
            System.out.println("  - POST   /api/scaffold/generate   - 生成项目脚手架");
            System.out.println("\n按 Ctrl+C 停止服务器");
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
                <title>AI助手</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: #f0f2f5; height: 100vh; display: flex; }
                    .sidebar { width: 280px; background: #2c3e50; color: white; display: flex; flex-direction: column; }
                    .sidebar-header { padding: 20px; border-bottom: 1px solid rgba(255,255,255,0.1); }
                    .sidebar-header h1 { font-size: 20px; margin-bottom: 5px; }
                    .sidebar-header p { font-size: 13px; opacity: 0.8; }
                    .new-chat-btn { margin: 15px; padding: 12px; background: #667eea; color: white; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }
                    .new-chat-btn:hover { background: #5a6fd6; }
                    .sessions-list { flex: 1; overflow-y: auto; padding: 10px; }
                    .session-item { padding: 12px; border-radius: 8px; margin-bottom: 5px; cursor: pointer; transition: background 0.2s; }
                    .session-item:hover, .session-item.active { background: rgba(255,255,255,0.1); }
                    .session-item h3 { font-size: 14px; margin-bottom: 3px; }
                    .session-item p { font-size: 12px; opacity: 0.7; }
                    .main { flex: 1; display: flex; flex-direction: column; }
                    .tab-bar { background: white; padding: 0 20px; border-bottom: 1px solid #e0e0e0; display: flex; }
                    .tab { padding: 15px 20px; cursor: pointer; border-bottom: 3px solid transparent; font-size: 14px; font-weight: 500; color: #666; }
                    .tab:hover { color: #667eea; }
                    .tab.active { color: #667eea; border-bottom-color: #667eea; }
                    .content { flex: 1; display: none; overflow: hidden; }
                    .content.active { display: flex; flex-direction: column; }
                    .chat-container { flex: 1; display: flex; flex-direction: column; }
                    .chat-messages { flex: 1; overflow-y: auto; padding: 20px; }
                    .message { margin-bottom: 20px; display: flex; gap: 12px; }
                    .message.user { flex-direction: row-reverse; }
                    .message-avatar { width: 40px; height: 40px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }
                    .message.user .message-avatar { background: #667eea; }
                    .message.assistant .message-avatar { background: #27ae60; }
                    .message-content { max-width: 70%; background: white; padding: 15px; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
                    .message.user .message-content { background: #667eea; color: white; }
                    .chat-input { background: white; padding: 20px; border-top: 1px solid #e0e0e0; display: flex; gap: 10px; }
                    .chat-input textarea { flex: 1; padding: 12px; border: 1px solid #ddd; border-radius: 8px; resize: none; font-size: 14px; }
                    .chat-input button { padding: 0 30px; background: #667eea; color: white; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }
                    .chat-input button:hover { background: #5a6fd6; }
                    .tools-panel { padding: 20px; overflow-y: auto; }
                    .tool-section { background: white; border-radius: 12px; padding: 25px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .tool-section h2 { font-size: 18px; color: #333; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 2px solid #f0f0f0; }
                    .form-group { margin-bottom: 15px; }
                    .form-group label { display: block; margin-bottom: 6px; color: #333; font-weight: 500; }
                    .form-group input, .form-group textarea, .form-group select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; }
                    .form-group textarea { min-height: 120px; resize: vertical; }
                    .btn { padding: 12px 24px; border: none; border-radius: 6px; font-size: 14px; cursor: pointer; }
                    .btn-primary { background: #667eea; color: white; }
                    .btn-primary:hover { background: #5a6fd6; }
                    .result-box { margin-top: 15px; background: #f8f9fa; border-radius: 8px; padding: 15px; border: 1px solid #e0e0e0; }
                    .result-box pre { white-space: pre-wrap; word-wrap: break-word; font-family: monospace; font-size: 13px; }
                    .template-list { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 15px; }
                    .template-card { border: 1px solid #ddd; border-radius: 8px; padding: 15px; cursor: pointer; transition: all 0.2s; }
                    .template-card:hover { border-color: #667eea; box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2); }
                    .template-card h3 { font-size: 14px; color: #333; margin-bottom: 8px; }
                    .template-card p { font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="sidebar">
                    <div class="sidebar-header">
                        <h1>🤖 AI助手</h1>
                        <p>EST框架AI编程助手</p>
                    </div>
                    <button class="new-chat-btn" onclick="createSession()">+ 新对话</button>
                    <div class="sessions-list" id="sessionsList"></div>
                </div>
                
                <div class="main">
                    <div class="tab-bar">
                        <div class="tab active" onclick="switchTab('chat')">💬 聊天</div>
                        <div class="tab" onclick="switchTab('templates')">📝 提示词模板</div>
                        <div class="tab" onclick="switchTab('code')">💻 代码生成</div>
                        <div class="tab" onclick="switchTab('scaffold')">🏗️ 项目脚手架</div>
                    </div>
                    
                    <div class="content active" id="chat-content">
                        <div class="chat-container">
                            <div class="chat-messages" id="chatMessages">
                                <div style="text-align: center; padding: 40px; color: #999;">
                                    <div style="font-size: 48px; margin-bottom: 15px;">👋</div>
                                    <p>选择或创建一个会话开始聊天</p>
                                </div>
                            </div>
                            <div class="chat-input">
                                <textarea id="messageInput" placeholder="输入你的消息..." rows="1"></textarea>
                                <button onclick="sendMessage()">发送</button>
                            </div>
                        </div>
                    </div>
                    
                    <div class="content" id="templates-content">
                        <div class="tools-panel">
                            <div class="tool-section">
                                <h2>📝 可用模板</h2>
                                <div class="template-list" id="templateList"></div>
                            </div>
                            <div class="tool-section" id="templateApplySection" style="display: none;">
                                <h2>✏️ 应用模板</h2>
                                <div id="templateVars"></div>
                                <button class="btn btn-primary" onclick="applyTemplate()">生成提示词</button>
                                <div class="result-box" id="templateResult" style="display: none;">
                                    <pre id="templateResultText"></pre>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="content" id="code-content">
                        <div class="tools-panel">
                            <div class="tool-section">
                                <h2>🎯 代码生成</h2>
                                <div class="form-group">
                                    <label>描述你想要的代码</label>
                                    <textarea id="codeDescription" placeholder="例如：创建一个用户管理的Controller类，包含增删改查方法..."></textarea>
                                </div>
                                <button class="btn btn-primary" onclick="generateCode()">生成代码</button>
                                <div class="result-box" id="codeResult" style="display: none;">
                                    <pre id="codeResultText"></pre>
                                </div>
                            </div>
                            <div class="tool-section">
                                <h2>🔍 代码审查</h2>
                                <div class="form-group">
                                    <label>粘贴要审查的代码</label>
                                    <textarea id="codeToReview" placeholder="在这里粘贴你的代码..."></textarea>
                                </div>
                                <button class="btn btn-primary" onclick="reviewCode()">审查代码</button>
                                <div class="result-box" id="reviewResult" style="display: none;">
                                    <pre id="reviewResultText"></pre>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="content" id="scaffold-content">
                        <div class="tools-panel">
                            <div class="tool-section">
                                <h2>🏗️ 项目脚手架生成</h2>
                                <div class="form-group">
                                    <label>项目名称</label>
                                    <input type="text" id="projectName" placeholder="my-project" />
                                </div>
                                <div class="form-group">
                                    <label>项目类型</label>
                                    <select id="projectType">
                                        <option value="web">Web应用</option>
                                        <option value="rest-api">REST API</option>
                                        <option value="microservice">微服务</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>包名</label>
                                    <input type="text" id="packageName" placeholder="com.example" />
                                </div>
                                <button class="btn btn-primary" onclick="generateScaffold()">生成脚手架</button>
                                <div class="result-box" id="scaffoldResult" style="display: none;">
                                    <pre id="scaffoldResultText"></pre>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <script>
                    let currentSessionId = null;
                    let selectedTemplate = null;
                    
                    async function loadSessions() {
                        const res = await fetch('/api/chat/sessions');
                        const data = await res.json();
                        if (data.success) {
                            renderSessions(data.data);
                        }
                    }
                    
                    function renderSessions(sessions) {
                        const container = document.getElementById('sessionsList');
                        container.innerHTML = sessions.map(session => \`
                            <div class="session-item \${session.id === currentSessionId ? 'active' : ''}" onclick="selectSession(\${session.id})">
                                <h3>\${escapeHtml(session.title)}</h3>
                                <p>\${session.messageCount} 条消息</p>
                            </div>
                        \`).join('');
                    }
                    
                    async function createSession() {
                        const title = prompt('请输入会话标题：', '新对话');
                        if (!title) return;
                        
                        const formData = new FormData();
                        formData.append('title', title);
                        
                        const res = await fetch('/api/chat/sessions', { method: 'POST', body: formData });
                        const data = await res.json();
                        if (data.success) {
                            currentSessionId = data.data.id;
                            loadSessions();
                            loadSession(currentSessionId);
                        }
                    }
                    
                    async function selectSession(id) {
                        currentSessionId = id;
                        loadSessions();
                        loadSession(id);
                    }
                    
                    async function loadSession(id) {
                        const res = await fetch('/api/chat/sessions/' + id);
                        const data = await res.json();
                        if (data.success) {
                            renderMessages(data.data.messages);
                        }
                    }
                    
                    function renderMessages(messages) {
                        const container = document.getElementById('chatMessages');
                        if (messages.length === 0) {
                            container.innerHTML = \`
                                <div style="text-align: center; padding: 40px; color: #999;">
                                    <div style="font-size: 48px; margin-bottom: 15px;">💬</div>
                                    <p>开始发送消息吧！</p>
                                </div>
                            \`;
                            return;
                        }
                        
                        container.innerHTML = messages.map(msg => \`
                            <div class="message \${msg.role}">
                                <div class="message-avatar">\${msg.role === 'user' ? '👤' : '🤖'}</div>
                                <div class="message-content">\${escapeHtml(msg.content)}</div>
                            </div>
                        \`).join('');
                        
                        container.scrollTop = container.scrollHeight;
                    }
                    
                    async function sendMessage() {
                        if (!currentSessionId) {
                            alert('请先选择或创建一个会话！');
                            return;
                        }
                        
                        const input = document.getElementById('messageInput');
                        const content = input.value.trim();
                        if (!content) return;
                        
                        const formData = new FormData();
                        formData.append('content', content);
                        
                        const res = await fetch('/api/chat/sessions/' + currentSessionId + '/messages', { method: 'POST', body: formData });
                        const data = await res.json();
                        if (data.success) {
                            input.value = '';
                            loadSession(currentSessionId);
                            loadSessions();
                        }
                    }
                    
                    async function loadTemplates() {
                        const res = await fetch('/api/templates');
                        const data = await res.json();
                        if (data.success) {
                            renderTemplates(data.data);
                        }
                    }
                    
                    function renderTemplates(templates) {
                        const container = document.getElementById('templateList');
                        container.innerHTML = templates.map(template => \`
                            <div class="template-card" onclick="selectTemplate('\${template.name}')">
                                <h3>\${escapeHtml(template.name)}</h3>
                                <p>变量：\${Object.keys(template.variables || {}).join(', ')}</p>
                            </div>
                        \`).join('');
                    }
                    
                    async function selectTemplate(name) {
                        selectedTemplate = name;
                        const res = await fetch('/api/templates');
                        const data = await res.json();
                        const template = data.data.find(t => t.name === name);
                        
                        const section = document.getElementById('templateApplySection');
                        const varsContainer = document.getElementById('templateVars');
                        
                        section.style.display = 'block';
                        varsContainer.innerHTML = Object.entries(template.variables || {}).map(([key, desc]) => \`
                            <div class="form-group">
                                <label>\${escapeHtml(key)} - \${escapeHtml(desc)}</label>
                                <input type="text" data-var="\${key}" placeholder="输入\${escapeHtml(key)}..." />
                            </div>
                        \`).join('');
                    }
                    
                    async function applyTemplate() {
                        if (!selectedTemplate) return;
                        
                        const vars = {};
                        document.querySelectorAll('[data-var]').forEach(input => {
                            vars[input.dataset.var] = input.value;
                        });
                        
                        const formData = new FormData();
                        formData.append('templateName', selectedTemplate);
                        Object.entries(vars).forEach(([key, value]) => {
                            formData.append('var_' + key, value);
                        });
                        
                        const res = await fetch('/api/templates/apply', { method: 'POST', body: formData });
                        const data = await res.json();
                        if (data.success) {
                            document.getElementById('templateResult').style.display = 'block';
                            document.getElementById('templateResultText').textContent = data.data;
                        }
                    }
                    
                    async function generateCode() {
                        const description = document.getElementById('codeDescription').value.trim();
                        if (!description) {
                            alert('请输入代码描述！');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('description', description);
                        
                        const res = await fetch('/api/code/generate', { method: 'POST', body: formData });
                        const data = await res.json();
                        if (data.success) {
                            document.getElementById('codeResult').style.display = 'block';
                            document.getElementById('codeResultText').textContent = data.data;
                        }
                    }
                    
                    async function reviewCode() {
                        const code = document.getElementById('codeToReview').value.trim();
                        if (!code) {
                            alert('请输入要审查的代码！');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('code', code);
                        
                        const res = await fetch('/api/code/review', { method: 'POST', body: formData });
                        const data = await res.json();
                        if (data.success) {
                            document.getElementById('reviewResult').style.display = 'block';
                            document.getElementById('reviewResultText').textContent = data.data;
                        }
                    }
                    
                    async function generateScaffold() {
                        const name = document.getElementById('projectName').value.trim();
                        const type = document.getElementById('projectType').value;
                        const pkg = document.getElementById('packageName').value.trim();
                        
                        if (!name) {
                            alert('请输入项目名称！');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('name', name);
                        formData.append('type', type);
                        formData.append('package', pkg);
                        
                        const res = await fetch('/api/scaffold/generate', { method: 'POST', body: formData });
                        const data = await res.json();
                        if (data.success) {
                            document.getElementById('scaffoldResult').style.display = 'block';
                            document.getElementById('scaffoldResultText').textContent = data.data;
                        }
                    }
                    
                    function switchTab(tab) {
                        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
                        document.querySelectorAll('.content').forEach(c => c.classList.remove('active'));
                        
                        document.querySelector('[onclick="switchTab(\\'' + tab + '\\')"]').classList.add('active');
                        document.getElementById(tab + '-content').classList.add('active');
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    document.getElementById('messageInput').addEventListener('keypress', e => {
                        if (e.key === 'Enter' && !e.shiftKey) {
                            e.preventDefault();
                            sendMessage();
                        }
                    });
                    
                    loadSessions();
                    loadTemplates();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void listSessions(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", chatSessions.values().stream()
                .map(s -> Map.of(
                    "id", s.id,
                    "title", s.title,
                    "messageCount", s.messages.size()
                ))
                .toList()
        ));
    }
    
    private static void createSession(Request req, Response res) {
        String title = req.formParam("title", "新对话");
        int id = sessionIdGenerator.getAndIncrement();
        ChatSession session = new ChatSession(id, title);
        chatSessions.put(id, session);
        
        res.status(201).json(Map.of(
            "success", true,
            "data", session
        ));
    }
    
    private static void getSession(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        ChatSession session = chatSessions.get(id);
        if (session != null) {
            res.json(Map.of("success", true, "data", session));
        } else {
            res.status(404).json(Map.of("success", false, "message", "会话不存在"));
        }
    }
    
    private static void sendMessage(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        ChatSession session = chatSessions.get(id);
        
        if (session == null) {
            res.status(404).json(Map.of("success", false, "message", "会话不存在"));
            return;
        }
        
        String content = req.formParam("content");
        if (content == null || content.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "消息内容不能为空"));
            return;
        }
        
        session.messages.add(new ChatMessage("user", content));
        
        String response = aiAssistant.chat(content);
        session.messages.add(new ChatMessage("assistant", response));
        
        res.json(Map.of(
            "success", true,
            "data", response
        ));
    }
    
    private static void deleteSession(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        if (chatSessions.remove(id) != null) {
            res.json(Map.of("success", true, "message", "会话删除成功"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "会话不存在"));
        }
    }
    
    private static void listTemplates(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", templateRegistry.getAll().stream()
                .map(t -> Map.of(
                    "name", t.getName(),
                    "template", t.getTemplate(),
                    "variables", t.getVariables()
                ))
                .toList()
        ));
    }
    
    private static void applyTemplate(Request req, Response res) {
        String templateName = req.formParam("templateName");
        PromptTemplate template = templateRegistry.get(templateName);
        
        if (template == null) {
            res.status(404).json(Map.of("success", false, "message", "模板不存在"));
            return;
        }
        
        Map<String, String> variables = new HashMap<>();
        for (String varName : template.getVariables().keySet()) {
            String value = req.formParam("var_" + varName, "");
            variables.put(varName, value);
        }
        
        String result = template.apply(variables);
        
        res.json(Map.of(
            "success", true,
            "data", result
        ));
    }
    
    private static void generateCode(Request req, Response res) {
        String description = req.formParam("description");
        if (description == null || description.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "描述不能为空"));
            return;
        }
        
        String code = codeGenerator.generate(description);
        
        res.json(Map.of(
            "success", true,
            "data", code
        ));
    }
    
    private static void reviewCode(Request req, Response res) {
        String code = req.formParam("code");
        if (code == null || code.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "代码不能为空"));
            return;
        }
        
        PromptTemplate reviewTemplate = templateRegistry.get("code-review");
        String review = reviewTemplate.apply(Map.of("code", code));
        
        res.json(Map.of(
            "success", true,
            "data", review
        ));
    }
    
    private static void generateScaffold(Request req, Response res) {
        String name = req.formParam("name");
        String type = req.formParam("type", "web");
        String pkg = req.formParam("package", "com.example");
        
        if (name == null || name.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "项目名称不能为空"));
            return;
        }
        
        String scaffold = projectScaffold.generate(name, type, pkg);
        
        res.json(Map.of(
            "success", true,
            "data", scaffold
        ));
    }
    
    static class ChatSession {
        public int id;
        public String title;
        public List<ChatMessage> messages;
        public long createdAt;
        
        public ChatSession(int id, String title) {
            this.id = id;
            this.title = title;
            this.messages = new ArrayList<>();
            this.createdAt = System.currentTimeMillis();
        }
    }
    
    static class ChatMessage {
        public String role;
        public String content;
        public long timestamp;
        
        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
