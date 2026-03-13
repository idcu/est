# EST Framework Kotlin Examples

This directory contains example code for EST Framework Kotlin native support.

## Features

- **Kotlin DSL** - Elegant domain-specific language support
- **Coroutine Integration** - Seamless integration between Kotlin coroutines and EST Framework
- **Extension Functions** - Rich Kotlin extension functions
- **Null Safety Optimization** - Leveraging Kotlin null safety features

## Module Structure

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
│                               ├── KotlinDslExample.kt      # Kotlin DSL Example
│                               └── KotlinExtensionsExample.kt # Extension Functions Example
├── pom.xml
└── README.md
```

## Quick Start

### Prerequisites

- JDK 21+
- Maven 3.8+
- Kotlin 1.9+

### Run Examples

```bash
# Enter example directory
cd est-examples-kotlin

# Compile project
mvn clean compile

# Run Kotlin DSL example
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.kotlin.KotlinDslExample"

# Run extension functions example
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.kotlin.KotlinExtensionsExample"
```

## Example Description

### 1. Kotlin DSL Example (KotlinDslExample.kt)

Demonstrates how to create EST applications using Kotlin DSL:

```kotlin
val app = estApplication("MyKotlinApp", "1.0.0") {
    description = "This is an EST application created with Kotlin DSL"
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

Features:
- Application configuration DSL
- Web service configuration
- Route definition DSL
- Fluent API design

### 2. Coroutine Example (KotlinDslExample.kt)

Demonstrates integration between Kotlin coroutines and EST Framework:

```kotlin
import ltd.idcu.est.kotlin.coroutines.estCoroutineScope
import ltd.idcu.est.kotlin.coroutines.estAsync

runBlocking {
    val job = estCoroutineScope {
        println("Coroutine executing...")
        delay(500)
    }
    
    val result = estAsync {
        "Coroutine result"
    }
}
```

Features:
- Custom coroutine dispatcher
- Coroutine scope management
- Asynchronous task execution
- Timeout control

### 3. Extension Functions Example (KotlinExtensionsExample.kt)

Demonstrates Kotlin extension functions provided by EST Framework:

```kotlin
// Collection extensions
val list = listOf(1, 2, 3, 4, 5)
val even = list.filter { it % 2 == 0 }
val doubled = list.map { it * 2 }

// String extensions
val str = "Hello, EST!"
val reversed = str.reversed()
val uppercase = str.uppercase()

// Safe type conversion
val safeInt = "123".toIntOrNull() ?: 0
```

## Related Documentation

- [Kotlin DSL Guide](../../docs/guides/kotlin-dsl.md)
- [Coroutine Integration Documentation](../../docs/guides/coroutines.md)
- [Extension Functions Reference](../../docs/guides/extensions.md)
- [EST Kotlin Support Module](../../est-modules/est-kotlin-support/README.md)

## Tech Stack

- **Kotlin**: 1.9.20
- **Kotlin Coroutines**: 1.7.3
- **EST Framework**: 2.4.0-SNAPSHOT

## License

MIT License
