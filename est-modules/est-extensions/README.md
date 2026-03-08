# EST Extensions 鎵╁睍妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Extensions锛焆(#浠€涔堟槸-est-extensions)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Extensions锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Extensions 灏卞儚鏄竴涓?宸ュ叿绠辨墿灞曞寘"銆傛兂璞′竴涓嬩綘鏈夊熀鏈殑宸ュ叿锛屼絾杩橀渶瑕佷竴浜涚壒娈婄殑鍔熻兘锛?
**浼犵粺鏂瑰紡**锛氬畾鏃朵换鍔¤鑷繁鍐欏畾鏃跺櫒锛屾彃浠剁郴缁熻鑷繁璁捐锛岀儹閲嶈浇瑕佽嚜宸卞疄鐜?.. 寰堥夯鐑︼紒

**EST Extensions 鏂瑰紡**锛氱粰浣犱竴濂楁墿灞曞伐鍏凤紝閲岄潰鏈夛細
- 鈴?**瀹氭椂璋冨害** - 鏀寔 Cron 琛ㄨ揪寮忓拰鍥哄畾鏃堕棿闂撮殧
- 馃攲 **鎻掍欢绯荤粺** - 鍔ㄦ€佸姞杞藉拰绠＄悊鎻掍欢
- 馃敟 **鐑噸杞?* - 浠ｇ爜淇敼鍚庤嚜鍔ㄩ噸鏂板姞杞?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍚敤瀹氭椂浠诲姟銆佹彃浠剁郴缁?- 鈿?**楂樻€ц兘** - 浼樺寲鐨勫畾鏃惰皟搴︼紝浣庡紑閿€鐨勬彃浠跺姞杞?- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夎皟搴︾瓥鐣ャ€佹彃浠跺姞杞藉櫒
- 馃帹 **鍔熻兘瀹屾暣** - 璋冨害銆佹彃浠躲€佺儹閲嶈浇涓€搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-scheduler</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-plugin</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓畾鏃朵换鍔?
```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.CronExpression;
import ltd.idcu.est.scheduler.FixedRate;

public class FirstExtensionApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Extensions 绗竴涓ず渚?===\n");
        
        Scheduler scheduler = Scheduler.create();
        
        scheduler.schedule(new FixedRate(5000), () -> {
            System.out.println("姣?5 绉掓墽琛屼竴娆? " + LocalDateTime.now());
        });
        
        scheduler.schedule(new CronExpression("0 * * * * ?"), () -> {
            System.out.println("姣忓垎閽熸墽琛屼竴娆? " + LocalDateTime.now());
        });
        
        System.out.println("瀹氭椂浠诲姟宸插惎鍔?);
        Thread.sleep(60000);
        scheduler.shutdown();
    }
}
```

---

## 鍩虹绡?
### 1. est-scheduler 瀹氭椂璋冨害

#### Cron 琛ㄨ揪寮?
```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.CronExpression;

Scheduler scheduler = Scheduler.create();

scheduler.schedule(new CronExpression("0 0 12 * * ?"), () -> {
    System.out.println("姣忓ぉ涓崍 12 鐐规墽琛?);
});

scheduler.schedule(new CronExpression("0 0 8-18 * * ?"), () -> {
    System.out.println("姣忓ぉ 8 鐐瑰埌 18 鐐癸紝姣忓皬鏃舵墽琛?);
});

scheduler.schedule(new CronExpression("0 0/5 * * * ?"), () -> {
    System.out.println("姣?5 鍒嗛挓鎵ц");
});
```

#### 鍥哄畾鏃堕棿闂撮殧

```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.FixedRate;
import ltd.idcu.est.scheduler.FixedDelay;

Scheduler scheduler = Scheduler.create();

scheduler.schedule(new FixedRate(1000), () -> {
    System.out.println("鍥哄畾棰戠巼锛氭瘡 1 绉掓墽琛屼竴娆★紙涓嶇瓑寰呬笂娆″畬鎴愶級");
});

scheduler.schedule(new FixedDelay(2000), () -> {
    System.out.println("鍥哄畾寤惰繜锛氫笂娆″畬鎴愬悗 2 绉掑啀鎵ц");
});
```

