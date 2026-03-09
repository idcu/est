package ltd.idcu.est.codecli.acp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcpServer {
    
    private final int port;
    private final String workDir;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running;
    
    public AcpServer(int port, String workDir) {
        this.port = port;
        this.workDir = workDir;
        this.executorService = Executors.newCachedThreadPool();
    }
    
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        
        System.out.println("EST Code CLI ACP Server started on port " + port);
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
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String response = handleMessage(line);
                writer.println(response);
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
    
    private String handleMessage(String message) {
        try {
            if (message.startsWith("{") && message.endsWith("}")) {
                return handleJsonMessage(message);
            } else {
                return createErrorResponse("Invalid message format");
            }
        } catch (Exception e) {
            return createErrorResponse("Error processing message: " + e.getMessage());
        }
    }
    
    private String handleJsonMessage(String json) {
        String method = extractJsonValue(json, "method");
        
        if (method == null) {
            return createErrorResponse("Missing method");
        }
        
        switch (method) {
            case "initialize":
                return handleInitialize();
            case "ping":
                return handlePing();
            case "tools/list":
                return handleListTools();
            case "tools/call":
                return handleCallTool(json);
            default:
                return createErrorResponse("Unknown method: " + method);
        }
    }
    
    private String handleInitialize() {
        return "{\"jsonrpc\":\"2.0\",\"result\":{\"serverInfo\":{\"name\":\"est-code-cli-acp\",\"version\":\"1.0.0\"},\"capabilities\":{\"tools\":{}}},"id\":1}";
    }
    
    private String handlePing() {
        return "{\"jsonrpc\":\"2.0\",\"result\":{},\"id\":1}";
    }
    
    private String handleListTools() {
        return "{\"jsonrpc\":\"2.0\",\"result\":{\"tools\":[]},\"id\":1}";
    }
    
    private String handleCallTool(String json) {
        return "{\"jsonrpc\":\"2.0\",\"result\":{\"content\":[{\"type\":\"text\",\"text\":\"Tool call received\"}]},\"id\":1}";
    }
    
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) {
            return null;
        }
        
        int valueStart = keyIndex + searchKey.length();
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        
        if (valueStart >= json.length()) {
            return null;
        }
        
        char firstChar = json.charAt(valueStart);
        if (firstChar == '\"') {
            int valueEnd = json.indexOf('\"', valueStart + 1);
            if (valueEnd != -1) {
                return json.substring(valueStart + 1, valueEnd);
            }
        }
        
        return null;
    }
    
    private String createErrorResponse(String message) {
        return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"" + escapeJson(message) + "\"},\"id\":null}";
    }
    
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
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
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 3000;
        String workDir = args.length > 1 ? args[1] : System.getProperty("user.dir");
        
        try {
            AcpServer server = new AcpServer(port, workDir);
            
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start ACP server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
