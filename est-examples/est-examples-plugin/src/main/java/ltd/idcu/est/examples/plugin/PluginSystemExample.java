package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginListener;
import ltd.idcu.est.plugin.api.PluginManager;
import ltd.idcu.est.plugin.api.PluginStats;
import ltd.idcu.est.plugin.impl.DefaultPluginManager;

public class PluginSystemExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("EST 插件系统示例");
        System.out.println("========================================\n");

        PluginManager pluginManager = new DefaultPluginManager();

        pluginManager.addListener(new PluginListener() {
            @Override
            public void onPluginLoaded(Plugin plugin) {
                System.out.println("[监听器] 插件已加载: " + plugin.getName());
            }

            @Override
            public void onPluginInitialized(Plugin plugin) {
                System.out.println("[监听器] 插件已初始化: " + plugin.getName());
            }

            @Override
            public void onPluginStarted(Plugin plugin) {
                System.out.println("[监听器] 插件已启动: " + plugin.getName());
            }

            @Override
            public void onPluginStopped(Plugin plugin) {
                System.out.println("[监听器] 插件已停止: " + plugin.getName());
            }

            @Override
            public void onPluginUnloaded(Plugin plugin) {
                System.out.println("[监听器] 插件已卸载: " + plugin.getName());
            }

            @Override
            public void onPluginError(Plugin plugin, Exception e) {
                System.err.println("[监听器] 插件错误: " + plugin.getName() + " - " + e.getMessage());
            }
        });

        System.out.println("--- 步骤 1: 加载 HelloPlugin ---");
        HelloPlugin helloPlugin = (HelloPlugin) pluginManager.loadPluginFromClass(HelloPlugin.class);
        System.out.println();

        System.out.println("--- 步骤 2: 加载 LoggingPlugin ---");
        LoggingPlugin loggingPlugin = (LoggingPlugin) pluginManager.loadPluginFromClass(LoggingPlugin.class);
        System.out.println();

        System.out.println("--- 步骤 3: 使用 HelloPlugin ---");
        System.out.println(helloPlugin.sayHello());
        System.out.println(helloPlugin.greet("EST User"));
        System.out.println();

        System.out.println("--- 步骤 4: 使用 LoggingPlugin ---");
        loggingPlugin.log("这是一条测试日志");
        loggingPlugin.log("插件系统运行正常");
        System.out.println("日志数量: " + loggingPlugin.getLogCount());
        System.out.println("日志内容:\n" + loggingPlugin.getLogsAsString());
        System.out.println();

        System.out.println("--- 步骤 5: 启动所有插件 ---");
        pluginManager.startAllPlugins();
        System.out.println();

        System.out.println("--- 步骤 6: 加载 GreetingPlugin (依赖 HelloPlugin) ---");
        try {
            GreetingPlugin greetingPlugin = (GreetingPlugin) pluginManager.loadPluginFromClass(GreetingPlugin.class);
            System.out.println(greetingPlugin.friendlyGreet("Plugin User"));
            System.out.println();
        } catch (Exception e) {
            System.err.println("加载 GreetingPlugin 失败: " + e.getMessage());
        }

        System.out.println("--- 步骤 7: 查看插件统计 ---");
        PluginStats stats = pluginManager.getStats();
        System.out.println("总插件数: " + stats.getTotalPlugins());
        System.out.println("运行中: " + stats.getRunningPlugins());
        System.out.println("已停止: " + stats.getStoppedPlugins());
        System.out.println("错误: " + stats.getErrorPlugins());
        System.out.println("总加载时间: " + stats.getTotalLoadTime() + "ms");
        System.out.println("总启动时间: " + stats.getTotalStartTime() + "ms");
        System.out.println();

        System.out.println("--- 步骤 8: 列出所有插件 ---");
        for (Plugin plugin : pluginManager.getPlugins()) {
            System.out.println("- " + plugin.getName() + " v" + plugin.getVersion() +
                    " [" + plugin.getState() + "]");
            System.out.println("  描述: " + plugin.getDescription());
            System.out.println("  作者: " + plugin.getAuthor());
        }
        System.out.println();

        System.out.println("--- 步骤 9: 停止所有插件 ---");
        pluginManager.stopAllPlugins();
        System.out.println();

        System.out.println("--- 步骤 10: 卸载所有插件 ---");
        for (Plugin plugin : pluginManager.getPlugins()) {
            pluginManager.unloadPlugin(plugin);
        }
        System.out.println();

        System.out.println("========================================");
        System.out.println("示例执行完成!");
        System.out.println("========================================");
    }
}