#### 涓€娆℃€т换鍔?
```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.OneTime;

Scheduler scheduler = Scheduler.create();

scheduler.schedule(new OneTime(5000), () -> {
    System.out.println("5 绉掑悗鎵ц涓€娆?);
});

scheduler.schedule(new OneTime(LocalDateTime.now().plusMinutes(1)), () -> {
    System.out.println("1 鍒嗛挓鍚庢墽琛屼竴娆?);
});
```

### 2. est-plugin 鎻掍欢绯荤粺

#### 瀹氫箟鎻掍欢

```java
import ltd.idcu.est.plugin.Plugin;
import ltd.idcu.est.plugin.PluginContext;

public interface MyPlugin extends Plugin {
    void doSomething();
}

public class MyPluginImpl implements MyPlugin {
    
    @Override
    public String getName() {
        return "my-plugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public void initialize(PluginContext context) {
        System.out.println("鎻掍欢鍒濆鍖? " + getName());
    }
    
    @Override
    public void destroy() {
        System.out.println("鎻掍欢閿€姣? " + getName());
    }
    
    @Override
    public void doSomething() {
        System.out.println("鎻掍欢鎵ц鎿嶄綔");
    }
}
```

#### 鍔犺浇鎻掍欢

```java
import ltd.idcu.est.plugin.PluginManager;
import ltd.idcu.est.plugin.PluginLoader;

PluginManager manager = PluginManager.create();

PluginLoader loader = manager.getPluginLoader();
loader.loadPlugin(Paths.get("./plugins/my-plugin.jar"));

MyPlugin plugin = manager.getPlugin("my-plugin", MyPlugin.class);
plugin.doSomething();

manager.unloadPlugin("my-plugin");
```

#### 鎻掍欢鐢熷懡鍛ㄦ湡

```java
import ltd.idcu.est.plugin.Plugin;
import ltd.idcu.est.plugin.PluginContext;

public class LifecyclePlugin implements Plugin {
    
    @Override
    public void initialize(PluginContext context) {
        System.out.println("鎻掍欢鍒濆鍖?);
        context.setAttribute("key", "value");
    }
    
    @Override
    public void start() {
        System.out.println("鎻掍欢鍚姩");
    }
    
    @Override
    public void stop() {
        System.out.println("鎻掍欢鍋滄");
    }
    
    @Override
    public void destroy() {
        System.out.println("鎻掍欢閿€姣?);
    }
}
```

### 3. est-hotreload 鐑噸杞?
#### 鍚敤鐑噸杞?
```java
import ltd.idcu.est.hotreload.HotReloader;
import ltd.idcu.est.hotreload.ReloadListener;

HotReloader reloader = HotReloader.create();

reloader.addListener(new ReloadListener() {
    @Override
    public void onReload(Class<?> clazz) {
        System.out.println("绫诲凡閲嶆柊鍔犺浇: " + clazz.getName());
    }
});

reloader.watch(Paths.get("./target/classes"));
reloader.start();
```

---

## 杩涢樁绡?
### 1. 鑷畾涔夎皟搴︾瓥鐣?
```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.SchedulingStrategy;
import ltd.idcu.est.scheduler.Task;

public class CustomSchedulingStrategy implements SchedulingStrategy {
    
    @Override
    public void schedule(Task task) {
        // 鑷畾涔夎皟搴﹂€昏緫
    }
    
    @Override
    public void cancel(String taskId) {
        // 鍙栨秷浠诲姟
    }
}

Scheduler scheduler = Scheduler.create();
scheduler.setStrategy(new CustomSchedulingStrategy());
```

