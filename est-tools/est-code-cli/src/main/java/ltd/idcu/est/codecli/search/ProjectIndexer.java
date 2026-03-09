package ltd.idcu.est.codecli.search;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.HashSet;

public class ProjectIndexer {
    
    private final FileIndex fileIndex;
    private final Set<String> excludedDirs;
    private final Set<String> includedExtensions;
    
    public ProjectIndexer(FileIndex fileIndex) {
        this.fileIndex = fileIndex;
        this.excludedDirs = new HashSet<>();
        this.includedExtensions = new HashSet<>();
        
        excludedDirs.add(".git");
        excludedDirs.add("target");
        excludedDirs.add("build");
        excludedDirs.add("node_modules");
        excludedDirs.add(".idea");
        excludedDirs.add(".vscode");
        
        includedExtensions.add(".java");
        includedExtensions.add(".xml");
        includedExtensions.add(".yml");
        includedExtensions.add(".yaml");
        includedExtensions.add(".json");
        includedExtensions.add(".md");
        includedExtensions.add(".txt");
        includedExtensions.add(".properties");
    }
    
    public void indexProject(String rootPath) throws IOException {
        Path root = Paths.get(rootPath);
        if (!Files.exists(root)) {
            throw new IOException("Path does not exist: " + rootPath);
        }
        
        fileIndex.clear();
        
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                String dirName = dir.getFileName().toString();
                if (excludedDirs.contains(dirName)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String fileName = file.toString();
                if (shouldIndexFile(fileName)) {
                    try {
                        String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                        fileIndex.indexFile(fileName, content);
                    } catch (IOException e) {
                        System.err.println("Warning: Could not read file " + file + ": " + e.getMessage());
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    public void addExcludedDir(String dirName) {
        excludedDirs.add(dirName);
    }
    
    public void removeExcludedDir(String dirName) {
        excludedDirs.remove(dirName);
    }
    
    public void addIncludedExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        includedExtensions.add(extension);
    }
    
    public void removeIncludedExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        includedExtensions.remove(extension);
    }
    
    private boolean shouldIndexFile(String fileName) {
        for (String ext : includedExtensions) {
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
    
    public Set<String> getExcludedDirs() {
        return new HashSet<>(excludedDirs);
    }
    
    public Set<String> getIncludedExtensions() {
        return new HashSet<>(includedExtensions);
    }
}
