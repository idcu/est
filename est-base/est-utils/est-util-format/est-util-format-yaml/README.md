# est-util-format-yaml - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-util-format-yaml](#浠€涔堟槸-est-util-format-yaml)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-util-format-yaml

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-util-format-yaml 灏卞儚"YAML 缈昏瘧瀹?锛屽府浣犲湪 Java 瀵硅薄鍜?YAML 涔嬮棿浜掔浉杞崲銆?
### 鏍稿績鐗圭偣
- **SnakeYAML 鏀寔**锛氶珮鎬ц兘 YAML 澶勭悊
- **閰嶇疆鍙嬪ソ**锛氶€傚悎閰嶇疆鏂囦欢璇诲啓
- **绫诲瀷瀹夊叏**锛氬畬鏁寸殑绫诲瀷鏀寔

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-format-yaml</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍩烘湰浣跨敤
```java
@Service
public class YamlService {
    
    @Inject
    private YamlService yamlService;
    
    public String toYaml(Config config) {
        return yamlService.toYaml(config);
    }
    
    public Config fromYaml(String yaml) {
        return yamlService.fromYaml(yaml, Config.class);
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [EST 宸ュ叿鎸囧崡](../../docs/utils/README.md)
