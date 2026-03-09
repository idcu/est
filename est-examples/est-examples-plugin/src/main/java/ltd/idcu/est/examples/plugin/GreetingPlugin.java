package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.PluginInfo;

public class GreetingPlugin extends AbstractPlugin {

    public GreetingPlugin() {
        super(PluginInfo.builder()
                .name("greeting-plugin")
                .version("1.0.0")
                .description("一个依赖 HelloPlugin 的问候插件，展示插件依赖关系")
                .author("EST Team")
                .mainClass(GreetingPlugin.class.getName())
                .dependencies("hello-plugin")
                .category("示例")
                .tags("greeting", "dependency", "example")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    private HelloPlugin helloPlugin;

    @Override
    public void onLoad() {
        System.out.println("[GreetingPlugin] 插件已加载");
    }

    @Override
    public void onEnable() {
        System.out.println("[GreetingPlugin] 插件已启用");
        if (getContext() != null) {
            helloPlugin = (HelloPlugin) getContext().getAttribute("hello-plugin");
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[GreetingPlugin] 插件已禁用");
    }

    @Override
    public void onUnload() {
        System.out.println("[GreetingPlugin] 插件已卸载");
    }

    public String friendlyGreet(String name) {
        if (helloPlugin != null) {
            return helloPlugin.greet(name) + " Welcome to EST Plugin System!";
        }
        return "Hello, " + name + "! (HelloPlugin not available)";
    }

    public String getGreetingCount() {
        return "Greetings sent: 0 (simulated)";
    }
}
