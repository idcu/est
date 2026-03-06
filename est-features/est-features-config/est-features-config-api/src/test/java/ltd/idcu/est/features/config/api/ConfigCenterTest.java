package ltd.idcu.est.features.config.api;

import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ltd.idcu.est.test.Assert.*;

public class ConfigCenterTest {

    @Test
    public void testSetAndGetProperty() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("app.name", "est-app");
        configCenter.setProperty("app.version", "1.0.0");
        
        Optional<Object> name = configCenter.getProperty("app.name");
        Optional<Object> version = configCenter.getProperty("app.version");
        
        assertTrue(name.isPresent());
        assertTrue(version.isPresent());
        assertEquals("est-app", name.get());
        assertEquals("1.0.0", version.get());
    }

    @Test
    public void testGetPropertyWithType() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("timeout", 5000);
        configCenter.setProperty("enabled", true);
        
        Optional<Integer> timeout = configCenter.getProperty("timeout", Integer.class);
        Optional<Boolean> enabled = configCenter.getProperty("enabled", Boolean.class);
        
        assertTrue(timeout.isPresent());
        assertTrue(enabled.isPresent());
        assertEquals(5000, (int) timeout.get());
        assertTrue(enabled.get());
    }

    @Test
    public void testGetTypedProperties() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("string.key", "value");
        configCenter.setProperty("int.key", 42);
        configCenter.setProperty("long.key", 1000L);
        configCenter.setProperty("double.key", 3.14);
        configCenter.setProperty("boolean.key", true);
        
        assertEquals("value", configCenter.getString("string.key", "default"));
        assertEquals(42, configCenter.getInt("int.key", 0));
        assertEquals(1000L, configCenter.getLong("long.key", 0L));
        assertEquals(3.14, configCenter.getDouble("double.key", 0.0), 0.001);
        assertTrue(configCenter.getBoolean("boolean.key", false));
    }

    @Test
    public void testDefaultValues() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        assertEquals("default", configCenter.getString("non.existent", "default"));
        assertEquals(100, configCenter.getInt("non.existent", 100));
        assertFalse(configCenter.getBoolean("non.existent", false));
    }

    @Test
    public void testRemoveProperty() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("test.key", "value");
        assertTrue(configCenter.containsProperty("test.key"));
        
        configCenter.removeProperty("test.key");
        assertFalse(configCenter.containsProperty("test.key"));
    }

    @Test
    public void testGetAllProperties() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("key1", "value1");
        configCenter.setProperty("key2", "value2");
        
        Map<String, Object> all = configCenter.getAllProperties();
        assertEquals(2, all.size());
        assertTrue(all.containsKey("key1"));
        assertTrue(all.containsKey("key2"));
    }

    @Test
    public void testConfigChangeListener() {
        ConfigCenter configCenter = new DefaultConfigCenter();
        List<ConfigChangeEvent> events = new ArrayList<>();
        
        ConfigChangeListener listener = events::add;
        configCenter.addChangeListener(listener);
        
        configCenter.setProperty("test.key", "value1");
        configCenter.setProperty("test.key", "value2");
        configCenter.removeProperty("test.key");
        
        assertEquals(3, events.size());
        assertEquals(ConfigChangeEvent.ChangeType.ADDED, events.get(0).getChangeType());
        assertEquals(ConfigChangeEvent.ChangeType.MODIFIED, events.get(1).getChangeType());
        assertEquals(ConfigChangeEvent.ChangeType.DELETED, events.get(2).getChangeType());
    }
}
