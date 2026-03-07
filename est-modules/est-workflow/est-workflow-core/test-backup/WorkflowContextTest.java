package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.WorkflowContext;

import java.util.Optional;

public class WorkflowContextTest {

    @Test
    public void testSetAndGetVariable() {
        WorkflowContext context = new DefaultWorkflowContext();
        
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
        WorkflowContext context = new DefaultWorkflowContext();
        
        Optional<String> value = context.getVariable("nonExistent", String.class);
        
        Assertions.assertFalse(value.isPresent());
    }

    @Test
    public void testRemoveVariable() {
        WorkflowContext context = new DefaultWorkflowContext();
        
        context.setVariable("key", "value");
        Optional<String> beforeRemove = context.getVariable("key", String.class);
        
        context.removeVariable("key");
        Optional<String> afterRemove = context.getVariable("key", String.class);
        
        Assertions.assertTrue(beforeRemove.isPresent());
        Assertions.assertFalse(afterRemove.isPresent());
    }

    @Test
    public void testGetVariableNames() {
        WorkflowContext context = new DefaultWorkflowContext();
        
        context.setVariable("key1", "value1");
        context.setVariable("key2", 42);
        
        var variableNames = context.getVariableNames();
        
        Assertions.assertTrue(variableNames.contains("key1"));
        Assertions.assertTrue(variableNames.contains("key2"));
        Assertions.assertEquals(2, variableNames.size());
    }

    @Test
    public void testHasVariable() {
        WorkflowContext context = new DefaultWorkflowContext();
        
        context.setVariable("key", "value");
        
        Assertions.assertTrue(context.hasVariable("key"));
        Assertions.assertFalse(context.hasVariable("nonExistent"));
    }
}
