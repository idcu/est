# EST 鎻愮ず璇嶅伐绋嬫寚鍗?
## 浠€涔堟槸鎻愮ず璇嶅伐绋嬶紵

鎻愮ず璇嶅伐绋嬫槸璁捐鍜屼紭鍖栨彁绀鸿瘝锛屼互浠?AI 妯″瀷鑾峰緱鏈€浣宠緭鍑虹殑杩囩▼銆傝壇濂界殑鎻愮ず璇嶅彲浠ユ樉钁楁彁楂?AI 鐨勮緭鍑鸿川閲忋€佷竴鑷存€у拰鍙潬鎬с€?
EST 妗嗘灦鍐呯疆浜嗗己澶х殑鎻愮ず璇嶆ā鏉跨郴缁燂紝甯姪浣犺交鏉惧簲鐢ㄦ彁绀鸿瘝宸ョ▼鐨勬渶浣冲疄璺点€?
---

## 鎻愮ず璇嶈璁″師鍒?
### 1. 娓呮櫚鏄庣‘

**涓嶅ソ鐨勭ず渚?*锛?```
鍐欑偣浠ｇ爜
```

**濂界殑绀轰緥**锛?```
璇峰啓涓€涓?Java 鏂规硶锛屾帴鍙椾竴涓瓧绗︿覆鍒楄〃锛岃繑鍥為暱搴︽渶闀跨殑瀛楃涓层€?瑕佹眰锛?- 浣跨敤 Java 8 Stream API
- 澶勭悊绌哄垪琛ㄧ殑鎯呭喌
- 娣诲姞 Javadoc 娉ㄩ噴
```

### 2. 鎻愪緵涓婁笅鏂?
**涓嶅ソ鐨勭ず渚?*锛?```
浼樺寲杩欐浠ｇ爜
```

**濂界殑绀轰緥**锛?```
杩欐槸涓€涓數鍟嗙郴缁熺殑璁㈠崟鏌ヨ鏂规硶锛岃浼樺寲瀹冪殑鎬ц兘锛?
浠ｇ爜锛?${code}

涓婁笅鏂囦俊鎭細
- 鏁版嵁搴撴槸 MySQL 8.0
- 璁㈠崟琛ㄦ湁 100 涓? 鏁版嵁
- 闇€瑕佹敮鎸佹寜鏃堕棿鑼冨洿鏌ヨ
```

### 3. 鎸囧畾杈撳嚭鏍煎紡

**涓嶅ソ鐨勭ず渚?*锛?```
鍒嗘瀽杩欎釜闂
```

**濂界殑绀轰緥**锛?```
璇峰垎鏋愪互涓嬮棶棰橈紝骞舵寜浠ヤ笅鏍煎紡杩斿洖锛?
銆愰棶棰樺垎鏋愩€?...

銆愯В鍐虫柟妗堛€?...

銆愪唬鐮佺ず渚嬨€?...

銆愭敞鎰忎簨椤广€?...
```

### 4. 璁惧畾瑙掕壊

**涓嶅ソ鐨勭ず渚?*锛?```
甯垜鍐欎唬鐮?```

**濂界殑绀轰緥**锛?```
浣犳槸涓€浣嶆湁 10 骞寸粡楠岀殑 Java 鍚庣鏋舵瀯甯堬紝绮鹃€?Spring Boot銆丒ST 妗嗘灦鍜屽井鏈嶅姟鏋舵瀯銆?璇蜂互涓撳鐨勮韩浠藉府鎴戣璁″苟瀹炵幇浠ヤ笅鍔熻兘...
```

---

## EST 鎻愮ず璇嶆ā鏉跨郴缁?
### 浣跨敤鍐呯疆妯℃澘

