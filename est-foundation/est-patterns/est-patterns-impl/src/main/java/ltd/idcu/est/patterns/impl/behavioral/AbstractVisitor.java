package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Visitor;
import ltd.idcu.est.patterns.api.behavioral.Visitable;

public abstract class AbstractVisitor implements Visitor {
    
    protected final String name;
    
    public AbstractVisitor(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
