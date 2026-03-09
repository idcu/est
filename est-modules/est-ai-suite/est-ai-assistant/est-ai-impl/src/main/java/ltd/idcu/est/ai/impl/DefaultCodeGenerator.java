package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.impl.llm.LlmClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultCodeGenerator implements CodeGenerator {
    
    private LlmClient llmClient;
    
    public DefaultCodeGenerator() {
        this.llmClient = LlmClientFactory.create();
    }
    
    public DefaultCodeGenerator(LlmClient llmClient) {
        this.llmClient = llmClient;
    }
    
    @Override
    public LlmClient getLlmClient() {
        return llmClient;
    }
    
    @Override
    public void setLlmClient(LlmClient llmClient) {
        this.llmClient = llmClient;
    }
    
    @Override
    public String generateWebApp(String projectName, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.web.Web;
            import ltd.idcu.est.web.api.WebApplication;
            
            public class %s {
                public static void main(String[] args) {
                    WebApplication app = Web.create("%s", "1.0.0");
                    
                    app.get("/", (req, res) -> {
                        res.html("<h1>Welcome to %s!</h1>");
                    });
                    
                    app.onStartup(() -> {
                        System.out.println("🚀 Server started!");
                        System.out.println("📍 http://localhost:8080");
                    });
                    
                    app.run(8080);
                }
            }
            """, packageName, projectName, projectName, projectName);
    }
    
    @Override
    public String generateController(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.web.Web;
            import ltd.idcu.est.web.api.WebApplication;
            import java.util.Map;
            
            public class %s {
                
                public static void registerRoutes(WebApplication app) {
                    app.get("/api/%s", (req, res) -> {
                        res.json(Map.of("message", "Hello from %s!"));
                    });
                }
            }
            """, packageName, className, className.toLowerCase().replace("controller", ""), className);
    }
    
    @Override
    public String generateService(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            public interface %s {
                
            }
            
            class %sImpl implements %s {
                
            }
            """, packageName, className, className, className);
    }
    
    @Override
    public String generateRepository(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.data.api.Repository;
            
            public interface %s extends Repository {
                
            }
            """, packageName, className);
    }
    
    @Override
    public String generateEntity(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.data.api.annotation.Entity;
            import ltd.idcu.est.data.api.annotation.Id;
            import ltd.idcu.est.data.api.annotation.Column;
            
            @Entity
            public class %s {
                
                @Id
                private String id;
                
                public String getId() {
                    return id;
                }
                
                public void setId(String id) {
                    this.id = id;
                }
            }
            """, packageName, className);
    }
    
    @Override
    public String generateTest(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.test.api.Assertions;
            import ltd.idcu.est.test.api.Tests;
            import org.junit.jupiter.api.Test;
            
            public class %sTest {
                
                @Test
                public void testSomething() {
                    Assertions.assertTrue(true);
                }
            }
            """, packageName, className);
    }
    
    @Override
    public String generatePomXml(String projectName, String groupId, String artifactId, String version) {
        return String.format("""
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                
                <groupId>%s</groupId>
                <artifactId>%s</artifactId>
                <version>%s</version>
                
                <name>%s</name>
                
                <properties>
                    <maven.compiler.source>21</maven.compiler.source>
                    <maven.compiler.target>21</maven.compiler.target>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    <est.version>2.1.0</est.version>
                </properties>
                
                <dependencies>
                    <dependency>
                        <groupId>ltd.idcu</groupId>
                        <artifactId>est-web-impl</artifactId>
                        <version>${est.version}</version>
                    </dependency>
                </dependencies>
            </project>
            """, groupId, artifactId, version, projectName);
    }
    
    @Override
    public String generateFromRequirement(String requirement) {
        if (llmClient != null && llmClient.isAvailable()) {
            String prompt = "你是一个Java开发专家，使用EST框架。请根据以下需求生成完整的Java代码：\n\n" + requirement +
                           "\n\n请只返回代码，不要其他解释。如果需要生成多个文件，请使�?====分隔符分隔不同文件，并在每个文件前注明文件名�?;
            return llmClient.generate(prompt);
        }
        return "LLM not available. Please configure an LLM client first.";
    }
    
    @Override
    public String generateUnitTest(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.test.api.Assertions;
            import ltd.idcu.est.test.api.Tests;
            import org.junit.jupiter.api.BeforeEach;
            import org.junit.jupiter.api.Test;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.Nested;
            
            import java.util.List;
            import java.util.Map;
            import java.util.Optional;
            
            @DisplayName("%s 单元测试")
            public class %sTest {
                
                private %s service;
                
                @BeforeEach
                void setUp() {
                    service = new %s();
                }
                
                @Nested
                @DisplayName("基本功能测试")
                class BasicFunctionalityTests {
                    
                    @Test
                    @DisplayName("测试对象创建")
                    void testObjectCreation() {
                        Assertions.assertNotNull(service);
                    }
                    
                    @Test
                    @DisplayName("测试基本操作")
                    void testBasicOperations() {
                        Assertions.assertTrue(true);
                    }
                }
                
                @Nested
                @DisplayName("边界条件测试")
                class EdgeCaseTests {
                    
                    @Test
                    @DisplayName("测试空值处理")
                    void testNullHandling() {
                        Assertions.assertDoesNotThrow(() -> {
                        });
                    }
                    
                    @Test
                    @DisplayName("测试空集合")
                    void testEmptyCollection() {
                        List<String> emptyList = List.of();
                        Assertions.assertTrue(emptyList.isEmpty());
                    }
                }
                
                @Nested
                @DisplayName("异常情况测试")
                class ExceptionTests {
                    
                    @Test
                    @DisplayName("测试预期异常")
                    void testExpectedExceptions() {
                        Assertions.assertThrows(Exception.class, () -> {
                            throw new Exception("Test exception");
                        });
                    }
                }
            }
            """, packageName, className, className, className, className);
    }
    
    @Override
    public String generateIntegrationTest(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.test.api.Assertions;
            import ltd.idcu.est.test.api.Tests;
            import org.junit.jupiter.api.BeforeEach;
            import org.junit.jupiter.api.Test;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.Tag;
            
            @Tag("integration")
            @DisplayName("%s 集成测试")
            public class %sIntegrationTest {
                
                @BeforeEach
                void setUp() {
                }
                
                @Test
                @DisplayName("测试端到端流程")
                void testEndToEndFlow() {
                    Assertions.assertTrue(true);
                }
                
                @Test
                @DisplayName("测试组件交互")
                void testComponentInteraction() {
                    Assertions.assertTrue(true);
                }
                
                @Test
                @DisplayName("测试数据持久化")
                void testDataPersistence() {
                    Assertions.assertTrue(true);
                }
            }
            """, packageName, className, className);
    }
    
    @Override
    public String generatePerformanceTest(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.test.api.Assertions;
            import org.junit.jupiter.api.Test;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.Tag;
            
            import java.util.concurrent.TimeUnit;
            
            @Tag("performance")
            @DisplayName("%s 性能测试")
            public class %sPerformanceTest {
                
                @Test
                @DisplayName("测试响应时间")
                void testResponseTime() {
                    long startTime = System.nanoTime();
                    
                    long endTime = System.nanoTime();
                    long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
                    
                    Assertions.assertTrue(duration < 1000, "响应时间应小于1秒");
                }
                
                @Test
                @DisplayName("测试吞吐量")
                void testThroughput() {
                    int iterations = 1000;
                    long startTime = System.nanoTime();
                    
                    for (int i = 0; i < iterations; i++) {
                    }
                    
                    long endTime = System.nanoTime();
                    long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
                    double throughput = (double) iterations / (duration / 1000.0);
                    
                    Assertions.assertTrue(throughput > 100, "吞吐量应大于100操作/秒");
                }
                
                @Test
                @DisplayName("测试内存使用")
                void testMemoryUsage() {
                    Runtime runtime = Runtime.getRuntime();
                    long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
                    
                    long afterMemory = runtime.totalMemory() - runtime.freeMemory();
                    long memoryUsed = afterMemory - beforeMemory;
                    
                    Assertions.assertTrue(memoryUsed < 100 * 1024 * 1024, "内存使用应小于100MB");
                }
            }
            """, packageName, className, className);
    }
    
    @Override
    public List<String> generateTestSuite(String className, String packageName, Map<String, Object> options) {
        List<String> testSuite = new ArrayList<>();
        
        testSuite.add(generateUnitTest(className, packageName, options));
        testSuite.add(generateIntegrationTest(className, packageName, options));
        testSuite.add(generatePerformanceTest(className, packageName, options));
        testSuite.add(generateMockData(className, options));
        testSuite.add(generateTestAssertions(className, options));
        
        return testSuite;
    }
    
    @Override
    public String generateMockData(String className, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import java.util.*;
            
            public class %sMockData {
                
                public static List<String> getTestNames() {
                    return Arrays.asList("Test1", "Test2", "Test3");
                }
                
                public static Map<String, Object> getTestMap() {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", 1);
                    map.put("name", "Test");
                    map.put("active", true);
                    return map;
                }
                
                public static List<Map<String, Object>> getTestEntities() {
                    List<Map<String, Object>> entities = new ArrayList<>();
                    for (int i = 1; i <= 10; i++) {
                        Map<String, Object> entity = new HashMap<>();
                        entity.put("id", i);
                        entity.put("name", "Entity" + i);
                        entities.add(entity);
                    }
                    return entities;
                }
            }
            """, "test", className);
    }
    
    @Override
    public String generateTestAssertions(String className, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.test.api.Assertions;
            import java.util.Collection;
            import java.util.Map;
            
            public class %sAssertions {
                
                public static void assertValidEntity(Object entity) {
                    Assertions.assertNotNull(entity, "实体不应为null");
                }
                
                public static void assertValidId(String id) {
                    Assertions.assertNotNull(id, "ID不应为null");
                    Assertions.assertFalse(id.isEmpty(), "ID不应为空");
                }
                
                public static void assertValidCollection(Collection<?> collection) {
                    Assertions.assertNotNull(collection, "集合不应为null");
                    Assertions.assertFalse(collection.isEmpty(), "集合不应为空");
                }
                
                public static void assertValidMap(Map<?, ?> map) {
                    Assertions.assertNotNull(map, "Map不应为null");
                    Assertions.assertFalse(map.isEmpty(), "Map不应为空");
                }
                
                public static void assertInRange(int value, int min, int max) {
                    Assertions.assertTrue(value >= min, "值应大于等于最小值");
                    Assertions.assertTrue(value <= max, "值应小于等于最大值");
                }
            }
            """, "test", className);
    }
}
