package ltd.idcu.est.codecli.plugin.official;

import ltd.idcu.est.codecli.plugin.BaseEstPlugin;
import ltd.idcu.est.codecli.plugin.PluginException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class ApiTestPlugin extends BaseEstPlugin {

    private static final String PLUGIN_ID = "api-test-plugin";
    private static final String PLUGIN_NAME = "API Test Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "API 测试插件，提供 REST API 测试、性能测试、请求记录等功能";
    private static final String PLUGIN_AUTHOR = "EST Team";

    private List<ApiRequest> requestHistory;
    private Map<String, String> defaultHeaders;
    private int timeout;

    public ApiTestPlugin() {
        this.requestHistory = new ArrayList<>();
        this.defaultHeaders = new HashMap<>();
        this.timeout = 30000;
        
        addCapability("features", Map.of(
            "get", true,
            "post", true,
            "put", true,
            "delete", true,
            "performance", true,
            "history", true
        ));
        addCapability("commands", new String[]{"api_get", "api_post", "api_put", "api_delete", "api_perf", "api_history"});
    }

    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public String getDescription() {
        return PLUGIN_DESCRIPTION;
    }

    @Override
    public String getAuthor() {
        return PLUGIN_AUTHOR;
    }

    @Override
    protected void onInitialize() throws PluginException {
        logInfo("API 测试插件初始化完成");
        setDefaultHeader("Content-Type", "application/json");
        setDefaultHeader("Accept", "application/json");
    }

    public void setDefaultHeader(String name, String value) {
        defaultHeaders.put(name, value);
        logInfo("设置默认请求头: " + name + " = " + value);
    }

    public void removeDefaultHeader(String name) {
        defaultHeaders.remove(name);
        logInfo("移除默认请求头: " + name);
    }

    public void setTimeout(int timeoutMs) {
        this.timeout = timeoutMs;
        logInfo("设置超时时间: " + timeoutMs + "ms");
    }

    public ApiResponse get(String url) throws PluginException {
        return sendRequest("GET", url, null, null);
    }

    public ApiResponse get(String url, Map<String, String> headers) throws PluginException {
        return sendRequest("GET", url, headers, null);
    }

    public ApiResponse post(String url, String body) throws PluginException {
        return sendRequest("POST", url, null, body);
    }

    public ApiResponse post(String url, Map<String, String> headers, String body) throws PluginException {
        return sendRequest("POST", url, headers, body);
    }

    public ApiResponse put(String url, String body) throws PluginException {
        return sendRequest("PUT", url, null, body);
    }

    public ApiResponse put(String url, Map<String, String> headers, String body) throws PluginException {
        return sendRequest("PUT", url, headers, body);
    }

    public ApiResponse delete(String url) throws PluginException {
        return sendRequest("DELETE", url, null, null);
    }

    public ApiResponse delete(String url, Map<String, String> headers) throws PluginException {
        return sendRequest("DELETE", url, headers, null);
    }

    private ApiResponse sendRequest(String method, String url, Map<String, String> headers, String body) throws PluginException {
        long startTime = System.currentTimeMillis();
        
        try {
            URL requestUrl = URI.create(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            
            Map<String, String> allHeaders = new HashMap<>(defaultHeaders);
            if (headers != null) {
                allHeaders.putAll(headers);
            }
            
            for (Map.Entry<String, String> header : allHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            
            if (body != null && !body.isEmpty()) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(body.getBytes());
                    os.flush();
                }
            }
            
            int statusCode = connection.getResponseCode();
            long duration = System.currentTimeMillis() - startTime;
            
            StringBuilder responseBody = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(statusCode >= 400 ? connection.getErrorStream() : connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
            }
            
            Map<String, List<String>> responseHeaders = connection.getHeaderFields();
            
            ApiRequest request = new ApiRequest(method, url, allHeaders, body);
            ApiResponse response = new ApiResponse(statusCode, responseBody.toString().trim(), responseHeaders, duration);
            
            requestHistory.add(request);
            logInfo(method + " " + url + " - " + statusCode + " (" + duration + "ms)");
            
            return response;
        } catch (IOException e) {
            throw new PluginException("API 请求失败: " + e.getMessage(), e);
        }
    }

    public PerformanceTestResult runPerformanceTest(String url, String method, int iterations, int concurrency) throws PluginException {
        logInfo("开始性能测试: " + method + " " + url + ", 迭代次数: " + iterations + ", 并发数: " + concurrency);
        
        List<Long> responseTimes = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;
        
        for (int i = 0; i < iterations; i++) {
            try {
                ApiResponse response = sendRequest(method, url, null, null);
                responseTimes.add(response.getDuration());
                if (response.getStatusCode() >= 200 && response.getStatusCode() < 400) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (PluginException e) {
                failureCount++;
                logWarn("请求失败: " + e.getMessage());
            }
        }
        
        Collections.sort(responseTimes);
        long minTime = responseTimes.isEmpty() ? 0 : responseTimes.get(0);
        long maxTime = responseTimes.isEmpty() ? 0 : responseTimes.get(responseTimes.size() - 1);
        double avgTime = responseTimes.isEmpty() ? 0 : responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long p50 = getPercentile(responseTimes, 50);
        long p90 = getPercentile(responseTimes, 90);
        long p95 = getPercentile(responseTimes, 95);
        long p99 = getPercentile(responseTimes, 99);
        
        PerformanceTestResult result = new PerformanceTestResult(
            url, method, iterations, successCount, failureCount,
            minTime, maxTime, avgTime, p50, p90, p95, p99
        );
        
        logInfo("性能测试完成: 成功率 " + (successCount * 100.0 / iterations) + "%, 平均响应时间 " + avgTime + "ms");
        
        return result;
    }

    private long getPercentile(List<Long> sortedTimes, int percentile) {
        if (sortedTimes.isEmpty()) {
            return 0;
        }
        int index = (int) Math.ceil(percentile / 100.0 * sortedTimes.size()) - 1;
        return sortedTimes.get(Math.max(0, Math.min(index, sortedTimes.size() - 1)));
    }

    public List<ApiRequest> getRequestHistory() {
        return new ArrayList<>(requestHistory);
    }

    public List<ApiRequest> getRequestHistory(int limit) {
        int start = Math.max(0, requestHistory.size() - limit);
        return new ArrayList<>(requestHistory.subList(start, requestHistory.size()));
    }

    public void clearHistory() {
        requestHistory.clear();
        logInfo("请求历史已清空");
    }

    public boolean assertStatusCode(ApiResponse response, int expectedStatusCode) {
        return response.getStatusCode() == expectedStatusCode;
    }

    public boolean assertStatusCodeInRange(ApiResponse response, int min, int max) {
        return response.getStatusCode() >= min && response.getStatusCode() <= max;
    }

    public boolean assertResponseBodyContains(ApiResponse response, String content) {
        return response.getBody() != null && response.getBody().contains(content);
    }

    public boolean assertResponseBodyMatches(ApiResponse response, String regex) {
        return response.getBody() != null && Pattern.matches(regex, response.getBody());
    }

    public boolean assertResponseTimeLessThan(ApiResponse response, long maxMs) {
        return response.getDuration() < maxMs;
    }

    public boolean assertHeaderExists(ApiResponse response, String headerName) {
        return response.getHeaders().containsKey(headerName);
    }

    public static class ApiRequest {
        private final String method;
        private final String url;
        private final Map<String, String> headers;
        private final String body;
        private final long timestamp;

        public ApiRequest(String method, String url, Map<String, String> headers, String body) {
            this.method = method;
            this.url = url;
            this.headers = headers;
            this.body = body;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMethod() { return method; }
        public String getUrl() { return url; }
        public Map<String, String> getHeaders() { return headers; }
        public String getBody() { return body; }
        public long getTimestamp() { return timestamp; }
    }

    public static class ApiResponse {
        private final int statusCode;
        private final String body;
        private final Map<String, List<String>> headers;
        private final long duration;

        public ApiResponse(int statusCode, String body, Map<String, List<String>> headers, long duration) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
            this.duration = duration;
        }

        public int getStatusCode() { return statusCode; }
        public String getBody() { return body; }
        public Map<String, List<String>> getHeaders() { return headers; }
        public long getDuration() { return duration; }
    }

    public static class PerformanceTestResult {
        private final String url;
        private final String method;
        private final int totalRequests;
        private final int successCount;
        private final int failureCount;
        private final long minTime;
        private final long maxTime;
        private final double avgTime;
        private final long p50;
        private final long p90;
        private final long p95;
        private final long p99;

        public PerformanceTestResult(String url, String method, int totalRequests, int successCount, int failureCount,
                                      long minTime, long maxTime, double avgTime, long p50, long p90, long p95, long p99) {
            this.url = url;
            this.method = method;
            this.totalRequests = totalRequests;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.minTime = minTime;
            this.maxTime = maxTime;
            this.avgTime = avgTime;
            this.p50 = p50;
            this.p90 = p90;
            this.p95 = p95;
            this.p99 = p99;
        }

        public String getUrl() { return url; }
        public String getMethod() { return method; }
        public int getTotalRequests() { return totalRequests; }
        public int getSuccessCount() { return successCount; }
        public int getFailureCount() { return failureCount; }
        public long getMinTime() { return minTime; }
        public long getMaxTime() { return maxTime; }
        public double getAvgTime() { return avgTime; }
        public long getP50() { return p50; }
        public long getP90() { return p90; }
        public long getP95() { return p95; }
        public long getP99() { return p99; }
        public double getSuccessRate() { return totalRequests > 0 ? successCount * 100.0 / totalRequests : 0; }
    }
}
