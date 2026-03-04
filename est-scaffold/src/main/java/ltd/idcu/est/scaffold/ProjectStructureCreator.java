package ltd.idcu.est.scaffold;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ProjectStructureCreator {

    private ProjectStructureCreator() {
    }

    public static void createBasicProjectStructure(Path basePath) throws IOException {
        Files.createDirectories(basePath.resolve("src/main/java"));
        Files.createDirectories(basePath.resolve("src/test/java"));
        Files.createDirectories(basePath.resolve("src/main/resources"));
        Files.createDirectories(basePath.resolve("src/test/resources"));
    }

    public static void createWebProjectStructure(Path basePath) throws IOException {
        createBasicProjectStructure(basePath);
        Files.createDirectories(basePath.resolve("src/main/resources/static"));
        Files.createDirectories(basePath.resolve("src/main/resources/templates"));
    }

    public static void createApiProjectStructure(Path basePath) throws IOException {
        createBasicProjectStructure(basePath);
    }
}
