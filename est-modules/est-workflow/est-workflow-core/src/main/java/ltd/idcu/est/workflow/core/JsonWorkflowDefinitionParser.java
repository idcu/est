package ltd.idcu.est.workflow.core;

import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.core.node.TaskNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWorkflowDefinitionParser implements WorkflowDefinitionParser {
    
    @Override
    public WorkflowDefinition parse(String content) {
        Map<String, Object> json = JsonUtils.parseObject(content);
        if (json == null) {
            throw new IllegalArgumentException("Invalid JSON content");
        }
        
        String id = JsonUtils.getString(json, "id");
        String name = JsonUtils.getString(json, "name");
        String description = JsonUtils.getString(json, "description", "");
        String version = JsonUtils.getString(json, "version", "1.0.0");
        
        DefaultWorkflowDefinition workflow = new DefaultWorkflowDefinition(id, name, description, version);
        
        List<Object> nodesArray = JsonUtils.getList(json, "nodes");
        Map<String, Node> nodeMap = new HashMap<>();
        
        if (nodesArray != null) {
            for (Object nodeObj : nodesArray) {
                if (nodeObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> nodeJson = (Map<String, Object>) nodeObj;
                    String nodeId = JsonUtils.getString(nodeJson, "id");
                    String nodeName = JsonUtils.getString(nodeJson, "name");
                    String nodeType = JsonUtils.getString(nodeJson, "type");
                    
                    Node node = createNode(nodeId, nodeName, nodeType);
                    nodeMap.put(nodeId, node);
                    workflow.addNode(node);
                }
            }
        }
        
        if (json.containsKey("startNode")) {
            String startNodeId = JsonUtils.getString(json, "startNode");
            workflow.setStartNode(nodeMap.get(startNodeId));
        }
        
        if (json.containsKey("endNode")) {
            String endNodeId = JsonUtils.getString(json, "endNode");
            workflow.setEndNode(nodeMap.get(endNodeId));
        }
        
        if (json.containsKey("edges")) {
            List<Object> edgesArray = JsonUtils.getList(json, "edges");
            if (edgesArray != null) {
                for (Object edgeObj : edgesArray) {
                    if (edgeObj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> edgeJson = (Map<String, Object>) edgeObj;
                        String edgeId = JsonUtils.getString(edgeJson, "id");
                        String sourceId = JsonUtils.getString(edgeJson, "source");
                        String targetId = JsonUtils.getString(edgeJson, "target");
                        String label = JsonUtils.getString(edgeJson, "label", "");
                        
                        DefaultEdge edge = new DefaultEdge(edgeId, sourceId, targetId, label);
                        workflow.addEdge(edge);
                    }
                }
            }
        }
        
        return workflow;
    }
    
    @Override
    public String serialize(WorkflowDefinition workflow) {
        Map<String, Object> builder = new HashMap<>();
        builder.put("id", workflow.getId());
        builder.put("name", workflow.getName());
        builder.put("description", workflow.getDescription() != null ? workflow.getDescription() : "");
        builder.put("version", workflow.getVersion() != null ? workflow.getVersion() : "1.0.0");
        
        List<Map<String, Object>> nodesList = new ArrayList<>();
        for (Node node : workflow.getNodes()) {
            Map<String, Object> nodeMap = new HashMap<>();
            nodeMap.put("id", node.getId());
            nodeMap.put("name", node.getName());
            nodeMap.put("type", node.getType());
            nodesList.add(nodeMap);
        }
        builder.put("nodes", nodesList);
        
        if (workflow.getStartNode() != null) {
            builder.put("startNode", workflow.getStartNode().getId());
        }
        
        if (workflow.getEndNode() != null) {
            builder.put("endNode", workflow.getEndNode().getId());
        }
        
        if (!workflow.getEdges().isEmpty()) {
            List<Map<String, Object>> edgesList = new ArrayList<>();
            for (var edge : workflow.getEdges()) {
                Map<String, Object> edgeMap = new HashMap<>();
                edgeMap.put("id", edge.getId());
                edgeMap.put("source", edge.getSourceNodeId());
                edgeMap.put("target", edge.getTargetNodeId());
                edgeMap.put("label", edge.getLabel() != null ? edge.getLabel() : "");
                edgesList.add(edgeMap);
            }
            builder.put("edges", edgesList);
        }
        
        return JsonUtils.toJson(builder);
    }
    
    private Node createNode(String id, String name, String type) {
        return new TaskNode(id, name, ctx -> {
        });
    }
}
