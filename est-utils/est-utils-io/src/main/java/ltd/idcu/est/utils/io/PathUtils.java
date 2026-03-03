package ltd.idcu.est.utils.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class PathUtils {

    private PathUtils() {
    }

    public static Path createPath(String first, String... more) {
        return Paths.get(first, more);
    }

    public static Path createPath(File file) {
        return file == null ? null : file.toPath();
    }

    public static Path createPath(URI uri) {
        return Paths.get(uri);
    }

    public static Path getCurrentDirectory() {
        return Paths.get("").toAbsolutePath();
    }

    public static Path getUserHomeDirectory() {
        return Paths.get(System.getProperty("user.home"));
    }

    public static Path getTempDirectory() {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }

    public static Path getWorkingDirectory() {
        return Paths.get(System.getProperty("user.dir"));
    }

    public static boolean exists(Path path) {
        return path != null && Files.exists(path);
    }

    public static boolean notExists(Path path) {
        return path != null && Files.notExists(path);
    }

    public static boolean isDirectory(Path path) {
        return path != null && Files.isDirectory(path);
    }

    public static boolean isRegularFile(Path path) {
        return path != null && Files.isRegularFile(path);
    }

    public static boolean isSymbolicLink(Path path) {
        return path != null && Files.isSymbolicLink(path);
    }

    public static boolean isHidden(Path path) throws IOException {
        return path != null && Files.isHidden(path);
    }

    public static boolean isReadable(Path path) {
        return path != null && Files.isReadable(path);
    }

    public static boolean isWritable(Path path) {
        return path != null && Files.isWritable(path);
    }

    public static boolean isExecutable(Path path) {
        return path != null && Files.isExecutable(path);
    }

    public static Path createDirectory(Path dir) throws IOException {
        return Files.createDirectory(dir);
    }

    public static Path createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        return Files.createDirectory(dir, attrs);
    }

    public static Path createDirectories(Path dir) throws IOException {
        return Files.createDirectories(dir);
    }

    public static Path createDirectories(Path dir, FileAttribute<?>... attrs) throws IOException {
        return Files.createDirectories(dir, attrs);
    }

    public static Path createFile(Path path) throws IOException {
        return Files.createFile(path);
    }

    public static Path createFile(Path path, FileAttribute<?>... attrs) throws IOException {
        return Files.createFile(path, attrs);
    }

    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix);
    }

    public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return Files.createTempFile(prefix, suffix, attrs);
    }

    public static Path createTempFile(Path dir, String prefix, String suffix) throws IOException {
        return Files.createTempFile(dir, prefix, suffix);
    }

    public static Path createTempFile(Path dir, String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return Files.createTempFile(dir, prefix, suffix, attrs);
    }

    public static Path createTempDirectory(String prefix) throws IOException {
        return Files.createTempDirectory(prefix);
    }

    public static Path createTempDirectory(String prefix, FileAttribute<?>... attrs) throws IOException {
        return Files.createTempDirectory(prefix, attrs);
    }

    public static Path createTempDirectory(Path dir, String prefix) throws IOException {
        return Files.createTempDirectory(dir, prefix);
    }

    public static Path createTempDirectory(Path dir, String prefix, FileAttribute<?>... attrs) throws IOException {
        return Files.createTempDirectory(dir, prefix, attrs);
    }

    public static void delete(Path path) throws IOException {
        Files.delete(path);
    }

    public static boolean deleteIfExists(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public static void deleteDirectoryContents(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            return;
        }
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    public static void deleteRecursively(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        if (Files.isDirectory(path)) {
            deleteDirectoryContents(path);
        }
        Files.delete(path);
    }

    public static Path copy(Path source, Path target, CopyOption... options) throws IOException {
        return Files.copy(source, target, options);
    }

    public static Path copy(Path source, Path target, boolean replaceExisting) throws IOException {
        if (replaceExisting) {
            return Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return Files.copy(source, target);
    }

    public static Path move(Path source, Path target, CopyOption... options) throws IOException {
        return Files.move(source, target, options);
    }

    public static Path move(Path source, Path target, boolean replaceExisting) throws IOException {
        if (replaceExisting) {
            return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return Files.move(source, target);
    }

    public static Path rename(Path source, String newName) throws IOException {
        Path target = source.resolveSibling(newName);
        return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static long size(Path path) throws IOException {
        return Files.size(path);
    }

    public static long directorySize(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            return 0;
        }
        try (Stream<Path> stream = Files.walk(directory)) {
            return stream.filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .sum();
        }
    }

    public static String getFileName(Path path) {
        return path == null ? null : path.getFileName() != null ? path.getFileName().toString() : null;
    }

    public static String getExtension(Path path) {
        if (path == null) {
            return null;
        }
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    public static String getBaseName(Path path) {
        if (path == null) {
            return null;
        }
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, dotIndex);
    }

    public static Path getParent(Path path) {
        return path == null ? null : path.getParent();
    }

    public static Path getRoot(Path path) {
        return path == null ? null : path.getRoot();
    }

    public static Path getAbsolutePath(Path path) {
        return path == null ? null : path.toAbsolutePath();
    }

    public static Path getCanonicalPath(Path path) throws IOException {
        return path == null ? null : path.toRealPath();
    }

    public static Path normalize(Path path) {
        return path == null ? null : path.normalize();
    }

    public static Path resolve(Path base, String other) {
        return base == null ? null : base.resolve(other);
    }

    public static Path resolve(Path base, Path other) {
        return base == null ? null : base.resolve(other);
    }

    public static Path resolveSibling(Path base, String other) {
        return base == null ? null : base.resolveSibling(other);
    }

    public static Path resolveSibling(Path base, Path other) {
        return base == null ? null : base.resolveSibling(other);
    }

    public static Path relativize(Path base, Path other) {
        return base == null ? null : base.relativize(other);
    }

    public static boolean startsWith(Path path, Path other) {
        return path != null && path.startsWith(other);
    }

    public static boolean startsWith(Path path, String other) {
        return path != null && path.startsWith(other);
    }

    public static boolean endsWith(Path path, Path other) {
        return path != null && path.endsWith(other);
    }

    public static boolean endsWith(Path path, String other) {
        return path != null && path.endsWith(other);
    }

    public static int getNameCount(Path path) {
        return path == null ? 0 : path.getNameCount();
    }

    public static Path getName(Path path, int index) {
        return path == null ? null : path.getName(index);
    }

    public static Path subpath(Path path, int beginIndex, int endIndex) {
        return path == null ? null : path.subpath(beginIndex, endIndex);
    }

    public static List<Path> list(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.list(directory)) {
            return stream.toList();
        }
    }

    public static List<Path> listFiles(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.list(directory)) {
            return stream.filter(Files::isRegularFile).toList();
        }
    }

    public static List<Path> listDirectories(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.list(directory)) {
            return stream.filter(Files::isDirectory).toList();
        }
    }

    public static List<Path> walk(Path start) throws IOException {
        return walk(start, Integer.MAX_VALUE);
    }

    public static List<Path> walk(Path start, int maxDepth) throws IOException {
        if (start == null || !Files.exists(start)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            return stream.toList();
        }
    }

    public static List<Path> walkFiles(Path start) throws IOException {
        return walkFiles(start, Integer.MAX_VALUE);
    }

    public static List<Path> walkFiles(Path start, int maxDepth) throws IOException {
        if (start == null || !Files.exists(start)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            return stream.filter(Files::isRegularFile).toList();
        }
    }

    public static List<Path> find(Path start, int maxDepth, BiPredicate<Path, BasicFileAttributes> matcher) throws IOException {
        if (start == null || !Files.exists(start)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.find(start, maxDepth, matcher)) {
            return stream.toList();
        }
    }

    @FunctionalInterface
    public interface BiPredicate<T, U> {
        boolean test(T t, U u);
    }

    public static Path createSymbolicLink(Path link, Path target) throws IOException {
        return Files.createSymbolicLink(link, target);
    }

    public static Path createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
        return Files.createSymbolicLink(link, target, attrs);
    }

    public static Path readSymbolicLink(Path link) throws IOException {
        return Files.readSymbolicLink(link);
    }

    public static Path setLastModifiedTime(Path path, FileTime time) throws IOException {
        return Files.setLastModifiedTime(path, time);
    }

    public static FileTime getLastModifiedTime(Path path) throws IOException {
        return Files.getLastModifiedTime(path);
    }

    public static Path setOwner(Path path, String owner) throws IOException {
        return Files.setOwner(path, path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(owner));
    }

    public static String getOwner(Path path) throws IOException {
        return Files.getOwner(path).getName();
    }

    public static Object getAttribute(Path path, String attribute) throws IOException {
        return Files.getAttribute(path, attribute);
    }

    public static Path setAttribute(Path path, String attribute, Object value) throws IOException {
        return Files.setAttribute(path, attribute, value);
    }

    public static boolean isSameFile(Path path1, Path path2) throws IOException {
        return Files.isSameFile(path1, path2);
    }

    public static boolean isAbsolute(Path path) {
        return path != null && path.isAbsolute();
    }

    public static String toString(Path path) {
        return path == null ? null : path.toString();
    }

    public static File toFile(Path path) {
        return path == null ? null : path.toFile();
    }

    public static URI toUri(Path path) {
        return path == null ? null : path.toUri();
    }

    public static String[] split(Path path) {
        if (path == null) {
            return new String[0];
        }
        String[] parts = new String[path.getNameCount()];
        for (int i = 0; i < path.getNameCount(); i++) {
            parts[i] = path.getName(i).toString();
        }
        return parts;
    }

    public static Path join(String first, String... more) {
        return Paths.get(first, more);
    }

    public static Path changeExtension(Path path, String newExtension) {
        if (path == null) {
            return null;
        }
        String baseName = getBaseName(path);
        if (baseName == null) {
            return path;
        }
        String newFileName = newExtension == null || newExtension.isEmpty()
                ? baseName
                : baseName + "." + newExtension;
        return path.resolveSibling(newFileName);
    }

    public static Path removeExtension(Path path) {
        if (path == null) {
            return null;
        }
        String baseName = getBaseName(path);
        if (baseName == null) {
            return path;
        }
        return path.resolveSibling(baseName);
    }

    public static boolean hasExtension(Path path, String extension) {
        if (path == null || extension == null) {
            return false;
        }
        String ext = getExtension(path);
        return extension.equalsIgnoreCase(ext);
    }

    public static boolean hasExtension(Path path, String... extensions) {
        if (path == null || extensions == null || extensions.length == 0) {
            return false;
        }
        String ext = getExtension(path);
        if (ext == null) {
            return false;
        }
        for (String extension : extensions) {
            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }
}