EST 鎻愪緵浜嗗绉嶄笓涓氱骇鐨勬彁绀鸿瘝妯℃澘锛?
```java
PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 浠ｇ爜瀹℃煡
String reviewPrompt = engine.render("code-review", Map.of(
    "code", yourCode,
    "language", "Java",
    "focus", "performance"
));

// 浠ｇ爜鐢熸垚
String generatePrompt = engine.render("code-generate", Map.of(
    "requirement", "鐢ㄦ埛绠＄悊妯″潡",
    "framework", "EST 2.0",
    "database", "MySQL"
));

// 娴嬭瘯鐢熸垚
String testPrompt = engine.render("test-generate", Map.of(
    "code", yourCode,
    "framework", "JUnit 5",
    "mock": "Mockito"
));
```

### 鍐呯疆妯℃澘鍒楄〃

| 妯℃澘鍚嶇О | 鍒嗙被 | 璇存槑 |
|---------|------|------|
| code-review | code | 浠ｇ爜瀹℃煡妯℃澘 |
| code-explain | code | 浠ｇ爜瑙ｉ噴妯℃澘 |
| code-optimize | code | 浠ｇ爜浼樺寲妯℃澘 |
| code-generate | code | 浠ｇ爜鐢熸垚妯℃澘 |
| test-generate | test | 娴嬭瘯浠ｇ爜鐢熸垚妯℃澘 |
| doc-generate | doc | 鏂囨。鐢熸垚妯℃澘 |
| bug-fix | debug | Bug 淇妯℃澘 |
| refactor | refactor | 閲嶆瀯妯℃澘 |
| architecture | design | 鏋舵瀯璁捐妯℃澘 |

### 鍒涘缓鑷畾涔夋ā鏉?
```java
PromptTemplate template = new PromptTemplate();
template.setName("rest-api-design");
template.setCategory("design");
template.setDescription("REST API 璁捐妯℃澘");
template.setContent("""
    浣犳槸涓€浣?REST API 璁捐涓撳銆?    
    璇蜂负浠ヤ笅闇€姹傝璁?REST API锛?    
    闇€姹傛弿杩帮細${requirement}
    
    璇锋彁渚涳細
    1. API 绔偣鍒楄〃锛圚TTP 鏂规硶 + URL锛?    2. 璇锋眰鍜屽搷搴旀牸寮?    3. 閿欒澶勭悊璁捐
    4. 瀹夊叏鎬ц€冭檻
    
    鎶€鏈害鏉燂細
    - 妗嗘灦锛欵ST Framework 2.0
    - 璁よ瘉锛欽WT
    - 鏂囨。锛歋wagger/OpenAPI 3.0
    """);

engine.registerTemplate(template);

// 浣跨敤鑷畾涔夋ā鏉?String prompt = engine.render("rest-api-design", Map.of(
    "requirement", "璁㈠崟绠＄悊 API"
));
```

---

## 楂樼骇鎻愮ず璇嶆妧宸?
### 1. 鎬濈淮閾?(Chain of Thought)

寮曞 AI 閫愭鎬濊€冿細

```java
String prompt = """
    璇疯В鍐充互涓嬮棶棰橈紝浣嗗湪缁欏嚭鏈€缁堢瓟妗堜箣鍓嶏紝璇峰厛瑙ｉ噴浣犵殑鎬濊€冭繃绋嬨€?    
    闂锛?{problem}
    
    璇锋寜浠ヤ笅鏍煎紡鍥炵瓟锛?    銆愭€濊€冭繃绋嬨€?    1. 棣栧厛锛屾垜闇€瑕佺悊瑙?..
    2. 鐒跺悗锛屾垜搴旇鑰冭檻...
    3. 鏈€鍚庯紝鎴戝彲浠ュ緱鍑?..
    
    銆愭渶缁堢瓟妗堛€?    ...
    """;
```

### 2. 灏戞牱鏈涔?(Few-Shot Learning)

