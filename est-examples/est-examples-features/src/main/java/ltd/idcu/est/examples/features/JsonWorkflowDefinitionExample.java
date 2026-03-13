package ltd.idcu.est.examples.features;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowRepository;
import ltd.idcu.est.workflow.core.Workflows;

public class JsonWorkflowDefinitionExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Workflow JSON Definition Example ===\n");
        
        WorkflowDefinitionParser parser = Workflows.newJsonParser();
        
        String json = "{\n" +
                "  \"id\": \"json-workflow\",\n" +
                "  \"name\": \"JSON Workflow\",\n" +
                "  \"description\": \"Workflow defined in JSON\",\n" +
                "  \"nodes\": [\n" +
                "    {\"id\": \"task1\", \"name\": \"Task 1\", \"type\": \"TASK\"},\n" +
                "    {\"id\": \"task2\", \"name\": \"Task 2\", \"type\": \"TASK\"}\n" +
                "  ],\n" +
                "  \"startNode\": \"task1\",\n" +
                "  \"endNode\": \"task2\"\n" +
                "}";
        
        System.out.println("Parsing JSON workflow definition...");
        WorkflowDefinition workflow = parser.parse(json);
        System.out.println("Workflow ID: " + workflow.getId());
        System.out.println("Workflow Name: " + workflow.getName());
        System.out.println("Node count: " + workflow.getNodes().size());
        
        System.out.println("\nSerializing workflow definition back to JSON...");
        String serialized = parser.serialize(workflow);
        System.out.println(serialized);
        
        WorkflowRepository repository = Workflows.newMemoryRepository();
        WorkflowEngine engine = Workflows.newWorkflowEngine(repository);
        
        System.out.println("\nRegistering and executing workflow...");
        engine.registerWorkflow(workflow);
        engine.startWorkflow("json-workflow");
        
        System.out.println("\nWorkflow execution complete!");
        
        engine.shutdown();
    }
}
