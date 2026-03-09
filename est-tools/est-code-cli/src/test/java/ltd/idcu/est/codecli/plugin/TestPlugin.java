package ltd.idcu.est.codecli.plugin;

import java.util.Map;

public class TestPlugin extends BaseEstPlugin {

    private boolean initialized = false;
    private boolean shutdown = false;

    @Override
    public String getId() {
        return "test-plugin";
    }

    @Override
    public String getName() {
        return "Test Plugin";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getDescription() {
        return "Test plugin for PluginManager";
    }

    @Override
    public String getAuthor() {
        return "Test Author";
    }

    @Override
    protected void onInitialize() throws PluginException {
        initialized = true;
        addCapability("test", true);
    }

    @Override
    protected void onShutdown() throws PluginException {
        shutdown = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isShutdown() {
        return shutdown;
    }
}
