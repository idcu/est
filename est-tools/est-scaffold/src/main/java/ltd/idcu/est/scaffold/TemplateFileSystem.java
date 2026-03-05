package ltd.idcu.est.scaffold;

import java.io.IOException;

public class TemplateFileSystem {

    private final ProjectConfig config;
    private final ProjectType type;
    private final TemplateEngine engine;

    public TemplateFileSystem(ProjectType type, ProjectConfig config) {
        this.type = type;
        this.config = config;
        this.engine = TemplateEngine.fromConfig(config);
        setTypeFlags();
    }

    private void setTypeFlags() {
        switch (type) {
            case BASIC:
                engine.setVariable("basic", "true");
                break;
            case WEB:
                engine.setVariable("web", "true");
                break;
            case API:
                engine.setVariable("api", "true");
                break;
            case CLI:
                engine.setVariable("cli", "true");
                break;
            case LIBRARY:
                engine.setVariable("library", "true");
                break;
            case PLUGIN:
                engine.setVariable("plugin", "true");
                break;
            case MICROSERVICE:
                engine.setVariable("microservice", "true");
                break;
        }
    }

    public String renderPomXml() throws IOException {
        return engine.renderFromFile("templates/pom.xml.template");
    }

    public String renderGitignore() throws IOException {
        return engine.renderFromFile("templates/.gitignore.template");
    }

    public String renderReadme() throws IOException {
        return engine.renderFromFile("templates/README.md.template");
    }

    public String renderMainClass() throws IOException {
        String templatePath = getTypeSpecificTemplate("Main.java.template");
        return engine.renderFromFile(templatePath);
    }

    public String renderController() throws IOException {
        if (type == ProjectType.WEB) {
            return engine.renderFromFile("templates/web/HomeController.java.template");
        } else if (type == ProjectType.API) {
            return engine.renderFromFile("templates/api/UserController.java.template");
        }
        throw new UnsupportedOperationException("Controller not supported for project type: " + type);
    }

    public String renderModel() throws IOException {
        if (type == ProjectType.API) {
            return engine.renderFromFile("templates/api/User.java.template");
        }
        throw new UnsupportedOperationException("Model not supported for project type: " + type);
    }

    private String getTypeSpecificTemplate(String fileName) {
        String typePath = getTypePath();
        return "templates/" + typePath + "/" + fileName;
    }

    private String getTypePath() {
        switch (type) {
            case BASIC:
                return "basic";
            case WEB:
                return "web";
            case API:
                return "api";
            case CLI:
                return "cli";
            case LIBRARY:
                return "library";
            case PLUGIN:
                return "plugin";
            case MICROSERVICE:
                return "microservice";
            default:
                return "basic";
        }
    }

    public String getControllerFileName() {
        if (type == ProjectType.WEB) {
            return "HomeController.java";
        } else if (type == ProjectType.API) {
            return "UserController.java";
        }
        throw new UnsupportedOperationException("Controller not supported for project type: " + type);
    }

    public String getModelFileName() {
        if (type == ProjectType.API) {
            return "User.java";
        }
        throw new UnsupportedOperationException("Model not supported for project type: " + type);
    }

    public String renderDockerfile() throws IOException {
        return engine.renderFromFile("templates/Dockerfile.template");
    }

    public String renderDockerignore() throws IOException {
        return engine.renderFromFile("templates/.dockerignore.template");
    }

    public String renderDockerCompose() throws IOException {
        return engine.renderFromFile("templates/docker-compose.yml.template");
    }

    public String renderGithubActions() throws IOException {
        return engine.renderFromFile("templates/.github/workflows/maven.yml.template");
    }

    public String renderGitlabCI() throws IOException {
        return engine.renderFromFile("templates/.gitlab-ci.yml.template");
    }

    public String renderJenkinsfile() throws IOException {
        return engine.renderFromFile("templates/Jenkinsfile.template");
    }
}
