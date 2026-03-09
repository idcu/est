# EST Kotlin Support

Kotlin原生支持模块，提供Kotlin DSL、协程集成、扩展函数等功能。

## 功能特性

- **Kotlin DSL** - 优雅的领域特定语言支持
- **协程集成** - Kotlin协程与EST框架无缝集成
- **扩展函数** - 丰富的Kotlin扩展函数
- **空安全优化** - 利用Kotlin空安全特性
- **数据流支持** - Kotlin Flow集成

## 模块结构

```
est-kotlin-support/
├── est-kotlin-api/      # Kotlin支持API
├── est-kotlin-impl/     # Kotlin支持实现
└── README.md
```

## 快速开始

### 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-kotlin-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 使用Kotlin DSL

```kotlin
import ltd.idcu.est.kotlin.dsl.*

val app = estApplication("MyApp", "1.0.0") {
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

### 使用协程

```kotlin
import ltd.idcu.est.kotlin.coroutines.*
import kotlinx.coroutines.*

estCoroutineScope {
    val result = async {
        // 异步操作
        "Result from coroutine"
    }
    
    println(result.await())
}
```

## 文档

- [Kotlin DSL指南](docs/kotlin-dsl.md)
- [协程集成文档](docs/coroutines.md)
- [扩展函数参考](docs/extensions.md)

## 许可证

MIT License
