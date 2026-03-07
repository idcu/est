package ltd.idcu.est.examples.features;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowRepository;
import ltd.idcu.est.workflow.core.Workflows;

public class JsonWorkflowDefinitionExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Workflow JSON 定义示例 ===\n");
        
        WorkflowDefinitionParser parser = Workflows.newJsonParser();
        
        String json = "{\n" +
                "  \"id\": \"json-workflow\",\n" +
                "  \"name\": \"JSON 工作流\",\n" +
                "  \"description\": \"�?JSON 定义的工作流\",\n" +
                "  \"nodes\": [\n" +
                "    {\"id\": \"task1\", \"name\": \"任务1\", \"type\": \"TASK\"},\n" +
                "    {\"id\": \"task2\", \"name\": \"任务2\", \"type\": \"TASK\"}\n" +
                "  ],\n" +
                "  \"startNode\": \"task1\",\n" +
                "  \"endNode\": \"task2\"\n" +
                "}";
        
        System.out.println("解析 JSON 工作流定�?..");
        WorkflowDefinition workflow = parser.parse(json);
        System.out.println("工作�?ID: " + workflow.getId());
        System.out.println("工作流名�? " + workflow.getName());
        System.out.println("节点数量: " + workflow.getNodes().size());
        
        System.out.println("\n将工作流定义序列化回 JSON...");
        String serialized = parser.serialize(workflow);
        System.out.println(serialized);
        
        WorkflowRepository repository = Workflows.newMemoryRepository();
        WorkflowEngine engine = Workflows.newWorkflowEngine(repository);
        
        System.out.println("\n注册并执行工作流...");
        engine.registerWorkflow(workflow);
        engine.startWorkflow("json-workflow");
        
        System.out.println("\n工作流执行完成！");
        
        engine.shutdown();
    }
}
