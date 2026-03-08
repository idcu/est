package ltd.idcu.est.discovery.impl;

import ltd.idcu.est.discovery.api.HealthChecker;
import ltd.idcu.est.discovery.api.HttpHealthCheckConfig;
import ltd.idcu.est.discovery.api.ServiceInstance;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHealthChecker implements HealthChecker {
    private HttpHealthCheckConfig config;

    public HttpHealthChecker() {
        this(new HttpHealthCheckConfig("/health"));
    }

    public HttpHealthChecker(HttpHealthCheckConfig config) {
        this.config = config;
    }

    public void setConfig(HttpHealthCheckConfig config) {
        this.config = config;
    }

    @Override
    public boolean checkHealth(ServiceInstance instance) {
        String healthUrl = instance.getUri() + config.getPath();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(healthUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(config.getTimeoutMs());
            connection.setReadTimeout(config.getTimeoutMs());
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            return responseCode == config.getExpectedStatusCode();
        } catch (IOException e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
