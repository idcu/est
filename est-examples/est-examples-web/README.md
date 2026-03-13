# EST Web Examples

Welcome to the EST Web Examples module! This module demonstrates how to build various web applications using the EST Framework.

## Table of Contents
1. [What is EST Web Examples?](#what-is-est-web-examples)
2. [Quick Start](#quick-start)
3. [Example List](#example-list)
4. [How to Run Examples](#how-to-run-examples)
5. [Best Practices](#best-practices)
6. [Next Steps](#next-steps)

---

## What is EST Web Examples?
### In Simple Terms

EST Web Examples are like website templates. Imagine you want to build a website:

**From scratch**: Write HTML, CSS, JavaScript, and backend logic yourself - that's a lot of work.

**EST Web Examples**: Gives you ready-to-use templates like Todo apps, blogs, kanban boards, chat apps, etc., that you can use directly.

It includes various common web application scenarios:
- Simple Hello World web app
- RESTful API development
- MVC architecture implementation
- Middleware usage
- Template engine rendering
- WebSocket real-time communication
- File upload handling
- Todo list management
- Blog system
- Kanban project management
- Chat application

### Core Features

- [X] **Rich Scenarios** - Covers various web application scenarios
- [X] **Quick Start** - Every example can be run directly
- [X] **From Simple to Complex** - Progressive learning from simple to complex
- [X] **Practical Guidance** - All are commonly used features in real projects
- [X] **Best Practices** - Demonstrates web development best practices

---

## Quick Start
### Prerequisites

- [X] Completed learning of [Basic Examples](../est-examples-basic/)
- [X] Understand EST core concepts
- [X] Basic web development knowledge

### Run Your First Example
```bash
cd est-examples/est-examples-web
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

Then visit in your browser: http://localhost:8080

---

## Example List

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [BasicWebAppExample](src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java) | Basic Web App | Routing, request/response | ⭐ |
| [TodoAppExample](src/main/java/ltd/idcu/est/examples/web/TodoAppExample.java) | Todo App | CRUD, forms | ⭐ |
| [BlogAppExample](src/main/java/ltd/idcu/est/examples/web/BlogAppExample.java) | Blog App | Template engine | ⭐ |
| [KanbanAppExample](src/main/java/ltd/idcu/est/examples/web/KanbanAppExample.java) | Kanban App | Complex interactions | ⭐⭐ |
| [ChatAppExample](src/main/java/ltd/idcu/est/examples/web/ChatAppExample.java) | Chat App | WebSocket | ⭐⭐ |
| [MvcExample](src/main/java/ltd/idcu/est/examples/web/MvcExample.java) | MVC Example | MVC pattern | ⭐ |
| [RestApiExample](src/main/java/ltd/idcu/est/examples/web/RestApiExample.java) | REST API Example | RESTful API | ⭐ |
| [TemplateAndFormExample](src/main/java/ltd/idcu/est/examples/web/TemplateAndFormExample.java) | Template and Form Example | Templates, forms | ⭐ |
| [FileUploadExample](src/main/java/ltd/idcu/est/examples/web/FileUploadExample.java) | File Upload Example | File handling | ⭐ |
| [MiddlewareExample](src/main/java/ltd/idcu/est/examples/web/MiddlewareExample.java) | Middleware Example | Middleware | ⭐ |
| [EnhancedWebExample](src/main/java/ltd/idcu/est/examples/web/EnhancedWebExample.java) | Enhanced Web Example | Advanced features | ⭐⭐ |

---

## How to Run Examples

### Run BasicWebAppExample (Recommended)

```bash
cd est-examples/est-examples-web
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

Then visit: http://localhost:8080

### Run Other Examples

```bash
cd est-examples/est-examples-web
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.TodoAppExample"
```

---

## Best Practices

### 1. RESTful API Design
```java
// Recommended: RESTful style
@Get("/users")
public List<User> getUsers() {
    return userService.findAll();
}

@Get("/users/{id}")
public User getUser(@PathParam("id") String id) {
    return userService.findById(id);
}

@Post("/users")
public User createUser(@Body User user) {
    return userService.save(user);
}

// Not recommended: Non-RESTful style
@Get("/getUsers")
public List<User> getUsers() { ... }

@Post("/createUser")
public User createUser(@Body User) { ... }
```

### 2. Error Handling

```java
// Recommended: Unified error handling
@Get("/users/{id}")
public User getUser(@PathParam("id") String id) {
    User user = userService.findById(id);
    if (user == null) {
        throw new NotFoundException("User not found: " + id);
    }
    return user;
}

// Not recommended: Return null or error code directly
@Get("/users/{id}")
public User getUser(@PathParam("id") String id) {
    return userService.findById(id); // May return null
}
```

---

## Next Steps
- 🌱 Start with [BasicWebAppExample](src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java)
- 📖 Want to see more features? Check [Features Examples](../est-examples-features/)
- 📚 Want to learn more about Web module? Check [Web Module Docs](../../docs/modules/app/README.md)

---

**Documentation Version**: 2.0  
**Last Updated**: 2026-03-13  
**Maintainer**: EST Framework Team
