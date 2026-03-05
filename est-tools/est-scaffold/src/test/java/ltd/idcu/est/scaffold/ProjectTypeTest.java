package ltd.idcu.est.scaffold;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class ProjectTypeTest {

    @Test
    public void testFromCommand() {
        Assertions.assertEquals(ProjectType.BASIC, ProjectType.fromCommand("basic"));
        Assertions.assertEquals(ProjectType.BASIC, ProjectType.fromCommand("BASIC"));
        Assertions.assertEquals(ProjectType.WEB, ProjectType.fromCommand("web"));
        Assertions.assertEquals(ProjectType.WEB, ProjectType.fromCommand("WEB"));
        Assertions.assertEquals(ProjectType.API, ProjectType.fromCommand("api"));
        Assertions.assertEquals(ProjectType.CLI, ProjectType.fromCommand("cli"));
        Assertions.assertEquals(ProjectType.LIBRARY, ProjectType.fromCommand("library"));
        Assertions.assertEquals(ProjectType.PLUGIN, ProjectType.fromCommand("plugin"));
        Assertions.assertEquals(ProjectType.MICROSERVICE, ProjectType.fromCommand("microservice"));
    }

    @Test
    public void testFromInvalidCommand() {
        try {
            ProjectType.fromCommand("invalid");
            Assertions.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("Unknown project type"));
        }
    }

    @Test
    public void testGetCommand() {
        Assertions.assertEquals("basic", ProjectType.BASIC.getCommand());
        Assertions.assertEquals("web", ProjectType.WEB.getCommand());
        Assertions.assertEquals("api", ProjectType.API.getCommand());
        Assertions.assertEquals("cli", ProjectType.CLI.getCommand());
        Assertions.assertEquals("library", ProjectType.LIBRARY.getCommand());
        Assertions.assertEquals("plugin", ProjectType.PLUGIN.getCommand());
        Assertions.assertEquals("microservice", ProjectType.MICROSERVICE.getCommand());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertNotNull(ProjectType.BASIC.getDescription());
        Assertions.assertNotNull(ProjectType.WEB.getDescription());
        Assertions.assertNotNull(ProjectType.API.getDescription());
        Assertions.assertNotNull(ProjectType.CLI.getDescription());
        Assertions.assertNotNull(ProjectType.LIBRARY.getDescription());
        Assertions.assertNotNull(ProjectType.PLUGIN.getDescription());
        Assertions.assertNotNull(ProjectType.MICROSERVICE.getDescription());
    }

    @Test
    public void testValues() {
        ProjectType[] types = ProjectType.values();
        Assertions.assertEquals(13, types.length);
    }
}
