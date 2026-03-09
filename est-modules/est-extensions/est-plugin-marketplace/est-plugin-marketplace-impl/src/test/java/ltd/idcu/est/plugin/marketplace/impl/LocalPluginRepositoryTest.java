package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.api.PluginInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LocalPluginRepositoryTest {

    private LocalPluginRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LocalPluginRepository("test-repo", "Test Repository", "https://test.example.com");
    }

    @AfterEach
    void tearDown() {
        repository = null;
    }

    @Test
    void testGetId() {
        assertEquals("test-repo", repository.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Test Repository", repository.getName());
    }

    @Test
    void testGetUrl() {
        assertEquals("https://test.example.com", repository.getUrl());
    }

    @Test
    void testIsEnabledByDefault() {
        assertTrue(repository.isEnabled());
    }

    @Test
    void testSetEnabled() {
        repository.setEnabled(false);
        assertFalse(repository.isEnabled());
        
        repository.setEnabled(true);
        assertTrue(repository.isEnabled());
    }

    @Test
    void testGetPluginNonExistent() {
        Optional<PluginInfo> plugin = repository.getPlugin("nonexistent");
        assertTrue(plugin.isEmpty());
    }

    @Test
    void testAddAndGetPlugin() {
        PluginInfo plugin = PluginInfo.builder()
            .name("test-plugin")
            .version("1.0.0")
            .description("Test plugin")
            .author("Test Author")
            .mainClass("com.example.TestPlugin")
            .category("tools")
            .tags("test", "utility")
            .rating(4.5)
            .downloadCount(100)
            .publishTime(System.currentTimeMillis())
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .build();
        
        repository.addPlugin(plugin);
        
        Optional<PluginInfo> retrieved = repository.getPlugin("test-plugin");
        assertTrue(retrieved.isPresent());
        assertEquals("test-plugin", retrieved.get().getName());
        assertEquals("1.0.0", retrieved.get().getVersion());
    }

    @Test
    void testGetPluginByVersion() {
        PluginInfo plugin1 = PluginInfo.builder()
            .name("versioned-plugin")
            .version("1.0.0")
            .description("Version 1")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        PluginInfo plugin2 = PluginInfo.builder()
            .name("versioned-plugin")
            .version("2.0.0")
            .description("Version 2")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        repository.addPlugin(plugin1);
        repository.addPlugin(plugin2);
        
        Optional<PluginInfo> v1 = repository.getPlugin("versioned-plugin", "1.0.0");
        Optional<PluginInfo> v2 = repository.getPlugin("versioned-plugin", "2.0.0");
        Optional<PluginInfo> v3 = repository.getPlugin("versioned-plugin", "3.0.0");
        
        assertTrue(v1.isPresent());
        assertEquals("1.0.0", v1.get().getVersion());
        assertTrue(v2.isPresent());
        assertEquals("2.0.0", v2.get().getVersion());
        assertTrue(v3.isEmpty());
    }

    @Test
    void testGetAllPlugins() {
        PluginInfo plugin1 = PluginInfo.builder()
            .name("plugin1")
            .version("1.0.0")
            .description("Plugin 1")
            .author("Author")
            .mainClass("com.example.Plugin1")
            .build();
        
        PluginInfo plugin2 = PluginInfo.builder()
            .name("plugin2")
            .version("1.0.0")
            .description("Plugin 2")
            .author("Author")
            .mainClass("com.example.Plugin2")
            .build();
        
        repository.addPlugin(plugin1);
        repository.addPlugin(plugin2);
        
        List<PluginInfo> all = repository.getAllPlugins();
        assertEquals(2, all.size());
    }

    @Test
    void testSearchPlugins() {
        PluginInfo plugin1 = PluginInfo.builder()
            .name("database-plugin")
            .version("1.0.0")
            .description("Database connectivity plugin")
            .author("Author")
            .mainClass("com.example.DbPlugin")
            .tags("database", "sql")
            .build();
        
        PluginInfo plugin2 = PluginInfo.builder()
            .name("web-plugin")
            .version("1.0.0")
            .description("Web framework plugin")
            .author("Author")
            .mainClass("com.example.WebPlugin")
            .tags("web", "http")
            .build();
        
        repository.addPlugin(plugin1);
        repository.addPlugin(plugin2);
        
        List<PluginInfo> results = repository.searchPlugins("database");
        assertEquals(1, results.size());
        assertEquals("database-plugin", results.get(0).getName());
        
        List<PluginInfo> results2 = repository.searchPlugins("web");
        assertEquals(1, results2.size());
        assertEquals("web-plugin", results2.get(0).getName());
    }

    @Test
    void testGetPluginVersions() {
        PluginInfo v1 = PluginInfo.builder()
            .name("multi-version")
            .version("1.0.0")
            .description("v1")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        PluginInfo v2 = PluginInfo.builder()
            .name("multi-version")
            .version("2.0.0")
            .description("v2")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        repository.addPlugin(v1);
        repository.addPlugin(v2);
        
        List<PluginInfo> versions = repository.getPluginVersions("multi-version");
        assertEquals(2, versions.size());
    }

    @Test
    void testDownloadPlugin() {
        PluginInfo plugin = PluginInfo.builder()
            .name("downloadable")
            .version("1.0.0")
            .description("Downloadable")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        repository.addPlugin(plugin);
        
        boolean result = repository.downloadPlugin("downloadable", "1.0.0", "/target/path");
        assertTrue(result);
    }

    @Test
    void testUploadPlugin() {
        PluginInfo plugin = PluginInfo.builder()
            .name("uploaded")
            .version("1.0.0")
            .description("Uploaded")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        boolean result = repository.uploadPlugin(plugin, new byte[0]);
        assertTrue(result);
        
        Optional<PluginInfo> retrieved = repository.getPlugin("uploaded");
        assertTrue(retrieved.isPresent());
    }

    @Test
    void testDeletePlugin() {
        PluginInfo plugin = PluginInfo.builder()
            .name("to-delete")
            .version("1.0.0")
            .description("To delete")
            .author("Author")
            .mainClass("com.example.Plugin")
            .build();
        
        repository.addPlugin(plugin);
        assertTrue(repository.getPlugin("to-delete").isPresent());
        
        boolean result = repository.deletePlugin("to-delete");
        assertTrue(result);
        assertTrue(repository.getPlugin("to-delete").isEmpty());
    }

    @Test
    void testDeleteNonExistentPlugin() {
        boolean result = repository.deletePlugin("nonexistent");
        assertFalse(result);
    }
}
