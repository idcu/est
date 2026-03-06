package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Implementor;

public class ConcreteImplementorB implements Implementor {
    
    @Override
    public void operationImpl() {
        System.out.println("ConcreteImplementorB: operationImpl");
    }
}
