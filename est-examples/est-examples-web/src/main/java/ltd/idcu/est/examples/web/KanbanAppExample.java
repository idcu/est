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
        tasks.put(1, new Task(1, "Design database schema", "Design user, order, and product tables", "todo", "high", "Zhang San"));
        tasks.put(2, new Task(2, "Develop user auth module", "Implement login and registration", "in-progress", "high", "Li Si"));
        tasks.put(3, new Task(3, "Write API documentation", "Write detailed docs for all endpoints", "todo", "medium", "Wang Wu"));
        tasks.put(4, new Task(4, "Frontend page development", "Develop main and detail pages", "in-progress", "medium", "Zhao Liu"));
        tasks.put(5, new Task(5, "Unit testing", "Write unit tests for core modules", "done", "low", "Zhang San"));
        tasks.put(6, new Task(6, "Performance optimization", "Optimize database query performance", "review", "high", "Li Si"));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("Task Board Application - Kanban App");
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
            System.out.println("\n[OK] Kanban server started successfully!");
            System.out.println("\nAccess URLs:");
            System.out.println("  - http://localhost:8080          (Board Interface)");
            System.out.println("\nAPI Endpoints:");
            System.out.println("  - GET    /api/tasks              - Get all tasks");
            System.out.println("  - GET    /api/tasks/:id          - Get single task");
            System.out.println("  - POST   /api/tasks              - Create task");
            System.out.println("  - PUT    /api/tasks/:id          - Update task");
            System.out.println("  - PATCH  /api/tasks/:id/status   - Update task status");
            System.out.println("  - DELETE /api/tasks/:id          - Delete task");
            System.out.println("\n[X] Ctrl+C to stop server");
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
                <title>Task Board</title>
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
                    <h1>📋 Task Board</h1>
                    <button onclick="openCreateModal()">+ Add Task</button>
                </div>
                
                <div class="container">
                    <div class="stats-bar" id="statsBar"></div>
                    
                    <div class="kanban-board">
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">📝 Todo</span>
                                <span class="column-count" id="todo-count">0</span>
                            </div>
                            <div class="task-list" id="todo"></div>
                        </div>
                        
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">🚀 In Progress</span>
                                <span class="column-count" id="in-progress-count">0</span>
                            </div>
                            <div class="task-list" id="in-progress"></div>
                        </div>
                        
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">👀 Review</span>
                                <span class="column-count" id="review-count">0</span>
                            </div>
                            <div class="task-list" id="review"></div>
                        </div>
                        
                        <div class="column">
                            <div class="column-header">
                                <span class="column-title">✅ Done</span>
                                <span class="column-count" id="done-count">0</span>
                            </div>
                            <div class="task-list" id="done"></div>
                        </div>
                    </div>
                </div>
                
                <div class="modal-overlay" id="createModal">
                    <div class="modal">
                        <h2>➕ Create New Task</h2>
                        <div class="form-group">
                            <label>Task Title</label>
                            <input type="text" id="create-title" placeholder="Enter task title..." />
                        </div>
                        <div class="form-group">
                            <label>Task Description</label>
                            <textarea id="create-description" placeholder="Enter task description..."></textarea>
                        </div>
                        <div class="form-group">
                            <label>Priority</label>
                            <select id="create-priority">
                                <option value="low">Low</option>
                                <option value="medium" selected>Medium</option>
                                <option value="high">High</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Assignee</label>
                            <input type="text" id="create-assignee" placeholder="Enter assignee..." />
                        </div>
                        <div class="modal-actions">
                            <button class="btn btn-secondary" onclick="closeCreateModal()">Cancel</button>
                            <button class="btn btn-primary" onclick="createTask()">Create</button>
                        </div>
                    </div>
                </div>
                
                <div class="modal-overlay" id="editModal">
                    <div class="modal">
                        <h2>✏️ Edit Task</h2>
                        <input type="hidden" id="edit-id" />
                        <div class="form-group">
                            <label>Task Title</label>
                            <input type="text" id="edit-title" />
                        </div>
                        <div class="form-group">
                            <label>Task Description</label>
                            <textarea id="edit-description"></textarea>
                        </div>
                        <div class="form-group">
                            <label>Status</label>
                            <select id="edit-status">
                                <option value="todo">Todo</option>
                                <option value="in-progress">In Progress</option>
                                <option value="review">Review</option>
                                <option value="done">Done</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Priority</label>
                            <select id="edit-priority">
                                <option value="low">Low</option>
                                <option value="medium">Medium</option>
                                <option value="high">High</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Assignee</label>
                            <input type="text" id="edit-assignee" />
                        </div>
                        <div class="modal-actions">
                            <button class="btn btn-danger" onclick="deleteTask()" style="margin-right: auto;">Delete</button>
                            <button class="btn btn-secondary" onclick="closeEditModal()">Cancel</button>
                            <button class="btn btn-primary" onclick="updateTask()">Save</button>
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
                                        <span class="task-assignee">👤 \${escapeHtml(task.assignee || 'Unassigned')}</span>
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
                                <div class="stat-label">Total Tasks</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${todo}</div>
                                <div class="stat-label">Todo</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${inProgress}</div>
                                <div class="stat-label">In Progress</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${review}</div>
                                <div class="stat-label">Review</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">\${done}</div>
                                <div class="stat-label">Done</div>
                            </div>
                        \`;
                    }
                    
                    function getPriorityText(priority) {
                        const map = { high: 'High', medium: 'Medium', low: 'Low' };
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
                            alert('Please enter task title!');
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
                            alert('Please enter task title!');
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
                        if (confirm('Are you sure you want to delete this task?')) {
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
            res.status(404).json(Map.of("success", false, "message", "Task not found"));
        }
    }
    
    private static void createTask(Request req, Response res) {
        String title = req.formParam("title");
        String description = req.formParam("description", "");
        String priority = req.formParam("priority", "medium");
        String assignee = req.formParam("assignee", "");
        
        if (title == null || title.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "Task title cannot be empty"));
            return;
        }
        
        int id = taskIdGenerator.getAndIncrement();
        Task task = new Task(id, title, description, "todo", priority, assignee);
        tasks.put(id, task);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "Task created successfully",
            "data", task
        ));
    }
    
    private static void updateTask(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Task task = tasks.get(id);
        
        if (task == null) {
            res.status(404).json(Map.of("success", false, "message", "Task not found"));
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
            "message", "Task updated successfully",
            "data", task
        ));
    }
    
    private static void updateTaskStatus(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Task task = tasks.get(id);
        
        if (task == null) {
            res.status(404).json(Map.of("success", false, "message", "Task not found"));
            return;
        }
        
        String status = req.formParam("status");
        if (status != null) {
            task.status = status;
        }
        
        res.json(Map.of(
            "success", true,
            "message", "Task status updated successfully",
            "data", task
        ));
    }
    
    private static void deleteTask(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        if (tasks.remove(id) != null) {
            res.json(Map.of("success", true, "message", "Task deleted successfully"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "Task not found"));
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
