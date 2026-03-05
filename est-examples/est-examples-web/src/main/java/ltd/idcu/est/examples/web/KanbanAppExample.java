package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class KanbanAppExample {
    
    private static final Map<Integer, Task> tasks = new ConcurrentHashMap<>();
    private static final AtomicInteger taskIdGenerator = new AtomicInteger(1);
    
    static {
        tasks.put(1, new Task(1, "设计数据库架构", "设计用户表、订单表、商品表等", "todo", "high", "张三"));
        tasks.put(2, new Task(2, "开发用户认证模块", "实现登录注册功能", "in-progress", "high", "李四"));
        tasks.put(3, new Task(3, "编写API文档", "为所有接口编写详细文档", "todo", "medium", "王五"));
        tasks.put(4, new Task(4, "前端页面开发", "开发主页面和详情页", "in-progress", "medium", "赵六"));
        tasks.put(5, new Task(5, "单元测试", "为核心模块编写单元测试", "done", "low", "张三"));
        tasks.put(6, new Task(6, "性能优化", "优化数据库查询性能", "review", "high", "李四"));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("任务看板应用 - Kanban App");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("Kanban App", "1.0.0");
        
        app.enableCors();
        
        app.get("/", KanbanAppExample::homePage);
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.group("/tasks", (taskRouter, taskGroup) -> {
                    taskRouter.get("", KanbanAppExample::listTasks);
                    taskRouter.get("/:id", KanbanAppExample::getTask);
                    taskRouter.post("", KanbanAppExample::createTask);
                    taskRouter.put("/:id", KanbanAppExample::updateTask);
                    taskRouter.patch("/:id/status", KanbanAppExample::updateTaskStatus);
                    taskRouter.delete("/:id", KanbanAppExample::deleteTask);
                });
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ 任务看板服务器启动成功！");
            System.out.println("\n访问地址：");
            System.out.println("  - http://localhost:8080          (看板界面)");
            System.out.println("\nAPI 端点：");
            System.out.println("  - GET    /api/tasks              - 获取所有任务");
            System.out.println("  - GET    /api/tasks/:id          - 获取单个任务");
            System.out.println("  - POST   /api/tasks              - 创建任务");
            System.out.println("  - PUT    /api/tasks/:id          - 更新任务");
            System.out.println("  - PATCH  /api/tasks/:id/status   - 更新任务状态");
            System.out.println("  - DELETE /api/tasks/:id          - 删除任务");
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
                <title>任务看板</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: #f0f2f5; min-height: 100vh; }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px 40px; display: flex; justify-content: space-between; align-items: center; }
                    .header h1 { font-size: 28px; }
                    .header button { padding: 12px 24px; background: rgba(255,255,255,0.2); color: white; border: 2px solid white; border-radius: 8px; font-size: 16px; cursor: pointer; transition: all 0.3s; }
                    .header button:hover { background: white; color: #667eea; }
                    .container { padding: 30px 40px; }
                    .kanban-board { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
                    .column { background: #ebecf0; border-radius: 10px; padding: 15px; min-height: 500px; }
                    .column-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding: 10px 0; }
                    .column-title { font-size: 16px; font-weight: 600; color: #333; }
                    .column-count { background: rgba(0,0,0,0.1); padding: 4px 10px; border-radius: 12px; font-size: 14px; font-weight: 500; }
                    .task-list { display: flex; flex-direction: column; gap: 10px; min-height: 100px; }
                    .task-card { background: white; border-radius: 8px; padding: 15px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); cursor: pointer; transition: all 0.2s; }
                    .task-card:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
                    .task-title { font-size: 15px; font-weight: 600; color: #333; margin-bottom: 8px; }
                    .task-description { font-size: 13px; color: #666; margin-bottom: 12px; line-height: 1.5; }
                    .task-footer { display: flex; justify-content: space-between; align-items: center; }
                    .task-priority { padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: 600; }
                    .priority-high { background: #ff4757; color: white; }
                    .priority-medium { background: #ffa502; color: white; }
                    .priority-low { background: #2ed573; color: white; }
                    .task-assignee { font-size: 13px; color: #999; display: flex; align-items: center; gap: 5px; }
                    .modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: none; align-items: center; justify-content: center; z-index: 1000; }
                    .modal-overlay.active { display: flex; }
                    .modal { background: white; border-radius: 12px; padding: 30px; width: 500px; max-width: 90%; box-shadow: 0 4px 20px rgba(0,0,0,0.3); }
                    .modal h2 { margin-bottom: 20px; color: #333; }
                    .form-group { margin-bottom: 15px; }
                    .form-group label { display: block; margin-bottom: 6px; color: #333; font-weight: 500; }
                    .form-group input, .form-group textarea, .form-group select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; }
                    .form-group textarea { height: 80px; resize: vertical; }
                    .modal-actions { display: flex; gap: 10px; margin-top: 25px; justify-content: flex-end; }
                    .btn { padding: 10px 20px; border: none; border-radius: 6px; font-size: 14px; cursor: pointer; }
                    .btn-primary { background: #667eea; color: white; }
                    .btn-primary:hover { background: #5a6fd6; }
                    .btn-secondary { background: #e0e0e0; color: #333; }
                    .btn-secondary:hover { background: #d0d0d0; }
                    .btn-danger { background: #ff4757; color: white; }
                    .btn-danger:hover { background: #ff3838; }
                    .stats-bar { display: flex; gap: 30px; margin-bottom: 25px; padding: 20px; background: white; border-radius: 10px; }
                    .stat-item { text-align: center; }
                    .stat-number { font-size: 28px; font-weight: bold; color: #667eea; }
                    .stat-label { font-size: 13px; color: #666; margin-top: 5px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>📋 任务看板</h1>
                    <button onclick="openCreateModal()">+ 添加任务</button>
                </div>
                
                <div class="container">
                    <div class="stats-bar" id="statsBar"></div>
                    
                    <div class="kanban-board">
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">📝 待办</span>
                                <span class="column-count" id="todo-count">0</span>
                            </div>
                            <div class="task-list" id="todo"></div>
                        </div>
                        
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">🚀 进行中</span>
                                <span class="column-count" id="in-progress-count">0</span>
                            </div>
                            <div class="task-list" id="in-progress"></div>
                        </div>
                        
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">👀 审核中</span>
                                <span class="column-count" id="review-count">0</span>
                            </div>
                            <div class="task-list" id="review"></div>
                        </div>
                        
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">✅ 已完成</span>
                                <span class="column-count" id="done-count">0</span>
                            </div>
                            <div class="task-list" id="done"></div>
                        </div>
                    </div>
                </div>
                
                <div class="modal-overlay" id="createModal">
                    <div class="modal">
                        <h2>➕ 创建新任务</h2>
                        <div class="form-group">
                            <label>任务标题</label>
                            <input type="text" id="create-title" placeholder="输入任务标题..." />
                        </div>
                        <div class="form-group">
                            <label>任务描述</label>
                            <textarea id="create-description" placeholder="输入任务描述..."></textarea>
                        </div>
                        <div class="form-group">
                            <label>优先级</label>
                            <select id="create-priority">
                                <option value="low">低</option>
                                <option value="medium" selected>中</option>
                                <option value="high">高</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>负责人</label>
                            <input type="text" id="create-assignee" placeholder="输入负责人..." />
                        </div>
                        <div class="modal-actions">
                            <button class="btn btn-secondary" onclick="closeCreateModal()">取消</button>
                            <button class="btn btn-primary" onclick="createTask()">创建</button>
                        </div>
                    </div>
                </div>
                
                <div class="modal-overlay" id="editModal">
                    <div class="modal">
                        <h2>✏️ 编辑任务</h2>
                        <input type="hidden" id="edit-id" />
                        <div class="form-group">
                            <label>任务标题</label>
                            <input type="text" id="edit-title" />
                        </div>
                        <div class="form-group">
                            <label>任务描述</label>
                            <textarea id="edit-description"></textarea>
                        </div>
                        <div class="form-group">
                            <label>状态</label>
                            <select id="edit-status">
                                <option value="todo">待办</option>
                                <option value="in-progress">进行中</option>
                                <option value="review">审核中</option>
                                <option value="done">已完成</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>优先级</label>
                            <select id="edit-priority">
                                <option value="low">低</option>
                                <option value="medium">中</option>
                                <option value="high">高</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>负责人</label>
                            <input type="text" id="edit-assignee" />
                        </div>
                        <div class="modal-actions">
                            <button class="btn btn-danger" onclick="deleteTask()" style="margin-right: auto;">删除</button>
                            <button class="btn btn-secondary" onclick="closeEditModal()">取消</button>
                            <button class="btn btn-primary" onclick="updateTask()">保存</button>
                        </div>
                    </div>
                </div>
                
                <script>
                    let tasks = [];
                    
                    async function loadTasks() {
                        const res = await fetch('/api/tasks');
                        const data = await res.json();
                        if (data.success) {
                            tasks = data.data;
                            renderTasks();
                            renderStats();
                        }
                    }
                    
                    function renderTasks() {
                        const statuses = ['todo', 'in-progress', 'review', 'done'];
                        
                        statuses.forEach(status => {
                            const container = document.getElementById(status);
                            const filteredTasks = tasks.filter(t => t.status === status);
                            
                            document.getElementById(status + '-count').textContent = filteredTasks.length;
                            
                            container.innerHTML = filteredTasks.map(task => \`
                                <div class="task-card" onclick="openEditModal(\${task.id})">
                                    <div class="task-title">\${escapeHtml(task.title)}</div>
                                    \${task.description ? '<div class="task-description">' + escapeHtml(task.description) + '</div>' : ''}
                                    <div class="task-footer">
                                        <span class="task-priority priority-\${task.priority}">\${getPriorityText(task.priority)}</span>
                                        <span class="task-assignee">👤 \${escapeHtml(task.assignee || '未分配')}</span>
                                    </div>
                                </div>
                            \`).join('');
                        });
                    }
                    
                    function renderStats() {
                        const todo = tasks.filter(t => t.status === 'todo').length;
                        const inProgress = tasks.filter(t => t.status === 'in-progress').length;
                        const review = tasks.filter(t => t.status === 'review').length;
                        const done = tasks.filter(t => t.status === 'done').length;
                        const total = tasks.length;
                        
                        document.getElementById('statsBar').innerHTML = \`
                            <div class="stat-item">
                                <div class="stat-number">\${total}</div>
                                <div class="stat-label">总任务</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${todo}</div>
                                <div class="stat-label">待办</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${inProgress}</div>
                                <div class="stat-label">进行中</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${review}</div>
                                <div class="stat-label">审核中</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${done}</div>
                                <div class="stat-label">已完成</div>
                            </div>
                        \`;
                    }
                    
                    function getPriorityText(priority) {
                        const map = { high: '高', medium: '中', low: '低' };
                        return map[priority] || priority;
                    }
                    
                    function openCreateModal() {
                        document.getElementById('createModal').classList.add('active');
                        document.getElementById('create-title').value = '';
                        document.getElementById('create-description').value = '';
                        document.getElementById('create-priority').value = 'medium';
                        document.getElementById('create-assignee').value = '';
                    }
                    
                    function closeCreateModal() {
                        document.getElementById('createModal').classList.remove('active');
                    }
                    
                    async function createTask() {
                        const title = document.getElementById('create-title').value.trim();
                        const description = document.getElementById('create-description').value.trim();
                        const priority = document.getElementById('create-priority').value;
                        const assignee = document.getElementById('create-assignee').value.trim();
                        
                        if (!title) {
                            alert('请输入任务标题！');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('title', title);
                        formData.append('description', description);
                        formData.append('priority', priority);
                        formData.append('assignee', assignee);
                        
                        await fetch('/api/tasks', { method: 'POST', body: formData });
                        closeCreateModal();
                        loadTasks();
                    }
                    
                    async function openEditModal(id) {
                        const task = tasks.find(t => t.id === id);
                        if (!task) return;
                        
                        document.getElementById('edit-id').value = task.id;
                        document.getElementById('edit-title').value = task.title;
                        document.getElementById('edit-description').value = task.description || '';
                        document.getElementById('edit-status').value = task.status;
                        document.getElementById('edit-priority').value = task.priority;
                        document.getElementById('edit-assignee').value = task.assignee || '';
                        
                        document.getElementById('editModal').classList.add('active');
                    }
                    
                    function closeEditModal() {
                        document.getElementById('editModal').classList.remove('active');
                    }
                    
                    async function updateTask() {
                        const id = document.getElementById('edit-id').value;
                        const title = document.getElementById('edit-title').value.trim();
                        const description = document.getElementById('edit-description').value.trim();
                        const status = document.getElementById('edit-status').value;
                        const priority = document.getElementById('edit-priority').value;
                        const assignee = document.getElementById('edit-assignee').value.trim();
                        
                        if (!title) {
                            alert('请输入任务标题！');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('title', title);
                        formData.append('description', description);
                        formData.append('status', status);
                        formData.append('priority', priority);
                        formData.append('assignee', assignee);
                        
                        await fetch('/api/tasks/' + id, { method: 'PUT', body: formData });
                        closeEditModal();
                        loadTasks();
                    }
                    
                    async function deleteTask() {
                        const id = document.getElementById('edit-id').value;
                        if (confirm('确定要删除这个任务吗？')) {
                            await fetch('/api/tasks/' + id, { method: 'DELETE' });
                            closeEditModal();
                            loadTasks();
                        }
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    loadTasks();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void listTasks(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", new ArrayList<>(tasks.values())
        ));
    }
    
    private static void getTask(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Task task = tasks.get(id);
        if (task != null) {
            res.json(Map.of("success", true, "data", task));
        } else {
            res.status(404).json(Map.of("success", false, "message", "任务不存在"));
        }
    }
    
    private static void createTask(Request req, Response res) {
        String title = req.formParam("title");
        String description = req.formParam("description", "");
        String priority = req.formParam("priority", "medium");
        String assignee = req.formParam("assignee", "");
        
        if (title == null || title.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "任务标题不能为空"));
            return;
        }
        
        int id = taskIdGenerator.getAndIncrement();
        Task task = new Task(id, title, description, "todo", priority, assignee);
        tasks.put(id, task);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "任务创建成功",
            "data", task
        ));
    }
    
    private static void updateTask(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Task task = tasks.get(id);
        
        if (task == null) {
            res.status(404).json(Map.of("success", false, "message", "任务不存在"));
            return;
        }
        
        String title = req.formParam("title");
        String description = req.formParam("description");
        String status = req.formParam("status");
        String priority = req.formParam("priority");
        String assignee = req.formParam("assignee");
        
        if (title != null) task.title = title;
        if (description != null) task.description = description;
        if (status != null) task.status = status;
        if (priority != null) task.priority = priority;
        if (assignee != null) task.assignee = assignee;
        
        res.json(Map.of(
            "success", true,
            "message", "任务更新成功",
            "data", task
        ));
    }
    
    private static void updateTaskStatus(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Task task = tasks.get(id);
        
        if (task == null) {
            res.status(404).json(Map.of("success", false, "message", "任务不存在"));
            return;
        }
        
        String status = req.formParam("status");
        if (status != null) {
            task.status = status;
        }
        
        res.json(Map.of(
            "success", true,
            "message", "任务状态更新成功",
            "data", task
        ));
    }
    
    private static void deleteTask(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        if (tasks.remove(id) != null) {
            res.json(Map.of("success", true, "message", "任务删除成功"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "任务不存在"));
        }
    }
    
    static class Task {
        public int id;
        public String title;
        public String description;
        public String status;
        public String priority;
        public String assignee;
        public long createdAt;
        
        public Task(int id, String title, String description, String status, String priority, String assignee) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.status = status;
            this.priority = priority;
            this.assignee = assignee;
            this.createdAt = System.currentTimeMillis();
        }
    }
}
