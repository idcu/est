package ltd.idcu.est.examples.plugin;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.PluginInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoggingPlugin extends AbstractPlugin {

    private final List<String> logs = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LoggingPlugin() {
        super(PluginInfo.builder()
                .name("logging-plugin")
                .version("1.0.0")
                .description("A logging plugin demonstrating plugin state and attribute management")
                .author("EST Team")
                .mainClass(LoggingPlugin.class.getName())
                .category("Utility")
                .tags("logging", "utility", "example")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    @Override
    public void onLoad() {
        log("Plugin loaded");
        setAttribute("loadTime", System.currentTimeMillis());
    }

    @Override
    public void onEnable() {
        log("Plugin enabled");
        setAttribute("enableTime", System.currentTimeMillis());
    }

    @Override
    public void onDisable() {
        log("Plugin disabled");
    }

    @Override
    public void onUnload() {
        log("Plugin unloaded");
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = String.format("[%s] %s", timestamp, message);
        logs.add(logEntry);
        System.out.println("[LoggingPlugin] " + logEntry);
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public String getLogsAsString() {
        return String.join("\n", logs);
    }

    public void clearLogs() {
        logs.clear();
        log("Logs cleared");
    }

    public int getLogCount() {
        return logs.size();
    }
}
