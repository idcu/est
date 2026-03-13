# EST Advanced Examples

Welcome to the EST Advanced Examples module! This demonstrates the advanced features and complex scenario applications of the EST Framework.

## 📋 Table of Contents

1. [What is EST Advanced Examples?](#what-is-est-advanced-examples)
2. [Quick Start](#quick-start)
3. [Example List](#example-list)
4. [How to Run Examples](#how-to-run-examples)
5. [Best Practices](#best-practices)
6. [Next Steps](#next-steps)

---

## What is EST Advanced Examples?

### In Simple Terms

EST Advanced Examples is like an advanced programming textbook. Imagine you already know how to drive, now you want to learn drifting, stunt driving, etc.:

**Basic Examples**: Learn how to start, stop, and turn.

**Advanced Examples**: Learn how to drift, overtake, and handle complex road conditions.

It contains content that only experienced developers will use:
- Complete application architecture
- Deep module integration
- Custom framework extensions
- Performance optimization techniques
- New architecture practices

### Key Features

- 🎯 **Deep Understanding** - Not just how to use, but understand why
- 🚀 **Performance Optimization** - Demonstrates various performance optimization techniques
- 📚 **Architecture Design** - Complete application architecture examples
- 🔧 **Framework Extension** - How to customize and extend EST
- 💡 **Real-World Scenarios** - Complex scenarios from real projects

---

## Quick Start

### Prerequisites

- ✅ Already completed [Basic Examples](../est-examples-basic/) learning
- ✅ Understand EST's core concepts
- ✅ Have some Java development experience

### Run Your First Example

```bash
cd est-examples/est-examples-advanced
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.advanced.Main"
```

---

## Example List

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [CompleteApplicationExample](src/main/java/ltd/idcu/est/examples/advanced/CompleteApplicationExample.java) | Complete application example | Complete application architecture | ⭐⭐⭐ |
| [ModuleIntegrationExample](src/main/java/ltd/idcu/est/examples/advanced/ModuleIntegrationExample.java) | Module integration example | Module integration | ⭐⭐⭐ |
| [MultiModuleIntegrationExample](src/main/java/ltd/idcu/est/examples/advanced/MultiModuleIntegrationExample.java) | Multi-module integration example | Multi-module integration | ⭐⭐⭐ |
| [CustomExtensionExample](src/main/java/ltd/idcu/est/examples/advanced/CustomExtensionExample.java) | Custom extension example | Framework extension | ⭐⭐⭐ |
| [NewArchitectureExample](src/main/java/ltd/idcu/est/examples/advanced/NewArchitectureExample.java) | New architecture example | New architecture | ⭐⭐⭐ |
| [PerformanceOptimizationExample](src/main/java/ltd/idcu/est/examples/advanced/PerformanceOptimizationExample.java) | Performance optimization example | Performance tuning | ⭐⭐⭐ |
| [Main](src/main/java/ltd/idcu/est/examples/advanced/Main.java) | Main entry point | Run all examples | ⭐⭐ |

---

## How to Run Examples

### Run Main Class (Recommended)

```bash
cd est-examples/est-examples-advanced
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.advanced.Main"
```

### Run Specific Example

```bash
cd est-examples/est-examples-advanced
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.advanced.CompleteApplicationExample"
```

---

## Best Practices

### 1. Understand First, Then Extend

```java
// ✅ Recommended: First understand existing code, then extend
// First read EST source code, understand design thinking
// Then perform custom extensions

// ❌ Not recommended: Blindly extend without understanding principles
// Directly write code, may damage framework design
```

### 2. Performance Optimization Needs Basis

```java
// ✅ Recommended: First measure, then optimize
long start = System.currentTimeMillis();
// Execute operations
long duration = System.currentTimeMillis() - start;
if (duration > 1000) {
    // Consider optimization
}

// ❌ Not recommended: Blindly optimize without measurement
// Optimize for optimization's sake, may actually reduce performance
```

---

## Next Steps

- 🚀 Start with [CompleteApplicationExample](src/main/java/ltd/idcu/est/examples/advanced/CompleteApplicationExample.java)
- 📚 Want to understand architecture design, check [Architecture Documentation](../../docs/architecture/README.md)
- 🌐 Want to contribute code, check [Contribution Guide](../../docs/contributing/README.md)

---

**Documentation Version**: 2.0  
**Last Updated**: 2026-03-07  
**Maintainer**: EST Framework Team
