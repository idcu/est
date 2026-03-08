# est-web-template - 小白从入门到精通

## 目录
- [什么是 est-web-template](#什么是-est-web-template)
- [快速入门：5分钟上手](#快速入门5分钟上手)
- [基础篇：核心功能](#基础篇核心功能)
- [进阶篇：高级用法](#进阶篇高级用法)
- [最佳实践](#最佳实践)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-web-template

### 用大白话理解
est-web-template 就像做网页的"模板打印机"。你先做好一个网页模板，里面留好空位（变量），然后把数据填进去，就能生成完整的网页。支持多种模板引擎，想用哪个用哪个。

### 核心特点
- **多引擎支持**：Freemarker、Thymeleaf、Mustache、Velocity
- **统一 API**：一套接口，切换引擎无需改代码
- **热加载**：开发时修改模板自动刷新
- **模板继承**：支持布局继承和组件复用

---

## 快速入门：5分钟上手

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-web-template</artifactId>
    <version>2.0.0</version>
</dependency>

<!-- 选择一个模板引擎 -->
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-web-template-freemarker</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置模板
```yaml
est:
  web:
    template:
      engine: freemarker  # 模板引擎
      prefix: /templates/ # 模板路径前缀
      suffix: .html       # 模板文件后缀
      cache: false        # 开发时关闭缓存
      encoding: UTF-8
```

### 3. 创建模板
```html
<!-- /templates/index.html -->
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
    <h1>欢迎, ${user.name}!</h1>
    <p>今天是 ${date}</p>
    
    <ul>
        <#list items as item>
            <li>${item.name} - ${item.price}</li>
        </#list>
    </ul>
</body>
</html>
```

### 4. 渲染页面
```java
@Controller
public class HomeController {
    
    @Get("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        
        mv.addObject("title", "首页");
        mv.addObject("user", currentUser());
        mv.addObject("date", LocalDate.now());
        mv.addObject("items", getProducts());
        
        return mv;
    }
}
```

---

## 基础篇：核心功能

### 1. 模板引擎选择

#### Freemarker（推荐）
```yaml
est:
  web:
    template:
      engine: freemarker
```

```html
<!-- Freemarker 语法 -->
<h1>${title}</h1>
<#if user?exists>
    <p>欢迎, ${user.name}</p>
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
<!-- Thymeleaf 语法 -->
<h1 th:text="${title}">标题</h1>
<p th:if="${user != null}" th:text="'欢迎, ' + ${user.name}"></p>
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
<!-- Mustache 语法 -->
<h1>{{title}}</h1>
{{#user}}
    <p>欢迎, {{name}}</p>
{{/user}}
{{#items}}
    <div>{{name}}</div>
{{/items}}
```

### 2. ModelAndView 用法

#### 基本用法
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

#### 快捷方法
```java
// 直接返回模板名称
@Get("/about")
public String about() {
    return "about";
}

// 返回 Map 作为模型
@Get("/contact")
public Map<String, Object> contact() {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "联系我们");
    model.put("form", new ContactForm());
    return model;
}
```

### 3. 模板变量和表达式

#### 变量输出
```java
// 控制器
mv.addObject("username", "张三");
mv.addObject("age", 25);
mv.addObject("user", user);
```

```html
<!-- 模板 -->
<p>用户名: ${username}</p>
<p>年龄: ${age}</p>
<p>邮箱: ${user.email}</p>
```

#### 条件判断
```html
<#if user.online>
    <span class="online">在线</span>
<#elseif user.offline>
    <span class="offline">离线</span>
<#else>
    <span class="away">离开</span>
</#if>
```

#### 循环遍历
```html
<table>
    <tr>
        <th>序号</th>
        <th>名称</th>
        <th>价格</th>
    </tr>
    <#list products as product>
        <tr class="${product_index % 2 == 0 ? 'even' : 'odd'}">
            <td>${product_index + 1}</td>
            <td>${product.name}</td>
            <td>¥${product.price}</td>
        </tr>
    </#list>
</table>
```

### 4. 模板包含和继承

#### 包含组件
```html
<!-- header.ftl -->
<header>
    <nav>
        <a href="/">首页</a>
        <a href="/products">产品</a>
        <a href="/about">关于</a>
    </nav>
</header>
```

```html
<!-- 页面中引用 -->
<#include "header.ftl">

<main>
    <h1>页面内容</h1>
</main>

<#include "footer.ftl">
```

#### 布局继承
```html
<!-- layout.ftl - 基础布局 -->
<!DOCTYPE html>
<html>
<head>
    <title><#nested "title">默认标题</#nested></title>
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
<!-- 使用布局 -->
<#import "layout.ftl" as layout>
<@layout.main>
    <#nested "title">产品列表</#nested>
    
    <#nested "content">
        <h1>产品列表</h1>
        <#list products as product>
            <div class="product">${product.name}</div>
        </#list>
    </#nested>
</@layout.main>
```

---

## 进阶篇：高级用法

### 1. 自定义模板函数

#### 创建自定义函数
```java
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

#### 在模板中使用
```html
<p>发布日期: ${formatDate(post.createTime, 'yyyy年MM月dd日')}</p>
<p>摘要: ${truncate(post.content, 100)}</p>
```

### 2. 模板缓存和性能优化

#### 配置缓存策略
```yaml
est:
  web:
    template:
      cache: true
      cache-ttl: 3600  # 缓存1小时
      check-interval: 60  # 每60秒检查更新
```

#### 预编译模板
```java
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
                // 记录错误但不影响启动
            }
        });
    }
}
```

### 3. 国际化支持

#### 配置国际化
```yaml
est:
  web:
    template:
      i18n:
        enabled: true
        basename: messages
        default-locale: zh_CN
