# EST 框架服务网格配置

本目录包含 EST 框架的 Istio 服务网格配置，提供完整的流量管理、安全、可观测性等服务网格能力。

## 目录结构

```
deploy/servicemesh/
├── gateway.yaml              # Istio Gateway - 入口网关
├── virtualservice.yaml       # VirtualService - 流量路由
├── destinationrule.yaml      # DestinationRule - 目标规则
├── serviceentry.yaml         # ServiceEntry - 外部服务
├── peerauthentication.yaml   # PeerAuthentication - mTLS 安全
├── authorizationpolicy.yaml  # AuthorizationPolicy - 授权策略
├── sidecar.yaml              # Sidecar - 边车配置
├── kustomization.yaml        # Kustomize 配置
└── README.md                 # 本文档
```

---

## 前提要求

- Kubernetes 1.24+
- Istio 1.18+
- kubectl 1.24+
- 已部署的 EST 框架应用 (deploy/k8s/)

### 安装 Istio

```bash
# 下载 Istio
curl -L https://istio.io/downloadIstio | sh -
cd istio-*

# 安装 Istio (demo 配置用于测试, production 用于生产)
istioctl install --set profile=demo -y

# 或使用生产配置
istioctl install --set profile=default -y

# 验证安装
istioctl verify-install
```

### 为命名空间启用 Sidecar 注入

```bash
# 为 est 命名空间启用自动注入
kubectl label namespace est istio-injection=enabled

# 验证
kubectl get namespace -L istio-injection
```

---

## 快速开始

### 方法一: 使用 Kustomize (推荐)

```bash
# 部署服务网格配置
kubectl apply -k deploy/servicemesh

# 查看资源
kubectl get gateway,virtualservice,destinationrule -n est
```

### 方法二: 逐个部署

```bash
cd deploy/servicemesh

# 部署 Gateway
kubectl apply -f gateway.yaml

# 部署 VirtualService
kubectl apply -f virtualservice.yaml

# 部署 DestinationRule
kubectl apply -f destinationrule.yaml

# 部署 ServiceEntry
kubectl apply -f serviceentry.yaml

# 部署安全策略
kubectl apply -f peerauthentication.yaml
kubectl apply -f authorizationpolicy.yaml

# 部署 Sidecar 配置
kubectl apply -f sidecar.yaml
```

---

## 配置说明

### 1. Gateway (gateway.yaml)

Istio Gateway 管理网格的入口和出口流量。

**特性**
- HTTP/HTTPS 入口
- TLS 终止 (使用 cert-manager)
- HTTP → HTTPS 自动重定向
- 安全的 TLS 配置 (TLS 1.2/1.3)

**访问应用:**
```bash
# 获取 Ingress Gateway IP
export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')

# 访问应用
curl -H "Host: est-app.example.com" http://$INGRESS_HOST:$INGRESS_PORT
```

### 2. VirtualService (virtualservice.yaml)

VirtualService 配置流量路由规则。

**特性**
- 权重分发 (90% v1, 10% v2)
- 请求超时设置
- 自动重试
- CORS 策略
- 安全头设置
- 故障注入 (用于测试)
- 内部服务路由分离

**权重分发调整:**
```bash
# 编辑 virtualservice.yaml 调整权重
# v1: 90%, v2: 10% → v1: 50%, v2: 50%
kubectl apply -f virtualservice.yaml
```

### 3. DestinationRule (destinationrule.yaml)

DestinationRule 配置目标服务的策略。

**特性**
- 负载均衡策略 (LEAST_CONN)
- 连接池配置
- 异常检测 (熔断)
- 双向 TLS (mTLS)
- 版本子集 (v1, v2)

**异常检测:**
- 连续 5 次 5xx 错误
- 30s 检测间隔
- 30s 基准熔断时间
- 最大 50% 熔断比例

### 4. ServiceEntry (serviceentry.yaml)

ServiceEntry 配置外部服务访问。

**已配置的服务:**
- PostgreSQL (内部服务)
- Redis (内部服务)
- 外部 API (api.example.com)
- 监控服务 (Prometheus, Grafana, Jaeger)

