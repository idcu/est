package ltd.idcu.est.scaffold;

import java.io.IOException;
import java.nio.file.Path;

public final class PomXmlGenerator {

    private PomXmlGenerator() {
    }

    public static void createBasicPomXml(Path basePath, String projectName) throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>" + projectName + "</artifactId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "\n" +
                "    <name>" + projectName + "</name>\n" +
                "    <description>EST Framework Project</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <maven.compiler.source>21</maven.compiler.source>\n" +
                "        <maven.compiler.target>21</maven.compiler.target>\n" +
                "        <maven.compiler.release>21</maven.compiler.release>\n" +
                "        <est.version>1.3.0-SNAPSHOT</est.version>\n" +
                "    </properties>\n" +
                "\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-core-impl</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.11.0</version>\n" +
                "                <configuration>\n" +
                "                    <source>21</source>\n" +
                "                    <target>21</target>\n" +
                "                    <release>21</release>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>\n";
        
        Path pomPath = basePath.resolve("pom.xml");
        FileWriterUtil.writeFile(pomPath, content);
    }

    public static void createWebPomXml(Path basePath, String projectName) throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>" + projectName + "</artifactId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "\n" +
                "    <name>" + projectName + "</name>\n" +
                "    <description>EST Web Application</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <maven.compiler.source>21</maven.compiler.source>\n" +
                "        <maven.compiler.target>21</maven.compiler.target>\n" +
                "        <maven.compiler.release>21</maven.compiler.release>\n" +
                "        <est.version>1.3.0-SNAPSHOT</est.version>\n" +
                "    </properties>\n" +
                "\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-web-impl</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.11.0</version>\n" +
                "                <configuration>\n" +
                "                    <source>21</source>\n" +
                "                    <target>21</target>\n" +
                "                    <release>21</release>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>\n";
        
        Path pomPath = basePath.resolve("pom.xml");
        FileWriterUtil.writeFile(pomPath, content);
    }

    public static void createApiPomXml(Path basePath, String projectName) throws IOException {
        createWebPomXml(basePath, projectName);
    }
}
