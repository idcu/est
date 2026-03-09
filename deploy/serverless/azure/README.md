# Azure Functions 部署指南

本指南介绍如何将 EST 框架应用部署�?Azure Functions�?
## 前置条件

- Azure 账号
- Azure CLI 已安装并登录
- Azure Functions Core Tools
- Java 21+
- Maven 3.8+

## 快速开�?
### 1. 创建 Azure Functions 项目

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.microsoft.azure \
  -DarchetypeArtifactId=azure-functions-archetype \
  -DjavaVersion=21
```

### 2. 配置 pom.xml

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-api</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-serverless-azure</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

### 3. 创建你的函数

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
```

### 4. 创建 Azure Functions Handler

```java
public class HelloWorldAzureHandler extends AzureFunctionHandler {
    public HelloWorldAzureHandler() {
        super(new HelloWorldFunction());
    }
}
```

### 5. 本地运行

```bash
mvn clean package
mvn azure-functions:run
```

### 6. 部署�?Azure

```bash
# 创建资源�?az group create --name est-functions-rg --location eastus

# 创建 Function App
az functionapp create \
  --resource-group est-functions-rg \
  --name est-functions-app \
  --consumption-plan-location eastus \
  --runtime java \
  --runtime-version 21 \
  --functions-version 4 \
  --storage-account estfunctionsstorage

# 部署函数
mvn azure-functions:deploy
```

## 高级配置

### 应用程序设置

使用 Azure CLI 设置环境变量�?
```bash
az functionapp config appsettings set \
  --name est-functions-app \
  --resource-group est-functions-rg \
  --settings "APP_ENV=production" "LOG_LEVEL=INFO"
```

### 监控

#### Application Insights

Azure Functions 默认集成 Application Insights。可以在 Azure 门户中查看：
- 实时指标�?- 失败请求
- 性能
- 日志

查看日志�?```bash
az functionapp log tail \
  --name est-functions-app \
  --resource-group est-functions-rg
```

### 伸缩配置

#### 消费计划 (Consumption Plan)
- 根据请求自动伸缩
- 按执行时间付�?- 默认最大实例数�?00

#### 高级计划 (Premium Plan)
- 预配实例
- 更长的执行时�?- VNet 访问

```bash
az functionapp plan create \
  --resource-group est-functions-rg \
  --name est-premium-plan \
  --sku EP1 \
  --number-of-workers 2

az functionapp create \
  --resource-group est-functions-rg \
  --name est-functions-premium \
  --plan est-premium-plan \
  --runtime java \
  --runtime-version 21 \
  --functions-version 4 \
  --storage-account estfunctionsstorage
```

### 安全配置

#### 系统分配的托管标�?
```bash
az functionapp identity assign \
  --name est-functions-app \
  --resource-group est-functions-rg
```

#### 访问 Key Vault

```bash
az keyvault create \
  --name est-keyvault \
  --resource-group est-functions-rg \
  --location eastus

az keyvault set-policy \
  --name est-keyvault \
  --resource-group est-functions-rg \
  --object-id <managed-identity-object-id> \
  --secret-permissions get list

az functionapp config appsettings set \
  --name est-functions-app \
  --resource-group est-functions-rg \
  --settings "DB_CONNECTION_STRING=@Microsoft.KeyVault(SecretUri=https://est-keyvault.vault.azure.net/secrets/db-connection-string)"
```

## 清理资源

```bash
az group delete --name est-functions-rg
```

## 参考资�?
- [Azure Functions Java 开发者指南](https://learn.microsoft.com/azure/azure-functions/functions-reference-java)
- [Azure Functions 最佳实践](https://learn.microsoft.com/azure/azure-functions/functions-best-practices)
- [Azure Functions 性能优化](https://learn.microsoft.com/azure/azure-functions/functions-performance)
