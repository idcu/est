package ltd.idcu.est.event.local;

import ltd.idcu.est.event.api.Event;
import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventConfig;
import ltd.idcu.est.event.api.EventListener;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalEventBusTest {

    @Test
    public void testPublishAndSubscribe() {
        EventBus bus = LocalEvents.newLocalEventBus();
        List<String> receivedEvents = new ArrayList<>();
        
        bus.subscribe("test.event", (event, data) -> {
            receivedEvents.add(data);
        });
        
        bus.publish("test.event", "Hello World");
        
        Assertions.assertEquals(1, receivedEvents.size());
        Assertions.assertEquals("Hello World", receivedEvents.get(0));
    }

    @Test
    public void testSubscribeWithConsumer() {
        EventBus bus = LocalEvents.newLocalEventBus();
        List<String> receivedEvents = new ArrayList<>();
        
        LocalEvents.subscribe(bus, "test.event", data -> {
            receivedEvents.add(data);
        });
        
        bus.publish("test.event", "Hello Consumer");
        
        Assertions.assertEquals(1, receivedEvents.size());
        Assertions.assertEquals("Hello Consumer", receivedEvents.get(0));
    }

    @Test
    public void testMultipleSubscribers() {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger count1 = new AtomicInteger(0);
        AtomicInteger count2 = new AtomicInteger(0);
        
        bus.subscribe("test.event", (event, data) -> count1.incrementAndGet());
        bus.subscribe("test.event", (event, data) -> count2.incrementAndGet());
        
        bus.publish("test.event", "data");
        
        Assertions.assertEquals(1, count1.get());
        Assertions.assertEquals(1, count2.get());
    }

    @Test
    public void testUnsubscribe() {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger count = new AtomicInteger(0);
        
        EventListener<String> listener = (event, data) -> count.incrementAndGet();
        bus.subscribe("test.event", listener);
        
        bus.publish("test.event", "data1");
        Assertions.assertEquals(1, count.get());
        
        bus.unsubscribe("test.event", listener);
        
        bus.publish("test.event", "data2");
        Assertions.assertEquals(1, count.get());
    }

    @Test
    public void testUnsubscribeAll() {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger count = new AtomicInteger(0);
        
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        
        bus.publish("test.event", "data1");
        Assertions.assertEquals(2, count.get());
        
        bus.unsubscribeAll("test.event");
        
        bus.publish("test.event", "data2");
        Assertions.assertEquals(2, count.get());
    }

    @Test
    public void testDifferentEventTypes() {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger event1Count = new AtomicInteger(0);
        AtomicInteger event2Count = new AtomicInteger(0);
        
        bus.subscribe("event.type1", (event, data) -> event1Count.incrementAndGet());
        bus.subscribe("event.type2", (event, data) -> event2Count.incrementAndGet());
        
        bus.publish("event.type1", "data");
        Assertions.assertEquals(1, event1Count.get());
        Assertions.assertEquals(0, event2Count.get());
        
        bus.publish("event.type2", "data");
        Assertions.assertEquals(1, event1Count.get());
        Assertions.assertEquals(1, event2Count.get());
    }

    @Test
    public void testPublishWithSource() {
        EventBus bus = LocalEvents.newLocalEventBus();
        Object[] receivedSource = new Object[1];
        
        bus.subscribe("test.event", (event, data) -> {
            receivedSource[0] = event.getSource();
        });
        
        Object source = new Object();
        bus.publish("test.event", "data", source);
        
        Assertions.assertSame(source, receivedSource[0]);
    }

    @Test
    public void testPublishNullEventTypeThrowsException() {
        EventBus bus = LocalEvents.newLocalEventBus();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bus.publish(null, "data");
        });
    }

    @Test
    public void testPublishEmptyEventTypeThrowsException() {
        EventBus bus = LocalEvents.newLocalEventBus();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bus.publish("", "data");
        });
    }

    @Test
    public void testPublishAsync() throws Exception {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger count = new AtomicInteger(0);
        
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        
        CompletableFuture<Void> future = bus.publishAsync("test.event", "data");
        future.get();
        
        Assertions.assertEquals(1, count.get());
    }

    @Test
    public void testPublishAsyncWithSource() throws Exception {
        EventBus bus = LocalEvents.newLocalEventBus();
        Object[] receivedSource = new Object[1];
        
        bus.subscribe("test.event", (event, data) -> {
            receivedSource[0] = event.getSource();
        });
        
        Object source = new Object();
        CompletableFuture<Void> future = bus.publishAsync("test.event", "data", source);
        future.get();
        
        Assertions.assertSame(source, receivedSource[0]);
    }

    @Test
    public void testPublishAndWait() throws Exception {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger count = new AtomicInteger(0);
        
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        
        CompletableFuture<String> future = LocalEvents.publishAndWait(bus, "test.event", "data");
        String result = future.get();
        
        Assertions.assertEquals(1, count.get());
        Assertions.assertEquals("data", result);
    }

    @Test
    public void testListenerPriority() {
        EventBus bus = LocalEvents.newLocalEventBus();
        List<String> order = new ArrayList<>();
        
        bus.subscribe("test.event", 2, (event, data) -> order.add("low"));
        bus.subscribe("test.event", 1, (event, data) -> order.add("high"));
        
        bus.publish("test.event", "data");
        
        Assertions.assertEquals(2, order.size());
        Assertions.assertEquals("high", order.get(0));
        Assertions.assertEquals("low", order.get(1));
    }

    @Test
    public void testSubscribeWithPriorityConsumer() {
        EventBus bus = LocalEvents.newLocalEventBus();
        List<String> order = new ArrayList<>();
        
        LocalEvents.subscribe(bus, "test.event", 2, data -> order.add("low"));
        LocalEvents.subscribe(bus, "test.event", 1, data -> order.add("high"));
        
        bus.publish("test.event", "data");
        
        Assertions.assertEquals(2, order.size());
        Assertions.assertEquals("high", order.get(0));
        Assertions.assertEquals("low", order.get(1));
    }

    @Test
    public void testBuilder() {
        EventBus bus = LocalEvents.builder()
                .maxListenersPerEvent(500)
                .propagateExceptions(true)
                .build();
        
        AtomicInteger count = new AtomicInteger(0);
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        
        bus.publish("test.event", "data");
        Assertions.assertEquals(1, count.get());
    }

    @Test
    public void testClear() {
        EventBus bus = LocalEvents.newLocalEventBus();
        AtomicInteger count = new AtomicInteger(0);
        
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        bus.subscribe("another.event", (event, data) -> count.incrementAndGet());
        
        bus.publish("test.event", "data");
        Assertions.assertEquals(1, count.get());
        
        bus.clear();
        
        bus.publish("test.event", "data");
        bus.publish("another.event", "data");
        Assertions.assertEquals(1, count.get());
    }

    @Test
    public void testStats() {
        EventBus bus = LocalEvents.newLocalEventBus();
        
        bus.subscribe("test.event", (event, data) -> {});
        bus.publish("test.event", "data1");
        bus.publish("test.event", "data2");
        bus.publish("nonexistent.event", "data");
        
        Assertions.assertEquals(3, bus.getStats().getPublishedCount());
    }

    @Test
    public void testWithConfig() {
        EventConfig config = EventConfig.defaultConfig()
                .setMaxListenersPerEvent(500)
                .setPropagateExceptions(true);
        
        EventBus bus = LocalEvents.newLocalEventBus(config);
        
        AtomicInteger count = new AtomicInteger(0);
        bus.subscribe("test.event", (event, data) -> count.incrementAndGet());
        
        bus.publish("test.event", "data");
        Assertions.assertEquals(1, count.get());
    }
}
