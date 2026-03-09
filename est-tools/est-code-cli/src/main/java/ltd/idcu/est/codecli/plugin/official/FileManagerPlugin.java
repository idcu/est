package ltd.idcu.est.codecli.plugin.official;

import ltd.idcu.est.codecli.plugin.BaseEstPlugin;
import ltd.idcu.est.codecli.plugin.PluginException;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManagerPlugin extends BaseEstPlugin {

    private static final String PLUGIN_ID = "file-manager-plugin";
    private static final String PLUGIN_NAME = "File Manager Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "文件管理插件，提供文件浏览、搜索、复制、移动、删除、压缩等功能";
    private static final String PLUGIN_AUTHOR = "EST Team";

    private Path currentDirectory;
    private Set<String> hiddenExtensions;

    public FileManagerPlugin() {
        this.hiddenExtensions = new HashSet<>(Arrays.asList(".class", ".log", ".tmp", ".bak"));
        
        addCapability("features", Map.of(
            "browse", true,
            "search", true,
            "copy", true,
            "move", true,
            "delete", true,
            "compress", true
        ));
        addCapability("commands", new String[]{"fm_ls", "fm_cd", "fm_find", "fm_cp", "fm_mv", "fm_rm", "fm_zip"});
    }

    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public String getDescription() {
        return PLUGIN_DESCRIPTION;
    }

    @Override
    public String getAuthor() {
        return PLUGIN_AUTHOR;
    }

    @Override
    protected void onInitialize() throws PluginException {
        this.currentDirectory = context.getWorkDir();
        logInfo("文件管理插件初始化完成");
        logInfo("当前目录: " + currentDirectory);
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }

    public void changeDirectory(Path path) throws PluginException {
        Path newDir = currentDirectory.resolve(path).normalize();
        if (!Files.exists(newDir)) {
            throw new PluginException("目录不存在: " + newDir);
        }
        if (!Files.isDirectory(newDir)) {
            throw new PluginException("不是目录: " + newDir);
        }
        this.currentDirectory = newDir;
        logInfo("切换目录到: " + currentDirectory);
    }

    public List<FileInfo> listFiles() throws PluginException {
        return listFiles(false);
    }

    public List<FileInfo> listFiles(boolean showHidden) throws PluginException {
        List<FileInfo> files = new ArrayList<>();
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
            for (Path path : stream) {
                if (!showHidden && isHiddenFile(path)) {
                    continue;
                }
                files.add(new FileInfo(path));
            }
        } catch (IOException e) {
            throw new PluginException("列出文件失败: " + e.getMessage(), e);
        }
        
        files.sort(Comparator.comparing(f -> !f.isDirectory).thenComparing(f -> f.name.toLowerCase()));
        logInfo("列出 " + files.size() + " 个文件/目录");
        return files;
    }

    private boolean isHiddenFile(Path path) {
        try {
            String fileName = path.getFileName().toString();
            if (fileName.startsWith(".")) {
                return true;
            }
            String ext = getExtension(fileName);
            return hiddenExtensions.contains(ext);
        } catch (Exception e) {
            return false;
        }
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex).toLowerCase() : "";
    }

    public FileInfo getFileInfo(Path path) throws PluginException {
        Path fullPath = currentDirectory.resolve(path).normalize();
        if (!Files.exists(fullPath)) {
            throw new PluginException("文件不存在: " + fullPath);
        }
        return new FileInfo(fullPath);
    }

    public List<FileInfo> searchFiles(String pattern) throws PluginException {
        return searchFiles(pattern, Integer.MAX_VALUE);
    }

    public List<FileInfo> searchFiles(String pattern, int maxResults) throws PluginException {
        List<FileInfo> results = new ArrayList<>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        
        try {
            Files.walkFileTree(currentDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    Path relativePath = currentDirectory.relativize(file);
                    if (matcher.matches(relativePath) || matcher.matches(file.getFileName())) {
                        results.add(new FileInfo(file));
                        if (results.size() >= maxResults) {
                            return FileVisitResult.TERMINATE;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.getFileName() != null && dir.getFileName().toString().startsWith(".")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new PluginException("搜索文件失败: " + e.getMessage(), e);
        }
        
        logInfo("搜索到 " + results.size() + " 个文件");
        return results;
    }

    public List<FileInfo> findByContent(String content) throws PluginException {
        return findByContent(content, Integer.MAX_VALUE);
    }

    public List<FileInfo> findByContent(String content, int maxResults) throws PluginException {
        List<FileInfo> results = new ArrayList<>();
        
        try {
            Files.walkFileTree(currentDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        if (Files.size(file) > 10 * 1024 * 1024) {
                            return FileVisitResult.CONTINUE;
                        }
                        String fileContent = Files.readString(file);
                        if (fileContent.contains(content)) {
                            results.add(new FileInfo(file));
                            if (results.size() >= maxResults) {
                                return FileVisitResult.TERMINATE;
                            }
                        }
                    } catch (IOException e) {
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.getFileName() != null && dir.getFileName().toString().startsWith(".")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new PluginException("搜索文件内容失败: " + e.getMessage(), e);
        }
        
        logInfo("在 " + results.size() + " 个文件中找到匹配内容");
        return results;
    }

    public void copyFile(Path source, Path target) throws PluginException {
        copyFile(source, target, false);
    }

    public void copyFile(Path source, Path target, boolean overwrite) throws PluginException {
        Path sourcePath = currentDirectory.resolve(source).normalize();
        Path targetPath = currentDirectory.resolve(target).normalize();
        
        if (!Files.exists(sourcePath)) {
            throw new PluginException("源文件不存在: " + sourcePath);
        }
        
        try {
            if (Files.isDirectory(sourcePath)) {
                copyDirectory(sourcePath, targetPath, overwrite);
            } else {
                CopyOption[] options = overwrite ? 
                    new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES} :
                    new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES};
                Files.copy(sourcePath, targetPath, options);
            }
            logInfo("复制成功: " + sourcePath + " -> " + targetPath);
        } catch (IOException e) {
            throw new PluginException("复制文件失败: " + e.getMessage(), e);
        }
    }

    private void copyDirectory(Path source, Path target, boolean overwrite) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = target.resolve(source.relativize(file));
                CopyOption[] options = overwrite ? 
                    new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES} :
                    new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES};
                Files.copy(file, targetFile, options);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void moveFile(Path source, Path target) throws PluginException {
        Path sourcePath = currentDirectory.resolve(source).normalize();
        Path targetPath = currentDirectory.resolve(target).normalize();
        
        if (!Files.exists(sourcePath)) {
            throw new PluginException("源文件不存在: " + sourcePath);
        }
        
        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logInfo("移动成功: " + sourcePath + " -> " + targetPath);
        } catch (IOException e) {
            throw new PluginException("移动文件失败: " + e.getMessage(), e);
        }
    }

    public void deleteFile(Path path) throws PluginException {
        deleteFile(path, false);
    }

    public void deleteFile(Path path, boolean recursive) throws PluginException {
        Path targetPath = currentDirectory.resolve(path).normalize();
        
        if (!Files.exists(targetPath)) {
            throw new PluginException("文件不存在: " + targetPath);
        }
        
        try {
            if (recursive && Files.isDirectory(targetPath)) {
                Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                Files.delete(targetPath);
            }
            logInfo("删除成功: " + targetPath);
        } catch (IOException e) {
            throw new PluginException("删除文件失败: " + e.getMessage(), e);
        }
    }

    public long getDirectorySize(Path path) throws PluginException {
        Path dirPath = currentDirectory.resolve(path).normalize();
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new PluginException("不是有效目录: " + dirPath);
        }
        
        try (Stream<Path> stream = Files.walk(dirPath)) {
            return stream
                .filter(Files::isRegularFile)
                .mapToLong(p -> {
                    try {
                        return Files.size(p);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
        } catch (IOException e) {
            throw new PluginException("计算目录大小失败: " + e.getMessage(), e);
        }
    }

    public void addHiddenExtension(String ext) {
        if (!ext.startsWith(".")) {
            ext = "." + ext;
        }
        hiddenExtensions.add(ext.toLowerCase());
        logInfo("添加隐藏扩展名: " + ext);
    }

    public void removeHiddenExtension(String ext) {
        if (!ext.startsWith(".")) {
            ext = "." + ext;
        }
        hiddenExtensions.remove(ext.toLowerCase());
        logInfo("移除隐藏扩展名: " + ext);
    }

    public static class FileInfo {
        public final Path path;
        public final String name;
        public final boolean isDirectory;
        public final long size;
        public final long lastModified;
        public final boolean isHidden;
        public final boolean isReadable;
        public final boolean isWritable;

        public FileInfo(Path path) {
            this.path = path;
            this.name = path.getFileName() != null ? path.getFileName().toString() : path.toString();
            this.isDirectory = Files.isDirectory(path);
            try {
                this.size = Files.size(path);
                this.lastModified = Files.getLastModifiedTime(path).toMillis();
                this.isHidden = Files.isHidden(path);
                this.isReadable = Files.isReadable(path);
                this.isWritable = Files.isWritable(path);
            } catch (IOException e) {
                this.size = 0;
                this.lastModified = 0;
                this.isHidden = false;
                this.isReadable = false;
                this.isWritable = false;
            }
        }

        public String getFormattedSize() {
            if (isDirectory) {
                return "<DIR>";
            }
            if (size < 1024) {
                return size + " B";
            } else if (size < 1024 * 1024) {
                return String.format("%.1f KB", size / 1024.0);
            } else if (size < 1024 * 1024 * 1024) {
                return String.format("%.1f MB", size / (1024.0 * 1024));
            } else {
                return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
            }
        }
    }
}
