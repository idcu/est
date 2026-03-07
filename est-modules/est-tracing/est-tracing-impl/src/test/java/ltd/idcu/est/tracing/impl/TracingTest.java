package ltd.idcu.est.tracing.impl;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.tracing.api.SpanExporter;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.api.Tracer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TracingTest {

    @Test
    public void testTracerCreation() {
        Tracer tracer = new DefaultTracer("test-service");
        Assertions.assertNotNull(tracer);
    }

    @Test
    public void testStartAndEndSpan() {
        Tracer tracer = new DefaultTracer("test-service");
        TraceContext context = tracer.startSpan("test-span");
        
        Assertions.assertNotNull(context);
        Assertions.assertNotNull(context.getTraceId());
        Assertions.assertNotNull(context.getSpanId());
        Assertions.assertEquals("test-service", context.getServiceName());
        
        tracer.endSpan(context);
    }

    @Test
    public void testSpanWithParent() {
        Tracer tracer = new DefaultTracer("test-service");
        TraceContext parent = tracer.startSpan("parent-span");
        TraceContext child = tracer.startSpan("child-span", parent);
        
        Assertions.assertEquals(parent.getTraceId(), child.getTraceId());
        Assertions.assertEquals(parent.getSpanId(), child.getParentSpanId());
        
        tracer.endSpan(child);
        tracer.endSpan(parent);
    }

    @Test
    public void testAddTags() {
        Tracer tracer = new DefaultTracer("test-service");
        TraceContext context = tracer.startSpan("test-span");
        
        tracer.addTag(context, "string-tag", "value");
        tracer.addTag(context, "long-tag", 123L);
        tracer.addTag(context, "boolean-tag", true);
        
        tracer.endSpan(context);
        
        Assertions.assertTrue(context.getTags().containsKey("string-tag"));
        Assertions.assertEquals("value", context.getTags().get("string-tag"));
        Assertions.assertTrue(context.getTags().containsKey("long-tag"));
        Assertions.assertEquals(123L, context.getTags().get("long-tag"));
        Assertions.assertTrue(context.getTags().containsKey("boolean-tag"));
        Assertions.assertEquals(true, context.getTags().get("boolean-tag"));
    }

    @Test
    public void testCurrentContext() {
        Tracer tracer = new DefaultTracer("test-service");
        
        Assertions.assertNull(tracer.getCurrentContext());
        
        TraceContext context = tracer.startSpan("test-span");
        Assertions.assertEquals(context, tracer.getCurrentContext());
        
        tracer.endSpan(context);
        Assertions.assertNull(tracer.getCurrentContext());
    }

    @Test
    public void testSetAndClearCurrentContext() {
        Tracer tracer = new DefaultTracer("test-service");
        TraceContext context = tracer.startSpan("test-span");
        
        tracer.clearCurrentContext();
        Assertions.assertNull(tracer.getCurrentContext());
        
        tracer.setCurrentContext(context);
        Assertions.assertEquals(context, tracer.getCurrentContext());
        
        tracer.endSpan(context);
    }

    @Test
    public void testTraceWithSupplier() {
        Tracer tracer = new DefaultTracer("test-service");
        AtomicInteger counter = new AtomicInteger(0);
        
        String result = tracer.trace("supplier-span", () -> {
            counter.incrementAndGet();
            return "test-result";
        });
        
        Assertions.assertEquals("test-result", result);
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testTraceWithRunnable() {
        Tracer tracer = new DefaultTracer("test-service");
        AtomicInteger counter = new AtomicInteger(0);
        
        tracer.trace("runnable-span", () -> {
            counter.incrementAndGet();
        });
        
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testTraceWithException() {
        Tracer tracer = new DefaultTracer("test-service");
        AtomicInteger counter = new AtomicInteger(0);
        
        try {
            tracer.trace("exception-span", () -> {
                counter.incrementAndGet();
                throw new RuntimeException("Test exception");
            });
            Assertions.fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Test exception", e.getMessage());
        }
        
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void testGenerateTraceId() {
        Tracer tracer = new DefaultTracer("test-service");
        String traceId1 = tracer.generateTraceId();
        String traceId2 = tracer.generateTraceId();
        
        Assertions.assertNotNull(traceId1);
        Assertions.assertNotNull(traceId2);
        Assertions.assertNotEquals(traceId1, traceId2);
        Assertions.assertFalse(traceId1.contains("-"));
    }

    @Test
    public void testGenerateSpanId() {
        Tracer tracer = new DefaultTracer("test-service");
        String spanId1 = tracer.generateSpanId();
        String spanId2 = tracer.generateSpanId();
        
        Assertions.assertNotNull(spanId1);
        Assertions.assertNotNull(spanId2);
        Assertions.assertNotEquals(spanId1, spanId2);
        Assertions.assertEquals(16, spanId1.length());
    }

    @Test
    public void testTraceContextJsonSerialization() {
        TraceContext context = new TraceContext("trace-123", "span-456", "parent-789", "test-service");
        context.setStartTimeMs(1000L);
        context.setEndTimeMs(2000L);
        context.setSuccess(true);
        context.addTag("key1", "value1");
        context.addTag("key2", 123L);
        
        String json = context.toJson();
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("trace-123"));
        Assertions.assertTrue(json.contains("span-456"));
        Assertions.assertTrue(json.contains("parent-789"));
        Assertions.assertTrue(json.contains("test-service"));
        Assertions.assertTrue(json.contains("key1"));
        Assertions.assertTrue(json.contains("value1"));
        
        TraceContext restored = TraceContext.fromJson(json);
        Assertions.assertEquals("trace-123", restored.getTraceId());
        Assertions.assertEquals("span-456", restored.getSpanId());
        Assertions.assertEquals("parent-789", restored.getParentSpanId());
        Assertions.assertEquals("test-service", restored.getServiceName());
        Assertions.assertTrue(restored.isSuccess());
    }

    @Test
    public void testFileSpanExporter() throws IOException, InterruptedException {
        String tempPath = System.getProperty("java.io.tmpdir") + "/test-traces.jsonl";
        FileSpanExporter exporter = new FileSpanExporter(tempPath, 10, 100);
        
        TraceContext context = new TraceContext("trace-1", "span-1", null, "test-service");
        exporter.export(context);
        
        exporter.flush();
        exporter.shutdown();
        
        List<TraceContext> loaded = exporter.loadSpans();
        Assertions.assertEquals(1, loaded.size());
        Assertions.assertEquals("trace-1", loaded.get(0).getTraceId());
        
        java.io.File tempFile = new java.io.File(tempPath);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testLoggingSpanExporter() {
        LoggingSpanExporter exporter = new LoggingSpanExporter();
        TraceContext context = new TraceContext("trace-1", "span-1", null, "test-service");
        
        exporter.export(context);
        exporter.flush();
        exporter.shutdown();
    }

    @Test
    public void testCustomSpanExporter() {
        List<TraceContext> exported = new ArrayList<>();
        SpanExporter exporter = new SpanExporter() {
            @Override
            public void export(TraceContext context) {
                exported.add(context);
            }

            @Override
            public void exportBatch(List<TraceContext> contexts) {
                exported.addAll(contexts);
            }

            @Override
            public void flush() {
            }

            @Override
            public void shutdown() {
            }
        };
        
        Tracer tracer = new DefaultTracer("test-service", exporter);
        TraceContext context = tracer.startSpan("test-span");
        tracer.endSpan(context);
        
        Assertions.assertEquals(1, exported.size());
        Assertions.assertEquals("test-span", exported.get(0).getSpanName());
    }

    @Test
    public void testDefaultTracerRegistry() {
        DefaultTracerRegistry registry = new DefaultTracerRegistry();
        
        Tracer tracer1 = registry.getOrCreate("service-1");
        Tracer tracer2 = registry.getOrCreate("service-2");
        Tracer tracer3 = registry.getOrCreate("service-1");
        
        Assertions.assertNotNull(tracer1);
        Assertions.assertNotNull(tracer2);
        Assertions.assertEquals(tracer1, tracer3);
        Assertions.assertNotEquals(tracer1, tracer2);
        
        Assertions.assertTrue(registry.getAllServiceNames().contains("service-1"));
        Assertions.assertTrue(registry.getAllServiceNames().contains("service-2"));
        
        registry.remove("service-1");
        Assertions.assertFalse(registry.getAllServiceNames().contains("service-1"));
        
        registry.clear();
        Assertions.assertTrue(registry.getAllServiceNames().isEmpty());
    }

    @Test
    public void testNestedSpans() {
        Tracer tracer = new DefaultTracer("test-service");
        List<String> spanNames = new ArrayList<>();
        
        TraceContext parent = tracer.startSpan("parent");
        spanNames.add(parent.getSpanName());
        
        TraceContext child1 = tracer.startSpan("child-1");
        spanNames.add(child1.getSpanName());
        
        TraceContext grandchild = tracer.startSpan("grandchild");
        spanNames.add(grandchild.getSpanName());
        
        tracer.endSpan(grandchild);
        tracer.endSpan(child1);
        
        TraceContext child2 = tracer.startSpan("child-2");
        spanNames.add(child2.getSpanName());
        
        tracer.endSpan(child2);
        tracer.endSpan(parent);
        
        Assertions.assertEquals(4, spanNames.size());
        Assertions.assertEquals("parent", spanNames.get(0));
        Assertions.assertEquals("child-1", spanNames.get(1));
        Assertions.assertEquals("grandchild", spanNames.get(2));
        Assertions.assertEquals("child-2", spanNames.get(3));
    }
}
