package ltd.idcu.est.ai.api;

import java.util.ArrayList;
import java.util.List;

public class TestReport {
    
    private String testSuiteName;
    private int totalTests;
    private int passedTests;
    private int failedTests;
    private int skippedTests;
    private long durationMs;
    private List<TestResult> results;
    private String summary;
    
    public TestReport() {
        this.results = new ArrayList<>();
    }
    
    public String getTestSuiteName() {
        return testSuiteName;
    }
    
    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }
    
    public int getTotalTests() {
        return totalTests;
    }
    
    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }
    
    public int getPassedTests() {
        return passedTests;
    }
    
    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }
    
    public int getFailedTests() {
        return failedTests;
    }
    
    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
    }
    
    public int getSkippedTests() {
        return skippedTests;
    }
    
    public void setSkippedTests(int skippedTests) {
        this.skippedTests = skippedTests;
    }
    
    public long getDurationMs() {
        return durationMs;
    }
    
    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
    
    public List<TestResult> getResults() {
        return results;
    }
    
    public void setResults(List<TestResult> results) {
        this.results = results;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public void addResult(TestResult result) {
        this.results.add(result);
    }
    
    public double getPassRate() {
        return totalTests > 0 ? (double) passedTests / totalTests * 100 : 0;
    }
    
    public static class TestResult {
        private String testName;
        private TestStatus status;
        private long durationMs;
        private String errorMessage;
        private String stackTrace;
        
        public TestResult(String testName, TestStatus status) {
            this.testName = testName;
            this.status = status;
        }
        
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        public TestStatus getStatus() { return status; }
        public void setStatus(TestStatus status) { this.status = status; }
        public long getDurationMs() { return durationMs; }
        public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public String getStackTrace() { return stackTrace; }
        public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    }
    
    public enum TestStatus {
        PASSED,
        FAILED,
        SKIPPED,
        ERROR
    }
}
