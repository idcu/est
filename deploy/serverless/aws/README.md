# AWS Lambda 部署指南

本指南介绍如何将 EST 框架应用部署�?AWS Lambda�?
## 前置条件

- AWS 账号
- AWS CLI 已配�?- AWS SAM CLI 已安�?- Java 21+
- Maven 3.8+

## 快速开�?
### 1. 项目配置

�?pom.xml 中添�?AWS Lambda 插件�?
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="com.github.edwgiz.mavenShadePlugin.log4j2CacheTransformer.PluginsCacheFileTransformer">
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
            <dependencies>
                <dependency>
                    <groupId>com.github.edwgiz</groupId>
                    <artifactId>maven-shade-plugin.log4j2-cachefile-transformer</artifactId>
                    <version>2.15</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

### 2. 构建项目

```bash
mvn clean package
```

### 3. 使用 SAM 部署

```bash
# 进入配置目录
cd deploy/serverless/aws

# 构建 SAM 应用
sam build

# 部署�?AWS
sam deploy --guided
```

部署时会提示你输入以下信息：
- Stack Name: 你的 CloudFormation 堆栈名称
- AWS Region: AWS 区域 (�?us-east-1)
- Confirm changes before deploy: N
- Allow SAM CLI IAM role creation: Y
- Save arguments to samconfig.toml: Y

### 4. 测试函数

部署成功后，你可以在输出中看�?API Gateway �?URL�?
```bash
curl https://<api-id>.execute-api.<region>.amazonaws.com/Prod/hello?name=EST
```

## 高级配置

### 环境变量

�?`template.yaml` 中配置环境变量：

```yaml
Globals:
  Function:
    Environment:
      Variables:
        APP_ENV: production
        DATABASE_URL: !Sub "{{resolve:ssm:/est/database/url:1}}"
        API_KEY: !Sub "{{resolve:secretsmanager:/est/api/key:SecretString:key}}"
```

### 内存和超时设�?
```yaml
Resources:
  EstHelloWorldFunction:
    Type: AWS::Serverless::Function
    Properties:
      MemorySize: 1024
      Timeout: 60
```

### Provisioned Concurrency

为了减少冷启动时间，可以配置 Provisioned Concurrency�?
```yaml
Resources:
  EstHelloWorldFunction:
    Type: AWS::Serverless::Function
    Properties:
      AutoPublishAlias: live
      ProvisionedConcurrencyConfig:
        ProvisionedConcurrentExecutions: 5
```

### VPC 配置

如果需要访�?VPC 内的资源�?
```yaml
Resources:
  EstHelloWorldFunction:
    Type: AWS::Serverless::Function
    Properties:
      VpcConfig:
        SecurityGroupIds:
          - sg-123456789
        SubnetIds:
          - subnet-123456789
          - subnet-987654321
```

### �?(Layers)

使用层来共享依赖，减少部署包大小�?
```yaml
Resources:
  EstDependenciesLayer:
    Type: AWS::Serverless::LayerVersion
    Properties:
      LayerName: est-dependencies
      Description: EST Framework Dependencies
      ContentUri: dependencies/
      CompatibleRuntimes:
        - java21
      RetentionPolicy: Retain

  EstHelloWorldFunction:
    Type: AWS::Serverless::Function
    Properties:
      Layers:
        - !Ref EstDependenciesLayer
```

## 监控和日�?
### CloudWatch Logs

Lambda 会自动将日志发送到 CloudWatch Logs。日志组名称格式为：
`/aws/lambda/<function-name>`

查看日志�?```bash
aws logs tail /aws/lambda/est-hello-world --follow
```

### X-Ray 追踪

启用 X-Ray 追踪�?
```yaml
Resources:
  EstHelloWorldFunction:
    Type: AWS::Serverless::Function
    Properties:
      Tracing: Active
```

## 清理资源

```bash
sam delete
```

## 参考资�?
- [AWS SAM 开发者指南](https://docs.aws.amazon.com/serverless-application-model/)
- [AWS Lambda Java 开发者指南](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java.html)
- [AWS Lambda 最佳实践](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html)
