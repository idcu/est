package ltd.idcu.est.codecli.plugin;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;
import ltd.idcu.est.test.annotation.AfterEach;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class PluginManagerTest {

    private PluginManager pluginManager;
    private Path tempDir;

    @BeforeEach
    void beforeEach() throws Exception {
        tempDir = Files.createTempDirectory("est-test-");
        pluginManager = new PluginManager(tempDir);
    }

    @AfterEach
    void afterEach() throws Exception {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception e) {
                    }
                });
        }
    }

    @Test
    void testPluginManagerInitialization() {
        assertNotNull(pluginManager);
        assertNotNull(pluginManager.getPluginsDir());
        assertTrue(Files.exists(pluginManager.getPluginsDir()));
    }

    @Test
    void testRegisterService() {
        String serviceName = "testService";
        Object service = new Object();
        
        pluginManager.registerService(serviceName, service);
        
        List<EstPlugin> plugins = pluginManager.getAllPlugins();
        assertTrue(plugins.isEmpty());
    }

    @Test
    void testGetAllPluginsEmpty() {
        List<EstPlugin> plugins = pluginManager.getAllPlugins();
        assertNotNull(plugins);
        assertTrue(plugins.isEmpty());
    }

    @Test
    void testGetEnabledPluginsEmpty() {
        List<EstPlugin> plugins = pluginManager.getEnabledPlugins();
        assertNotNull(plugins);
        assertTrue(plugins.isEmpty());
    }

    @Test
    void testGetPluginNonexistent() {
        EstPlugin plugin = pluginManager.getPlugin("nonexistent");
        assertNull(plugin);
    }

    @Test
    void testUnloadAllPluginsEmpty() {
        pluginManager.unloadAllPlugins();
        assertTrue(pluginManager.getAllPlugins().isEmpty());
    }

    @Test
    void testShutdown() {
        pluginManager.shutdown();
        assertTrue(pluginManager.getAllPlugins().isEmpty());
    }

    @Test
    void testLoadAllPluginsEmptyDir() {
        pluginManager.loadAllPlugins();
        assertTrue(pluginManager.getAllPlugins().isEmpty());
    }
}
