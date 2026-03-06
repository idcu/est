package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.HashMap;
import java.util.Map;

public class Core07_ConfigExample {
    public static void main(String[] args) {
        System.out.println("=== 高级篇：配置管理 ===\n");
        
        Map<String, Object> props = new HashMap<>();
        props.put("app.name", "我的应用");
        props.put("app.version", "1.0.0");
        props.put("server.port", 8080);
        props.put("debug", true);
        
        Config config = new DefaultConfig(props);
        
        String appName = config.getString("app.name");
        int port = config.getInt("server.port", 8080);
        boolean debug = config.getBoolean("debug", false);
        
        System.out.println("应用名：" + appName);
        System.out.println("端口：" + port);
        System.out.println("调试模式：" + debug);
        
        System.out.println("\n--- 在容器中使用配置 ---");
        Container container = new DefaultContainer(config);
        container.registerSingleton(Config.class, config);
        
        Config containerConfig = container.get(Config.class);
        System.out.println("从容器获取的应用名：" + containerConfig.getString("app.name"));
    }
}
