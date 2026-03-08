package ltd.idcu.est.workflow.api;

import java.util.List;
import java.util.Map;

public interface WorkflowDefinition {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    String getVersion();
    
    List<Node> getNodes();
    
    Node getStartNode();
    
    Node getEndNode();
    
    Node getNode(String nodeId);
    
    boolean hasNode(String nodeId);
    
    List<Edge> getEdges();
    
    List<Edge> getOutgoingEdges(String nodeId);
    
    List<Edge> getIncomingEdges(String nodeId);
}

