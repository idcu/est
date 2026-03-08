package ltd.idcu.est.ai.api;

import java.util.List;
import java.util.Map;

public interface TestAndDeployManager {
    
    TestSuite generateTests(String code, String context);
    
    DeploymentPlan createDeploymentPlan(String projectName, Map<String, Object> config);
    
    boolean runTests(TestSuite testSuite);
    
    boolean deploy(DeploymentPlan plan);
    
    TestReport getTestReport();
    
    DeploymentStatus getDeploymentStatus();
}
