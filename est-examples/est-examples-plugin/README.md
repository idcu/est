# EST Plugin System Examples

This example demonstrates how to use the EST Framework Plugin System.

## Features

- **Plugin Lifecycle Management**: Load, initialize, start, stop, uninstall
- **Plugin Dependency Management**: Support for dependency relationships between plugins
- **Event Listening**: Listen to various lifecycle events of plugins
- **Plugin Statistics**: Get runtime statistics of the plugin system
- **Attribute Management**: Plugins can share attributes with each other

## Project Structure

```
est-examples-plugin/
├── src/main/java/ltd/idcu/est/examples/plugin/
│   ├── HelloPlugin.java          # Basic Hello Plugin
│   ├── GreetingPlugin.java       # Greeting Plugin depending on HelloPlugin
│   ├── LoggingPlugin.java        # Logging Plugin
│   └── PluginSystemExample.java  # Plugin System Main Example
├── pom.xml
└── README.md
```

## Quick Start

### Run Example

```bash
cd est-examples-plugin
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.examples.plugin.PluginSystemExample"
```

### Or Run in IDE

Directly run the `main` method of `PluginSystemExample.java`.

## Example Description

### 1. HelloPlugin

The simplest plugin example, demonstrating the basic structure and lifecycle methods of a plugin.

```java
public class HelloPlugin extends AbstractPlugin {
    public HelloPlugin() {
        super(PluginInfo.builder()
                .name("hello-plugin")
                .version("1.0.0")
                .description("A simple Hello plugin")
                .author("EST Team")
                .build());
    }

    @Override
    public void onLoad() {
        System.out.println("[HelloPlugin] Plugin loaded");
    }

    public String sayHello() {
        return "Hello from HelloPlugin!";
    }
}
```

### 2. LoggingPlugin

Demonstrates attribute management and state management features of plugins.

```java
public class LoggingPlugin extends AbstractPlugin {
    @Override
    public void onLoad() {
        setAttribute("loadTime", System.currentTimeMillis());
    }

    public void log(String message) {
        // Log message
    }
}
```

### 3. GreetingPlugin

Demonstrates dependency relationships between plugins.

```java
public class GreetingPlugin extends AbstractPlugin {
    public GreetingPlugin() {
        super(PluginInfo.builder()
                .name("greeting-plugin")
                .dependencies("hello-plugin")  // Depends on HelloPlugin
                .build());
    }
}
```

## Plugin System API

### PluginManager

Core plugin manager interface, providing the following features:

```java
PluginManager pluginManager = new DefaultPluginManager();

// Load plugin from class
Plugin plugin = pluginManager.loadPluginFromClass(MyPlugin.class);

// Load plugin from path
Plugin plugin = pluginManager.loadPlugin("/path/to/plugin.jar");

// Start plugin
pluginManager.startPlugin("plugin-name");

// Stop plugin
pluginManager.stopPlugin("plugin-name");

// Uninstall plugin
pluginManager.unloadPlugin("plugin-name");

// Get all plugins
List<Plugin> plugins = pluginManager.getPlugins();

// Get statistics
PluginStats stats = pluginManager.getStats();
```

### PluginListener

Plugin event listener:

```java
pluginManager.addListener(new PluginListener() {
    @Override
    public void onPluginLoaded(Plugin plugin) {
        System.out.println("Plugin loaded: " + plugin.getName());
    }

    @Override
    public void onPluginStarted(Plugin plugin) {
        System.out.println("Plugin started: " + plugin.getName());
    }

    // Other lifecycle methods...
});
```

### Plugin Lifecycle

Plugins have the following states:

- `LOADED` - Loaded
- `INITIALIZED` - Initialized
- `STARTING` - Starting
- `RUNNING` - Running
- `STOPPING` - Stopping
- `STOPPED` - Stopped
- `ERROR` - Error state

## Create Your Own Plugin

1. Extend `AbstractPlugin` class
2. Provide `PluginInfo` in constructor
3. Override needed lifecycle methods
4. Implement your own business logic

```java
public class MyPlugin extends AbstractPlugin {
    public MyPlugin() {
        super(PluginInfo.builder()
                .name("my-plugin")
                .version("1.0.0")
                .description("My plugin")
                .author("My Name")
                .mainClass(MyPlugin.class.getName())
                .category("Tool")
                .tags("my", "plugin")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    @Override
    public void onEnable() {
        System.out.println("My plugin enabled!");
    }

    public void doSomething() {
        System.out.println("Doing something...");
    }
}
```

## More Information

- Check EST Framework documentation for more plugin system features
- Reference plugin implementations in `est-tools/est-code-cli`
