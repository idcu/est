# est-audit - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-audit](#浠€涔堟槸-est-audit)
- [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
- [鍩虹绡囷細鏍稿績鍔熻兘](#鍩虹绡囨牳蹇冨姛鑳?
- [杩涢樁绡囷細楂樼骇鐢ㄦ硶](#杩涢樁绡囬珮绾х敤娉?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-audit

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-audit 灏卞儚鍏徃鐨?鐩戞帶鎽勫儚澶?锛岃褰曡皝鍦ㄤ粈涔堟椂鍊欏仛浜嗕粈涔堜簨銆傛瘮濡傝皝鐧诲綍浜嗙郴缁熴€佽皝淇敼浜嗘暟鎹€佽皝鍒犻櫎浜嗘枃浠讹紝鎵€鏈夋搷浣滈兘鏈夎褰曞彲鏌ャ€?
### 鏍稿績鐗圭偣
- **鑷姩璁板綍**锛氭敞瑙ｅ紡瀹¤锛屾棤闇€鎵嬪姩鍐欐棩蹇?- **澶氬瓨鍌ㄦ敮鎸?*锛氭暟鎹簱銆佹枃浠躲€丒lasticsearch
- **鏁版嵁鍙樻洿杩借釜**锛氳褰曚慨鏀瑰墠鍚庣殑鏁版嵁瀵规瘮
- **鏌ヨ鍒嗘瀽**锛氬己澶х殑瀹¤鏃ュ織鏌ヨ鍜岀粺璁?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-audit</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆瀹¤
```yaml
est:
  security:
    audit:
      enabled: true
      store-type: jdbc  # 瀛樺偍绫诲瀷锛歫dbc/file/elasticsearch
      include-ip: true  # 璁板綍 IP 鍦板潃
      include-user-agent: true  # 璁板綍 User-Agent
```

### 3. 浣跨敤瀹¤娉ㄨВ
```java
@Controller
public class UserController {
    
    @Post("/users")
    @Audit(action = "鍒涘缓鐢ㄦ埛", resource = "鐢ㄦ埛")
    public User createUser(@Body User user) {
        return userService.create(user);
    }
    
    @Put("/users/{id}")
    @Audit(action = "淇敼鐢ㄦ埛", resource = "鐢ㄦ埛")
    public User updateUser(@PathParam Long id, @Body User user) {
        return userService.update(id, user);
    }
    
    @Delete("/users/{id}")
    @Audit(action = "鍒犻櫎鐢ㄦ埛", resource = "鐢ㄦ埛")
    public void deleteUser(@PathParam Long id) {
        userService.delete(id);
    }
}
```

### 4. 鏌ヨ瀹¤鏃ュ織
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

## 鍩虹绡囷細鏍稿績鍔熻兘

### 1. @Audit 娉ㄨВ璇﹁В

#### 鍩烘湰鐢ㄦ硶
```java
@Audit(
    action = "鍒涘缓璁㈠崟",
    resource = "璁㈠崟",
    module = "璁㈠崟绠＄悊",
    description = "鍒涘缓鏂拌鍗?
)
public Order createOrder(@Body Order order) {
    return orderService.create(order);
}
```

#### SpEL 琛ㄨ揪寮忔敮鎸?```java
@Audit(
    action = "淇敼鐢ㄦ埛",
    resource = "鐢ㄦ埛",
    resourceId = "#id",
    description = "'淇敼鐢ㄦ埛: ' + #user.username"
)
public User updateUser(@PathParam Long id, @Body User user) {
    return userService.update(id, user);
}

@Audit(
    action = "鍒犻櫎璁㈠崟",
    resource = "璁㈠崟",
    resourceId = "#orderId",
    condition = "#orderId != null"
)
public void deleteOrder(@PathParam Long orderId) {
    orderService.delete(orderId);
}
```

#### 蹇界暐瀹¤
```java
@Audit(ignore = true)
public void internalOperation() {
    // 杩欎釜鎿嶄綔涓嶄細琚褰?}

@Audit(ignoreResult = true)
public List<User> getAllUsers() {
    // 璁板綍鎿嶄綔锛屼絾涓嶈褰曡繑鍥炵粨鏋?    return userService.findAll();
}
```

### 2. 鏁版嵁鍙樻洿杩借釜

#### 璁板綍瀹炰綋鍙樻洿
```java
@Entity
@Auditable
public class User {
    
    @Id
    private Long id;
    
    @AuditField
    private String username;
    
    @AuditField(label = "閭")
    private String email;
    
    @AuditField(mask = true)  // 鑴辨晱
    private String password;
    
    @NotAudited  // 涓嶈褰曟瀛楁
    private String tempData;
}
```

#### 鑷姩璁板綍鍙樻洿
```java
@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    @AuditEntity(action = "淇敼鐢ㄦ埛")
    public User updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id);
        
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        
        return userRepository.save(user);
    }
}
```

#### 鑾峰彇鍙樻洿璁板綍
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

### 3. 瀹¤鏃ュ織鏌ヨ

#### 绠€鍗曟煡璇?```java
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

#### 澶嶆潅鏌ヨ
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

#### 缁熻鍒嗘瀽
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

### 4. 瀹¤涓婁笅鏂?
#### 鑾峰彇褰撳墠瀹¤涓婁笅鏂?```java
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

#### 鑷畾涔夊璁′笂涓嬫枃
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

## 杩涢樁绡囷細楂樼骇鐢ㄦ硶

### 1. 澶氱瀛樺偍鍚庣

#### 鏁版嵁搴撳瓨鍌紙榛樿锛?```yaml
est:
  security:
    audit:
      store-type: jdbc
      table-name: est_audit_log
      entity-table-name: est_entity_audit
```

#### 鏂囦欢瀛樺偍
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

#### Elasticsearch 瀛樺偍
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

### 2. 寮傛瀹¤

#### 閰嶇疆寮傛瀹¤
```yaml
est:
  security:
    audit:
      async:
        enabled: true
        thread-pool-size: 4
        queue-capacity: 10000
```

#### 寮傛瀹¤鐩戝惉鍣?```java
@Component
public class AsyncAuditListener {
    
    @Inject
    private AuditLogRepository auditLogRepository;
    
    @OnAuditEvent(async = true)
    public void onAuditEvent(AuditEvent event) {
        AuditLog log = event.getAuditLog();
        
        auditLogRepository.save(log);
        
        if (log.getAction().contains("鍒犻櫎")) {
            alertService.sendAlert("鏁忔劅鎿嶄綔: " + log.getAction());
        }
    }
}
```

### 3. 瀹¤浜嬩欢璁㈤槄

#### 瀹¤浜嬩欢鐩戝惉
```java
@Component
public class AuditEventSubscriber {
    
    @OnAuditCreate
    public void onAuditCreate(AuditLog auditLog) {
        System.out.println("瀹¤鏃ュ織鍒涘缓: " + auditLog.getAction());
    }
    
    @OnAuditCreate(actions = {"鍒犻櫎鐢ㄦ埛", "鍒犻櫎璁㈠崟"})
    public void onSensitiveOperation(AuditLog auditLog) {
        alertService.notifyAdmin("鏁忔劅鎿嶄綔: " + auditLog);
    }
    
    @OnAuditCreate(resources = {"鐢ㄦ埛", "璁㈠崟"})
    public void onResourceChange(AuditLog auditLog) {
        cacheService.evict(auditLog.getResource(), auditLog.getResourceId());
    }
}
```

### 4. 瀹¤鏁版嵁瀵煎嚭

#### 瀵煎嚭 Excel
```java
@Service
public class AuditExportService {
    
    public byte[] exportToExcel(LocalDateTime start, LocalDateTime end) {
        List<AuditLog> logs = auditLogRepository.findByTimeBetween(start, end);
        
        ExcelWorkbook workbook = new ExcelWorkbook();
        ExcelSheet sheet = workbook.createSheet("瀹¤鏃ュ織");
        
        sheet.setHeaders("鏃堕棿", "鐢ㄦ埛", "鎿嶄綔", "璧勬簮", "IP", "缁撴灉");
        
        logs.forEach(log -> {
            sheet.addRow(
                log.getTime(),
                log.getUsername(),
                log.getAction(),
                log.getResource(),
                log.getIp(),
                log.isSuccess() ? "鎴愬姛" : "澶辫触"
            );
        });
        
        return workbook.toBytes();
    }
}
```

#### 瀵煎嚭 PDF
```java
@Service
public class AuditReportService {
    
    public byte[] generateReport(LocalDateTime start, LocalDateTime end) {
        AuditStats stats = auditStatsService.getStats(start, end);
        List<AuditLog> logs = auditLogRepository.findByTimeBetween(start, end);
        
        PdfReport report = PdfReport.builder()
            .title("瀹¤鎶ュ憡")
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

## 鏈€浣冲疄璺?
### 鉁?鎺ㄨ崘鍋氭硶

| 鍦烘櫙 | 鎺ㄨ崘鍋氭硶 | 璇存槑 |
|------|---------|------|
| 瀹¤鑼冨洿 | 鍏抽敭鎿嶄綔蹇呴』瀹¤ | 澧炲垹鏀广€佺櫥褰曘€佹潈闄愬彉鏇?|
| 瀛樺偍閫夋嫨 | 鐢熶骇鐢?ES锛屽紑鍙戠敤 JDBC | ES 鏌ヨ蹇紝JDBC 绠€鍗?|
| 鎬ц兘浼樺寲 | 鍚敤寮傛瀹¤ | 涓嶅奖鍝嶄富涓氬姟鎬ц兘 |
| 鏃ュ織淇濈暀 | 瀹氭湡褰掓。娓呯悊 | 閬垮厤鏃犻檺澧為暱 |
| 鏁忔劅鏁版嵁 | 鑴辨晱澶勭悊 | 瀵嗙爜銆佽韩浠借瘉鍙风瓑 |

### 鉂?涓嶆帹鑽愬仛娉?
```java
// 鉂?涓嶈鍦ㄥ璁℃棩蹇椾腑瀛樻晱鎰熸暟鎹?@Audit
public void login(String username, String password) {
    // password 浼氳璁板綍鍒板璁℃棩蹇楋紒
}

// 鉁?搴旇鏍囪涓哄拷鐣ユ垨鑴辨晱
@Audit(ignoreArgs = {"password"})
public void login(String username, String password) {
    // 濂界殑锛宲assword 涓嶄細琚褰?}
```

---

## 妯″潡缁撴瀯

```
est-audit/
鈹溾攢鈹€ est-audit-api/            # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/security/audit/
鈹?  鈹?      鈹溾攢鈹€ AuditLog.java          # 瀹¤鏃ュ織
鈹?  鈹?      鈹溾攢鈹€ EntityAudit.java       # 瀹炰綋瀹¤
鈹?  鈹?      鈹溾攢鈹€ AuditService.java      # 瀹¤鏈嶅姟
鈹?  鈹?      鈹斺攢鈹€ annotations/           # 瀹¤娉ㄨВ
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-audit-impl/           # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/security/audit/
鈹?  鈹?      鈹溾攢鈹€ JdbcAuditStore.java
鈹?  鈹?      鈹溾攢鈹€ FileAuditStore.java
鈹?  鈹?      鈹溾攢鈹€ ElasticsearchAuditStore.java
鈹?  鈹?      鈹斺攢鈹€ DefaultAuditService.java
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [瀹夊叏璁よ瘉妯″潡](../est-security/README.md)
- [RBAC 鏉冮檺妯″潡](../est-rbac/README.md)
- [EST 瀹夊叏鎸囧崡](../../docs/security/README.md)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-security/)
