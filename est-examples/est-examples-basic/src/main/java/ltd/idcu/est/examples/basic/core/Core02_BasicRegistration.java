package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.Optional;

public class Core02_BasicRegistration {
    public static void main(String[] args) {
        System.out.println("=== 基础篇：注册和获取组件 ===\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("--- 方式一：注册实现类 ---");
        container.register(UserService.class, UserServiceImpl.class);
        UserService userService = container.get(UserService.class);
        System.out.println(userService.getUserInfo());
        
        System.out.println("\n--- 方式二：注册单例 ---");
        AppConfig config = new AppConfig("我的应用", "1.0.0");
        container.registerSingleton(AppConfig.class, config);
        AppConfig containerConfig = container.get(AppConfig.class);
        System.out.println("应用名：" + containerConfig.getName());
        System.out.println("版本：" + containerConfig.getVersion());
        
        System.out.println("\n--- 方式三：安全获取 ---");
        Optional<UserService> optional = container.getIfPresent(UserService.class);
        if (optional.isPresent()) {
            System.out.println("找到了服务！");
        }
        
        if (container.contains(UserService.class)) {
            System.out.println("服务已注册");
        }
    }
}

interface UserService {
    String getUserInfo();
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserInfo() {
        return "用户信息：张三";
    }
}

class AppConfig {
    private String name;
    private String version;
    
    public AppConfig(String name, String version) {
        this.name = name;
        this.version = version;
    }
    
    public String getName() { return name; }
    public String getVersion() { return version; }
}
