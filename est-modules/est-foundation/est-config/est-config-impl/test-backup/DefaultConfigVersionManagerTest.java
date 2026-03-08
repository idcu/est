package ltd.idcu.est.config.impl;

import ltd.idcu.est.config.api.ConfigVersion;
import ltd.idcu.est.config.api.ConfigVersionManager;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConfigVersionManagerTest {

    @Test
    public void testCreateVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config = new HashMap<>();
        config.put("app.name", "Test App");
        config.put("app.port", 8080);
        
        ConfigVersion version = manager.createVersion("test-config", config, "Initial version");
        
        Assertions.assertNotNull(version);
        Assertions.assertEquals("v1", version.getVersionId());
        Assertions.assertEquals("test-config", version.getConfigId());
        Assertions.assertEquals("Initial version", version.getDescription());
        Assertions.assertNotNull(version.getCreatedAt());
    }

    @Test
    public void testVersionIncrement() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config1 = new HashMap<>();
        config1.put("key", "value1");
        ConfigVersion v1 = manager.createVersion("test-config", config1, "Version 1");
        
        Map<String, Object> config2 = new HashMap<>();
        config2.put("key", "value2");
        ConfigVersion v2 = manager.createVersion("test-config", config2, "Version 2");
        
        Assertions.assertEquals("v1", v1.getVersionId());
        Assertions.assertEquals("v2", v2.getVersionId());
    }

    @Test
    public void testListVersions() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> config = new HashMap<>();
            config.put("key", "value" + i);
            manager.createVersion("test-config", config, "Version " + (i + 1));
        }
        
        List<ConfigVersion> versions = manager.listVersions("test-config", 0, 10);
        Assertions.assertEquals(5, versions.size());
        Assertions.assertEquals("v5", versions.get(0).getVersionId());
        Assertions.assertEquals("v1", versions.get(4).getVersionId());
    }

    @Test
    public void testGetVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config = new HashMap<>();
        config.put("test.key", "test.value");
        ConfigVersion created = manager.createVersion("test-config", config, "Test version");
        
        ConfigVersion retrieved = manager.getVersion("test-config", "v1");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals(created.getVersionId(), retrieved.getVersionId());
        Assertions.assertEquals(created.getConfig().get("test.key"), retrieved.getConfig().get("test.key"));
    }

    @Test
    public void testRollback() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config1 = new HashMap<>();
        config1.put("app.name", "App v1");
        manager.createVersion("test-config", config1, "Version 1");
        
        Map<String, Object> config2 = new HashMap<>();
        config2.put("app.name", "App v2");
        manager.createVersion("test-config", config2, "Version 2");
        
        ConfigVersion rolledBack = manager.rollback("test-config", "v1");
        Assertions.assertNotNull(rolledBack);
        Assertions.assertEquals("v3", rolledBack.getVersionId());
        Assertions.assertEquals("App v1", rolledBack.getConfig().get("app.name"));
    }

    @Test
    public void testGetCurrentVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config1 = new HashMap<>();
        config1.put("key", "v1");
        manager.createVersion("test-config", config1, "v1");
        
        Map<String, Object> config2 = new HashMap<>();
        config2.put("key", "v2");
        manager.createVersion("test-config", config2, "v2");
        
        ConfigVersion current = manager.getCurrentVersion("test-config");
        Assertions.assertNotNull(current);
        Assertions.assertEquals("v2", current.getVersionId());
    }

    @Test
    public void testDeleteVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        manager.createVersion("test-config", new HashMap<>(), "v1");
        manager.createVersion("test-config", new HashMap<>(), "v2");
        manager.createVersion("test-config", new HashMap<>(), "v3");
        
        boolean deleted = manager.deleteVersion("test-config", "v2");
        Assertions.assertTrue(deleted);
        
        List<ConfigVersion> versions = manager.listVersions("test-config", 0, 10);
        Assertions.assertEquals(2, versions.size());
    }

    @Test
    public void testClearVersions() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        manager.createVersion("test-config", new HashMap<>(), "v1");
        manager.createVersion("test-config", new HashMap<>(), "v2");
        
        manager.clearVersions("test-config");
        List<ConfigVersion> versions = manager.listVersions("test-config", 0, 10);
        Assertions.assertEquals(0, versions.size());
    }

    @Test
    public void testPagination() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        for (int i = 0; i < 10; i++) {
            manager.createVersion("test-config", new HashMap<>(), "Version " + (i + 1));
        }
        
        List<ConfigVersion> page1 = manager.listVersions("test-config", 0, 3);
        Assertions.assertEquals(3, page1.size());
        Assertions.assertEquals("v10", page1.get(0).getVersionId());
        
        List<ConfigVersion> page2 = manager.listVersions("test-config", 3, 3);
        Assertions.assertEquals(3, page2.size());
        Assertions.assertEquals("v7", page2.get(0).getVersionId());
    }
}
