package ltd.idcu.est.scaffold;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.io.IOException;

public class TemplateFileSystemTest {

    @Test
    public void testTemplateEngineRendering() throws IOException {
        TemplateEngine engine = new TemplateEngine();
        engine.setVariable("testVar", "Hello World");
        
        String result = engine.render("Test: ${testVar}");
        
        Assertions.assertEquals("Test: Hello World", result);
    }

    @Test
    public void testTemplateEngineConditionals() throws IOException {
        TemplateEngine engine = new TemplateEngine();
        engine.setVariable("showText", "true");
        
        String result = engine.render("${if showText}Shown Text${endif}");
        
        Assertions.assertTrue(result.contains("Shown Text"));
        
        engine.setVariable("showText", "false");
        result = engine.render("${if showText}Shown Text${endif}");
        
        Assertions.assertFalse(result.contains("Shown Text"));
    }

    @Test
    public void testTemplateFileSystemCreation() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        Assertions.assertNotNull(fs);
    }

    @Test
    public void testTemplateFileSystemRenderPomXml() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String pomXml = fs.renderPomXml();
        
        Assertions.assertNotNull(pomXml);
        Assertions.assertTrue(pomXml.contains("test-project"));
    }

    @Test
    public void testTemplateFileSystemRenderGitignore() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String gitignore = fs.renderGitignore();
        
        Assertions.assertNotNull(gitignore);
        Assertions.assertTrue(gitignore.contains("target/"));
    }

    @Test
    public void testTemplateFileSystemRenderReadme() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String readme = fs.renderReadme();
        
        Assertions.assertNotNull(readme);
        Assertions.assertTrue(readme.contains("test-project"));
    }

    @Test
    public void testTemplateFileSystemRenderMainClassBasic() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String mainClass = fs.renderMainClass();
        
        Assertions.assertNotNull(mainClass);
        Assertions.assertTrue(mainClass.contains("package com.example.testproject"));
    }

    @Test
    public void testTemplateFileSystemRenderMainClassWeb() throws IOException {
        ProjectConfig config = new ProjectConfig("test-web-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.WEB, config);
        
        String mainClass = fs.renderMainClass();
        
        Assertions.assertNotNull(mainClass);
        Assertions.assertTrue(mainClass.contains("WebApplication"));
    }

    @Test
    public void testTemplateFileSystemRenderDockerfile() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String dockerfile = fs.renderDockerfile();
        
        Assertions.assertNotNull(dockerfile);
        Assertions.assertTrue(dockerfile.contains("FROM"));
    }

    @Test
    public void testTemplateFileSystemRenderDockerignore() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String dockerignore = fs.renderDockerignore();
        
        Assertions.assertNotNull(dockerignore);
        Assertions.assertTrue(dockerignore.contains("target/"));
    }

    @Test
    public void testTemplateFileSystemRenderDockerCompose() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String compose = fs.renderDockerCompose();
        
        Assertions.assertNotNull(compose);
        Assertions.assertTrue(compose.contains("version:"));
    }

    @Test
    public void testTemplateFileSystemRenderGithubActions() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String actions = fs.renderGithubActions();
        
        Assertions.assertNotNull(actions);
        Assertions.assertTrue(actions.contains("name:"));
    }

    @Test
    public void testTemplateFileSystemRenderGitlabCI() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String gitlab = fs.renderGitlabCI();
        
        Assertions.assertNotNull(gitlab);
        Assertions.assertTrue(gitlab.contains("image:"));
    }

    @Test
    public void testTemplateFileSystemRenderJenkinsfile() throws IOException {
        ProjectConfig config = new ProjectConfig("test-project");
        TemplateFileSystem fs = new TemplateFileSystem(ProjectType.BASIC, config);
        
        String jenkins = fs.renderJenkinsfile();
        
        Assertions.assertNotNull(jenkins);
        Assertions.assertTrue(jenkins.contains("pipeline"));
    }
}
