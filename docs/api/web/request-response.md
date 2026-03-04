# Request & Response API

本页面详细介绍 `Request` 和 `Response` 接口的使用方法。

## Request 接口

`Request` 接口提供了访问 HTTP 请求信息的方法。

### 接口定义

```java
package ltd.idcu.est.web.api;

public interface Request {
    HttpMethod getMethod();
    String getPath();
    String getUri();
    String getQueryString();
    
    String getHeader(String name);
    List<String> getHeaders(String name);
    Map<String, List<String>> getAllHeaders();
    
    String getParameter(String name);
    String getParameter(String name, String defaultValue);
    int getIntParameter(String name, int defaultValue);
    long getLongParameter(String name, long defaultValue);
    double getDoubleParameter(String name, double defaultValue);
    boolean getBooleanParameter(String name, boolean defaultValue);
    Map<String, List<String>> getAllParameters();
    
    String getPathVariable(String name);
    Map<String, String> getAllPathVariables();
    
    String getBody();
    <T> T getJsonBody(Class<T> type);
    Map<String, Object> getJsonBody();
    
    String getContentType();
    long getContentLength();
    
    Session getSession();
    Session getSession(boolean create);
    
    Object getAttribute(String name);
    void setAttribute(String name, Object value);
    Map<String, Object> getAllAttributes();
    
    String getRemoteAddress();
    int getRemotePort();
    
    String getProtocol();
    boolean isSecure();
}
```

### 请求方法和路径

#### getMethod

获取 HTTP 方法。

**返回：**
- HTTP 方法

**示例：**
```java
HttpMethod method = request.getMethod();
if (method == HttpMethod.GET) {
    // 处理 GET 请求
}
```

#### getPath

获取请求路径。

**返回：**
- 请求路径

**示例：**
```java
String path = request.getPath();
if (path.equals("/users")) {
    // 处理用户列表
}
```

#### getUri

获取完整 URI。

**返回：**
- 完整 URI

**示例：**
```java
String uri = request.getUri();
System.out.println("Full URI: " + uri);
```

#### getQueryString

获取查询字符串。

**返回：**
- 查询字符串

**示例：**
```java
String queryString = request.getQueryString();
System.out.println("Query: " + queryString);
```

### 请求头

#### getHeader

获取请求头。

**参数：**
- `name` - 头名称

**返回：**
- 头值，不存在返回 null

**示例：**
```java
String userAgent = request.getHeader("User-Agent");
String contentType = request.getHeader("Content-Type");
```

#### getHeaders

获取请求头的所有值。

**参数：**
- `name` - 头名称

**返回：**
- 头值列表

**示例：**
```java
List<String> acceptHeaders = request.getHeaders("Accept");
for (String accept : acceptHeaders) {
    System.out.println("Accept: " + accept);
}
```

#### getAllHeaders

获取所有请求头。

**返回：**
- 所有请求头的映射

