package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.LoggingMiddleware;
import ltd.idcu.est.web.StringTemplateEngine;
import ltd.idcu.est.web.api.FormData;
import ltd.idcu.est.web.api.MultipartFile;
import ltd.idcu.est.web.api.WebApplication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TemplateAndFormExample {
    public static void main(String[] args) {
        System.out.println("\n=== Template and Form Example ===");
        
        WebApplication app = new DefaultWebApplication();
        
        app.use(new LoggingMiddleware());
        
        app.setViewEngine(new StringTemplateEngine());
        
        app.routes(router -> {
            router.get("/", (req, res) -> {
                Map<String, Object> model = new HashMap<>();
                model.put("title", "EST Web Framework");
                model.put("message", "Welcome to EST Web Framework with Template Engine!");
                res.render("home", model);
            });
            
            router.get("/form", (req, res) -> {
                res.html("<html><body>" +
                    "<h1>Submit a Form</h1>" +
                    "<form method='post' action='/submit' enctype='multipart/form-data'>" +
                    "Name: <input type='text' name='name'><br>" +
                    "Email: <input type='email' name='email'><br>" +
                    "File: <input type='file' name='file'><br>" +
                    "<input type='submit' value='Submit'>" +
                    "</form></body></html>");
            });
            
            router.post("/submit", (req, res) -> {
                FormData formData = req.getFormData();
                String name = formData.getParameter("name");
                String email = formData.getParameter("email");
                MultipartFile file = formData.getFile("file");
                
                Map<String, Object> model = new HashMap<>();
                model.put("name", name != null ? name : "Guest");
                model.put("email", email != null ? email : "Not provided");
                
                if (file != null && !file.isEmpty()) {
                    model.put("fileInfo", "Uploaded: " + file.getOriginalFilename() + 
                        " (" + file.getSize() + " bytes)");
                    try {
                        File uploadDir = new File("uploads");
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        File destFile = new File(uploadDir, file.getOriginalFilename());
                        file.transferTo(destFile);
                    } catch (Exception e) {
                        model.put("fileInfo", "Error saving file: " + e.getMessage());
                    }
                } else {
                    model.put("fileInfo", "No file uploaded");
                }
                
                res.render("result", model);
            });
            
            router.get("/greet/:name", (req, res) -> {
                Map<String, Object> model = new HashMap<>();
                model.put("name", req.getPathVariable("name"));
                res.render("greeting", model);
            });
        });
        
        app.onStartup(() -> {
            System.out.println("Server started on http://localhost:8080");
            System.out.println("Available routes:");
            System.out.println("  - GET /          - Home page with template");
            System.out.println("  - GET /form      - Form submission page");
            System.out.println("  - POST /submit   - Handle form submission");
            System.out.println("  - GET /greet/:name - Personalized greeting");
            System.out.println("\nPress Ctrl+C to stop the server");
        });
        
        app.run(8080);
    }
    
    public static void run() {
        main(new String[]{});
    }
}
