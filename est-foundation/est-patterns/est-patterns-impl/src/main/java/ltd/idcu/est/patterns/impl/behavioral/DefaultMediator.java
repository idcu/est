package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Colleague;
import ltd.idcu.est.patterns.api.behavioral.Mediator;

import java.util.ArrayList;
import java.util.List;

public class DefaultMediator implements Mediator {
    
    private final List<Colleague> colleagues = new ArrayList<>();
    
    @Override
    public void register(Colleague colleague) {
        colleagues.add(colleague);
    }
    
    @Override
    public void send(String message, Colleague sender) {
        for (Colleague colleague : colleagues) {
            if (colleague != sender) {
                colleague.receive(message);
            }
        }
    }
}
