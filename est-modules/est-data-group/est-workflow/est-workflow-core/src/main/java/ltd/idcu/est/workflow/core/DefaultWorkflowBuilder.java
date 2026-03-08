package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.Edge;
import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.builder.WorkflowBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public class DefaultWorkflowBuilder implements WorkflowBuilder {
    
    private String id;
    private String name;
    private String description;
    private String version = "1.0.0";
    private final DefaultWorkflowDefinition definition;
    private final List<Edge> edges;
    private final AtomicLong edgeIdGenerator;
    
    public DefaultWorkflowBuilder() {
        this.definition = new DefaultWorkflowDefinition(null, null, null, null);
        this.edges = new ArrayList<>();
        this.edgeIdGenerator = new AtomicLong(1);
    }
    
    @Override
    public WorkflowBuilder id(String id) {
        this.id = id;
        return this;
    }
    
    @Override
    public WorkflowBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public WorkflowBuilder description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public WorkflowBuilder version(String version) {
        this.version = version;
        return this;
    }
    
    @Override
    public WorkflowBuilder addNode(Node node) {
        definition.addNode(node);
        return this;
    }
    
    @Override
    public WorkflowBuilder startNode(Node node) {
        definition.setStartNode(node);
        return this;
    }
    
    @Override
    public WorkflowBuilder endNode(Node node) {
        definition.setEndNode(node);
        return this;
    }
    
    @Override
    public WorkflowBuilder connect(String fromNodeId, String toNodeId) {
        return connect(fromNodeId, toNodeId, null, null);
    }
    
    @Override
    public WorkflowBuilder connect(String fromNodeId, String toNodeId, String label) {
        return connect(fromNodeId, toNodeId, label, null);
    }
    
    @Override
    public WorkflowBuilder connect(String fromNodeId, String toNodeId, String label, Predicate<WorkflowContext> condition) {
        String edgeId = "edge-" + edgeIdGenerator.getAndIncrement();
        Edge edge = new DefaultEdge(edgeId, fromNodeId, toNodeId, label, condition);
        edges.add(edge);
        return this;
    }
    
    @Override
    public WorkflowBuilder addEdge(Edge edge) {
        edges.add(edge);
        return this;
    }
    
    @Override
    public WorkflowDefinition build() {
        if (id == null || id.isEmpty()) {
            throw new IllegalStateException("Workflow id is required");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Workflow name is required");
        }
        
        DefaultWorkflowDefinition result = new DefaultWorkflowDefinition(id, name, description, version);
        for (Node node : definition.getNodes()) {
            result.addNode(node);
        }
        if (definition.getStartNode() != null) {
            result.setStartNode(definition.getStartNode());
        }
        if (definition.getEndNode() != null) {
            result.setEndNode(definition.getEndNode());
        }
        for (Edge edge : edges) {
            result.addEdge(edge);
        }
        
        return result;
    }
}
