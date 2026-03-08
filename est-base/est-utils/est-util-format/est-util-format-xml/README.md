# est-util-format-xml - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-util-format-xml](#浠€涔堟槸-est-util-format-xml)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-util-format-xml

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-util-format-xml 灏卞儚"XML 缈昏瘧瀹?锛屽府浣犲湪 Java 瀵硅薄鍜?XML 涔嬮棿浜掔浉杞崲銆?
### 鏍稿績鐗圭偣
- **JAXB 鏀寔**锛氭爣鍑?JAXB 娉ㄨВ
- **澶氬紩鎿?*锛欽AXB銆丣ackson XML
- **绠€鍗曟槗鐢?*锛氱粺涓€鐨?API

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-format-xml</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍩烘湰浣跨敤
```java
@Service
public class XmlService {
    
    @Inject
    private XmlService xmlService;
    
    public String toXml(User user) {
        return xmlService.toXml(user);
    }
    
    public User fromXml(String xml) {
        return xmlService.fromXml(xml, User.class);
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [EST 宸ュ叿鎸囧崡](../../docs/utils/README.md)
