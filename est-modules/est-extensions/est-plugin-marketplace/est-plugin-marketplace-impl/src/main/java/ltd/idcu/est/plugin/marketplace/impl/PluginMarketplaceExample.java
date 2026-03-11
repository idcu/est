package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.marketplace.api.*;

import java.util.List;
import java.util.Optional;

public class PluginMarketplaceExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST Plugin Marketplace Example ===\n");
        
        LocalPluginRepository repo = new LocalPluginRepository(
            "est-official", 
            "EST Official Repository", 
            "https://marketplace.est.idcu.ltd"
        );
        
        addSamplePlugins(repo);
        
        PluginMarketplace marketplace = new DefaultPluginMarketplace.Builder()
            .addRepository(repo)
            .localCacheDirectory(System.getProperty("java.io.tmpdir") + "/est-plugins-example")
            .build();
        
        PluginReviewService reviewService = new DefaultPluginReviewService();
        
        System.out.println("1. Getting all plugins:");
        List<PluginInfo> allPlugins = repo.getAllPlugins();
        for (PluginInfo plugin : allPlugins) {
            System.out.println("   - " + plugin.getName() + " v" + plugin.getVersion());
        }
        System.out.println();
        
        System.out.println("2. Searching for 'security' plugins:");
        List<PluginInfo> securityPlugins = marketplace.searchPlugins("security");
        for (PluginInfo plugin : securityPlugins) {
            System.out.println("   - " + plugin.getName() + " (" + plugin.getDescription() + ")");
        }
        System.out.println();
        
        System.out.println("3. Advanced search - certified plugins sorted by downloads:");
        PluginSearchQuery query = PluginSearchQuery.builder()
            .certified(true)
            .sortBy("downloads")
            .page(0)
            .pageSize(10)
            .build();
        
        SearchResult result = marketplace.searchPlugins(query);
        System.out.println("   Total: " + result.getTotalCount() + " plugins");
        for (PluginInfo plugin : result.getPlugins()) {
            System.out.println("   - " + plugin.getName() + " | Downloads: " + plugin.getDownloadCount() + " | Rating: " + plugin.getRating());
        }
        System.out.println();
        
        System.out.println("4. Getting popular plugins (top 3):");
        List<PluginInfo> popular = marketplace.getPopularPlugins(3);
        for (PluginInfo plugin : popular) {
            System.out.println("   - " + plugin.getName() + " (" + plugin.getDownloadCount() + " downloads)");
        }
        System.out.println();
        
        System.out.println("5. Getting plugin categories:");
        List<PluginCategory> categories = marketplace.getPluginCategories();
        for (PluginCategory category : categories) {
            System.out.println("   - " + category.getIcon() + " " + category.getName() + " (" + category.getPluginCount() + " plugins)");
        }
        System.out.println();
        
        System.out.println("6. Getting popular tags:");
        List<String> tags = marketplace.getPopularTags(5);
        System.out.println("   " + String.join(", ", tags));
        System.out.println();
        
        System.out.println("7. Installing database-plugin:");
        boolean installed = marketplace.installPlugin("database-plugin");
        System.out.println("   Installed: " + installed);
        
        System.out.println("\n8. Getting installed plugins:");
        List<PluginInfo> installedPlugins = marketplace.getInstalledPlugins();
        for (PluginInfo plugin : installedPlugins) {
            System.out.println("   - " + plugin.getName() + " v" + plugin.getVersion());
        }
        System.out.println();
        
        System.out.println("9. Adding a review for security-plugin:");
        PluginReview review = reviewService.addReview(
            "security-plugin",
            "user-123",
            "John Doe",
            5,
            "Excellent security plugin!",
            "This plugin provides comprehensive security features and is very easy to use. Highly recommended!"
        );
        System.out.println("   Review added: " + review.getTitle());
        System.out.println();
        
        System.out.println("10. Adding another review:");
        PluginReview review2 = reviewService.addReview(
            "security-plugin",
            "user-456",
            "Jane Smith",
            4,
            "Great plugin, room for improvement",
            "Good security features, but documentation could be better."
        );
        System.out.println("   Review added: " + review2.getTitle());
        System.out.println();
        
        System.out.println("11. Getting reviews for security-plugin:");
        List<PluginReview> reviews = reviewService.getReviewsForPlugin("security-plugin");
        for (PluginReview r : reviews) {
            System.out.println("   - " + r.getUserName() + ": " + r.getRating() + " stars - " + r.getTitle());
            System.out.println("     " + r.getContent());
        }
        System.out.println();
        
        System.out.println("12. Average rating for security-plugin:");
        double avgRating = reviewService.getAverageRating("security-plugin");
        System.out.println("   " + String.format("%.1f", avgRating) + " stars");
        System.out.println();
        
        System.out.println("13. Marking first review as helpful:");
        boolean marked = reviewService.markHelpful(review.getId(), "user-789");
        System.out.println("   Marked as helpful: " + marked);
        System.out.println();
        
        System.out.println("14. Uninstalling database-plugin:");
        boolean uninstalled = marketplace.uninstallPlugin("database-plugin");
        System.out.println("   Uninstalled: " + uninstalled);
        System.out.println();
        
        System.out.println("=== Example Complete ===");
    }
    
    private static void addSamplePlugins(LocalPluginRepository repo) {
        PluginInfo databasePlugin = PluginInfo.builder()
            .name("database-plugin")
            .version("1.0.0")
            .description("Database connectivity plugin with JDBC support")
            .author("EST Team")
            .mainClass("ltd.idcu.est.plugins.database.DatabasePlugin")
            .category("database")
            .tags("database", "sql", "jdbc", "persistence")
            .rating(4.8)
            .downloadCount(5234)
            .publishTime(System.currentTimeMillis() - 86400000 * 30)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .license("Apache-2.0")
            .homepage("https://est.idcu.ltd/plugins/database")
            .minFrameworkVersion("2.3.0")
            .build();
        
        PluginInfo webPlugin = PluginInfo.builder()
            .name("web-plugin")
            .version("2.1.0")
            .description("Web framework plugin with REST API support")
            .author("EST Team")
            .mainClass("ltd.idcu.est.plugins.web.WebPlugin")
            .category("web")
            .tags("web", "http", "rest", "api")
            .rating(4.5)
            .downloadCount(3456)
            .publishTime(System.currentTimeMillis() - 86400000 * 20)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .license("Apache-2.0")
            .homepage("https://est.idcu.ltd/plugins/web")
            .minFrameworkVersion("2.3.0")
            .build();
        
        PluginInfo securityPlugin = PluginInfo.builder()
            .name("security-plugin")
            .version("1.5.0")
            .description("Security plugin with authentication and authorization")
            .author("EST Team")
            .mainClass("ltd.idcu.est.plugins.security.SecurityPlugin")
            .category("security")
            .tags("security", "auth", "encryption", "rbac")
            .rating(4.9)
            .downloadCount(7890)
            .publishTime(System.currentTimeMillis() - 86400000 * 15)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .license("Apache-2.0")
            .homepage("https://est.idcu.ltd/plugins/security")
            .minFrameworkVersion("2.3.0")
            .build();
        
        PluginInfo aiPlugin = PluginInfo.builder()
            .name("ai-plugin")
            .version("0.9.0")
            .description("AI integration plugin with LLM support")
            .author("Community")
            .mainClass("ltd.idcu.est.plugins.ai.AiPlugin")
            .category("ai")
            .tags("ai", "llm", "chatgpt", "openai")
            .rating(4.2)
            .downloadCount(1234)
            .publishTime(System.currentTimeMillis() - 86400000 * 7)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(false)
            .license("MIT")
            .homepage("https://github.com/est-plugins/ai-plugin")
            .minFrameworkVersion("2.4.0")
            .build();
        
        PluginInfo monitoringPlugin = PluginInfo.builder()
            .name("monitoring-plugin")
            .version("1.2.0")
            .description("Monitoring and observability plugin")
            .author("EST Team")
            .mainClass("ltd.idcu.est.plugins.monitoring.MonitoringPlugin")
            .category("monitoring")
            .tags("monitoring", "metrics", "tracing", "logs")
            .rating(4.6)
            .downloadCount(2345)
            .publishTime(System.currentTimeMillis() - 86400000 * 10)
            .lastUpdateTime(System.currentTimeMillis())
            .certified(true)
            .license("Apache-2.0")
            .homepage("https://est.idcu.ltd/plugins/monitoring")
            .minFrameworkVersion("2.3.0")
            .build();
        
        repo.addPlugin(databasePlugin);
        repo.addPlugin(webPlugin);
        repo.addPlugin(securityPlugin);
        repo.addPlugin(aiPlugin);
        repo.addPlugin(monitoringPlugin);
    }
}
