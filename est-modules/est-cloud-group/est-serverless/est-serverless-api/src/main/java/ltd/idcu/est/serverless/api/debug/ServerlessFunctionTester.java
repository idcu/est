package ltd.idcu.est.serverless.api.debug;

import ltd.idcu.est.serverless.api.ServerlessFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerlessFunctionTester {
    
    public static <I, O> TestResult testFunction(ServerlessFunction<I, O> function, I input, Map<String, Object> config) {
        long startTime = System.currentTimeMillis();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        TestResult result = new TestResult();
        
        try {
            if (config != null) {
                function.initialize(config);
            }
            
            Map<String, Object> context = new HashMap<>();
            context.put("test", true);
            context.put("timestamp", System.currentTimeMillis());
            
            O output = function.handle(input, context);
            
            long endTime = System.currentTimeMillis();
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            
            result.setSuccess(true);
            result.setOutput(output);
            result.setExecutionTime(endTime - startTime);
            result.setMemoryUsed(endMemory - startMemory);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setError(e.getMessage());
            result.setException(e);
        } finally {
            try {
                function.destroy();
            } catch (Exception e) {
            }
        }
        
        return result;
    }
    
    public static <I, O> TestResult testFunction(ServerlessFunction<I, O> function, I input) {
        return testFunction(function, input, null);
    }
    
    public static void interactiveTest(ServerlessFunction<String, String> function) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== EST Serverless Function Interactive Tester ===");
        System.out.println("Type your input and press Enter. Type 'exit' to quit.\n");
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            
            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                break;
            }
            
            TestResult result = testFunction(function, input);
            
            System.out.println("\n--- Result ---");
            if (result.isSuccess()) {
                System.out.println("Status: SUCCESS");
                System.out.println("Output: " + result.getOutput());
                System.out.println("Execution Time: " + result.getExecutionTime() + "ms");
                System.out.println("Memory Used: " + formatMemory(result.getMemoryUsed()));
            } else {
                System.out.println("Status: FAILED");
                System.out.println("Error: " + result.getError());
                if (result.getException() != null) {
                    result.getException().printStackTrace();
                }
            }
            System.out.println("---\n");
        }
        
        scanner.close();
        System.out.println("Bye!");
    }
    
    private static String formatMemory(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        }
    }
    
    public static class TestResult {
        private boolean success;
        private Object output;
        private String error;
        private Exception exception;
        private long executionTime;
        private long memoryUsed;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        @SuppressWarnings("unchecked")
        public <T> T getOutput() {
            return (T) output;
        }
        
        public void setOutput(Object output) {
            this.output = output;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        public Exception getException() {
            return exception;
        }
        
        public void setException(Exception exception) {
            this.exception = exception;
        }
        
        public long getExecutionTime() {
            return executionTime;
        }
        
        public void setExecutionTime(long executionTime) {
            this.executionTime = executionTime;
        }
        
        public long getMemoryUsed() {
            return memoryUsed;
        }
        
        public void setMemoryUsed(long memoryUsed) {
            this.memoryUsed = memoryUsed;
        }
    }
}
