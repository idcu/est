package ltd.idcu.est.examples.features;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.core.Workflows;

public class WorkflowExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Workflow Example ===\n");
        
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            System.out.println("[Start node executed]");
            ctx.setVariable("message", "Hello from Workflow!");
        });
        
        var processNode = Workflows.newTaskNode("process", "Process", ctx -> {
            System.out.println("[Process node executed]");
            String message = ctx.getVariable("message", String.class).orElse("");
            ctx.setVariable("result", message.toUpperCase());
        });
        
        var endNode = Workflows.newTaskNode("end", "End", ctx -> {
            System.out.println("[End node executed]");
            String result = ctx.getVariable("result", String.class).orElse("");
            System.out.println("Final result: " + result);
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("simple-workflow")
                .name("Simple Workflow Example")
                .description("A simple workflow example")
                .startNode(startNode)
                .addNode(processNode)
                .endNode(endNode)
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("simple-workflow");
        
        System.out.println("\nWorkflow execution completed!");
        System.out.println("Status: " + instance.getStatus());
        System.out.println("Duration: " + instance.getDuration() + "ms");
        
        engine.shutdown();
    }
}