**添加新的外部服务:**
```yaml
apiVersion: networking.istio.io/v1alpha3
kind: ServiceEntry
metadata:
  name: my-external-service
spec:
  hosts:
  - myservice.com
  ports:
  - number: 443
    name: https
    protocol: HTTPS
  resolution: DNS
  location: MESH_EXTERNAL
```

### 5. PeerAuthentication (peerauthentication.yaml)

PeerAuthentication 配置 mTLS 策略。

**策略:**
- 命名空间级别: STRICT mTLS
- 健康检查端点: PERMISSIVE (兼容 kubelet)

**mTLS 模式:**
- `STRICT`: 只接受 mTLS 流量
- `PERMISSIVE`: 接受明文和 mTLS 流量
- `DISABLE`: 只接受明文流量

### 6. AuthorizationPolicy (authorizationpolicy.yaml)

AuthorizationPolicy 配置服务访问权限。

**策略说明:**
1. `est-allow-health-checks`: 允许所有人访问 /health 和 /metrics
2. `est-allow-api-from-gateway`: 允许从 Ingress Gateway 访问 /api/*
3. `est-deny-all-by-default`: 默认拒绝所有其他请求

**添加自定义权限:**
```yaml
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: allow-specific-service
spec:
  selector:
    matchLabels:
      app: est-web-app
  action: ALLOW
  rules:
  - from:
    - source:
        principals: ["cluster.local/ns/est/sa/other-service-sa"]
    to:
    - operation:
        methods: ["POST"]
        paths: ["/api/v1/*"]
```

### 7. Sidecar (sidecar.yaml)

Sidecar 配置边车代理的行为。

**特性**
- 出口流量限制 (REGISTRY_ONLY)
- 减少边车内存占用
- 指定允许访问的服务

**网络策略:**
- `REGISTRY_ONLY`: 只允许访问已注册的服务
- `ALLOW_ANY`: 允许访问任何服务 (默认)

---

## 流量管理

### 1. 金丝雀发布 (Canary Release)

```bash
# 编辑 virtualservice.yaml 调整流量权重
# v1: 70%, v2: 30%
kubectl apply -f deploy/servicemesh/virtualservice.yaml

# 逐步增加 v2 流量
# v1: 50%, v2: 50%
# v1: 30%, v2: 70%
# v1: 0%, v2: 100%
```

### 2. 流量镜像 (Traffic Mirroring)

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: est-web-app-vs
spec:
  hosts:
  - "est-app.example.com"
  http:
  - route:
    - destination:
        host: est-web-app-service
        subset: v1
      weight: 100
    mirror:
      host: est-web-app-service
      subset: v2
    mirrorPercentage:
      value: 100.0
```

### 3. 请求超时

```yaml
http:
- timeout: 5s
  route:
  - destination:
      host: est-web-app-service
```

### 4. 重试策略

```yaml
retries:
  attempts: 3
  perTryTimeout: 2s
  retryOn: "connect-failure,refused-stream"
```

### 5. 熔断 (Circuit Breaking)

在 destinationrule.yaml 中已配置:
- 最大 100 个 TCP 连接
- 最大 1024 个 HTTP 请求
- 连续 5 次 5xx 错误触发熔断

---

## 安全

### 1. 双向 TLS (mTLS)

```bash
# 验证 mTLS 状态
istioctl pc security <pod-name> -n est

# 检查 PeerAuthentication
kubectl get peerauthentication -n est
```

### 2. 授权策略

```bash
# 查看授权策略
kubectl get authorizationpolicy -n est

# 测试授权 (应该被拒绝)
kubectl exec -it <other-pod> -n other-ns -- curl est-web-app-service.est.svc.cluster.local/api/v1/data
```

### 3. JWT 认证

添加 RequestAuthentication:
```yaml
apiVersion: security.istio.io/v1beta1
kind: RequestAuthentication
metadata:
  name: est-jwt-auth
  namespace: est
spec:
  selector:
    matchLabels:
      app: est-web-app
  jwtRules:
  - issuer: "https://est-app.example.com"
    jwksUri: "https://est-app.example.com/.well-known/jwks.json"
```

---

## 可观测性

### 1. 监控 (Prometheus + Grafana)

```bash
# 安装 Prometheus 和 Grafana (如果未安装)
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/prometheus.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/grafana.yaml

# 访问 Grafana
kubectl port-forward -n istio-system svc/grafana 3000:3000
# 打开浏览器: http://localhost:3000
```

### 2. 分布式追踪 (Jaeger)

```bash
# 安装 Jaeger
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/jaeger.yaml

# 访问 Jaeger
kubectl port-forward -n istio-system svc/jaeger-query 16686:16686
# 打开浏览器: http://localhost:16686
```

### 3. 访问日志 (Kiali)

```bash
# 安装 Kiali
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/kiali.yaml

# 访问 Kiali
kubectl port-forward -n istio-system svc/kiali 20001:20001
# 打开浏览器: http://localhost:20001
```

### 4. 查看流量指标

```bash
# 查看服务指标
kubectl exec -it <pod-name> -c istio-proxy -n est -- pilot-agent request GET /stats/prometheus

# 或使用 istioctl
istioctl proxy-config stats <pod-name> -n est
```

---

## 故障注入

### 1. 延迟注入

已在 virtualservice.yaml 中配置 0.1% 流量延迟 5s。

### 2. 错误注入

```yaml
fault:
  abort:
    percentage:
      value: 0.1
    httpStatus: 500
```

---

## 常用命令

### 部署和验证

```bash
# 部署所有配置
kubectl apply -k deploy/servicemesh

# 检查资源状态
kubectl get gateway,virtualservice,destinationrule,authorizationpolicy -n est

# 查看配置详情
kubectl describe virtualservice est-web-app-vs -n est
```

### 流量管理

```bash
# 查看路由规则
istioctl pc routes <pod-name> -n est -o json

# 查看端点
istioctl pc endpoints <pod-name> -n est

# 查看集群信息
istioctl proxy-status
```

### 排查

```bash
# 查看访问日志
kubectl logs <pod-name> -c istio-proxy -n est

# 进入边车代理
kubectl exec -it <pod-name> -c istio-proxy -n est -- bash

# 验证配置
istioctl analyze -n est
```

### 清理

```bash
# 删除服务网格配置
kubectl delete -k deploy/servicemesh

# 禁用 Sidecar 注入
kubectl label namespace est istio-injection-

# 重启 Pod 以移除 Sidecar
kubectl rollout restart deployment est-web-app -n est
```

---

## 性能优化

### 1. 限制 Sidecar 资源

更新 deployment.yaml 添加资源限制:
```yaml
template:
  metadata:
    annotations:
      sidecar.istio.io/proxyCPU: "100m"
      sidecar.istio.io/proxyMemory: "128Mi"
      sidecar.istio.io/proxyCPULimit: "500m"
      sidecar.istio.io/proxyMemoryLimit: "512Mi"
```

### 2. 使用 Sidecar 资源限制

已在 sidecar.yaml 中配置 `REGISTRY_ONLY` 模式，减少内存使用。

### 3. 禁用不需要的统计

```yaml
apiVersion: meshconfig/v1alpha1
kind: MeshConfig
spec:
  defaultConfig:
    proxyStatsMatcher:
      inclusionPrefixes:
      - "http."
      - "listener."
```

---

## 生产环境检查清单

- [ ] Istio 生产配置 (profile=default)
- [ ] mTLS 强制模式 (STRICT)
- [ ] 授权策略 (默认拒绝)
- [ ] Sidecar 资源限制
- [ ] 监控和告警 (Prometheus + Grafana)
- [ ] 分布式追踪 (Jaeger)
- [ ] 访问日志收集
- [ ] 熔断配置
- [ ] 重试策略
- [ ] 超时设置
- [ ] 健康检查配置
- [ ] 证书管理 (cert-manager)
- [ ] 定期 Istio 版本更新
- [ ] 故障恢复演练

---

## 参考资源

- [Istio 官方文档](https://istio.io/latest/docs/)
- [Istio 最佳实践](https://istio.io/latest/docs/ops/best-practices/)
- [EST 框架 Kubernetes 部署](../k8s/README.md)
- [Kubernetes 官方文档](https://kubernetes.io/docs/)