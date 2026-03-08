package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ParallelGatewayTest {

    @Test
    public void testParallelGatewaySplitAndJoin() throws InterruptedException {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> executedNodes = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(2);
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            executedNodes.add("start");
        });
        
        Node splitGateway = Workflows.newParallelGateway("split", "Split Gateway");
        
        Node task1 = Workflows.newTaskNode("task1", "Task 1", ctx -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executedNodes.add("task1");
            latch.countDown();
        });
        
        Node task2 = Workflows.newTaskNode("task2", "Task 2", ctx -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executedNodes.add("task2");
            latch.countDown();
        });
        
        Node joinGateway = Workflows.newParallelGateway("join", "Join Gateway");
        
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {
            executedNodes.add("end");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("parallel-workflow")
                .name("Parallel Workflow")
                .startNode(startNode)
                .connect("start", "split")
                .connect("split", "task1")
                .connect("split", "task2")
                .connect("task1", "join")
                .connect("task2", "join")
                .connect("join", "end")
                .build();
        
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
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
                .connect("start", "split")
                .connect("split", "taskA")
                .connect("split", "taskB")
                .connect("split", "taskC")
                .connect("taskA", "join")
                .connect("taskB", "join")
                .connect("taskC", "join")
                .connect("join", "end")
                .build();
        
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Assertions.assertEquals(WorkflowStatus.COMPLETED, instance.getStatus());
        Assertions.assertTrue(executedNodes.contains("start"));
        Assertions.assertTrue(executedNodes.contains("taskA"));
        Assertions.assertTrue(executedNodes.contains("taskB"));
        Assertions.assertTrue(executedNodes.contains("taskC"));
        Assertions.assertTrue(executedNodes.contains("end"));
    }
}
