package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.*;

import java.util.ArrayList;
import java.util.List;

public class ParallelGatewayTest {

    @Test
    public void testParallelGatewaySplitAndJoin() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> executedNodes = new ArrayList<>();
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            executedNodes.add("start");
        });
        
        Node splitGateway = Workflows.newParallelGateway("split", "Split Gateway");
        
        Node task1 = Workflows.newTaskNode("task1", "Task 1", ctx -> {
            executedNodes.add("task1");
        });
        
        Node task2 = Workflows.newTaskNode("task2", "Task 2", ctx -> {
            executedNodes.add("task2");
        });
        
        Node joinGateway = Workflows.newParallelGateway("join", "Join Gateway");
        
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {
            executedNodes.add("end");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("parallel-workflow")
                .name("Parallel Workflow")
                .startNode(startNode)
                .addNode(splitGateway)
                .addNode(task1)
                .addNode(task2)
                .addNode(joinGateway)
                .endNode(endNode)
                .connect("start", "split")
                .connect("split", "task1")
                .connect("split", "task2")
                .connect("task1", "join")
                .connect("task2", "join")
                .connect("join", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("parallel-workflow");
        
        Assertions.assertEquals(WorkflowStatus.COMPLETED, instance.getStatus());
        Assertions.assertTrue(executedNodes.contains("start"));
        Assertions.assertTrue(executedNodes.contains("task1"));
        Assertions.assertTrue(executedNodes.contains("task2"));
        Assertions.assertTrue(executedNodes.contains("end"));
    }

    @Test
    public void testParallelGatewayWithMultipleBranches() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> executedNodes = new ArrayList<>();
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            executedNodes.add("start");
        });
        
        Node splitGateway = Workflows.newParallelGateway("split", "Split Gateway");
        
        Node taskA = Workflows.newTaskNode("taskA", "Task A", ctx -> {
            executedNodes.add("taskA");
        });
        
        Node taskB = Workflows.newTaskNode("taskB", "Task B", ctx -> {
            executedNodes.add("taskB");
        });
        
        Node taskC = Workflows.newTaskNode("taskC", "Task C", ctx -> {
            executedNodes.add("taskC");
        });
        
        Node joinGateway = Workflows.newParallelGateway("join", "Join Gateway");
        
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {
            executedNodes.add("end");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("parallel-multi-workflow")
                .name("Parallel Multi-Branch Workflow")
                .startNode(startNode)
                .addNode(splitGateway)
                .addNode(taskA)
                .addNode(taskB)
                .addNode(taskC)
                .addNode(joinGateway)
                .endNode(endNode)
                .connect("start", "split")
                .connect("split", "taskA")
                .connect("split", "taskB")
                .connect("split", "taskC")
                .connect("taskA", "join")
                .connect("taskB", "join")
                .connect("taskC", "join")
                .connect("join", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("parallel-multi-workflow");
        
        Assertions.assertEquals(WorkflowStatus.COMPLETED, instance.getStatus());
        Assertions.assertTrue(executedNodes.contains("start"));
        Assertions.assertTrue(executedNodes.contains("taskA"));
        Assertions.assertTrue(executedNodes.contains("taskB"));
        Assertions.assertTrue(executedNodes.contains("taskC"));
        Assertions.assertTrue(executedNodes.contains("end"));
    }
}
