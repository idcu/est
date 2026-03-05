package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.CodeGenerator;

import java.util.Map;

public class DefaultCodeGenerator implements CodeGenerator {
    
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
            
            import ltd.idcu.est.features.data.api.Repository;
            
            public interface %s extends Repository {
                
            }
            """, packageName, className);
    }
    
    @Override
    public String generateEntity(String className, String packageName, Map<String, Object> options) {
        return String.format("""
            package %s;
            
            import ltd.idcu.est.features.data.api.annotation.Entity;
            import ltd.idcu.est.features.data.api.annotation.Id;
            import ltd.idcu.est.features.data.api.annotation.Column;
            
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
                    <est.version>1.3.0-SNAPSHOT</est.version>
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
}
