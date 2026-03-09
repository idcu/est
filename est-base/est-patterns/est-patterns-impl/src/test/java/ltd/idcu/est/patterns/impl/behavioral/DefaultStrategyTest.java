package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class DefaultStrategyTest {

    @Test
    public void testExecute() {
        DefaultStrategy<Integer, Integer> doubleStrategy = new DefaultStrategy<>(
            "double", 
            i -> i * 2
        );
        
        Assertions.assertEquals(10, doubleStrategy.execute(5).intValue());
    }

    @Test
    public void testGetName() {
        DefaultStrategy<Integer, Integer> strategy = DefaultStrategy.of(
            "test-strategy", 
            i -> i
        );
        
        Assertions.assertEquals("test-strategy", strategy.getName());
    }

    @Test
    public void testOfFactoryMethod() {
        DefaultStrategy<Integer, String> strategy = DefaultStrategy.of(
            "toString", 
            i -> String.valueOf(i)
        );
        
        Assertions.assertNotNull(strategy);
        Assertions.assertEquals("123", strategy.execute(123));
    }

    @Test
    public void testNullNameThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultStrategy<>(null, i -> i);
        });
    }

    @Test
    public void testEmptyNameThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultStrategy<>("", i -> i);
        });
    }

    @Test
    public void testNullFunctionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultStrategy<>("test", null);
        });
    }
}
