package ltd.idcu.est.observability.api;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Map;

public class SimpleTraceContextTest {

    @Test
    public void testCreateContext() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        Assertions.assertEquals(traceId, context.getTraceId());
        Assertions.assertEquals(spanId, context.getSpanId());
        Assertions.assertNull(context.getParentSpanId());
        Assertions.assertTrue(context.isSampled());
    }

    @Test
    public void testCreateContextWithParent() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        String parentSpanId = SimpleTraceContext.generateSpanId();
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, parentSpanId, true);
        
        Assertions.assertEquals(traceId, context.getTraceId());
        Assertions.assertEquals(spanId, context.getSpanId());
        Assertions.assertEquals(parentSpanId, context.getParentSpanId());
    }

    @Test
    public void testBaggage() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        
        context.setBaggageItem("key1", "value1");
        context.setBaggageItem("key2", "value2");
        
        Assertions.assertEquals("value1", context.getBaggageItem("key1"));
        Assertions.assertEquals("value2", context.getBaggageItem("key2"));
        
        Map<String, String> baggage = context.getBaggage();
        Assertions.assertEquals(2, baggage.size());
        Assertions.assertTrue(baggage.containsKey("key1"));
        Assertions.assertTrue(baggage.containsKey("key2"));
    }

    @Test
    public void testCreateChild() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        
        SimpleTraceContext parent = new SimpleTraceContext(traceId, spanId, true);
        TraceContext child = parent.createChild("child-span");
        
        Assertions.assertEquals(traceId, child.getTraceId());
        Assertions.assertNotEquals(spanId, child.getSpanId());
        Assertions.assertEquals(spanId, child.getParentSpanId());
    }

    @Test
    public void testToMap() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        context.setBaggageItem("test", "value");
        
        Map<String, Object> map = context.toMap();
        
        Assertions.assertEquals(traceId, map.get("traceId"));
        Assertions.assertEquals(spanId, map.get("spanId"));
        Assertions.assertTrue((Boolean) map.get("sampled"));
        Assertions.assertNotNull(map.get("baggage"));
    }

    @Test
    public void testW3CTraceparent() {
        String traceId = "0123456789abcdef0123456789abcdef";
        String spanId = "0123456789abcdef";
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        String traceparent = context.toW3CTraceparent();
        
        Assertions.assertTrue(traceparent.startsWith("00-"));
        Assertions.assertTrue(traceparent.contains(traceId));
        Assertions.assertTrue(traceparent.contains(spanId));
        Assertions.assertTrue(traceparent.endsWith("-01"));
    }

    @Test
    public void testW3CTracestate() {
        String traceId = SimpleTraceContext.generateTraceId();
        String spanId = SimpleTraceContext.generateSpanId();
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, true);
        context.setBaggageItem("key1", "value1");
        context.setBaggageItem("key2", "value2");
        
        String tracestate = context.toW3CTracestate();
        
        Assertions.assertTrue(tracestate.contains("key1=value1"));
        Assertions.assertTrue(tracestate.contains("key2=value2"));
    }

    @Test
    public void testGenerateTraceId() {
        String traceId1 = SimpleTraceContext.generateTraceId();
        String traceId2 = SimpleTraceContext.generateTraceId();
        
        Assertions.assertNotNull(traceId1);
        Assertions.assertEquals(32, traceId1.length());
        Assertions.assertNotEquals(traceId1, traceId2);
    }

    @Test
    public void testGenerateSpanId() {
        String spanId1 = SimpleTraceContext.generateSpanId();
        String spanId2 = SimpleTraceContext.generateSpanId();
        
        Assertions.assertNotNull(spanId1);
        Assertions.assertEquals(16, spanId1.length());
        Assertions.assertNotEquals(spanId1, spanId2);
    }
}
