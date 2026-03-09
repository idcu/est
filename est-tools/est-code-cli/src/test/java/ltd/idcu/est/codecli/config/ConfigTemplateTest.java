package ltd.idcu.est.codecli.config;

import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assertions.*;

public class ConfigTemplateTest {
    
    @Test
    void testCreateDefaultTemplate() {
        ConfigTemplate template = ConfigTemplate.createDefault();
        
        assertEquals("default", template.getId());
        assertNotNull(template.getName());
        assertNotNull(template.getDescription());
        assertNotNull(template.getDefaults());
    }
    
    @Test
    void testCreateMinimalTemplate() {
        ConfigTemplate template = ConfigTemplate.createMinimal();
        
        assertEquals("minimal", template.getId());
        assertNotNull(template.getName());
        assertNotNull(template.getDescription());
    }
    
    @Test
    void testCreateDeveloperTemplate() {
        ConfigTemplate template = ConfigTemplate.createDeveloper();
        
        assertEquals("developer", template.getId());
        assertNotNull(template.getName());
        assertNotNull(template.getDescription());
        
        Object planningMode = template.getDefaults().get("planningMode");
        assertNotNull(planningMode);
    }
    
    @Test
    void testCreateSecureTemplate() {
        ConfigTemplate template = ConfigTemplate.createSecure();
        
        assertEquals("secure", template.getId());
        assertNotNull(template.getName());
        assertNotNull(template.getDescription());
        
        Object hitlEnabled = template.getDefaults().get("hitlEnabled");
        assertNotNull(hitlEnabled);
        assertTrue((Boolean) hitlEnabled);
    }
    
    @Test
    void testGetAllTemplates() {
        var templates = ConfigTemplate.getAllTemplates();
        
        assertNotNull(templates);
        assertEquals(4, templates.size());
        
        var ids = templates.stream()
            .map(ConfigTemplate::getId)
            .toList();
        
        assertTrue(ids.contains("default"));
        assertTrue(ids.contains("minimal"));
        assertTrue(ids.contains("developer"));
        assertTrue(ids.contains("secure"));
    }
    
    @Test
    void testTemplateSetAndGet() {
        ConfigTemplate template = new ConfigTemplate("test", "Test Template");
        
        template.setDefault("key1", "value1");
        template.setDefault("key2", 123);
        template.setDefault("key3", true);
        
        assertEquals("value1", template.getDefaults().get("key1"));
        assertEquals(123, template.getDefaults().get("key2"));
        assertEquals(true, template.getDefaults().get("key3"));
    }
    
    @Test
    void testApplyTemplateToConfig() {
        ConfigTemplate template = ConfigTemplate.createDeveloper();
        CliConfig config = new CliConfig();
        
        String originalNickname = config.getNickname();
        config.applyTemplate(template);
        
        Object newNickname = template.getDefaults().get("nickname");
        if (newNickname != null) {
            assertEquals(newNickname, config.getNickname());
        }
        
        Object planningMode = template.getDefaults().get("planningMode");
        if (planningMode != null) {
            assertEquals(planningMode, config.isPlanningMode());
        }
    }
    
    @Test
    void testFromTemplate() {
        ConfigTemplate template = ConfigTemplate.createSecure();
        CliConfig config = CliConfig.fromTemplate(template);
        
        assertNotNull(config);
        
        Object hitlEnabled = template.getDefaults().get("hitlEnabled");
        if (hitlEnabled != null) {
            assertEquals(hitlEnabled, config.isHitlEnabled());
        }
    }
    
    @Test
    void testTemplateChaining() {
        ConfigTemplate template = new ConfigTemplate("chain", "Chain Test")
            .setDefault("key1", "value1")
            .setDefault("key2", "value2")
            .setDefault("key3", "value3");
        
        assertEquals(3, template.getDefaults().size());
        assertEquals("value1", template.getDefaults().get("key1"));
        assertEquals("value2", template.getDefaults().get("key2"));
        assertEquals("value3", template.getDefaults().get("key3"));
    }
    
    @Test
    void testTemplateGetters() {
        ConfigTemplate template = new ConfigTemplate("test-id", "Test Name");
        template.setDescription("Test Description");
        
        assertEquals("test-id", template.getId());
        assertEquals("Test Name", template.getName());
        assertEquals("Test Description", template.getDescription());
    }
}
