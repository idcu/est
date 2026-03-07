package ltd.idcu.est.workflow.core;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;

import java.io.StringReader;

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
              "sourceNodeId": "start",
              "targetNodeId": "end"
            }
          ],
          "startNodeId": "start"
        }
        """;
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        WorkflowDefinition workflow = parser.parse(new StringReader(json));
        
        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("json-test-workflow", workflow.getId());
        Assertions.assertEquals("JSON Test Workflow", workflow.getName());
        Assertions.assertTrue(workflow.getDescription().isPresent());
        Assertions.assertEquals("Test workflow from JSON", workflow.getDescription().get());
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
              "sourceNodeId": "start",
              "targetNodeId": "gateway"
            },
            {
              "sourceNodeId": "gateway",
              "targetNodeId": "path1",
              "label": "Condition 1"
            },
            {
              "sourceNodeId": "gateway",
              "targetNodeId": "path2",
              "label": "Condition 2"
            }
          ],
          "startNodeId": "start"
        }
        """;
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        WorkflowDefinition workflow = parser.parse(new StringReader(json));
        
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
          "startNodeId": "start"
        }
        """;
        
        WorkflowDefinitionParser parser = new JsonWorkflowDefinitionParser();
        WorkflowDefinition workflow = parser.parse(new StringReader(json));
        
        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("minimal-workflow", workflow.getId());
        Assertions.assertEquals("Minimal Workflow", workflow.getName());
        Assertions.assertEquals(1, workflow.getNodes().size());
    }
}
