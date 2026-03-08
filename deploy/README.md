# EST 框架部署配置

本目录包含 EST 框架的部署相关配置文件，支持 Docker、Docker Compose 和 Kubernetes 部署方式。

## 目录结构

```
deploy/
├── docker/              # Docker 相关配置
│   ├── Dockerfile       # Docker 镜像构建文件
│   ├── docker-compose.yml  # Docker Compose 配置
│   └── .dockerignore    # Docker 构建忽略文件
├── k8s/                 # Kubernetes 相关配置
│   ├── namespace.yaml         # 命名空间配置
│   ├── serviceaccount.yaml    # 服务账号配置
│   ├── rbac.yaml              # 权限控制配置
│   ├── configmap.yaml         # 应用配置管理
│   ├── secret.yaml            # 敏感信息管理
│   ├── service.yaml           # 服务配置
│   ├── deployment.yaml        # 部署配置
│   ├── hpa.yaml               # 水平自动伸缩配置
│   ├── pdb.yaml               # Pod 中断预算
│   ├── ingress.yaml           # 入口配置
│   └── kustomization.yaml     # Kustomize 配置
├── servicemesh/         # Service Mesh (Istio) 配置
├── scripts/             # 部署脚本
│   ├── build-docker.bat      # Windows Docker 构建脚本
│   ├── build-docker.sh        # Linux/Mac Docker 构建脚本
│   ├── deploy-k8s.bat        # Windows Kubernetes 部署脚本
│   ├── deploy-k8s.sh          # Linux/Mac Kubernetes 部署脚本
│   ├── undeploy-k8s.bat      # Windows Kubernetes 卸载脚本
│   └── undeploy-k8s.sh        # Linux/Mac Kubernetes 卸载脚本
└── README.md            # 本文档
```

---

## 快速开始

### Docker 部署

#### 前提要求

- Docker 20.10+
- Docker Compose 2.0+ (可选)

#### 方法一：使用脚本构建（推荐）
**Windows:**
```bash
cd deploy/scripts
build-docker.bat 2.1.0
```

**Linux/Mac:**
```bash
cd deploy/scripts
chmod +x build-docker.sh
./build-docker.sh 2.1.0
```

#### 方法二：手动构建

```bash
cd deploy/docker
docker build -t est-demo:2.1.0 -f Dockerfile ../..
```

#### 运行容器

```bash
docker run -d \
  --name est-demo \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xms512m -Xmx2g" \
  -e APP_ENV=production \
  est-demo:2.1.0
```

#### 使用 Docker Compose

```bash
cd deploy/docker
docker-compose up -d
```

### 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| JAVA_OPTS | -Xms512m -Xmx2g -XX:+UseG1GC | JVM 参数 |
| APP_ENV | production | 应用环境 |
| APP_NAME | est-demo | 应用名称 |
| APP_VERSION | 2.1.0 | 应用版本 |
| LOG_LEVEL | INFO | 日志级别 |

---

## Kubernetes 部署

### 前提要求

- Kubernetes 1.24+
- kubectl 1.24+
- 可选：nginx-ingress-controller
- 可选：cert-manager (用于 TLS)
- 可选：prometheus (用于监控)

### 快速开始

#### 方法一：使用脚本部署（推荐）
**Windows:**
```bash
cd deploy/scripts
deploy-k8s.bat
```

**Linux/Mac:**
```bash
cd deploy/scripts
chmod +x deploy-k8s.sh
./deploy-k8s.sh
```

#### 方法二：使用 Kustomize 部署

```bash
cd deploy/k8s

# 预览配置
kubectl kustomize .

# 应用配置
kubectl apply -k .
```

#### 方法三：使用 kubectl 直接应用

```bash
cd deploy/k8s

# 1. 创建命名空间
kubectl apply -f namespace.yaml

# 2. 创建服务账号和权限
kubectl apply -f serviceaccount.yaml
kubectl apply -f rbac.yaml

# 3. 创建配置和密钥
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml

# 4. 创建服务
kubectl apply -f service.yaml

# 5. 创建部署
kubectl apply -f deployment.yaml

# 6. 创建自动伸缩配置
kubectl apply -f hpa.yaml

# 7. 创建 Pod 中断预算
kubectl apply -f pdb.yaml

# 8. 创建入口 (可选)
kubectl apply -f ingress.yaml
```

### 配置说明

#### 1. namespace.yaml
创建独立的 `est` 命名空间，用于隔离 EST 框架应用。

#### 2. serviceaccount.yaml & rbac.yaml
为应用创建专用的服务账号，并配置最小权限原则的 RBAC 规则。

#### 3. configmap.yaml
管理应用配置，包括：
- 应用基本信息
- 服务器配置
- 日志配置
- 缓存配置
- 监控配置
- 健康检查配置

#### 4. secret.yaml
管理敏感信息，包括：
- 数据库连接信息
- JWT 密钥
- API 密钥
- 加密密钥

🔒 **重要**: 生产环境务必修改默认密码和密钥！

