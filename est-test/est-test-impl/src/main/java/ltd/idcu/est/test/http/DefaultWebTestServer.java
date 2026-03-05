package ltd.idcu.est.test.http;

import ltd.idcu.est.web.api.WebApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DefaultWebTestServer implements WebTestServer {

    private final WebApplication application;
    private final int port;
    private final HttpClient client;
    private ExecutorService executorService;
    private volatile boolean running = false;

    public DefaultWebTestServer(Consumer<WebApplication> configurer) {
        this(configurer, 0);
    }

    public DefaultWebTestServer(Consumer<WebApplication> configurer, int port) {
        this.application = WebApplication.create("Test Application", "1.0.0");
        this.port = port;
        this.client = new DefaultHttpClient();
        
        if (configurer != null) {
            configurer.accept(application);
        }
    }

    public DefaultWebTestServer(WebApplication application) {
        this(application, 0);
    }

    public DefaultWebTestServer(WebApplication application, int port) {
        this.application = application;
        this.port = port;
        this.client = new DefaultHttpClient();
    }

    @Override
    public void start() throws Exception {
        if (running) {
            return;
        }

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                if (port > 0) {
                    application.run(port);
                } else {
                    application.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(1000);
        running = true;
        client.setBaseUrl(getBaseUrl());
    }

    @Override
    public void stop() throws Exception {
        if (!running) {
            return;
        }

        application.shutdown();
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        running = false;
    }

    @Override
    public void restart() throws Exception {
        stop();
        Thread.sleep(500);
        start();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPort() {
        if (port > 0) {
            return port;
        }
        return application.getServer() != null ? application.getServer().getPort() : 8080;
    }

    @Override
    public String getBaseUrl() {
        return "http://localhost:" + getPort();
    }

    @Override
    public HttpClient getClient() {
        return client;
    }

    @Override
    public void close() {
        try {
            stop();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close WebTestServer", e);
        }
    }
}
