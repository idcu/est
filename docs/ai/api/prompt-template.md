# 鎻愮ず璇嶆ā鏉?API 鍙傝€?
## PromptTemplateEngine 鎺ュ彛

```java
public interface PromptTemplateEngine {
    String render(String templateName, Map<String, Object> variables);
    String render(PromptTemplate template, Map<String, Object> variables);
    void registerTemplate(PromptTemplate template);
    PromptTemplate getTemplate(String templateName);
    List<String> listTemplates();
}
```

### 鏂规硶璇存槑

#### render(String templateName, Map&lt;String, Object&gt; variables)
娓叉煋鎸囧畾鍚嶇О鐨勬ā鏉裤€?
**鍙傛暟锛?*
- `templateName` - 妯℃澘鍚嶇О
- `variables` - 鍙橀噺鏄犲皠

**杩斿洖鍊硷細** 娓叉煋鍚庣殑鎻愮ず璇?
**绀轰緥锛?*
```java
String prompt = engine.render("code-review", Map.of(
    "code", codeToReview,
    "language", "Java"
));
```

---

#### render(PromptTemplate template, Map&lt;String, Object&gt; variables)
娓叉煋缁欏畾鐨勬ā鏉垮璞°€?
**鍙傛暟锛?*
- `template` - 妯℃澘瀵硅薄
- `variables` - 鍙橀噺鏄犲皠

**杩斿洖鍊硷細** 娓叉煋鍚庣殑鎻愮ず璇?
**绀轰緥锛?*
```java
PromptTemplate template = new PromptTemplate();
template.setName("custom");
template.setContent("浣犲ソ锛?{name}锛?);

String prompt = engine.render(template, Map.of("name", "寮犱笁"));
```

---

#### registerTemplate(PromptTemplate template)
娉ㄥ唽涓€涓嚜瀹氫箟妯℃澘銆?
**鍙傛暟锛?*
- `template` - 妯℃澘瀵硅薄

**绀轰緥锛?*
```java
PromptTemplate template = new PromptTemplate();
template.setName("my-template");
template.setContent("浣犳槸涓€涓?${role}锛岃澶勭悊浠ヤ笅鍐呭锛?{content}");
template.setDescription("鎴戠殑鑷畾涔夋ā鏉?);

engine.registerTemplate(template);
```

---

#### getTemplate(String templateName)
鑾峰彇鎸囧畾鍚嶇О鐨勬ā鏉裤€?
**鍙傛暟锛?*
- `templateName` - 妯℃澘鍚嶇О

**杩斿洖鍊硷細** 妯℃澘瀵硅薄锛屽鏋滀笉瀛樺湪杩斿洖 null

**绀轰緥锛?*
```java
PromptTemplate template = engine.getTemplate("code-review");
```

---

#### listTemplates()
鍒楀嚭鎵€鏈夊彲鐢ㄧ殑妯℃澘鍚嶇О銆?
**杩斿洖鍊硷細** 妯℃澘鍚嶇О鍒楄〃

**绀轰緥锛?*
```java
List<String> templates = engine.listTemplates();
```

---

## PromptTemplate 绫?
鎻愮ず璇嶆ā鏉跨被銆?
### 鏋勯€犲嚱鏁?
```java
public PromptTemplate()
public PromptTemplate(String name, String content)
```

### 灞炴€?
| 灞炴€?| 绫诲瀷 | 璇存槑 |
|------|------|------|
| name | String | 妯℃澘鍚嶇О |
| content | String | 妯℃澘鍐呭 |
| description | String | 妯℃澘鎻忚堪 |
| category | String | 妯℃澘鍒嗙被 |

### 绀轰緥

```java
PromptTemplate template = new PromptTemplate();
template.setName("code-review");
template.setContent("""
    浣犳槸涓€涓唬鐮佸鏌ヤ笓瀹躲€?    璇峰鏌ヤ互涓嬩唬鐮侊紝鎸囧嚭闂骞舵彁渚涙敼杩涘缓璁€?    
    浠ｇ爜锛?    ${code}
    
    璇█锛?{language}
    """);
template.setDescription("浠ｇ爜瀹℃煡妯℃澘");
template.setCategory("code");
```

---

## DefaultPromptTemplateEngine 瀹炵幇

榛樿鐨勬彁绀鸿瘝妯℃澘寮曟搸瀹炵幇绫汇€?
### 鏋勯€犲嚱鏁?
```java
public DefaultPromptTemplateEngine()
```

### 鍐呯疆妯℃澘

| 妯℃澘鍚嶇О | 鍒嗙被 | 璇存槑 |
|----------|------|------|
| code-review | code | 浠ｇ爜瀹℃煡妯℃澘 |
| code-explain | code | 浠ｇ爜瑙ｉ噴妯℃澘 |
| code-optimize | code | 浠ｇ爜浼樺寲妯℃澘 |
| code-generate | code | 浠ｇ爜鐢熸垚妯℃澘 |
| test-generate | test | 娴嬭瘯浠ｇ爜鐢熸垚妯℃澘 |
| doc-generate | doc | 鏂囨。鐢熸垚妯℃澘 |
| bug-fix | debug | Bug 淇妯℃澘 |

### 绀轰緥

```java
PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 浣跨敤鍐呯疆妯℃澘
String prompt = engine.render("code-review", Map.of(
    "code", code,
    "language", "Java"
));

// 娉ㄥ唽鑷畾涔夋ā鏉?PromptTemplate custom = new PromptTemplate("my-template", "Hello, ${name}!");
engine.registerTemplate(custom);
```

---

## 妯℃澘璇硶

妯℃澘浣跨敤 `${variableName}` 璇硶鏉ュ紩鐢ㄥ彉閲忋€?
### 绀轰緥

```
浣犲ソ锛?{userName}锛?
璇峰鐞嗕互涓嬪唴瀹癸細
${content}

鏃堕棿锛?{timestamp}
```

娓叉煋锛?```java
Map<String, Object> variables = Map.of(
    "userName", "寮犱笁",
    "content", "杩欐槸瑕佸鐞嗙殑鍐呭",
    "timestamp", LocalDateTime.now().toString()
);
String result = engine.render(template, variables);
```

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
