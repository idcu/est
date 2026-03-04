package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Implementor;

public class RefinedAbstraction extends AbstractAbstraction {
    
    public RefinedAbstraction(Implementor implementor) {
        super(implementor);
    }
    
    @Override
    public void operation() {
        System.out.println("RefinedAbstraction: operation");
        implementor.operationImpl();
    }
}
