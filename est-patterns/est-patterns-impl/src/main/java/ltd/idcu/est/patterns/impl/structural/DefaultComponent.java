package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Component;

public class DefaultComponent implements Component {
    
    private final String name;
    
    public DefaultComponent(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void operation() {
        System.out.println("Component " + name + " performing operation");
    }
}
