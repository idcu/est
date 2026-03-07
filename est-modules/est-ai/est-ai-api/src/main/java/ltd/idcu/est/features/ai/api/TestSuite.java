package ltd.idcu.est.features.ai.api;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {
    
    private String name;
    private String targetClass;
    private List<TestCase> testCases;
    private List<String> setupCode;
    private List<String> teardownCode;
    
    public TestSuite() {
        this.testCases = new ArrayList<>();
        this.setupCode = new ArrayList<>();
        this.teardownCode = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTargetClass() {
        return targetClass;
    }
    
    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }
    
    public List<TestCase> getTestCases() {
        return testCases;
    }
    
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
    
    public List<String> getSetupCode() {
        return setupCode;
    }
    
    public void setSetupCode(List<String> setupCode) {
        this.setupCode = setupCode;
    }
    
    public List<String> getTeardownCode() {
        return teardownCode;
    }
    
    public void setTeardownCode(List<String> teardownCode) {
        this.teardownCode = teardownCode;
    }
    
    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
    }
    
    public static class TestCase {
        private String name;
        private String description;
        private String testCode;
        private String expectedResult;
        private TestType type;
        private int priority;
        
        public TestCase(String name, String description, String testCode, TestType type) {
            this.name = name;
            this.description = description;
            this.testCode = testCode;
            this.type = type;
            this.priority = 5;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getTestCode() { return testCode; }
        public void setTestCode(String testCode) { this.testCode = testCode; }
        public String getExpectedResult() { return expectedResult; }
        public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }
        public TestType getType() { return type; }
        public void setType(TestType type) { this.type = type; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }
    
    public enum TestType {
        UNIT,
        INTEGRATION,
        PERFORMANCE,
        SECURITY,
        BOUNDARY,
        EDGE_CASE
    }
}
