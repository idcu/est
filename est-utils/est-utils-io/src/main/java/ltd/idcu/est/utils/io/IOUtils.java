package ltd.idcu.est.utils.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int EOF = -1;

    private IOUtils() {
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = input.read(buffer)) != EOF) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }
    }

    public static byte[] toByteArray(InputStream input, int size) throws IOException {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be >= 0: " + size);
        }
        if (size == 0) {
            return new byte[0];
        }
        byte[] data = new byte[size];
        int offset = 0;
        int read;
        while (offset < size && (read = input.read(data, offset, size - offset)) != EOF) {
            offset += read;
        }
        if (offset != size) {
            throw new IOException("Unexpected read size. expected: " + size + ", actual: " + offset);
        }
        return data;
    }

    public static char[] toCharArray(InputStream input) throws IOException {
        return toCharArray(input, StandardCharsets.UTF_8);
    }

    public static char[] toCharArray(InputStream input, Charset charset) throws IOException {
        try (Reader reader = new InputStreamReader(input, charset)) {
            return toCharArray(reader);
        }
    }

    public static char[] toCharArray(Reader reader) throws IOException {
        try (CharArrayWriter writer = new CharArrayWriter()) {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = reader.read(buffer)) != EOF) {
                writer.write(buffer, 0, n);
            }
            return writer.toCharArray();
        }
    }

    public static String toString(InputStream input) throws IOException {
        return toString(input, StandardCharsets.UTF_8);
    }

    public static String toString(InputStream input, Charset charset) throws IOException {
        try (Reader reader = new InputStreamReader(input, charset)) {
            return toString(reader);
        }
    }

    public static String toString(Reader reader) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = reader.read(buffer)) != EOF) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        }
    }

    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(input, StandardCharsets.UTF_8);
    }

    public static List<String> readLines(InputStream input, Charset charset) throws IOException {
        try (Reader reader = new InputStreamReader(input, charset)) {
            return readLines(reader);
        }
    }

    public static List<String> readLines(Reader reader) throws IOException {
        try (BufferedReader br = toBufferedReader(reader)) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(byte[] data, OutputStream output, int off, int len) throws IOException {
        if (data != null) {
            output.write(data, off, len);
        }
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(char[] data, Writer output, int off, int len) throws IOException {
        if (data != null) {
            output.write(data, off, len);
        }
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(String data, OutputStream output) throws IOException {
        write(data, output, StandardCharsets.UTF_8);
    }

    public static void write(String data, OutputStream output, Charset charset) throws IOException {
        if (data != null) {
            output.write(data.getBytes(charset));
        }
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, StandardCharsets.UTF_8);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset charset) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        for (Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes(charset));
            }
            output.write(lineEnding.getBytes(charset));
        }
    }

    public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        for (Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while ((n = input.read(buffer)) != EOF) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        long count = 0;
        int n;
        while ((n = input.read(buffer)) != EOF) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copy(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while ((n = reader.read(buffer)) != EOF) {
            writer.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copy(Reader reader, OutputStream output) throws IOException {
        return copy(reader, output, StandardCharsets.UTF_8);
    }

    public static long copy(Reader reader, OutputStream output, Charset charset) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(output, charset)) {
            return copy(reader, writer);
        }
    }

    public static long copy(InputStream input, Writer writer) throws IOException {
        return copy(input, writer, StandardCharsets.UTF_8);
    }

    public static long copy(InputStream input, Writer writer, Charset charset) throws IOException {
        try (Reader reader = new InputStreamReader(input, charset)) {
            return copy(reader, writer);
        }
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, was: " + toSkip);
        }
        long remaining = toSkip;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (remaining > 0) {
            int read = input.read(buffer, 0, (int) Math.min(remaining, DEFAULT_BUFFER_SIZE));
            if (read == EOF) {
                break;
            }
            remaining -= read;
        }
        return toSkip - remaining;
    }

    public static long skip(Reader reader, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, was: " + toSkip);
        }
        long remaining = toSkip;
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        while (remaining > 0) {
            int read = reader.read(buffer, 0, (int) Math.min(remaining, DEFAULT_BUFFER_SIZE));
            if (read == EOF) {
                break;
            }
            remaining -= read;
        }
        return toSkip - remaining;
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, was: " + toSkip);
        }
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + ", actual: " + skipped);
        }
    }

    public static void skipFully(Reader reader, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, was: " + toSkip);
        }
        long skipped = skip(reader, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + ", actual: " + skipped);
        }
    }

    public static int read(InputStream input, byte[] buffer) throws IOException {
        int remaining = buffer.length;
        int offset = 0;
        while (remaining > 0) {
            int read = input.read(buffer, offset, remaining);
            if (read == EOF) {
                break;
            }
            remaining -= read;
            offset += read;
        }
        return buffer.length - remaining;
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        int remaining = length;
        while (remaining > 0) {
            int read = input.read(buffer, offset + (length - remaining), remaining);
            if (read == EOF) {
                throw new EOFException("Unexpected end of input");
            }
            remaining -= read;
        }
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedWriter toBufferedWriter(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static BufferedInputStream toBufferedInputStream(InputStream input) {
        return input instanceof BufferedInputStream ? (BufferedInputStream) input : new BufferedInputStream(input);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream output) {
        return output instanceof BufferedOutputStream ? (BufferedOutputStream) output : new BufferedOutputStream(output);
    }

    public static InputStream toInputStream(String input) {
        return toInputStream(input, StandardCharsets.UTF_8);
    }

    public static InputStream toInputStream(String input, Charset charset) {
        return new ByteArrayInputStream(input.getBytes(charset));
    }

    public static InputStream toInputStream(CharSequence input) {
        return toInputStream(input, StandardCharsets.UTF_8);
    }

    public static InputStream toInputStream(CharSequence input, Charset charset) {
        return new ByteArrayInputStream(input.toString().getBytes(charset));
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                closeQuietly(closeable);
            }
        }
    }

    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            IOException firstException = null;
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        if (firstException == null) {
                            firstException = e;
                        } else {
                            firstException.addSuppressed(e);
                        }
                    }
                }
            }
            if (firstException != null) {
                throw firstException;
            }
        }
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        if (input1 == input2) {
            return true;
        }
        byte[] buffer1 = new byte[DEFAULT_BUFFER_SIZE];
        byte[] buffer2 = new byte[DEFAULT_BUFFER_SIZE];
        while (true) {
            int read1 = input1.read(buffer1);
            int read2 = input2.read(buffer2);
            if (read1 != read2) {
                return false;
            }
            if (read1 == EOF) {
                return true;
            }
            for (int i = 0; i < read1; i++) {
                if (buffer1[i] != buffer2[i]) {
                    return false;
                }
            }
        }
    }

    public static boolean contentEquals(Reader reader1, Reader reader2) throws IOException {
        if (reader1 == reader2) {
            return true;
        }
        char[] buffer1 = new char[DEFAULT_BUFFER_SIZE];
        char[] buffer2 = new char[DEFAULT_BUFFER_SIZE];
        while (true) {
            int read1 = reader1.read(buffer1);
            int read2 = reader2.read(buffer2);
            if (read1 != read2) {
                return false;
            }
            if (read1 == EOF) {
                return true;
            }
            for (int i = 0; i < read1; i++) {
                if (buffer1[i] != buffer2[i]) {
                    return false;
                }
            }
        }
    }

    public static boolean contentEqualsIgnoreEOL(Reader reader1, Reader reader2) throws IOException {
        if (reader1 == reader2) {
            return true;
        }
        try (BufferedReader br1 = toBufferedReader(reader1);
             BufferedReader br2 = toBufferedReader(reader2)) {
            String line1 = br1.readLine();
            String line2 = br2.readLine();
            while (line1 != null && line2 != null && line1.equals(line2)) {
                line1 = br1.readLine();
                line2 = br2.readLine();
            }
            return line1 == null && line2 == null;
        }
    }

    public static long length(InputStream input) throws IOException {
        return input.transferTo(OutputStream.nullOutputStream());
    }
}
