package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Flyweight;

public class DefaultFlyweight implements Flyweight {
    
    private final String intrinsicState;
    
    public DefaultFlyweight(String intrinsicState) {
        this.intrinsicState = intrinsicState;
    }
    
    @Override
    public void operation(String extrinsicState) {
        System.out.println("Flyweight with intrinsic state: " + intrinsicState + 
            ", extrinsic state: " + extrinsicState);
    }
    
    @Override
    public String getIntrinsicState() {
        return intrinsicState;
    }
}
