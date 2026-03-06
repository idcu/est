package ltd.idcu.est.test.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultHttpClient implements HttpClient {

    private String baseUrl;
    private final Map<String, String> defaultHeaders = new HashMap<>();

    public DefaultHttpClient() {
        this(null);
    }

    public DefaultHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public HttpResponse get(String url) throws IOException {
        return get(url, null);
    }

    @Override
    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        return request("GET", url, null, headers);
    }

    @Override
    public HttpResponse post(String url, String body) throws IOException {
        return post(url, body, null);
    }

    @Override
    public HttpResponse post(String url, String body, Map<String, String> headers) throws IOException {
        return request("POST", url, body, headers);
    }

    @Override
    public HttpResponse post(String url, Map<String, String> formData) throws IOException {
        return post(url, formData, null);
    }

    @Override
    public HttpResponse post(String url, Map<String, String> formData, Map<String, String> headers) throws IOException {
        StringBuilder body = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (!first) {
                body.append("&");
            }
            body.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            body.append("=");
            body.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            first = false;
        }
        Map<String, String> allHeaders = new HashMap<>();
        if (headers != null) {
            allHeaders.putAll(headers);
        }
        allHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        return request("POST", url, body.toString(), allHeaders);
    }

    @Override
    public HttpResponse put(String url, String body) throws IOException {
        return put(url, body, null);
    }

    @Override
    public HttpResponse put(String url, String body, Map<String, String> headers) throws IOException {
        return request("PUT", url, body, headers);
    }

    @Override
    public HttpResponse delete(String url) throws IOException {
        return delete(url, null);
    }

    @Override
    public HttpResponse delete(String url, Map<String, String> headers) throws IOException {
        return request("DELETE", url, null, headers);
    }

    @Override
    public HttpResponse request(String method, String url, String body, Map<String, String> headers) throws IOException {
        String fullUrl = buildUrl(url);
        HttpURLConnection connection = null;
        try {
            URL requestUrl = URI.create(fullUrl).toURL();
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (body != null && !body.isEmpty()) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }
            }

            int statusCode = connection.getResponseCode();
            String statusMessage = connection.getResponseMessage();

            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                inputStream = connection.getErrorStream();
            }

            byte[] responseBody;
            if (inputStream != null) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, read);
                    }
                    responseBody = baos.toByteArray();
                }
            } else {
                responseBody = new byte[0];
            }

            Map<String, List<String>> responseHeaders = new HashMap<>(connection.getHeaderFields());
            responseHeaders.remove(null);

            return new DefaultHttpResponse(statusCode, statusMessage, responseBody, responseHeaders);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String buildUrl(String url) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return url;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        String cleanBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String cleanUrl = url.startsWith("/") ? url : "/" + url;
        return cleanBaseUrl + cleanUrl;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void setDefaultHeader(String name, String value) {
        defaultHeaders.put(name, value);
    }

    @Override
    public void removeDefaultHeader(String name) {
        defaultHeaders.remove(name);
    }

    @Override
    public void clearDefaultHeaders() {
        defaultHeaders.clear();
    }

    @Override
    public Map<String, String> getDefaultHeaders() {
        return new HashMap<>(defaultHeaders);
    }
}
