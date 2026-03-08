package ltd.idcu.est.scaffold;

import java.util.*;

public class ConfigGenerator {
    
    public static String generatePomXml(ProjectConfig config, ProjectType projectType) {
        StringBuilder dependencies = new StringBuilder();
        dependencies.append("        <dependency>\n");
        dependencies.append("            <groupId>ltd.idcu</groupId>\n");
        dependencies.append("            <artifactId>est-core-api</artifactId>\n");
        dependencies.append("            <version>${est.version}</version>\n");
        dependencies.append("        </dependency>\n");
        dependencies.append("        <dependency>\n");
        dependencies.append("            <groupId>ltd.idcu</groupId>\n");
        dependencies.append("            <artifactId>est-core-impl</artifactId>\n");
        dependencies.append("            <version>${est.version}</version>\n");
        dependencies.append("        </dependency>\n");
        
        switch (projectType) {
            case WEB:
            case API:
            case FULLSTACK:
            case MICROSERVICE:
            case AI_ENHANCED:
            case REALTIME:
            case SECURE_APP:
                dependencies.append("        <dependency>\n");
                dependencies.append("            <groupId>ltd.idcu</groupId>\n");
                dependencies.append("            <artifactId>est-web-api</artifactId>\n");
                dependencies.append("            <version>${est.version}</version>\n");
                dependencies.append("        </dependency>\n");
                dependencies.append("        <dependency>\n");
                dependencies.append("            <groupId>ltd.idcu</groupId>\n");
                dependencies.append("            <artifactId>est-web-impl</artifactId>\n");
                dependencies.append("            <version>${est.version}</version>\n");
                dependencies.append("        </dependency>\n");
                break;
        }
        
        if (projectType == ProjectType.CACHE_APP) {
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-cache-api</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-cache-memory</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
        }
        
        if (projectType == ProjectType.EVENT_DRIVEN || projectType == ProjectType.MICROSERVICE) {
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-event-api</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-event-local</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
        }
        
        if (projectType == ProjectType.AI_ENHANCED) {
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-ai-api</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
        }
        
        if (projectType == ProjectType.SECURE_APP) {
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-security-api</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
            dependencies.append("        <dependency>\n");
            dependencies.append("            <groupId>ltd.idcu</groupId>\n");
            dependencies.append("            <artifactId>est-features-security-jwt</artifactId>\n");
            dependencies.append("            <version>${est.version}</version>\n");
            dependencies.append("        </dependency>\n");
        }
        
        return String.format("""
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
            
            <groupId>%s</groupId>
            <artifactId>%s</artifactId>
            <version>%s</version>
            <packaging>jar</packaging>
            
            <name>%s</name>
            <description>%s</description>
            
            <properties>
                <maven.compiler.source>%s</maven.compiler.source>
                <maven.compiler.target>%s</maven.compiler.target>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <est.version>2.1.0</est.version>
            </properties>
            
            <dependencies>
        %s
            </dependencies>
            
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-jar-plugin</artifactId>
                        <version>3.2.0</version>
                        <configuration>
                            <archive>
                                <manifest>
                                    <mainClass>%s.Main</mainClass>
                                </manifest>
                            </archive>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </project>
        """, 
            config.getGroupId(),
            config.getArtifactId(),
            config.getVersion(),
            config.getArtifactId(),
            projectType.getChineseDescription(),
            String.valueOf(config.getJavaVersion()),
            String.valueOf(config.getJavaVersion()),
            dependencies.toString(),
            config.getPackageName()
        );
    }
    
