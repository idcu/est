# est-util-format-json - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-util-format-json](#浠€涔堟槸-est-util-format-json)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-util-format-json

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-util-format-json 灏卞儚"JSON 缈昏瘧瀹?锛屽府浣犳妸 Java 瀵硅薄杞崲鎴?JSON 瀛楃涓诧紝鎴栬€呮妸 JSON 瀛楃涓茶浆鍥?Java 瀵硅薄銆?
### 鏍稿績鐗圭偣
- **绠€鍗曟槗鐢?*锛氫竴琛屼唬鐮佹悶瀹氬簭鍒楀寲/鍙嶅簭鍒楀寲
- **澶氬紩鎿庢敮鎸?*锛欽ackson銆丟son銆丗astjson
- **楂樻€ц兘**锛氫紭鍖栫殑搴忓垪鍖栨€ц兘
- **鐏垫椿閰嶇疆**锛氭敮鎸佽嚜瀹氫箟搴忓垪鍖栬鍒?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-format-json</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍩烘湰浣跨敤
```java
@Service
public class UserService {
    
    @Inject
    private JsonService jsonService;
    
    public String toJson(User user) {
        return jsonService.toJson(user);
    }
    
    public User fromJson(String json) {
        return jsonService.fromJson(json, User.class);
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [EST 宸ュ叿鎸囧崡](../../docs/utils/README.md)
