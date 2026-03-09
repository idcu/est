# Architecture Overview

## Layered Architecture

EST Framework uses a clean, six-layer architecture:

### 1. est-base (鍩虹灞?
The foundation layer providing basic utilities, collections, patterns, and test support.

**Modules:**
- est-utils: Common utilities (common, io, format)
- est-collection: Enhanced collection utilities
- est-patterns: Design pattern implementations
- est-test: Testing support

### 2. est-core (鏍稿績灞?
The core framework capabilities including DI container, configuration, lifecycle management, AOP, and transactions.

**Modules:**
- est-core-container: Dependency Injection container
- est-core-config: Core configuration
- est-core-lifecycle: Lifecycle management
- est-core-module: Module management
- est-core-aop: AOP support
- est-core-tx: Transaction management

### 3. est-modules (妯″潡�?
Feature modules organized into functional groups.

**Groups:**
- **est-foundation**: Infrastructure modules (cache, event, logging, config, monitor, tracing)
- **est-data-group**: Data access (data, workflow)
- **est-security-group**: Security and permissions (security, rbac, audit)
- **est-integration-group**: Messaging and integration (messaging, integration)
- **est-web-group**: Web framework components (router, middleware, session, template, gateway)
- **est-ai-suite**: AI and LLM modules (ai-config, llm-core, llm, ai-assistant)
- **est-microservices**: Microservices support (discovery, circuitbreaker, performance)
- **est-extensions**: Additional features (scheduler, plugin, hotreload)

### 4. est-app (搴旂敤灞?
Application frameworks that assemble modules into usable applications.

**Modules:**
- est-web: Web application framework
- est-admin: Administration console
- est-console: Console application framework

### 5. est-tools (宸ュ叿灞?
Development tools and utilities.

**Modules:**
- est-scaffold: Project scaffolding
- est-codegen: Code generation
- est-migration: Migration tools
- est-cli: Command-line interface

### 6. est-examples (绀轰緥�?
Example applications demonstrating framework usage.

## Key Design Principles

1. **Zero Dependencies**: Core framework has no external dependencies
2. **API/Impl Separation**: Clear separation between interfaces and implementations
3. **Progressive Usage**: Use only what you need
4. **AI-Friendly**: Designed with AI assistance in mind