    public static String generateApplicationYml(ProjectConfig config, ProjectType projectType) {
        StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("app:\n");
        configBuilder.append("  name: ").append(config.getArtifactId()).append("\n");
        configBuilder.append("  version: ").append(config.getVersion()).append("\n");
        configBuilder.append("  env: development\n\n");
        
        configBuilder.append("server:\n");
        configBuilder.append("  port: 8080\n");
        configBuilder.append("  host: 0.0.0.0\n\n");
        
        if (projectType == ProjectType.CACHE_APP) {
            configBuilder.append("cache:\n");
            configBuilder.append("  type: memory\n");
            configBuilder.append("  ttl: 3600\n\n");
        }
        
        if (projectType == ProjectType.SECURE_APP) {
            configBuilder.append("security:\n");
            configBuilder.append("  jwt:\n");
            configBuilder.append("    secret: your-super-secret-key-change-in-production\n");
            configBuilder.append("    expiration: 86400000\n\n");
        }
        
        if (projectType == ProjectType.AI_ENHANCED) {
            configBuilder.append("ai:\n");
            configBuilder.append("  provider: openai\n");
            configBuilder.append("  api-key: ${AI_API_KEY}\n");
            configBuilder.append("  model: gpt-4\n\n");
        }
        
        configBuilder.append("logging:\n");
        configBuilder.append("  level: INFO\n");
        configBuilder.append("  format: \"[%d{yyyy-MM-dd HH:mm:ss}] [%p] %c - %m%n\"\n");
        
        return configBuilder.toString();
    }
    
    public static String generateGitignore() {
        return """
        # Compiled class files
        *.class
        
        # Log files
        *.log
        
        # Package Files
        *.jar
        *.war
        *.nar
        *.ear
        *.zip
        *.tar.gz
        *.rar
        
        # Maven
        target/
        pom.xml.tag
        pom.xml.releaseBackup
        pom.xml.versionsBackup
        pom.xml.next
        release.properties
        dependency-reduced-pom.xml
        buildNumber.properties
        .mvn/timing.properties
        .mvn/wrapper/maven-wrapper.jar
        
        # IDE
        .idea/
        *.iml
        *.iws
        *.ipr
        .vscode/
        .settings/
        .classpath
        .project
        
        # OS
        .DS_Store
        Thumbs.db
        
        # Environment variables
        .env
        .env.local
        """;
    }
    
    public static String generateReadme(ProjectConfig config, ProjectType projectType) {
        return String.format("""
        # %s
        
        %s
        
        ## 快速开�?
        
        ### 前置要求
        - JDK %s+
        - Maven 3.6+
        
        ### 构建项目
        
        ```bash
        mvn clean package
        ```
        
        ### 运行项目
        
        ```bash
        mvn exec:java -Dexec.mainClass="%s.Main"
        ```
        
        或者运行打包后�?JAR�?
        
        ```bash
        java -jar target/%s-%s.jar
        ```
        
        ## 项目结构
        
        ```
        %s/
        ├── src/
        �?  ├── main/
        �?  �?  ├── java/
        �?  �?  �?  └── %s/
        �?  �?  �?      └── Main.java
        �?  �?  └── resources/
        �?  �?      └── application.yml
        �?  └── test/
        �?      └── java/
        ├── pom.xml
        └── README.md
        ```
        
        ## 配置
        
        应用配置位于 `src/main/resources/application.yml`�?
        
        ## 技术栈
        
        - EST Framework %s
        - Java %s
        
        ## 许可�?
        
        本项目使�?EST Framework�?
        """,
            config.getArtifactId(),
            projectType.getChineseDescription(),
            String.valueOf(config.getJavaVersion()),
            config.getPackageName(),
            config.getArtifactId(),
            config.getVersion(),
            config.getArtifactId(),
            config.getPackageName().replace(".", "/"),
            "2.1.0",
            config.getJavaVersion()
        );
    }
    
    public static Map<String, String> generateAllConfigs(ProjectConfig config, ProjectType projectType) {
        Map<String, String> configs = new HashMap<>();
        configs.put("pom.xml", generatePomXml(config, projectType));
        configs.put("src/main/resources/application.yml", generateApplicationYml(config, projectType));
        configs.put(".gitignore", generateGitignore());
        configs.put("README.md", generateReadme(config, projectType));
        return configs;
    }
}
