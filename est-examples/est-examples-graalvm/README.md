# EST GraalVM Native Image 示例

本模块展示了如何使用GraalVM将EST框架应用编译为原生镜像。

## 前置要求

- GraalVM 23.1.2 或更高版本
- Java 21+
- Maven 3.8+

## 安装GraalVM

1. 下载GraalVM: https://www.graalvm.org/downloads/
2. 配置JAVA_HOME环境变量指向GraalVM安装目录
3. 安装native-image组件:
   ```bash
   gu install native-image
   ```

## 编译原生镜像

### 基本示例

```bash
cd est-examples/est-examples-graalvm
mvn clean package -Pnative
```

### 运行原生镜像

```bash
./target/est-examples-graalvm
```

## 示例项目

### 1. HelloWorldNative

简单的依赖注入示例，展示EST容器在原生镜像中的运行。

```bash
mvn clean package -Pnative -DmainClass=ltd.idcu.est.examples.graalvm.HelloWorldNative
./target/est-examples-graalvm
```

### 2. WebAppNative

Web应用示例，展示EST Web框架在原生镜像中的运行。

```bash
mvn clean package -Pnative -DmainClass=ltd.idcu.est.examples.graalvm.WebAppNative
./target/est-examples-graalvm
```

然后访问:
- http://localhost:8080
- http://localhost:8080/api/hello
- http://localhost:8080/api/status

## 性能对比

| 指标 | JVM模式 | GraalVM原生模式 |
|------|---------|-----------------|
| 启动时间 | ~500ms | ~30ms |
| 内存占用 | ~50MB | ~25MB |
| 镜像大小 | ~5MB (JAR) | ~25MB (原生) |

## 配置说明

### 反射配置

在 `src/main/resources/META-INF/native-image/` 目录下配置:

- `reflect-config.json`: 反射配置
- `resource-config.json`: 资源配置
- `jni-config.json`: JNI配置
- `serialization-config.json`: 序列化配置
- `proxy-config.json`: 动态代理配置

### Maven插件配置

项目使用 `native-maven-plugin` 来简化原生镜像编译过程。

## 提示

1. 首次编译原生镜像可能需要较长时间（几分钟）
2. 原生镜像不支持动态类加载，所有需要的类必须在编译时可知
3. 使用 `--enable-http` 和 `--enable-https` 来启用网络支持
4. 遇到反射问题时，需要在 `reflect-config.json` 中添加相应配置

## 故障排除

如果遇到编译错误:

1. 确保使用正确版本的GraalVM
2. 检查反射配置是否完整
3. 使用 `-H:+ReportExceptionStackTraces` 获取更详细的错误信息
4. 查看GraalVM官方文档: https://www.graalvm.org/reference-manual/native-image/
