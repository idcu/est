package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.WorkflowContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorkflowContextTest {

    @Test
    public void testSetAndGetVariable() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        context.setVariable("key1", "value1");
        context.setVariable("key2", 42);
        
        Optional<String> value1 = context.getVariable("key1", String.class);
        Optional<Integer> value2 = context.getVariable("key2", Integer.class);
        
        Assertions.assertTrue(value1.isPresent());
        Assertions.assertEquals("value1", value1.get());
        
        Assertions.assertTrue(value2.isPresent());
        Assertions.assertEquals(42, value2.get());
    }

    @Test
    public void testGetNonExistentVariable() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        Optional<String> value = context.getVariable("nonExistent", String.class);
        
        Assertions.assertFalse(value.isPresent());
    }

    @Test
    public void testRemoveVariable() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        context.setVariable("key", "value");
        Optional<String> beforeRemove = context.getVariable("key", String.class);
        
        context.removeVariable("key");
        Optional<String> afterRemove = context.getVariable("key", String.class);
        
        Assertions.assertTrue(beforeRemove.isPresent());
        Assertions.assertFalse(afterRemove.isPresent());
    }

    @Test
    public void testGetVariables() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        context.setVariable("key1", "value1");
        context.setVariable("key2", 42);
        
        Map<String, Object> variables = context.getVariables();
        
        Assertions.assertTrue(variables.containsKey("key1"));
        Assertions.assertTrue(variables.containsKey("key2"));
        Assertions.assertEquals(2, variables.size());
    }

    @Test
    public void testSetVariables() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        Map<String, Object> newVariables = new HashMap<>();
        newVariables.put("key1", "value1");
        newVariables.put("key2", 42);
        
        context.setVariables(newVariables);
        
        Assertions.assertTrue(context.hasVariable("key1"));
        Assertions.assertTrue(context.hasVariable("key2"));
    }

    @Test
    public void testHasVariable() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        context.setVariable("key", "value");
        
        Assertions.assertTrue(context.hasVariable("key"));
        Assertions.assertFalse(context.hasVariable("nonExistent"));
    }

    @Test
    public void testClearVariables() {
        WorkflowContext context = new DefaultWorkflowContext("test-instance");
        
        context.setVariable("key1", "value1");
        context.setVariable("key2", "value2");
        
        Assertions.assertEquals(2, context.getVariables().size());
        
        context.clearVariables();
        
        Assertions.assertEquals(0, context.getVariables().size());
    }

    @Test
    public void testGetWorkflowInstanceId() {
        String instanceId = "test-instance-123";
        WorkflowContext context = new DefaultWorkflowContext(instanceId);
        
        Assertions.assertEquals(instanceId, context.getWorkflowInstanceId());
    }
}
