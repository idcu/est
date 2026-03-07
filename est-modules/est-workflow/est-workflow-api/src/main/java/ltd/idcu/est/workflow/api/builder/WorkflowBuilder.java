package ltd.idcu.est.workflow.api.builder;

import ltd.idcu.est.workflow.api.Edge;
import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowDefinition;

import java.util.function.Predicate;

public interface WorkflowBuilder {
    
    WorkflowBuilder id(String id);
    
    WorkflowBuilder name(String name);
    
    WorkflowBuilder description(String description);
    
    WorkflowBuilder version(String version);
    
    WorkflowBuilder addNode(Node node);
    
    WorkflowBuilder startNode(Node node);
    
    WorkflowBuilder endNode(Node node);
    
    WorkflowBuilder connect(String fromNodeId, String toNodeId);
    
    WorkflowBuilder connect(String fromNodeId, String toNodeId, String label);
    
    WorkflowBuilder connect(String fromNodeId, String toNodeId, String label, Predicate<WorkflowContext> condition);
    
    WorkflowBuilder addEdge(Edge edge);
    
    WorkflowDefinition build();
}
