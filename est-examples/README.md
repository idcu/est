# EST Examples Module

Contains various usage examples of the EST Framework.

## Module Structure

```
est-examples/
├── est-examples-basic/          # Basic Examples
├── est-examples-web/            # Web Examples
├── est-examples-features/       # Features Examples
├── est-examples-advanced/       # Advanced Examples
├── est-examples-ai/             # AI Assistant Examples
├── est-examples-graalvm/        # GraalVM Examples
├── est-examples-microservices/  # Microservices Examples
└── pom.xml
```

## Example List

### Basic Examples (est-examples-basic)

- Dependency injection container usage
- Configuration management
- Collection operations
- Design pattern usage
- Utility class usage

### Web Examples (est-examples-web)

- Hello World Web application
- RESTful API
- Routing and filters
- Middleware usage
- Session management
- Template engine
- Todo application
- Chat room application
- Kanban application
- Calendar application
- File upload
- Admin management system example

### Features Examples (est-examples-features)

- Cache system
- Event bus
- Logging system
- Data access (JDBC, in-memory, MongoDB, Redis)
- Security authentication
- Scheduler system
- Monitor system
- Message system (ActiveMQ, Kafka, RabbitMQ, Redis, etc.)
- Circuit breaker
- Workflow engine

### Advanced Examples (est-examples-advanced)

- Plugin system
- Complete applications
- Performance optimization
- Testing practices
- Module integration
- Multi-module integration
- New architecture examples

### AI Assistant Examples (est-examples-ai)

- AI rapid development
- AI-assisted Web application
- Code generation examples
- Prompt template examples
- LLM integration examples

### GraalVM Examples (est-examples-graalvm)

- Hello World native application
- Web application native image

### Microservices Examples (est-examples-microservices)

- Microservices mesh
- User service
- Order service

### Admin Management System Examples (est-app/est-admin + est-admin-ui)

A complete frontend-backend separated management system, including:
- Backend RESTful API
- JWT Token authentication
- Vue 3 + Element Plus frontend
- User, role, menu, department, tenant management

For detailed documentation, please refer to: [EST Admin Frontend-Backend Integration Guide](../docs/guides/admin-integration.md)

## Run Examples

```bash
# Run basic examples
cd est-examples-basic
mvn exec:java

# Run Web examples
cd est-examples-web
mvn exec:java

# Run features examples
cd est-examples-features
./run-examples.bat

# Run AI examples
cd est-examples-ai
mvn exec:java

# Run Admin examples
# 1. Start backend
cd ../est-app/est-admin/est-admin-impl
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.admin.DefaultAdminApplication"

# 2. Start frontend (new terminal)
cd ../../../est-admin-ui
npm install
npm run dev
```

## Dependencies

The examples module depends on other modules of the EST Framework.

## Documentation

For more detailed documentation, please check:
- [Example Code Documentation](../docs/examples/README.md)
- [Quick Start](../docs/getting-started/README.md)
- [Admin Configuration Guide](../docs/guides/admin-integration.md)