**示例：**
```java
Map<String, List<String>> headers = request.getAllHeaders();
for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

### 查询参数

#### getParameter

获取查询参数。

**参数：**
- `name` - 参数名称
- `defaultValue` - 默认值（可选）

**返回：**
- 参数值或默认值

**示例：**
```java
String query = request.getParameter("q");
String page = request.getParameter("page", "1");
```

#### getIntParameter

获取整数查询参数。

**参数：**
- `name` - 参数名称
- `defaultValue` - 默认值

**返回：**
- 整数值或默认值

**示例：**
```java
int page = request.getIntParameter("page", 1);
int limit = request.getIntParameter("limit", 10);
```

#### getLongParameter

获取长整数查询参数。

**参数：**
- `name` - 参数名称
- `defaultValue` - 默认值

**返回：**
- 长整数值或默认值

**示例：**
```java
long since = request.getLongParameter("since", 0L);
```

#### getDoubleParameter

获取浮点数查询参数。

**参数：**
- `name` - 参数名称
- `defaultValue` - 默认值

**返回：**
- 浮点数值或默认值

**示例：**
```java
double minPrice = request.getDoubleParameter("minPrice", 0.0);
```

#### getBooleanParameter

获取布尔查询参数。

**参数：**
- `name` - 参数名称
- `defaultValue` - 默认值

**返回：**
- 布尔值或默认值

**示例：**
```java
boolean verbose = request.getBooleanParameter("verbose", false);
```

#### getAllParameters

获取所有查询参数。

**返回：**
- 所有查询参数的映射

**示例：**
```java
Map<String, List<String>> params = request.getAllParameters();
for (Map.Entry<String, List<String>> entry : params.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

### 路径参数

#### getPathVariable

获取路径参数。

**参数：**
- `name` - 参数名称

**返回：**
- 路径参数值

**示例：**
```java
String userId = request.getPathVariable("id");
String postId = request.getPathVariable("postId");
```

#### getAllPathVariables

获取所有路径参数。

**返回：**
- 所有路径参数的映射

**示例：**
```java
Map<String, String> pathVars = request.getAllPathVariables();
for (Map.Entry<String, String> entry : pathVars.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

### 请求体

#### getBody

获取原始请求体。

**返回：**
- 原始请求体字符串

**示例：**
```java
String body = request.getBody();
System.out.println("Request body: " + body);
```

#### getJsonBody

获取 JSON 请求体。

**参数：**
- `type` - 目标类型（可选）

**返回：**
- 解析后的对象或 Map

**示例：**
```java
// 作为 Map 获取
Map<String, Object> body = request.getJsonBody();
String name = (String) body.get("name");

// 作为特定类型获取
User user = request.getJsonBody(User.class);
```

### 内容类型和长度

#### getContentType

获取内容类型。

**返回：**
- 内容类型

**示例：**
```java
String contentType = request.getContentType();
if (contentType != null && contentType.startsWith("application/json")) {
    // 处理 JSON 请求
}
```

#### getContentLength

获取内容长度。

**返回：**
- 内容长度（字节）

**示例：**
```java
long length = request.getContentLength();
if (length > 1048576) {
    // 请求体太大
}
```

### 会话

#### getSession

获取会话。

**参数：**
- `create` - 是否在不存在时创建

**返回：**
- 会话对象

**示例：**
```java
// 获取现有会话，不存在返回 null
Session session = request.getSession(false);

// 获取或创建会话
Session session = request.getSession(true);
```

### 属性

#### getAttribute

获取请求属性。

**参数：**
- `name` - 属性名称

**返回：**
- 属性值

**示例：**
```java
User user = (User) request.getAttribute("user");
```

#### setAttribute

设置请求属性。

**参数：**
- `name` - 属性名称
- `value` - 属性值

**示例：**
```java
request.setAttribute("user", currentUser);
```

#### getAllAttributes

获取所有请求属性。

**返回：**
- 所有请求属性的映射

**示例：**
```java
Map<String, Object> attrs = request.getAllAttributes();
```

### 客户端信息

#### getRemoteAddress

获取客户端地址。

**返回：**
- 客户端 IP 地址

**示例：**
```java
String clientIp = request.getRemoteAddress();
System.out.println("Client IP: " + clientIp);
```

#### getRemotePort

获取客户端端口。

**返回：**
- 客户端端口

**示例：**
```java
int clientPort = request.getRemotePort();
```

### 协议信息

#### getProtocol

获取协议版本。

**返回：**
- 协议版本

**示例：**
```java
String protocol = request.getProtocol();
System.out.println("Protocol: " + protocol);
```

#### isSecure

检查是否使用 HTTPS。

**返回：**
- 是否使用 HTTPS

**示例：**
```java
if (request.isSecure()) {
    // 使用 HTTPS
}
```

---

## Response 接口

`Response` 接口提供了构建 HTTP 响应的方法。

### 接口定义

```java
package ltd.idcu.est.web.api;

public interface Response {
    Response status(int statusCode);
    int getStatus();
    
    Response header(String name, String value);
    String getHeader(String name);
    Map<String, String> getAllHeaders();
    
    Response contentType(String contentType);
    String getContentType();
    
    Response body(String body);
    String getBody();
    
    Response text(String text);
    Response html(String html);
    Response json(Object data);
    Response xml(String xml);
    
    Response redirect(String location);
    Response redirect(String location, int statusCode);
    
    Response cookie(String name, String value);
    Response cookie(String name, String value, int maxAge);
    Response removeCookie(String name);
    
    void send();
    boolean isSent();
}
```

### 状态码

#### status

设置状态码。

**参数：**
- `statusCode` - HTTP 状态码

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.status(200);           // OK
response.status(201);           // Created
response.status(400);           // Bad Request
response.status(401);           // Unauthorized
response.status(403);           // Forbidden
response.status(404);           // Not Found
response.status(500);           // Internal Server Error
```

#### getStatus

获取当前状态码。

**返回：**
- 当前状态码

**示例：**
```java
int status = response.getStatus();
```

### 响应头

#### header

设置响应头。

**参数：**
- `name` - 头名称
- `value` - 头值

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.header("Content-Type", "application/json")
        .header("Cache-Control", "no-cache")
        .header("X-Frame-Options", "DENY");
```

#### getHeader

获取响应头。

**参数：**
- `name` - 头名称

**返回：**
- 头值

**示例：**
```java
String contentType = response.getHeader("Content-Type");
```

#### getAllHeaders

获取所有响应头。

**返回：**
- 所有响应头的映射

**示例：**
```java
Map<String, String> headers = response.getAllHeaders();
```

### 内容类型

#### contentType

设置内容类型。

**参数：**
- `contentType` - 内容类型

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.contentType("text/plain");
response.contentType("text/html; charset=UTF-8");
response.contentType("application/json");
response.contentType("application/xml");
```

#### getContentType

获取当前内容类型。

**返回：**
- 当前内容类型

**示例：**
```java
String contentType = response.getContentType();
```

### 响应体

#### body

设置原始响应体。

**参数：**
- `body` - 响应体内容

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.body("Hello, World!");
```

#### getBody

获取当前响应体。

**返回：**
- 当前响应体

**示例：**
```java
String body = response.getBody();
```

### 快捷响应方法

#### text

发送文本响应。

**参数：**
- `text` - 文本内容

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.text("Hello, World!");
response.status(200).text("Success");
```

#### html

发送 HTML 响应。

**参数：**
- `html` - HTML 内容

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.html("""
    <!DOCTYPE html>
    <html>
    <body>
        <h1>Hello, World!</h1>
    </body>
    </html>
    """);
```

#### json

发送 JSON 响应。

**参数：**
- `data` - 要序列化的对象

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.json(Map.of("message", "Hello"));
response.json(user);
response.status(200).json(users);
```

#### xml

发送 XML 响应。

**参数：**
- `xml` - XML 内容

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.xml("<user><name>Alice</name></user>");
```

### 重定向

#### redirect

重定向到另一个 URL。

**参数：**
- `location` - 目标 URL
- `statusCode` - 状态码（可选，默认 302）

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.redirect("/new-page");
response.redirect("https://example.com");
response.redirect("/moved", 301);  // 永久重定向
```

### Cookie

#### cookie

设置 Cookie。

**参数：**
- `name` - Cookie 名称
- `value` - Cookie 值
- `maxAge` - 最大年龄（秒，可选）

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.cookie("sessionId", "abc123");
response.cookie("userId", "123", 3600);  // 1小时过期
```

#### removeCookie

删除 Cookie。

**参数：**
- `name` - Cookie 名称

**返回：**
- 响应对象（链式调用）

**示例：**
```java
response.removeCookie("sessionId");
```

### 发送响应

#### send

发送响应。

**示例：**
```java
response.text("Hello").send();
```

#### isSent

检查响应是否已发送。

**返回：**
- 响应是否已发送

**示例：**
```java
if (!response.isSent()) {
    response.text("Hello").send();
}
```

## 完整示例

```java
router.get("/api/users/:id", (req, res) -> {
    // 获取路径参数
    String userId = req.getPathVariable("id");
    
    // 获取查询参数
    boolean includeDetails = req.getBooleanParameter("details", false);
    
    // 获取请求头
    String authHeader = req.getHeader("Authorization");
    
    // 验证
    if (authHeader == null) {
        res.status(401).json(Map.of("error", "Unauthorized"));
        return;
    }
    
    // 查找用户
    User user = userService.findById(userId);
    if (user == null) {
        res.status(404).json(Map.of("error", "User not found"));
        return;
    }
    
    // 返回响应
    res.json(user);
});
```

## 最佳实践

1. **使用适当的状态码**
   - 200 OK - 成功
   - 201 Created - 创建成功
   - 400 Bad Request - 请求错误
   - 401 Unauthorized - 未授权
   - 403 Forbidden - 禁止访问
   - 404 Not Found - 未找到
   - 500 Internal Server Error - 服务器错误

2. **设置正确的 Content-Type**
   - text/plain - 纯文本
   - text/html - HTML
   - application/json - JSON
   - application/xml - XML

3. **使用便捷方法**
   - text() - 发送文本
   - html() - 发送 HTML
   - json() - 发送 JSON

4. **链式调用**
   ```java
   response.status(200)
           .header("Cache-Control", "no-cache")
           .json(data);
   ```
