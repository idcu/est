package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Implementor;

public class ConcreteImplementorA implements Implementor {
    
    @Override
    public void operationImpl() {
        System.out.println("ConcreteImplementorA: operationImpl");
    }
}
