package ltd.idcu.est.examples.basic.patterns;

import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

public class Patterns01_FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Patterns 5分钟上手 ===");
        System.out.println();

        Singleton<DatabaseConnection> singleton = 
            DefaultSingleton.of(DatabaseConnection::new);
        
        DatabaseConnection conn1 = singleton.getInstance();
        DatabaseConnection conn2 = singleton.getInstance();
        
        System.out.println("是否是同一个实例: " + (conn1 == conn2));
        
        System.out.println();
        System.out.println("恭喜！你已经学会使用第一个设计模式了！ 🎉");
    }
}

class DatabaseConnection {
    public DatabaseConnection() {
        System.out.println("数据库连接已创建");
    }
}
