# 部署配置目录

本目录包含 EST 框架的部署相关配置文件。

## 目录结构

```
deploy/
├── docker/          # Docker 相关配置
│   ├── Dockerfile
│   └── docker-compose.yml
├── k8s/             # Kubernetes 相关配置
│   ├── deployment.yaml
│   ├── service.yaml
│   └── ingress.yaml
└── README.md        # 本文件
```

## 使用说明

### Docker 部署

详见 [docs/CLOUD_DEPLOYMENT.md](../docs/CLOUD_DEPLOYMENT.md) 中的 Docker 部署章节。

### Docker Compose 部署

详见 [docs/CLOUD_DEPLOYMENT.md](../docs/CLOUD_DEPLOYMENT.md) 中的 Docker Compose 部署章节。

### Kubernetes 部署

详见 [docs/CLOUD_DEPLOYMENT.md](../docs/CLOUD_DEPLOYMENT.md) 中的 Kubernetes 部署章节。
