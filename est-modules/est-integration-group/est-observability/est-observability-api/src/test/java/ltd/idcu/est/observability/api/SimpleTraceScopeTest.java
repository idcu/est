package ltd.idcu.est.observability.api;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Map;

public class SimpleTraceScopeTest {

    @Test
    public void testCreateScope() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        MockTracesExporter exporter = new MockTracesExporter();
        SimpleTraceScope scope = new SimpleTraceScope("test-span", context, exporter);
        
        Assertions.assertEquals("test-span", scope.getSpanName());
        Assertions.assertEquals(traceId, scope.getTraceId());
        Assertions.assertEquals(spanId, scope.getSpanId());
        Assertions.assertTrue(scope.getStartTime() > 0);
        Assertions.assertTrue(scope.isSuccess());
    }

    @Test
    public void testSetSuccess() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        MockTracesExporter exporter = new MockTracesExporter();
        SimpleTraceScope scope = new SimpleTraceScope("test-span", context, exporter);
        
        scope.setSuccess(false);
        Assertions.assertFalse(scope.isSuccess());
        
        scope.setSuccess(true);
        Assertions.assertTrue(scope.isSuccess());
    }

    @Test
    public void testTags() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        MockTracesExporter exporter = new MockTracesExporter();
        SimpleTraceScope scope = new SimpleTraceScope("test-span", context, exporter);
        
        scope.setTag("string-tag", "value");
        scope.setTag("long-tag", 123L);
        scope.setTag("boolean-tag", true);
        
        Map<String, Object> tags = scope.getTags();
        
        Assertions.assertEquals("value", tags.get("string-tag"));
        Assertions.assertEquals(123L, tags.get("long-tag"));
        Assertions.assertEquals(true, tags.get("boolean-tag"));
    }

    @Test
    public void testDuration() throws InterruptedException {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        MockTracesExporter exporter = new MockTracesExporter();
        SimpleTraceScope scope = new SimpleTraceScope("test-span", context, exporter);
        
        Thread.sleep(50);
        
        Assertions.assertTrue(scope.getDuration() >= 50);
    }

    @Test
    public void testClose() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        MockTracesExporter exporter = new MockTracesExporter();
        SimpleTraceScope scope = new SimpleTraceScope("test-span", context, exporter);
        
        scope.close();
        
        Assertions.assertTrue(scope.getEndTime() > 0);
        Assertions.assertTrue(exporter.spanEnded);
    }

    @Test
    public void testGetContext() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        MockTracesExporter exporter = new MockTracesExporter();
        SimpleTraceScope scope = new SimpleTraceScope("test-span", context, exporter);
        
        TraceContext returnedContext = scope.getContext();
        
        Assertions.assertSame(context, returnedContext);
    }

    private static class MockTracesExporter implements TracesExporter {
        boolean spanEnded = false;

        @Override
        public void start() {}

        @Override
        public void stop() {}

        @Override
        public boolean isRunning() { return false; }

        @Override
        public TraceScope startSpan(String spanName) { return null; }

        @Override
        public TraceScope startSpan(String spanName, TraceContext parent) { return null; }

        @Override
        public void endSpan(TraceScope scope) {
            spanEnded = true;
        }

        @Override
        public void endSpan(TraceScope scope, boolean success) {
            spanEnded = true;
        }

        @Override
        public void addTag(TraceScope scope, String key, String value) {}

        @Override
        public void addTag(TraceScope scope, String key, long value) {}

        @Override
        public void addTag(TraceScope scope, String key, boolean value) {}

        @Override
        public void addTag(TraceScope scope, String key, double value) {}

        @Override
        public void addEvent(TraceScope scope, String eventName) {}

        @Override
        public void addEvent(TraceScope scope, String eventName, Map<String, Object> attributes) {}

        @Override
        public <T> T trace(String spanName, java.util.function.Supplier<T> supplier) { return null; }

        @Override
        public void trace(String spanName, Runnable runnable) {}

        @Override
        public TraceContext getCurrentContext() { return null; }

        @Override
        public void setCurrentContext(TraceContext context) {}

        @Override
        public void clearCurrentContext() {}

        @Override
        public String generateTraceId() { return null; }

        @Override
        public String generateSpanId() { return null; }
    }
}
