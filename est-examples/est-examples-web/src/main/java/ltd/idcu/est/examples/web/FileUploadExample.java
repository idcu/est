package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileUploadExample {
    
    private static final Map<String, UploadedFile> files = new ConcurrentHashMap<>();
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("文件上传示例 - File Upload");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("File Upload Example", "1.0.0");
        
        app.enableCors();
        
        app.get("/", FileUploadExample::homePage);
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.get("/files", FileUploadExample::listFiles);
                r.post("/upload", FileUploadExample::uploadFile);
                r.get("/download/:id", FileUploadExample::downloadFile);
                r.delete("/files/:id", FileUploadExample::deleteFile);
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ 文件上传服务器启动成功！");
            System.out.println("\n访问地址：");
            System.out.println("  - http://localhost:8080          (文件管理界面)");
            System.out.println("\nAPI 端点：");
            System.out.println("  - GET    /api/files             - 获取文件列表");
            System.out.println("  - POST   /api/upload            - 上传文件");
            System.out.println("  - GET    /api/download/:id     - 下载文件");
            System.out.println("  - DELETE /api/files/:id        - 删除文件");
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
                <title>文件管理</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: #f0f2f5; min-height: 100vh; }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px; text-align: center; }
                    .header h1 { font-size: 36px; margin-bottom: 10px; }
                    .header p { font-size: 16px; opacity: 0.9; }
                    .container { max-width: 1200px; margin: 40px auto; padding: 0 20px; }
                    .upload-area { background: white; border-radius: 12px; padding: 40px; margin-bottom: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; border: 3px dashed #ddd; transition: all 0.3s; }
                    .upload-area:hover, .upload-area.dragover { border-color: #667eea; background: #f5f7ff; }
                    .upload-area h2 { color: #333; margin-bottom: 20px; }
                    .upload-area p { color: #666; margin-bottom: 20px; }
                    .upload-btn { padding: 15px 40px; background: #667eea; color: white; border: none; border-radius: 8px; font-size: 18px; cursor: pointer; }
                    .upload-btn:hover { background: #5a6fd6; }
                    .file-input { display: none; }
                    .file-list { background: white; border-radius: 12px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .file-list h2 { color: #333; margin-bottom: 20px; padding-bottom: 15px; border-bottom: 2px solid #f0f0f0; }
                    .file-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 15px; }
                    .file-card { border: 1px solid #eee; border-radius: 8px; padding: 20px; transition: all 0.2s; }
                    .file-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); transform: translateY(-2px); }
                    .file-icon { font-size: 48px; margin-bottom: 10px; }
                    .file-name { font-weight: 600; color: #333; margin-bottom: 5px; word-break: break-all; }
                    .file-info { color: #999; font-size: 13px; margin-bottom: 15px; }
                    .file-actions { display: flex; gap: 10px; }
                    .file-actions button { flex: 1; padding: 8px 12px; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; }
                    .btn-download { background: #2ecc71; color: white; }
                    .btn-download:hover { background: #27ae60; }
                    .btn-delete { background: #e74c3c; color: white; }
                    .btn-delete:hover { background: #c0392b; }
                    .empty-state { text-align: center; padding: 60px 20px; color: #999; }
                    .empty-state .icon { font-size: 64px; margin-bottom: 20px; }
                    .progress-bar { width: 100%; height: 8px; background: #eee; border-radius: 4px; margin-top: 15px; display: none; }
                    .progress-bar.active { display: block; }
                    .progress-fill { height: 100%; background: #667eea; border-radius: 4px; width: 0%; transition: width 0.3s; }
                    .stats { display: flex; gap: 30px; margin-bottom: 25px; }
                    .stat-card { flex: 1; background: white; border-radius: 10px; padding: 20px; text-align: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .stat-number { font-size: 32px; font-weight: bold; color: #667eea; }
                    .stat-label { color: #666; font-size: 14px; margin-top: 5px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>📁 文件管理系统</h1>
                    <p>上传、下载和管理您的文件</p>
                </div>
                
                <div class="container">
                    <div class="stats" id="stats"></div>
                    
                    <div class="upload-area" id="uploadArea">
                        <h2>⬆️ 上传文件</h2>
                        <p>拖放文件到此处，或点击选择文件</p>
                        <input type="file" id="fileInput" class="file-input" multiple />
                        <button class="upload-btn" onclick="document.getElementById('fileInput').click()">选择文件</button>
                        <div class="progress-bar" id="progressBar">
                            <div class="progress-fill" id="progressFill"></div>
                        </div>
                    </div>
                    
                    <div class="file-list">
                        <h2>📂 文件列表</h2>
                        <div class="file-grid" id="fileGrid"></div>
                    </div>
                </div>
                
                <script>
                    let files = [];
                    
                    async function loadFiles() {
                        const res = await fetch('/api/files');
                        const data = await res.json();
                        if (data.success) {
                            files = data.data;
                            renderFiles();
                            renderStats();
                        }
                    }
                    
                    function renderFiles() {
                        const container = document.getElementById('fileGrid');
                        
                        if (files.length === 0) {
                            container.innerHTML = \`
                                <div class="empty-state">
                                    <div class="icon">📭</div>
                                    <p>暂无文件，快去上传吧！</p>
                                </div>
                            \`;
                            return;
                        }
                        
                        container.innerHTML = files.map(file => \`
                            <div class="file-card">
                                <div class="file-icon">\${getFileIcon(file.type)}</div>
                                <div class="file-name">\${escapeHtml(file.name)}</div>
                                <div class="file-info">\${formatSize(file.size)} · \${formatDate(file.uploadedAt)}</div>
                                <div class="file-actions">
                                    <button class="btn-download" onclick="downloadFile('\${file.id}')">下载</button>
                                    <button class="btn-delete" onclick="deleteFile('\${file.id}')">删除</button>
                                </div>
                            </div>
                        \`).join('');
                    }
                    
                    function renderStats() {
                        const totalFiles = files.length;
                        const totalSize = files.reduce((sum, f) => sum + f.size, 0);
                        
                        document.getElementById('stats').innerHTML = \`
                            <div class="stat-card">
                                <div class="stat-number">\${totalFiles}</div>
                                <div class="stat-label">文件数量</div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-number">\${formatSize(totalSize)}</div>
                                <div class="stat-label">总大小</div>
                            </div>
                        \`;
                    }
                    
                    function getFileIcon(type) {
                        if (type.startsWith('image/')) return '🖼️';
                        if (type.startsWith('video/')) return '🎬';
                        if (type.startsWith('audio/')) return '🎵';
                        if (type.includes('pdf')) return '📄';
                        if (type.includes('word') || type.includes('document')) return '📝';
                        if (type.includes('sheet') || type.includes('excel')) return '📊';
                        return '📁';
                    }
                    
                    function formatSize(bytes) {
                        if (bytes === 0) return '0 B';
                        const k = 1024;
                        const sizes = ['B', 'KB', 'MB', 'GB'];
                        const i = Math.floor(Math.log(bytes) / Math.log(k));
                        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
                    }
                    
                    function formatDate(timestamp) {
                        return new Date(timestamp).toLocaleString('zh-CN');
                    }
                    
                    async function uploadFile(file) {
                        const formData = new FormData();
                        formData.append('file', file);
                        
                        const progressBar = document.getElementById('progressBar');
                        const progressFill = document.getElementById('progressFill');
                        
                        progressBar.classList.add('active');
                        progressFill.style.width = '30%';
                        
                        try {
                            const res = await fetch('/api/upload', {
                                method: 'POST',
                                body: formData
                            });
                            
                            progressFill.style.width = '100%';
                            
                            setTimeout(() => {
                                progressBar.classList.remove('active');
                                progressFill.style.width = '0%';
                            }, 500);
                            
                            loadFiles();
                        } catch (error) {
                            alert('上传失败：' + error.message);
                            progressBar.classList.remove('active');
                        }
                    }
                    
                    async function downloadFile(id) {
                        window.location.href = '/api/download/' + id;
                    }
                    
                    async function deleteFile(id) {
                        if (confirm('确定要删除这个文件吗？')) {
                            await fetch('/api/files/' + id, { method: 'DELETE' });
                            loadFiles();
                        }
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    const fileInput = document.getElementById('fileInput');
                    fileInput.addEventListener('change', (e) => {
                        Array.from(e.target.files).forEach(file => {
                            uploadFile(file);
                        });
                        fileInput.value = '';
                    });
                    
                    const uploadArea = document.getElementById('uploadArea');
                    
                    uploadArea.addEventListener('dragover', (e) => {
                        e.preventDefault();
                        uploadArea.classList.add('dragover');
                    });
                    
                    uploadArea.addEventListener('dragleave', () => {
                        uploadArea.classList.remove('dragover');
                    });
                    
                    uploadArea.addEventListener('drop', (e) => {
                        e.preventDefault();
                        uploadArea.classList.remove('dragover');
                        Array.from(e.dataTransfer.files).forEach(file => {
                            uploadFile(file);
                        });
                    });
                    
                    loadFiles();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void listFiles(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", new ArrayList<>(files.values())
        ));
    }
    
    private static void uploadFile(Request req, Response res) {
        try {
            MultipartFile file = req.file("file");
            if (file == null) {
                res.status(400).json(Map.of("success", false, "message", "请选择文件"));
                return;
            }
            
            String id = UUID.randomUUID().toString();
            UploadedFile uploadedFile = new UploadedFile(
                id,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes(),
                System.currentTimeMillis()
            );
            files.put(id, uploadedFile);
            
            res.status(201).json(Map.of(
                "success", true,
                "message", "文件上传成功",
                "data", uploadedFile
            ));
        } catch (Exception e) {
            res.status(500).json(Map.of("success", false, "message", "上传失败：" + e.getMessage()));
        }
    }
    
    private static void downloadFile(Request req, Response res) {
        String id = req.param("id");
        UploadedFile file = files.get(id);
        if (file == null) {
            res.status(404).json(Map.of("success", false, "message", "文件不存在"));
            return;
        }
        
        res.header("Content-Disposition", "attachment; filename=\"" + file.name + "\"");
        res.header("Content-Type", file.type);
        res.send(file.content);
    }
    
    private static void deleteFile(Request req, Response res) {
        String id = req.param("id");
        if (files.remove(id) != null) {
            res.json(Map.of("success", true, "message", "文件删除成功"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "文件不存在"));
        }
    }
    
    static class UploadedFile {
        public String id;
        public String name;
        public String type;
        public long size;
        public byte[] content;
        public long uploadedAt;
        
        public UploadedFile(String id, String name, String type, long size, byte[] content, long uploadedAt) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.size = size;
            this.content = content;
            this.uploadedAt = uploadedAt;
        }
    }
}