### 2. 鎻掍欢渚濊禆绠＄悊

```java
import ltd.idcu.est.plugin.Plugin;
import ltd.idcu.est.plugin.PluginDependency;
import ltd.idcu.est.plugin.DependencyResolver;

public class DependentPlugin implements Plugin {
    
    @Override
    public PluginDependency[] getDependencies() {
        return new PluginDependency[]{
            new PluginDependency("core-plugin", "1.0.0"),
            new PluginDependency("utils-plugin", "2.1.0")
        };
    }
}

PluginManager manager = PluginManager.create();
manager.setDependencyResolver(new DependencyResolver() {
    @Override
    public boolean resolve(PluginDependency dependency) {
        return manager.getPlugin(dependency.getName()) != null;
    }
});
```

### 3. 鐑噸杞介厤缃?
```java
import ltd.idcu.est.hotreload.HotReloader;
import ltd.idcu.est.hotreload.HotReloadConfig;

HotReloadConfig config = HotReloadConfig.builder()
    .watchInterval(1000) // 1 绉掓鏌ヤ竴娆?    .includePackages("ltd.idcu.est.demo")
    .excludePackages("ltd.idcu.est.demo.excluded")
    .build();

HotReloader reloader = HotReloader.create(config);
reloader.watch(Paths.get("./src/main/java"));
reloader.start();
```

---

## 鏈€浣冲疄璺?
### 1. 瀹氭椂浠诲姟寮傚父澶勭悊

```java
// 鉁?鎺ㄨ崘锛氭崟鑾峰紓甯革紝璁板綍鏃ュ織
scheduler.schedule(new FixedRate(5000), () -> {
    try {
        doSomething();
    } catch (Exception e) {
        System.err.println("瀹氭椂浠诲姟鎵ц澶辫触: " + e.getMessage());
    }
});

// 鉂?涓嶆帹鑽愶細涓嶅鐞嗗紓甯?scheduler.schedule(new FixedRate(5000), () -> {
    doSomething(); // 寮傚父浼氬鑷翠换鍔″仠姝?});
```

### 2. 鎻掍欢鐗堟湰绠＄悊

```java
// 鉁?鎺ㄨ崘锛氭槑纭増鏈姹?public class MyPlugin implements Plugin {
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public PluginDependency[] getDependencies() {
        return new PluginDependency[]{
            new PluginDependency("core-plugin", ">=1.0.0,<2.1.0")
        };
    }
}

// 鉂?涓嶆帹鑽愶細涓嶆寚瀹氱増鏈?public class BadPlugin implements Plugin {
    @Override
    public String getVersion() {
        return "unknown";
    }
}
```

### 3. 鐑噸杞戒娇鐢ㄥ満鏅?
```java
// 鉁?鎺ㄨ崘锛氬紑鍙戠幆澧冧娇鐢ㄧ儹閲嶈浇
if (isDevelopment) {
    HotReloader reloader = HotReloader.create();
    reloader.watch(Paths.get("./src"));
    reloader.start();
}

// 鉂?涓嶆帹鑽愶細鐢熶骇鐜浣跨敤鐑噸杞?// 鐢熶骇鐜搴旇浣跨敤瀹屾暣鐨勯儴缃叉祦绋?```

---

## 妯″潡缁撴瀯

```
est-extensions/
鈹溾攢鈹€ est-scheduler/    # 瀹氭椂璋冨害锛圕ron銆丗ixed锛?鈹溾攢鈹€ est-plugin/       # 鎻掍欢绯荤粺
鈹斺攢鈹€ est-hotreload/    # 鐑噸杞?```

---

## 鐩稿叧璧勬簮

- [est-scheduler README](./est-scheduler/README.md) - 瀹氭椂璋冨害璇︾粏鏂囨。
- [est-plugin README](./est-plugin/README.md) - 鎻掍欢绯荤粺璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-advanced/) - 楂樼骇绀轰緥
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
