# est-rbac - 小白从入门到精通

## 目录
- [什么是 est-rbac](#什么是-est-rbac)
- [快速入门：5分钟上手](#快速入门5分钟上手)
- [基础篇：核心功能](#基础篇核心功能)
- [进阶篇：高级用法](#进阶篇高级用法)
- [最佳实践](#最佳实践)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-rbac

### 用大白话理解
est-rbac 就像公司的"权限管理系统"。每个人（用户）有不同的职位（角色），每个职位能做不同的事（权限）。比如"员工"只能看自己的资料，"经理"能审批申请，"管理员"能管理所有人。

### 核心特点
- **标准 RBAC 模型**：用户-角色-权限三层架构
- **权限继承**：支持角色层级和权限继承
- **动态权限**：运行时动态调整权限
- **注解支持**：声明式权限控制

---

## 快速入门：5分钟上手

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-rbac</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置 RBAC
```yaml
est:
  security:
    rbac:
      enabled: true
      default-role: USER
      super-admin-role: ADMIN
```

### 3. 定义权限和角色
```java
@Configuration
public class RbacConfig {
    
    @Bean
    public PermissionInitializer permissionInitializer() {
        return registry -> {
            registry.definePermission("user:view", "查看用户");
            registry.definePermission("user:create", "创建用户");
            registry.definePermission("user:edit", "编辑用户");
            registry.definePermission("user:delete", "删除用户");
            
            registry.defineRole("USER")
                .addPermission("user:view");
            
            registry.defineRole("MANAGER")
                .inherit("USER")
                .addPermission("user:create")
                .addPermission("user:edit");
            
            registry.defineRole("ADMIN")
                .inherit("MANAGER")
                .addPermission("user:delete");
        };
    }
}
```

### 4. 使用权限控制
```java
@Controller
public class UserController {
    
    @Get("/users")
    @RequirePermission("user:view")
    public List<User> listUsers() {
        return userService.findAll();
    }
    
    @Post("/users")
    @RequireRole("MANAGER")
    public User createUser(@Body User user) {
        return userService.create(user);
    }
    
    @Delete("/users/{id}")
    @RequireRole("ADMIN")
    public void deleteUser(@PathParam Long id) {
        userService.delete(id);
    }
}
```

---

## 基础篇：核心功能

### 1. 用户-角色-权限模型

#### 用户管理
```java
@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private RoleService roleService;
    
    public User createUser(String username, String password) {
        User user = new User(username, password);
        user.addRole(roleService.findByName("USER"));
        return userRepository.save(user);
    }
    
    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId);
        Role role = roleService.findByName(roleName);
        user.addRole(role);
        userRepository.save(user);
    }
}
```

#### 角色管理
```java
@Service
public class RoleService {
    
    @Inject
    private RoleRepository roleRepository;
    
    public Role createRole(String name, String description) {
        Role role = new Role(name, description);
        return roleRepository.save(role);
    }
    
    public void addPermission(String roleName, String permissionCode) {
        Role role = roleRepository.findByName(roleName);
        Permission permission = permissionService.findByCode(permissionCode);
        role.addPermission(permission);
        roleRepository.save(role);
    }
    
    public void setParentRole(String roleName, String parentRoleName) {
        Role role = roleRepository.findByName(roleName);
        Role parent = roleRepository.findByName(parentRoleName);
        role.setParent(parent);
        roleRepository.save(role);
    }
}
```

#### 权限管理
```java
@Service
public class PermissionService {
    
    @Inject
    private PermissionRepository permissionRepository;
    
    public Permission definePermission(String code, String name) {
        Permission permission = new Permission(code, name);
        return permissionRepository.save(permission);
    }
    
    public boolean hasPermission(User user, String permissionCode) {
        return user.getRoles().stream()
            .flatMap(role -> getAllPermissions(role).stream())
            .anyMatch(p -> p.getCode().equals(permissionCode));
    }
    
    private Set<Permission> getAllPermissions(Role role) {
        Set<Permission> permissions = new HashSet<>(role.getPermissions());
        if (role.getParent() != null) {
            permissions.addAll(getAllPermissions(role.getParent()));
        }
        return permissions;
    }
}
```

### 2. 注解式权限控制

#### @RequirePermission
```java
@Controller
public class OrderController {
    
    @Get("/orders")
    @RequirePermission("order:view")
    public List<Order> listOrders() {
        return orderService.findAll();
    }
    
    @Post("/orders")
    @RequirePermission("order:create")
    public Order createOrder(@Body Order order) {
        return orderService.create(order);
    }
    
    @Put("/orders/{id}")
    @RequirePermission("order:edit")
    public Order updateOrder(@PathParam Long id, @Body Order order) {
        return orderService.update(id, order);
    }
    
    @Delete("/orders/{id}")
    @RequirePermission("order:delete")
    public void deleteOrder(@PathParam Long id) {
        orderService.delete(id);
    }
}
```

#### @RequireRole
```java
@Controller
public class AdminController {
    
    @Get("/admin/dashboard")
    @RequireRole("ADMIN")
    public Dashboard getDashboard() {
        return adminService.getDashboard();
    }
    
    @Get("/admin/settings")
    @RequireAnyRole({"ADMIN", "MANAGER"})
    public Settings getSettings() {
        return adminService.getSettings();
    }
    
    @Post("/admin/system")
    @RequireAllRoles({"ADMIN", "SYSTEM_ADMIN"})
    public void updateSystemConfig(@Body Config config) {
        adminService.updateConfig(config);
    }
}
```

#### @RequireUser
```java
@Controller
public class ProfileController {
    
    @Get("/profile")
    @RequireUser
    public Profile getProfile() {
        return profileService.getCurrentUserProfile();
    }
    
    @Put("/profile/{id}")
    @RequireUser
    public void updateProfile(@PathParam Long id, @Body Profile profile) {
        User currentUser = SecurityContext.getCurrentUser();
        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("只能修改自己的资料");
        }
        profileService.update(id, profile);
    }
}
```

### 3. 编程式权限检查

#### 基础检查
```java
@Service
public class DocumentService {
    
    @Inject
    private RbacService rbacService;
    
    public void viewDocument(Long docId) {
        User currentUser = SecurityContext.getCurrentUser();
        
        if (!rbacService.hasPermission(currentUser, "document:view")) {
            throw new AccessDeniedException("没有查看权限");
        }
    }
    
    public void editDocument(Document doc) {
        User currentUser = SecurityContext.getCurrentUser();
        
        if (doc.getOwnerId().equals(currentUser.getId())) {
            return;
        }
        
        if (!rbacService.hasRole(currentUser, "MANAGER")) {
            throw new AccessDeniedException("没有编辑权限");
        }
    }
}
```

#### 复杂权限表达式
```java
@Service
public class ApprovalService {
    
    @Inject
    private RbacService rbacService;
    
    public void approve(Approval approval) {
        User currentUser = SecurityContext.getCurrentUser();
        
        boolean canApprove = rbacService.evaluate(currentUser, 
            "(hasRole('MANAGER') and approval.amount < 10000) or " +
            "(hasRole('DIRECTOR') and approval.amount < 100000) or " +
            "hasRole('CEO')"
        );
        
        if (!canApprove) {
            throw new AccessDeniedException("没有审批权限");
        }
    }
}
```

### 4. 权限监听器

#### 权限变更监听
```java
@Component
public class PermissionChangeListener {
    
    @OnPermissionGranted
    public void onPermissionGranted(PermissionGrantedEvent event) {
        User user = event.getUser();
        Permission permission = event.getPermission();
        auditLogService.log("用户 " + user.getUsername() + 
                           " 获得权限 " + permission.getCode());
    }
    
    @OnPermissionRevoked
    public void onPermissionRevoked(PermissionRevokedEvent event) {
        User user = event.getUser();
        Permission permission = event.getPermission();
        auditLogService.log("用户 " + user.getUsername() + 
                           " 失去权限 " + permission.getCode());
    }
    
    @OnRoleAssigned
    public void onRoleAssigned(RoleAssignedEvent event) {
        User user = event.getUser();
        Role role = event.getRole();
        auditLogService.log("用户 " + user.getUsername() + 
                           " 被分配角色 " + role.getName());
    }
}
```

---

## 进阶篇：高级用法

### 1. 数据权限控制

#### 行级权限
```java
@Entity
@DataPermission
public class Order {
    
    @Id
    private Long id;
    
    @DataPermissionOwner
    private Long ownerId;
    
    @DataPermissionDept
    private Long deptId;
    
    private String status;
    private BigDecimal amount;
}
```

```java
@Service
public class OrderService {
    
    @Inject
    private DataPermissionService dataPermissionService;
    
    public List<Order> findOrders() {
        User currentUser = SecurityContext.getCurrentUser();
        DataPermissionRule rule = dataPermissionService.getRule(currentUser, "order");
        
        Specification<Order> spec = rule.toSpecification();
        return orderRepository.findAll(spec);
    }
}
```

#### 字段级权限
```java
@Entity
public class User {
    
    @Id
    private Long id;
    
    private String username;
    
    @FieldPermission(roles = {"ADMIN", "HR"})
    private String salary;
    
    @FieldPermission(permissions = {"user:view:contact"})
    private String phone;
    
    @FieldPermission(permissions = {"user:view:private"})
    private String homeAddress;
}
```

### 2. 动态权限加载

#### 从数据库加载权限
```java
@Component
public class DatabasePermissionLoader implements PermissionLoader {
    
    @Inject
    private PermissionRepository permissionRepository;
    
    @Inject
    private RoleRepository roleRepository;
    
    @Override
    public void load(RbacRegistry registry) {
        List<Permission> permissions = permissionRepository.findAll();
        permissions.forEach(p -> 
            registry.definePermission(p.getCode(), p.getName())
        );
        
        List<Role> roles = roleRepository.findAll();
        roles.forEach(role -> {
            RoleBuilder builder = registry.defineRole(role.getName());
            if (role.getParent() != null) {
                builder.inherit(role.getParent().getName());
            }
            role.getPermissions().forEach(p -> 
                builder.addPermission(p.getCode())
            );
        });
    }
}
```

#### 权限热更新
```java
@Service
public class RbacRefreshService {
    
    @Inject
    private RbacService rbacService;
    
    @Inject
    private PermissionCache permissionCache;
    
    @Schedule(interval = 300)  // 每5分钟刷新
    public void refreshPermissions() {
        permissionCache.clear();
        rbacService.reload();
    }
    
    public void refreshUserPermissions(Long userId) {
        permissionCache.clearUserCache(userId);
    }
}
```

### 3. 多租户权限隔离

#### 租户级权限
```java
@Service
public class TenantRbacService {
    
    @Inject
    private TenantContext tenantContext;
    
    public boolean hasPermission(String permissionCode) {
        Long tenantId = tenantContext.getCurrentTenantId();
        User user = SecurityContext.getCurrentUser();
        
        return rbacService.hasPermission(tenantId, user, permissionCode);
    }
    
    public Set<String> getUserPermissions() {
        Long tenantId = tenantContext.getCurrentTenantId();
        User user = SecurityContext.getCurrentUser();
        
        return rbacService.getUserPermissions(tenantId, user);
    }
}
```

---

## 最佳实践

### ✅ 推荐做法

| 场景 | 推荐做法 | 说明 |
|------|---------|------|
| 权限设计 | 细粒度权限 + 粗粒度角色 | 权限细分，角色组合 |
| 命名规范 | module:action:resource | user:create:order |
| 权限检查 | 优先用注解，复杂用代码 | 注解简洁，代码灵活 |
| 缓存策略 | 权限数据需要缓存 | 减少数据库查询 |
| 审计日志 | 记录所有权限变更 | 便于追溯和审计 |

### ❌ 不推荐做法

```java
// ❌ 不要硬编码权限检查
if (user.getRole().equals("ADMIN")) {
    // 直接放行
}

// ✅ 应该使用统一的权限服务
if (rbacService.hasPermission(user, "user:delete")) {
    // 通过权限检查
}
```

---

## 模块结构

```
est-rbac/
├── est-rbac-api/             # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/security/rbac/
│   │       ├── User.java              # 用户接口
│   │       ├── Role.java              # 角色接口
│   │       ├── Permission.java        # 权限接口
│   │       ├── RbacService.java       # RBAC 服务
│   │       └── annotations/           # 权限注解
│   └── pom.xml
├── est-rbac-impl/            # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/security/rbac/
│   │       ├── DefaultUser.java
│   │       ├── DefaultRole.java
│   │       ├── DefaultPermission.java
│   │       └── DefaultRbacService.java
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [安全认证模块](../est-security/README.md)
- [审计日志模块](../est-audit/README.md)
- [EST 安全指南](../../docs/security/README.md)
- [示例代码](../../est-examples/est-examples-security/)
