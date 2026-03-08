# est-audit - 小白从入门到精通

## 目录
- [什么是 est-audit](#什么是-est-audit)
- [快速入门：5分钟上手](#快速入门5分钟上手)
- [基础篇：核心功能](#基础篇核心功能)
- [进阶篇：高级用法](#进阶篇高级用法)
- [最佳实践](#最佳实践)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-audit

### 用大白话理解
est-audit 就像公司的"监控摄像头"，记录谁在什么时候做了什么事。比如谁登录了系统、谁修改了数据、谁删除了文件，所有操作都有记录可查。

### 核心特点
- **自动记录**：注解式审计，无需手动写日志
- **多存储支持**：数据库、文件、Elasticsearch
- **数据变更追踪**：记录修改前后的数据对比
- **查询分析**：强大的审计日志查询和统计

---

## 快速入门：5分钟上手

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-audit</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置审计
```yaml
est:
  security:
    audit:
      enabled: true
      store-type: jdbc  # 存储类型：jdbc/file/elasticsearch
      include-ip: true  # 记录 IP 地址
      include-user-agent: true  # 记录 User-Agent
```

### 3. 使用审计注解
```java
@Controller
public class UserController {
    
    @Post("/users")
    @Audit(action = "创建用户", resource = "用户")
    public User createUser(@Body User user) {
        return userService.create(user);
    }
    
    @Put("/users/{id}")
    @Audit(action = "修改用户", resource = "用户")
    public User updateUser(@PathParam Long id, @Body User user) {
        return userService.update(id, user);
    }
    
    @Delete("/users/{id}")
    @Audit(action = "删除用户", resource = "用户")
    public void deleteUser(@PathParam Long id) {
        userService.delete(id);
    }
}
```

### 4. 查询审计日志
```java
@Service
public class AuditLogService {
    
    @Inject
    private AuditLogRepository auditLogRepository;
    
    public List<AuditLog> findByUser(String username) {
        return auditLogRepository.findByUsername(username);
    }
    
    public List<AuditLog> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimeBetween(start, end);
    }
}
```

---

## 基础篇：核心功能

### 1. @Audit 注解详解

#### 基本用法
```java
@Audit(
    action = "创建订单",
    resource = "订单",
    module = "订单管理",
    description = "创建新订单"
)
public Order createOrder(@Body Order order) {
    return orderService.create(order);
}
```

#### SpEL 表达式支持
```java
@Audit(
    action = "修改用户",
    resource = "用户",
    resourceId = "#id",
    description = "'修改用户: ' + #user.username"
)
public User updateUser(@PathParam Long id, @Body User user) {
    return userService.update(id, user);
}

@Audit(
    action = "删除订单",
    resource = "订单",
    resourceId = "#orderId",
    condition = "#orderId != null"
)
public void deleteOrder(@PathParam Long orderId) {
    orderService.delete(orderId);
}
```

#### 忽略审计
```java
@Audit(ignore = true)
public void internalOperation() {
    // 这个操作不会被记录
}

@Audit(ignoreResult = true)
public List<User> getAllUsers() {
    // 记录操作，但不记录返回结果
    return userService.findAll();
}
```

### 2. 数据变更追踪

#### 记录实体变更
```java
@Entity
@Auditable
public class User {
    
    @Id
    private Long id;
    
    @AuditField
    private String username;
    
    @AuditField(label = "邮箱")
    private String email;
    
    @AuditField(mask = true)  // 脱敏
    private String password;
    
    @NotAudited  // 不记录此字段
    private String tempData;
}
```

#### 自动记录变更
```java
@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    @AuditEntity(action = "修改用户")
    public User updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id);
        
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        
        return userRepository.save(user);
    }
}
```

#### 获取变更记录
```java
@Service
public class EntityAuditService {
    
    @Inject
    private EntityAuditRepository entityAuditRepository;
    
    public List<EntityAudit> getAuditHistory(Class<?> entityClass, Object entityId) {
        return entityAuditRepository.findByEntityTypeAndEntityId(
            entityClass.getName(),
            String.valueOf(entityId)
        );
    }
    
    public EntityDiff getDiff(Long auditId) {
        EntityAudit audit = entityAuditRepository.findById(auditId);
        return audit.getDiff();
    }
}
```

### 3. 审计日志查询

#### 简单查询
```java
@Service
public class AuditQueryService {
    
    @Inject
    private AuditLogRepository auditLogRepository;
    
    public List<AuditLog> findRecentLogs(int limit) {
        return auditLogRepository.findTopNByOrderByTimeDesc(limit);
    }
    
    public List<AuditLog> findByUser(String username) {
        return auditLogRepository.findByUsernameOrderByTimeDesc(username);
    }
    
    public List<AuditLog> findByAction(String action) {
        return auditLogRepository.findByActionContaining(action);
    }
}
```

#### 复杂查询
```java
@Service
public class AuditSearchService {
    
    public Page<AuditLog> search(AuditQueryCriteria criteria) {
        Specification<AuditLog> spec = Specification.where(null);
        
        if (criteria.getUsername() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("username"), criteria.getUsername())
            );
        }
        
        if (criteria.getStartTime() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("time"), criteria.getStartTime())
            );
        }
        
        if (criteria.getEndTime() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("time"), criteria.getEndTime())
            );
        }
        
        if (criteria.getAction() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.like(root.get("action"), "%" + criteria.getAction() + "%")
            );
        }
        
        return auditLogRepository.findAll(spec, criteria.getPageable());
    }
}
```

#### 统计分析
```java
@Service
public class AuditStatsService {
    
    public AuditStats getStats(LocalDateTime start, LocalDateTime end) {
        return AuditStats.builder()
            .totalLogs(auditLogRepository.countByTimeBetween(start, end))
            .userCount(auditLogRepository.countDistinctUsernameByTimeBetween(start, end))
            .actionStats(getActionStats(start, end))
            .userStats(getUserStats(start, end))
            .build();
    }
    
    public List<ActionStat> getActionStats(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.groupByAction(start, end);
    }
    
    public List<UserStat> getUserStats(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.groupByUsername(start, end);
    }
}
```

### 4. 审计上下文

#### 获取当前审计上下文
```java
@Service
public class CustomAuditService {
    
    public void customAudit(String action, String resource) {
        AuditContext context = AuditContext.getCurrent();
        
        AuditLog log = AuditLog.builder()
            .action(action)
            .resource(resource)
            .username(context.getUsername())
            .ip(context.getIp())
            .userAgent(context.getUserAgent())
            .time(LocalDateTime.now())
            .build();
        
        auditLogRepository.save(log);
    }
}
```

#### 自定义审计上下文
```java
@Component
public class CustomAuditContextProvider implements AuditContextProvider {
    
    @Override
    public AuditContext getContext() {
        User user = SecurityContext.getCurrentUser();
        HttpServletRequest request = RequestContext.getRequest();
        
        return AuditContext.builder()
            .userId(user != null ? user.getId() : null)
            .username(user != null ? user.getUsername() : "anonymous")
            .ip(getClientIp(request))
            .userAgent(request != null ? request.getHeader("User-Agent") : null)
            .tenantId(TenantContext.getCurrentTenantId())
            .build();
    }
}
```

---

## 进阶篇：高级用法

### 1. 多种存储后端

#### 数据库存储（默认）
```yaml
est:
  security:
    audit:
      store-type: jdbc
      table-name: est_audit_log
      entity-table-name: est_entity_audit
```

#### 文件存储
```yaml
est:
  security:
    audit:
      store-type: file
      file:
        path: /var/log/est/audit
        filename-pattern: audit-%d{yyyy-MM-dd}.log
        max-size: 100MB
        max-history: 30
```

#### Elasticsearch 存储
```yaml
est:
  security:
    audit:
      store-type: elasticsearch
      elasticsearch:
        hosts: localhost:9200
        index-pattern: est-audit-%{+yyyy.MM.dd}
        batch-size: 1000
        flush-interval: 5s
```

### 2. 异步审计

#### 配置异步审计
```yaml
est:
  security:
    audit:
      async:
        enabled: true
        thread-pool-size: 4
        queue-capacity: 10000
```

#### 异步审计监听器
```java
@Component
public class AsyncAuditListener {
    
    @Inject
    private AuditLogRepository auditLogRepository;
    
    @OnAuditEvent(async = true)
    public void onAuditEvent(AuditEvent event) {
        AuditLog log = event.getAuditLog();
        
        auditLogRepository.save(log);
        
        if (log.getAction().contains("删除")) {
            alertService.sendAlert("敏感操作: " + log.getAction());
        }
    }
}
```

### 3. 审计事件订阅

#### 审计事件监听
```java
@Component
public class AuditEventSubscriber {
    
    @OnAuditCreate
    public void onAuditCreate(AuditLog auditLog) {
        System.out.println("审计日志创建: " + auditLog.getAction());
    }
    
    @OnAuditCreate(actions = {"删除用户", "删除订单"})
    public void onSensitiveOperation(AuditLog auditLog) {
        alertService.notifyAdmin("敏感操作: " + auditLog);
    }
    
    @OnAuditCreate(resources = {"用户", "订单"})
    public void onResourceChange(AuditLog auditLog) {
        cacheService.evict(auditLog.getResource(), auditLog.getResourceId());
    }
}
```

### 4. 审计数据导出

#### 导出 Excel
```java
@Service
public class AuditExportService {
    
    public byte[] exportToExcel(LocalDateTime start, LocalDateTime end) {
        List<AuditLog> logs = auditLogRepository.findByTimeBetween(start, end);
        
        ExcelWorkbook workbook = new ExcelWorkbook();
        ExcelSheet sheet = workbook.createSheet("审计日志");
        
        sheet.setHeaders("时间", "用户", "操作", "资源", "IP", "结果");
        
        logs.forEach(log -> {
            sheet.addRow(
                log.getTime(),
                log.getUsername(),
                log.getAction(),
                log.getResource(),
                log.getIp(),
                log.isSuccess() ? "成功" : "失败"
            );
        });
        
        return workbook.toBytes();
    }
}
```

#### 导出 PDF
```java
@Service
public class AuditReportService {
    
    public byte[] generateReport(LocalDateTime start, LocalDateTime end) {
        AuditStats stats = auditStatsService.getStats(start, end);
        List<AuditLog> logs = auditLogRepository.findByTimeBetween(start, end);
        
        PdfReport report = PdfReport.builder()
            .title("审计报告")
            .period(start, end)
            .stats(stats)
            .topActions(stats.getActionStats().subList(0, 10))
            .sampleLogs(logs.subList(0, 100))
            .build();
        
        return report.generate();
    }
}
```

---

## 最佳实践

### ✅ 推荐做法

| 场景 | 推荐做法 | 说明 |
|------|---------|------|
| 审计范围 | 关键操作必须审计 | 增删改、登录、权限变更 |
| 存储选择 | 生产用 ES，开发用 JDBC | ES 查询快，JDBC 简单 |
| 性能优化 | 启用异步审计 | 不影响主业务性能 |
| 日志保留 | 定期归档清理 | 避免无限增长 |
| 敏感数据 | 脱敏处理 | 密码、身份证号等 |

### ❌ 不推荐做法

```java
// ❌ 不要在审计日志中存敏感数据
@Audit
public void login(String username, String password) {
    // password 会被记录到审计日志！
}

// ✅ 应该标记为忽略或脱敏
@Audit(ignoreArgs = {"password"})
public void login(String username, String password) {
    // 好的，password 不会被记录
}
```

---

## 模块结构

```
est-audit/
├── est-audit-api/            # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/security/audit/
│   │       ├── AuditLog.java          # 审计日志
│   │       ├── EntityAudit.java       # 实体审计
│   │       ├── AuditService.java      # 审计服务
│   │       └── annotations/           # 审计注解
│   └── pom.xml
├── est-audit-impl/           # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/security/audit/
│   │       ├── JdbcAuditStore.java
│   │       ├── FileAuditStore.java
│   │       ├── ElasticsearchAuditStore.java
│   │       └── DefaultAuditService.java
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [安全认证模块](../est-security/README.md)
- [RBAC 权限模块](../est-rbac/README.md)
- [EST 安全指南](../../docs/security/README.md)
- [示例代码](../../est-examples/est-examples-security/)
