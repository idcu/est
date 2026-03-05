# EST 框架开发及运行环境部署指南

本文档详细介绍 EST 框架的开发环境搭建、项目构建以及运行环境部署的完整流程。

## 目录

- [一、开发环境准备](#一开发环境准备)
- [二、项目构建](#二项目构建)
- [三、本地运行](#三本地运行)
- [四、Docker 部署](#四docker-部署)
- [五、Docker Compose 部署](#五docker-compose-部署)
- [六、Kubernetes 部署](#六kubernetes-部署)
- [七、CI/CD 持续集成](#七cicd-持续集成)
- [八、常见问题排查](#八常见问题排查)

---

## 一、开发环境准备

### 1.1 硬件要求

- **CPU**: 双核及以上
- **内存**: 4GB 及以上（推荐 8GB+）
- **磁盘空间**: 至少 5GB 可用空间

### 1.2 软件要求

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 21 或更高 | 必须 |
| Maven | 3.6 或更高 | 用于项目构建 |
| Git | 任意 | 用于代码版本控制 |
| IDE | IntelliJ IDEA / Eclipse / VS Code | 推荐 IntelliJ IDEA |

### 1.3 JDK 安装

#### Windows

1. 下载 JDK 21 安装包：https://adoptium.net/
2. 运行安装程序，按照提示完成安装
3. 配置环境变量：
   - 新建系统变量 `JAVA_HOME`，值为 JDK 安装路径（如 `C:\Program Files\Eclipse Adoptium\jdk-21.x.x-hotspot`）
   - 在 `Path` 变量中添加 `%JAVA_HOME%\bin`

#### macOS

```bash
# 使用 Homebrew 安装
brew install openjdk@21

# 配置环境变量
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"' >> ~/.zshrc
source ~/.zshrc
```

#### Linux (Ubuntu/Debian)

```bash
# 更新软件包列表
sudo apt update

# 安装 JDK 21
sudo apt install -y openjdk-21-jdk

# 验证安装
java -version
```

### 1.4 Maven 安装

#### Windows

1. 下载 Maven：https://maven.apache.org/download.cgi
2. 解压到指定目录（如 `C:\Program Files\Apache\maven`）
3. 配置环境变量：
   - 新建系统变量 `MAVEN_HOME`，值为 Maven 解压路径
   - 在 `Path` 变量中添加 `%MAVEN_HOME%\bin`

#### macOS

```bash
brew install maven
```

#### Linux (Ubuntu/Debian)

```bash
sudo apt install -y maven
```

### 1.5 验证环境配置

打开终端，执行以下命令验证环境是否配置正确：

```bash
# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version

# 检查 Git 版本
git --version
```

预期输出示例：
```
java version "21.0.1" 2023-10-17 LTS
Apache Maven 3.9.5
git version 2.42.0
```

### 1.6 IDE 配置（推荐 IntelliJ IDEA）

1. 安装 IntelliJ IDEA：https://www.jetbrains.com/idea/
2. 打开 IDEA，选择 `File` → `Open`，选择项目根目录
3. 配置 JDK：
   - `File` → `Project Structure` → `Project`
   - 设置 `SDK` 为 JDK 21
   - 设置 `Language level` 为 `21 - Pattern matching for switch`
4. 配置 Maven：
   - `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven`
   - 确认 `Maven home path` 配置正确
5. 启用预览特性（如需要）：
   - `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Java Compiler`
   - 在 `Additional command line parameters` 中添加 `--enable-preview`

---

## 二、项目构建

### 2.1 克隆项目

```bash
# 克隆项目仓库
git clone https://github.com/idcu/est.git
cd est
```

### 2.2 Maven 构建命令

#### 完整构建（包含测试）

```bash
mvn clean install
```

#### 快速构建（跳过测试）

```bash
mvn clean install -DskipTests
```

#### 只构建指定模块

```bash
# 只构建 web 模块
mvn clean install -pl est-web

# 构建 web 模块及其依赖
mvn clean install -pl est-web -am
```

#### 只编译不打包

```bash
mvn clean compile
```

#### 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定模块的测试
mvn test -pl est-web

# 运行指定测试类
mvn test -Dtest=DefaultRouterTest
```

### 2.3 构建产物

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

## 三、本地运行

### 3.1 运行示例应用

#### 方式一：使用 Maven 运行

```bash
# 进入示例模块目录
cd est-examples/est-examples-web

# 编译并运行
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

#### 方式二：直接运行 JAR

```bash
# 先构建项目
mvn clean package -DskipTests

# 运行示例应用
java -jar est-examples/est-examples-web/target/est-examples-web-1.3.0-SNAPSHOT.jar
```

#### 方式三：在 IDE 中运行

1. 在 IDEA 中打开项目
2. 找到示例类：`est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java`
3. 右键点击类名，选择 `Run 'BasicWebAppExample.main()'`

### 3.2 验证应用运行

应用启动后，打开浏览器访问以下地址验证：

- 首页：http://localhost:8080
- API 示例：http://localhost:8080/api/greeting

### 3.3 JVM 参数调优

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

## 四、Docker 部署

### 4.1 Docker 环境准备

#### 安装 Docker

- **Windows/macOS**: 下载 Docker Desktop：https://www.docker.com/products/docker-desktop
- **Linux**: 参考官方文档：https://docs.docker.com/engine/install/

#### 验证 Docker 安装

```bash
docker --version
docker-compose --version
```

### 4.2 构建 Docker 镜像

项目已提供 Dockerfile，位于 `deploy/docker/Dockerfile`。

```bash
# 在项目根目录执行
docker build -t est-web-app:latest -f deploy/docker/Dockerfile .
```

### 4.3 运行 Docker 容器

```bash
# 运行容器
docker run -d \
  --name est-web-app \
  -p 8080:8080 \
  --restart unless-stopped \
  est-web-app:latest
```

### 4.4 Docker 常用命令

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

### 4.5 访问应用

打开浏览器访问：http://localhost:8080

---

## 五、Docker Compose 部署

### 5.1 配置说明

项目已提供 docker-compose.yml，位于 `deploy/docker/docker-compose.yml`。

### 5.2 启动服务

```bash
# 方式一：在 deploy/docker 目录下执行
cd deploy/docker
docker-compose up -d

# 方式二：在项目根目录执行
docker-compose -f deploy/docker/docker-compose.yml up -d
```

### 5.3 Docker Compose 常用命令

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

## 六、Kubernetes 部署

### 6.1 环境准备

#### 安装 kubectl

参考官方文档：https://kubernetes.io/docs/tasks/tools/

#### 准备 Kubernetes 集群

可以选择以下任一方式：

- **本地开发**：minikube、kind、k3s
- **云平台**：AWS EKS、Azure AKS、Google GKE、阿里云 ACK

### 6.2 构建并推送镜像

```bash
# 构建镜像（替换为你的镜像仓库地址）
docker build -t your-registry/est-web-app:latest -f deploy/docker/Dockerfile .

# 登录镜像仓库
docker login your-registry

# 推送镜像
docker push your-registry/est-web-app:latest
```

### 6.3 更新 Kubernetes 配置

编辑 `deploy/k8s/deployment.yaml`，修改镜像地址：

```yaml
spec:
  containers:
  - name: est-web-app
    image: your-registry/est-web-app:latest  # 替换为你的镜像地址
```

### 6.4 部署应用

```bash
# 创建命名空间（可选）
kubectl create namespace est-app

# 部署应用
kubectl apply -f deploy/k8s/deployment.yaml
kubectl apply -f deploy/k8s/service.yaml
kubectl apply -f deploy/k8s/ingress.yaml
```

### 6.5 验证部署

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

### 6.6 访问应用

根据你的 Kubernetes 环境配置访问应用。

---

## 七、CI/CD 持续集成

### 7.1 GitHub Actions 配置

项目已配置 GitHub Actions 工作流，位于 `.github/workflows/ci-cd.yml`。

### 7.2 配置 Secrets

在 GitHub 仓库的 `Settings` → `Secrets and variables` → `Actions` 中添加以下 Secrets：

| Secret 名称 | 说明 |
|-------------|------|
| `DOCKER_HUB_USERNAME` | Docker Hub 用户名 |
| `DOCKER_HUB_TOKEN` | Docker Hub 访问令牌 |
| `KUBE_CONFIG` | Kubernetes 配置文件内容（base64 编码） |

### 7.3 触发 CI/CD

推送到 `main` 或 `master` 分支会自动触发 CI/CD 流水线，执行以下步骤：

1. 检出代码
2. 设置 JDK 21
3. 缓存 Maven 依赖
4. 构建项目
5. 构建并推送 Docker 镜像
6. 部署到 Kubernetes 集群

---

## 八、常见问题排查

### 8.1 开发环境问题

#### Q: Maven 构建速度慢

A: 配置国内 Maven 镜像源，编辑 `~/.m2/settings.xml`：

```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Aliyun Maven</name>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

#### Q: 编译时提示找不到符号

A: 确保已执行 `mvn clean install` 安装所有模块到本地仓库。

### 8.2 运行时问题

#### Q: 端口 8080 已被占用

A: 可以修改应用端口或停止占用端口的进程：

```bash
# Windows 查看端口占用
netstat -ano | findstr :8080

# Linux/macOS 查看端口占用
lsof -i :8080
```

#### Q: 内存不足导致 OOM

A: 增加 JVM 堆内存：

```bash
java -Xms1g -Xmx4g -jar app.jar
```

### 8.3 Docker 问题

#### Q: Docker 镜像构建失败

A: 确保 Docker Desktop 正在运行，并且有足够的磁盘空间。

#### Q: 容器启动后立即退出

A: 查看容器日志排查问题：

```bash
docker logs est-web-app
```

### 8.4 Kubernetes 问题

#### Q: Pod 一直处于 Pending 状态

A: 检查节点资源和事件：

```bash
kubectl describe pod <pod-name>
kubectl get events
```

#### Q: 无法访问应用

A: 检查 Service 和 Ingress 配置：

```bash
kubectl get service
kubectl get ingress
kubectl describe ingress <ingress-name>
```

---

## 附录

### A. 参考资料

- [EST 框架 GitHub 仓库](https://github.com/idcu/est)
- [Java 21 官方文档](https://docs.oracle.com/en/java/javase/21/)
- [Maven 官方文档](https://maven.apache.org/guides/)
- [Docker 官方文档](https://docs.docker.com/)
- [Kubernetes 官方文档](https://kubernetes.io/docs/)

### B. 联系方式

如有问题，请提交 Issue：https://github.com/idcu/est/issues

---

**文档版本**: 1.0  
**最后更新**: 2026-03-05
