package ltd.idcu.est.gateway;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClient {

    public static class HttpResponse {
        private final int statusCode;
        private final Map&lt;String, String&gt; headers;
        private final byte[] body;

        public HttpResponse(int statusCode, Map&lt;String, String&gt; headers, byte[] body) {
            this.statusCode = statusCode;
            this.headers = headers;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public Map&lt;String, String&gt; getHeaders() {
            return headers;
        }

        public byte[] getBody() {
            return body;
        }

        public String getBodyAsString() {
            return new String(body, StandardCharsets.UTF_8);
        }
    }

    public HttpResponse execute(String method, String url, Map&lt;String, String&gt; headers, byte[] body) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        
        try {
            connection.setRequestMethod(method);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            
            if (headers != null) {
                for (Map.Entry&lt;String, String&gt; header : headers.entrySet()) {
                    if (!header.getKey().equalsIgnoreCase("Host") &amp;&amp; 
                        !header.getKey().equalsIgnoreCase("Content-Length") &amp;&amp;
                        !header.getKey().equalsIgnoreCase("Connection")) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                    }
                }
            }
            
            if (body != null &amp;&amp; body.length &gt; 0) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(body);
                }
            }
            
            int statusCode = connection.getResponseCode();
            
            Map&lt;String, String&gt; responseHeaders = new java.util.HashMap&lt;&gt;();
            for (Map.Entry&lt;String, java.util.List&lt;String&gt;&gt; entry : connection.getHeaderFields().entrySet()) {
                if (entry.getKey() != null &amp;&amp; !entry.getValue().isEmpty()) {
                    responseHeaders.put(entry.getKey(), entry.getValue().get(0));
                }
            }
            
            byte[] responseBody;
            try (InputStream is = (statusCode &gt;= 200 &amp;&amp; statusCode &lt; 400) 
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
