package ltd.idcu.est.codecli.contract;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;
import ltd.idcu.est.test.annotation.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class ContractManagerTest {

    private Path tempWorkDir;
    private ContractManager contractManager;

    @BeforeEach
    void beforeEach() throws IOException {
        tempWorkDir = Files.createTempDirectory("est-code-cli-test-contract");
        contractManager = new ContractManager(tempWorkDir);
    }

    @AfterEach
    void afterEach() throws IOException {
        Path contractPath = tempWorkDir.resolve("EST.md");
        if (Files.exists(contractPath)) {
            Files.delete(contractPath);
        }
        if (Files.exists(tempWorkDir)) {
            Files.delete(tempWorkDir);
        }
    }

    @Test
    void testExistsWhenNotCreated() {
        assertFalse(contractManager.exists());
    }

    @Test
    void testLoadWhenNotExists() throws IOException {
        ProjectContract contract = contractManager.load();
        assertNull(contract);
    }

    @Test
    void testInit() throws IOException {
        ProjectContract contract = contractManager.init();
        assertNotNull(contract);
        assertNotNull(contract.getProjectName());
        assertNotNull(contract.getFrameworkVersion());
        assertTrue(contractManager.exists());
    }

    @Test
    void testInitSetsProjectName() throws IOException {
        ProjectContract contract = contractManager.init();
        assertEquals(tempWorkDir.getFileName().toString(), contract.getProjectName());
    }

    @Test
    void testInitSetsFrameworkVersion() throws IOException {
        ProjectContract contract = contractManager.init();
        assertEquals("2.3.0-SNAPSHOT", contract.getFrameworkVersion());
    }

    @Test
    void testSaveAndLoad() throws IOException {
        ProjectContract original = contractManager.init();
        original.setProjectName("TestProject");
        original.setDescription("Test description");
        original.setProjectType("Maven");
        
        contractManager.save(original);
        
        ProjectContract loaded = contractManager.load();
        assertNotNull(loaded);
        assertEquals("TestProject", loaded.getProjectName());
        assertEquals("Test description", loaded.getDescription());
        assertEquals("Maven", loaded.getProjectType());
    }

    @Test
    void testInitWithPomXml() throws IOException {
        Files.createFile(tempWorkDir.resolve("pom.xml"));
        
        ProjectContract contract = contractManager.init();
        assertEquals("Maven", contract.getProjectType());
        assertTrue(contract.getBuildCommands().contains("mvn clean compile"));
        assertTrue(contract.getBuildCommands().contains("mvn clean package"));
        assertTrue(contract.getTestCommands().contains("mvn test"));
        assertTrue(contract.getImportantFiles().contains("pom.xml"));
    }

    @Test
    void testInitWithBuildGradle() throws IOException {
        Files.createFile(tempWorkDir.resolve("build.gradle"));
        
        ProjectContract contract = contractManager.init();
        assertEquals("Gradle", contract.getProjectType());
        assertTrue(contract.getBuildCommands().contains("gradle clean build"));
        assertTrue(contract.getTestCommands().contains("gradle test"));
    }

    @Test
    void testInitWithReadme() throws IOException {
        Files.createFile(tempWorkDir.resolve("README.md"));
        
        ProjectContract contract = contractManager.init();
        assertTrue(contract.getImportantFiles().contains("README.md"));
    }

    @Test
    void testInitWithSrcDir() throws IOException {
        Files.createDirectory(tempWorkDir.resolve("src"));
        
        ProjectContract contract = contractManager.init();
        assertTrue(contract.getImportantFiles().contains("src/"));
    }

    @Test
    void testInitAddsDefaultCodingStandards() throws IOException {
        ProjectContract contract = contractManager.init();
        List<String> standards = contract.getCodingStandards();
        assertTrue(standards.size() >= 3);
        assertTrue(standards.contains("Follow EST framework coding conventions"));
        assertTrue(standards.contains("Use dependency injection via @Inject"));
        assertTrue(standards.contains("Mark components with @Component, @Controller, or @Service"));
    }

    @Test
    void testParseContract() throws IOException {
        String contractContent = """
            # EST Project Contract
            
            ## Project Name
            TestProject
            
            ## Description
            This is a test project
            
            ## Project Type
            Maven
            
            ## Framework Version
            2.3.0-SNAPSHOT
            
            ## Build Commands
            - mvn clean compile
            - mvn clean package
            
            ## Test Commands
            - mvn test
            
            ## Environment Variables
            - JAVA_HOME=/usr/lib/jvm/java-21
            - APP_ENV=test
            
            ## Coding Standards
            - Follow EST framework coding conventions
            
            ## Important Files
            - pom.xml
            - README.md
            """;
        
        Path contractPath = tempWorkDir.resolve("EST.md");
        Files.writeString(contractPath, contractContent);
        
        ProjectContract contract = contractManager.load();
        assertNotNull(contract);
        assertEquals("TestProject", contract.getProjectName());
        assertEquals("This is a test project", contract.getDescription());
        assertEquals("Maven", contract.getProjectType());
        assertEquals("2.3.0-SNAPSHOT", contract.getFrameworkVersion());
        assertTrue(contract.getBuildCommands().contains("mvn clean compile"));
        assertTrue(contract.getBuildCommands().contains("mvn clean package"));
        assertTrue(contract.getTestCommands().contains("mvn test"));
        assertEquals("/usr/lib/jvm/java-21", contract.getEnvironmentVariables().get("JAVA_HOME"));
        assertEquals("test", contract.getEnvironmentVariables().get("APP_ENV"));
        assertTrue(contract.getCodingStandards().contains("Follow EST framework coding conventions"));
        assertTrue(contract.getImportantFiles().contains("pom.xml"));
        assertTrue(contract.getImportantFiles().contains("README.md"));
    }
}
