# EST Framework Kotlin 示例

本目录包含 EST Framework Kotlin 原生支持的示例代码。

## 功能特性

- **Kotlin DSL** - 优雅的领域特定语言支持
- **协程集成** - Kotlin协程与EST框架无缝集成
- **扩展函数** - 丰富的Kotlin扩展函数
- **空安全优化** - 利用Kotlin空安全特性

## 模块结构

```
est-examples-kotlin/
├── src/
│   └── main/
│       └── kotlin/
│           └── ltd/
│               └── idcu/
│                   └── est/
│                       └── examples/
│                           └── kotlin/
│                               ├── KotlinDslExample.kt      # Kotlin DSL 示例
│                               └── KotlinExtensionsExample.kt # 扩展函数示例
├── pom.xml
└── README.md
```

## 快速开始

### 前置要求

- JDK 21+
- Maven 3.8+
- Kotlin 1.9+

### 运行示例

```bash
# 进入示例目录
cd est-examples-kotlin

# 编译项目
mvn clean compile

# 运行 Kotlin DSL 示例
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.kotlin.KotlinDslExample"

# 运行扩展函数示例
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.kotlin.KotlinExtensionsExample"
```

## 示例说明

### 1. Kotlin DSL 示例 (KotlinDslExample.kt)

展示如何使用 Kotlin DSL 创建 EST 应用：

```kotlin
val app = estApplication("MyKotlinApp", "1.0.0") {
    description = "这是一个使用 Kotlin DSL 创建的 EST 应用"
    author = "EST Team"
    
    web {
        port = 8080
        
        router {
            get("/hello") { req, res ->
                res.send("Hello, Kotlin!")
            }
        }
    }
}

app.run()
```

功能特性：
- 应用配置 DSL
- Web 服务配置
- 路由定义 DSL
- 流畅的 API 设计

### 2. 协程示例 (KotlinDslExample.kt)

展示 Kotlin 协程与 EST 框架的集成：

```kotlin
import ltd.idcu.est.kotlin.coroutines.estCoroutineScope
import ltd.idcu.est.kotlin.coroutines.estAsync

runBlocking {
    val job = estCoroutineScope {
        println("协程执行中...")
        delay(500)
    }
    
    val result = estAsync {
        "协程结果"
    }
}
```

功能特性：
- 自定义协程调度器
- 协程作用域管理
- 异步任务执行
- 超时控制

### 3. 扩展函数示例 (KotlinExtensionsExample.kt)

展示 EST 框架提供的 Kotlin 扩展函数：

```kotlin
// 集合扩展
val list = listOf(1, 2, 3, 4, 5)
val even = list.filter { it % 2 == 0 }
val doubled = list.map { it * 2 }

// 字符串扩展
val str = "Hello, EST!"
val reversed = str.reversed()
val uppercase = str.uppercase()

// 安全类型转换
val safeInt = "123".toIntOrNull() ?: 0
```

## 相关文档

- [Kotlin DSL 指南](../../docs/guides/kotlin-dsl.md)
- [协程集成文档](../../docs/guides/coroutines.md)
- [扩展函数参考](../../docs/guides/extensions.md)
- [EST Kotlin 支持模块](../../est-modules/est-kotlin-support/README.md)

## 技术栈

- **Kotlin**: 1.9.20
- **Kotlin Coroutines**: 1.7.3
- **EST Framework**: 2.4.0-SNAPSHOT

## 许可证

MIT License
