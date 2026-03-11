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
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("test-def");
        
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
        
        engine.registerWorkflow(workflow);
        engine.startWorkflow("test-def");
        engine.startWorkflow("test-def");
        
        var instances = repository.findInstancesByWorkflowId("test-def");
        
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

    @Test
    public void testFindAllDefinitions() {
        WorkflowRepository repository = new MemoryWorkflowRepository();
        
        Node startNode1 = Workflows.newTaskNode("start1", "Start 1", ctx -> {});
        Node startNode2 = Workflows.newTaskNode("start2", "Start 2", ctx -> {});
        
        WorkflowDefinition workflow1 = Workflows.newWorkflowBuilder()
                .id("test-def-1")
                .name("Test Definition 1")
                .startNode(startNode1)
                .build();
        
        WorkflowDefinition workflow2 = Workflows.newWorkflowBuilder()
                .id("test-def-2")
                .name("Test Definition 2")
                .startNode(startNode2)
                .build();
        
        repository.saveDefinition(workflow1);
        repository.saveDefinition(workflow2);
        
        var allDefinitions = repository.findAllDefinitions();
        
        Assertions.assertEquals(2, allDefinitions.size());
    }
}
