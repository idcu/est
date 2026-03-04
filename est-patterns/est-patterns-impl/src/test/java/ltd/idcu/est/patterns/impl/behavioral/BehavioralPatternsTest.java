package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BehavioralPatternsTest {

    @Test
    public void testStrategyExecute() {
        DefaultStrategy<Integer, String> strategy = DefaultStrategy.of(
            "numberToString",
            num -> "Number: " + num
        );
        
        String result = strategy.execute(42);
        
        Assertions.assertEquals("Number: 42", result);
        Assertions.assertEquals("numberToString", strategy.getName());
    }

    @Test
    public void testStrategyNullName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultStrategy<>(null, num -> num.toString());
        });
    }

    @Test
    public void testStrategyEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultStrategy<>("", num -> num.toString());
        });
    }

    @Test
    public void testStrategyNullFunction() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultStrategy<>("test", null);
        });
    }

    @Test
    public void testSubjectAttachObserver() {
        DefaultSubject<String> subject = new DefaultSubject<>();
        TestObserver observer = new TestObserver();
        
        subject.attach(observer);
        
        Assertions.assertEquals(1, subject.getObserverCount());
    }

    @Test
    public void testSubjectAttachNullObserver() {
        DefaultSubject<String> subject = new DefaultSubject<>();
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            subject.attach(null);
        });
    }

    @Test
    public void testSubjectNotifyObservers() {
        DefaultSubject<String> subject = new DefaultSubject<>();
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        
        subject.attach(observer1);
        subject.attach(observer2);
        subject.notifyObservers("test data");
        
        Assertions.assertEquals(1, observer1.receivedData.size());
        Assertions.assertEquals("test data", observer1.receivedData.get(0));
        Assertions.assertEquals(1, observer2.receivedData.size());
        Assertions.assertEquals("test data", observer2.receivedData.get(0));
    }

    @Test
    public void testSubjectDetachObserver() {
        DefaultSubject<String> subject = new DefaultSubject<>();
        TestObserver observer1 = new TestObserver("id1");
        TestObserver observer2 = new TestObserver("id2");
        
        subject.attach(observer1);
        subject.attach(observer2);
        Assertions.assertEquals(2, subject.getObserverCount());
        
        subject.detach(observer1);
        Assertions.assertEquals(1, subject.getObserverCount());
        
        subject.notifyObservers("test");
        Assertions.assertEquals(0, observer1.receivedData.size());
        Assertions.assertEquals(1, observer2.receivedData.size());
    }

    @Test
    public void testStrategyContext() {
        DefaultStrategyContext<Integer, String> context = new DefaultStrategyContext<>();
        DefaultStrategy<Integer, String> strategy1 = DefaultStrategy.of("addOne", num -> "Result: " + (num + 1));
        DefaultStrategy<Integer, String> strategy2 = DefaultStrategy.of("multiplyByTwo", num -> "Result: " + (num * 2));
        
        context.registerStrategy("addOne", strategy1);
        context.registerStrategy("multiplyByTwo", strategy2);
        
        String result1 = context.execute("addOne", 5);
        Assertions.assertEquals("Result: 6", result1);
        
        String result2 = context.execute("multiplyByTwo", 5);
        Assertions.assertEquals("Result: 10", result2);
    }

    @Test
    public void testCommandInvoker() {
        DefaultCommandInvoker invoker = new DefaultCommandInvoker();
        AtomicInteger counter = new AtomicInteger(0);
        
        AbstractCommand incrementCommand = new AbstractCommand("increment") {
            @Override
            protected void doExecute() {
                counter.incrementAndGet();
            }
            
            @Override
            protected void doUndo() {
                counter.decrementAndGet();
            }
        };
        
        invoker.executeCommand(incrementCommand);
        invoker.executeCommand(incrementCommand);
        
        Assertions.assertEquals(2, counter.get());
        Assertions.assertTrue(invoker.canUndo());
        Assertions.assertFalse(invoker.canRedo());
        
        invoker.undoLast();
        Assertions.assertEquals(1, counter.get());
        Assertions.assertTrue(invoker.canUndo());
        Assertions.assertTrue(invoker.canRedo());
        
        invoker.redoLast();
        Assertions.assertEquals(2, counter.get());
    }

    private static class TestObserver implements Observer<String> {
        private final String id;
        public final List<String> receivedData = new ArrayList<>();

        public TestObserver() {
            this(null);
        }

        public TestObserver(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void update(String data) {
            receivedData.add(data);
        }
    }
}
