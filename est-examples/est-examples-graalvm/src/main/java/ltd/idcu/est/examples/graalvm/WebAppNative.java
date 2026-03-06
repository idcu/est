package ltd.idcu.est.examples.graalvm;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class WebAppNative {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        WebApplication app = Web.create("EST Native Web App", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello from EST GraalVM Native Web App!");
        });
        
        app.get("/api/hello", (req, res) -> {
            res.json(new Message("Hello from REST API!", System.currentTimeMillis()));
        });
        
        app.get("/api/status", (req, res) -> {
            res.json(new Status("ok", "GraalVM Native", Runtime.getRuntime().availableProcessors()));
        });
        
        long initTime = System.currentTimeMillis() - startTime;
        System.out.println("Server initialized in " + initTime + "ms");
        
        app.run(8080);
    }
    
    static class Message {
        public String message;
        public long timestamp;
        
        public Message(String message, long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
    }
    
    static class Status {
        public String status;
        public String mode;
        public int processors;
        
        public Status(String status, String mode, int processors) {
            this.status = status;
            this.mode = mode;
            this.processors = processors;
        }
    }
}
