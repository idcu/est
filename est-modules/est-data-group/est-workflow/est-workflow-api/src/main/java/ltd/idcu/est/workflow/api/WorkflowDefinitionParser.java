package ltd.idcu.est.workflow.api;

public interface WorkflowDefinitionParser {
    
    WorkflowDefinition parse(String content);
    
    String serialize(WorkflowDefinition workflow);
}