#### 5. service.yaml
配置 ClusterIP 类型的服务，暴露 HTTP 和 Metrics 端口。

#### 6. deployment.yaml
核心部署配置，特性包括：
- 滚动更新策略 (maxSurge=1, maxUnavailable=0)
- 健康检查 (liveness/readiness/startup probes)
- 资源限制和请求
- 安全上下文 (避免 root 用户运行)
- Pod 亲和性和反亲和性 (高可用)
- 拓扑和节点选择器
- 配置挂载

#### 7. hpa.yaml
水平自动伸缩配置：
- 最小副本数: 2
- 最大副本数: 10
- 伸缩指标: CPU 70%, 内存 80%
- 智能伸缩行为配置

#### 8. pdb.yaml
Pod 中断预算，确保至少 1 个 Pod 可用。

#### 9. ingress.yaml
Ingress 配置，特性包括：
- TLS 支持 (使用 cert-manager)
- 安全请求配置
- 请求大小限制
- 超时配置

### 验证部署

```bash
# 检查 Pod 状态
kubectl get pods -n est

# 查看 Pod 日志
kubectl logs -f deployment/est-demo -n est

# 检查服务
kubectl get svc -n est

# 检查 HPA
kubectl get hpa -n est

# 检查 Ingress
kubectl get ingress -n est

# 端口转发访问应用
kubectl port-forward svc/est-demo-service 8080:80 -n est
```

访问 http://localhost:8080 或 http://localhost:8080/health 验证应用是否正常运行。

### 伸缩

#### 手动伸缩
```bash
kubectl scale deployment est-demo --replicas=5 -n est
```

#### 查看 HPA 状态
```bash
kubectl describe hpa est-demo-hpa -n est
```

### 更新部署

#### 更新镜像
```bash
kubectl set image deployment/est-demo est-demo=est-demo:2.1.0 -n est
```

#### 更新配置
```bash
# 修改 configmap.yaml
kubectl apply -f configmap.yaml

# 触发滚动更新
kubectl rollout restart deployment/est-demo -n est
```

#### 查看更新状态
```bash
kubectl rollout status deployment/est-demo -n est
kubectl rollout history deployment/est-demo -n est
```

#### 回滚
```bash
kubectl rollout undo deployment/est-demo -n est
```

### 卸载部署

#### 使用脚本卸载（推荐）

**Windows:**
```bash
cd deploy/scripts
undeploy-k8s.bat
```

**Linux/Mac:**
```bash
cd deploy/scripts
chmod +x undeploy-k8s.sh
./undeploy-k8s.sh
```

#### 手动卸载
```bash
# 使用 Kustomize 删除
kubectl delete -k deploy/k8s

# 或逐个删除
kubectl delete -f deploy/k8s/
```

---

## 监控与可观测性

### Prometheus 集成

部署已配置 Prometheus 发现：

```yaml
annotations:
  prometheus.io/scrape: "true"
  prometheus.io/port: "8080"
  prometheus.io/path: "/metrics"
```

### 健康检查端点
- `/health` - 应用健康状态
- `/metrics` - Prometheus 指标

---

## 安全建议

### 1. 修改默认密码
```bash
# 生成强密码
kubectl create secret generic est-demo-secret \
  --from-literal=db.password=$(openssl rand -base64 32) \
  --from-literal=jwt.secret=$(openssl rand -base64 64) \
  -n est \
  --dry-run=client -o yaml | kubectl apply -f -
```

### 2. 网络策略
建议创建 NetworkPolicy 限制 Pod 间通信。

### 3. 镜像安全
- 使用私有镜像仓库
- 扫描镜像漏洞
- 使用最小基础镜像

### 4. 资源限制
确保配置了合理的 resources.requests 和 resources.limits。

---

## 故障排查

### Pod 无法启动
```bash
kubectl describe pod <pod-name> -n est
kubectl logs <pod-name> -n est
```

### 健康检查失败
```bash
kubectl exec -it <pod-name> -n est -- curl http://localhost:8080/health
```

### 服务无法访问
```bash
kubectl get endpoints est-demo-service -n est
kubectl port-forward svc/est-demo-service 8080:80 -n est
```

---

## 生产环境检查清单
- [ ] 修改所有默认密码和密钥
- [ ] 配置 TLS (cert-manager)
- [ ] 配置资源限制
- [ ] 配置 Pod 安全策略
- [ ] 配置网络策略
- [ ] 配置灾难恢复
- [ ] 配置监控告警
- [ ] 配置日志集成
- [ ] 测试伸缩
- [ ] 测试回滚流程
- [ ] 故障恢复演练

---

## 参考资源
- [Kubernetes 官方文档](https://kubernetes.io/docs/)
- [EST 框架文档](../docs/README.md)
- [Docker 官方文档](https://docs.docker.com/)
- [Istio 官方文档](https://istio.io/docs/)

---

## 版本信息

- **EST 框架版本**: 2.1.0
- **文档版本**: 2.1.0
- **最后更新**: 2026-03-08