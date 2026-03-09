package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.State;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.List;

public class DefaultStateContextTest {

    @Test
    public void testInitialState() {
        TestState state = new TestState("Initial");
        DefaultStateContext context = new DefaultStateContext(state);
        
        Assertions.assertNotNull(context);
    }

    @Test
    public void testSetState() {
        TestState state1 = new TestState("State 1");
        TestState state2 = new TestState("State 2");
        DefaultStateContext context = new DefaultStateContext(state1);
        
        context.setState(state2);
        
        context.request();
        
        Assertions.assertEquals("State 2", state2.getLastHandled());
    }

    @Test
    public void testRequest() {
        TestState state = new TestState("Test State");
        DefaultStateContext context = new DefaultStateContext(state);
        
        context.request();
        
        Assertions.assertEquals("Test State", state.getLastHandled());
    }

    @Test
    public void testStateTransition() {
        TestState stateA = new TestState("State A");
        TestState stateB = new TestState("State B");
        DefaultStateContext context = new DefaultStateContext(stateA);
        
        context.request();
        Assertions.assertEquals("State A", stateA.getLastHandled());
        Assertions.assertNull(stateB.getLastHandled());
        
        context.setState(stateB);
        context.request();
        Assertions.assertEquals("State B", stateB.getLastHandled());
    }

    private static class TestState extends AbstractState {
        private String lastHandled;

        TestState(String name) {
            super(name);
        }

        @Override
        public void handle() {
            this.lastHandled = getName();
        }

        String getLastHandled() {
            return lastHandled;
        }
    }
}
