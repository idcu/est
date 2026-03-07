package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.*;

import java.util.Optional;

public class MemoryWorkflowRepositoryTest {

    @Test
    public void testSaveAndFindWorkflowDefinition() {
        WorkflowRepository repository = new MemoryWorkflowRepository();
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("test-def")
                .name("Test Definition")
                .startNode(startNode)
                .build();
        
        repository.saveDefinition(workflow);
        
        Optional<WorkflowDefinition> found = repository.findDefinitionById("test-def");
        
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("test-def", found.get().getId());
        Assertions.assertEquals("Test Definition", found.get().getName());
    }

    @Test
    public void testFindNonExistentDefinition() {
        WorkflowRepository repository = new MemoryWorkflowRepository();
        
        Optional<WorkflowDefinition> found = repository.findDefinitionById("non-existent");
        
        Assertions.assertFalse(found.isPresent());
    }

    @Test
    public void testSaveAndFindWorkflowInstance() {
        WorkflowRepository repository = new MemoryWorkflowRepository();
        WorkflowEngine engine = Workflows.newWorkflowEngine(repository);
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("test-def")
                .name("Test Definition")
                .startNode(startNode)
                .build();
        
        repository.saveDefinition(workflow);
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Optional<WorkflowInstance> found = repository.findInstanceById(instance.getInstanceId());
        
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(instance.getInstanceId(), found.get().getInstanceId());
        Assertions.assertEquals(WorkflowStatus.COMPLETED, found.get().getStatus());
    }

    @Test
    public void testFindInstancesByDefinitionId() {
        WorkflowRepository repository = new MemoryWorkflowRepository();
        WorkflowEngine engine = Workflows.newWorkflowEngine(repository);
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("test-def")
                .name("Test Definition")
                .startNode(startNode)
                .build();
        
        repository.saveDefinition(workflow);
        engine.startWorkflow(workflow);
        engine.startWorkflow(workflow);
        
        var instances = repository.findInstancesByDefinitionId("test-def");
        
        Assertions.assertEquals(2, instances.size());
    }

    @Test
    public void testDeleteDefinition() {
        WorkflowRepository repository = new MemoryWorkflowRepository();
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("test-def")
                .name("Test Definition")
                .startNode(startNode)
                .build();
        
        repository.saveDefinition(workflow);
        Assertions.assertTrue(repository.findDefinitionById("test-def").isPresent());
        
        repository.deleteDefinition("test-def");
        Assertions.assertFalse(repository.findDefinitionById("test-def").isPresent());
    }
}
