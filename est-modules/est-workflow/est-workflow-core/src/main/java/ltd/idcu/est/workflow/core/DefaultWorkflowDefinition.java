package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.Edge;
import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultWorkflowDefinition implements WorkflowDefinition {
    
    private final String id;
    private final String name;
    private final String description;
    private final String version;
    private final List<Node> nodes;
    private final Map<String, Node> nodeMap;
    private final List<Edge> edges;
    private final Map<String, List<Edge>> outgoingEdgesMap;
    private final Map<String, List<Edge>> incomingEdgesMap;
    private Node startNode;
    private Node endNode;
    
    public DefaultWorkflowDefinition(String id, String name, String description, String version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.nodes = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        this.edges = new ArrayList<>();
        this.outgoingEdgesMap = new HashMap<>();
        this.incomingEdgesMap = new HashMap<>();
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getVersion() {
        return version;
    }
    
    @Override
    public List<Node> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    public void addNode(Node node) {
        nodes.add(node);
        nodeMap.put(node.getId(), node);
    }
    
    @Override
    public Node getStartNode() {
        return startNode;
    }
    
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
        if (!nodeMap.containsKey(startNode.getId())) {
            addNode(startNode);
        }
    }
    
    @Override
    public Node getEndNode() {
        return endNode;
    }
    
    public void setEndNode(Node endNode) {
        this.endNode = endNode;
        if (!nodeMap.containsKey(endNode.getId())) {
            addNode(endNode);
        }
    }
    
    @Override
    public Node getNode(String nodeId) {
        return nodeMap.get(nodeId);
    }
    
    @Override
    public boolean hasNode(String nodeId) {
        return nodeMap.containsKey(nodeId);
    }
    
    @Override
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    public void addEdge(Edge edge) {
        edges.add(edge);
        outgoingEdgesMap.computeIfAbsent(edge.getSourceNodeId(), k -> new ArrayList<>()).add(edge);
        incomingEdgesMap.computeIfAbsent(edge.getTargetNodeId(), k -> new ArrayList<>()).add(edge);
    }
    
    @Override
    public List<Edge> getOutgoingEdges(String nodeId) {
        return outgoingEdgesMap.getOrDefault(nodeId, new ArrayList<>());
    }
    
    @Override
    public List<Edge> getIncomingEdges(String nodeId) {
        return incomingEdgesMap.getOrDefault(nodeId, new ArrayList<>());
    }
}
