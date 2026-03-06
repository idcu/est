package ltd.idcu.est.microservices.gateway;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClient {

    public static class HttpResponse {
        private final int statusCode;
        private final Map<String, String> headers;
        private final byte[] body;

        public HttpResponse(int statusCode, Map<String, String> headers, byte[] body) {
            this.statusCode = statusCode;
            this.headers = headers;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public byte[] getBody() {
            return body;
        }

        public String getBodyAsString() {
            return new String(body, StandardCharsets.UTF_8);
        }
    }

    public HttpResponse execute(String method, String url, Map<String, String> headers, byte[] body) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        
        try {
            connection.setRequestMethod(method);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    if (!header.getKey().equalsIgnoreCase("Host") && 
                        !header.getKey().equalsIgnoreCase("Content-Length") &&
                        !header.getKey().equalsIgnoreCase("Connection")) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                    }
                }
            }
            
            if (body != null && body.length > 0) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(body);
                }
            }
            
            int statusCode = connection.getResponseCode();
            
            Map<String, String> responseHeaders = new java.util.HashMap<>();
            for (Map.Entry<String, java.util.List<String>> entry : connection.getHeaderFields().entrySet()) {
                if (entry.getKey() != null && !entry.getValue().isEmpty()) {
                    responseHeaders.put(entry.getKey(), entry.getValue().get(0));
                }
            }
            
            byte[] responseBody;
            try (InputStream is = (statusCode >= 200 && statusCode < 400) 
                    ? connection.getInputStream() 
                    : connection.getErrorStream()) {
                if (is != null) {
                    responseBody = readAllBytes(is);
                } else {
                    responseBody = new byte[0];
                }
            }
            
            return new HttpResponse(statusCode, responseHeaders, responseBody);
        } finally {
            connection.disconnect();
        }
    }

    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
