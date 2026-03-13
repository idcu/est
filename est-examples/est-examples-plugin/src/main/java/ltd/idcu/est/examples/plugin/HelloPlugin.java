package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.PluginInfo;

public class HelloPlugin extends AbstractPlugin {

    public HelloPlugin() {
        super(PluginInfo.builder()
                .name("hello-plugin")
                .version("1.0.0")
                .description("A simple Hello plugin demonstrating basic usage of EST Plugin System")
                .author("EST Team")
                .mainClass(HelloPlugin.class.getName())
                .category("Example")
                .tags("hello", "example", "basic")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    @Override
    public void onLoad() {
        System.out.println("[HelloPlugin] Plugin loaded");
    }

    @Override
    public void onEnable() {
        System.out.println("[HelloPlugin] Plugin enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[HelloPlugin] Plugin disabled");
    }

    @Override
    public void onUnload() {
        System.out.println("[HelloPlugin] Plugin unloaded");
    }

    public String sayHello() {
        return "Hello from HelloPlugin!";
    }

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
