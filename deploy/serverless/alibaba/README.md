# 阿里云函数计算部署指�?
本指南介绍如何将 EST 框架应用部署到阿里云函数计算�?
## 前置条件

- 阿里云账�?- Serverless Devs (s 工具) 已安�?- 已配置阿里云账号密钥
- Java 21+
- Maven 3.8+

## 快速开�?
### 1. 安装 Serverless Devs

```bash
# Windows
npm install -g @serverless-devs/s

# Mac/Linux
curl -o- -L http://cli.so/install.sh | bash
```

### 2. 配置阿里云账�?
```bash
s config add
```

按照提示输入�?- AccessKey ID
- AccessKey Secret
- Account ID
- Default region (�?cn-hangzhou)

### 3. 项目配置

�?pom.xml 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-api</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-alibaba</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

### 4. 创建函数

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

public class HelloWorldFcHandler extends AlibabaFcHandler {
    public HelloWorldFcHandler() {
        super(new HelloWorldFunction());
    }
}
```

### 5. 构建项目

```bash
mvn clean package
```

### 6. 部署函数

```bash
cd deploy/serverless/alibaba
s deploy
```

### 7. 测试函数

部署成功后，会显示函数的 URL�?
```bash
curl "https://<url>.cn-hangzhou.fc.aliyuncs.com/2016-08-15/proxy/est-hello-world/est-hello-world/?name=EST"
```

## 高级配置

### 环境变量

�?template.yml 中配置：

```yaml
environmentVariables:
  APP_ENV: production
  DATABASE_URL: ${env.DATABASE_URL}
  API_KEY: ${env.API_KEY}
```

使用命令行传递：
```bash
DATABASE_URL=jdbc:mysql://... s deploy
```

### 内存和超�?
```yaml
memorySize: 1024
timeout: 60
cpu: 1.0
```

### 实例类型

```yaml
instanceType: e1  # 性能实例 (默认)
# instanceType: c1  # 计算密集�?# instanceType: g1  # GPU 实例
```

### 预留实例

为了减少冷启动：

```yaml
provisionedConcurrency: 5
```

### VPC 配置

```yaml
vpcConfig:
  vpcId: vpc-xxx
  vswitchIds:
    - vsw-xxx
  securityGroupIds:
    - sg-xxx
```

### �?(Layers)

使用层共享依赖：

```yaml
layers:
  - name: est-dependencies
    description: EST Framework Dependencies
    code: ./layers/est-dependencies
    compatibleRuntime:
      - java21
```

### 日志配置

```yaml
logConfig:
  project: est-log-project
  logstore: est-log-store
  enableRequestMetrics: true
  enableInstanceMetrics: true
```

## 监控和调�?
### 查看日志

```bash
s logs --tail
```

### 在线调试

```bash
s invoke -e '{"httpMethod":"GET","path":"/","queryParameters":{"name":"EST"}}'
```

### 性能监控

在阿里云控制台查看：
- 调用次数
- 延迟
- 错误�?- 资源使用

## 清理资源

```bash
s remove
```

## 参考资�?
- [阿里云函数计算官方文档](https://help.aliyun.com/product/50980.html)
- [Serverless Devs 文档](https://docs.serverless-devs.com/)
- [Java 函数开发指南](https://help.aliyun.com/document_detail/154869.html)
- [函数计算最佳实践](https://help.aliyun.com/document_detail/89681.html)
