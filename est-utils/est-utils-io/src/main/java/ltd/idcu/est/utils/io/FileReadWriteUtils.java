package ltd.idcu.est.utils.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public final class FileReadWriteUtils {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private FileReadWriteUtils() {
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
}
