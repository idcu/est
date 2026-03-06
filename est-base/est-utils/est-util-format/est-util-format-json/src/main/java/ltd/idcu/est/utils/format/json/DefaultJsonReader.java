package ltd.idcu.est.utils.format.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DefaultJsonReader implements JsonReader {
    
    private final Reader reader;
    private final char[] buffer;
    private int bufferPos;
    private int bufferLen;
    private boolean closed;
    private JsonToken currentToken;
    private String currentName;
    private Object currentValue;
    private int pos;
    
    private static final int BUFFER_SIZE = 8192;
    
    private final boolean zeroCopyMode;
    private final char[] sourceChars;
    private final int sourceOffset;
    private final int sourceLength;
    
    public DefaultJsonReader(Reader reader) {
        this.reader = reader;
        this.buffer = new char[BUFFER_SIZE];
        this.bufferPos = 0;
        this.bufferLen = 0;
        this.closed = false;
        this.currentToken = null;
        this.currentName = null;
        this.currentValue = null;
        this.pos = 0;
        this.zeroCopyMode = false;
        this.sourceChars = null;
        this.sourceOffset = 0;
        this.sourceLength = 0;
    }
    
    public DefaultJsonReader(String json) {
        this(json.toCharArray(), 0, json.length());
    }
    
    public DefaultJsonReader(char[] chars, int offset, int length) {
        this.reader = null;
        this.buffer = chars;
        this.bufferPos = offset;
        this.bufferLen = offset + length;
        this.closed = false;
        this.currentToken = null;
        this.currentName = null;
        this.currentValue = null;
        this.pos = offset;
        this.zeroCopyMode = true;
        this.sourceChars = chars;
        this.sourceOffset = offset;
        this.sourceLength = length;
    }
    
    private char readChar() throws IOException {
        if (closed) {
            throw new IOException("JsonReader is closed");
        }
        if (zeroCopyMode) {
            if (bufferPos >= bufferLen) {
                throw new IOException("Unexpected end of input");
            }
            pos++;
            return buffer[bufferPos++];
        }
        if (bufferPos >= bufferLen) {
            bufferLen = reader.read(buffer);
            if (bufferLen == -1) {
                throw new IOException("Unexpected end of input");
            }
            bufferPos = 0;
        }
        pos++;
        return buffer[bufferPos++];
    }
    
    private char peekChar() throws IOException {
        if (closed) {
            throw new IOException("JsonReader is closed");
        }
        if (zeroCopyMode) {
            if (bufferPos >= bufferLen) {
                throw new IOException("Unexpected end of input");
            }
            return buffer[bufferPos];
        }
        if (bufferPos >= bufferLen) {
            bufferLen = reader.read(buffer);
            if (bufferLen == -1) {
                throw new IOException("Unexpected end of input");
            }
            bufferPos = 0;
        }
        return buffer[bufferPos];
    }
    
    private void skipWhitespace() throws IOException {
        while (true) {
            if (zeroCopyMode) {
                if (bufferPos >= bufferLen) {
                    return;
                }
            } else {
                if (bufferPos >= bufferLen) {
                    bufferLen = reader.read(buffer);
                    if (bufferLen == -1) {
                        return;
                    }
                    bufferPos = 0;
                }
            }
            char c = buffer[bufferPos];
            if (!Character.isWhitespace(c)) {
                break;
            }
            bufferPos++;
            pos++;
        }
    }
    
    @Override
    public JsonToken peek() throws IOException {
        if (currentToken != null) {
            return currentToken;
        }
        return nextToken();
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        skipWhitespace();
        if (zeroCopyMode) {
            if (bufferPos >= bufferLen) {
                currentToken = JsonToken.END_OF_INPUT;
                return currentToken;
            }
        } else {
            if (bufferPos >= bufferLen) {
                bufferLen = reader.read(buffer);
                if (bufferLen == -1) {
                    currentToken = JsonToken.END_OF_INPUT;
                    return currentToken;
                }
                bufferPos = 0;
            }
        }
        
        char c = buffer[bufferPos];
        switch (c) {
            case '{':
                bufferPos++;
                pos++;
                currentToken = JsonToken.START_OBJECT;
                break;
            case '}':
                bufferPos++;
                pos++;
                currentToken = JsonToken.END_OBJECT;
                break;
            case '[':
                bufferPos++;
                pos++;
                currentToken = JsonToken.START_ARRAY;
                break;
            case ']':
                bufferPos++;
                pos++;
                currentToken = JsonToken.END_ARRAY;
                break;
            case '"':
                currentToken = JsonToken.FIELD_NAME;
                currentValue = readString();
                break;
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                currentToken = JsonToken.VALUE_NUMBER;
                currentValue = readNumber();
                break;
            case 't':
                if (zeroCopyMode) {
                    if (bufferPos + 3 >= bufferLen) {
                        throw new IOException("Unexpected end of input");
                    }
                    if (buffer[bufferPos + 1] != 'r' || buffer[bufferPos + 2] != 'u' || buffer[bufferPos + 3] != 'e') {
                        throw new IOException("Unexpected character at position " + pos + ": " + c);
                    }
                }
                bufferPos += 4;
                pos += 4;
                currentToken = JsonToken.VALUE_TRUE;
                currentValue = Boolean.TRUE;
                break;
            case 'f':
                if (zeroCopyMode) {
                    if (bufferPos + 4 >= bufferLen) {
                        throw new IOException("Unexpected end of input");
                    }
                    if (buffer[bufferPos + 1] != 'a' || buffer[bufferPos + 2] != 'l' || buffer[bufferPos + 3] != 's' || buffer[bufferPos + 4] != 'e') {
                        throw new IOException("Unexpected character at position " + pos + ": " + c);
                    }
                }
                bufferPos += 5;
                pos += 5;
                currentToken = JsonToken.VALUE_FALSE;
                currentValue = Boolean.FALSE;
                break;
            case 'n':
                if (zeroCopyMode) {
                    if (bufferPos + 3 >= bufferLen) {
                        throw new IOException("Unexpected end of input");
                    }
                    if (buffer[bufferPos + 1] != 'u' || buffer[bufferPos + 2] != 'l' || buffer[bufferPos + 3] != 'l') {
                        throw new IOException("Unexpected character at position " + pos + ": " + c);
                    }
                }
                bufferPos += 4;
                pos += 4;
                currentToken = JsonToken.VALUE_NULL;
                currentValue = null;
                break;
            default:
                throw new IOException("Unexpected character at position " + pos + ": " + c);
        }
        return currentToken;
    }
    
    private String readString() throws IOException {
        bufferPos++;
        pos++;
        if (zeroCopyMode) {
            return readStringZeroCopy();
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (bufferPos >= bufferLen) {
                bufferLen = reader.read(buffer);
                if (bufferLen == -1) {
                    throw new IOException("Unterminated string");
                }
                bufferPos = 0;
            }
            char c = buffer[bufferPos];
            if (c == '"') {
                bufferPos++;
                pos++;
                return sb.toString();
            }
            if (c == '\\') {
                bufferPos++;
                pos++;
                if (bufferPos >= bufferLen) {
                    bufferLen = reader.read(buffer);
                    if (bufferLen == -1) {
                        throw new IOException("Unexpected end of string");
                    }
                    bufferPos = 0;
                }
                char escaped = buffer[bufferPos];
                switch (escaped) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        if (bufferPos + 4 >= bufferLen) {
                            char[] unicode = new char[4];
                            for (int i = 0; i < 4; i++) {
                                if (bufferPos + 1 >= bufferLen) {
                                    bufferLen = reader.read(buffer);
                                    if (bufferLen == -1) {
                                        throw new IOException("Invalid unicode escape");
                                    }
                                    bufferPos = 0;
                                }
                                bufferPos++;
                                pos++;
                                unicode[i] = buffer[bufferPos];
                            }
                            sb.append((char) Integer.parseInt(new String(unicode), 16));
                        } else {
                            String hex = new String(buffer, bufferPos + 1, 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            bufferPos += 4;
                            pos += 4;
                        }
                    }
                    default -> throw new IOException("Invalid escape character: " + escaped);
                }
            } else {
                sb.append(c);
            }
            bufferPos++;
            pos++;
        }
    }
    
    private String readStringZeroCopy() throws IOException {
        int start = bufferPos;
        boolean hasEscape = false;
        while (true) {
            if (bufferPos >= bufferLen) {
                throw new IOException("Unterminated string");
            }
            char c = buffer[bufferPos];
            if (c == '"') {
                break;
            }
            if (c == '\\') {
                hasEscape = true;
            }
            bufferPos++;
            pos++;
        }
        int end = bufferPos;
        bufferPos++;
        pos++;
        if (!hasEscape) {
            return new String(buffer, start, end - start);
        }
        StringBuilder sb = new StringBuilder(end - start);
        int i = start;
        while (i < end) {
            char c = buffer[i];
            if (c == '\\') {
                i++;
                if (i >= end) {
                    throw new IOException("Unexpected end of string");
                }
                char escaped = buffer[i];
                switch (escaped) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        if (i + 4 >= end) {
                            throw new IOException("Invalid unicode escape");
                        }
                        String hex = new String(buffer, i + 1, 4);
                        sb.append((char) Integer.parseInt(hex, 16));
                        i += 4;
                    }
                    default -> throw new IOException("Invalid escape character: " + escaped);
                }
            } else {
                sb.append(c);
            }
            i++;
        }
        return sb.toString();
    }
    
    private Number readNumber() throws IOException {
        if (zeroCopyMode) {
            return readNumberZeroCopy();
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (bufferPos >= bufferLen) {
                break;
            }
            char c = buffer[bufferPos];
            if (Character.isDigit(c) || c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
                sb.append(c);
                bufferPos++;
                pos++;
            } else {
                break;
            }
        }
        String numStr = sb.toString();
        if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E")) {
            return Double.parseDouble(numStr);
        }
        long longValue = Long.parseLong(numStr);
        if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
            return (int) longValue;
        }
        return longValue;
    }
    
    private Number readNumberZeroCopy() throws IOException {
        int start = bufferPos;
        boolean isDecimal = false;
        while (true) {
            if (bufferPos >= bufferLen) {
                break;
            }
            char c = buffer[bufferPos];
            if (Character.isDigit(c) || c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
                if (c == '.' || c == 'e' || c == 'E') {
                    isDecimal = true;
                }
                bufferPos++;
                pos++;
            } else {
                break;
            }
        }
        String numStr = new String(buffer, start, bufferPos - start);
        if (isDecimal) {
            return Double.parseDouble(numStr);
        }
        try {
            long longValue = Long.parseLong(numStr);
            if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                return (int) longValue;
            }
            return longValue;
        } catch (NumberFormatException e) {
            return Double.parseDouble(numStr);
        }
    }
    
    @Override
    public String getCurrentName() throws IOException {
        if (currentToken == JsonToken.FIELD_NAME && currentValue instanceof String) {
            currentName = (String) currentValue;
            skipWhitespace();
            if (bufferPos < bufferLen && buffer[bufferPos] == ':') {
                bufferPos++;
                pos++;
            }
            currentToken = null;
            return currentName;
        }
        return currentName;
    }
    
    @Override
    public String getString() throws IOException {
        if (currentToken == JsonToken.FIELD_NAME || currentToken == JsonToken.VALUE_STRING) {
            if (currentValue instanceof String) {
                String result = (String) currentValue;
                currentToken = null;
                currentValue = null;
                return result;
            }
            return readString();
        }
        if (currentValue != null) {
            return currentValue.toString();
        }
        throw new IOException("Expected string value");
    }
    
    @Override
    public int getInt() throws IOException {
        if (currentValue instanceof Number) {
            int result = ((Number) currentValue).intValue();
            currentToken = null;
            currentValue = null;
            return result;
        }
        throw new IOException("Expected number value");
    }
    
    @Override
    public long getLong() throws IOException {
        if (currentValue instanceof Number) {
            long result = ((Number) currentValue).longValue();
            currentToken = null;
            currentValue = null;
            return result;
        }
        throw new IOException("Expected number value");
    }
    
    @Override
    public double getDouble() throws IOException {
        if (currentValue instanceof Number) {
            double result = ((Number) currentValue).doubleValue();
            currentToken = null;
            currentValue = null;
            return result;
        }
        throw new IOException("Expected number value");
    }
    
    @Override
    public boolean getBoolean() throws IOException {
        if (currentValue instanceof Boolean) {
            boolean result = (Boolean) currentValue;
            currentToken = null;
            currentValue = null;
            return result;
        }
        throw new IOException("Expected boolean value");
    }
    
    @Override
    public boolean isNull() throws IOException {
        return currentToken == JsonToken.VALUE_NULL;
    }
    
    @Override
    public void skipValue() throws IOException {
        int depth = 0;
        while (true) {
            JsonToken token = nextToken();
            if (token == JsonToken.END_OF_INPUT) {
                break;
            }
            if (token == JsonToken.START_OBJECT || token == JsonToken.START_ARRAY) {
                depth++;
            } else if (token == JsonToken.END_OBJECT || token == JsonToken.END_ARRAY) {
                depth--;
                if (depth < 0) {
                    break;
                }
            }
            if (depth == 0 && token != JsonToken.START_OBJECT && token != JsonToken.START_ARRAY) {
                break;
            }
        }
        currentToken = null;
        currentValue = null;
    }
    
    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            if (reader != null) {
                reader.close();
            }
        }
    }
}
