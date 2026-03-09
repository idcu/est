# Serverless 部署配置

本目录包含 EST 框架在各主流云平台的 Serverless 部署配置。

## 支持的云平台

- AWS Lambda
- Azure Functions
- 阿里云函数计算 (Alibaba Cloud Function Compute)
- Google Cloud Functions

## 目录结构

```
serverless/
├── aws/                    # AWS Lambda 配置
│   ├── template.yaml       # SAM 模板
│   └── README.md
├── azure/                  # Azure Functions 配置
│   ├── host.json
│   ├── local.settings.json
│   └── README.md
├── alibaba/                # 阿里云函数计算配置
│   ├── template.yml        # Serverless Devs 模板
│   └── README.md
├── google/                 # Google Cloud Functions 配置
│   └── README.md
└── README.md               # 本文档
```

## 快速开始

### 1. 引入依赖

根据目标云平台，在你的项目 pom.xml 中添加相应的依赖：

```xml
<!-- Serverless API (必需) -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-api</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>

<!-- AWS Lambda -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-aws</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>

<!-- Azure Functions -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-azure</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>

<!-- 阿里云函数计算 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-alibaba</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>

<!-- Google Cloud Functions -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-google</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

### 2. 创建你的函数

```java
import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

public class HelloWorldFunction implements HttpServerlessFunction {
    
    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        String name = request.getQueryParameters().getOrDefault("name", "World");
        String message = String.format("{\"message\":\"Hello, %s!\"}", name);
        
        ServerlessResponse response = ServerlessResponse.ok(message);
        response.addHeader("Content-Type", "application/json");
        return response;
    }
}
```

### 3. 创建云平台特定的 Handler

**AWS Lambda:**
```java
import ltd.idcu.est.serverless.aws.AwsLambdaHandler;

public class HelloWorldLambdaHandler extends AwsLambdaHandler {
    public HelloWorldLambdaHandler() {
        super(new HelloWorldFunction());
    }
}
```

**Azure Functions:**
```java
import ltd.idcu.est.serverless.azure.AzureFunctionHandler;

public class HelloWorldAzureHandler extends AzureFunctionHandler {
    public HelloWorldAzureHandler() {
        super(new HelloWorldFunction());
    }
}
```

**阿里云函数计算:**
```java
import ltd.idcu.est.serverless.alibaba.AlibabaFcHandler;

public class HelloWorldFcHandler extends AlibabaFcHandler {
    public HelloWorldFcHandler() {
        super(new HelloWorldFunction());
    }
}
```

**Google Cloud Functions:**
```java
import ltd.idcu.est.serverless.google.GoogleCloudFunctionHandler;

public class HelloWorldGoogleHandler extends GoogleCloudFunctionHandler {
    public HelloWorldGoogleHandler() {
        super(new HelloWorldFunction());
    }
}
```

## 平台特定文档

- [AWS Lambda 部署指南](./aws/README.md)
- [Azure Functions 部署指南](./azure/README.md)
- [阿里云函数计算部署指南](./alibaba/README.md)
- [Google Cloud Functions 部署指南](./google/README.md)

## 最佳实践

1. **冷启动优化**
   - 保持初始化代码精简
   - 使用连接池和缓存
   - 考虑使用 Provisioned Concurrency (AWS Lambda)

2. **错误处理**
   - 妥善处理异常
   - 返回适当的 HTTP 状态码
   - 记录详细的错误日志

3. **安全**
   - 使用环境变量存储敏感信息
   - 实施适当的认证和授权
   - 验证所有输入

4. **监控和日志**
   - 使用云平台提供的监控服务
   - 记录关键指标
   - 设置告警

5. **依赖管理**
   - 只引入必要的依赖
   - 使用更小的依赖库
   - 考虑使用原生镜像 (GraalVM)

## 参考资源

- [AWS Lambda 官方文档](https://docs.aws.amazon.com/lambda/)
- [Azure Functions 官方文档](https://learn.microsoft.com/azure/azure-functions/)
- [阿里云函数计算官方文档](https://help.aliyun.com/product/50980.html)
- [Google Cloud Functions 官方文档](https://cloud.google.com/functions)
