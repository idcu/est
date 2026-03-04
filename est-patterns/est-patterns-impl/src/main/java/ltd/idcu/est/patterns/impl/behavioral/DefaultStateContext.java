package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.State;
import ltd.idcu.est.patterns.api.behavioral.StateContext;

public class DefaultStateContext implements StateContext {
    
    private State state;
    
    public DefaultStateContext(State initialState) {
        this.state = initialState;
    }
    
    @Override
    public void setState(State state) {
        this.state = state;
        System.out.println("State changed to: " + state.getName());
    }
    
    @Override
    public void request() {
        state.handle();
    }
}
