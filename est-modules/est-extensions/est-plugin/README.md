# EST Plugin 鎻掍欢妯″潡

鎻愪緵鎻掍欢绯荤粺鏀寔锛屽寘鎷彃浠舵帴鍙ｃ€佹彃浠跺姞杞藉櫒鍜屾彃浠剁鐞嗗櫒銆?

## 妯″潡缁撴瀯

```
est-plugin/
鈹溾攢鈹€ est-plugin-api/      # 鎻掍欢鎺ュ彛瀹氫箟
鈹溾攢鈹€ est-plugin-impl/     # 鎻掍欢瀹炵幇
鈹斺攢鈹€ pom.xml
```

## 涓昏鍔熻兘

### 鎻掍欢鎺ュ彛

```java
import ltd.idcu.est.plugin.api.Plugin;

public interface Plugin {
    String getName();
    String getVersion();
    void onLoad(PluginContext context);
    void onEnable();
    void onDisable();
    void onUnload();
}
```

### 鍒涘缓鎻掍欢

```java
public class MyPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "MyPlugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public void onLoad(PluginContext context) {
        System.out.println("Plugin loaded");
    }
    
    @Override
    public void onEnable() {
        System.out.println("Plugin enabled");
    }
    
    @Override
    public void onDisable() {
        System.out.println("Plugin disabled");
    }
    
    @Override
    public void onUnload() {
        System.out.println("Plugin unloaded");
    }
}
```

### 鎻掍欢绠＄悊鍣?

```java
import ltd.idcu.est.plugin.api.PluginManager;
import ltd.idcu.est.plugin.impl.DefaultPluginManager;

// 鍒涘缓鎻掍欢绠＄悊鍣?
PluginManager manager = new DefaultPluginManager();

// 鍔犺浇鎻掍欢
manager.loadPlugin(new MyPlugin());

// 浠庣洰褰曞姞杞?
manager.loadFromDirectory("./plugins");

// 浠嶫AR鍔犺浇
manager.loadFromJar("./plugins/my-plugin.jar");

// 鍚敤鎵€鏈夋彃浠?
manager.enableAll();

// 鑾峰彇鎻掍欢
Optional<Plugin> plugin = manager.getPlugin("MyPlugin");

// 鍒楀嚭鎵€鏈夋彃浠?
List<Plugin> plugins = manager.getPlugins();
```

### 鎻掍欢渚濊禆

```java
@PluginDependency(plugin = "AnotherPlugin", version = ">=1.0.0")
public class MyPlugin implements Plugin {
    // ...
}
```

## 渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-plugin-api</artifactId>
    <version>2.1.0</version>
</dependency>
```
