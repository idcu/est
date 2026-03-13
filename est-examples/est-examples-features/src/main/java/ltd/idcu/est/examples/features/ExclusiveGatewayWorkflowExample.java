package ltd.idcu.est.examples.features;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.core.Workflows;

public class ExclusiveGatewayWorkflowExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Workflow Exclusive Gateway Example ===\n");
        
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "Start", ctx -> {
            System.out.println("[Start] Preparing data");
            ctx.setVariable("amount", 800);
        });
        
        var gateway = Workflows.newExclusiveGateway("gateway", "Check Amount");
        
        var highAmountNode = Workflows.newTaskNode("high", "High Amount Process", ctx -> {
            System.out.println("[High Amount Process] Processing amount > 500");
            Integer amount = ctx.getVariable("amount", Integer.class).orElse(0);
            System.out.println("Amount: " + amount);
        });
        
        var lowAmountNode = Workflows.newTaskNode("low", "Low Amount Process", ctx -> {
            System.out.println("[Low Amount Process] Processing amount <= 500");
            Integer amount = ctx.getVariable("amount", Integer.class).orElse(0);
            System.out.println("Amount: " + amount);
        });
        
        var endNode = Workflows.newTaskNode("end", "End", ctx -> {
            System.out.println("[End] Workflow finished");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("gateway-workflow")
                .name("Exclusive Gateway Example")
                .startNode(startNode)
                .addNode(gateway)
                .addNode(highAmountNode)
                .addNode(lowAmountNode)
                .endNode(endNode)
                .connect("start", "gateway")
                .connect("gateway", "high", "High", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) > 500)
                .connect("gateway", "low", "Low", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) <= 500)
                .connect("high", "end")
                .connect("low", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("gateway-workflow");
        
        System.out.println("\nWorkflow execution complete!");
        System.out.println("Status: " + instance.getStatus());
        
        engine.shutdown();
    }
}
