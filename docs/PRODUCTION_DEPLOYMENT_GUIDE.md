# 生产环境部署指南

本文档提供 EST 框架应用在生产环境中的完整部署指南和安全最佳实践。

## 目录

- [前置准备](#前置准备)
- [应用构建与打包](#应用构建与打包)
- [部署方式](#部署方式)
- [安全配置](#安全配置)
- [监控与日志](#监控与日志)
- [备份与恢复](#备份与恢复)
- [性能优化](#性能优化)
- [故障排查](#故障排查)

---

## 前置准备

### 环境要求

| 组件 | 推荐版本 | 说明 |
|------|---------|------|
| JDK | 21+ | 使用 LTS 版本 |
| 内存 | 2GB+ | 根据应用负载调整 |
| 磁盘 | 20GB+ | 包含日志和备份 |
| 操作系统 | Linux (Debian/Ubuntu/CentOS) | 推荐生产环境使用 |

### 基础设施安全基线

1. **操作系统安全**
   - 定期安装安全更新
   - 禁用不必要的服务和端口
   - 配置 SSH 密钥认证，禁用密码登录
   - 启用防火墙 (iptables/ufw/firewalld)

2. **网络安全**
   - 使用私有网络隔离应用
   - 配置网络访问控制列表 (ACL)
   - 使用 VPN 或 SSH 隧道访问管理接口

---

## 应用构建与打包

### 使用 Maven 构建

```bash
# 清理并构建项目，跳过测试
mvn clean package -DskipTests

# 构建时运行测试
mvn clean package

# 构建包含所有依赖的可执行 JAR
mvn clean package shade:shade
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

---

## 部署方式

### 方式一：使用 systemd 服务（推荐用于单机部署）

#### 1. 创建专用用户

```bash
sudo groupadd -r appgroup
sudo useradd -r -g appgroup -d /opt/est-app -s /sbin/nologin appuser
```

#### 2. 创建应用目录

```bash
sudo mkdir -p /opt/est-app/{bin,config,logs,data}
sudo chown -R appuser:appgroup /opt/est-app
sudo chmod 750 /opt/est-app
```

#### 3. 创建 systemd 服务文件

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

#### 4. 管理服务

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

### 方式二：Docker 部署

#### 1. 生产环境优化的 Dockerfile

```dockerfile
# 多阶段构建
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# 先复制 pom 文件，利用 Docker 缓存
COPY pom.xml .
COPY est-core ./est-core
COPY est-patterns ./est-patterns
COPY est-utils ./est-utils
COPY est-test ./est-test
COPY est-collection ./est-collection
COPY est-features ./est-features
COPY est-plugin ./est-plugin
COPY est-web ./est-web
COPY est-scaffold ./est-scaffold
COPY est-examples ./est-examples

# 构建应用
RUN mvn clean package -DskipTests -P production

# 运行时镜像
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="your-team@example.com"
LABEL description="EST Framework Production Application"

# 创建非 root 用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

WORKDIR /opt/app

# 从构建阶段复制应用
COPY --from=builder --chown=appuser:appgroup \
    /build/est-examples/est-examples-web/target/est-examples-web-*.jar app.jar

# 创建必要目录
RUN mkdir -p /opt/app/{logs,config,data} && \
    chown -R appuser:appgroup /opt/app && \
    chmod 750 /opt/app

USER appuser

EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

# JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

#### 2. Docker Compose 生产环境配置

创建 `docker-compose.prod.yml`：

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile.prod
    container_name: est-app-prod
    restart: unless-stopped
    user: "1001:1001"
    ports:
      - "127.0.0.1:8080:8080"
    environment:
      - APP_ENV=production
      - JAVA_OPTS=-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
    volumes:
      - ./config:/opt/app/config:ro
      - app-logs:/opt/app/logs
      - app-data:/opt/app/data
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "wget", "--quiet", "--tries=1", "--spider", "http://localhost:8080/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s
    deploy:
      resources:
        limits:
          cpus: '2.0'
          memory: 2G
        reservations:
          cpus: '0.5'
          memory: 512M
    security_opt:
      - no-new-privileges:true
    read_only: false
    tmpfs:
      - /tmp

  nginx:
    image: nginx:alpine
    container_name: est-nginx-proxy
    restart: unless-stopped
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
      - nginx-logs:/var/log/nginx
    depends_on:
      - app
    networks:
      - app-network
    security_opt:
      - no-new-privileges:true

volumes:
  app-logs:
  app-data:
  nginx-logs:

networks:
  app-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

### 方式三：Kubernetes 部署

#### 1. 生产级 Deployment 配置

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: est-app
  namespace: production
  labels:
    app: est-app
    environment: production
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: est-app
  template:
    metadata:
      labels:
        app: est-app
        environment: production
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/metrics"
    spec:
      securityContext:
        runAsNonRoot: true
        runAsUser: 1001
        fsGroup: 1001
        seccompProfile:
          type: RuntimeDefault
      containers:
      - name: est-app
        image: your-registry/est-app:v1.0.0
        imagePullPolicy: Always
        ports:
        - name: http
          containerPort: 8080
          protocol: TCP
        env:
        - name: APP_ENV
          value: "production"
        - name: JAVA_OPTS
          value: "-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: est-app-secrets
              key: db-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: est-app-secrets
              key: jwt-secret
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
          initialDelaySeconds: 60
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: config-volume
          mountPath: /opt/app/config
          readOnly: true
        - name: logs-volume
          mountPath: /opt/app/logs
        - name: tmp-volume
          mountPath: /tmp
        securityContext:
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
              - ALL
      volumes:
      - name: config-volume
        configMap:
          name: est-app-config
      - name: logs-volume
        emptyDir: {}
      - name: tmp-volume
        emptyDir: {}
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - est-app
              topologyKey: kubernetes.io/hostname
      nodeSelector:
        environment: production
      tolerations:
      - key: "dedicated"
        operator: "Equal"
        value: "app"
        effect: "NoSchedule"
```

#### 2. Secrets 配置

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: est-app-secrets
  namespace: production
type: Opaque
stringData:
  db-password: "your-strong-db-password"
  jwt-secret: "your-256-bit-jwt-secret-key"
  api-key: "your-api-key"
```

#### 3. ConfigMap 配置

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: est-app-config
  namespace: production
data:
  application.properties: |
    app.name=EST Production App
    server.port=8080
    server.host=0.0.0.0
    logging.level=WARN
    logging.file=/opt/app/logs/app.log
```

---

## 安全配置

### 1. 认证与授权

#### 使用 JWT 认证

```java
JwtSecurityConfig config = JwtSecurityConfig.builder()
    .secretKey(System.getenv("JWT_SECRET"))
    .tokenExpiration(3600)
    .refreshTokenExpiration(86400 * 7)
    .algorithm("HS256")
    .build();
```

#### 实现 RBAC 授权

```java
public class AuthorizationMiddleware implements Middleware {
    @Override
    public boolean before(Request request, Response response) {
        String path = request.getPath();
        Set<String> publicPaths = Set.of("/login", "/health", "/metrics");
        
        if (publicPaths.contains(path)) {
            return true;
        }
        
        String token = extractToken(request);
        if (token == null || !validateToken(token)) {
            response.status(401).json(Map.of("error", "Unauthorized"));
            return false;
        }
        
        return true;
    }
}
```

### 2. Web 安全头

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

### 3. HTTPS 配置

#### Nginx 反向代理配置

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

### 4. 配置管理与密钥安全

#### 使用环境变量管理敏感配置

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

### 5. 输入验证与 SQL 注入防护

```java
public class InputValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return ValidationResult.invalid("Email is required");
        }
        if (email.length() > 254) {
            return ValidationResult.invalid("Email too long");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.invalid("Invalid email format");
        }
        return ValidationResult.valid();
    }
}
```

### 6. 日志安全

```java
public class SafeLogger {
    private static final Set<String> SENSITIVE_FIELDS = 
        Set.of("password", "creditCard", "ssn", "token");
    
    public static void info(String message, Map<String, Object> data) {
        Map<String, Object> safeData = sanitize(data);
        logger.info(message, safeData);
    }
    
    private static Map<String, Object> sanitize(Map<String, Object> data) {
        return data.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> SENSITIVE_FIELDS.contains(e.getKey().toLowerCase()) 
                    ? "***REDACTED***" 
                    : e.getValue()
            ));
    }
}
```

---

## 监控与日志

### 1. 应用健康检查

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

### 2. 日志配置

使用 Logback 配置生产环境日志：

```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/opt/app/logs/app.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/opt/app/logs/app.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>30</maxHistory>
            <totalSizeCap>10GB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
        <queueSize>1024</queueSize>
        <discardingThreshold>0</discardingThreshold>
        <appender-ref ref="FILE" />
    </appender>

    <root level="WARN">
        <appender-ref ref="ASYNC_FILE" />
    </root>

    <logger name="ltd.idcu.est" level="INFO" />
    <logger name="com.yourcompany" level="DEBUG" />
</configuration>
```

### 3. 日志轮转配置 (logrotate)

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

---

## 备份与恢复

### 1. 数据库备份脚本

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

---

## 性能优化

### 1. JVM 参数调优

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

### 2. 连接池配置

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

## 故障排查

### 常见问题诊断

1. **应用启动失败**

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

2. **内存问题**

```bash
# 生成堆转储
jmap -dump:format=b,file=/tmp/heapdump.hprof <pid>

# 查看堆内存使用
jstat -gc <pid> 1s 10

# 查看线程堆栈
jstack <pid> > /tmp/threaddump.txt
```

3. **性能分析**

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
- [部署指南](./best-practices/deployment.md)
- [云部署指南](./CLOUD_DEPLOYMENT.md)

### 联系支持

如需技术支持，请联系：idcu@qq.com
