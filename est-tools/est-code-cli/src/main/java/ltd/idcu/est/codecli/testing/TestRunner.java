package ltd.idcu.est.codecli.testing;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestRunner {
    
    public static class TestResult {
        private final String testName;
        private final boolean passed;
        private final String output;
        private final long duration;
        private final String errorMessage;
        
        public TestResult(String testName, boolean passed, String output, long duration, String errorMessage) {
            this.testName = testName;
            this.passed = passed;
            this.output = output;
            this.duration = duration;
            this.errorMessage = errorMessage;
        }
        
        public String getTestName() { return testName; }
        public boolean isPassed() { return passed; }
        public String getOutput() { return output; }
        public long getDuration() { return duration; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    private final String workDir;
    
    public TestRunner(String workDir) {
        this.workDir = workDir;
    }
    
    public List<TestResult> runMavenTests() {
        List<TestResult> results = new ArrayList<>();
        
        try {
            ProcessBuilder pb = new ProcessBuilder("mvn", "test");
            pb.directory(new File(workDir));
            pb.redirectErrorStream(true);
            
            long startTime = System.currentTimeMillis();
            Process process = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.waitFor();
            long duration = System.currentTimeMillis() - startTime;
            
            boolean passed = exitCode == 0;
            String errorMessage = passed ? null : "Tests failed with exit code: " + exitCode;
            
            results.add(new TestResult("Maven Test Suite", passed, output.toString(), duration, errorMessage));
            
        } catch (Exception e) {
            results.add(new TestResult("Maven Test Suite", false, "", 0, e.getMessage()));
        }
        
        return results;
    }
    
    public List<TestResult> runMavenCompile() {
        List<TestResult> results = new ArrayList<>();
        
        try {
            ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "compile");
            pb.directory(new File(workDir));
            pb.redirectErrorStream(true);
            
            long startTime = System.currentTimeMillis();
            Process process = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.waitFor();
            long duration = System.currentTimeMillis() - startTime;
            
            boolean passed = exitCode == 0;
            String errorMessage = passed ? null : "Compile failed with exit code: " + exitCode;
            
            results.add(new TestResult("Maven Compile", passed, output.toString(), duration, errorMessage));
            
        } catch (Exception e) {
            results.add(new TestResult("Maven Compile", false, "", 0, e.getMessage()));
        }
        
        return results;
    }
    
    public String formatResults(List<TestResult> results) {
        StringBuilder sb = new StringBuilder();
        int passed = 0;
        int failed = 0;
        long totalDuration = 0;
        
        sb.append("Test Results:\n");
        sb.append("=============\n\n");
        
        for (TestResult result : results) {
            sb.append("Test: ").append(result.getTestName()).append("\n");
            sb.append("Status: ").append(result.isPassed() ? "PASSED" : "FAILED").append("\n");
            sb.append("Duration: ").append(result.getDuration()).append("ms\n");
            
            if (result.getErrorMessage() != null) {
                sb.append("Error: ").append(result.getErrorMessage()).append("\n");
            }
            
            if (result.getOutput() != null && !result.getOutput().isEmpty()) {
                sb.append("\nOutput:\n");
                String output = result.getOutput();
                if (output.length() > 1000) {
                    output = output.substring(0, 1000) + "... (truncated)";
                }
                sb.append(output).append("\n");
            }
            
            sb.append("\n");
            
            if (result.isPassed()) {
                passed++;
            } else {
                failed++;
            }
            totalDuration += result.getDuration();
        }
        
        sb.append("Summary:\n");
        sb.append("  Passed: ").append(passed).append("\n");
        sb.append("  Failed: ").append(failed).append("\n");
        sb.append("  Total: ").append(results.size()).append("\n");
        sb.append("  Total Duration: ").append(totalDuration).append("ms\n");
        
        return sb.toString();
    }
}
