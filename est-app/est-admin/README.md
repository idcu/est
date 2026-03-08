# est-admin - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-admin](#浠€涔堟槸-est-admin)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-admin

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-admin 灏卞儚"鍚庡彴绠＄悊绯荤粺"鐨勯鏋讹紝鎻愪緵鐢ㄦ埛绠＄悊銆佽鑹茬鐞嗐€佹潈闄愮鐞嗐€佽彍鍗曠鐞嗙瓑甯哥敤鍔熻兘锛屽紑绠卞嵆鐢ㄣ€?
### 鏍稿績鐗圭偣
- **鐢ㄦ埛绠＄悊**锛氱敤鎴稢RUD銆佺姸鎬佺鐞?- **瑙掕壊绠＄悊**锛氳鑹插垎閰嶃€佹潈闄愰厤缃?- **鏉冮檺绠＄悊**锛氳彍鍗曟潈闄愩€佹暟鎹潈闄?- **瀹¤鏃ュ織**锛氭搷浣滆褰曘€佺櫥褰曟棩蹇?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-admin</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆绠＄悊鍚庡彴
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

## 鏍稿績鍔熻兘

### 鐢ㄦ埛绠＄悊
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

### 瑙掕壊绠＄悊
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

## 妯″潡缁撴瀯

```
est-admin/
鈹溾攢鈹€ est-admin-api/          # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/app/admin/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-admin-impl/         # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/app/admin/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [RBAC 妯″潡](../../est-modules/est-security-group/est-rbac/README.md)
- [瀹¤鏃ュ織](../../est-modules/est-security-group/est-audit/README.md)
