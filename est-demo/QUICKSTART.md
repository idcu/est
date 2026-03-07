# EST Demo 快速启动指南

## 项目已成功创建！

EST Demo应用已经成功创建并编译。由于PowerShell执行策略和Maven参数解析的限制，
我们提供以下几种方式来运行这个演示应用：

## 方式一：使用IDE运行（推荐）

1. 在您的IDE（IntelliJ IDEA, Eclipse等）中打开项目
2. 导航到 `est-demo/src/main/java/ltd/idcu/est/demo/EstDemoApplication.java`
3. 右键点击 `main` 方法，选择 "Run" 或 "Debug"
4. 应用将在 http://localhost:8080 启动

## 方式二：使用Maven命令（需要cmd.exe）

如果您有cmd.exe访问权限，可以：

```bash
cd est-demo
mvn exec:java -Dexec.mainClass="ltd.idcu.est.demo.EstDemoApplication"
```

## 方式三：构建可执行JAR

```bash
cd est-demo
mvn clean package
java -jar target/est-demo-2.0.0.jar
```

## 可用的API端点

启动成功后，您可以访问以下端点：

- `GET /` - 欢迎页面
- `GET /api/users` - 获取所有用户列表
- `GET /api/users/1` - 获取ID为1的用户信息
- `GET /hello?name=YourName` - 打招呼接口

## 示例请求

```bash
# 获取欢迎信息
curl http://localhost:8080

# 获取用户列表
curl http://localhost:8080/api/users

# 获取特定用户
curl http://localhost:8080/api/users/2

# 打招呼
curl http://localhost:8080/hello?name=EST
```

## 项目结构

```
est-demo/
├── pom.xml                          # Maven配置文件
├── README.md                        # 项目说明
├── QUICKSTART.md                    # 本文件
├── run.bat                          # Windows批处理启动脚本
├── run.ps1                          # PowerShell启动脚本
├── run-with-classes.ps1            # 使用编译类的启动脚本
└── src/
    └── main/
        └── java/
            └── ltd/
                └── idcu/
                    └── est/
                        └── demo/
                            └── EstDemoApplication.java  # 主应用类
```

## 技术栈

- EST Web框架 - 零依赖Web服务器
- EST JSON工具 - JSON序列化/反序列化
- EST 日志 - 控制台日志输出
- EST 缓存 - 内存缓存支持

享受使用EST框架！
