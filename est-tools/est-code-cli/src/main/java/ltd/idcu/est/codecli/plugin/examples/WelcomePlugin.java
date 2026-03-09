package ltd.idcu.est.codecli.plugin.examples;

import ltd.idcu.est.codecli.plugin.BaseEstPlugin;
import ltd.idcu.est.codecli.plugin.PluginException;

import java.util.Map;

public class WelcomePlugin extends BaseEstPlugin {
    
    private static final String PLUGIN_ID = "welcome-plugin";
    private static final String PLUGIN_NAME = "Welcome Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "一个简单的欢迎插件，展示EST Code CLI插件系统";
    private static final String PLUGIN_AUTHOR = "EST Team";
    
    private int welcomeCount = 0;
    
    public WelcomePlugin() {
        addCapability("features", Map.of(
            "welcome", true,
            "custom_greeting", true
        ));
        addCapability("commands", new String[]{"welcome", "greet"});
    }
    
    @Override
    public String getId() {
        return PLUGIN_ID;
    }
    
    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
    
    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }
    
    @Override
    public String getDescription() {
        return PLUGIN_DESCRIPTION;
    }
    
    @Override
    public String getAuthor() {
        return PLUGIN_AUTHOR;
    }
    
    @Override
    protected void onInitialize() throws PluginException {
        logInfo("欢迎插件已初始化！");
        logInfo("可用命令: welcome, greet");
    }
    
    public String welcome() {
        welcomeCount++;
        return "欢迎使用 EST Code CLI！\n" +
               "这是第 " + welcomeCount + " 次欢迎。\n" +
               "插件: " + getName() + " v" + getVersion();
    }
    
    public String greet(String name) {
        welcomeCount++;
        return "你好，" + name + "！\n" +
               "欢迎使用 EST Code CLI！";
    }
    
    public String getStats() {
        return "欢迎次数: " + welcomeCount;
    }
    
    @Override
    protected void onShutdown() throws PluginException {
        logInfo("欢迎插件已关闭。统计: " + getStats());
    }
}
