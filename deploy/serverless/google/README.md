# Google Cloud Functions 部署指南

本指南介绍如何将 EST 框架应用部署�?Google Cloud Functions�?
## 前置条件

- Google Cloud 账号
- Google Cloud SDK (gcloud) 已安�?- Java 21+
- Maven 3.8+

## 快速开�?
### 1. 配置 gcloud

```bash
# 登录
gcloud auth login

# 设置项目
gcloud config set project YOUR_PROJECT_ID

# 设置区域
gcloud config set functions/region us-central1
```

### 2. 项目配置

�?pom.xml 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-api</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-google</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

### 3. 创建函数

```java
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

public class HelloWorldGoogleHandler extends GoogleCloudFunctionHandler {
    public HelloWorldGoogleHandler() {
        super(new HelloWorldFunction());
    }
}
```

### 4. 构建项目

```bash
mvn clean package
```

### 5. 部署函数

```bash
gcloud functions deploy est-hello-world \
  --gen2 \
  --runtime=java21 \
  --region=us-central1 \
  --source=target/deploy \
  --entry-point=ltd.idcu.est.examples.serverless.HelloWorldGoogleHandler \
  --trigger-http \
  --allow-unauthenticated \
  --memory=512MB \
  --timeout=30s \
  --set-env-vars=APP_ENV=production,LOG_LEVEL=INFO
```

### 6. 测试函数

```bash
gcloud functions call est-hello-world --data '{"name":"EST"}'
```

或者使�?HTTP 调用�?
```bash
curl $(gcloud functions describe est-hello-world --gen2 --format='value(serviceConfig.uri)')?name=EST
```

## 高级配置

### 环境变量

```bash
gcloud functions deploy est-hello-world \
  --set-env-vars=APP_ENV=production \
  --set-env-vars=DATABASE_URL=jdbc:postgresql://... \
  --set-secrets=API_KEY=projects/[PROJECT]/secrets/API_KEY/versions/latest
```

### 内存�?CPU

```bash
gcloud functions deploy est-hello-world \
  --memory=1024MB \
  --cpu=2
```

### 超时设置

```bash
gcloud functions deploy est-hello-world \
  --timeout=60s
```

### 最小实例数

减少冷启动：

```bash
gcloud functions deploy est-hello-world \
  --min-instances=2 \
  --max-instances=100
```

### VPC 访问

```bash
gcloud functions deploy est-hello-world \
  --vpc-connector=projects/[PROJECT]/locations/[REGION]/connectors/[CONNECTOR] \
  --egress-settings=all
```

### 安全配置

#### 服务账号

```bash
gcloud functions deploy est-hello-world \
  --service-account=[SERVICE_ACCOUNT]@[PROJECT].iam.gserviceaccount.com
```

#### 限制访问

移除 `--allow-unauthenticated`，然后配�?IAM�?
```bash
gcloud functions add-iam-policy-binding est-hello-world \
  --member=user:your-email@example.com \
  --role=roles/cloudfunctions.invoker
```

## 监控和日�?
### 查看日志

```bash
gcloud functions logs read est-hello-world --limit=50
```

实时跟踪�?
```bash
gcloud functions logs read est-hello-world --tail
```

### 性能监控

�?Google Cloud Console 中查看：
- 调用次数
- 延迟分布
- 错误�?- 内存使用
- 执行时间

### Trace 分析

启用 Cloud Trace�?
```bash
gcloud functions deploy est-hello-world \
  --ingress-settings=all
```

## 本地开�?
### 使用 Functions Framework

```bash
# 添加依赖
<dependency>
    <groupId>com.google.cloud.functions</groupId>
    <artifactId>functions-framework-api</artifactId>
    <version>1.0.4</version>
    <scope>provided</scope>
</dependency>

# 本地运行
mvn function:run
```

### 本地测试

```bash
curl http://localhost:8080?name=EST
```

## 清理资源

```bash
gcloud functions delete est-hello-world
```

## 参考资�?
- [Google Cloud Functions 官方文档](https://cloud.google.com/functions/docs)
- [Java 函数开发指南](https://cloud.google.com/functions/docs/concepts/java-runtime)
- [Cloud Functions 最佳实践](https://cloud.google.com/functions/docs/bestpractices)
- [本地开发工具](https://cloud.google.com/functions/docs/running/function-frameworks)
