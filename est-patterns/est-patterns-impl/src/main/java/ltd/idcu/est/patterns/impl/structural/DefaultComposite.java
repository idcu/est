package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Component;
import ltd.idcu.est.patterns.api.structural.Composite;

import java.util.ArrayList;
import java.util.List;

public class DefaultComposite implements Composite {
    
    private final String name;
    private final List<Component> children = new ArrayList<>();
    
    public DefaultComposite(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void operation() {
        System.out.println("Composite " + name + " performing operation");
        for (Component child : children) {
            child.operation();
        }
    }
    
    @Override
    public void add(Component component) {
        children.add(component);
    }
    
    @Override
    public void remove(Component component) {
        children.remove(component);
    }
    
    @Override
    public Component getChild(int index) {
        return children.get(index);
    }
}
