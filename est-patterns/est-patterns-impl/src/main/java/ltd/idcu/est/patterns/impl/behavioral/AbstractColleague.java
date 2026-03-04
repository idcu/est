package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Colleague;
import ltd.idcu.est.patterns.api.behavioral.Mediator;

public abstract class AbstractColleague implements Colleague {
    
    protected final Mediator mediator;
    protected final String name;
    
    public AbstractColleague(Mediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
        mediator.register(this);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void send(String message) {
        System.out.println(name + " sending: " + message);
        mediator.send(message, this);
    }
}
