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
                .addNode(processNode)
                .endNode(endNode)
                .connect("start", "process")
                .connect("process", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("test-workflow");
        
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
        
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {
            executedNodes.add("end");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("gateway-workflow")
                .name("Gateway Workflow")
                .startNode(startNode)
                .addNode(gateway)
                .addNode(highNode)
                .addNode(lowNode)
                .endNode(endNode)
                .connect("start", "gateway")
                .connect("gateway", "high", "High Amount", 
                    ctx -> ctx.getVariable("amount", Integer.class).orElse(0) > 500)
                .connect("gateway", "low", "Low Amount")
                .connect("high", "end")
                .connect("low", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("gateway-workflow");
        
        Assertions.assertEquals(WorkflowStatus.COMPLETED, instance.getStatus());
        Assertions.assertTrue(executedNodes.contains("start"));
        Assertions.assertTrue(executedNodes.contains("high"));
        Assertions.assertFalse(executedNodes.contains("low"));
        Assertions.assertTrue(executedNodes.contains("end"));
    }

    @Test
    public void testWorkflowListener() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> events = new ArrayList<>();
        
        engine.addWorkflowListener(new WorkflowListener() {
            @Override
            public void onWorkflowStart(WorkflowInstance instance) {
                events.add("started:" + instance.getWorkflowId());
            }

            @Override
            public void onWorkflowComplete(WorkflowInstance instance) {
                events.add("completed:" + instance.getWorkflowId());
            }

            @Override
            public void onWorkflowFail(WorkflowInstance instance, Throwable error) {
                events.add("failed:" + instance.getWorkflowId());
            }

            @Override
            public void onWorkflowPause(WorkflowInstance instance) {
                events.add("paused:" + instance.getWorkflowId());
            }

            @Override
            public void onWorkflowResume(WorkflowInstance instance) {
                events.add("resumed:" + instance.getWorkflowId());
            }

            @Override
            public void onWorkflowCancel(WorkflowInstance instance) {
                events.add("cancelled:" + instance.getWorkflowId());
            }
        });
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("listener-workflow")
                .name("Listener Workflow")
                .startNode(startNode)
                .build();
        
        engine.registerWorkflow(workflow);
        engine.startWorkflow("listener-workflow");
        
        Assertions.assertTrue(events.contains("started:listener-workflow"));
        Assertions.assertTrue(events.contains("completed:listener-workflow"));
    }

    @Test
    public void testNodeListener() {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        List<String> events = new ArrayList<>();
        
        engine.addNodeListener(new NodeListener() {
            @Override
            public void onNodeStart(WorkflowInstance instance, Node node) {
                events.add("nodeStarted:" + node.getId());
            }

            @Override
            public void onNodeComplete(WorkflowInstance instance, Node node) {
                events.add("nodeCompleted:" + node.getId());
            }

            @Override
            public void onNodeFail(WorkflowInstance instance, Node node, Throwable error) {
                events.add("nodeFailed:" + node.getId());
            }

            @Override
            public void onNodeSkip(WorkflowInstance instance, Node node) {
                events.add("nodeSkipped:" + node.getId());
            }
        });
        
        Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        Node endNode = Workflows.newTaskNode("end", "End", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("node-listener-workflow")
                .name("Node Listener Workflow")
                .startNode(startNode)
                .endNode(endNode)
                .connect("start", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        engine.startWorkflow("node-listener-workflow");
        
        Assertions.assertTrue(events.contains("nodeStarted:start"));
        Assertions.assertTrue(events.contains("nodeCompleted:start"));
        Assertions.assertTrue(events.contains("nodeStarted:end"));
        Assertions.assertTrue(events.contains("nodeCompleted:end"));
    }
}
