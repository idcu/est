package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginListener;
import ltd.idcu.est.plugin.api.PluginManager;
import ltd.idcu.est.plugin.api.PluginStats;
import ltd.idcu.est.plugin.impl.DefaultPluginManager;

public class PluginSystemExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("EST Plugin System Example");
        System.out.println("========================================\n");

        PluginManager pluginManager = new DefaultPluginManager();

        pluginManager.addListener(new PluginListener() {
            @Override
            public void onPluginLoaded(Plugin plugin) {
                System.out.println("[Listener] Plugin loaded: " + plugin.getName());
            }

            @Override
            public void onPluginInitialized(Plugin plugin) {
                System.out.println("[Listener] Plugin initialized: " + plugin.getName());
            }

            @Override
            public void onPluginStarted(Plugin plugin) {
                System.out.println("[Listener] Plugin started: " + plugin.getName());
            }

            @Override
            public void onPluginStopped(Plugin plugin) {
                System.out.println("[Listener] Plugin stopped: " + plugin.getName());
            }

            @Override
            public void onPluginUnloaded(Plugin plugin) {
                System.out.println("[Listener] Plugin unloaded: " + plugin.getName());
            }

            @Override
            public void onPluginError(Plugin plugin, Exception e) {
                System.err.println("[Listener] Plugin error: " + plugin.getName() + " - " + e.getMessage());
            }
        });

        System.out.println("--- Step 1: Load HelloPlugin ---");
        HelloPlugin helloPlugin = (HelloPlugin) pluginManager.loadPluginFromClass(HelloPlugin.class);
        System.out.println();

        System.out.println("--- Step 2: Load LoggingPlugin ---");
        LoggingPlugin loggingPlugin = (LoggingPlugin) pluginManager.loadPluginFromClass(LoggingPlugin.class);
        System.out.println();

        System.out.println("--- Step 3: Use HelloPlugin ---");
        System.out.println(helloPlugin.sayHello());
        System.out.println(helloPlugin.greet("EST User"));
        System.out.println();

        System.out.println("--- Step 4: Use LoggingPlugin ---");
        loggingPlugin.log("This is a test log");
        loggingPlugin.log("Plugin system running normally");
        System.out.println("Log count: " + loggingPlugin.getLogCount());
        System.out.println("Log content:\n" + loggingPlugin.getLogsAsString());
        System.out.println();

        System.out.println("--- Step 5: Start all plugins ---");
        pluginManager.startAllPlugins();
        System.out.println();

        System.out.println("--- Step 6: Load GreetingPlugin (depends on HelloPlugin) ---");
        try {
            GreetingPlugin greetingPlugin = (GreetingPlugin) pluginManager.loadPluginFromClass(GreetingPlugin.class);
            System.out.println(greetingPlugin.friendlyGreet("Plugin User"));
            System.out.println();
        } catch (Exception e) {
            System.err.println("Failed to load GreetingPlugin: " + e.getMessage());
        }

        System.out.println("--- Step 7: View plugin statistics ---");
        PluginStats stats = pluginManager.getStats();
        System.out.println("Total plugins: " + stats.getTotalPlugins());
        System.out.println("Running: " + stats.getRunningPlugins());
        System.out.println("Stopped: " + stats.getStoppedPlugins());
        System.out.println("Error: " + stats.getErrorPlugins());
        System.out.println("Total load time: " + stats.getTotalLoadTime() + "ms");
        System.out.println("Total start time: " + stats.getTotalStartTime() + "ms");
        System.out.println();

        System.out.println("--- Step 8: List all plugins ---");
        for (Plugin plugin : pluginManager.getPlugins()) {
            System.out.println("- " + plugin.getName() + " v" + plugin.getVersion() +
                    " [" + plugin.getState() + "]");
            System.out.println("  Description: " + plugin.getDescription());
            System.out.println("  Author: " + plugin.getAuthor());
        }
        System.out.println();

        System.out.println("--- Step 9: Stop all plugins ---");
        pluginManager.stopAllPlugins();
        System.out.println();

        System.out.println("--- Step 10: Unload all plugins ---");
        for (Plugin plugin : pluginManager.getPlugins()) {
            pluginManager.unloadPlugin(plugin);
        }
        System.out.println();

        System.out.println("========================================");
        System.out.println("Example execution completed!");
        System.out.println("========================================");
    }
}
