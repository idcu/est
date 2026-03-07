package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.*;

import java.util.ArrayList;
import java.util.List;

public class WorkflowEngineTest {

    @Test
    public void testSimpleWorkflowExecution() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> executedNodes = new ArrayList<>();
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            executedNodes.add("start");
        });
        
        Node processNode = Workflows.newTaskNode("process", "Process", ctx -> {
            executedNodes.add("process");
            ctx.setVariable("processed", true);
        });
        
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {
            executedNodes.add("end");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("test-workflow")
                .name("Test Workflow")
                .startNode(startNode)
                .connect("start", "process")
                .connect("process", "end")
                .build();
        
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Assertions.assertNotNull(instance);
        Assertions.assertEquals(WorkflowStatus.COMPLETED, instance.getStatus());
        Assertions.assertEquals(3, executedNodes.size());
        Assertions.assertTrue(executedNodes.contains("start"));
        Assertions.assertTrue(executedNodes.contains("process"));
        Assertions.assertTrue(executedNodes.contains("end"));
        
        var processed = instance.getContext().getVariable("processed", Boolean.class);
        Assertions.assertTrue(processed.isPresent());
        Assertions.assertTrue(processed.get());
    }

    @Test
    public void testExclusiveGateway() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> executedNodes = new ArrayList<>();
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            executedNodes.add("start");
            ctx.setVariable("amount", 1000);
        });
        
        Node gateway = Workflows.newExclusiveGateway("gateway", "Amount Gateway");
        
        Node highNode = Workflows.newTaskNode("high", "High Amount", ctx -> {
            executedNodes.add("high");
        });
        
        Node lowNode = Workflows.newTaskNode("low", "Low Amount", ctx -> {
            executedNodes.add("low");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("gateway-workflow")
                .name("Gateway Workflow")
                .startNode(startNode)
                .connect("start", "gateway")
                .connect("gateway", "high", "High Amount", 
                    ctx -> ctx.getVariable("amount", Integer.class).orElse(0) > 500)
                .connect("gateway", "low", "Low Amount")
                .build();
        
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Assertions.assertEquals(WorkflowStatus.COMPLETED, instance.getStatus());
        Assertions.assertTrue(executedNodes.contains("start"));
        Assertions.assertTrue(executedNodes.contains("high"));
        Assertions.assertFalse(executedNodes.contains("low"));
    }

    @Test
    public void testWorkflowListener() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> events = new ArrayList<>();
        
        engine.addWorkflowListener(new WorkflowListener() {
            @Override
            public void onWorkflowStarted(WorkflowInstance instance) {
                events.add("started:" + instance.getDefinition().getId());
            }

            @Override
            public void onWorkflowCompleted(WorkflowInstance instance) {
                events.add("completed:" + instance.getDefinition().getId());
            }

            @Override
            public void onWorkflowFailed(WorkflowInstance instance, Throwable error) {
                events.add("failed:" + instance.getDefinition().getId());
            }
        });
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("listener-workflow")
                .name("Listener Workflow")
                .startNode(startNode)
                .build();
        
        engine.startWorkflow(workflow);
        
        Assertions.assertTrue(events.contains("started:listener-workflow"));
        Assertions.assertTrue(events.contains("completed:listener-workflow"));
    }

    @Test
    public void testNodeListener() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> events = new ArrayList<>();
        
        engine.addNodeListener(new NodeListener() {
            @Override
            public void onNodeStarted(WorkflowInstance instance, NodeExecution execution) {
                events.add("nodeStarted:" + execution.getNodeId());
            }

            @Override
            public void onNodeCompleted(WorkflowInstance instance, NodeExecution execution) {
                events.add("nodeCompleted:" + execution.getNodeId());
            }

            @Override
            public void onNodeFailed(WorkflowInstance instance, NodeExecution execution, Throwable error) {
                events.add("nodeFailed:" + execution.getNodeId());
            }
        });
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("node-listener-workflow")
                .name("Node Listener Workflow")
                .startNode(startNode)
                .connect("start", "end")
                .build();
        
        engine.startWorkflow(workflow);
        
        Assertions.assertTrue(events.contains("nodeStarted:start"));
        Assertions.assertTrue(events.contains("nodeCompleted:start"));
        Assertions.assertTrue(events.contains("nodeStarted:end"));
        Assertions.assertTrue(events.contains("nodeCompleted:end"));
    }
}
