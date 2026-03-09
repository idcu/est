package ltd.idcu.est.observability.elk;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import ltd.idcu.est.observability.api.LogsExporter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ElkLogsExporter implements LogsExporter {

    private final String elasticsearchHost;
    private final int elasticsearchPort;
    private final String indexName;
    private final String serviceName;
    private ElasticsearchClient client;
    private RestClient restClient;
    private boolean running = false;
    private final LinkedBlockingQueue<Map<String, Object>> logQueue;
    private final ExecutorService executorService;
    private Thread consumerThread;

    public ElkLogsExporter(String elasticsearchHost, int elasticsearchPort, 
                           String indexName, String serviceName) {
        this.elasticsearchHost = elasticsearchHost;
        this.elasticsearchPort = elasticsearchPort;
        this.indexName = indexName;
        this.serviceName = serviceName;
        this.logQueue = new LinkedBlockingQueue<>(10000);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public ElkLogsExporter(String indexName, String serviceName) {
        this("localhost", 9200, indexName, serviceName);
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        try {
            restClient = RestClient.builder(
                    new HttpHost(elasticsearchHost, elasticsearchPort, "http")
            ).build();
            ElasticsearchTransport transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper()
            );
            client = new ElasticsearchClient(transport);
            
            consumerThread = new Thread(this::consumeLogs);
            consumerThread.setDaemon(true);
            consumerThread.start();
            
            running = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start ELK exporter", e);
        }
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (consumerThread != null) {
            consumerThread.interrupt();
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        if (restClient != null) {
            try {
                restClient.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void log(LogLevel level, String message) {
        log(level, message, null, null);
    }

    @Override
    public void log(LogLevel level, String message, Map<String, Object> context) {
        log(level, message, null, context);
    }

    @Override
    public void log(LogLevel level, String message, Throwable throwable) {
        log(level, message, throwable, null);
    }

    @Override
    public void log(LogLevel level, String message, Throwable throwable, Map<String, Object> context) {
        if (!running) {
            return;
        }
        Map<String, Object> logEvent = new HashMap<>();
        logEvent.put("@timestamp", Instant.now().toString());
        logEvent.put("level", level.name());
        logEvent.put("message", message);
        logEvent.put("service.name", serviceName);
        
        if (throwable != null) {
            logEvent.put("exception.type", throwable.getClass().getName());
            logEvent.put("exception.message", throwable.getMessage());
            logEvent.put("exception.stacktrace", getStackTraceAsString(throwable));
        }
        
        if (context != null) {
            logEvent.putAll(context);
        }
        
        logQueue.offer(logEvent);
    }

    @Override
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(String message, Map<String, Object> context) {
        log(LogLevel.DEBUG, message, context);
    }

    @Override
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    @Override
    public void info(String message, Map<String, Object> context) {
        log(LogLevel.INFO, message, context);
    }

    @Override
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    @Override
    public void warn(String message, Map<String, Object> context) {
        log(LogLevel.WARN, message, context);
    }

    @Override
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    public void error(String message, Map<String, Object> context) {
        log(LogLevel.ERROR, message, context);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    @Override
    public void error(String message, Throwable throwable, Map<String, Object> context) {
        log(LogLevel.ERROR, message, throwable, context);
    }

    private void consumeLogs() {
        while (running) {
            try {
                Map<String, Object> logEvent = logQueue.poll(1, TimeUnit.SECONDS);
                if (logEvent != null) {
                    indexLog(logEvent);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // Log error but continue
            }
        }
    }

    private void indexLog(Map<String, Object> logEvent) {
        try {
            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .document(logEvent)
            );
            client.index(request);
        } catch (Exception e) {
            // Fallback to console
            System.err.println("Failed to index log: " + logEvent);
        }
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