```

#### 资源文件
```properties
# messages_zh_CN.properties
welcome=欢迎
product.list=产品列表
button.submit=提交
```

```properties
# messages_en_US.properties
welcome=Welcome
product.list=Product List
button.submit=Submit
```

#### 在模板中使用
```html
<h1>${i18n('welcome')}</h1>
<h2>${i18n('product.list')}</h2>
<button>${i18n('button.submit')}</button>
```

### 4. 模板安全

#### 沙箱配置
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

## 最佳实践

### ✅ 推荐做法

| 场景 | 推荐做法 | 说明 |
|------|---------|------|
| 目录结构 | 按功能模块组织 | templates/user/, templates/product/ |
| 命名规范 | 小写+连字符 | product-list.ftl, user-profile.ftl |
| 布局复用 | 使用模板继承 | 减少重复代码 |
| 业务逻辑 | 尽量在 Controller 处理 | 模板只负责展示 |
| 性能优化 | 生产环境开启缓存 | 大幅提升渲染速度 |

### ❌ 不推荐做法

```java
// ❌ 不要在模板中写复杂逻辑
<#-- 模板中 -->
<#if user.age > 18 && user.country == 'CN' && user.status == 'ACTIVE'>
    ...
</#if>

// ✅ 应该在 Controller 中处理
// Controller
mv.addObject("canView", userService.canViewContent(user));

// 模板
<#if canView>
    ...
</#if>
```

---

## 模块结构

```
est-web-template/
├── est-web-template-api/            # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/web/template/
│   │       ├── TemplateEngine.java      # 模板引擎接口
│   │       ├── ModelAndView.java        # 模型视图
│   │       ├── TemplateConfig.java      # 配置接口
│   │       └── TemplateFunction.java    # 模板函数注解
│   └── pom.xml
├── est-web-template-freemarker/    # Freemarker 实现
│   ├── src/main/java/
│   └── pom.xml
├── est-web-template-thymeleaf/     # Thymeleaf 实现
│   ├── src/main/java/
│   └── pom.xml
├── est-web-template-mustache/      # Mustache 实现
│   ├── src/main/java/
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [Web 路由模块](../est-web-router/README.md)
- [Web 会话模块](../est-web-session/README.md)
- [EST 快速入门](../../docs/getting-started/README.md)
- [示例代码](../../est-examples/est-examples-web/)
