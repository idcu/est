# est-compliance - 合规性支持模块

## 目录

1. [什么是 est-compliance？](#什么是-est-compliance)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 est-compliance？

est-compliance 是 EST 框架的合规性支持模块，提供 GDPR（通用数据保护条例）和等保（等级保护）相关的功能支持。

### 核心特性

- **GDPR 支持**
  - 数据主体权利管理
  - 同意管理
  - 数据脱敏
  - 数据主体请求处理

- **等保支持**
  - 安全事件日志
  - 安全审计日志
  - 访问控制策略
  - 合规性检查

---

## 快速入门：5分钟上手

### 第一步：引入依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-compliance</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

### 第二步：使用数据脱敏

```java
import ltd.idcu.est.compliance.api.gdpr.DataMasker;
import ltd.idcu.est.compliance.gdpr.DefaultDataMasker;

public class MaskingExample {
    public static void main(String[] args) {
        DataMasker masker = new DefaultDataMasker();
        
        System.out.println(masker.maskEmail("user@example.com"));
        System.out.println(masker.maskPhone("13800138000"));
        System.out.println(masker.maskName("张三"));
    }
}
```

### 第三步：管理用户同意

```java
import ltd.idcu.est.compliance.api.gdpr.Consent;
import ltd.idcu.est.compliance.api.gdpr.ConsentService;
import ltd.idcu.est.compliance.gdpr.DefaultConsentRequest;
import ltd.idcu.est.compliance.gdpr.DefaultConsentService;

public class ConsentExample {
    public static void main(String[] args) {
        ConsentService consentService = new DefaultConsentService();
        
        Consent consent = consentService.grantConsent(
            "user123",
            "marketing",
            DefaultConsentRequest.builder()
                .description("接收营销邮件")
                .build()
        );
        
        boolean hasConsent = consentService.hasConsent("user123", "marketing");
    }
}
```

---

## 基础篇

### 1. GDPR 数据主体权利

```java
import ltd.idcu.est.compliance.api.gdpr.*;
import ltd.idcu.est.compliance.gdpr.DefaultDataSubjectRequestService;
import ltd.idcu.est.compliance.gdpr.DefaultRequestData;

public class DataSubjectRightsExample {
    public static void main(String[] args) {
        DataSubjectRequestService requestService = new DefaultDataSubjectRequestService();
        
        DataSubjectRequest request = requestService.submitRequest(
            "user123",
            DataSubjectRight.RIGHT_TO_ACCESS,
            new DefaultRequestData("需要访问我的个人数据", null)
        );
        
        requestService.processRequest(
            request.getId(),
            RequestStatus.IN_PROGRESS,
            "正在处理中"
        );
    }
}
```

### 2. 安全事件日志

```java
import ltd.idcu.est.compliance.api.cybersecurity.*;
import ltd.idcu.est.compliance.cybersecurity.DefaultSecurityEventService;

public class SecurityEventExample {
    public static void main(String[] args) {
        SecurityEventService eventService = new DefaultSecurityEventService();
        
        eventService.logEvent(
            SecurityEventType.LOGIN_SUCCESS,
            SecurityEventSeverity.LOW,
            "auth-service",
            "用户登录成功",
            null
        );
        
        List<SecurityEvent> highEvents = eventService.getHighSeverityEvents();
    }
}
```

### 3. 安全审计日志

```java
import ltd.idcu.est.compliance.api.cybersecurity.SecurityAuditLog;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityAuditService;
import ltd.idcu.est.compliance.cybersecurity.DefaultSecurityAuditService;

public class AuditLogExample {
    public static void main(String[] args) {
        SecurityAuditService auditService = new DefaultSecurityAuditService();
        
        SecurityAuditLog log = auditService.logAccess(
            "user123",
            "UPDATE",
            "user-profile",
            "SUCCESS",
            "192.168.1.100",
            "Mozilla/5.0",
            null,
            true
        );
        
        List<SecurityAuditLog> sensitiveLogs = auditService.getSensitiveAuditLogs();
    }
}
```

### 4. 合规性检查

```java
import ltd.idcu.est.compliance.api.cybersecurity.ComplianceCheck;
import ltd.idcu.est.compliance.api.cybersecurity.ComplianceService;
import ltd.idcu.est.compliance.cybersecurity.DefaultComplianceService;

public class ComplianceCheckExample {
    public static void main(String[] args) {
        ComplianceService complianceService = new DefaultComplianceService();
        
        ComplianceCheck check = complianceService.runCheck("GDPR Basic Compliance");
        
        List<ComplianceCheck> allChecks = complianceService.runAllChecks();
    }
}
```

---

## 进阶篇

### 自定义合规检查

```java
// 可扩展的合规检查实现
```

### 集成现有的审计系统

```java
// 与 est-audit 模块集成
```

---

## 最佳实践

### 推荐做法

| 场景 | 推荐做法 | 说明 |
|------|---------|------|
| 数据脱敏 | 敏感数据必须脱敏 | 邮箱、手机号、身份证号等 |
| 同意管理 | 明确记录用户同意 | 保留同意历史记录 |
| 审计日志 | 关键操作必须审计 | 登录、数据修改、权限变更等 |
| 合规检查 | 定期运行合规检查 | 及时发现合规问题 |

---

## 模块结构

```
est-compliance/
├── est-compliance-api/      # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/compliance/api/
│   │       ├── gdpr/        # GDPR 相关接口
│   │       └── cybersecurity/ # 等保相关接口
│   └── pom.xml
├── est-compliance-impl/     # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/compliance/
│   │       ├── gdpr/        # GDPR 实现
│   │       └── cybersecurity/ # 等保实现
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [est-security 文档](../est-security/README.md)
- [est-audit 文档](../est-audit/README.md)
- [EST 安全指南](../../docs/security/README.md)

---

**文档版本**: 1.0  
**最后更新**: 2026-03-10
