package ltd.idcu.est.test;

import ltd.idcu.est.test.runner.TestRunner;

public class TestLauncher {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("EST Framework Test Launcher");
        System.out.println("========================================");
        System.out.println();
        
        TestRunner runner = new TestRunner();
        boolean allPassed = true;
        
        try {
            System.out.println("Running core container tests...");
            var result1 = runner.runClass(Class.forName("ltd.idcu.est.core.container.impl.DefaultContainerTest"));
            System.out.println("  Container tests: " + (result1.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result1.isSuccessful();
            System.out.println();
            
            System.out.println("Running core config tests...");
            var result2 = runner.runClass(Class.forName("ltd.idcu.est.core.config.impl.DefaultConfigTest"));
            System.out.println("  Config tests: " + (result2.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result2.isSuccessful();
            System.out.println();
            
            System.out.println("Running patterns tests...");
            var result3 = runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.creational.DefaultFactoryTest"));
            var result4 = runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.creational.DefaultSingletonTest"));
            System.out.println("  Factory test: " + (result3.isSuccessful() ? "PASSED" : "FAILED"));
            System.out.println("  Singleton test: " + (result4.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result3.isSuccessful() && result4.isSuccessful();
            System.out.println();
            
            System.out.println("Running collection tests...");
            var result5 = runner.runClass(Class.forName("ltd.idcu.est.collection.impl.SeqTest"));
            var result6 = runner.runClass(Class.forName("ltd.idcu.est.collection.impl.MapSeqsTest"));
            System.out.println("  Seq test: " + (result5.isSuccessful() ? "PASSED" : "FAILED"));
            System.out.println("  MapSeqs test: " + (result6.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result5.isSuccessful() && result6.isSuccessful();
            System.out.println();
            
            System.out.println("Running utils tests...");
            var result7 = runner.runClass(Class.forName("ltd.idcu.est.utils.common.AssertUtilsTest"));
            var result8 = runner.runClass(Class.forName("ltd.idcu.est.utils.common.StringUtilsTest"));
            System.out.println("  AssertUtils test: " + (result7.isSuccessful() ? "PASSED" : "FAILED"));
            System.out.println("  StringUtils test: " + (result8.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result7.isSuccessful() && result8.isSuccessful();
            System.out.println();
            
            System.out.println("Running workflow tests...");
            var result9 = runner.runClass(Class.forName("ltd.idcu.est.workflow.core.WorkflowEngineTest"));
            var result10 = runner.runClass(Class.forName("ltd.idcu.est.workflow.core.WorkflowContextTest"));
            System.out.println("  WorkflowEngine test: " + (result9.isSuccessful() ? "PASSED" : "FAILED"));
            System.out.println("  WorkflowContext test: " + (result10.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result9.isSuccessful() && result10.isSuccessful();
            System.out.println();
            
            System.out.println("Running admin tests...");
            var result11 = runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultUserServiceTest"));
            var result12 = runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultRoleServiceTest"));
            System.out.println("  UserService test: " + (result11.isSuccessful() ? "PASSED" : "FAILED"));
            System.out.println("  RoleService test: " + (result12.isSuccessful() ? "PASSED" : "FAILED"));
            allPassed &= result11.isSuccessful() && result12.isSuccessful();
            System.out.println();
            
        } catch (ClassNotFoundException e) {
            System.err.println("Test class not found: " + e.getMessage());
            allPassed = false;
        }
        
        System.out.println("========================================");
        System.out.println("Test Summary: " + (allPassed ? "ALL TESTS PASSED" : "SOME TESTS FAILED"));
        System.out.println("========================================");
        
        System.exit(allPassed ? 0 : 1);
    }
}
