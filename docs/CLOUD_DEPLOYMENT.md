# EST 框架云部署指南

本指南介绍如何将 EST 框架应用部署到云端。

## 目录

- [构建可执行 JAR](#构建可执行-jar)
- [Docker 部署](#docker-部署)
- [Docker Compose 部署](#docker-compose-部署)
- [Kubernetes 部署](#kubernetes-部署)
- [CI/CD 持续集成](#cicd-持续集成)

## 构建可执行 JAR

首先，使用 Maven 构建项目：

```bash
mvn clean package -DskipTests
```

构建完成后，可执行 JAR 文件位于：
`est-examples/est-examples-web/target/est-examples-web-1.3.0-SNAPSHOT.jar`

## Docker 部署

Docker 相关文件位于 `deploy/docker/` 目录。

### 1. 构建 Docker 镜像

```bash
docker build -t est-web-app:latest -f deploy/docker/Dockerfile .
```

### 2. 运行 Docker 容器

```bash
docker run -d -p 8080:8080 --name est-web-app est-web-app:latest
```

### 3. 查看日志

```bash
docker logs -f est-web-app
```

### 4. 访问应用

打开浏览器访问：`http://localhost:8080`

## Docker Compose 部署

使用 Docker Compose 可以更方便地管理容器。

### 1. 启动服务

```bash
cd deploy/docker
docker-compose up -d
```

或者在项目根目录：

```bash
docker-compose -f deploy/docker/docker-compose.yml up -d
```

### 2. 查看服务状态

```bash
cd deploy/docker
docker-compose ps
```

### 3. 查看日志

```bash
cd deploy/docker
docker-compose logs -f
```

### 4. 停止服务

```bash
cd deploy/docker
docker-compose down
```

## Kubernetes 部署

Kubernetes 相关配置文件位于 `deploy/k8s/` 目录。

### 前置准备

确保已安装并配置好 Kubernetes 集群（如 minikube、kind、EKS、GKE、AKS 等）。

### 部署步骤

1. **构建并推送 Docker 镜像**

```bash
# 构建镜像
docker build -t your-registry/est-web-app:latest -f deploy/docker/Dockerfile .

# 推送镜像到仓库
docker push your-registry/est-web-app:latest
```

2. **更新 Kubernetes 配置**

编辑 `deploy/k8s/deployment.yaml`，将 `image` 字段修改为你的镜像地址。

3. **部署到 Kubernetes**

```bash
# 创建命名空间（可选）
kubectl create namespace est-app

# 部署应用
kubectl apply -f deploy/k8s/deployment.yaml
kubectl apply -f deploy/k8s/service.yaml
kubectl apply -f deploy/k8s/ingress.yaml
```

4. **查看部署状态**

```bash
# 查看 Pod 状态
kubectl get pods

# 查看 Deployment
kubectl get deployment est-web-app

# 查看 Service
kubectl get service est-web-app-service

# 查看 Ingress
kubectl get ingress
```

5. **访问应用**

根据你的 Kubernetes 环境配置，访问应用。

## CI/CD 持续集成

项目已配置 GitHub Actions 工作流，实现自动构建、推送和部署。

### 配置 Secrets

在 GitHub 仓库中配置以下 Secrets：

- `DOCKER_HUB_USERNAME`: Docker Hub 用户名
- `DOCKER_HUB_TOKEN`: Docker Hub 访问令牌
- `KUBE_CONFIG`: Kubernetes 配置文件内容

### 触发 CI/CD

推送到 `main` 或 `master` 分支会自动触发 CI/CD 流水线：

1. 构建项目
2. 构建并推送 Docker 镜像
3. 部署到 Kubernetes 集群

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

## 安全建议

1. **使用非 root 用户运行容器**
2. **配置网络策略限制 Pod 间通信**
3. **使用 Secrets 管理敏感信息**
4. **定期更新基础镜像**
5. **启用 TLS 加密**
6. **配置资源限制**
7. **设置 Pod 安全策略**

## 监控和日志

- 使用 **Prometheus** + **Grafana** 监控应用
- 使用 **ELK Stack** (Elasticsearch, Logstash, Kibana) 或 **Loki** 收集和分析日志
- 配置告警规则

## 备份和恢复

- 定期备份数据库
- 使用版本控制管理配置文件
- 配置灾难恢复计划
