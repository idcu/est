package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.marketplace.api.PluginMarketplace;
import ltd.idcu.est.plugin.marketplace.api.PluginRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultPluginMarketplaceTest {

    private PluginMarketplace marketplace;
    private LocalPluginRepository repo1;
    private LocalPluginRepository repo2;

    @BeforeEach
    void setUp() {
        repo1 = new LocalPluginRepository("repo1", "Repository 1", "https://repo1.example.com");
        repo2 = new LocalPluginRepository("repo2", "Repository 2", "https://repo2.example.com");
        
        marketplace = new DefaultPluginMarketplace.Builder()
            .addRepository(repo1)
            .addRepository(repo2)
            .localCacheDirectory("/tmp/cache")
            .build();
        
        addTestPlugins();
    }

    @AfterEach
    void tearDown() {
        marketplace = null;
        repo1 = null;
        repo2 = null;
    }

    private void addTestPlugins() {
        PluginInfo plugin1 = PluginInfo.builder()
            .name("database-plugin")
            .version("1.0.0")
            .description("Database connectivity plugin")
            .author("Author 1")
            .mainClass("com.example.DbPlugin")
            .category("database")
            .tags("database", "sql", "jdbc")
            .rating(4.5)
            .downloadCount(1000)
            .publishTime(System.currentTimeMillis() - 86400000)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .build();
        
        PluginInfo plugin2 = PluginInfo.builder()
            .name("web-plugin")
            .version("2.0.0")
            .description("Web framework plugin")
            .author("Author 2")
            .mainClass("com.example.WebPlugin")
            .category("web")
            .tags("web", "http", "rest")
            .rating(3.8)
            .downloadCount(500)
            .publishTime(System.currentTimeMillis() - 172800000)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(false)
            .build();
        
        PluginInfo plugin3 = PluginInfo.builder()
            .name("security-plugin")
            .version("1.5.0")
            .description("Security plugin")
            .author("Author 3")
            .mainClass("com.example.SecurityPlugin")
            .category("security")
            .tags("security", "auth", "encryption")
            .rating(4.9)
            .downloadCount(2000)
            .publishTime(System.currentTimeMillis() - 43200000)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .build();
        
        repo1.addPlugin(plugin1);
        repo1.addPlugin(plugin2);
        repo2.addPlugin(plugin3);
    }

    @Test
    void testGetPlugin() {
        Optional<PluginInfo> plugin = marketplace.getPlugin("database-plugin");
        assertTrue(plugin.isPresent());
        assertEquals("database-plugin", plugin.get().getName());
    }

    @Test
    void testGetPluginNonExistent() {
        Optional<PluginInfo> plugin = marketplace.getPlugin("nonexistent-plugin");
        assertTrue(plugin.isEmpty());
    }

    @Test
    void testSearchPlugins() {
        List<PluginInfo> results = marketplace.searchPlugins("security");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("security-plugin")));
    }

    @Test
    void testSearchPluginsByCategory() {
        List<PluginInfo> results = marketplace.searchPluginsByCategory("database");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(p -> "database".equals(p.getCategory())));
    }

    @Test
    void testSearchPluginsByTags() {
        List<PluginInfo> results = marketplace.searchPluginsByTags("security", "auth");
        assertFalse(results.isEmpty());
    }

    @Test
    void testGetPopularPlugins() {
        List<PluginInfo> popular = marketplace.getPopularPlugins(10);
        assertEquals(3, popular.size());
        assertEquals("security-plugin", popular.get(0).getName());
    }

    @Test
    void testGetLatestPlugins() {
        List<PluginInfo> latest = marketplace.getLatestPlugins(10);
        assertEquals(3, latest.size());
    }

    @Test
    void testGetCertifiedPlugins() {
        List<PluginInfo> certified = marketplace.getCertifiedPlugins();
        assertEquals(2, certified.size());
        assertTrue(certified.stream().allMatch(PluginInfo::isCertified));
    }

    @Test
    void testGetFeaturedPlugins() {
        List<PluginInfo> featured = marketplace.getFeaturedPlugins();
        assertFalse(featured.isEmpty());
    }

    @Test
    void testGetCategories() {
        List<String> categories = marketplace.getCategories();
        assertTrue(categories.contains("database"));
        assertTrue(categories.contains("web"));
        assertTrue(categories.contains("security"));
    }

    @Test
    void testGetPopularTags() {
        List<String> tags = marketplace.getPopularTags(10);
        assertFalse(tags.isEmpty());
    }

    @Test
    void testInstallPlugin() {
        boolean result = marketplace.installPlugin("database-plugin");
        assertTrue(result);
        
        Optional<PluginInfo> installed = marketplace.getInstalledPlugin("database-plugin");
        assertTrue(installed.isPresent());
    }

    @Test
    void testInstallPluginWithVersion() {
        boolean result = marketplace.installPlugin("database-plugin", "1.0.0");
        assertTrue(result);
    }

    @Test
    void testInstallNonExistentPlugin() {
        boolean result = marketplace.installPlugin("nonexistent");
        assertFalse(result);
    }

    @Test
    void testUpdatePlugin() {
        marketplace.installPlugin("database-plugin");
        boolean result = marketplace.updatePlugin("database-plugin");
        assertTrue(result);
    }

    @Test
    void testUninstallPlugin() {
        marketplace.installPlugin("database-plugin");
        boolean result = marketplace.uninstallPlugin("database-plugin");
        assertTrue(result);
        
        Optional<PluginInfo> installed = marketplace.getInstalledPlugin("database-plugin");
        assertTrue(installed.isEmpty());
    }

    @Test
    void testGetInstalledPlugins() {
        marketplace.installPlugin("database-plugin");
        marketplace.installPlugin("web-plugin");
        
        List<PluginInfo> installed = marketplace.getInstalledPlugins();
        assertEquals(2, installed.size());
    }

    @Test
    void testGetUpdatesAvailable() {
        marketplace.installPlugin("database-plugin");
        
        List<PluginInfo> updates = marketplace.getUpdatesAvailable();
        assertNotNull(updates);
    }

    @Test
    void testAddAndRemoveRepository() {
        List<PluginRepository> repos = marketplace.getRepositories();
        assertEquals(2, repos.size());
        
        marketplace.removePluginRepository("repo1");
        repos = marketplace.getRepositories();
        assertEquals(1, repos.size());
    }

    @Test
    void testGetRepositories() {
        List<PluginRepository> repos = marketplace.getRepositories();
        assertEquals(2, repos.size());
    }

    @Test
    void testBuilder() {
        PluginMarketplace built = new DefaultPluginMarketplace.Builder()
            .addRepository(new LocalPluginRepository("new-repo", "New Repo", "https://new.example.com"))
            .localCacheDirectory("/tmp/new-cache")
            .build();
        
        assertNotNull(built);
        assertEquals(1, built.getRepositories().size());
    }

    @Test
    void testSearchWithDisabledRepository() {
        repo2.setEnabled(false);
        
        List<PluginInfo> results = marketplace.searchPlugins("security");
        assertTrue(results.isEmpty());
    }
}
