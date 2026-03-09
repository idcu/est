package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.testing.TestRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunTestsMcpTool implements McpTool {
    
    private final TestRunner testRunner;
    
    public RunTestsMcpTool(String workDir) {
        this.testRunner = new TestRunner(workDir);
    }
    
    @Override
    public String getName() {
        return "est_run_tests";
    }
    
    @Override
    public String getDescription() {
        return "Runs Maven tests and shows results";
    }
    
    @Override
    public String getInputSchema() {
        return "{\n" +
               "  \"type\": \"object\",\n" +
               "  \"properties\": {\n" +
               "    \"type\": {\n" +
               "      \"type\": \"string\",\n" +
               "      \"description\": \"Type of test to run: 'compile' or 'test' (default: 'test')\"\n" +
               "    }\n" +
               "  }\n" +
               "}";
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> args) {
        try {
            String type = args.get("type") != null ? (String) args.get("type") : "test";
            List<TestRunner.TestResult> results;
            
            if ("compile".equals(type)) {
                results = testRunner.runMavenCompile();
            } else {
                results = testRunner.runMavenTests();
            }
            
            String formattedResults = testRunner.formatResults(results);
            
            long totalPassed = results.stream().filter(TestRunner.TestResult::isPassed).count();
            long totalFailed = results.stream().filter(r -> !r.isPassed()).count();
            long totalDuration = results.stream().mapToLong(TestRunner.TestResult::getDuration).sum();
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("type", type);
            metadata.put("totalTests", results.size());
            metadata.put("passed", totalPassed);
            metadata.put("failed", totalFailed);
            metadata.put("totalDurationMs", totalDuration);
            
            return McpToolResult.success(formattedResults, metadata);
            
        } catch (Exception e) {
            return McpToolResult.error("Failed to run tests: " + e.getMessage());
        }
    }
}
