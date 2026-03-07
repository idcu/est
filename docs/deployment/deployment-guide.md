# 部署指南

本文档提供 EST 框架应用的完整部署指南，包括项目构建、本地运行、Docker 部署、Kubernetes 部署、CI/CD 持续集成以及生产环境最佳实践。

---

## 目录

- [项目构建](#项目构建)
- [本地运行](#本地运行)
- [systemd 服务部署（单机推荐）](#systemd-服务部署单机推荐)
- [Docker 部署](#docker-部署)
- [Docker Compose 部署](#docker-compose-部署)
- [Kubernetes 部署](#kubernetes-部署)
- [CI/CD 持续集成](#cicd-持续集成)
- [配置管理](#配置管理)
- [日志管理](#日志管理)
- [监控和健康检查](#监控和健康检查)
- [安全建议](#安全建议)
- [性能优化](#性能优化)
- [备份策略](#备份策略)
- [回滚策略](#回滚策略)
- [云平台部署建议](#云平台部署建议)
- [故障排除](#故障排除)

---

## 项目构建

### 使用 Maven 构建

```bash
# 清理并构建项目，跳过测试
mvn clean package -DskipTests

# 构建时运行测试
mvn clean package

# 只编译不打包
mvn clean compile

# 运行测试
mvn test
```

### 构建可执行 JAR

首先，确保你的项目配置正确，能够构建为可执行 JAR。

在 `pom.xml` 中添加：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.3.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>com.example.Application</mainClass>
                        <addClasspath>true</addClasspath>
                    </manifest>
                </archive>
            </configuration>
        </plugin>
        
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
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.example.Application</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

构建应用：

```bash
mvn clean package
```

生成的 JAR 文件位于 `target/` 目录。

---

## 本地运行

### 方式一：使用 Maven 运行

```bash
# 进入示例模块目录
cd est-examples/est-examples-web

# 编译并运行
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

### 方式二：直接运行 JAR

#### 基本运行

```bash
java -jar your-app.jar
```

#### 指定配置文件

```bash
java -jar your-app.jar --config=/path/to/config.properties
```

#### 设置 JVM 参数

```bash
java -Xms512m -Xmx2g -jar your-app.jar
```

#### 后台运行

```bash
# Linux/macOS
nohup java -jar your-app.jar > app.log 2>&1 &

# Windows
start /B java -jar your-app.jar
```

#### 运行示例应用

```bash
# 先构建项目
mvn clean package -DskipTests

# 运行示例应用
java -jar est-examples/est-examples-web/target/est-examples-web-1.3.0-SNAPSHOT.jar
```

---

## systemd 服务部署（单机推荐）

### 创建服务文件

创建 `/etc/systemd/system/yourapp.service`：

```ini
[Unit]
Description=EST Application
After=network.target

[Service]
Type=simple
User=appuser
Group=appgroup
WorkingDirectory=/opt/yourapp
ExecStart=/usr/bin/java -jar /opt/yourapp/yourapp.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=yourapp

# 环境变量
Environment="JAVA_OPTS=-Xms512m -Xmx2g"
Environment="APP_ENV=production"

[Install]
WantedBy=multi-user.target
```

### 管理服务

```bash
# 重新加载 systemd 配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start yourapp

# 停止服务
sudo systemctl stop yourapp

# 重启服务
sudo systemctl restart yourapp

# 查看状态
sudo systemctl status yourapp

# 查看日志
sudo journalctl -u yourapp -f

# 开机自启
sudo systemctl enable yourapp
```

---

## Docker 部署

### Docker 环境准备

#### 安装 Docker

- **Windows/macOS**: 下载 Docker Desktop：https://www.docker.com/products/docker-desktop
- **Linux**: 参考官方文档：https://docs.docker.com/engine/install/

#### 验证 Docker 安装

```bash
docker --version
docker-compose --version
```

### 创建 Dockerfile

```dockerfile
FROM openjdk:21-jdk-slim

LABEL maintainer="your-name"
LABEL description="EST Application"

# 创建应用目录
RUN mkdir -p /opt/app
WORKDIR /opt/app

# 复制应用
COPY target/yourapp.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 构建和运行

```bash
# 构建镜像
docker build -t yourapp:latest .

# 运行容器
docker run -d -p 8080:8080 --name yourapp yourapp:latest

# 查看日志
docker logs -f yourapp
```

---

## Docker Compose 部署

### 创建 docker-compose.yml

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - JAVA_OPTS=-Xms512m -Xmx2g
      - APP_ENV=production
    volumes:
      - ./config:/opt/app/config
      - ./logs:/opt/app/logs
    depends_on:
      - db
    restart: unless-stopped

  db:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=secret
      - MYSQL_DATABASE=yourapp
    volumes:
      - mysql-data:/var/lib/mysql
    restart: unless-stopped

volumes:
  mysql-data:
```

### 使用 docker-compose

```bash
docker-compose up -d
```

---

## Kubernetes 部署

### Deployment 配置

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: yourapp
  labels:
    app: yourapp
spec:
  replicas: 3
  selector:
    matchLabels:
      app: yourapp
  template:
    metadata:
      labels:
        app: yourapp
    spec:
      containers:
      - name: yourapp
        image: yourapp:latest
        ports:
        - containerPort: 8080
        env:
        - name: JAVA_OPTS
          value: "-Xms512m -Xmx2g"
        - name: APP_ENV
          value: "production"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "2000m"
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

### Service 配置

```yaml
apiVersion: v1
kind: Service
metadata:
  name: yourapp
spec:
  selector:
    app: yourapp
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

### Ingress 配置

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: yourapp
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  tls:
  - hosts:
    - yourapp.example.com
    secretName: yourapp-tls
  rules:
  - host: yourapp.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: yourapp
            port:
              number: 80
```

---

## CI/CD 持续集成

### GitHub Actions 示例

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn clean package -DskipTests
```

---

## 配置管理

### 环境变量

```bash
# 设置环境变量
export APP_NAME="My App"
export SERVER_PORT=8080
export DB_URL="jdbc:mysql://localhost:3306/mydb"

# 运行应用
java -jar yourapp.jar
```

### 外部配置文件

```properties
# config/application-prod.properties
app.name=My Production App
server.port=8080
server.host=0.0.0.0

db.url=jdbc:mysql://prod-db:3306/mydb
db.username=prod_user
db.password=secret

logging.level=WARN
logging.file=/var/log/yourapp/app.log
```

---

## 日志管理

### 日志轮转

使用 logrotate 管理日志文件。创建 `/etc/logrotate.d/yourapp`：

```
/var/log/yourapp/*.log {
    daily
    rotate 14
    compress
    delaycompress
    missingok
    notifempty
    create 0640 appuser appgroup
    sharedscripts
    postrotate
        systemctl reload yourapp > /dev/null 2>&1 || true
    endscript
}
```

---

## 监控和健康检查

### 健康检查端点

```java
router.get("/health", (req, res) -> {
    HealthCheckResult result = healthChecker.check();
    if (result.isHealthy()) {
        res.json(Map.of(
            "status", "healthy",
            "timestamp", System.currentTimeMillis()
        ));
    } else {
        res.status(503).json(Map.of(
            "status", "unhealthy",
            "error", result.getMessage()
        ));
    }
});
```

### 就绪检查端点

```java
router.get("/ready", (req, res) -> {
    boolean dbReady = database.isConnected();
    boolean cacheReady = cache.isAvailable();
    
    if (dbReady && cacheReady) {
        res.json(Map.of("status", "ready"));
    } else {
        res.status(503).json(Map.of("status", "not ready"));
    }
});
```

---

## 安全建议

1. **使用非 root 用户运行**

```dockerfile
RUN useradd -m -u 1000 appuser
USER appuser
```

2. **配置防火墙规则**

```bash
# 只允许必要的端口
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable
```

3. **使用 HTTPS**

```java
WebApplication app = DefaultWebApplication.create();
app.run(8443, HttpsConfig.builder()
    .keyStorePath("/path/to/keystore.jks")
    .keyStorePassword("password")
    .build());
```

4. **定期更新依赖**

```bash
# 检查依赖漏洞
mvn dependency-check:check
```

---

## 性能优化

### JVM 参数调优

```bash
java \
  -Xms512m \
  -Xmx2g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/yourapp/ \
  -jar yourapp.jar
```

### 连接池配置

```properties
db.maxConnections=50
db.minIdleConnections=10
db.connectionTimeout=30000
```

---

## 备份策略

### 数据库备份

```bash
#!/bin/bash
# backup.sh
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=/var/backups/yourapp
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u root -psecret yourapp | gzip > $BACKUP_DIR/db_$DATE.sql.gz

# 备份配置
cp /opt/yourapp/config/application-prod.properties $BACKUP_DIR/config_$DATE.properties

# 保留最近 30 天的备份
find $BACKUP_DIR -type f -mtime +30 -delete
```

添加到 crontab：

```bash
# 每天凌晨 2 点备份
0 2 * * * /opt/yourapp/scripts/backup.sh
```

---

## 回滚策略

### 蓝绿部署

使用 Docker 或 Kubernetes 实现蓝绿部署，确保零停机时间。

### 版本标签

```bash
# 使用版本标签构建
docker build -t yourapp:v1.0.0 .
docker build -t yourapp:latest .
```

---

## 云平台部署建议

### AWS

- 使用 ECS/EKS 进行容器化部署
- 使用 RDS 托管数据库
- 使用 ALB 作为负载均衡

### Azure

- 使用 AKS 进行 Kubernetes 部署
- 使用 Azure Database for MySQL
- 使用 Application Gateway

### GCP

- 使用 GKE 进行 Kubernetes 部署
- 使用 Cloud SQL
- 使用 Cloud Load Balancing

---

## 故障排除

### 常见问题

**问题：应用启动失败**

检查：
- Java 版本
- 配置文件权限
- 端口占用
- 日志文件

```bash
# 检查 Java 版本
java -version

# 检查端口占用
netstat -tlnp | grep 8080

# 查看日志
tail -f /var/log/yourapp/app.log
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06
