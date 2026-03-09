package ltd.idcu.est.codecli.web;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EstWebServer {
    
    private final int port;
    private final String workDir;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running;
    
    public EstWebServer(int port, String workDir) {
        this.port = port;
        this.workDir = workDir;
        this.executorService = Executors.newCachedThreadPool();
    }
    
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        
        System.out.println("EST Code CLI Web Server started on http://localhost:" + port);
        System.out.println("Press Ctrl+C to stop");
        
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try (
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)
        ) {
            String requestLine = reader.readLine();
            if (requestLine == null) {
                return;
            }
            
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                sendError(writer, 400, "Bad Request");
                return;
            }
            
            String method = parts[0];
            String path = parts[1];
            
            if ("GET".equals(method)) {
                handleGet(path, writer, output);
            } else {
                sendError(writer, 405, "Method Not Allowed");
            }
            
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }
    }
    
    private void handleGet(String path, PrintWriter writer, OutputStream output) throws IOException {
        if ("/".equals(path)) {
            path = "/index.html";
        }
        
        String resourcePath = "/web" + path;
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
        
        if (resourceStream != null) {
            byte[] content = readAllBytes(resourceStream);
            String contentType = getContentType(path);
            
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: " + contentType);
            writer.println("Content-Length: " + content.length);
            writer.println();
            writer.flush();
            
            output.write(content);
            output.flush();
        } else {
            sendError(writer, 404, "Not Found");
        }
    }
    
    private byte[] readAllBytes(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        } else if (path.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        } else if (path.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        } else if (path.endsWith(".json")) {
            return "application/json; charset=UTF-8";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else if (path.endsWith(".svg")) {
            return "image/svg+xml";
        }
        return "text/plain; charset=UTF-8";
    }
    
    private void sendError(PrintWriter writer, int code, String message) {
        writer.println("HTTP/1.1 " + code + " " + message);
        writer.println("Content-Type: text/plain; charset=UTF-8");
        writer.println();
        writer.println(message);
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
        executorService.shutdown();
    }
    
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        String workDir = args.length > 1 ? args[1] : System.getProperty("user.dir");
        
        try {
            EstWebServer server = new EstWebServer(port, workDir);
            
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
