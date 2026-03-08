# est-rbac - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-rbac](#浠€涔堟槸-est-rbac)
- [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
- [鍩虹绡囷細鏍稿績鍔熻兘](#鍩虹绡囨牳蹇冨姛鑳?
- [杩涢樁绡囷細楂樼骇鐢ㄦ硶](#杩涢樁绡囬珮绾х敤娉?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-rbac

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-rbac 灏卞儚鍏徃鐨?鏉冮檺绠＄悊绯荤粺"銆傛瘡涓汉锛堢敤鎴凤級鏈変笉鍚岀殑鑱屼綅锛堣鑹诧級锛屾瘡涓亴浣嶈兘鍋氫笉鍚岀殑浜嬶紙鏉冮檺锛夈€傛瘮濡?鍛樺伐"鍙兘鐪嬭嚜宸辩殑璧勬枡锛?缁忕悊"鑳藉鎵圭敵璇凤紝"绠＄悊鍛?鑳界鐞嗘墍鏈変汉銆?
### 鏍稿績鐗圭偣
- **鏍囧噯 RBAC 妯″瀷**锛氱敤鎴?瑙掕壊-鏉冮檺涓夊眰鏋舵瀯
- **鏉冮檺缁ф壙**锛氭敮鎸佽鑹插眰绾у拰鏉冮檺缁ф壙
- **鍔ㄦ€佹潈闄?*锛氳繍琛屾椂鍔ㄦ€佽皟鏁存潈闄?- **娉ㄨВ鏀寔**锛氬０鏄庡紡鏉冮檺鎺у埗

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-rbac</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆 RBAC
```yaml
est:
  security:
    rbac:
      enabled: true
      default-role: USER
      super-admin-role: ADMIN
```

### 3. 瀹氫箟鏉冮檺鍜岃鑹?```java
@Configuration
public class RbacConfig {
    
    @Bean
    public PermissionInitializer permissionInitializer() {
        return registry -> {
            registry.definePermission("user:view", "鏌ョ湅鐢ㄦ埛");
            registry.definePermission("user:create", "鍒涘缓鐢ㄦ埛");
            registry.definePermission("user:edit", "缂栬緫鐢ㄦ埛");
            registry.definePermission("user:delete", "鍒犻櫎鐢ㄦ埛");
            
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

### 4. 浣跨敤鏉冮檺鎺у埗
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

## 鍩虹绡囷細鏍稿績鍔熻兘

### 1. 鐢ㄦ埛-瑙掕壊-鏉冮檺妯″瀷

#### 鐢ㄦ埛绠＄悊
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

#### 瑙掕壊绠＄悊
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

#### 鏉冮檺绠＄悊
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

### 2. 娉ㄨВ寮忔潈闄愭帶鍒?
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
            throw new AccessDeniedException("鍙兘淇敼鑷繁鐨勮祫鏂?);
        }
        profileService.update(id, profile);
    }
}
```

### 3. 缂栫▼寮忔潈闄愭鏌?
#### 鍩虹妫€鏌?```java
@Service
public class DocumentService {
    
    @Inject
    private RbacService rbacService;
    
    public void viewDocument(Long docId) {
        User currentUser = SecurityContext.getCurrentUser();
        
        if (!rbacService.hasPermission(currentUser, "document:view")) {
            throw new AccessDeniedException("娌℃湁鏌ョ湅鏉冮檺");
        }
    }
    
    public void editDocument(Document doc) {
        User currentUser = SecurityContext.getCurrentUser();
        
        if (doc.getOwnerId().equals(currentUser.getId())) {
            return;
        }
        
        if (!rbacService.hasRole(currentUser, "MANAGER")) {
            throw new AccessDeniedException("娌℃湁缂栬緫鏉冮檺");
        }
    }
}
```

#### 澶嶆潅鏉冮檺琛ㄨ揪寮?```java
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
            throw new AccessDeniedException("娌℃湁瀹℃壒鏉冮檺");
        }
    }
}
```

### 4. 鏉冮檺鐩戝惉鍣?
#### 鏉冮檺鍙樻洿鐩戝惉
```java
@Component
public class PermissionChangeListener {
    
    @OnPermissionGranted
    public void onPermissionGranted(PermissionGrantedEvent event) {
        User user = event.getUser();
        Permission permission = event.getPermission();
        auditLogService.log("鐢ㄦ埛 " + user.getUsername() + 
                           " 鑾峰緱鏉冮檺 " + permission.getCode());
    }
    
    @OnPermissionRevoked
    public void onPermissionRevoked(PermissionRevokedEvent event) {
        User user = event.getUser();
        Permission permission = event.getPermission();
        auditLogService.log("鐢ㄦ埛 " + user.getUsername() + 
                           " 澶卞幓鏉冮檺 " + permission.getCode());
    }
    
    @OnRoleAssigned
    public void onRoleAssigned(RoleAssignedEvent event) {
        User user = event.getUser();
        Role role = event.getRole();
        auditLogService.log("鐢ㄦ埛 " + user.getUsername() + 
                           " 琚垎閰嶈鑹?" + role.getName());
    }
}
```

---

## 杩涢樁绡囷細楂樼骇鐢ㄦ硶

### 1. 鏁版嵁鏉冮檺鎺у埗

#### 琛岀骇鏉冮檺
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

#### 瀛楁绾ф潈闄?```java
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

### 2. 鍔ㄦ€佹潈闄愬姞杞?
#### 浠庢暟鎹簱鍔犺浇鏉冮檺
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

#### 鏉冮檺鐑洿鏂?```java
@Service
public class RbacRefreshService {
    
    @Inject
    private RbacService rbacService;
    
    @Inject
    private PermissionCache permissionCache;
    
    @Schedule(interval = 300)  // 姣?鍒嗛挓鍒锋柊
    public void refreshPermissions() {
        permissionCache.clear();
        rbacService.reload();
    }
    
    public void refreshUserPermissions(Long userId) {
        permissionCache.clearUserCache(userId);
    }
}
```

### 3. 澶氱鎴锋潈闄愰殧绂?
#### 绉熸埛绾ф潈闄?```java
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

## 鏈€浣冲疄璺?
### 鉁?鎺ㄨ崘鍋氭硶

| 鍦烘櫙 | 鎺ㄨ崘鍋氭硶 | 璇存槑 |
|------|---------|------|
| 鏉冮檺璁捐 | 缁嗙矑搴︽潈闄?+ 绮楃矑搴﹁鑹?| 鏉冮檺缁嗗垎锛岃鑹茬粍鍚?|
| 鍛藉悕瑙勮寖 | module:action:resource | user:create:order |
| 鏉冮檺妫€鏌?| 浼樺厛鐢ㄦ敞瑙ｏ紝澶嶆潅鐢ㄤ唬鐮?| 娉ㄨВ绠€娲侊紝浠ｇ爜鐏垫椿 |
| 缂撳瓨绛栫暐 | 鏉冮檺鏁版嵁闇€瑕佺紦瀛?| 鍑忓皯鏁版嵁搴撴煡璇?|
| 瀹¤鏃ュ織 | 璁板綍鎵€鏈夋潈闄愬彉鏇?| 渚夸簬杩芥函鍜屽璁?|

### 鉂?涓嶆帹鑽愬仛娉?
```java
// 鉂?涓嶈纭紪鐮佹潈闄愭鏌?if (user.getRole().equals("ADMIN")) {
    // 鐩存帴鏀捐
}

// 鉁?搴旇浣跨敤缁熶竴鐨勬潈闄愭湇鍔?if (rbacService.hasPermission(user, "user:delete")) {
    // 閫氳繃鏉冮檺妫€鏌?}
```

---

## 妯″潡缁撴瀯

```
est-rbac/
鈹溾攢鈹€ est-rbac-api/             # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/security/rbac/
鈹?  鈹?      鈹溾攢鈹€ User.java              # 鐢ㄦ埛鎺ュ彛
鈹?  鈹?      鈹溾攢鈹€ Role.java              # 瑙掕壊鎺ュ彛
鈹?  鈹?      鈹溾攢鈹€ Permission.java        # 鏉冮檺鎺ュ彛
鈹?  鈹?      鈹溾攢鈹€ RbacService.java       # RBAC 鏈嶅姟
鈹?  鈹?      鈹斺攢鈹€ annotations/           # 鏉冮檺娉ㄨВ
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-rbac-impl/            # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/security/rbac/
鈹?  鈹?      鈹溾攢鈹€ DefaultUser.java
鈹?  鈹?      鈹溾攢鈹€ DefaultRole.java
鈹?  鈹?      鈹溾攢鈹€ DefaultPermission.java
鈹?  鈹?      鈹斺攢鈹€ DefaultRbacService.java
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [瀹夊叏璁よ瘉妯″潡](../est-security/README.md)
- [瀹¤鏃ュ織妯″潡](../est-audit/README.md)
- [EST 瀹夊叏鎸囧崡](../../docs/security/README.md)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-security/)
