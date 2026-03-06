package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.impl.DefaultContainer;

public class Core06_LifecycleExample {
    public static void main(String[] args) {
        System.out.println("=== 高级篇：生命周期管理 ===\n");
        
        Container container = new DefaultContainer();
        container.register(DatabaseService.class, DatabaseServiceImpl.class);
        
        System.out.println("1. 获取数据库服务...");
        DatabaseService db = container.get(DatabaseService.class);
        
        System.out.println("\n2. 使用服务...");
        db.query("SELECT * FROM users");
        
        System.out.println("\n3. 关闭容器...");
        container.close();
    }
}

interface DatabaseService {
    void query(String sql);
}

class DatabaseServiceImpl implements DatabaseService {
    
    @PostConstruct
    public void init() {
        System.out.println("   [@PostConstruct] 数据库连接已建立");
    }
    
    @Override
    public void query(String sql) {
        System.out.println("   执行 SQL：" + sql);
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("   [@PreDestroy] 数据库连接已关闭");
    }
}
