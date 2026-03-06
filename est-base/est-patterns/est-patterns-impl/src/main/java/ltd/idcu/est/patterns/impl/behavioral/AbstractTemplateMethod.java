package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.TemplateMethod;

public abstract class AbstractTemplateMethod implements TemplateMethod {
    
    @Override
    public final void templateMethod() {
        primitiveOperation1();
        primitiveOperation2();
        hook();
    }
    
    protected abstract void primitiveOperation1();
    
    protected abstract void primitiveOperation2();
    
    protected void hook() {
    }
}
