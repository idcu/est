# est-web-template - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-web-template](#浠€涔堟槸-est-web-template)
- [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
- [鍩虹绡囷細鏍稿績鍔熻兘](#鍩虹绡囨牳蹇冨姛鑳?
- [杩涢樁绡囷細楂樼骇鐢ㄦ硶](#杩涢樁绡囬珮绾х敤娉?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-web-template

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-web-template 灏卞儚鍋氱綉椤电殑"妯℃澘鎵撳嵃鏈?銆備綘鍏堝仛濂戒竴涓綉椤垫ā鏉匡紝閲岄潰鐣欏ソ绌轰綅锛堝彉閲忥級锛岀劧鍚庢妸鏁版嵁濉繘鍘伙紝灏辫兘鐢熸垚瀹屾暣鐨勭綉椤点€傛敮鎸佸绉嶆ā鏉垮紩鎿庯紝鎯崇敤鍝釜鐢ㄥ摢涓€?
### 鏍稿績鐗圭偣
- **澶氬紩鎿庢敮鎸?*锛欶reemarker銆乀hymeleaf銆丮ustache銆乂elocity
- **缁熶竴 API**锛氫竴濂楁帴鍙ｏ紝鍒囨崲寮曟搸鏃犻渶鏀逛唬鐮?- **鐑姞杞?*锛氬紑鍙戞椂淇敼妯℃澘鑷姩鍒锋柊
- **妯℃澘缁ф壙**锛氭敮鎸佸竷灞€缁ф壙鍜岀粍浠跺鐢?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-web-template</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- 閫夋嫨涓€涓ā鏉垮紩鎿?-->
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-web-template-freemarker</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆妯℃澘
```yaml
est:
  web:
    template:
      engine: freemarker  # 妯℃澘寮曟搸
      prefix: /templates/ # 妯℃澘璺緞鍓嶇紑
      suffix: .html       # 妯℃澘鏂囦欢鍚庣紑
      cache: false        # 寮€鍙戞椂鍏抽棴缂撳瓨
      encoding: UTF-8
```

### 3. 鍒涘缓妯℃澘
```html
<!-- /templates/index.html -->
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
    <h1>娆㈣繋, ${user.name}!</h1>
    <p>浠婂ぉ鏄?${date}</p>
    
    <ul>
        <#list items as item>
            <li>${item.name} - ${item.price}</li>
        </#list>
    </ul>
</body>
</html>
```

### 4. 娓叉煋椤甸潰
```java
@Controller
public class HomeController {
    
    @Get("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        
        mv.addObject("title", "棣栭〉");
        mv.addObject("user", currentUser());
        mv.addObject("date", LocalDate.now());
        mv.addObject("items", getProducts());
        
        return mv;
    }
}
```

---

## 鍩虹绡囷細鏍稿績鍔熻兘

### 1. 妯℃澘寮曟搸閫夋嫨

#### Freemarker锛堟帹鑽愶級
```yaml
est:
  web:
    template:
      engine: freemarker
```

```html
<!-- Freemarker 璇硶 -->
<h1>${title}</h1>
<#if user?exists>
    <p>娆㈣繋, ${user.name}</p>
</#if>
<#list items as item>
    <div>${item_index + 1}. ${item.name}</div>
</#list>
```

#### Thymeleaf
```yaml
est:
  web:
    template:
      engine: thymeleaf
```

```html
<!-- Thymeleaf 璇硶 -->
<h1 th:text="${title}">鏍囬</h1>
<p th:if="${user != null}" th:text="'娆㈣繋, ' + ${user.name}"></p>
<div th:each="item, iterStat : ${items}">
    <span th:text="${iterStat.index + 1}"></span>
    <span th:text="${item.name}"></span>
</div>
```

#### Mustache
```yaml
est:
  web:
    template:
      engine: mustache
```

```html
<!-- Mustache 璇硶 -->
<h1>{{title}}</h1>
{{#user}}
    <p>娆㈣繋, {{name}}</p>
{{/user}}
{{#items}}
    <div>{{name}}</div>
{{/items}}
```

### 2. ModelAndView 鐢ㄦ硶

#### 鍩烘湰鐢ㄦ硶
```java
@Controller
public class ProductController {
    
    @Get("/products/{id}")
    public ModelAndView productDetail(@PathParam Long id) {
        ModelAndView mv = new ModelAndView("product/detail");
        
        Product product = productService.getById(id);
        mv.addObject("product", product);
        mv.addObject("reviews", reviewService.findByProduct(id));
        
        return mv;
    }
}
```

#### 蹇嵎鏂规硶
```java
// 鐩存帴杩斿洖妯℃澘鍚嶇О
@Get("/about")
public String about() {
    return "about";
}

// 杩斿洖 Map 浣滀负妯″瀷
@Get("/contact")
public Map<String, Object> contact() {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "鑱旂郴鎴戜滑");
    model.put("form", new ContactForm());
    return model;
}
```

### 3. 妯℃澘鍙橀噺鍜岃〃杈惧紡

#### 鍙橀噺杈撳嚭
```java
// 鎺у埗鍣?mv.addObject("username", "寮犱笁");
mv.addObject("age", 25);
mv.addObject("user", user);
```

```html
<!-- 妯℃澘 -->
<p>鐢ㄦ埛鍚? ${username}</p>
<p>骞撮緞: ${age}</p>
<p>閭: ${user.email}</p>
```

#### 鏉′欢鍒ゆ柇
```html
<#if user.online>
    <span class="online">鍦ㄧ嚎</span>
<#elseif user.offline>
    <span class="offline">绂荤嚎</span>
<#else>
    <span class="away">绂诲紑</span>
</#if>
```

#### 寰幆閬嶅巻
```html
<table>
    <tr>
        <th>搴忓彿</th>
        <th>鍚嶇О</th>
        <th>浠锋牸</th>
    </tr>
    <#list products as product>
        <tr class="${product_index % 2 == 0 ? 'even' : 'odd'}">
            <td>${product_index + 1}</td>
            <td>${product.name}</td>
            <td>楼${product.price}</td>
        </tr>
    </#list>
</table>
```

### 4. 妯℃澘鍖呭惈鍜岀户鎵?
#### 鍖呭惈缁勪欢
```html
<!-- header.ftl -->
<header>
    <nav>
        <a href="/">棣栭〉</a>
        <a href="/products">浜у搧</a>
        <a href="/about">鍏充簬</a>
    </nav>
</header>
```

```html
<!-- 椤甸潰涓紩鐢?-->
<#include "header.ftl">

<main>
    <h1>椤甸潰鍐呭</h1>
</main>

<#include "footer.ftl">
```

#### 甯冨眬缁ф壙
```html
<!-- layout.ftl - 鍩虹甯冨眬 -->
<!DOCTYPE html>
<html>
<head>
    <title><#nested "title">榛樿鏍囬</#nested></title>
    <link rel="stylesheet" href="/css/style.css">
    <#nested "head">
</head>
<body>
    <#include "header.ftl">
    
    <main>
        <#nested "content">
    </main>
    
    <#include "footer.ftl">
    
    <script src="/js/main.js"></script>
    <#nested "scripts">
</body>
</html>
```

```html
<!-- 浣跨敤甯冨眬 -->
<#import "layout.ftl" as layout>
<@layout.main>
    <#nested "title">浜у搧鍒楄〃</#nested>
    
    <#nested "content">
        <h1>浜у搧鍒楄〃</h1>
        <#list products as product>
            <div class="product">${product.name}</div>
        </#list>
    </#nested>
</@layout.main>
```

---

## 杩涢樁绡囷細楂樼骇鐢ㄦ硶

### 1. 鑷畾涔夋ā鏉垮嚱鏁?
#### 鍒涘缓鑷畾涔夊嚱鏁?```java
@Component
public class MyTemplateFunctions {
    
    @TemplateFunction("formatDate")
    public String formatDate(LocalDate date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(date);
    }
    
    @TemplateFunction("truncate")
    public String truncate(String text, int length) {
        if (text == null || text.length() <= length) {
            return text;
        }
        return text.substring(0, length) + "...";
    }
}
```

#### 鍦ㄦā鏉夸腑浣跨敤
```html
<p>鍙戝竷鏃ユ湡: ${formatDate(post.createTime, 'yyyy骞碝M鏈坉d鏃?)}</p>
<p>鎽樿: ${truncate(post.content, 100)}</p>
```

### 2. 妯℃澘缂撳瓨鍜屾€ц兘浼樺寲

#### 閰嶇疆缂撳瓨绛栫暐
```yaml
est:
  web:
    template:
      cache: true
      cache-ttl: 3600  # 缂撳瓨1灏忔椂
      check-interval: 60  # 姣?0绉掓鏌ユ洿鏂?```

#### 棰勭紪璇戞ā鏉?```java
@Service
public class TemplatePreloader {
    
    @Inject
    private TemplateEngine templateEngine;
    
    @OnApplicationStart
    public void preloadTemplates() {
        List<String> templates = Arrays.asList(
            "index",
            "product/list",
            "product/detail",
            "user/profile"
        );
        
        templates.forEach(tpl -> {
            try {
                templateEngine.precompile(tpl);
            } catch (Exception e) {
                // 璁板綍閿欒浣嗕笉褰卞搷鍚姩
            }
        });
    }
}
```

### 3. 鍥介檯鍖栨敮鎸?
#### 閰嶇疆鍥介檯鍖?```yaml
est:
  web:
    template:
      i18n:
        enabled: true
        basename: messages
        default-locale: zh_CN
```

#### 璧勬簮鏂囦欢
```properties
# messages_zh_CN.properties
welcome=娆㈣繋
product.list=浜у搧鍒楄〃
button.submit=鎻愪氦
```

```properties
# messages_en_US.properties
welcome=Welcome
product.list=Product List
button.submit=Submit
```

#### 鍦ㄦā鏉夸腑浣跨敤
```html
<h1>${i18n('welcome')}</h1>
<h2>${i18n('product.list')}</h2>
<button>${i18n('button.submit')}</button>
```

### 4. 妯℃澘瀹夊叏

#### 娌欑閰嶇疆
```java
@Configuration
public class TemplateSecurityConfig {
    
    @Bean
    public TemplateSecurityConfigurer securityConfigurer() {
        return new TemplateSecurityConfigurer()
            .allowClass("java.lang.String")
            .allowClass("java.util.List")
            .denyMethod("java.lang.Runtime", "exec")
            .denyMethod("java.lang.System", "exit");
    }
}
```

---

## 鏈€浣冲疄璺?
### 鉁?鎺ㄨ崘鍋氭硶

| 鍦烘櫙 | 鎺ㄨ崘鍋氭硶 | 璇存槑 |
|------|---------|------|
| 鐩綍缁撴瀯 | 鎸夊姛鑳芥ā鍧楃粍缁?| templates/user/, templates/product/ |
| 鍛藉悕瑙勮寖 | 灏忓啓+杩炲瓧绗?| product-list.ftl, user-profile.ftl |
| 甯冨眬澶嶇敤 | 浣跨敤妯℃澘缁ф壙 | 鍑忓皯閲嶅浠ｇ爜 |
| 涓氬姟閫昏緫 | 灏介噺鍦?Controller 澶勭悊 | 妯℃澘鍙礋璐ｅ睍绀?|
| 鎬ц兘浼樺寲 | 鐢熶骇鐜寮€鍚紦瀛?| 澶у箙鎻愬崌娓叉煋閫熷害 |

### 鉂?涓嶆帹鑽愬仛娉?
```java
// 鉂?涓嶈鍦ㄦā鏉夸腑鍐欏鏉傞€昏緫
<#-- 妯℃澘涓?-->
<#if user.age > 18 && user.country == 'CN' && user.status == 'ACTIVE'>
    ...
</#if>

// 鉁?搴旇鍦?Controller 涓鐞?// Controller
mv.addObject("canView", userService.canViewContent(user));

// 妯℃澘
<#if canView>
    ...
</#if>
```

---

## 妯″潡缁撴瀯

```
est-web-template/
鈹溾攢鈹€ est-web-template-api/            # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/web/template/
鈹?  鈹?      鈹溾攢鈹€ TemplateEngine.java      # 妯℃澘寮曟搸鎺ュ彛
鈹?  鈹?      鈹溾攢鈹€ ModelAndView.java        # 妯″瀷瑙嗗浘
鈹?  鈹?      鈹溾攢鈹€ TemplateConfig.java      # 閰嶇疆鎺ュ彛
鈹?  鈹?      鈹斺攢鈹€ TemplateFunction.java    # 妯℃澘鍑芥暟娉ㄨВ
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-web-template-freemarker/    # Freemarker 瀹炵幇
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-web-template-thymeleaf/     # Thymeleaf 瀹炵幇
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-web-template-mustache/      # Mustache 瀹炵幇
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [Web 璺敱妯″潡](../est-web-router/README.md)
- [Web 浼氳瘽妯″潡](../est-web-session/README.md)
- [EST 蹇€熷叆闂╙(../../docs/getting-started/README.md)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-web/)
