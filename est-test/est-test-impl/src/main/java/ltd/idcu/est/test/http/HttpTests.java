package ltd.idcu.est.test.http;

import ltd.idcu.est.web.api.WebApplication;

import java.util.function.Consumer;

public final class HttpTests {

    private HttpTests() {
    }

    public static HttpClient client() {
        return new DefaultHttpClient();
    }

    public static HttpClient client(String baseUrl) {
        return new DefaultHttpClient(baseUrl);
    }

    public static WebTestServer server(Consumer<WebApplication> configurer) {
        return new DefaultWebTestServer(configurer);
    }

    public static WebTestServer server(Consumer<WebApplication> configurer, int port) {
        return new DefaultWebTestServer(configurer, port);
    }

    public static WebTestServer server(WebApplication application) {
        return new DefaultWebTestServer(application);
    }

    public static WebTestServer server(WebApplication application, int port) {
        return new DefaultWebTestServer(application, port);
    }
}
