package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TodoAppExample {
    
    private static final Map<String, Todo> todos = new ConcurrentHashMap<>();
    
    static {
        todos.put("1", new Todo("1", "学习EST框架", "了解EST框架的核心功能", false, "high"));
        todos.put("2", new Todo("2", "创建第一个Web应用", "使用EST创建一个简单的Web应用", true, "medium"));
        todos.put("3", new Todo("3", "探索更多功能", "了解EST的其他功能模块", false, "low"));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("待办事项管理应用 - Todo App");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("Todo App", "1.0.0");
        
        app.enableCors();
        
        app.get("/", TodoAppExample::homePage);
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.group("/todos", (todoRouter, todoGroup) -> {
                    todoRouter.get("", TodoAppExample::listTodos);
                    todoRouter.get("/:id", TodoAppExample::getTodo);
                    todoRouter.post("", TodoAppExample::createTodo);
                    todoRouter.put("/:id", TodoAppExample::updateTodo);
                    todoRouter.delete("/:id", TodoAppExample::deleteTodo);
                    todoRouter.patch("/:id/complete", TodoAppExample::toggleComplete);
                });
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ Todo App 服务器启动成功！");
            System.out.println("\n访问地址：");
            System.out.println("  - http://localhost:8080          (Web 界面)");
            System.out.println("\nAPI 端点：");
            System.out.println("  - GET    /api/todos           - 获取所有待办");
            System.out.println("  - GET    /api/todos/:id       - 获取单个待办");
            System.out.println("  - POST   /api/todos           - 创建待办");
            System.out.println("  - PUT    /api/todos/:id       - 更新待办");
            System.out.println("  - DELETE /api/todos/:id       - 删除待办");
            System.out.println("  - PATCH  /api/todos/:id/complete - 切换完成状态");
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
                <title>待办事项管理</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; background: #f5f5f5; }
                    .container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    h1 { color: #333; margin-bottom: 30px; text-align: center; }
                    .form { display: flex; gap: 10px; margin-bottom: 30px; }
                    .form input[type="text"] { flex: 1; padding: 12px; border: 2px solid #ddd; border-radius: 5px; font-size: 16px; }
                    .form select { padding: 12px; border: 2px solid #ddd; border-radius: 5px; font-size: 16px; }
                    .form button { padding: 12px 30px; background: #007bff; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; }
                    .form button:hover { background: #0056b3; }
                    .todo-list { list-style: none; }
                    .todo-item { display: flex; align-items: center; padding: 15px; border: 1px solid #eee; margin-bottom: 10px; border-radius: 5px; transition: all 0.3s; }
                    .todo-item:hover { background: #f9f9f9; }
                    .todo-item.completed { opacity: 0.6; }
                    .todo-item.completed .todo-text { text-decoration: line-through; color: #999; }
                    .todo-checkbox { width: 22px; height: 22px; margin-right: 15px; cursor: pointer; }
                    .todo-content { flex: 1; }
                    .todo-text { font-size: 18px; margin-bottom: 5px; }
                    .todo-desc { color: #666; font-size: 14px; }
                    .todo-priority { padding: 4px 10px; border-radius: 3px; font-size: 12px; margin-right: 10px; }
                    .priority-high { background: #ff4444; color: white; }
                    .priority-medium { background: #ffaa00; color: white; }
                    .priority-low { background: #4CAF50; color: white; }
                    .todo-actions { display: flex; gap: 5px; }
                    .todo-actions button { padding: 8px 15px; border: none; border-radius: 3px; cursor: pointer; font-size: 14px; }
                    .btn-delete { background: #ff4444; color: white; }
                    .btn-delete:hover { background: #cc0000; }
                    .stats { display: flex; justify-content: center; gap: 30px; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; }
                    .stat { text-align: center; }
                    .stat-number { font-size: 28px; font-weight: bold; color: #007bff; }
                    .stat-label { color: #666; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>📝 待办事项管理</h1>
                    
                    <div class="form">
                        <input type="text" id="title" placeholder="待办标题..." />
                        <input type="text" id="desc" placeholder="描述（可选）..." />
                        <select id="priority">
                            <option value="low">低优先级</option>
                            <option value="medium" selected>中优先级</option>
                            <option value="high">高优先级</option>
                        </select>
                        <button onclick="addTodo()">添加</button>
                    </div>
                    
                    <ul class="todo-list" id="todoList"></ul>
                    
                    <div class="stats">
                        <div class="stat">
                            <div class="stat-number" id="totalCount">0</div>
                            <div class="stat-label">总计</div>
                        </div>
                        <div class="stat">
                            <div class="stat-number" id="completedCount">0</div>
                            <div class="stat-label">已完成</div>
                        </div>
                        <div class="stat">
                            <div class="stat-number" id="pendingCount">0</div>
                            <div class="stat-label">待处理</div>
                        </div>
                    </div>
                </div>
                
                <script>
                    let todos = [];
                    
                    async function loadTodos() {
                        const res = await fetch('/api/todos');
                        const data = await res.json();
                        todos = data.data || [];
                        renderTodos();
                        updateStats();
                    }
                    
                    async function addTodo() {
                        const title = document.getElementById('title').value;
                        const desc = document.getElementById('desc').value;
                        const priority = document.getElementById('priority').value;
                        
                        if (!title.trim()) return;
                        
                        const formData = new FormData();
                        formData.append('title', title);
                        formData.append('description', desc);
                        formData.append('priority', priority);
                        
                        await fetch('/api/todos', { method: 'POST', body: formData });
                        document.getElementById('title').value = '';
                        document.getElementById('desc').value = '';
                        loadTodos();
                    }
                    
                    async function toggleTodo(id) {
                        await fetch('/api/todos/' + id + '/complete', { method: 'PATCH' });
                        loadTodos();
                    }
                    
                    async function deleteTodo(id) {
                        await fetch('/api/todos/' + id, { method: 'DELETE' });
                        loadTodos();
                    }
                    
                    function renderTodos() {
                        const list = document.getElementById('todoList');
                        list.innerHTML = todos.map(todo => \`
                            <li class="todo-item \${todo.completed ? 'completed' : ''}">
                                <input type="checkbox" class="todo-checkbox" 
                                    \${todo.completed ? 'checked' : ''} 
                                    onchange="toggleTodo('\${todo.id}')" />
                                <div class="todo-content">
                                    <div class="todo-text">\${escapeHtml(todo.title)}</div>
                                    <div class="todo-desc">\${escapeHtml(todo.description || '')}</div>
                                </div>
                                <span class="todo-priority priority-\${todo.priority}">\${getPriorityText(todo.priority)}</span>
                                <div class="todo-actions">
                                    <button class="btn-delete" onclick="deleteTodo('\${todo.id}')">删除</button>
                                </div>
                            </li>
                        \`).join('');
                    }
                    
                    function updateStats() {
                        const total = todos.length;
                        const completed = todos.filter(t => t.completed).length;
                        const pending = total - completed;
                        
                        document.getElementById('totalCount').textContent = total;
                        document.getElementById('completedCount').textContent = completed;
                        document.getElementById('pendingCount').textContent = pending;
                    }
                    
                    function getPriorityText(priority) {
                        const map = { high: '高', medium: '中', low: '低' };
                        return map[priority] || priority;
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    document.getElementById('title').addEventListener('keypress', e => {
                        if (e.key === 'Enter') addTodo();
                    });
                    
                    loadTodos();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void listTodos(Request req, Response res) {
        String filter = req.queryParam("filter");
        
        var filtered = todos.values().stream();
        if ("active".equals(filter)) {
            filtered = filtered.filter(t -> !t.completed);
        } else if ("completed".equals(filter)) {
            filtered = filtered.filter(t -> t.completed);
        }
        
        res.json(Map.of(
            "success", true,
            "data", filtered.collect(Collectors.toList())
        ));
    }
    
    private static void getTodo(Request req, Response res) {
        String id = req.param("id");
        Todo todo = todos.get(id);
        if (todo != null) {
            res.json(Map.of("success", true, "data", todo));
        } else {
            res.status(404).json(Map.of("success", false, "message", "待办不存在"));
        }
    }
    
    private static void createTodo(Request req, Response res) {
        String title = req.formParam("title");
        String description = req.formParam("description", "");
        String priority = req.formParam("priority", "medium");
        
        if (title == null || title.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "标题不能为空"));
            return;
        }
        
        String id = UUID.randomUUID().toString();
        Todo todo = new Todo(id, title, description, false, priority);
        todos.put(id, todo);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "待办创建成功",
            "data", todo
        ));
    }
    
    private static void updateTodo(Request req, Response res) {
        String id = req.param("id");
        Todo todo = todos.get(id);
        
        if (todo == null) {
            res.status(404).json(Map.of("success", false, "message", "待办不存在"));
            return;
        }
        
        String title = req.formParam("title");
        String description = req.formParam("description");
        String priority = req.formParam("priority");
        
        if (title != null) todo.title = title;
        if (description != null) todo.description = description;
        if (priority != null) todo.priority = priority;
        
        res.json(Map.of(
            "success", true,
            "message", "待办更新成功",
            "data", todo
        ));
    }
    
    private static void deleteTodo(Request req, Response res) {
        String id = req.param("id");
        if (todos.remove(id) != null) {
            res.json(Map.of("success", true, "message", "待办删除成功"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "待办不存在"));
        }
    }
    
    private static void toggleComplete(Request req, Response res) {
        String id = req.param("id");
        Todo todo = todos.get(id);
        
        if (todo == null) {
            res.status(404).json(Map.of("success", false, "message", "待办不存在"));
            return;
        }
        
        todo.completed = !todo.completed;
        res.json(Map.of(
            "success", true,
            "message", "状态更新成功",
            "data", todo
        ));
    }
    
    static class Todo {
        public String id;
        public String title;
        public String description;
        public boolean completed;
        public String priority;
        public long createdAt;
        
        public Todo(String id, String title, String description, boolean completed, String priority) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.completed = completed;
            this.priority = priority;
            this.createdAt = System.currentTimeMillis();
        }
    }
}
