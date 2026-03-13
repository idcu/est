# EST AI Examples

Welcome to the EST AI Examples module! This module demonstrates how to use the AI features of the EST Framework.

---

## Table of Contents
1. [What is EST AI Examples?](#what-is-est-ai-examples)
2. [Quick Start](#quick-start)
3. [Example List](#example-list)
4. [How to Run Examples](#how-to-run-examples)
5. [Best Practices](#best-practices)
6. [Next Steps](#next-steps)

---

## What is EST AI Examples?
### In Simple Terms

EST AI Examples are like an AI toolkit. Imagine you want to add AI features to your application:

**Traditional approach**: Integrate various AI APIs yourself, handle authentication, retries, error handling - that's a lot of work.

**EST AI Examples**: Gives you ready-to-use tools with quick start, chat assistant, code generation, prompt templates, etc., that you can use directly.

It includes various usages of the EST AI module:
- AI features quick start
- AI assistant web app
- Code generation examples
- Prompt template usage
- Integration with other modules

### Core Features

- [X] **Simple to Use** - Use AI with just a few lines of code
- [X] **Out-of-the-Box** - Pre-configured common features and templates
- [X] **Rich Scenarios** - Covers various AI application scenarios
- [X] **Flexible Extension** - Can customize prompts and workflows
- [X] **Best Practices** - Demonstrates AI application best practices

---

## Quick Start
### Prerequisites

- [X] Completed learning of [Basic Examples](../est-examples-basic/)
- [X] Understand EST core concepts
- [X] JDK 21+ and Maven 3.6+

### Run Your First Example
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

---

## Example List

| Example | Description | Knowledge Points | Difficulty |
|---------|-------------|------------------|------------|
| [Main](src/main/java/ltd/idcu/est/examples/ai/Main.java) | AI Examples Main Entry | Example navigation | - |
| [AiQuickStartExample](src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java) | AI Quick Start | AI basics | ⭐ |
| [ComprehensiveAiExample](src/main/java/ltd/idcu/est/examples/ai/ComprehensiveAiExample.java) | Comprehensive AI Example | All AI features | ⭐⭐ |
| [StorageExample](src/main/java/ltd/idcu/est/examples/ai/StorageExample.java) | Storage System Example | Memory/file storage, template/Skill persistence | ⭐⭐ |
| [ConfigExample](src/main/java/ltd/idcu/est/examples/ai/ConfigExample.java) | Config Management Example | YAML/environment variables, LLM config | ⭐⭐ |
| [AiAssistantWebExample](src/main/java/ltd/idcu/est/examples/ai/AiAssistantWebExample.java) | AI Assistant Web App | AI + Web | ⭐⭐ |
| [CodeGeneratorExample](src/main/java/ltd/idcu/est/examples/ai/CodeGeneratorExample.java) | Code Generator Example | Code generation | ⭐⭐ |
| [PromptTemplateExample](src/main/java/ltd/idcu/est/examples/ai/PromptTemplateExample.java) | Prompt Template Example | Prompt tools | ⭐⭐ |
| [LlmIntegrationExample](src/main/java/ltd/idcu/est/examples/ai/LlmIntegrationExample.java) | LLM Integration Example | Multi-provider LLM | ⭐⭐⭐ |
| [AdvancedAiExample](src/main/java/ltd/idcu/est/examples/ai/AdvancedAiExample.java) | Advanced AI Example | Skill/MCP/LLM | ⭐⭐⭐ |
| [MidTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/MidTermFeaturesExample.java) | Mid-Term Features Demo | Refactoring assistant, architecture consultation | ⭐⭐⭐ |
| [LongTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/LongTermFeaturesExample.java) | Long-Term Goal Features Demo | Requirements analysis, architecture design, testing/deployment | ⭐⭐⭐ |

---

## How to Run Examples

### Run Main - See All Examples (Recommended First)
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.Main"
```

### Run AiQuickStartExample - AI Quick Start
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

### Run ComprehensiveAiExample - Comprehensive AI Example

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.ComprehensiveAiExample"
```

### Run StorageExample - Storage System Example

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.StorageExample"
```

### Run ConfigExample - Config Management Example

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.ConfigExample"
```

### Run AiWebAssistantExample - AI Web Assistant
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiWebAssistantExample"
```

Then visit: http://localhost:8080

This is a complete web interface that provides:
- Quick reference lookup
- Best practice acquisition
- Tutorial learning
- Code suggestions
- Code explanations
- Code optimization
- Entity/Service/Controller generation
- pom.xml generation

### Run MidtermFeaturesExample - Mid-Term Features Demo
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.MidTermFeaturesExample"
```

Demo content:
- Refactoring assistant - Auto-identify refactoring opportunities
- Architecture consultation - Provide architecture design and optimization suggestions
- Intelligent code completion - Context-aware code suggestions
- LLM client abstraction layer - Unified LLM integration interface

### Run LongTermFeaturesExample - Long-Term Goal Features Demo
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.LongTermFeaturesExample"
```

Demo content:
- Requirements analyzer - Parse natural language requirements, extract components, assess complexity
- Architecture designer - Design system architecture, recommend architecture patterns, validate architecture design
- Test and deployment manager - Generate test cases, create deployment plans, run tests, execute deployment
- Complete workflow - Full workflow demo from requirements to deployment

---

## Best Practices

### 1. Prompt Engineering
```java
// Recommended: Clear, specific prompts
String prompt = """
    You are a Java code review expert.
    Please review the following code, point out issues and provide improvement suggestions.
    
    Code:
    ${code}
    
    Please output in the following format:
    1. Issue list
    2. Improvement suggestions
    3. Optimized code
    """;

// Not recommended: Vague prompts
String prompt = "Help me look at this code";
```

### 2. Using AI Assistant
```java
// Create AI assistant
AiAssistant assistant = new DefaultAiAssistant();

// Get quick reference
String webRef = assistant.getQuickReference("web development");

// Get best practice
String bestPractice = assistant.getBestPractice("code style");

// Get tutorial
String tutorial = assistant.getTutorial("first application");

// Suggest code
String code = assistant.suggestCode("Create a UserService");

// Explain code
String explanation = assistant.explainCode(code);

// Optimize code
String optimized = assistant.optimizeCode(code);
```

### 3. Using Code Generator
```java
CodeGenerator generator = new DefaultCodeGenerator();

// Generate Entity
String entity = generator.generateEntity("Product", "com.example.entity",
    Map.of("fields", List.of("id:Long", "name:String", "price:BigDecimal")));

// Generate Repository
String repo = generator.generateRepository("ProductRepository", "com.example.repository", Map.of());

// Generate Service
String service = generator.generateService("ProductService", "com.example.service", Map.of());

// Generate pom.xml
String pom = generator.generatePomXml("MyProject", "com.example", "my-app", "1.0.0");
```

---

## Next Steps
- 🌱 Start with [AiQuickStartExample](src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java)
- 🔍 Explore [AdvancedAiExample](src/main/java/ltd/idcu/est/examples/ai/AdvancedAiExample.java) to learn all features
- 🎭 Experience [AiAssistantWebExample](src/main/java/ltd/idcu/est/examples/ai/AiAssistantWebExample.java) web interface
- ⏭️ Check mid-term features [MidTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/MidTermFeaturesExample.java)
- 🌱 Experience long-term goals [LongTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/LongTermFeaturesExample.java)
- 📖 Want to do Web apps? Check [Web Examples](../est-examples-web/)
- 📚 Want to learn more about AI module? Check [AI Module Docs](../../est-modules/est-ai/README.md)
- 🎯 Deep learning? Check [AI Developer Specialization](../../docs/ai/README.md)

---

**Documentation Version**: 2.0  
**Last Updated**: 2026-03-13  
**Maintainer**: EST Framework Team
