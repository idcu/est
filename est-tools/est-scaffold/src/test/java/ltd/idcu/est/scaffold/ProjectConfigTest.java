package ltd.idcu.est.scaffold;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class ProjectConfigTest {

    @Test
    public void testDefaultConfig() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertEquals("com.example", config.getGroupId());
        Assertions.assertEquals("test-project", config.getArtifactId());
        Assertions.assertEquals("1.0.0-SNAPSHOT", config.getVersion());
        Assertions.assertEquals("testproject", config.getPackageName());
        Assertions.assertEquals(21, config.getJavaVersion());
    }

    @Test
    public void testCustomGroupId() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setGroupId("com.mycompany");
        
        Assertions.assertEquals("com.mycompany", config.getGroupId());
    }

    @Test
    public void testCustomPackageName() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setPackageName("myapp");
        
        Assertions.assertEquals("myapp", config.getPackageName());
    }

    @Test
    public void testCustomJavaVersion() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setJavaVersion(17);
        
        Assertions.assertEquals(17, config.getJavaVersion());
    }

    @Test
    public void testPackagePath() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertEquals("com/example/testproject", config.getPackagePath());
    }

    @Test
    public void testFullPackageName() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertEquals("com.example.testproject", config.getFullPackageName());
    }

    @Test
    public void testCustomGroupIdPackagePath() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setGroupId("org.myorg");
        
        Assertions.assertEquals("org/myorg/testproject", config.getPackagePath());
    }

    @Test
    public void testGitInit() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertFalse(config.isGitInit());
        config.setGitInit(true);
        Assertions.assertTrue(config.isGitInit());
    }

    @Test
    public void testDryRun() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertFalse(config.isDryRun());
        config.setDryRun(true);
        Assertions.assertTrue(config.isDryRun());
    }

    @Test
    public void testCustomVersion() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setVersion("2.0.0");
        
        Assertions.assertEquals("2.0.0", config.getVersion());
    }

    @Test
    public void testDescription() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setDescription("My test project");
        
        Assertions.assertEquals("My test project", config.getDescription());
    }

    @Test
    public void testEstVersion() {
        ProjectConfig config = new ProjectConfig("test-project");
        config.setEstVersion("1.4.0");
        
        Assertions.assertEquals("1.4.0", config.getEstVersion());
    }

    @Test
    public void testDocker() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertFalse(config.isDocker());
        config.setDocker(true);
        Assertions.assertTrue(config.isDocker());
    }

    @Test
    public void testCiGithub() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertFalse(config.isCiGithub());
        config.setCiGithub(true);
        Assertions.assertTrue(config.isCiGithub());
    }

    @Test
    public void testCiGitlab() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertFalse(config.isCiGitlab());
        config.setCiGitlab(true);
        Assertions.assertTrue(config.isCiGitlab());
    }

    @Test
    public void testCiJenkins() {
        ProjectConfig config = new ProjectConfig("test-project");
        
        Assertions.assertFalse(config.isCiJenkins());
        config.setCiJenkins(true);
        Assertions.assertTrue(config.isCiJenkins());
    }
}
