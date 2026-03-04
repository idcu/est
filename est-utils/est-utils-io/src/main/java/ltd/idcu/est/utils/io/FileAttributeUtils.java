package ltd.idcu.est.utils.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;

public final class FileAttributeUtils {

    private static final long ONE_KB = 1024;
    private static final long ONE_MB = ONE_KB * ONE_KB;
    private static final long ONE_GB = ONE_KB * ONE_MB;
    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};

    private FileAttributeUtils() {
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

    public static void touch(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        if (!file.exists()) {
            FileDirectoryUtils.forceMkdirParent(file);
            try (java.io.OutputStream out = new java.io.FileOutputStream(file)) {
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
