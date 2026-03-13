package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BlogAppExample {
    
    private static final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static final Map<Integer, Comment> comments = new ConcurrentHashMap<>();
    private static final AtomicInteger postIdGenerator = new AtomicInteger(1);
    private static final AtomicInteger commentIdGenerator = new AtomicInteger(1);
    
    static {
        posts.put(1, new Post(
            1,
            "Welcome to EST Blog",
            "This is a simple blog system built with EST framework",
            "EST is a zero-dependency modern Java development framework designed for rapid development of high-performance web applications.\n\nKey Features:\n- Zero-dependency, lightweight\n- Clean API design\n- High-performance HTTP server\n- RESTful routing support\n- Template engine\n- Middleware support",
            "EST Development Team",
            Arrays.asList("EST", "Framework", "Java"),
            System.currentTimeMillis()
        ));
        
        posts.put(2, new Post(
            2,
            "RESTful API Design Best Practices",
            "Learn how to design elegant RESTful APIs",
            "In this article, we will explore best practices for RESTful API design.\n\nCore Principles:\n1. Use correct HTTP methods\n2. Proper resource naming\n3. Version control\n4. Error handling\n5. Authentication and authorization",
            "Tech Team",
            Arrays.asList("REST", "API", "Best Practices"),
            System.currentTimeMillis() - 86400000
        ));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("Blog System - Blog App");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("Blog App", "1.0.0");
        
        app.enableCors();
        
        app.get("/", BlogAppExample::homePage);
        app.get("/post/:id", BlogAppExample::postPage);
        app.get("/admin", BlogAppExample::adminPage);
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.group("/posts", (postRouter, postGroup) -> {
                    postRouter.get("", BlogAppExample::listPosts);
                    postRouter.get("/:id", BlogAppExample::getPost);
                    postRouter.post("", BlogAppExample::createPost);
                    postRouter.put("/:id", BlogAppExample::updatePost);
                    postRouter.delete("/:id", BlogAppExample::deletePost);
                });
                
                r.group("/comments", (commentRouter, commentGroup) -> {
                    commentRouter.get("", BlogAppExample::listComments);
                    commentRouter.post("", BlogAppExample::createComment);
                    commentRouter.delete("/:id", BlogAppExample::deleteComment);
                });
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n[X] Blog system server started successfully!");
            System.out.println("\nAccess URLs:");
            System.out.println("  - http://localhost:8080          (Blog Homepage)");
            System.out.println("  - http://localhost:8080/admin    (Admin Dashboard)");
            System.out.println("\nAPI Endpoints:");
            System.out.println("  - GET    /api/posts              - Get post list");
            System.out.println("  - GET    /api/posts/:id          - Get single post");
            System.out.println("  - POST   /api/posts              - Create post");
            System.out.println("  - PUT    /api/posts/:id          - Update post");
            System.out.println("  - DELETE /api/posts/:id          - Delete post");
            System.out.println("  - GET    /api/comments           - Get comments");
            System.out.println("  - POST   /api/comments           - Post comment");
            System.out.println("  - DELETE /api/comments/:id       - Delete comment");
            System.out.println("\n[X] Press Ctrl+C to stop server");
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
                <title>EST Blog</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: #f8f9fa; min-height: 100vh; }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 60px 20px; text-align: center; }
                    .header h1 { font-size: 48px; margin-bottom: 10px; }
                    .header p { font-size: 18px; opacity: 0.9; }
                    .nav { background: white; padding: 15px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); position: sticky; top: 0; z-index: 100; }
                    .nav-container { max-width: 1200px; margin: 0 auto; padding: 0 20px; display: flex; justify-content: space-between; align-items: center; }
                    .nav a { color: #333; text-decoration: none; padding: 10px 20px; border-radius: 5px; transition: all 0.3s; }
                    .nav a:hover { background: #667eea; color: white; }
                    .container { max-width: 1200px; margin: 40px auto; padding: 0 20px; }
                    .posts-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(350px, 1fr)); gap: 30px; }
                    .post-card { background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); transition: transform 0.3s, box-shadow 0.3s; cursor: pointer; }
                    .post-card:hover { transform: translateY(-5px); box-shadow: 0 4px 20px rgba(0,0,0,0.15); }
                    .post-image { height: 200px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center; color: white; font-size: 48px; }
                    .post-content { padding: 25px; }
                    .post-title { font-size: 22px; color: #333; margin-bottom: 10px; font-weight: 600; }
                    .post-excerpt { color: #666; line-height: 1.6; margin-bottom: 15px; }
                    .post-meta { display: flex; justify-content: space-between; align-items: center; color: #999; font-size: 14px; }
                    .post-tags { display: flex; gap: 8px; flex-wrap: wrap; }
                    .tag { background: #f0f0f0; padding: 4px 12px; border-radius: 15px; font-size: 12px; color: #666; }
                    .footer { background: #333; color: white; text-align: center; padding: 40px 20px; margin-top: 60px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>📚 EST Blog</h1>
                    <p>Explore the tech world, share knowledge and experience</p>
                </div>
                
                <div class="nav">
                    <div class="nav-container">
                        <a href="/">🏠 Home</a>
                        <a href="/admin">⚙️ Admin Dashboard</a>
                    </div>
                </div>
                
                <div class="container">
                    <div class="posts-grid" id="postsGrid"></div>
                </div>
                
                <div class="footer">
                    <p>© 2024 EST Blog - Powered by EST Framework</p>
                </div>
                
                <script>
                    async function loadPosts() {
                        const res = await fetch('/api/posts');
                        const data = await res.json();
                        if (data.success) {
                            renderPosts(data.data);
                        }
                    }
                    
                    function renderPosts(postList) {
                        const container = document.getElementById('postsGrid');
                        container.innerHTML = postList.map(post => \`
                            <div class="post-card" onclick="viewPost(\${post.id})">
                                <div class="post-image">📄</div>
                                <div class="post-content">
                                    <h3 class="post-title">\${escapeHtml(post.title)}</h3>
                                    <p class="post-excerpt">\${escapeHtml(post.excerpt)}</p>
                                    <div class="post-meta">
                                        <span>👤 \${escapeHtml(post.author)}</span>
                                        <span>\${formatDate(post.createdAt)}</span>
                                    </div>
                                    <div class="post-tags" style="margin-top: 10px;">
                                        \${post.tags.map(tag => '<span class="tag">' + escapeHtml(tag) + '</span>').join('')}
                                    </div>
                                </div>
                            </div>
                        \`).join('');
                    }
                    
                    function viewPost(id) {
                        window.location.href = '/post/' + id;
                    }
                    
                    function formatDate(timestamp) {
                        return new Date(timestamp).toLocaleDateString('en-US');
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    loadPosts();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void postPage(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Post Detail</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: #f8f9fa; min-height: 100vh; }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px 20px; }
                    .header a { color: white; text-decoration: none; display: inline-flex; align-items: center; gap: 8px; }
                    .container { max-width: 900px; margin: 40px auto; padding: 0 20px; }
                    .post { background: white; border-radius: 10px; padding: 40px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .post-title { font-size: 36px; color: #333; margin-bottom: 20px; }
                    .post-meta { display: flex; gap: 20px; color: #999; margin-bottom: 30px; padding-bottom: 20px; border-bottom: 1px solid #eee; }
                    .post-content { line-height: 1.8; color: #444; font-size: 16px; white-space: pre-wrap; }
                    .post-tags { margin-top: 30px; display: flex; gap: 10px; }
                    .tag { background: #f0f0f0; padding: 6px 16px; border-radius: 20px; font-size: 14px; color: #666; }
                    .comments-section { margin-top: 40px; }
                    .comments-section h2 { font-size: 24px; color: #333; margin-bottom: 20px; }
                    .comment-form { background: white; border-radius: 10px; padding: 30px; margin-bottom: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .comment-form input, .comment-form textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; margin-bottom: 15px; font-size: 14px; }
                    .comment-form textarea { height: 100px; resize: vertical; }
                    .comment-form button { background: #667eea; color: white; border: none; padding: 12px 30px; border-radius: 5px; font-size: 16px; cursor: pointer; }
                    .comment-form button:hover { background: #5a6fd6; }
                    .comment-list { display: flex; flex-direction: column; gap: 15px; }
                    .comment { background: white; border-radius: 10px; padding: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .comment-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
                    .comment-author { font-weight: bold; color: #333; }
                    .comment-date { color: #999; font-size: 12px; }
                    .comment-content { color: #444; line-height: 1.6; }
                </style>
            </head>
            <body>
                <div class="header">
                    <a href="/">← Back to Home</a>
                </div>
                
                <div class="container">
                    <div class="post" id="postContent"></div>
                    
                    <div class="comments-section">
                        <h2>💬 Comments</h2>
                        <div class="comment-form">
                            <input type="text" id="commentAuthor" placeholder="Your name..." />
                            <textarea id="commentContent" placeholder="Write your comment..."></textarea>
                            <button onclick="submitComment()">Post Comment</button>
                        </div>
                        <div class="comment-list" id="commentList"></div>
                    </div>
                </div>
                
                <script>
                    const postId = window.location.pathname.split('/').pop();
                    
                    async function loadPost() {
                        const res = await fetch('/api/posts/' + postId);
                        const data = await res.json();
                        if (data.success) {
                            renderPost(data.data);
                        }
                    }
                    
                    async function loadComments() {
                        const res = await fetch('/api/comments?postId=' + postId);
                        const data = await res.json();
                        if (data.success) {
                            renderComments(data.data);
                        }
                    }
                    
                    function renderPost(post) {
                        const container = document.getElementById('postContent');
                        container.innerHTML = \`
                            <h1 class="post-title">\${escapeHtml(post.title)}</h1>
                            <div class="post-meta">
                                <span>👤 \${escapeHtml(post.author)}</span>
                                <span>📅 \${formatDate(post.createdAt)}</span>
                            </div>
                            <div class="post-content">\${escapeHtml(post.content)}</div>
                            <div class="post-tags">
                                \${post.tags.map(tag => '<span class="tag">#' + escapeHtml(tag) + '</span>').join('')}
                            </div>
                        \`;
                    }
                    
                    function renderComments(commentList) {
                        const container = document.getElementById('commentList');
                        container.innerHTML = commentList.map(comment => \`
                            <div class="comment">
                                <div class="comment-header">
                                    <span class="comment-author">\${escapeHtml(comment.author)}</span>
                                    <span class="comment-date">\${formatDate(comment.createdAt)}</span>
                                </div>
                                <div class="comment-content">\${escapeHtml(comment.content)}</div>
                            </div>
                        \`).join('');
                    }
                    
                    async function submitComment() {
                        const author = document.getElementById('commentAuthor').value.trim();
                        const content = document.getElementById('commentContent').value.trim();
                        
                        if (!author || !content) {
                            alert('Please fill in name and comment!');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('postId', postId);
                        formData.append('author', author);
                        formData.append('content', content);
                        
                        await fetch('/api/comments', { method: 'POST', body: formData });
                        document.getElementById('commentAuthor').value = '';
                        document.getElementById('commentContent').value = '';
                        loadComments();
                    }
                    
                    function formatDate(timestamp) {
                        return new Date(timestamp).toLocaleDateString('en-US');
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    loadPost();
                    loadComments();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void adminPage(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Blog Admin Dashboard</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: #f8f9fa; min-height: 100vh; }
                    .header { background: #333; color: white; padding: 20px 40px; display: flex; justify-content: space-between; align-items: center; }
                    .header h1 { font-size: 24px; }
                    .header a { color: white; text-decoration: none; padding: 10px 20px; background: #667eea; border-radius: 5px; }
                    .container { max-width: 1400px; margin: 40px auto; padding: 0 20px; }
                    .dashboard { display: grid; grid-template-columns: 350px 1fr; gap: 30px; }
                    .panel { background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .panel h2 { font-size: 20px; color: #333; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 2px solid #667eea; }
                    .form-group { margin-bottom: 20px; }
                    .form-group label { display: block; margin-bottom: 8px; color: #333; font-weight: 500; }
                    .form-group input, .form-group textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; }
                    .form-group textarea { height: 200px; resize: vertical; }
                    .btn { padding: 12px 30px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; }
                    .btn-primary { background: #667eea; color: white; }
                    .btn-primary:hover { background: #5a6fd6; }
                    .btn-danger { background: #e74c3c; color: white; }
                    .btn-danger:hover { background: #c0392b; }
                    .posts-list { display: flex; flex-direction: column; gap: 15px; }
                    .post-item { padding: 20px; border: 1px solid #eee; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }
                    .post-item h3 { font-size: 16px; color: #333; margin-bottom: 5px; }
                    .post-item p { color: #999; font-size: 13px; }
                    .post-actions { display: flex; gap: 10px; }
                    .post-actions button { padding: 8px 16px; font-size: 13px; }
                    .stats { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 30px; }
                    .stat-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; border-radius: 10px; text-align: center; }
                    .stat-number { font-size: 36px; font-weight: bold; }
                    .stat-label { font-size: 14px; opacity: 0.9; margin-top: 5px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>⚙️ Blog Admin Dashboard</h1>
                    <a href="/">← Back to Home</a>
                </div>
                
                <div class="container">
                    <div class="stats" id="stats"></div>
                    
                    <div class="dashboard">
                        <div class="panel">
                            <h2>✏️ Write Post</h2>
                            <div class="form-group">
                                <label>Title</label>
                                <input type="text" id="title" placeholder="Post title..." />
                            </div>
                            <div class="form-group">
                                <label>Excerpt</label>
                                <input type="text" id="excerpt" placeholder="Post excerpt..." />
                            </div>
                            <div class="form-group">
                                <label>Content</label>
                                <textarea id="content" placeholder="Post content..."></textarea>
                            </div>
                            <div class="form-group">
                                <label>Author</label>
                                <input type="text" id="author" placeholder="Author name..." />
                            </div>
                            <div class="form-group">
                                <label>Tags (comma separated)</label>
                                <input type="text" id="tags" placeholder="tag1, tag2, tag3..." />
                            </div>
                            <button class="btn btn-primary" onclick="createPost()">Publish Post</button>
                        </div>
                        
                        <div class="panel">
                            <h2>📚 Post Management</h2>
                            <div class="posts-list" id="postsList"></div>
                        </div>
                    </div>
                </div>
                
                <script>
                    async function loadStats() {
                        const res = await fetch('/api/posts');
                        const data = await res.json();
                        if (data.success) {
                            const posts = data.data;
                            const commentsRes = await fetch('/api/comments');
                            const commentsData = await commentsRes.json();
                            const comments = commentsData.data || [];
                            
                            document.getElementById('stats').innerHTML = \`
                                <div class="stat-card">
                                    <div class="stat-number">\${posts.length}</div>
                                    <div class="stat-label">Posts</div>
                                </div>
                                <div class="stat-card">
                                    <div class="stat-number">\${comments.length}</div>
                                    <div class="stat-label">Comments</div>
                                </div>
                                <div class="stat-card">
                                    <div class="stat-number">\${new Set(posts.flatMap(p => p.tags)).size}</div>
                                    <div class="stat-label">Tags</div>
                                </div>
                            \`;
                        }
                    }
                    
                    async function loadPosts() {
                        const res = await fetch('/api/posts');
                        const data = await res.json();
                        if (data.success) {
                            renderPosts(data.data);
                        }
                    }
                    
                    function renderPosts(postList) {
                        const container = document.getElementById('postsList');
                        container.innerHTML = postList.map(post => \`
                            <div class="post-item">
                                <div>
                                    <h3>\${escapeHtml(post.title)}</h3>
                                    <p>👤 \${escapeHtml(post.author)} · \${formatDate(post.createdAt)}</p>
                                </div>
                                <div class="post-actions">
                                    <button class="btn btn-danger" onclick="deletePost(\${post.id})">Delete</button>
                                </div>
                            </div>
                        \`).join('');
                    }
                    
                    async function createPost() {
                        const title = document.getElementById('title').value.trim();
                        const excerpt = document.getElementById('excerpt').value.trim();
                        const content = document.getElementById('content').value.trim();
                        const author = document.getElementById('author').value.trim();
                        const tags = document.getElementById('tags').value.trim();
                        
                        if (!title || !content || !author) {
                            alert('Please fill in complete post information!');
                            return;
                        }
                        
                        const formData = new FormData();
                        formData.append('title', title);
                        formData.append('excerpt', excerpt);
                        formData.append('content', content);
                        formData.append('author', author);
                        formData.append('tags', tags);
                        
                        await fetch('/api/posts', { method: 'POST', body: formData });
                        
                        document.getElementById('title').value = '';
                        document.getElementById('excerpt').value = '';
                        document.getElementById('content').value = '';
                        document.getElementById('author').value = '';
                        document.getElementById('tags').value = '';
                        
                        loadPosts();
                        loadStats();
                    }
                    
                    async function deletePost(id) {
                        if (confirm('Are you sure you want to delete this post?')) {
                            await fetch('/api/posts/' + id, { method: 'DELETE' });
                            loadPosts();
                            loadStats();
                        }
                    }
                    
                    function formatDate(timestamp) {
                        return new Date(timestamp).toLocaleDateString('en-US');
                    }
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                    
                    loadPosts();
                    loadStats();
                </script>
            </body>
            </html>
            """);
    }
    
    private static void listPosts(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", posts.values().stream()
                .sorted((a, b) -> Long.compare(b.createdAt, a.createdAt))
                .toList()
        ));
    }
    
    private static void getPost(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Post post = posts.get(id);
        if (post != null) {
            res.json(Map.of("success", true, "data", post));
        } else {
            res.status(404).json(Map.of("success", false, "message", "Post not found"));
        }
    }
    
    private static void createPost(Request req, Response res) {
        String title = req.formParam("title");
        String excerpt = req.formParam("excerpt", "");
        String content = req.formParam("content");
        String author = req.formParam("author");
        String tagsStr = req.formParam("tags", "");
        
        if (title == null || title.isBlank() || content == null || content.isBlank() || author == null || author.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "Title, content and author cannot be empty"));
            return;
        }
        
        List<String> tags = Arrays.asList(tagsStr.split(","))
            .stream()
            .map(String::trim)
            .filter(t -> !t.isBlank())
            .toList();
        
        int id = postIdGenerator.getAndIncrement();
        Post post = new Post(id, title, excerpt, content, author, tags, System.currentTimeMillis());
        posts.put(id, post);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "Post created successfully",
            "data", post
        ));
    }
    
    private static void updatePost(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        Post post = posts.get(id);
        
        if (post == null) {
            res.status(404).json(Map.of("success", false, "message", "Post not found"));
            return;
        }
        
        String title = req.formParam("title");
        String excerpt = req.formParam("excerpt");
        String content = req.formParam("content");
        String author = req.formParam("author");
        String tagsStr = req.formParam("tags");
        
        if (title != null) post.title = title;
        if (excerpt != null) post.excerpt = excerpt;
        if (content != null) post.content = content;
        if (author != null) post.author = author;
        if (tagsStr != null) {
            post.tags = Arrays.asList(tagsStr.split(","))
                .stream()
                .map(String::trim)
                .filter(t -> !t.isBlank())
                .toList();
        }
        
        res.json(Map.of(
            "success", true,
            "message", "Post updated successfully",
            "data", post
        ));
    }
    
    private static void deletePost(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        if (posts.remove(id) != null) {
            res.json(Map.of("success", true, "message", "Post deleted successfully"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "Post not found"));
        }
    }
    
    private static void listComments(Request req, Response res) {
        String postIdStr = req.queryParam("postId");
        var filtered = comments.values().stream();
        
        if (postIdStr != null) {
            int postId = Integer.parseInt(postIdStr);
            filtered = filtered.filter(c -> c.postId == postId);
        }
        
        res.json(Map.of(
            "success", true,
            "data", filtered
                .sorted((a, b) -> Long.compare(b.createdAt, a.createdAt))
                .toList()
        ));
    }
    
    private static void createComment(Request req, Response res) {
        String postIdStr = req.formParam("postId");
        String author = req.formParam("author");
        String content = req.formParam("content");
        
        if (postIdStr == null || author == null || content == null || content.isBlank()) {
            res.status(400).json(Map.of("success", false, "message", "Post ID, author and content cannot be empty"));
            return;
        }
        
        int postId = Integer.parseInt(postIdStr);
        if (!posts.containsKey(postId)) {
            res.status(404).json(Map.of("success", false, "message", "Post not found"));
            return;
        }
        
        int id = commentIdGenerator.getAndIncrement();
        Comment comment = new Comment(id, postId, author, content, System.currentTimeMillis());
        comments.put(id, comment);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "Comment posted successfully",
            "data", comment
        ));
    }
    
    private static void deleteComment(Request req, Response res) {
        int id = Integer.parseInt(req.param("id"));
        if (comments.remove(id) != null) {
            res.json(Map.of("success", true, "message", "Comment deleted successfully"));
        } else {
            res.status(404).json(Map.of("success", false, "message", "Comment not found"));
        }
    }
    
    static class Post {
        public int id;
        public String title;
        public String excerpt;
        public String content;
        public String author;
        public List<String> tags;
        public long createdAt;
        
        public Post(int id, String title, String excerpt, String content, String author, List<String> tags, long createdAt) {
            this.id = id;
            this.title = title;
            this.excerpt = excerpt;
            this.content = content;
            this.author = author;
            this.tags = tags;
            this.createdAt = createdAt;
        }
    }
    
    static class Comment {
        public int id;
        public int postId;
        public String author;
        public String content;
        public long createdAt;
        
        public Comment(int id, int postId, String author, String content, long createdAt) {
            this.id = id;
            this.postId = postId;
            this.author = author;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
