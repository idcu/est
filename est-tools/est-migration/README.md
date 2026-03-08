# est-migration - 小白从入门到精通

## 目录
- [什么是 est-migration](#什么是-est-migration)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-migration

### 用大白话理解
est-migration 就像"数据库搬家公司"，帮你管理数据库版本变更，创建表、修改字段、数据迁移，一套脚本搞定。

### 核心特点
- **版本管理**：按版本管理数据库变更
- **SQL脚本**：支持 SQL 脚本迁移
- **Java迁移**：支持 Java 代码迁移
- **回滚支持**：支持迁移回滚

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-migration</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 创建迁移脚本
```sql
-- db/migration/V1__Create_user_table.sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. 执行迁移
```bash
# 执行迁移
est migrate

# 查看状态
est migrate status

# 回滚到指定版本
est migrate rollback 1
```

---

## 核心功能

### Java 迁移
```java
@Migration(version = 2, description = "添加用户索引")
public class V2__Add_user_index implements JavaMigration {
    
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE INDEX idx_username ON user(username)");
        }
    }
}
```

### 迁移配置
```yaml
est:
  migration:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
```

---

## 模块结构

```
est-migration/
├── src/main/java/
│   └── ltd/idcu/est/tools/migration/
│       ├── migration/
│       └── resolver/
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [CLI 工具](../est-cli/README.md)
