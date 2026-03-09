package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.List;

public class DefaultSubjectTest {

    @Test
    public void testAttachAndDetach() {
        DefaultSubject<String> subject = new DefaultSubject<>();
        TestObserver observer = new TestObserver();
        
        Assertions.assertEquals(0, subject.getObserverCount());
        
        subject.attach(observer);
        Assertions.assertEquals(1, subject.getObserverCount());
        
        subject.detach(observer);
        Assertions.assertEquals(0, subject.getObserverCount());
    }

    @Test
    public void testNotifyObservers() {
        DefaultSubject<String> subject = new DefaultSubject<>();
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        
        subject.attach(observer1);
        subject.attach(observer2);
        
        subject.notifyObservers("test-message");
        
        Assertions.assertEquals("test-message", observer1.getLastReceived());
        Assertions.assertEquals("test-message", observer2.getLastReceived());
    }

    @Test
    public void testAttachNullThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DefaultSubject<String> subject = new DefaultSubject<>();
            subject.attach(null);
        });
    }

    private static class TestObserver extends AbstractObserver<String> {
        private String lastReceived;

        @Override
        public void update(String data) {
            this.lastReceived = data;
        }

        String getLastReceived() {
            return lastReceived;
        }
    }
}
