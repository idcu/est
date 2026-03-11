package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.*;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;

public class JsonWorkflowDefinitionParserTest {

    @Test
    public void testParseSimpleWorkflow() {
        String json = """
        {
          "id": "json-test-workflow",
          "name": "JSON Test Workflow",
          "description": "Test workflow from JSON",
          "nodes": [
            {
              "id": "start",
              "name": "Start",
              "type": "task"
            },
            {
              "id": "end",
              "name": "End",
              "type": "task"
            }
          ],
          "edges": [
            {
              "id": "e1",
              "source": "start",
              "target": "end"
            }
          ],
          "startNode": "start",
          "endNode": "end"
        }
        """;
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        WorkflowDefinition workflow = parser.parse(json);
        
        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("json-test-workflow", workflow.getId());
        Assertions.assertEquals("JSON Test Workflow", workflow.getName());
        Assertions.assertNotNull(workflow.getDescription());
        Assertions.assertEquals("Test workflow from JSON", workflow.getDescription());
        Assertions.assertEquals(2, workflow.getNodes().size());
        Assertions.assertEquals(1, workflow.getEdges().size());
    }

    @Test
    public void testParseWorkflowWithGateway() {
        String json = """
        {
          "id": "gateway-workflow",
          "name": "Gateway Workflow",
          "nodes": [
            {
              "id": "start",
              "name": "Start",
              "type": "task"
            },
            {
              "id": "gateway",
              "name": "Decision Gateway",
              "type": "exclusiveGateway"
            },
            {
              "id": "path1",
              "name": "Path 1",
              "type": "task"
            },
            {
              "id": "path2",
              "name": "Path 2",
              "type": "task"
            }
          ],
          "edges": [
            {
              "id": "e1",
              "source": "start",
              "target": "gateway"
            },
            {
              "id": "e2",
              "source": "gateway",
              "target": "path1",
              "label": "Condition 1"
            },
            {
              "id": "e3",
              "source": "gateway",
              "target": "path2",
              "label": "Condition 2"
            }
          ],
          "startNode": "start"
        }
        """;
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        WorkflowDefinition workflow = parser.parse(json);
        
        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("gateway-workflow", workflow.getId());
        Assertions.assertEquals(4, workflow.getNodes().size());
        Assertions.assertEquals(3, workflow.getEdges().size());
    }

    @Test
    public void testParseMinimalWorkflow() {
        String json = """
        {
          "id": "minimal-workflow",
          "name": "Minimal Workflow",
          "nodes": [
            {
              "id": "start",
              "name": "Start",
              "type": "task"
            }
          ],
          "edges": [],
          "startNode": "start"
        }
        """;
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        WorkflowDefinition workflow = parser.parse(json);
        
        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("minimal-workflow", workflow.getId());
        Assertions.assertEquals("Minimal Workflow", workflow.getName());
        Assertions.assertEquals(1, workflow.getNodes().size());
    }

    @Test
    public void testSerializeWorkflow() {
        ltd.idcu.est.workflow.api.Node startNode = Workflows.newTaskNode("start", "Start", ctx -> {});
        ltd.idcu.est.workflow.api.Node endNode = Workflows.newTaskNode("end", "End", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("serialize-test")
                .name("Serialize Test")
                .description("Test serialization")
                .startNode(startNode)
                .endNode(endNode)
                .connect("start", "end")
                .build();
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        String json = parser.serialize(workflow);
        
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("serialize-test"));
        Assertions.assertTrue(json.contains("Serialize Test"));
        Assertions.assertTrue(json.contains("start"));
        Assertions.assertTrue(json.contains("end"));
    }
}
