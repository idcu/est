package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class DefaultAdapterTest {

    @Test
    public void testAdapt() {
        DefaultAdapter<Integer, String> adapter = new DefaultAdapter<>(
            Integer.class, 
            String.class, 
            i -> "Number: " + i
        );
        
        String result = adapter.adapt(123);
        Assertions.assertEquals("Number: 123", result);
    }

    @Test
    public void testAdaptNull() {
        DefaultAdapter<Integer, String> adapter = DefaultAdapter.of(
            Integer.class, 
            String.class, 
            i -> "Number: " + i
        );
        
        Assertions.assertNull(adapter.adapt(null));
    }

    @Test
    public void testGetSourceType() {
        DefaultAdapter<Integer, String> adapter = DefaultAdapter.of(
            Integer.class, 
            String.class, 
            i -> String.valueOf(i)
        );
        
        Assertions.assertEquals(Integer.class, adapter.getSourceType());
    }

    @Test
    public void testGetTargetType() {
        DefaultAdapter<Integer, String> adapter = DefaultAdapter.of(
            Integer.class, 
            String.class, 
            i -> String.valueOf(i)
        );
        
        Assertions.assertEquals(String.class, adapter.getTargetType());
    }

    @Test
    public void testOfFactoryMethod() {
        DefaultAdapter<Integer, String> adapter = DefaultAdapter.of(
            Integer.class, 
            String.class, 
            i -> String.valueOf(i)
        );
        
        Assertions.assertNotNull(adapter);
        Assertions.assertEquals("123", adapter.adapt(123));
    }

    @Test
    public void testNullSourceTypeThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultAdapter<>(null, String.class, i -> String.valueOf(i));
        });
    }

    @Test
    public void testNullTargetTypeThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultAdapter<>(Integer.class, null, i -> String.valueOf(i));
        });
    }

    @Test
    public void testNullAdapterFunctionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultAdapter<>(Integer.class, String.class, null);
        });
    }
}
