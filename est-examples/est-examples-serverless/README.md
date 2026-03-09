# EST Serverless 示例

本示例展示了如何使用 EST Framework 的 Serverless 功能。

## 支持的云平台

- **AWS Lambda** - Amazon Web Services 无服务器计算
- **Azure Functions** - Microsoft Azure 无服务器计算
- **阿里云函数计算** - 阿里云 Serverless 计算服务
- **Google Cloud Functions** - Google Cloud Platform 无服务器计算

## 项目结构

```
est-examples-serverless/
├── src/main/java/ltd/idcu/est/examples/serverless/
│   ├── HelloWorldFunction.java          # Hello World 示例函数
│   ├── CalculatorFunction.java          # 计算器示例函数
│   ├── OptimizedFunction.java           # 冷启动优化示例函数
│   ├── ServerlessLocalRunnerExample.java # 本地运行器示例
│   ├── aws/                              # AWS Lambda 处理器
│   │   ├── HelloWorldLambdaHandler.java
│   │   └── CalculatorLambdaHandler.java
│   ├── azure/                            # Azure Functions 处理器
│   │   ├── HelloWorldAzureHandler.java
│   │   └── CalculatorAzureHandler.java
│   ├── alibaba/                          # 阿里云函数计算处理器
│   │   ├── HelloWorldFcHandler.java
│   │   └── CalculatorFcHandler.java
│   └── google/                           # Google Cloud Functions 处理器
│       ├── HelloWorldGoogleHandler.java
│       └── CalculatorGoogleHandler.java
├── pom.xml
└── README.md
```

## 快速开始

### Hello World 函数

一个简单的问候函数，接受 `name` 参数并返回问候语。

**请求示例：**
```
GET /hello?name=EST
```

**响应示例：**
```json
{
  "message": "Hello, EST!",
  "version": "1.0.0"
}
```

### 计算器函数

支持加、减、乘、除运算的计算器函数。

**请求参数：**
- `op`: 操作类型（add/sub/mul/div）
- `a`: 第一个数字
- `b`: 第二个数字

**请求示例：**
```
GET /calculator?op=add&a=10&b=5
GET /calculator?op=mul&a=3&b=4
GET /calculator?op=div&a=15&b=3
```

**响应示例：**
```json
{
  "operation": "addition",
  "a": 10.0,
  "b": 5.0,
  "result": 15.0
}
```

### 优化函数

使用 ColdStartOptimizer 的优化函数示例，展示冷启动优化、缓存和统计功能。

**请求参数：**
- `action`: 操作类型（status/cache/stats/reset）

**请求示例：**
```
GET /optimized?action=status
GET /optimized?action=cache
GET /optimized?action=stats
GET /optimized?action=reset
```

**响应示例（status）：**
```json
{
  "status": "ok",
  "function": "optimized-function",
  "initialized": true,
  "cache_size": 2
}
```

**响应示例（stats）：**
```json
{
  "totalColdStarts": 1,
  "totalWarmStarts": 5,
  "coldStartRate": 0.166,
  "functions": {...}
}
```

## 各平台使用指南

### AWS Lambda

1. 构建项目
```bash
mvn clean package
```

2. 部署到 AWS Lambda
   - 使用 AWS SAM 模板：参考 `deploy/serverless/aws/template.yaml`
   - 或通过 AWS Console 手动上传 JAR 包

3. Handler 配置
   - HelloWorld: `ltd.idcu.est.examples.serverless.aws.HelloWorldLambdaHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.aws.CalculatorLambdaHandler`

### Azure Functions

1. 构建项目
```bash
mvn clean package
```

2. 部署到 Azure Functions
   - 参考 `deploy/serverless/azure/` 目录下的配置文件
   - 使用 Azure Functions Core Tools 进行部署

3. Handler 配置
   - HelloWorld: `ltd.idcu.est.examples.serverless.azure.HelloWorldAzureHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.azure.CalculatorAzureHandler`

### 阿里云函数计算

1. 构建项目
```bash
mvn clean package
```

2. 部署到阿里云函数计算
   - 使用 Serverless Devs：参考 `deploy/serverless/alibaba/template.yml`
   - 或通过阿里云控制台部署

3. Handler 配置
   - HelloWorld: `ltd.idcu.est.examples.serverless.alibaba.HelloWorldFcHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.alibaba.CalculatorFcHandler`

### Google Cloud Functions

1. 构建项目
```bash
mvn clean package
```

2. 部署到 Google Cloud Functions
   - 使用 gcloud CLI 进行部署
   - 参考 `deploy/serverless/google/README.md`

3. Handler 配置
   - HelloWorld: `ltd.idcu.est.examples.serverless.google.HelloWorldGoogleHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.google.CalculatorGoogleHandler`

## 创建自己的 Serverless 函数

### 1. 实现 HttpServerlessFunction

