package ltd.idcu.est.examples.features;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.core.Workflows;

public class ExclusiveGatewayWorkflowExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Workflow 排他网关示例 ===\n");
        
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "开�?, ctx -> {
            System.out.println("[开始] 准备数据");
            ctx.setVariable("amount", 800);
        });
        
        var gateway = Workflows.newExclusiveGateway("gateway", "判断金额");
        
        var highAmountNode = Workflows.newTaskNode("high", "高额处理", ctx -> {
            System.out.println("[高额处理] 处理金额 > 500");
            Integer amount = ctx.getVariable("amount", Integer.class).orElse(0);
            System.out.println("金额: " + amount);
        });
        
        var lowAmountNode = Workflows.newTaskNode("low", "低额处理", ctx -> {
            System.out.println("[低额处理] 处理金额 <= 500");
            Integer amount = ctx.getVariable("amount", Integer.class).orElse(0);
            System.out.println("金额: " + amount);
        });
        
        var endNode = Workflows.newTaskNode("end", "结束", ctx -> {
            System.out.println("[结束] 工作流结�?);
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("gateway-workflow")
                .name("排他网关示例")
                .startNode(startNode)
                .addNode(gateway)
                .addNode(highAmountNode)
                .addNode(lowAmountNode)
                .endNode(endNode)
                .connect("start", "gateway")
                .connect("gateway", "high", "高额", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) > 500)
                .connect("gateway", "low", "低额", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) <= 500)
                .connect("high", "end")
                .connect("low", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("gateway-workflow");
        
        System.out.println("\n工作流执行完成！");
        System.out.println("状�? " + instance.getStatus());
        
        engine.shutdown();
    }
}