鎻愪緵鍑犱釜绀轰緥甯姪 AI 鐞嗚В鏈熸湜锛?
```java
String prompt = """
    璇峰皢浠ヤ笅鑷劧璇█杞崲涓?SQL 鏌ヨ銆?    
    绀轰緥 1锛?    杈撳叆锛氭煡鎵炬墍鏈変环鏍煎ぇ浜?100 鐨勪骇鍝?    杈撳嚭锛歋ELECT * FROM product WHERE price > 100
    
    绀轰緥 2锛?    杈撳叆锛氱粺璁℃瘡涓被鍒殑浜у搧鏁伴噺
    杈撳嚭锛歋ELECT category, COUNT(*) FROM product GROUP BY category
    
    鐜板湪璇峰鐞嗭細
    杈撳叆锛?{input}
    杈撳嚭锛?    """;
```

### 3. 鑷垜涓€鑷存€?(Self-Consistency)

璁?AI 鐢熸垚澶氫釜绛旀锛岀劧鍚庨€夋嫨鏈€浣崇殑锛?
```java
String prompt = """
    璇蜂负浠ヤ笅闂鎻愪緵 3 涓笉鍚岀殑瑙ｅ喅鏂规锛岀劧鍚庨€夋嫨鏈€浣崇殑涓€涓苟璇存槑鐞嗙敱銆?    
    闂锛?{problem}
    
    鏂规 1锛?    ...
    
    鏂规 2锛?    ...
    
    鏂规 3锛?    ...
    
    鏈€浣虫柟妗堬細鏂规 X
    鐞嗙敱锛?..
    """;
```

### 4. 鎻愮ず璇嶅垎瑙?
灏嗗鏉備换鍔″垎瑙ｄ负澶氫釜姝ラ锛?
```java
// 绗竴姝ワ細鐞嗚В闇€姹?String step1 = aiAssistant.chat("璇峰垎鏋愪互涓嬮渶姹傦紝鍒楀嚭鏍稿績鍔熻兘鐐癸細" + requirement);

// 绗簩姝ワ細璁捐鏋舵瀯
String step2 = aiAssistant.chat("鍩轰簬浠ヤ笅鍔熻兘鐐癸紝璁捐绯荤粺鏋舵瀯锛? + step1);

// 绗笁姝ワ細鐢熸垚浠ｇ爜
String step3 = aiAssistant.chat("鍩轰簬浠ヤ笅鏋舵瀯璁捐锛岀敓鎴愪唬鐮侊細" + step2);
```

---

## 浠ｇ爜鐢熸垚鎻愮ず璇嶆渶浣冲疄璺?
### 1. 璇︾粏鐨勯渶姹傛弿杩?
```java
String prompt = """
    璇风敤 EST 妗嗘灦瀹炵幇涓€涓敤鎴疯璇佹ā鍧椼€?    
    璇︾粏闇€姹傦細
    1. 鐢ㄦ埛娉ㄥ唽锛堢敤鎴峰悕銆侀偖绠便€佸瘑鐮侊級
    2. 鐢ㄦ埛鐧诲綍锛堥偖绠?瀵嗙爜 + JWT Token锛?    3. 瀵嗙爜閲嶇疆
    4. 閭楠岃瘉
    
    鎶€鏈姹傦細
    - 浣跨敤 Spring Security
    - 瀵嗙爜浣跨敤 BCrypt 鍔犲瘑
    - JWT Token 鏈夋晥鏈?2 灏忔椂
    - 浣跨敤 EST 鐨勬暟鎹闂ā鍧?    
    璇风敓鎴愶細
    - Entity 绫?    - Repository 鎺ュ彛
    - Service 绫?    - Controller 绫?    - DTO 绫?    - 閰嶇疆绫?    """;
```

### 2. 鎸囧畾浠ｇ爜椋庢牸

```java
String prompt = """
    璇风敓鎴愮鍚堜互涓嬩唬鐮侀鏍肩殑 Java 浠ｇ爜锛?    
    浠ｇ爜椋庢牸瑙勮寖锛?    - 浣跨敤 Lombok 娉ㄨВ
    - 閬靛惊 Google Java Style
    - 绫诲悕浣跨敤澶ч┘宄?    - 鏂规硶鍚嶄娇鐢ㄥ皬椹煎嘲
    - 甯搁噺浣跨敤鍏ㄥぇ鍐欎笅鍒掔嚎鍒嗛殧
    - 娣诲姞 Javadoc 娉ㄩ噴
    - 浣跨敤 @Override 娉ㄨВ
    - 寮傚父澶勭悊瑕佸畬鍠?    
    闇€姹傦細${requirement}
    """;
```

