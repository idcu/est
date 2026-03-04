package ltd.idcu.est.web;

import ltd.idcu.est.web.api.FormData;
import ltd.idcu.est.web.api.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MultipartParser {

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FORM_DATA = "form-data";
    private static final String NAME = "name";
    private static final String FILENAME = "filename";

    public static FormData parse(String contentType, byte[] body) throws IOException {
        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            return parseUrlEncodedFormData(body);
        }

        String boundary = extractBoundary(contentType);
        if (boundary == null) {
            return new DefaultFormData(false);
        }

        return parseMultipart(new ByteArrayInputStream(body), boundary);
    }

    private static String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                String boundary = part.substring("boundary=".length());
                if (boundary.startsWith("\"") && boundary.endsWith("\"")) {
                    boundary = boundary.substring(1, boundary.length() - 1);
                }
                return "--" + boundary;
            }
        }
        return null;
    }

    private static FormData parseUrlEncodedFormData(byte[] body) {
        DefaultFormData formData = new DefaultFormData(false);
        if (body == null || body.length == 0) {
            return formData;
        }

        String bodyStr = new String(body, StandardCharsets.UTF_8);
        String[] pairs = bodyStr.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = urlDecode(pair.substring(0, idx));
                String value = urlDecode(pair.substring(idx + 1));
                formData.addParameter(key, value);
            } else if (!pair.isEmpty()) {
                String key = urlDecode(pair);
                formData.addParameter(key, "");
            }
        }
        return formData;
    }

    private static String urlDecode(String str) {
        try {
            return java.net.URLDecoder.decode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return str;
        }
    }

    private static FormData parseMultipart(InputStream inputStream, String boundary) throws IOException {
        DefaultFormData formData = new DefaultFormData(true);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        boolean inHeader = true;
        Map<String, String> headers = new HashMap<>();
        ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();

        byte[] boundaryBytes = boundary.getBytes(StandardCharsets.UTF_8);
        byte[] boundaryEndBytes = (boundary + "--").getBytes(StandardCharsets.UTF_8);

        byte[] currentBytes = new byte[boundaryEndBytes.length];
        int currentPos = 0;

        while ((b = inputStream.read()) != -1) {
            currentBytes[currentPos % currentBytes.length] = (byte) b;
            currentPos++;

            if (currentPos >= boundaryEndBytes.length) {
                if (startsWith(currentBytes, currentPos, boundaryEndBytes)) {
                    processPart(formData, headers, contentBuffer);
                    break;
                }
                if (startsWith(currentBytes, currentPos, boundaryBytes)) {
                    processPart(formData, headers, contentBuffer);
                    inHeader = true;
                    headers.clear();
                    contentBuffer.reset();
                    continue;
                }
            }

            if (inHeader) {
                buffer.write(b);
                if (b == '\n') {
                    String line = buffer.toString(StandardCharsets.UTF_8.name()).trim();
                    buffer.reset();
                    if (line.isEmpty()) {
                        inHeader = false;
                    } else {
                        int colonIdx = line.indexOf(':');
                        if (colonIdx > 0) {
                            String headerName = line.substring(0, colonIdx).trim();
                            String headerValue = line.substring(colonIdx + 1).trim();
                            headers.put(headerName, headerValue);
                        }
                    }
                }
            } else {
                contentBuffer.write(b);
            }
        }

        return formData;
    }

    private static boolean startsWith(byte[] buffer, int bufferPos, byte[] prefix) {
        int start = (bufferPos - prefix.length) % buffer.length;
        for (int i = 0; i < prefix.length; i++) {
            int idx = (start + i) % buffer.length;
            if (buffer[idx] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    private static void processPart(DefaultFormData formData, Map<String, String> headers, ByteArrayOutputStream contentBuffer) {
        if (headers.isEmpty() || contentBuffer.size() == 0) {
            return;
        }

        String contentDisposition = headers.get(CONTENT_DISPOSITION);
        if (contentDisposition == null || !contentDisposition.contains(FORM_DATA)) {
            return;
        }

        String name = extractAttribute(contentDisposition, NAME);
        String filename = extractAttribute(contentDisposition, FILENAME);
        String contentType = headers.get(CONTENT_TYPE);

        if (filename != null && !filename.isEmpty()) {
            byte[] content = contentBuffer.toByteArray();
            if (content.length > 2 && content[0] == '\r' && content[1] == '\n') {
                content = java.util.Arrays.copyOfRange(content, 2, content.length);
            } else if (content.length > 1 && content[0] == '\n') {
                content = java.util.Arrays.copyOfRange(content, 1, content.length);
            }
            MultipartFile file = new DefaultMultipartFile(name, filename, contentType, content);
            formData.addFile(name, file);
        } else {
            String content = contentBuffer.toString(StandardCharsets.UTF_8);
            if (content.startsWith("\r\n")) {
                content = content.substring(2);
            } else if (content.startsWith("\n")) {
                content = content.substring(1);
            }
            if (content.endsWith("\r\n")) {
                content = content.substring(0, content.length() - 2);
            } else if (content.endsWith("\n")) {
                content = content.substring(0, content.length() - 1);
            }
            formData.addParameter(name, content);
        }
    }

    private static String extractAttribute(String header, String attributeName) {
        String search = attributeName + "=";
        int idx = header.indexOf(search);
        if (idx == -1) {
            return null;
        }
        int start = idx + search.length();
        if (start >= header.length()) {
            return null;
        }
        
        char quote = header.charAt(start);
        if (quote == '"' || quote == '\'') {
            int end = header.indexOf(quote, start + 1);
            if (end == -1) {
                return null;
            }
            return header.substring(start + 1, end);
        } else {
            int end = header.indexOf(';', start);
            if (end == -1) {
                end = header.length();
            }
            return header.substring(start, end).trim();
        }
    }
}
