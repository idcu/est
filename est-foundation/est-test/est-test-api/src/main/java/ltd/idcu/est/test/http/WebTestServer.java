package ltd.idcu.est.test.http;

import java.io.Closeable;

public interface WebTestServer extends Closeable {

    void start() throws Exception;

    void stop() throws Exception;

    void restart() throws Exception;

    boolean isRunning();

    int getPort();

    String getBaseUrl();

    HttpClient getClient();
}