```java
public class MyFunction implements HttpServerlessFunction {
    
    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        String body = "{\"message\":\"Hello from my function!\"}";
        ServerlessResponse response = ServerlessResponse.ok(body);
        response.addHeader("Content-Type", "application/json");
        return response;
    }
}
```

### 2. 创建平台特定的 Handler

**AWS Lambda:**
```java
public class MyLambdaHandler extends AwsLambdaHandler {
    public MyLambdaHandler() {
        super(new MyFunction());
    }
}
```

**Azure Functions:**
```java
public class MyAzureHandler extends AzureFunctionHandler {
    public MyAzureHandler() {
        super(new MyFunction());
    }
}
```

**阿里云函数计算:**
```java
public class MyFcHandler extends AlibabaFcHandler {
    public MyFcHandler() {
        super(new MyFunction());
    }
}
```

**Google Cloud Functions:**
```java
public class MyGoogleHandler extends GoogleCloudFunctionHandler {
    public MyGoogleHandler() {
        super(new MyFunction());
    }
}
```

## ServerlessRequest API

```java
ServerlessRequest request = ...;

// 获取 HTTP 方法
String method = request.getMethod();

// 获取路径
String path = request.getPath();

// 获取查询参数
Map<String, String> queryParams = request.getQueryParameters();
String value = request.getQueryParameters().get("key");

// 获取请求头
Map<String, String> headers = request.getHeaders();
String header = request.getHeaders().get("Content-Type");

// 获取请求体
String body = request.getBody();
```

## ServerlessResponse API

```java
// 200 OK 响应
ServerlessResponse.ok("{\"status\":\"ok\"}");

// 400 Bad Request
ServerlessResponse.badRequest("{\"error\":\"Invalid input\"}");

// 500 Internal Server Error
ServerlessResponse.serverError("{\"error\":\"Something went wrong\"}");

// 自定义状态码
ServerlessResponse.create(201, "{\"created\":true}");

// 添加响应头
ServerlessResponse response = ServerlessResponse.ok(body);
response.addHeader("Content-Type", "application/json");
response.addHeader("X-Custom-Header", "value");
```

## 本地运行和测试

### 使用 ServerlessLocalRunner

EST Framework 提供了 ServerlessLocalRunner，可以在本地测试和调试 Serverless 函数，无需部署到云平台。

**运行本地示例：**
```bash
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.examples.serverless.ServerlessLocalRunnerExample"
```

**本地运行器功能：**
- 注册多个函数
- 编程方式调用函数
- 交互式命令行模式
- 完整的上下文支持

**交互式命令：**
```
> list              - 列出所有注册的函数
> invoke <name> [input] - 调用函数
> help              - 显示帮助
> exit/quit         - 退出
```

## 冷启动优化

### 使用 ColdStartOptimizer

EST Framework 提供了 ColdStartOptimizer 来帮助优化 Serverless 函数的冷启动性能。

**核心功能：**
- 冷启动/热启动统计
- 预热判断
- 资源预热
- 性能统计

**使用示例：**
```java
public class OptimizedFunction implements HttpServerlessFunction {
    
    private final ColdStartOptimizer optimizer = ColdStartOptimizer.getInstance();
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    @Override
    public void initialize(Map<String, Object> config) {
        optimizer.recordColdStart("my-function");
        
        optimizer.preWarm("my-function", () -> {
            cache.put("key", "value");
        });
    }
    
    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        optimizer.recordWarmStart("my-function");
        
        Map<String, Object> stats = optimizer.getStatistics();
        // 使用统计信息...
    }
}
```

## 最佳实践

### 1. 冷启动优化
- 保持 `initialize()` 方法精简
- 使用 ColdStartOptimizer 进行预热
- 延迟加载非必要资源
- 使用连接池和缓存
- 考虑使用预配置并发（AWS Lambda）

### 2. 错误处理
- 捕获所有异常
- 返回适当的 HTTP 状态码
- 记录详细的错误日志
- 提供有意义的错误消息

### 3. 安全
- 使用环境变量存储敏感信息
- 验证所有输入
- 实施适当的认证和授权
- 避免在日志中记录敏感数据

### 4. 性能
- 最小化依赖
- 使用高效的数据结构
- 考虑使用 GraalVM 原生镜像
- 监控性能指标

## 更多信息

- 查看 `deploy/serverless/` 目录下的平台特定部署文档
- 参考 EST Framework 官方文档
- 查看各云平台的 Serverless 官方文档

## 相关资源

- [AWS Lambda 文档](https://docs.aws.amazon.com/lambda/)
- [Azure Functions 文档](https://learn.microsoft.com/azure/azure-functions/)
- [阿里云函数计算文档](https://help.aliyun.com/product/50980.html)
- [Google Cloud Functions 文档](https://cloud.google.com/functions)
