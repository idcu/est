package ltd.idcu.est.observability.api.examples;

import ltd.idcu.est.observability.api.*;
import ltd.idcu.est.observability.prometheus.PrometheusMetricsExporter;
import ltd.idcu.est.observability.elk.ElkLogsExporter;
import ltd.idcu.est.observability.opentelemetry.OpenTelemetryTracesExporter;

import java.util.HashMap;
import java.util.Map;

public class ObservabilityExample {

    public static void main(String[] args) {
        basicExample();
        metricsExample();
        logsExample();
        tracesExample();
        fullIntegrationExample();
    }

    public static void basicExample() {
        System.out.println("=== Basic Observability Example ===");
        
        MetricsExporter metrics = new PrometheusMetricsExporter(9090);
        LogsExporter logs = new ElkLogsExporter("est-logs", "my-service");
        TracesExporter traces = new OpenTelemetryTracesExporter("my-service", "1.0.0");
        
        Observability observability = new DefaultObservability(
                "my-service",
                "1.0.0",
                metrics,
                logs,
                traces
        );
        
        observability.start();
        System.out.println("Observability started: " + observability.getServiceName());
        
        observability.stop();
        System.out.println("Observability stopped\n");
    }

    public static void metricsExample() {
        System.out.println("=== Metrics Example ===");
        
        MetricsExporter metrics = new PrometheusMetricsExporter(9091);
        metrics.start();
        
        metrics.registerCounter("http_requests_total", "Total HTTP requests", "method", "status");
        metrics.registerGauge("active_connections", "Number of active connections");
        metrics.registerHistogram("request_duration_seconds", "Request duration", 0.1, 0.5, 1.0, 2.0, 5.0);
        metrics.registerTimer("database_query_time", "Database query time");
        
        Map<String, String> labels = new HashMap<>();
        labels.put("method", "GET");
        labels.put("status", "200");
        metrics.incrementCounter("http_requests_total", labels);
        
        metrics.setGauge("active_connections", 150);
        
        metrics.recordHistogram("request_duration_seconds", 0.234);
        
        metrics.recordTimer("database_query_time", 45);
        
        Map<String, Object> metricsInfo = metrics.getMetrics();
        System.out.println("Metrics info: " + metricsInfo);
        
        metrics.stop();
        System.out.println("Metrics stopped\n");
    }

    public static void logsExample() {
        System.out.println("=== Logs Example ===");
        
        LogsExporter logs = new ElkLogsExporter("est-logs", "my-service");
        logs.start();
        
        logs.debug("Debug message");
        logs.info("Info message");
        logs.warn("Warning message");
        logs.error("Error message");
        
        Map<String, Object> context = new HashMap<>();
        context.put("user_id", "12345");
        context.put("request_id", "abc-123");
        logs.info("User login successful", context);
        
        try {
            throw new RuntimeException("Something went wrong");
        } catch (Exception e) {
            logs.error("Operation failed", e, context);
        }
        
        logs.stop();
        System.out.println("Logs stopped\n");
    }

    public static void tracesExample() {
        System.out.println("=== Traces Example ===");
        
        TracesExporter traces = new OpenTelemetryTracesExporter("my-service", "1.0.0");
        traces.start();
        
        TraceScope parentScope = traces.startSpan("parent-operation");
        System.out.println("Parent trace ID: " + parentScope.getTraceId());
        System.out.println("Parent span ID: " + parentScope.getSpanId());
        
        traces.addTag(parentScope, "operation", "create");
        traces.addTag(parentScope, "priority", "high");
        traces.addEvent(parentScope, "started");
        
        TraceContext parentContext = parentScope.getContext();
        TraceScope childScope = traces.startSpan("child-operation", parentContext);
        System.out.println("Child trace ID: " + childScope.getTraceId());
        System.out.println("Child span ID: " + childScope.getSpanId());
        System.out.println("Child parent span ID: " + childScope.getParentSpanId());
        
        traces.addTag(childScope, "step", 1);
        traces.addEvent(childScope, "processing");
        
        traces.endSpan(childScope, true);
        traces.addEvent(parentScope, "completed");
        traces.endSpan(parentScope, true);
        
        String result = traces.trace("lambda-operation", () -> {
            System.out.println("Executing traced operation");
            return "success";
        });
        System.out.println("Traced operation result: " + result);
        
        traces.trace("runnable-operation", () -> {
            System.out.println("Executing traced runnable");
        });
        
        traces.stop();
        System.out.println("Traces stopped\n");
    }

    public static void fullIntegrationExample() {
        System.out.println("=== Full Integration Example ===");
        
        MetricsExporter metrics = new PrometheusMetricsExporter(9092);
        LogsExporter logs = new ElkLogsExporter("est-logs", "ecommerce-service");
        TracesExporter traces = new OpenTelemetryTracesExporter("ecommerce-service", "2.4.0");
        
        Observability observability = new DefaultObservability(
                "ecommerce-service",
                "2.4.0",
                metrics,
                logs,
                traces
        );
        
        observability.start();
        System.out.println("Service: " + observability.getServiceName());
        System.out.println("Version: " + observability.getServiceVersion());
        
        metrics.registerCounter("orders_total", "Total orders", "status");
        metrics.registerHistogram("order_processing_time", "Order processing time");
        
        Map<String, String> counterLabels = new HashMap<>();
        counterLabels.put("status", "success");
        
        try (TraceScope scope = traces.startSpan("process-order")) {
            logs.info("Starting order processing");
            
            traces.addTag(scope, "order_id", "ORD-12345");
            traces.addTag(scope, "customer_id", "CUST-67890");
            
            metrics.incrementCounter("orders_total", counterLabels);
            long startTime = System.currentTimeMillis();
            
            try (TraceScope childScope = traces.startSpan("validate-order", scope.getContext())) {
                logs.debug("Validating order");
                traces.addEvent(childScope, "validation-passed");
            }
            
            try (TraceScope childScope = traces.startSpan("charge-payment", scope.getContext())) {
                logs.info("Charging payment");
                traces.addTag(childScope, "amount", 99.99);
            }
            
            long duration = System.currentTimeMillis() - startTime;
            metrics.recordHistogram("order_processing_time", duration);
            
            logs.info("Order processed successfully");
            traces.addTag(scope, "success", true);
            traces.addEvent(scope, "order-completed");
            
        } catch (Exception e) {
            logs.error("Order processing failed", e);
        }
        
        observability.stop();
        System.out.println("Full integration example completed\n");
    }
}
