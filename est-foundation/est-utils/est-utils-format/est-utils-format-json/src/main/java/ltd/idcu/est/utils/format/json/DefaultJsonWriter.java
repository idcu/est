package ltd.idcu.est.utils.format.json;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DefaultJsonWriter implements JsonWriter {
    
    private final Writer writer;
    private boolean closed;
    private final int[] stack;
    private int stackSize;
    private String separator;
    private boolean indentEnabled;
    private String indent;
    private int indentLevel;
    private final char[] buffer;
    private int bufferPos;
    
    private static final int BUFFER_SIZE = 8192;
    
    public DefaultJsonWriter(Writer writer) {
        this.writer = writer;
        this.closed = false;
        this.stack = new int[32];
        this.stackSize = 0;
        this.separator = "";
        this.indentEnabled = false;
        this.indent = "  ";
        this.indentLevel = 0;
        this.buffer = new char[BUFFER_SIZE];
        this.bufferPos = 0;
    }
    
    public DefaultJsonWriter setIndent(String indent) {
        this.indent = indent;
        this.indentEnabled = indent != null && !indent.isEmpty();
        return this;
    }
    
    public DefaultJsonWriter setIndentEnabled(boolean enabled) {
        this.indentEnabled = enabled;
        return this;
    }
    
    private void writeSeparator() throws IOException {
        if (separator != null && !separator.isEmpty()) {
            writeInternal(separator);
        }
        if (indentEnabled) {
            writeInternal('\n');
            for (int i = 0; i < indentLevel; i++) {
                writeInternal(indent);
            }
        }
        separator = ",";
    }
    
    private void writeInternal(char c) throws IOException {
        if (bufferPos >= buffer.length) {
            flushBuffer();
        }
        buffer[bufferPos++] = c;
    }
    
    private void writeInternal(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (bufferPos >= buffer.length) {
                flushBuffer();
            }
            buffer[bufferPos++] = s.charAt(i);
        }
    }
    
    private void flushBuffer() throws IOException {
        if (bufferPos > 0) {
            writer.write(buffer, 0, bufferPos);
            bufferPos = 0;
        }
    }
    
    private void push(int state) {
        if (stackSize == stack.length) {
            int[] newStack = new int[stack.length * 2];
            System.arraycopy(stack, 0, newStack, 0, stack.length);
        }
        stack[stackSize++] = state;
    }
    
    private int peek() {
        return stackSize > 0 ? stack[stackSize - 1] : 0;
    }
    
    private int pop() {
        return stack[--stackSize];
    }
    
    @Override
    public JsonWriter writeStartObject() throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeInternal('{');
        push(1);
        separator = "";
        indentLevel++;
        return this;
    }
    
    @Override
    public JsonWriter writeEndObject() throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (peek() != 1) {
            throw new IOException("Nesting problem");
        }
        pop();
        indentLevel--;
        if (indentEnabled) {
            writeInternal('\n');
            for (int i = 0; i < indentLevel; i++) {
                writeInternal(indent);
            }
        }
        writeInternal('}');
        separator = ",";
        return this;
    }
    
    @Override
    public JsonWriter writeStartArray() throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeInternal('[');
        push(2);
        separator = "";
        indentLevel++;
        return this;
    }
    
    @Override
    public JsonWriter writeEndArray() throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (peek() != 2) {
            throw new IOException("Nesting problem");
        }
        pop();
        indentLevel--;
        if (indentEnabled) {
            writeInternal('\n');
            for (int i = 0; i < indentLevel; i++) {
                writeInternal(indent);
            }
        }
        writeInternal(']');
        separator = ",";
        return this;
    }
    
    @Override
    public JsonWriter writeName(String name) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeStringInternal(name);
        writeInternal(':');
        if (indentEnabled) {
            writeInternal(' ');
        }
        separator = "";
        return this;
    }
    
    @Override
    public JsonWriter writeString(String value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (value == null) {
            return writeNull();
        }
        writeSeparator();
        writeStringInternal(value);
        return this;
    }
    
    private void writeStringInternal(String value) throws IOException {
        writeInternal('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> writeInternal("\\\"");
                case '\\' -> writeInternal("\\\\");
                case '\b' -> writeInternal("\\b");
                case '\f' -> writeInternal("\\f");
                case '\n' -> writeInternal("\\n");
                case '\r' -> writeInternal("\\r");
                case '\t' -> writeInternal("\\t");
                default -> {
                    if (c < ' ') {
                        writeInternal("\\u");
                        String hex = Integer.toHexString(c);
                        for (int j = hex.length(); j < 4; j++) {
                            writeInternal('0');
                        }
                        writeInternal(hex);
                    } else {
                        writeInternal(c);
                    }
                }
            }
        }
        writeInternal('"');
    }
    
    @Override
    public JsonWriter writeInt(int value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeInternal(Integer.toString(value));
        return this;
    }
    
    @Override
    public JsonWriter writeLong(long value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeInternal(Long.toString(value));
        return this;
    }
    
    @Override
    public JsonWriter writeDouble(double value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Numeric values must be finite");
        }
        writeSeparator();
        writeInternal(Double.toString(value));
        return this;
    }
    
    @Override
    public JsonWriter writeBigDecimal(BigDecimal value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (value == null) {
            return writeNull();
        }
        writeSeparator();
        writeInternal(value.toString());
        return this;
    }
    
    @Override
    public JsonWriter writeBigInteger(BigInteger value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (value == null) {
            return writeNull();
        }
        writeSeparator();
        writeInternal(value.toString());
        return this;
    }
    
    @Override
    public JsonWriter writeBoolean(boolean value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeInternal(value ? "true" : "false");
        return this;
    }
    
    @Override
    public JsonWriter writeNull() throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        writeSeparator();
        writeInternal("null");
        return this;
    }
    
    @Override
    public JsonWriter writeRawValue(String value) throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        if (value == null) {
            return writeNull();
        }
        writeSeparator();
        writeInternal(value);
        return this;
    }
    
    @Override
    public JsonWriter flush() throws IOException {
        if (closed) {
            throw new IOException("JsonWriter is closed");
        }
        flushBuffer();
        writer.flush();
        return this;
    }
    
    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            while (stackSize > 0) {
                int state = pop();
                if (state == 1) {
                    writeInternal('}');
                } else if (state == 2) {
                    writeInternal(']');
                }
            }
            flushBuffer();
            writer.close();
        }
    }
}
