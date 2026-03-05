# 部署指南

本文档提供 EST 框架应用的完整部署指南，包括本地运行、Docker 部署、Kubernetes 部署以及生产环境最佳实践。

---

## 目录

- [项目构建](#项目构建)
- [本地运行](#本地运行)
- [systemd 服务部署（单机推荐）](#systemd-服务部署单机推荐)
- [Docker 部署](#docker-部署)
- [Docker Compose 部署](#docker-compose-部署)
- [Kubernetes 部署](#kubernetes-部署)
- [CI/CD 持续集成](#cicd-持续集成)
- [生产环境最佳实践](#生产环境最佳实践)
- [云平台部署建议](#云平台部署建议)
- [故障排查](#故障排查)

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

# 运行指定模块的测试
mvn test -pl est-web

# 运行指定测试类
mvn test -Dtest=DefaultRouterTest
```

### 配置生产环境专用构建

在 `pom.xml` 中添加生产环境 profile：

```xml
<profiles>
  <profile>
    <id>production</id>
    <properties>
      <maven.test.skip>false</maven.test.skip>
      <checkstyle.skip>false</checkstyle.skip>
    </properties>
    <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-enforcer-plugin</artifactId>
          <executions>
            <execution>
              <id>enforce-versions</id>
              <goals>
                <goal>enforce</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>
```

使用生产环境 profile 构建：

```bash
mvn clean package -P production
```

### 构建产物

构建完成后，各模块的 JAR 文件位于各自的 `target` 目录中：

```
est1.3/
├── est-core/
│   ├── est-core-api/target/est-core-api-1.3.0-SNAPSHOT.jar
│   └── est-core-impl/target/est-core-impl-1.3.0-SNAPSHOT.jar
├── est-web/
│   ├── est-web-api/target/est-web-api-1.3.0-SNAPSHOT.jar
│   └── est-web-impl/target/est-web-impl-1.3.0-SNAPSHOT.jar
└── est-examples/
    └── est-examples-web/target/est-examples-web-1.3.0-SNAPSHOT.jar
```

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

```bash
# 先构建项目
mvn clean package -DskipTests

# 运行示例应用
java -jar est-examples/est-examples-web/target/est-examples-web-1.3.0-SNAPSHOT.jar
```

### 方式三：在 IDE 中运行

1. 在 IDEA 中打开项目
2. 找到示例类：`est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java`
3. 右键点击类名，选择 `Run 'BasicWebAppExample.main()'`

### 验证应用运行

应用启动后，打开浏览器访问以下地址验证：

- 首页：http://localhost:8080
- API 示例：http://localhost:8080/api/greeting

### JVM 参数调优

```bash
# 启动时设置 JVM 参数
java -Xms512m -Xmx2g -XX:+UseG1GC -jar app.jar
```

常用 JVM 参数说明：

| 参数 | 说明 |
|------|------|
| `-Xms512m` | 初始堆内存 512MB |
| `-Xmx2g` | 最大堆内存 2GB |
| `-XX:+UseG1GC` | 使用 G1 垃圾收集器 |
| `-XX:MaxMetaspaceSize=256m` | 最大元空间大小 |
| `-XX:+HeapDumpOnOutOfMemoryError` | OOM 时生成堆转储 |

---

## systemd 服务部署（单机推荐）

### 1. 创建专用用户

```bash
sudo groupadd -r appgroup
sudo useradd -r -g appgroup -d /opt/est-app -s /sbin/nologin appuser
```

### 2. 创建应用目录

```bash
sudo mkdir -p /opt/est-app/{bin,config,logs,data}
sudo chown -R appuser:appgroup /opt/est-app
sudo chmod 750 /opt/est-app
```

### 3. 创建 systemd 服务文件

创建 `/etc/systemd/system/est-app.service`：

```ini
[Unit]
Description=EST Framework Application
After=network.target syslog.target
Wants=network.target

[Service]
Type=simple
User=appuser
Group=appgroup

WorkingDirectory=/opt/est-app

ExecStart=/usr/bin/java \
  -Xms512m \
  -Xmx2g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/opt/est-app/logs/ \
  -Djava.awt.headless=true \
  -Djava.security.egd=file:/dev/./urandom \
  -jar /opt/est-app/bin/app.jar

Restart=always
RestartSec=10
StartLimitInterval=60
StartLimitBurst=3

StandardOutput=journal
StandardError=journal
SyslogIdentifier=est-app

# 安全加固
NoNewPrivileges=true
PrivateTmp=true
ProtectSystem=strict
ProtectHome=true
ReadWritePaths=/opt/est-app/logs /opt/est-app/data

# 环境变量
Environment="APP_ENV=production"
Environment="JAVA_OPTS=-Xms512m -Xmx2g"

[Install]
WantedBy=multi-user.target
```

### 4. 管理服务

```bash
# 重新加载 systemd 配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start est-app

# 设置开机自启
sudo systemctl enable est-app

# 查看状态
sudo systemctl status est-app

# 查看日志
sudo journalctl -u est-app -f -n 100

# 重启服务
sudo systemctl restart est-app

# 停止服务
sudo systemctl stop est-app
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

### 构建 Docker 镜像

项目已提供 Dockerfile，位于 `deploy/docker/Dockerfile`。

```bash
# 在项目根目录执行
docker build -t est-web-app:latest -f deploy/docker/Dockerfile .
```

### 运行 Docker 容器

```bash
# 运行容器
docker run -d \
  --name est-web-app \
  -p 8080:8080 \
  --restart unless-stopped \
  est-web-app:latest
```

### Docker 常用命令

```bash
# 查看容器状态
docker ps

# 查看容器日志
docker logs -f est-web-app

# 进入容器
docker exec -it est-web-app /bin/bash

# 停止容器
docker stop est-web-app

# 启动容器
docker start est-web-app

# 删除容器
docker rm -f est-web-app

# 删除镜像
docker rmi est-web-app:latest
```

### 访问应用

打开浏览器访问：http://localhost:8080

---

## Docker Compose 部署

### 配置说明

项目已提供 docker-compose.yml，位于 `deploy/docker/docker-compose.yml`。

### 启动服务

```bash
# 方式一：在 deploy/docker 目录下执行
cd deploy/docker
docker-compose up -d

# 方式二：在项目根目录执行
docker-compose -f deploy/docker/docker-compose.yml up -d
```

### Docker Compose 常用命令

```bash
# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f

# 查看指定服务日志
docker-compose logs -f app

# 停止服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v

# 重新构建并启动
docker-compose up -d --build
```

---

## Kubernetes 部署

### 环境准备

#### 安装 kubectl

参考官方文档：https://kubernetes.io/docs/tasks/tools/

#### 准备 Kubernetes 集群

可以选择以下任一方式：

- **本地开发**：minikube、kind、k3s
- **云平台**：AWS EKS、Azure AKS、Google GKE、阿里云 ACK

### 构建并推送镜像

```bash
# 构建镜像（替换为你的镜像仓库地址）
docker build -t your-registry/est-web-app:latest -f deploy/docker/Dockerfile .

# 登录镜像仓库
docker login your-registry

# 推送镜像
docker push your-registry/est-web-app:latest
```

### 更新 Kubernetes 配置

编辑 `deploy/k8s/deployment.yaml`，修改镜像地址：

```yaml
spec:
  containers:
  - name: est-web-app
    image: your-registry/est-web-app:latest  # 替换为你的镜像地址
```

### 部署应用

```bash
# 创建命名空间（可选）
kubectl create namespace est-app

# 部署应用
kubectl apply -f deploy/k8s/deployment.yaml
kubectl apply -f deploy/k8s/service.yaml
kubectl apply -f deploy/k8s/ingress.yaml
```

### 验证部署

```bash
# 查看 Pod 状态
kubectl get pods -n est-app

# 查看 Deployment
kubectl get deployment est-web-app -n est-app

# 查看 Service
kubectl get service est-web-app-service -n est-app

# 查看 Ingress
kubectl get ingress -n est-app

# 查看 Pod 日志
kubectl logs -f deployment/est-web-app -n est-app
```

### 访问应用

根据你的 Kubernetes 环境配置访问应用。

---

## CI/CD 持续集成

### GitHub Actions 配置

项目已配置 GitHub Actions 工作流，位于 `.github/workflows/ci-cd.yml`。

### 配置 Secrets

在 GitHub 仓库的 `Settings` → `Secrets and variables` → `Actions` 中添加以下 Secrets：

| Secret 名称 | 说明 |
|-------------|------|
| `DOCKER_HUB_USERNAME` | Docker Hub 用户名 |
| `DOCKER_HUB_TOKEN` | Docker Hub 访问令牌 |
| `KUBE_CONFIG` | Kubernetes 配置文件内容（base64 编码） |

### 触发 CI/CD

推送到 `main` 或 `master` 分支会自动触发 CI/CD 流水线，执行以下步骤：

1. 检出代码
2. 设置 JDK 21
3. 缓存 Maven 依赖
4. 构建项目
5. 构建并推送 Docker 镜像
6. 部署到 Kubernetes 集群

---

## 生产环境最佳实践

### 安全配置

#### 1. 使用非 root 用户运行应用

确保应用以非 root 用户运行，如前面的 systemd 和 Docker 配置所示。

#### 2. HTTPS 配置

使用 Nginx 反向代理配置 HTTPS：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/nginx/ssl/fullchain.pem;
    ssl_certificate_key /etc/nginx/ssl/privkey.pem;
    ssl_dhparam /etc/nginx/ssl/dhparam.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384;
    ssl_prefer_server_ciphers off;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 1d;
    ssl_session_tickets off;

    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains; preload" always;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
        
        client_max_body_size 10M;
    }

    location ~ /\.(?!well-known).* {
        deny all;
        access_log off;
        log_not_found off;
    }
}
```

#### 3. Web 安全头

配置安全中间件添加安全响应头：

```java
app.use((req, res) -> {
    res.header("X-Content-Type-Options", "nosniff");
    res.header("X-Frame-Options", "DENY");
    res.header("X-XSS-Protection", "1; mode=block");
    res.header("Content-Security-Policy", 
        "default-src 'self'; " +
        "script-src 'self' 'unsafe-inline'; " +
        "style-src 'self' 'unsafe-inline'; " +
        "img-src 'self' data:; " +
        "font-src 'self'; " +
        "connect-src 'self'; " +
        "object-src 'none'; " +
        "frame-ancestors 'none'; " +
        "form-action 'self'; " +
        "base-uri 'self'");
    res.header("Strict-Transport-Security", 
        "max-age=31536000; includeSubDomains; preload");
    res.header("Referrer-Policy", "strict-origin-when-cross-origin");
    res.header("Permissions-Policy", 
        "geolocation=(), microphone=(), camera=()");
    return true;
});
```

#### 4. 使用环境变量管理敏感配置

```java
public class AppConfig {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String jwtSecret;
    
    public AppConfig() {
        this.dbUrl = System.getenv("DB_URL");
        this.dbUsername = System.getenv("DB_USERNAME");
        this.dbPassword = System.getenv("DB_PASSWORD");
        this.jwtSecret = System.getenv("JWT_SECRET");
        
        validateConfig();
    }
    
    private void validateConfig() {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new SecurityException("JWT_SECRET must be at least 32 characters");
        }
    }
}
```

### 监控与日志

#### 应用健康检查

```java
router.get("/health", (req, res) -> {
    HealthCheckResult result = healthChecker.checkAll();
    int status = result.isHealthy() ? 200 : 503;
    res.status(status).json(Map.of(
        "status", result.isHealthy() ? "healthy" : "unhealthy",
        "checks", result.getChecks(),
        "timestamp", Instant.now().toString()
    ));
});

router.get("/ready", (req, res) -> {
    boolean dbReady = database.isConnected();
    boolean cacheReady = cache.isAvailable();
    boolean ready = dbReady && cacheReady;
    
    res.status(ready ? 200 : 503).json(Map.of(
        "status", ready ? "ready" : "not_ready",
        "database", dbReady ? "connected" : "disconnected",
        "cache", cacheReady ? "available" : "unavailable"
    ));
});

router.get("/metrics", (req, res) -> {
    res.json(Map.of(
        "jvm", Map.of(
            "memory_used", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
            "memory_max", Runtime.getRuntime().maxMemory(),
            "thread_count", Thread.activeCount()
        ),
        "application", Map.of(
            "uptime_seconds", managementService.getUptime() / 1000,
            "request_count", metrics.getRequestCount(),
            "error_count", metrics.getErrorCount()
        )
    ));
});
```

#### 日志配置

创建 `/etc/logrotate.d/est-app`：

```
/opt/app/logs/*.log {
    daily
    rotate 30
    compress
    delaycompress
    missingok
    notifempty
    create 0640 appuser appgroup
    sharedscripts
    postrotate
        /usr/bin/systemctl reload est-app > /dev/null 2>&1 || true
    endscript
}
```

### 备份与恢复

创建数据库备份脚本 `backup.sh`：

```bash
#!/bin/bash
# backup.sh - 生产环境备份脚本

set -euo pipefail

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/var/backups/est-app"
LOG_FILE="/var/log/est-app-backup.log"
RETENTION_DAYS=30

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
}

mkdir -p "$BACKUP_DIR"

log "Starting backup"

# 备份数据库
if [ -n "${DB_PASSWORD:-}" ]; then
    log "Backing up database"
    mysqldump -u "$DB_USER" -p"$DB_PASSWORD" --single-transaction --routines --triggers "$DB_NAME" | \
        gzip > "$BACKUP_DIR/db_$DATE.sql.gz"
fi

# 备份配置文件
log "Backing up configuration"
tar -czf "$BACKUP_DIR/config_$DATE.tar.gz" -C /opt/app config/

# 备份静态数据 (如果有)
if [ -d "/opt/app/data" ]; then
    log "Backing up data"
    tar -czf "$BACKUP_DIR/data_$DATE.tar.gz" -C /opt/app data/
fi

# 清理旧备份
log "Cleaning up old backups"
find "$BACKUP_DIR" -type f -mtime +$RETENTION_DAYS -delete

log "Backup completed successfully"
```

添加到 crontab：

```bash
# 每天凌晨 2 点备份
0 2 * * * /opt/est-app/scripts/backup.sh
```

### 性能优化

#### JVM 参数调优

```bash
JAVA_OPTS="
  -Xms1g
  -Xmx4g
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -XX:+UseStringDeduplication
  -XX:+OptimizeStringConcat
  -XX:+HeapDumpOnOutOfMemoryError
  -XX:HeapDumpPath=/opt/app/logs/
  -XX:+DisableExplicitGC
  -XX:+UseNUMA
  -XX:+UseBiasedLocking
  -Djava.awt.headless=true
  -Djava.security.egd=file:/dev/./urandom
  -Dfile.encoding=UTF-8
"
```

#### 连接池配置

```properties
db.pool.maxTotal=50
db.pool.maxIdle=20
db.pool.minIdle=10
db.pool.maxWaitMillis=30000
db.pool.testOnBorrow=true
db.pool.testWhileIdle=true
db.pool.timeBetweenEvictionRunsMillis=60000
db.pool.minEvictableIdleTimeMillis=300000
```

---

## 云平台部署建议

### AWS

- 使用 **ECS** 或 **EKS** 部署容器化应用
- 使用 **ECR** 存储 Docker 镜像
- 使用 **ELB** 或 **ALB** 作为负载均衡
- 使用 **RDS** 托管数据库

### Azure

- 使用 **ACI** 或 **AKS** 部署容器化应用
- 使用 **ACR** 存储 Docker 镜像
- 使用 **Application Gateway** 作为负载均衡
- 使用 **Azure Database** 托管数据库

### Google Cloud

- 使用 **GKE** 部署容器化应用
- 使用 **GCR** 或 **Artifact Registry** 存储 Docker 镜像
- 使用 **Cloud Load Balancing** 作为负载均衡
- 使用 **Cloud SQL** 托管数据库

---

## 故障排查

### 应用启动失败

```bash
# 查看应用日志
sudo journalctl -u est-app -n 100

# 检查端口占用
sudo netstat -tlnp | grep 8080

# 检查文件权限
ls -la /opt/est-app/

# 检查 Java 版本
java -version
```

### 内存问题

```bash
# 生成堆转储
jmap -dump:format=b,file=/tmp/heapdump.hprof <pid>

# 查看堆内存使用
jstat -gc <pid> 1s 10

# 查看线程堆栈
jstack <pid> > /tmp/threaddump.txt
```

### 性能分析

```bash
# 使用 JProfiler 或 Arthas 进行在线诊断
java -jar arthas-boot.jar

# 查看 GC 日志
tail -f /opt/app/logs/gc.log
```

---

## 安全检查清单

部署前请确认以下检查项：

- [ ] 使用非 root 用户运行应用
- [ ] 配置 HTTPS 并强制重定向
- [ ] 设置安全响应头
- [ ] 敏感信息使用环境变量或密钥管理服务
- [ ] 配置防火墙规则，仅开放必要端口
- [ ] 定期更新依赖和操作系统
- [ ] 配置日志轮转和监控告警
- [ ] 设置自动备份策略
- [ ] 实现健康检查和就绪检查
- [ ] 配置资源限制
- [ ] 禁用不必要的功能和端点
- [ ] 实施速率限制防止暴力攻击
- [ ] 记录审计日志
- [ ] 配置灾难恢复计划

---

## 附录

### 参考资源

- [EST 框架文档](./README.md)
- [安全性最佳实践](./best-practices/security.md)

### 联系支持

如需技术支持，请提交 Issue：https://github.com/idcu/est/issues

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06
