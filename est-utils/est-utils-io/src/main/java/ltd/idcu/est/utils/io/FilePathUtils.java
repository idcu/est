package ltd.idcu.est.utils.io;

import java.io.File;
import java.io.IOException;

public final class FilePathUtils {

    private FilePathUtils() {
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

    public static String getCanonicalPath(File file) throws IOException {
        return file == null ? null : file.getCanonicalPath();
    }

    public static String getAbsolutePath(File file) {
        return file == null ? null : file.getAbsolutePath();
    }

    public static String getPath(File file) {
        return file == null ? null : file.getPath();
    }
}
