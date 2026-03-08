# est-codegen - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-codegen](#浠€涔堟槸-est-codegen)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-codegen

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-codegen 灏卞儚"浠ｇ爜鐢熸垚鍣?锛屾牴鎹暟鎹簱琛ㄦ垨閰嶇疆鑷姩鐢熸垚 Entity銆丆ontroller銆丼ervice銆丮apper 绛変唬鐮併€?
### 鏍稿績鐗圭偣
- **鏁版嵁搴撹〃鐢熸垚**锛氫粠鏁版嵁搴撹〃鐢熸垚浠ｇ爜
- **妯℃澘鑷畾涔?*锛氭敮鎸佽嚜瀹氫箟浠ｇ爜妯℃澘
- **澶氭ā鍧楃敓鎴?*锛氭敮鎸佸绉嶄唬鐮侀鏍?- **澧為噺鏇存柊**锛氭敮鎸佸閲忎唬鐮佹洿鏂?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-codegen</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆鐢熸垚
```yaml
est:
  codegen:
    database:
      url: jdbc:mysql://localhost:3306/est
      username: root
      password: root
    output:
      dir: ./src/main/java
    template:
      prefix: ltd.idcu.est.demo
```

### 3. 鐢熸垚浠ｇ爜
```bash
# 鐢熸垚鍗曡〃浠ｇ爜
est codegen User

# 鐢熸垚鎵€鏈夎〃浠ｇ爜
est codegen --all
```

---

## 鏍稿績鍔熻兘

### 浠ｇ爜鐢熸垚鍣?```java
@Service
public class CodeGenerator {
    
    public void generate(String tableName) {
        TableInfo table = databaseService.getTableInfo(tableName);
        
        EntityCode entityCode = templateEngine.render("entity", table);
        MapperCode mapperCode = templateEngine.render("mapper", table);
        ServiceCode serviceCode = templateEngine.render("service", table);
        ControllerCode controllerCode = templateEngine.render("controller", table);
        
        fileService.save(entityCode);
        fileService.save(mapperCode);
        fileService.save(serviceCode);
        fileService.save(controllerCode);
    }
}
```

### 鑷畾涔夋ā鏉?```java
@Template(name = "my-entity", path = "templates/entity.java.template")
public class MyEntityTemplate {
    
    @Render
    public String render(TableInfo table) {
        return templateEngine.render(table);
    }
}
```

---

## 妯″潡缁撴瀯

```
est-codegen/
鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ ltd/idcu/est/tools/codegen/
鈹?      鈹溾攢鈹€ generator/
鈹?      鈹溾攢鈹€ template/
鈹?      鈹斺攢鈹€ database/
鈹溾攢鈹€ src/main/resources/
鈹?  鈹斺攢鈹€ templates/
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [CLI 宸ュ叿](../est-cli/README.md)