### 3. 鍖呭惈娴嬭瘯瑕佹眰

```java
String prompt = """
    璇峰疄鐜颁互涓嬪姛鑳斤紝骞跺寘鍚畬鏁寸殑鍗曞厓娴嬭瘯锛?    
    鍔熻兘闇€姹傦細${requirement}
    
    娴嬭瘯瑕佹眰锛?    - 浣跨敤 JUnit 5
    - 浣跨敤 Mockito 杩涜 Mock
    - 娴嬭瘯瑕嗙洊鐜?> 80%
    - 鍖呭惈姝ｅ父娴佺▼娴嬭瘯
    - 鍖呭惈杈圭晫鏉′欢娴嬭瘯
    - 鍖呭惈寮傚父鎯呭喌娴嬭瘯
    """;
```

---

## 璋冭瘯鍜屾敼杩涙彁绀鸿瘝

### 1. 杩唬寮忔敼杩?
```java
// 绗竴杞?String v1 = aiAssistant.chat(initialPrompt);

// 鑾峰緱鍙嶉骞舵敼杩?String feedback = """
    杩欐槸绗竴娆＄殑缁撴灉锛?    ${v1}
    
    璇锋牴鎹互涓嬪弽棣堟敼杩涳細
    1. 缂哄皯寮傚父澶勭悊
    2. 闇€瑕佹坊鍔犳棩蹇?    3. 鎬ц兘鍙互杩涗竴姝ヤ紭鍖?    """;

String v2 = aiAssistant.chat(feedback);
```

### 2. A/B 娴嬭瘯

鍚屾椂娴嬭瘯澶氫釜鎻愮ず璇嶇増鏈紝閫夋嫨鏁堟灉鏈€濂界殑锛?
```java
String promptA = "鐗堟湰 A 鐨勬彁绀鸿瘝...";
String promptB = "鐗堟湰 B 鐨勬彁绀鸿瘝...";

String resultA = aiAssistant.chat(promptA);
String resultB = aiAssistant.chat(promptB);

// 瀵规瘮骞堕€夋嫨鏈€浣崇粨鏋?```

### 3. 娓╁害鍙傛暟璋冩暣

鏍规嵁浠诲姟绫诲瀷璋冩暣娓╁害鍙傛暟锛?
```java
// 鍒涢€犳€т换鍔★紙杈冮珮娓╁害锛?LlmOptions creativeOptions = new LlmOptions();
creativeOptions.setTemperature(0.8);
String creativeResult = client.complete(creativePrompt, creativeOptions);

// 绮剧‘鎬т换鍔★紙杈冧綆娓╁害锛?LlmOptions preciseOptions = new LlmOptions();
preciseOptions.setTemperature(0.2);
String preciseResult = client.complete(precisePrompt, preciseOptions);
```

---

## 鎬荤粨

鎻愮ず璇嶅伐绋嬫槸涓€闂ㄨ壓鏈篃鏄竴闂ㄧ瀛︺€傞€氳繃锛?1. 浣跨敤 EST 鐨勬彁绀鸿瘝妯℃澘绯荤粺
2. 閬靛惊鎻愮ず璇嶈璁″師鍒?3. 搴旂敤楂樼骇鎻愮ず璇嶆妧宸?4. 鎸佺画杩唬鍜屾敼杩?
浣犲彲浠ュ厖鍒嗗彂鎸?AI 鐨勬綔鍔涳紝鏄捐憲鎻愬崌寮€鍙戞晥鐜囧拰浠ｇ爜璐ㄩ噺銆?
璁颁綇锛氬ソ鐨勬彁绀鸿瘝 = 娓呮櫚鐨勬寚浠?+ 鍏呭垎鐨勪笂涓嬫枃 + 鏄庣‘鐨勬牸寮忥紒

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
