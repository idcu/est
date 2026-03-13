package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.PluginInfo;

public class GreetingPlugin extends AbstractPlugin {

    public GreetingPlugin() {
        super(PluginInfo.builder()
                .name("greeting-plugin")
                .version("1.0.0")
                .description("A greeting plugin that depends on HelloPlugin, demonstrating plugin dependencies")
                .author("EST Team")
                .mainClass(GreetingPlugin.class.getName())
                .dependencies("hello-plugin")
                .category("Example")
                .tags("greeting", "dependency", "example")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    private HelloPlugin helloPlugin;

    @Override
    public void onLoad() {
        System.out.println("[GreetingPlugin] Plugin loaded");
    }

    @Override
    public void onEnable() {
        System.out.println("[GreetingPlugin] Plugin enabled");
        if (getContext() != null) {
            helloPlugin = (HelloPlugin) getContext().getAttribute("hello-plugin");
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[GreetingPlugin] Plugin disabled");
    }

    @Override
    public void onUnload() {
        System.out.println("[GreetingPlugin] Plugin unloaded");
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
