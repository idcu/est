# est-integration - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-integration](#浠€涔堟槸-est-integration)
- [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
- [鍩虹绡囷細鏍稿績鍔熻兘](#鍩虹绡囨牳蹇冨姛鑳?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-integration

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-integration 灏卞儚"涓囪兘杞帴鍣?銆傚畠甯綘杩炴帴鍚勭绗笁鏂规湇鍔★細鍙戦偖浠躲€佸彂鐭俊銆佸瓨鏂囦欢鍒?OSS锛屼竴濂?API 鍏ㄦ悶瀹氥€?
### 鏍稿績鐗圭偣
- **澶氶泦鎴愭敮鎸?*锛氶偖浠躲€佺煭淇°€丱SS銆佸井淇°€佹敮浠樺疂
- **缁熶竴鎺ュ彛**锛氫竴濂?API锛屽垏鎹㈠疄鐜版棤闇€鏀逛唬鐮?- **鍙墿灞?*锛氳交鏉炬坊鍔犳柊鐨勯泦鎴?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-integration</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆闆嗘垚
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

### 3. 浣跨敤閭欢鏈嶅姟
```java
@Service
public class NotificationService {
    
    @Inject
    private EmailService emailService;
    
    public void sendWelcomeEmail(String to, String username) {
        Email email = Email.builder()
            .to(to)
            .subject("娆㈣繋鍔犲叆")
            .content("浣犲ソ, " + username + "锛?)
            .build();
        
        emailService.send(email);
    }
}
```

---

## 鍩虹绡囷細鏍稿績鍔熻兘

### 1. 閭欢闆嗘垚

#### 鍙戦€佺畝鍗曢偖浠?```java
@Service
public class MyEmailService {
    
    @Inject
    private EmailService emailService;
    
    public void sendSimpleEmail() {
        Email email = Email.builder()
            .from("noreply@example.com")
            .to("user@example.com")
            .subject("娴嬭瘯閭欢")
            .content("杩欐槸涓€灏佹祴璇曢偖浠?)
            .build();
        
        emailService.send(email);
    }
}
```

#### 鍙戦€佸甫闄勪欢鐨勯偖浠?```java
public void sendEmailWithAttachment() {
    Email email = Email.builder()
        .to("user@example.com")
        .subject("甯﹂檮浠剁殑閭欢")
        .content("璇风湅闄勪欢")
        .attachment(new File("/path/to/file.pdf"))
        .build();
    
    emailService.send(email);
}
```

### 2. 鐭俊闆嗘垚

#### 鍙戦€佺煭淇?```java
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

### 3. OSS 瀵硅薄瀛樺偍

#### 涓婁紶鏂囦欢
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

## 妯″潡缁撴瀯

```
est-integration/
鈹溾攢鈹€ est-integration-api/       # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/integration/
鈹?  鈹?      鈹溾攢鈹€ email/
鈹?  鈹?      鈹溾攢鈹€ sms/
鈹?  鈹?      鈹斺攢鈹€ oss/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-integration-impl/      # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [娑堟伅闃熷垪](../est-messaging/README.md)
- [EST 闆嗘垚鎸囧崡](../../docs/integration/README.md)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-basic/)
