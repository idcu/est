package ltd.idcu.est.ide.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ESTProjectScanner {
    private final File projectRoot;

    public ESTProjectScanner(File projectRoot) {
        this.projectRoot = projectRoot;
    }

    public ESTProjectScanner(String projectRootPath) {
        this(new File(projectRootPath));
    }

    public boolean isESTProject() {
        File pomXml = new File(projectRoot, "pom.xml");
        if (!pomXml.exists()) {
            return false;
        }

        try {
            String content = new String(java.nio.file.Files.readAllBytes(pomXml.toPath()));
            return content.contains("<groupId>ltd.idcu</groupId>") || 
                   content.contains("<artifactId>est-");
        } catch (Exception e) {
            return false;
        }
    }

    public List<File> findESTModules() {
        List<File> modules = new ArrayList<>();
        findESTModulesRecursive(projectRoot, modules);
        return modules;
    }

    private void findESTModulesRecursive(File directory, List<File> modules) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                File pomXml = new File(file, "pom.xml");
                if (pomXml.exists()) {
                    try {
                        String content = new String(java.nio.file.Files.readAllBytes(pomXml.toPath()));
                        if (content.contains("<artifactId>est-")) {
                            modules.add(file);
                        }
                    } catch (Exception e) {
                    }
                }
                findESTModulesRecursive(file, modules);
            }
        }
    }

    public List<File> findJavaSourceFiles() {
        List<File> javaFiles = new ArrayList<>();
        findJavaSourceFilesRecursive(projectRoot, javaFiles);
        return javaFiles;
    }

    private void findJavaSourceFilesRecursive(File directory, List<File> javaFiles) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                findJavaSourceFilesRecursive(file, javaFiles);
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
    }

    public File findPomXml() {
        return new File(projectRoot, "pom.xml");
    }

    public File findReadme() {
        File readme = new File(projectRoot, "README.md");
        if (readme.exists()) {
            return readme;
        }
        readme = new File(projectRoot, "README.txt");
        if (readme.exists()) {
            return readme;
        }
        return null;
    }

    public String getProjectName() {
        File pomXml = findPomXml();
        if (pomXml.exists()) {
            try {
                String content = new String(java.nio.file.Files.readAllBytes(pomXml.toPath()));
                int nameStart = content.indexOf("<name>");
                int nameEnd = content.indexOf("</name>");
                if (nameStart != -1 && nameEnd != -1) {
                    return content.substring(nameStart + 6, nameEnd).trim();
                }
            } catch (Exception e) {
            }
        }
        return projectRoot.getName();
    }
}
