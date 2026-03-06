package ltd.idcu.est.utils.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class FileCopyMoveUtils {

    private FileCopyMoveUtils() {
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
                FileDirectoryUtils.deleteQuietly(destFile);
                throw new IOException("Failed to delete original file after copy: " + srcFile);
            }
        }
    }

    public static void moveFileToDirectory(File srcFile, File destDir) throws IOException {
        moveFileToDirectory(srcFile, destDir, false);
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if (destDir == null) {
            throw new IllegalArgumentException("Destination directory must not be null");
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        if (!destDir.exists()) {
            if (createDestDir) {
                if (!destDir.mkdirs()) {
                    throw new IOException("Destination directory '" + destDir + "' could not be created");
                }
            } else {
                throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist");
            }
        }
        File destFile = new File(destDir, srcFile.getName());
        moveFile(srcFile, destFile);
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
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }
        if (!srcDir.renameTo(destDir)) {
            copyDirectory(srcDir, destDir, true);
            FileDirectoryUtils.deleteDirectory(srcDir);
        }
    }
}
