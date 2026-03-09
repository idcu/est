package ltd.idcu.est.codecli.contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContractManager {
    
    private static final String CONTRACT_FILENAME = "EST.md";
    
    private final Path workDir;
    
    public ContractManager(String workDir) {
        this.workDir = Paths.get(workDir);
    }
    
    public ContractManager(Path workDir) {
        this.workDir = workDir;
    }
    
    public boolean exists() {
        return Files.exists(workDir.resolve(CONTRACT_FILENAME));
    }
    
    public ProjectContract load() throws IOException {
        Path contractPath = workDir.resolve(CONTRACT_FILENAME);
        if (!Files.exists(contractPath)) {
            return null;
        }
        
        String content = Files.readString(contractPath);
        return parseContract(content);
    }
    
    public void save(ProjectContract contract) throws IOException {
        Path contractPath = workDir.resolve(CONTRACT_FILENAME);
        Files.writeString(contractPath, contract.toString());
    }
    
    public ProjectContract init() throws IOException {
        ProjectContract contract = new ProjectContract();
        
        contract.setProjectName(workDir.getFileName().toString());
        contract.setFrameworkVersion("2.3.0-SNAPSHOT");
        
        detectProjectType(contract);
        detectBuildCommands(contract);
        detectTestCommands(contract);
        detectImportantFiles(contract);
        
        addDefaultCodingStandards(contract);
        
        save(contract);
        return contract;
    }
    
    private void detectProjectType(ProjectContract contract) {
        if (Files.exists(workDir.resolve("pom.xml"))) {
            contract.setProjectType("Maven");
        } else if (Files.exists(workDir.resolve("build.gradle"))) {
            contract.setProjectType("Gradle");
        } else {
            contract.setProjectType("Unknown");
        }
    }
    
    private void detectBuildCommands(ProjectContract contract) {
        if (Files.exists(workDir.resolve("pom.xml"))) {
            contract.addBuildCommand("mvn clean compile");
            contract.addBuildCommand("mvn clean package");
        } else if (Files.exists(workDir.resolve("build.gradle"))) {
            contract.addBuildCommand("gradle clean build");
        }
    }
    
    private void detectTestCommands(ProjectContract contract) {
        if (Files.exists(workDir.resolve("pom.xml"))) {
            contract.addTestCommand("mvn test");
        } else if (Files.exists(workDir.resolve("build.gradle"))) {
            contract.addTestCommand("gradle test");
        }
    }
    
    private void detectImportantFiles(ProjectContract contract) {
        if (Files.exists(workDir.resolve("pom.xml"))) {
            contract.addImportantFile("pom.xml");
        }
        if (Files.exists(workDir.resolve("README.md"))) {
            contract.addImportantFile("README.md");
        }
        if (Files.exists(workDir.resolve("src"))) {
            contract.addImportantFile("src/");
        }
    }
    
    private void addDefaultCodingStandards(ProjectContract contract) {
        contract.addCodingStandard("Follow EST framework coding conventions");
        contract.addCodingStandard("Use dependency injection via @Inject");
        contract.addCodingStandard("Mark components with @Component, @Controller, or @Service");
    }
    
    private ProjectContract parseContract(String content) {
        ProjectContract contract = new ProjectContract();
        
        String[] lines = content.split("\n");
        String currentSection = null;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.startsWith("## ")) {
                currentSection = line.substring(3).trim();
            } else if (currentSection != null && !line.isEmpty()) {
                parseSectionLine(contract, currentSection, line);
            }
        }
        
        return contract;
    }
    
    private void parseSectionLine(ProjectContract contract, String section, String line) {
        if (line.startsWith("- ")) {
            String item = line.substring(2).trim();
            
            switch (section) {
                case "Build Commands":
                    contract.addBuildCommand(item);
                    break;
                case "Test Commands":
                    contract.addTestCommand(item);
                    break;
                case "Run Commands":
                    contract.addRunCommand(item);
                    break;
                case "Environment Variables":
                    int eqIndex = item.indexOf('=');
                    if (eqIndex > 0) {
                        String key = item.substring(0, eqIndex).trim();
                        String value = item.substring(eqIndex + 1).trim();
                        contract.addEnvironmentVariable(key, value);
                    }
                    break;
                case "Coding Standards":
                    contract.addCodingStandard(item);
                    break;
                case "Important Files":
                    contract.addImportantFile(item);
                    break;
            }
        } else {
            switch (section) {
                case "Project Name":
                    if (contract.getProjectName() == null) {
                        contract.setProjectName(line);
                    }
                    break;
                case "Description":
                    if (contract.getDescription() == null) {
                        contract.setDescription(line);
                    }
                    break;
                case "Project Type":
                    if (contract.getProjectType() == null) {
                        contract.setProjectType(line);
                    }
                    break;
                case "Framework Version":
                    if (contract.getFrameworkVersion() == null) {
                        contract.setFrameworkVersion(line);
                    }
                    break;
            }
        }
    }
}
