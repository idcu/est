package ltd.idcu.est.patterns.api.structural;

public interface Composite extends Component {
    
    void add(Component component);
    
    void remove(Component component);
    
    Component getChild(int index);
}
