package ltd.idcu.est.utils.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileTempUtils {

    private FileTempUtils() {
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
}
