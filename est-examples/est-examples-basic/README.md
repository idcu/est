# EST Basic Examples

Welcome to the EST Basic Examples module! This is the starting point for beginners, containing the most basic and commonly used feature examples of the EST Framework.

## 📋 Table of Contents

1. [What is EST Basic Examples?](#what-is-est-basic-examples)
2. [Quick Start: 5-Minute Guide](#quick-start-5-minute-guide)
3. [Example Categories](#example-categories)
4. [How to Run Examples](#how-to-run-examples)
5. [Best Practices](#best-practices)
6. [Frequently Asked Questions](#frequently-asked-questions)
7. [Next Steps](#next-steps)

---

## What is EST Basic Examples?

### In Simple Terms

EST Basic Examples is like a training tool for programming. Imagine you're learning to ride a bicycle:

**Traditional approach**: Start riding a mountain bike right away, trying to do tricks - easy to fall.

**EST Basic Examples approach**: Start with a tricycle, training wheels, learn slowly, upgrade when comfortable.

It contains the most commonly used content in your daily development:
- How to use dependency injection?
- How to use configuration?
- How to use cache?
- How to use data?
- How to build a simple Web application?

### Key Features

- 🎯 **Simple and Easy to Understand** - Each example focuses on one feature, no complex distractions
- 🚀 **Runnable Directly** - Copy and run, no extra configuration needed
- 📚 **Comprehensive Coverage** - From container to Web, from cache to data
- 🏃 **Progressive** - From simple to complex, suitable for different skill levels
- 💡 **Real-World Scenarios** - Examples from real project needs

---

## Quick Start: 5-Minute Guide

### Step 1: Ensure the project is built

```bash
cd est2.0
mvn clean install -DskipTests
```

### Step 2: Run your first example

```java
import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.impl.DefaultContainer;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Basic Example - First Container ===\n");
        
        // 1. Create container
        Container container = new DefaultContainer();
        
        // 2. Register a string
        container.registerSingleton(String.class, "Hello EST Basic Example!");
        
        // 3. Get and use
        String message = container.get(String.class);
        System.out.println("Result: " + message);
        
        System.out.println("\nCongratulations! You've successfully run your first basic example!");
    }
}
```

### Step 3: Run Main class (Recommended)

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

---

## Example Categories

### 1. Core Container Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [CoreExample](src/main/java/ltd/idcu/est/examples/basic/CoreExample.java) | Core feature comprehensive example | Core features | ⭐ |
| [EnhancedContainerExample](src/main/java/ltd/idcu/est/examples/basic/EnhancedContainerExample.java) | Enhanced container example | Enhanced features | ⭐⭐ |
| [SimpleAutowiringTest](src/main/java/ltd/idcu/est/examples/basic/SimpleAutowiringTest.java) | Simple autowiring test | Autowiring | ⭐ |

### 2. Design Pattern Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [PatternExample](src/main/java/ltd/idcu/est/examples/basic/PatternExample.java) | Design pattern examples | Design patterns | ⭐ |

### 3. Utility Class Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [UtilsExample](src/main/java/ltd/idcu/est/examples/basic/UtilsExample.java) | Utility class examples | Utility classes | ⭐ |

### 4. Comprehensive Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [Main](src/main/java/ltd/idcu/est/examples/basic/Main.java) | Main entry point | Run all examples | ⭐ |

---

## How to Run Examples

### Prerequisites

- ☕ JDK 21+
- 📦 Maven 3.6+
- ✅ EST project already built (`mvn clean install`)

### Method 1: Run Main class (Recommended)

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

### Method 2: Run specific example

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.CoreExample"
```

### Method 3: Run in IDE

1. Open project in IDE
2. Find the example file
3. Run the main method directly

---

## Best Practices

### 1. Start Simple

```java
// ✅ Recommended: Start with simple implementation
Container container = new DefaultContainer();
container.registerSingleton(String.class, "Hello");

// ❌ Not recommended: Use complex configuration right away
Container container = DefaultContainer.builder()
    .scanPackages("com.example")
    .enableLazyLoading()
    .build();
```

**Why?**
- Get it running first, then optimize
- Simple implementation is easier to understand and debug
- Consider advanced configuration when you're familiar

### 2. Meaningful Names

```java
// ✅ Recommended: Meaningful variable names
Cache<String, User> userCache = Caches.newMemoryCache();
userCache.put("user:1", new User("ZhangSan"));

// ❌ Not recommended: Unreadable variable names
Cache<String, User> c = Caches.newMemoryCache();
c.put("u:1", new User("zs"));
```

### 3. Proper Use of Optional

```java
// ✅ Recommended: Use Optional to handle possible null values
Optional<User> userOpt = userCache.get("user:1");
userOpt.ifPresent(user -> System.out.println(user));

// Or provide default value
User user = userOpt.orElse(defaultUser);

// ❌ Not recommended: Direct get(), may throw exception
User user = userCache.get("user:1").get();
```

### 4. Remember to Clean Up Resources

```java
// ✅ Recommended: Use try-with-resources
try (Container container = new DefaultContainer()) {
    container.registerSingleton(String.class, "Hello");
    String message = container.get(String.class);
    System.out.println(message);
}

// ❌ Not recommended: Don't close resources, may cause leaks
Container container = new DefaultContainer();
// Don't care after use
```

---

## Frequently Asked Questions

### Q: What if the example fails to run?

A: First ensure:
1. You have run `mvn clean install`
2. JDK version is 21+
3. Maven version is 3.6+
4. Check for compilation errors

### Q: How to find the example I need?

A:
1. First look at categories, find related features
2. Read example descriptions, find matching ones
3. Start with basic examples, progress gradually

### Q: Can example code be directly used in projects?

A: Yes! But it's recommended:
1. First understand the example code
2. Adjust according to your project needs
3. Use in combination with best practices

---

## Next Steps

- 🚀 Start with [CoreExample](src/main/java/ltd/idcu/est/examples/basic/CoreExample.java)
- 📚 Want to see more examples, check [Feature Examples](../est-examples-features/)
- 🌐 Want to learn about Web development, check [Web Examples](../est-examples-web/)
- 🏃 Want to learn best practices, check [Best Practices Course](../../docs/best-practices/course/README.md)

---

**Documentation Version**: 2.0  
**Last Updated**: 2026-03-07  
**Maintainer**: EST Framework Team
