# 浠ｇ爜鐢熸垚鍣?API 鍙傝€?
## CodeGenerator 鎺ュ彛

```java
public interface CodeGenerator {
    String generateEntity(String className, String packageName, Map<String, Object> options);
    String generateRepository(String interfaceName, String packageName, Map<String, Object> options);
    String generateService(String className, String packageName, Map<String, Object> options);
    String generateController(String className, String packageName, Map<String, Object> options);
    String generatePomXml(String projectName, String groupId, String artifactId, String version);
}
```

### 鏂规硶璇存槑

#### generateEntity(String className, String packageName, Map&lt;String, Object&gt; options)
鐢熸垚瀹炰綋绫讳唬鐮併€?
**鍙傛暟锛?*
- `className` - 瀹炰綋绫诲悕
- `packageName` - 鍖呭悕
- `options` - 閫夐」锛屽寘鍚細
  - `fields` - 瀛楁鍒楄〃锛屾牸寮忎负 ["瀛楁鍚?绫诲瀷", ...]
  - `useLombok` - 鏄惁浣跨敤 Lombok锛堝彲閫夛紝榛樿 false锛?  - `tableName` - 鏁版嵁搴撹〃鍚嶏紙鍙€夛級

**杩斿洖鍊硷細** 鐢熸垚鐨勫疄浣撶被浠ｇ爜

**绀轰緥锛?*
```java
String entity = generator.generateEntity("Product", "com.example.entity",
    Map.of(
        "fields", List.of("id:Long", "name:String", "price:BigDecimal", "createTime:LocalDateTime"),
        "useLombok", true
    )
);
```

---

#### generateRepository(String interfaceName, String packageName, Map&lt;String, Object&gt; options)
鐢熸垚 Repository 鎺ュ彛浠ｇ爜銆?
**鍙傛暟锛?*
- `interfaceName` - 鎺ュ彛鍚?- `packageName` - 鍖呭悕
- `options` - 閫夐」

**杩斿洖鍊硷細** 鐢熸垚鐨?Repository 鎺ュ彛浠ｇ爜

**绀轰緥锛?*
```java
String repo = generator.generateRepository("ProductRepository", "com.example.repository", Map.of());
```

---

#### generateService(String className, String packageName, Map&lt;String, Object&gt; options)
鐢熸垚 Service 绫讳唬鐮併€?
**鍙傛暟锛?*
- `className` - Service 绫诲悕
- `packageName` - 鍖呭悕
- `options` - 閫夐」
  - `entityName` - 瀹炰綋绫诲悕锛堝彲閫夛級
  - `repositoryName` - Repository 鎺ュ彛鍚嶏紙鍙€夛級

**杩斿洖鍊硷細** 鐢熸垚鐨?Service 绫讳唬鐮?
**绀轰緥锛?*
```java
String service = generator.generateService("ProductService", "com.example.service",
    Map.of(
        "entityName", "Product",
        "repositoryName", "ProductRepository"
    )
);
```

---

#### generateController(String className, String packageName, Map&lt;String, Object&gt; options)
鐢熸垚 Controller 绫讳唬鐮併€?
**鍙傛暟锛?*
- `className` - Controller 绫诲悕
- `packageName` - 鍖呭悕
- `options` - 閫夐」
  - `serviceName` - Service 绫诲悕锛堝彲閫夛級
  - `basePath` - 鍩虹璺緞锛堝彲閫夛級

**杩斿洖鍊硷細** 鐢熸垚鐨?Controller 绫讳唬鐮?
**绀轰緥锛?*
```java
String controller = generator.generateController("ProductController", "com.example.controller",
    Map.of(
        "serviceName", "ProductService",
        "basePath", "/api/products"
    )
);
```

---

#### generatePomXml(String projectName, String groupId, String artifactId, String version)
鐢熸垚 pom.xml 鏂囦欢鍐呭銆?
**鍙傛暟锛?*
- `projectName` - 椤圭洰鍚嶇О
- `groupId` - Maven groupId
- `artifactId` - Maven artifactId
- `version` - 椤圭洰鐗堟湰

**杩斿洖鍊硷細** pom.xml 鍐呭

**绀轰緥锛?*
```java
String pom = generator.generatePomXml("MyProject", "com.example", "my-app", "1.0.0");
```

---

## DefaultCodeGenerator 瀹炵幇

榛樿鐨勪唬鐮佺敓鎴愬櫒瀹炵幇绫汇€?
### 鏋勯€犲嚱鏁?
```java
public DefaultCodeGenerator()
```

**绀轰緥锛?*
```java
CodeGenerator generator = new DefaultCodeGenerator();
```

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
