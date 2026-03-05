package ltd.idcu.est.test.http;

import java.io.IOException;
import java.util.Map;

public interface HttpClient {

    HttpResponse get(String url) throws IOException;

    HttpResponse get(String url, Map<String, String> headers) throws IOException;

    HttpResponse post(String url, String body) throws IOException;

    HttpResponse post(String url, String body, Map<String, String> headers) throws IOException;

    HttpResponse post(String url, Map<String, String> formData) throws IOException;

    HttpResponse post(String url, Map<String, String> formData, Map<String, String> headers) throws IOException;

    HttpResponse put(String url, String body) throws IOException;

    HttpResponse put(String url, String body, Map<String, String> headers) throws IOException;

    HttpResponse delete(String url) throws IOException;

    HttpResponse delete(String url, Map<String, String> headers) throws IOException;

    HttpResponse request(String method, String url, String body, Map<String, String> headers) throws IOException;

    void setBaseUrl(String baseUrl);

    String getBaseUrl();

    void setDefaultHeader(String name, String value);

    void removeDefaultHeader(String name);

    void clearDefaultHeaders();

    Map<String, String> getDefaultHeaders();
}
