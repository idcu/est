package ltd.idcu.est.examples.features;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.core.Workflows;

public class WorkflowExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Workflow 示例 ===\n");
        
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "开�?, ctx -> {
            System.out.println("[开始节点执�?);
            ctx.setVariable("message", "Hello from Workflow!");
        });
        
        var processNode = Workflows.newTaskNode("process", "处理", ctx -> {
            System.out.println("[处理节点执行");
            String message = ctx.getVariable("message", String.class).orElse("");
            ctx.setVariable("result", message.toUpperCase());
        });
        
        var endNode = Workflows.newTaskNode("end", "结束", ctx -> {
            System.out.println("[结束节点执行");
            String result = ctx.getVariable("result", String.class).orElse("");
            System.out.println("最终结�? " + result);
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("simple-workflow")
                .name("简单工作流示例")
                .description("一个简单的工作流示�?)
                .startNode(startNode)
                .addNode(processNode)
                .endNode(endNode)
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("simple-workflow");
        
        System.out.println("\n工作流执行完成！");
        System.out.println("状�? " + instance.getStatus());
        System.out.println("耗时: " + instance.getDuration() + "ms");
        
        engine.shutdown();
    }
}
