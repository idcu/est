package ltd.idcu.est.config.impl;

import ltd.idcu.est.config.api.ConfigVersion;
import ltd.idcu.est.config.api.ConfigVersionManager;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultConfigVersionManagerTest {

    @Test
    public void testCreateVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config = new HashMap<>();
        config.put("app.name", "Test App");
        config.put("app.port", 8080);
        
        String versionId = manager.createVersion(config, "Initial version");
        
        Assertions.assertNotNull(versionId);
        Assertions.assertEquals("v1", versionId);
        
        Optional<ConfigVersion> versionOpt = manager.getVersion(versionId);
        Assertions.assertTrue(versionOpt.isPresent());
        
        ConfigVersion version = versionOpt.get();
        Assertions.assertEquals(versionId, version.getVersionId());
        Assertions.assertEquals("Initial version", version.getComment());
        Assertions.assertNotNull(version.getCreatedAt());
    }

    @Test
    public void testVersionIncrement() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config1 = new HashMap<>();
        config1.put("key", "value1");
        String v1Id = manager.createVersion(config1, "Version 1");
        
        Map<String, Object> config2 = new HashMap<>();
        config2.put("key", "value2");
        String v2Id = manager.createVersion(config2, "Version 2");
        
        Assertions.assertEquals("v1", v1Id);
        Assertions.assertEquals("v2", v2Id);
    }

    @Test
    public void testListVersions() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> config = new HashMap<>();
            config.put("key", "value" + i);
            manager.createVersion(config, "Version " + (i + 1));
        }
        
        List<ConfigVersion> versions = manager.listVersions();
        Assertions.assertEquals(5, versions.size());
    }

    @Test
    public void testGetVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config = new HashMap<>();
        config.put("test.key", "test.value");
        String createdId = manager.createVersion(config, "Test version");
        
        Optional<ConfigVersion> retrievedOpt = manager.getVersion(createdId);
        Assertions.assertTrue(retrievedOpt.isPresent());
        
        ConfigVersion retrieved = retrievedOpt.get();
        Assertions.assertEquals(createdId, retrieved.getVersionId());
        Assertions.assertEquals("test.value", retrieved.getProperties().get("test.key"));
    }

    @Test
    public void testGetCurrentVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        Map<String, Object> config1 = new HashMap<>();
        config1.put("key", "v1");
        manager.createVersion(config1, "v1");
        
        Map<String, Object> config2 = new HashMap<>();
        config2.put("key", "v2");
        manager.createVersion(config2, "v2");
        
        Optional<ConfigVersion> currentOpt = manager.getCurrentVersion();
        Assertions.assertTrue(currentOpt.isPresent());
        
        ConfigVersion current = currentOpt.get();
        Assertions.assertEquals("v2", current.getVersionId());
    }

    @Test
    public void testDeleteVersion() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        manager.createVersion(new HashMap<>(), "v1");
        String v2Id = manager.createVersion(new HashMap<>(), "v2");
        manager.createVersion(new HashMap<>(), "v3");
        
        manager.deleteVersion(v2Id);
        
        List<ConfigVersion> versions = manager.listVersions();
        Assertions.assertEquals(2, versions.size());
    }

    @Test
    public void testClearVersions() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        manager.createVersion(new HashMap<>(), "v1");
        manager.createVersion(new HashMap<>(), "v2");
        
        manager.clearVersions();
        List<ConfigVersion> versions = manager.listVersions();
        Assertions.assertEquals(0, versions.size());
    }

    @Test
    public void testListVersionsWithLimit() {
        ConfigVersionManager manager = new DefaultConfigVersionManager();
        
        for (int i = 0; i < 10; i++) {
            manager.createVersion(new HashMap<>(), "Version " + (i + 1));
        }
        
        List<ConfigVersion> limited = manager.listVersions(3);
        Assertions.assertEquals(3, limited.size());
    }
}
