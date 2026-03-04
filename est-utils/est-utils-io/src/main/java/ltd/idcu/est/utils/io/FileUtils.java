package ltd.idcu.est.utils.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtils {

    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final long ONE_KB = 1024;
    private static final long ONE_MB = ONE_KB * ONE_KB;
    private static final long ONE_GB = ONE_KB * ONE_MB;
    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};

    private FileUtils() {
    }

    public static File getFile(String... names) {
        if (names == null || names.length == 0) {
            return null;
        }
        File file = new File(names[0]);
        for (int i = 1; i < names.length; i++) {
            file = new File(file, names[i]);
        }
        return file;
    }

    public static File getFile(File directory, String... names) {
        if (directory == null) {
            return getFile(names);
        }
        if (names == null || names.length == 0) {
            return directory;
        }
        File file = directory;
        for (String name : names) {
            file = new File(file, name);
        }
        return file;
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("File '" + file + "' could not be created");
            }
        }
        return new FileOutputStream(file, append);
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        try (InputStream in = openInputStream(file);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }
    }

    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, StandardCharsets.UTF_8);
    }

    public static String readFileToString(File file, Charset charset) throws IOException {
        try (InputStream in = openInputStream(file)) {
            return IOUtils.toString(in, charset);
        }
    }

    public static List<String> readLines(File file) throws IOException {
        return readLines(file, StandardCharsets.UTF_8);
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        try (InputStream in = openInputStream(file)) {
            return IOUtils.readLines(in, charset);
        }
    }

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        writeByteArrayToFile(file, data, false);
    }

    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        try (OutputStream out = openOutputStream(file, append)) {
            out.write(data);
        }
    }

    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, StandardCharsets.UTF_8);
    }

    public static void writeStringToFile(File file, String data, Charset charset) throws IOException {
        writeStringToFile(file, data, charset, false);
    }

    public static void writeStringToFile(File file, String data, Charset charset, boolean append) throws IOException {
        try (OutputStream out = openOutputStream(file, append)) {
            IOUtils.write(data, out, charset);
        }
    }

    public static void writeLines(File file, Collection<?> lines) throws IOException {
        writeLines(file, null, lines);
    }

    public static void writeLines(File file, String lineEnding, Collection<?> lines) throws IOException {
        writeLines(file, lineEnding, lines, false);
    }

    public static void writeLines(File file, String lineEnding, Collection<?> lines, boolean append) throws IOException {
        try (OutputStream out = openOutputStream(file, append)) {
            IOUtils.writeLines(lines, lineEnding, out);
        }
    }

    public static void write(File file, CharSequence data) throws IOException {
        write(file, data, StandardCharsets.UTF_8);
    }

    public static void write(File file, CharSequence data, Charset charset) throws IOException {
        write(file, data, charset, false);
    }

    public static void write(File file, CharSequence data, Charset charset, boolean append) throws IOException {
        writeStringToFile(file, data.toString(), charset, append);
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

    public static boolean isReadable(File file) {
        return file != null && file.canRead();
    }

    public static boolean isWritable(File file) {
        return file != null && file.canWrite();
    }

    public static boolean isHidden(File file) {
        return file != null && file.isHidden();
    }

    public static long sizeOf(File file) {
        if (file == null) {
            return 0;
        }
        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        }
        return file.length();
    }

    public static long sizeOfDirectory(File directory) {
        if (directory == null) {
            return 0;
        }
        if (!directory.exists()) {
            return 0;
        }
        if (!directory.isDirectory()) {
            return 0;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        long size = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                size += sizeOfDirectory(file);
            } else {
                size += file.length();
            }
        }
        return size;
    }

    public static int countFiles(File directory) {
        if (directory == null || !directory.isDirectory()) {
            return 0;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        int count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                count += countFiles(file);
            } else {
                count++;
            }
        }
        return count;
    }

    public static int countDirectories(File directory) {
        if (directory == null || !directory.isDirectory()) {
            return 0;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        int count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                count += countDirectories(file) + 1;
            }
        }
        return count;
    }

    public static String byteCountToDisplaySize(long size) {
        if (size < 0) {
            return "0 B";
        }
        int unitIndex = 0;
        double displaySize = size;
        while (displaySize >= 1024 && unitIndex < SIZE_UNITS.length - 1) {
            displaySize /= 1024;
            unitIndex++;
        }
        if (unitIndex == 0) {
            return String.format("%d %s", (long) displaySize, SIZE_UNITS[unitIndex]);
        }
        return String.format("%.2f %s", displaySize, SIZE_UNITS[unitIndex]);
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDot + 1);
    }

    public static String getExtension(File file) {
        return file == null ? null : getExtension(file.getName());
    }

    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return filename;
        }
        return filename.substring(0, lastDot);
    }

    public static String removeExtension(File file) {
        return file == null ? null : removeExtension(file.getName());
    }

    public static String getBaseName(String filename) {
        return removeExtension(filename);
    }

    public static String getBaseName(File file) {
        return file == null ? null : getBaseName(file.getName());
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int lastSeparator = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
        if (lastSeparator == -1) {
            return filename;
        }
        return filename.substring(lastSeparator + 1);
    }

    public static String getName(File file) {
        return file == null ? null : file.getName();
    }

    public static File getParent(File file) {
        return file == null ? null : file.getParentFile();
    }

    public static String getParentPath(File file) {
        return file == null ? null : file.getParent();
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

    public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
        return File.createTempFile(prefix, suffix, directory);
    }

    public static File createTempDirectory() throws IOException {
        return createTempDirectory("est-temp");
    }

    public static File createTempDirectory(String prefix) throws IOException {
        Path tempDir = Files.createTempDirectory(prefix);
        return tempDir.toFile();
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, false);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new IllegalArgumentException("Source file must not be null");
        }
        if (destFile == null) {
            throw new IllegalArgumentException("Destination file must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("Destination directory '" + parentFile + "' could not be created");
        }
        Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, false);
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new IllegalArgumentException("Destination directory must not be null");
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        File destFile = new File(destDir, srcFile.getName());
        copyFile(srcFile, destFile, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, false);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new IllegalArgumentException("Source directory must not be null");
        }
        if (destDir == null) {
            throw new IllegalArgumentException("Destination directory must not be null");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' is not a directory");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }
        doCopyDirectory(srcDir, destDir, preserveFileDate);
    }

    private static void doCopyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        File[] files = srcDir.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else {
            if (!destDir.mkdirs()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }
        }
        for (File file : files) {
            File destFile = new File(destDir, file.getName());
            if (file.isDirectory()) {
                doCopyDirectory(file, destFile, preserveFileDate);
            } else {
                copyFile(file, destFile, preserveFileDate);
            }
        }
        if (preserveFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new IllegalArgumentException("Source file must not be null");
        }
        if (destFile == null) {
            throw new IllegalArgumentException("Destination file must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        if (!srcFile.renameTo(destFile)) {
            copyFile(srcFile, destFile, true);
            if (!srcFile.delete()) {
                deleteQuietly(destFile);
                throw new IOException("Failed to delete original file after copy: " + srcFile);
            }
        }
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if (srcFile == null) {
            throw new IllegalArgumentException("Source file must not be null");
        }
        if (destDir == null) {
            throw new IllegalArgumentException("Destination directory must not be null");
        }
        if (!destDir.exists() && createDestDir) {
            forceMkdir(destDir);
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist");
        }
        if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
        moveFile(srcFile, new File(destDir, srcFile.getName()));
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new IllegalArgumentException("Source directory must not be null");
        }
        if (destDir == null) {
            throw new IllegalArgumentException("Destination directory must not be null");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' is not a directory");
        }
        if (destDir.exists()) {
            throw new IOException("Destination '" + destDir + "' already exists");
        }
        if (!srcDir.renameTo(destDir)) {
            copyDirectory(srcDir, destDir, true);
            deleteDirectory(srcDir);
        }
    }

    public static void touch(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (!file.exists()) {
            forceMkdirParent(file);
            try (OutputStream out = new FileOutputStream(file)) {
            }
        }
        boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    public static FileTime getLastModifiedTime(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        return Files.getLastModifiedTime(file.toPath());
    }

    public static FileTime getCreationTime(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        return (FileTime) Files.getAttribute(file.toPath(), "creationTime");
    }

    public static FileTime getLastAccessTime(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        return (FileTime) Files.getAttribute(file.toPath(), "lastAccessTime");
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
        String ext = getExtension(file);
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

    public static String getCanonicalPath(File file) throws IOException {
        return file == null ? null : file.getCanonicalPath();
    }

    public static String getAbsolutePath(File file) {
        return file == null ? null : file.getAbsolutePath();
    }

    public static String getPath(File file) {
        return file == null ? null : file.getPath();
    }

    public static void setReadOnly(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (!file.setReadOnly()) {
            throw new IOException("Unable to set read-only on " + file);
        }
    }

    public static void setWritable(File file, boolean writable) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (!file.setWritable(writable)) {
            throw new IOException("Unable to set writable=" + writable + " on " + file);
        }
    }

    public static void setReadable(File file, boolean readable) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (!file.setReadable(readable)) {
            throw new IOException("Unable to set readable=" + readable + " on " + file);
        }
    }

    public static void setExecutable(File file, boolean executable) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (!file.setExecutable(executable)) {
            throw new IOException("Unable to set executable=" + executable + " on " + file);
        }
    }
}
