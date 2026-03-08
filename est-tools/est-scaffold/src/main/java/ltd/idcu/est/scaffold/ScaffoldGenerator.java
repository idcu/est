package ltd.idcu.est.scaffold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ScaffoldGenerator {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("interactive")) {
            try {
                runInteractiveMode();
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        if (args.length > 0 && args[0].equals("snippet")) {
            try {
                handleSnippetCommand(args);
            } catch (Exception e) {
                System.err.println(ConsoleColors.red("Error: " + e.getMessage()));
                e.printStackTrace();
            }
            return;
        }

        if (args.length < 2) {
            printUsage();
            return;
        }

        String command = args[0];
        String projectName = args[1];

        try {
            ProjectConfig config = parseArguments(args, projectName);
            generateProject(ProjectType.fromCommand(command), config);
        } catch (IllegalArgumentException e) {
            System.err.println(ConsoleColors.red("Error: " + e.getMessage()));
            printUsage();
        } catch (IOException e) {
            System.err.println(ConsoleColors.red("Error generating project: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private static ProjectConfig parseArguments(String[] args, String projectName) throws IOException {
        ProjectConfig config = new ProjectConfig(projectName, true);
        
        for (int i = 2; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--groupId=")) {
                config.setGroupId(arg.substring("--groupId=".length()));
            } else if (arg.startsWith("--version=")) {
                config.setVersion(arg.substring("--version=".length()));
            } else if (arg.startsWith("--package=")) {
                config.setPackageName(arg.substring("--package=".length()));
            } else if (arg.startsWith("--java=")) {
                try {
                    config.setJavaVersion(Integer.parseInt(arg.substring("--java=".length())));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Java version: " + arg.substring("--java=".length()));
                }
            } else if (arg.startsWith("--preset=")) {
                String presetName = arg.substring("--preset=".length());
                config.applyPreset(presetName);
            } else if (arg.equals("--git")) {
                config.setGitInit(true);
            } else if (arg.equals("--docker")) {
                config.setDocker(true);
            } else if (arg.equals("--ci-github")) {
                config.setCiGithub(true);
            } else if (arg.equals("--ci-gitlab")) {
                config.setCiGitlab(true);
            } else if (arg.equals("--ci-jenkins")) {
                config.setCiJenkins(true);
            } else if (arg.equals("--dry-run")) {
                config.setDryRun(true);
            }
        }
        
        return config;
    }

    private static void printUsage() {
        System.out.println("EST Scaffold Generator");
        System.out.println("Usage:");
        System.out.println("  java -jar est-scaffold.jar interactive");
        System.out.println("  java -jar est-scaffold.jar <type> <project-name> [options]");
        System.out.println("  java -jar est-scaffold.jar snippet list");
        System.out.println("  java -jar est-scaffold.jar snippet <template> <file> [key=value...]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  interactive  - Run interactive configuration wizard");
        System.out.println("  snippet      - Generate code snippets");
        System.out.println();
        System.out.println("Project types:");
        System.out.println("  basic        - Create basic EST project");
        System.out.println("  web          - Create web application project");
        System.out.println("  api          - Create REST API project");
        System.out.println("  cli          - Create command line tool");
        System.out.println("  library      - Create library project");
        System.out.println("  plugin       - Create EST plugin project");
        System.out.println("  microservice - Create microservice project");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --groupId=<group>   - Set custom groupId (default: com.example)");
        System.out.println("  --version=<ver>     - Set project version (default: 1.0.0-SNAPSHOT)");
        System.out.println("  --package=<pkg>     - Set custom package name (default: derived from project name)");
        System.out.println("  --java=<version>    - Set Java version (default: 21)");
        System.out.println("  --preset=<name>     - Use preset from config file");
        System.out.println("  --git               - Initialize Git repository");
        System.out.println("  --docker            - Generate Docker configuration files");
        System.out.println("  --ci-github         - Generate GitHub Actions workflow");
        System.out.println("  --ci-gitlab         - Generate GitLab CI configuration");
        System.out.println("  --ci-jenkins        - Generate Jenkinsfile");
        System.out.println("  --dry-run           - Preview without creating files");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java -jar est-scaffold.jar interactive");
        System.out.println("  java -jar est-scaffold.jar web my-app --groupId=com.mycompany --java=17 --git --docker --ci-github");
        System.out.println("  java -jar est-scaffold.jar basic my-app --preset=microservice --dry-run");
    }

    public static void generateProject(ProjectType type, ProjectConfig config) throws IOException {
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("=======================================")));
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("  EST Project Generator")));
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("=======================================")));
        System.out.println();
        
        if (config.isDryRun()) {
            System.out.println(ConsoleColors.yellow("DRY RUN MODE - No files will be created"));
            System.out.println();
        }
        
        System.out.println(ConsoleColors.yellow("Generating " + type.getDescription() + ": " + config.getArtifactId()));
        System.out.println();
        
        Path basePath = Paths.get(config.getArtifactId());
        
        if (!config.isDryRun() && Files.exists(basePath)) {
            throw new IOException(ConsoleColors.red("Directory already exists: " + basePath.toAbsolutePath()));
        }
        
        int step = 1;
        int totalSteps = calculateTotalSteps(type, config.isGitInit(), config.isDocker(), 
            config.isCiGithub(), config.isCiGitlab(), config.isCiJenkins());
        
        System.out.println(ConsoleColors.blue("Creating project structure..."));
        
        TemplateFileSystem templateFS = new TemplateFileSystem(type, config);
        
        printProgress(step++, totalSteps, "Creating directory structure");
        if (config.isDryRun()) {
            System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: " + basePath));
        } else {
            Files.createDirectories(basePath);
        }
        createProjectStructure(basePath, config, type, config.isDryRun());
        
        printProgress(step++, totalSteps, "Creating pom.xml");
        String pomContent = templateFS.renderPomXml();
        FileWriterUtil.writeFile(basePath.resolve("pom.xml"), pomContent, config.isDryRun());
        
        printProgress(step++, totalSteps, "Creating Main.java");
        String mainContent = templateFS.renderMainClass();
        Path javaPath = basePath.resolve("src/main/java/" + config.getPackagePath() + "/Main.java");
        FileWriterUtil.writeFile(javaPath, mainContent, config.isDryRun());
        
        if (type == ProjectType.WEB || type == ProjectType.API) {
            printProgress(step++, totalSteps, "Creating controller");
            String controllerContent = templateFS.renderController();
            Path controllerPath = basePath.resolve("src/main/java/" + config.getPackagePath() + "/controller/" + templateFS.getControllerFileName());
            FileWriterUtil.writeFile(controllerPath, controllerContent, config.isDryRun());
        }
        
        if (type == ProjectType.API) {
            printProgress(step++, totalSteps, "Creating model");
            String modelContent = templateFS.renderModel();
            Path modelPath = basePath.resolve("src/main/java/" + config.getPackagePath() + "/model/" + templateFS.getModelFileName());
            FileWriterUtil.writeFile(modelPath, modelContent, config.isDryRun());
        }
        
        printProgress(step++, totalSteps, "Creating .gitignore");
        String gitignoreContent = templateFS.renderGitignore();
        FileWriterUtil.writeFile(basePath.resolve(".gitignore"), gitignoreContent, config.isDryRun());
        
        printProgress(step++, totalSteps, "Creating README.md");
        String readmeContent = templateFS.renderReadme();
        FileWriterUtil.writeFile(basePath.resolve("README.md"), readmeContent, config.isDryRun());
        
        if (config.isDocker()) {
            printProgress(step++, totalSteps, "Creating Dockerfile");
            String dockerfileContent = templateFS.renderDockerfile();
            FileWriterUtil.writeFile(basePath.resolve("Dockerfile"), dockerfileContent, config.isDryRun());
            
            printProgress(step++, totalSteps, "Creating .dockerignore");
            String dockerignoreContent = templateFS.renderDockerignore();
            FileWriterUtil.writeFile(basePath.resolve(".dockerignore"), dockerignoreContent, config.isDryRun());
            
            printProgress(step++, totalSteps, "Creating docker-compose.yml");
            String composeContent = templateFS.renderDockerCompose();
            FileWriterUtil.writeFile(basePath.resolve("docker-compose.yml"), composeContent, config.isDryRun());
        }
        
        if (config.isCiGithub()) {
            printProgress(step++, totalSteps, "Creating GitHub Actions workflow");
            String githubContent = templateFS.renderGithubActions();
            Path githubPath = basePath.resolve(".github/workflows/maven.yml");
            if (config.isDryRun()) {
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: .github/workflows"));
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create file: .github/workflows/maven.yml"));
            } else {
                Files.createDirectories(githubPath.getParent());
                FileWriterUtil.writeFile(githubPath, githubContent, false);
            }
        }
        
        if (config.isCiGitlab()) {
            printProgress(step++, totalSteps, "Creating GitLab CI configuration");
            String gitlabContent = templateFS.renderGitlabCI();
            FileWriterUtil.writeFile(basePath.resolve(".gitlab-ci.yml"), gitlabContent, config.isDryRun());
        }
        
        if (config.isCiJenkins()) {
            printProgress(step++, totalSteps, "Creating Jenkinsfile");
            String jenkinsContent = templateFS.renderJenkinsfile();
            FileWriterUtil.writeFile(basePath.resolve("Jenkinsfile"), jenkinsContent, config.isDryRun());
        }
        
        if (config.isGitInit()) {
            printProgress(step++, totalSteps, "Initializing Git repository");
            if (config.isDryRun()) {
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would initialize Git repository"));
            } else {
                initializeGitRepository(basePath, config);
            }
        }
        
        System.out.println();
        if (config.isDryRun()) {
            System.out.println(ConsoleColors.bold(ConsoleColors.yellow("âœ?Dry run completed!")));
        } else {
            System.out.println(ConsoleColors.bold(ConsoleColors.green("âœ?Project created successfully!")));
            System.out.println();
            System.out.println(ConsoleColors.cyan("Location: " + basePath.toAbsolutePath()));
            System.out.println();
            System.out.println(ConsoleColors.yellow("Next steps:"));
            System.out.println(ConsoleColors.white("  cd " + config.getArtifactId()));
            System.out.println(ConsoleColors.white("  mvn clean install"));
            System.out.println(ConsoleColors.white("  mvn exec:java -Dexec.mainClass=\"" + config.getFullPackageName() + ".Main\""));
            if (config.isDocker()) {
                System.out.println();
                System.out.println(ConsoleColors.yellow("Docker commands:"));
                System.out.println(ConsoleColors.white("  docker build -t " + config.getArtifactId() + " ."));
                System.out.println(ConsoleColors.white("  docker-compose up"));
            }
        }
        System.out.println();
    }
    
    private static int calculateTotalSteps(ProjectType type, boolean gitInit, boolean docker, 
        boolean ciGithub, boolean ciGitlab, boolean ciJenkins) {
        int steps = 5;
        if (type == ProjectType.WEB || type == ProjectType.API) {
            steps++;
        }
        if (type == ProjectType.API) {
            steps++;
        }
        if (docker) {
            steps += 3;
        }
        if (ciGithub) {
            steps++;
        }
        if (ciGitlab) {
            steps++;
        }
        if (ciJenkins) {
            steps++;
        }
        if (gitInit) {
            steps++;
        }
        return steps;
    }
    
    private static void initializeGitRepository(Path basePath, ProjectConfig config) {
        try {
            ProcessBuilder initBuilder = new ProcessBuilder("git", "init");
            initBuilder.directory(basePath.toFile());
            Process initProcess = initBuilder.start();
            initProcess.waitFor();
            
            ProcessBuilder addBuilder = new ProcessBuilder("git", "add", ".");
            addBuilder.directory(basePath.toFile());
            Process addProcess = addBuilder.start();
            addProcess.waitFor();
            
            ProcessBuilder commitBuilder = new ProcessBuilder("git", "commit", "-m", "Initial commit");
            commitBuilder.directory(basePath.toFile());
            Process commitProcess = commitBuilder.start();
            commitProcess.waitFor();
            
        } catch (Exception e) {
            System.err.println(ConsoleColors.yellow("Warning: Failed to initialize Git repository: " + e.getMessage()));
        }
    }
    
    private static void printProgress(int current, int total, String message) {
        System.out.println("[" + current + "/" + total + "] " + message + "... " + ConsoleColors.green("âœ?));
    }

    private static void createProjectStructure(Path basePath, ProjectConfig config, ProjectType type, boolean dryRun) throws IOException {
        String packagePath = config.getPackagePath();
        
        if (dryRun) {
            System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/java/" + packagePath));
            System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/resources"));
            System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/test/java/" + packagePath));
            System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/test/resources"));
            
            if (type == ProjectType.WEB || type == ProjectType.API) {
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/java/" + packagePath + "/controller"));
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/java/" + packagePath + "/service"));
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/java/" + packagePath + "/model"));
            }
            
            if (type == ProjectType.WEB) {
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/resources/static"));
                System.out.println(ConsoleColors.cyan("  [DRY RUN] Would create directory: src/main/resources/templates"));
            }
            return;
        }
        
        Files.createDirectories(basePath.resolve("src/main/java/" + packagePath));
        Files.createDirectories(basePath.resolve("src/main/resources"));
        Files.createDirectories(basePath.resolve("src/test/java/" + packagePath));
        Files.createDirectories(basePath.resolve("src/test/resources"));
        
        if (type == ProjectType.WEB || type == ProjectType.API) {
            Files.createDirectories(basePath.resolve("src/main/java/" + packagePath + "/controller"));
            Files.createDirectories(basePath.resolve("src/main/java/" + packagePath + "/service"));
            Files.createDirectories(basePath.resolve("src/main/java/" + packagePath + "/model"));
        }
        
        if (type == ProjectType.WEB) {
            Files.createDirectories(basePath.resolve("src/main/resources/static"));
            Files.createDirectories(basePath.resolve("src/main/resources/templates"));
        }
    }

    private static void runInteractiveMode() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=======================================");
        System.out.println("  EST Scaffold - Interactive Wizard");
        System.out.println("=======================================");
        System.out.println();

        System.out.println("Available project types:");
        for (ProjectType pt : ProjectType.values()) {
            System.out.println("  " + pt.getCommand() + " - " + pt.getDescription());
        }
        System.out.println();

        ProjectType projectType = null;
        while (projectType == null) {
            String input = readInput(reader, "Enter project type", "web");
            try {
                projectType = ProjectType.fromCommand(input);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColors.red("Invalid project type. Please try again."));
            }
        }

        String projectName = readInput(reader, "Enter project name", "my-app");

        ProjectConfig config = new ProjectConfig(projectName, true);

        String groupId = readInput(reader, "Enter groupId", config.getGroupId());
        config.setGroupId(groupId);

        String version = readInput(reader, "Enter version", config.getVersion());
        config.setVersion(version);

        String packageName = readInput(reader, "Enter package name", config.getPackageName());
        config.setPackageName(packageName);

        String javaVersionStr = readInput(reader, "Enter Java version", String.valueOf(config.getJavaVersion()));
        try {
            config.setJavaVersion(Integer.parseInt(javaVersionStr));
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.yellow("Invalid Java version, using default: " + config.getJavaVersion()));
        }

        String gitInit = readInput(reader, "Initialize Git repository? (y/n)", "n");
        config.setGitInit(gitInit.equalsIgnoreCase("y") || gitInit.equalsIgnoreCase("yes"));
        
        String docker = readInput(reader, "Generate Docker files? (y/n)", "n");
        config.setDocker(docker.equalsIgnoreCase("y") || docker.equalsIgnoreCase("yes"));
        
        String ciGithub = readInput(reader, "Generate GitHub Actions? (y/n)", "n");
        config.setCiGithub(ciGithub.equalsIgnoreCase("y") || ciGithub.equalsIgnoreCase("yes"));
        
        String ciGitlab = readInput(reader, "Generate GitLab CI? (y/n)", "n");
        config.setCiGitlab(ciGitlab.equalsIgnoreCase("y") || ciGitlab.equalsIgnoreCase("yes"));
        
        String ciJenkins = readInput(reader, "Generate Jenkinsfile? (y/n)", "n");
        config.setCiJenkins(ciJenkins.equalsIgnoreCase("y") || ciJenkins.equalsIgnoreCase("yes"));

        System.out.println();
        System.out.println("Configuration:");
        System.out.println("  Project Type: " + projectType.getDescription());
        System.out.println("  Project Name: " + config.getArtifactId());
        System.out.println("  GroupId: " + config.getGroupId());
        System.out.println("  Version: " + config.getVersion());
        System.out.println("  Package: " + config.getFullPackageName());
        System.out.println("  Java Version: " + config.getJavaVersion());
        System.out.println("  Git Init: " + (config.isGitInit() ? "Yes" : "No"));
        System.out.println("  Docker: " + (config.isDocker() ? "Yes" : "No"));
        System.out.println("  GitHub Actions: " + (config.isCiGithub() ? "Yes" : "No"));
        System.out.println("  GitLab CI: " + (config.isCiGitlab() ? "Yes" : "No"));
        System.out.println("  Jenkins: " + (config.isCiJenkins() ? "Yes" : "No"));
        System.out.println();

        String confirm = readInput(reader, "Generate project? (y/n)", "y");
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            generateProject(projectType, config);
        } else {
            System.out.println("Cancelled.");
        }
    }

    private static String readInput(BufferedReader reader, String prompt, String defaultValue) throws IOException {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = reader.readLine();
        return (input == null || input.trim().isEmpty()) ? defaultValue : input.trim();
    }

    private static void handleSnippetCommand(String[] args) throws IOException {
        if (args.length < 2 || args[1].equals("list")) {
            CodeSnippetGenerator.listAllTemplates();
            return;
        }

        if (args.length < 3) {
            printSnippetUsage();
            return;
        }

        String templateName = args[1];
        String outputFile = args[2];
        
        Map<String, String> variables = new HashMap<>();
        for (int i = 3; i < args.length; i++) {
            String arg = args[i];
            if (arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                variables.put(parts[0], parts[1]);
            }
        }

        CodeSnippetGenerator.CodeTemplate template = CodeSnippetGenerator.getTemplate(templateName);
        if (template == null) {
            System.err.println(ConsoleColors.red("Template not found: " + templateName));
            CodeSnippetGenerator.listAllTemplates();
            return;
        }

        for (String requiredVar : template.variables) {
            if (!variables.containsKey(requiredVar)) {
                System.err.println(ConsoleColors.red("Missing required variable: " + requiredVar));
                System.err.println("Required variables: " + String.join(", ", template.variables));
                return;
            }
        }

        String code = CodeSnippetGenerator.generate(templateName, variables);
        Path outputPath = Paths.get(outputFile);
        
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("Generating code snippet...")));
        System.out.println("  Template: " + template.name);
        System.out.println("  Output: " + outputPath.toAbsolutePath());
        System.out.println();
        
        FileWriterUtil.writeFile(outputPath, code, false);
        System.out.println(ConsoleColors.bold(ConsoleColors.green("âœ?Code snippet generated successfully!")));
    }

    private static void printSnippetUsage() {
        System.out.println("EST Scaffold - Code Snippet Generator");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar est-scaffold.jar snippet list");
        System.out.println("  java -jar est-scaffold.jar snippet <template-name> <output-file> [key=value...]");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar est-scaffold.jar snippet list");
        System.out.println("  java -jar est-scaffold.jar snippet model User.java package=com.example className=User");
        System.out.println("  java -jar est-scaffold.jar snippet controller UserController.java package=com.example className=UserController model=User resource=users");
    }
}
