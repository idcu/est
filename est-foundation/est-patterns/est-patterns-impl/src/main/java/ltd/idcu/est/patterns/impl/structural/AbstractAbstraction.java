package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Abstraction;
import ltd.idcu.est.patterns.api.structural.Implementor;

public abstract class AbstractAbstraction implements Abstraction {
    
    protected Implementor implementor;
    
    public AbstractAbstraction(Implementor implementor) {
        this.implementor = implementor;
    }
    
    public void setImplementor(Implementor implementor) {
        this.implementor = implementor;
    }
}
