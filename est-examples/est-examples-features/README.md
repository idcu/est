# EST Features Examples

Welcome to the EST Features Examples module! This module demonstrates detailed usage of each feature module of the EST Framework.

## Table of Contents
1. [What is EST Features Examples?](#what-is-est-features-examples)
2. [Quick Start](#quick-start)
3. [Example List](#example-list)
4. [How to Run Examples](#how-to-run-examples)
5. [Best Practices](#best-practices)
6. [Next Steps](#next-steps)

---

## What is EST Features Examples?
### In Simple Terms

EST Features Examples are like feature manuals. Imagine you bought a new phone:

**Basic examples**: Learn how to make calls, send messages.

**Features examples**: Learn how to take photos, record videos, set alarms, use various apps.

It includes detailed examples of all feature modules of the EST Framework:
- Various usages of cache system
- Practical scenarios of event bus
- Complete CRUD of data access
- Multiple methods of security authentication
- Scheduled tasks of scheduler system
- Indicator collection of monitor system
- Queue applications of message system

### Core Features

- [X] **Comprehensive Features** - Covers all EST feature modules
- [X] **From Simple to Complex** - Progressive learning from simple to complex
- [X] **Practical Guidance** - Every example comes from real requirements
- [X] **Directly Copyable** - Code can be directly used in projects
- [X] **Best Practices** - Every example demonstrates best practices

---

## Quick Start
### Prerequisites

- [X] Completed learning of [Basic Examples](../est-examples-basic/)
- [X] Understand EST core concepts

### Run Your First Example
```bash
cd est-examples/est-examples-features
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.features.CacheExample"
```

---

## Example List

### Cache Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [CacheExample](src/main/java/ltd/idcu/est/examples/features/CacheExample.java) | Cache Example | Cache basics | ⭐ |
| [EnhancedCacheExample](src/main/java/ltd/idcu/est/examples/features/EnhancedCacheExample.java) | Enhanced Cache Example | Cache advanced features | ⭐⭐ |

### Event Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [EventExample](src/main/java/ltd/idcu/est/examples/features/EventExample.java) | Event Example | Event bus | ⭐ |

### Logging Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [LoggingExample](src/main/java/ltd/idcu/est/examples/features/LoggingExample.java) | Logging Example | Logging configuration | ⭐ |

### Data Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [SimpleDataExample](src/main/java/ltd/idcu/est/examples/features/SimpleDataExample.java) | Simple Data Example | Data basics | ⭐ |
| [DataExample](src/main/java/ltd/idcu/est/examples/features/DataExample.java) | Data Example | Data access | ⭐ |
| [EnhancedDataExample](src/main/java/ltd/idcu/est/examples/features/EnhancedDataExample.java) | Enhanced Data Example | Data advanced features | ⭐⭐ |
| [CompleteDataExample](src/main/java/ltd/idcu/est/examples/features/CompleteDataExample.java) | Complete Data Example | Complete data features | ⭐⭐ |

### Security Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [SimpleSecurityExample](src/main/java/ltd/idcu/est/examples/features/SimpleSecurityExample.java) | Simple Security Example | Security basics | ⭐ |
| [SecurityExample](src/main/java/ltd/idcu/est/examples/features/SecurityExample.java) | Security Example | Security authentication | ⭐ |
| [CompleteSecurityExample](src/main/java/ltd/idcu/est/examples/features/CompleteSecurityExample.java) | Complete Security Example | Complete security features | ⭐⭐ |

### Scheduler Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [SimpleSchedulerExample](src/main/java/ltd/idcu/est/examples/features/SimpleSchedulerExample.java) | Simple Scheduler Example | Scheduler basics | ⭐ |
| [SchedulerExample](src/main/java/ltd/idcu/est/examples/features/SchedulerExample.java) | Scheduler Example | Scheduled tasks | ⭐ |
| [CompleteSchedulerExample](src/main/java/ltd/idcu/est/examples/features/CompleteSchedulerExample.java) | Complete Scheduler Example | Complete scheduler features | ⭐⭐ |

### Monitor Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [SimpleMonitorExample](src/main/java/ltd/idcu/est/examples/features/SimpleMonitorExample.java) | Simple Monitor Example | Monitor basics | ⭐ |
| [MonitorExample](src/main/java/ltd/idcu/est/examples/features/MonitorExample.java) | Monitor Example | Monitor metrics | ⭐ |
| [EnhancedMonitorExample](src/main/java/ltd/idcu/est/examples/features/EnhancedMonitorExample.java) | Enhanced Monitor Example | Monitor advanced features | ⭐⭐ |
| [CompleteMonitorExample](src/main/java/ltd/idcu/est/examples/features/CompleteMonitorExample.java) | Complete Monitor Example | Complete monitor features | ⭐⭐ |

### Messaging Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [SimpleMessagingExample](src/main/java/ltd/idcu/est/examples/features/SimpleMessagingExample.java) | Simple Messaging Example | Messaging basics | ⭐ |
| [MessagingExample](src/main/java/ltd/idcu/est/examples/features/MessagingExample.java) | Messaging Example | Message queues | ⭐ |
| [CompleteMessagingExample](src/main/java/ltd/idcu/est/examples/features/CompleteMessagingExample.java) | Complete Messaging Example | Complete messaging features | ⭐⭐ |

### Comprehensive Examples

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [ComprehensiveFeaturesExample](src/main/java/ltd/idcu/est/examples/features/ComprehensiveFeaturesExample.java) | Comprehensive Features Example | Multi-module integration | ⭐⭐ |
| [MicroservicesExample](src/main/java/ltd/idcu/est/examples/features/MicroservicesExample.java) | Microservices Example | Microservices | ⭐⭐ |
| [MigrationExample](src/main/java/ltd/idcu/est/examples/features/MigrationExample.java) | Migration Example | Framework migration | ⭐⭐ |
| [PerformanceExample](src/main/java/ltd/idcu/est/examples/features/PerformanceExample.java) | Performance Example | Performance testing | ⭐⭐ |

---

## How to Run Examples

### Run Specific Example

```bash
cd est-examples/est-examples-features
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.features.CacheExample"
```

### Use Script (Windows)
```bash
cd est-examples/est-examples-features
run-examples.bat
```

### Use Script (PowerShell)
```powershell
cd est-examples/est-examples-features
.\run-examples.ps1
```

---

## Best Practices

### 1. Choose Appropriate Implementation
```java
// Recommended: Choose according to scenario
// Small data volume, use memory cache
Cache<String, User> cache = Caches.newMemoryCache();

// Large data volume, need persistence, use Redis
Cache<String, User> cache = Caches.newRedisCache();

// Not recommended: Blind choice
// Use Redis for all scenarios, waste resources
```

### 2. Reasonable Configuration

```java
// Recommended: Configure according to actual needs
Cache<String, String> cache = Caches.<String, String>builder()
    .maxSize(10000)           // Set according to data volume
    .defaultTtl(3600000)       // Set according to update frequency
    .build();

// Not recommended: Blind configuration
Cache<String, String> badCache = Caches.<String, String>builder()
    .maxSize(10000000)        // Too large, waste memory
    .defaultTtl(1000)          // Too short, frequent expiration
    .build();
```

---

## Next Steps
- 🌱 Start with [CacheExample](src/main/java/ltd/idcu/est/examples/features/CacheExample.java)
- 📖 Want to see advanced examples? Check [Advanced Examples](../est-examples-advanced/)
- 📚 Want to learn more about modules? Check [Module Docs](../../docs/modules/README.md)

---

**Documentation Version**: 2.0  
**Last Updated**: 2026-03-13  
**Maintainer**: EST Framework Team
