# est-integration - 小白从入门到精通

## 目录
- [什么是 est-integration](#什么是-est-integration)
- [快速入门：5分钟上手](#快速入门5分钟上手)
- [基础篇：核心功能](#基础篇核心功能)
- [最佳实践](#最佳实践)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-integration

### 用大白话理解
est-integration 就像"万能转接器"。它帮你连接各种第三方服务：发邮件、发短信、存文件到 OSS，一套 API 全搞定。

### 核心特点
- **多集成支持**：邮件、短信、OSS、微信、支付宝
- **统一接口**：一套 API，切换实现无需改代码
- **可扩展**：轻松添加新的集成

---

## 快速入门：5分钟上手

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-integration</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置集成
```yaml
est:
  integration:
    email:
      enabled: true
      host: smtp.example.com
      port: 587
    sms:
      enabled: true
      provider: aliyun
    oss:
      enabled: true
      provider: aliyun-oss
```

### 3. 使用邮件服务
```java
@Service
public class NotificationService {
    
    @Inject
    private EmailService emailService;
    
    public void sendWelcomeEmail(String to, String username) {
        Email email = Email.builder()
            .to(to)
            .subject("欢迎加入")
            .content("你好, " + username + "！")
            .build();
        
        emailService.send(email);
    }
}
```

---

## 基础篇：核心功能

### 1. 邮件集成

#### 发送简单邮件
```java
@Service
public class MyEmailService {
    
    @Inject
    private EmailService emailService;
    
    public void sendSimpleEmail() {
        Email email = Email.builder()
            .from("noreply@example.com")
            .to("user@example.com")
            .subject("测试邮件")
            .content("这是一封测试邮件")
            .build();
        
        emailService.send(email);
    }
}
```

#### 发送带附件的邮件
```java
public void sendEmailWithAttachment() {
    Email email = Email.builder()
        .to("user@example.com")
        .subject("带附件的邮件")
        .content("请看附件")
        .attachment(new File("/path/to/file.pdf"))
        .build();
    
    emailService.send(email);
}
```

### 2. 短信集成

#### 发送短信
```java
@Service
public class MySmsService {
    
    @Inject
    private SmsService smsService;
    
    public void sendVerificationCode(String phone, String code) {
        Sms sms = Sms.builder()
            .phone(phone)
            .templateCode("VERIFICATION_CODE")
            .param("code", code)
            .build();
        
        smsService.send(sms);
    }
}
```

### 3. OSS 对象存储

#### 上传文件
```java
@Service
public class FileService {
    
    @Inject
    private OssService ossService;
    
    public String uploadFile(MultipartFile file) {
        String url = ossService.upload(file.getInputStream(), file.getOriginalFilename());
        return url;
    }
    
    public void deleteFile(String url) {
        ossService.delete(url);
    }
}
```

---

## 模块结构

```
est-integration/
├── est-integration-api/       # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/integration/
│   │       ├── email/
│   │       ├── sms/
│   │       └── oss/
│   └── pom.xml
├── est-integration-impl/      # 实现模块
│   ├── src/main/java/
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [消息队列](../est-messaging/README.md)
- [EST 集成指南](../../docs/integration/README.md)
- [示例代码](../../est-examples/est-examples-basic/)
