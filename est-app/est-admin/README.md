# est-admin - 小白从入门到精通

## 目录
- [什么是 est-admin](#什么是-est-admin)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-admin

### 用大白话理解
est-admin 就像"后台管理系统"的骨架，提供用户管理、角色管理、权限管理、菜单管理等常用功能，开箱即用。

### 核心特点
- **用户管理**：用户CRUD、状态管理
- **角色管理**：角色分配、权限配置
- **权限管理**：菜单权限、数据权限
- **审计日志**：操作记录、登录日志

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-admin</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置管理后台
```java
@EnableAdmin
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        EstApplication.run(AdminApplication.class, args);
    }
}
```

---

## 核心功能

### 用户管理
```java
@Controller
public class AdminUserController {
    
    @Get("/admin/users")
    public Page<User> listUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }
    
    @Post("/admin/users")
    public User createUser(@Body User user) {
        return userService.create(user);
    }
}
```

### 角色管理
```java
@Controller
public class AdminRoleController {
    
    @Get("/admin/roles")
    public List<Role> listRoles() {
        return roleService.findAll();
    }
    
    @Post("/admin/roles")
    public Role createRole(@Body Role role) {
        return roleService.create(role);
    }
}
```

---

## 模块结构

```
est-admin/
├── est-admin-api/          # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/app/admin/
│   └── pom.xml
├── est-admin-impl/         # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/app/admin/
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [RBAC 模块](../../est-modules/est-security-group/est-rbac/README.md)
- [审计日志](../../est-modules/est-security-group/est-audit/README.md)
