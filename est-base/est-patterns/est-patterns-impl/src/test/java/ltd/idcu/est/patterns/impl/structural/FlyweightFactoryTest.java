package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Flyweight;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class FlyweightFactoryTest {

    @Test
    public void testGetFlyweight() {
        FlyweightFactory factory = new FlyweightFactory();
        
        Flyweight flyweight1 = factory.getFlyweight("key1");
        Flyweight flyweight2 = factory.getFlyweight("key1");
        Flyweight flyweight3 = factory.getFlyweight("key2");
        
        Assertions.assertNotNull(flyweight1);
        Assertions.assertNotNull(flyweight2);
        Assertions.assertNotNull(flyweight3);
        
        Assertions.assertSame(flyweight1, flyweight2);
        Assertions.assertNotSame(flyweight1, flyweight3);
    }

    @Test
    public void testGetCount() {
        FlyweightFactory factory = new FlyweightFactory();
        
        Assertions.assertEquals(0, factory.getCount());
        
        factory.getFlyweight("key1");
        Assertions.assertEquals(1, factory.getCount());
        
        factory.getFlyweight("key2");
        Assertions.assertEquals(2, factory.getCount());
        
        factory.getFlyweight("key1");
        Assertions.assertEquals(2, factory.getCount());
    }

    @Test
    public void testFlyweightOperation() {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight flyweight = factory.getFlyweight("test-key");
        
        Assertions.assertNotNull(flyweight);
        flyweight.operation("test-data");
    }
}
