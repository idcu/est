package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.PluginInfo;

public class HelloPlugin extends AbstractPlugin {

    public HelloPlugin() {
        super(PluginInfo.builder()
                .name("hello-plugin")
                .version("1.0.0")
                .description("一个简单的 Hello 插件，展示 EST 插件系统的基本用法")
                .author("EST Team")
                .mainClass(HelloPlugin.class.getName())
                .category("示例")
                .tags("hello", "example", "basic")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    @Override
    public void onLoad() {
        System.out.println("[HelloPlugin] 插件已加载");
    }

    @Override
    public void onEnable() {
        System.out.println("[HelloPlugin] 插件已启用");
    }

    @Override
    public void onDisable() {
        System.out.println("[HelloPlugin] 插件已禁用");
    }

    @Override
    public void onUnload() {
        System.out.println("[HelloPlugin] 插件已卸载");
    }

    public String sayHello() {
        return "Hello from HelloPlugin!";
    }

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
