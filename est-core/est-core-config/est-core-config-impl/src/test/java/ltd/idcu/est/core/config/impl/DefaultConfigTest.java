package ltd.idcu.est.core.config.impl;

import ltd.idcu.est.core.config.api.Config;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Map;
import java.util.Optional;

public class DefaultConfigTest {

    @Test
    public void testConfigCreation() {
        Config config = new DefaultConfig();
        Assertions.assertNotNull(config);
    }

    @Test
    public void testSetAndGetString() {
        Config config = new DefaultConfig();
        config.set("app.name", "TestApp");
        
        String value = config.getString("app.name");
        Assertions.assertEquals("TestApp", value);
    }

    @Test
    public void testGetStringWithDefault() {
        Config config = new DefaultConfig();
        
        String value = config.getString("app.name", "DefaultApp");
        Assertions.assertEquals("DefaultApp", value);
    }

    @Test
    public void testGetOptional() {
        Config config = new DefaultConfig();
        config.set("app.name", "TestApp");
        
        Optional<String> present = config.get("app.name", String.class);
        Assertions.assertTrue(present.isPresent());
        Assertions.assertEquals("TestApp", present.get());
        
        Optional<String> notPresent = config.get("app.version", String.class);
        Assertions.assertFalse(notPresent.isPresent());
    }

    @Test
    public void testSetAndGetInt() {
        Config config = new DefaultConfig();
        config.set("app.port", 8080);
        
        int value = config.getInt("app.port");
        Assertions.assertEquals(8080, value);
    }

    @Test
    public void testGetIntWithDefault() {
        Config config = new DefaultConfig();
        
        int value = config.getInt("app.port", 9090);
        Assertions.assertEquals(9090, value);
    }

    @Test
    public void testSetAndGetLong() {
        Config config = new DefaultConfig();
        config.set("app.timeout", 30000L);
        
        long value = config.getLong("app.timeout");
        Assertions.assertEquals(30000L, value);
    }

    @Test
    public void testGetLongWithDefault() {
        Config config = new DefaultConfig();
        
        long value = config.getLong("app.timeout", 60000L);
        Assertions.assertEquals(60000L, value);
    }

    @Test
    public void testSetAndGetBoolean() {
        Config config = new DefaultConfig();
        config.set("app.debug", true);
        
        boolean value = config.getBoolean("app.debug");
        Assertions.assertTrue(value);
    }

    @Test
    public void testGetBooleanWithDefault() {
        Config config = new DefaultConfig();
        
        boolean value = config.getBoolean("app.debug", false);
        Assertions.assertFalse(value);
    }

    @Test
    public void testSetAndGetDouble() {
        Config config = new DefaultConfig();
        config.set("app.rate", 0.5);
        
        double value = config.getDouble("app.rate");
        Assertions.assertEqualsWithDelta(0.5, value, 0.001);
    }

    @Test
    public void testGetDoubleWithDefault() {
        Config config = new DefaultConfig();
        
        double value = config.getDouble("app.rate", 1.0);
        Assertions.assertEqualsWithDelta(1.0, value, 0.001);
    }

    @Test
    public void testContains() {
        Config config = new DefaultConfig();
        config.set("app.name", "TestApp");
        
        Assertions.assertTrue(config.contains("app.name"));
        Assertions.assertFalse(config.contains("app.version"));
    }

    @Test
    public void testRemove() {
        Config config = new DefaultConfig();
        config.set("app.name", "TestApp");
        
        Assertions.assertTrue(config.contains("app.name"));
        config.remove("app.name");
        Assertions.assertFalse(config.contains("app.name"));
    }

    @Test
    public void testClear() {
        Config config = new DefaultConfig();
        config.set("app.name", "TestApp");
        config.set("app.port", 8080);
        
        Assertions.assertTrue(config.contains("app.name"));
        Assertions.assertTrue(config.contains("app.port"));
        
        config.clear();
        
        Assertions.assertFalse(config.contains("app.name"));
        Assertions.assertFalse(config.contains("app.port"));
    }



    @Test
    public void testNestedKeys() {
        Config config = new DefaultConfig();
        config.set("database.url", "jdbc:mysql://localhost/db");
        config.set("database.username", "root");
        config.set("database.password", "secret");
        
        Assertions.assertEquals("jdbc:mysql://localhost/db", config.getString("database.url"));
        Assertions.assertEquals("root", config.getString("database.username"));
        Assertions.assertEquals("secret", config.getString("database.password"));
    }

    @Test
    public void testEmptyString() {
        Config config = new DefaultConfig();
        config.set("app.description", "");
        
        String value = config.getString("app.description");
        Assertions.assertEquals("", value);
    }

    @Test
    public void testNullValue() {
        Config config = new DefaultConfig();
        config.set("app.name", "TestApp");
        Assertions.assertTrue(config.contains("app.name"));
        
        config.set("app.name", null);
        Assertions.assertFalse(config.contains("app.name"));
    }

    @Test
    public void testGetGeneric() {
        Config config = new DefaultConfig();
        config.set("app.port", 8080);
        
        Integer value = config.get("app.port");
        Assertions.assertEquals(8080, value);
    }

    @Test
    public void testGetGenericWithDefault() {
        Config config = new DefaultConfig();
        
        Integer value = config.get("app.port", 9090);
        Assertions.assertEquals(9090, value);
    }
}
