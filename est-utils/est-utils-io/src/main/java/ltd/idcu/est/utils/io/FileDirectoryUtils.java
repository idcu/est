package ltd.idcu.est.utils.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class FileDirectoryUtils {

    private FileDirectoryUtils() {
    }

    public static void forceMkdir(File directory) throws IOException {
        if (directory == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new IOException("File '" + directory + "' exists and is not a directory");
            }
        } else {
            if (!directory.mkdirs()) {
                if (!directory.exists()) {
                    throw new IOException("Failed to create directory '" + directory + "'");
                }
            }
        }
    }

    public static void forceMkdirParent(File file) throws IOException {
        if (file == null) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            forceMkdir(parent);
        }
    }

    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception ignored) {
        }
        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                throw new IOException("Unable to delete file: " + file);
            }
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (directory == null) {
            return;
        }
        if (!directory.exists()) {
            return;
        }
        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }
        if (!directory.delete()) {
            throw new IOException("Unable to delete directory: " + directory);
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (directory == null) {
            return;
        }
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }
        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException e) {
                if (exception == null) {
                    exception = e;
                } else {
                    exception.addSuppressed(e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    public static List<File> listFiles(File directory) {
        return listFiles(directory, null, false);
    }

    public static List<File> listFiles(File directory, String[] extensions, boolean recursive) {
        List<File> files = new ArrayList<>();
        if (directory == null || !directory.isDirectory()) {
            return files;
        }
        if (recursive) {
            try (Stream<Path> stream = Files.walk(directory.toPath())) {
                stream.filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .filter(f -> matchesExtensions(f, extensions))
                        .forEach(files::add);
            } catch (IOException ignored) {
            }
        } else {
            File[] children = directory.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isFile() && matchesExtensions(child, extensions)) {
                        files.add(child);
                    }
                }
            }
        }
        return files;
    }

    private static boolean matchesExtensions(File file, String[] extensions) {
        if (extensions == null || extensions.length == 0) {
            return true;
        }
        String ext = FilePathUtils.getExtension(file);
        for (String extension : extensions) {
            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    public static List<File> listDirectories(File directory) {
        List<File> directories = new ArrayList<>();
        if (directory == null || !directory.isDirectory()) {
            return directories;
        }
        File[] children = directory.listFiles(File::isDirectory);
        if (children != null) {
            directories.addAll(List.of(children));
        }
        return directories;
    }

    public static boolean isSymlink(File file) {
        if (file == null) {
            return false;
        }
        return Files.isSymbolicLink(file.toPath());
    }

    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    public static boolean exists(File file) {
        return file != null && file.exists();
    }
}
