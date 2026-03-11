# EST Agent - 智能体模块

**版本**: 3.0.0  
**状态**: ✅ 开发中

---

## 📋 目录

1. [简介](#简介)
2. [快速开始](#快速开始)
3. [核心组件](#核心组件)
4. [API 参考](#api-参考)
5. [示例代码](#示例代码)

---

## 🎯 简介

EST Agent 是 EST AI Suite 的智能体（Agent）模块，提供了完整的技能体系、记忆系统和多步推理能力。

### 主要特性

- 🛠️ **技能体系** - 支持动态技能注册和执行
- 🧠 **记忆系统** - 短期记忆和长期记忆支持
- 🔄 **多步推理** - 自动规划和执行多步任务
- 🔌 **可扩展** - 轻松添加自定义技能和记忆实现
- 📊 **可观测** - 完整的执行步骤追踪和记录

---

## 🚀 快速开始

### 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-agent-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-agent-impl</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 最简单的用法

```java
import ltd.idcu.est.agent.api.*;
import ltd.idcu.est.agent.impl.*;
import ltd.idcu.est.agent.skill.*;

public class AgentQuickStart {
    public static void main(String[] args) {
        Agent agent = new DefaultAgent();
        
        agent.registerSkill(new WebSearchSkill());
        agent.registerSkill(new CalculatorSkill());
        
        AgentRequest request = new AgentRequest();
        request.setQuery("计算 100 的平方根，然后加上 25");
        
        AgentResponse response = agent.process(request);
        System.out.println("响应: " + response.getFinalAnswer());
    }
}
```

### 完整配置

```java
import ltd.idcu.est.agent.api.*;
import ltd.idcu.est.agent.impl.*;
import ltd.idcu.est.agent.skill.*;

public class AgentFullExample {
    public static void main(String[] args) {
        Memory memory = new InMemoryMemory();
        Agent agent = new DefaultAgent(memory);
        
        agent.registerSkill(new WebSearchSkill());
        agent.registerSkill(new CalculatorSkill());
        
        AgentRequest request = new AgentRequest();
        request.setQuery("搜索北京的天气，然后计算气温的平均值");
        request.setMaxSteps(5);
        
        AgentResponse response = agent.process(request);
        
        System.out.println("最终答案: " + response.getFinalAnswer());
        System.out.println("执行步骤: " + response.getSteps().size());
        
        for (AgentResponse.AgentStep step : response.getSteps()) {
            System.out.println("步骤 " + step.getStepNumber() + ": " + step.getAction());
        }
    }
}
```

---

## 🏗️ 核心组件

### 1. Agent（智能体）

核心智能体引擎，负责任务分解、技能选择和执行。

```java
Agent agent = new DefaultAgent();
agent.registerSkill(new MySkill());

AgentRequest request = new AgentRequest();
request.setQuery("你的问题");

AgentResponse response = agent.process(request);
```

### 2. Skill（技能）

可重用的能力单元，具有完整的生命周期。

#### 创建自定义技能

```java
public class MyCustomSkill implements Skill {
    
    @Override
    public String getName() {
        return "my_skill";
    }
    
    @Override
    public String getDescription() {
        return "我的自定义技能描述";
    }
    
    @Override
    public void initialize(SkillContext context) {
    }
    
    @Override
    public SkillResult execute(SkillInput input, SkillContext context) {
        String param = input.getParameter("param", String.class);
        return SkillResult.success("执行结果: " + param);
    }
    
    @Override
    public void cleanup(SkillContext context) {
    }
}
```

### 3. Memory（记忆系统）

存储对话历史、中间结果和重要信息。

#### InMemoryMemory（内存记忆）

```java
Memory memory = new InMemoryMemory();

MemoryItem item = new MemoryItem();
item.setType(MemoryItem.MemoryType.CONVERSATION);
item.setContent("用户: 你好");
memory.add(item);

List<MemoryItem> items = memory.getAll();
```

### 4. AgentRequest / AgentResponse

请求和响应模型。

```java
AgentRequest request = new AgentRequest();
request.setQuery("问题");
request.setMaxSteps(10);
request.setContext(Map.of("key", "value"));

AgentResponse response = agent.process(request);
String answer = response.getFinalAnswer();
List<AgentResponse.AgentStep> steps = response.getSteps();
```

---

## 📚 API 参考

### Agent 接口

```java
public interface Agent {
    void registerSkill(Skill skill);
    void unregisterSkill(String skillName);
    Skill getSkill(String skillName);
    List<Skill> getAllSkills();
    
    void setMemory(Memory memory);
    Memory getMemory();
    
    AgentResponse process(AgentRequest request);
}
```

### Skill 接口

```java
public interface Skill {
    String getName();
    String getDescription();
    
    void initialize(SkillContext context);
    SkillResult execute(SkillInput input, SkillContext context);
    void cleanup(SkillContext context);
}
```

### Memory 接口

```java
public interface Memory {
    void add(MemoryItem item);
    void addAll(List<MemoryItem> items);
    List<MemoryItem> getAll();
    List<MemoryItem> getByType(MemoryItem.MemoryType type);
    List<MemoryItem> getRecent(int limit);
    
    void clear();
    void remove(String id);
    int size();
}
```

---

## 💡 示例代码

### 创建自定义技能

```java
public class WeatherSkill implements Skill {
    
    @Override
    public String getName() {
        return "weather";
    }
    
    @Override
    public String getDescription() {
        return "查询指定城市的天气";
    }
    
    @Override
    public void initialize(SkillContext context) {
        System.out.println("WeatherSkill 初始化");
    }
    
    @Override
    public SkillResult execute(SkillInput input, SkillContext context) {
        String city = input.getParameter("city", String.class, "北京");
        
        String weatherInfo = String.format("%s 的天气: 晴朗, 25°C", city);
        return SkillResult.success(weatherInfo);
    }
    
    @Override
    public void cleanup(SkillContext context) {
        System.out.println("WeatherSkill 清理");
    }
}
```

### 使用记忆系统

```java
Memory memory = new InMemoryMemory();
Agent agent = new DefaultAgent(memory);

AgentRequest request1 = new AgentRequest();
request1.setQuery("我叫张三");
agent.process(request1);

AgentRequest request2 = new AgentRequest();
request2.setQuery("我叫什么名字？");
AgentResponse response = agent.process(request2);

List<MemoryItem> conversation = memory.getByType(MemoryItem.MemoryType.CONVERSATION);
for (MemoryItem item : conversation) {
    System.out.println(item.getContent());
}
```

### 多步任务执行

```java
Agent agent = new DefaultAgent();
agent.registerSkill(new CalculatorSkill());
agent.registerSkill(new WebSearchSkill());

AgentRequest request = new AgentRequest();
request.setQuery("搜索中国的人口数，然后除以 14 亿，计算百分比");
request.setMaxSteps(10);

AgentResponse response = agent.process(request);

System.out.println("=== 执行过程 ===");
for (AgentResponse.AgentStep step : response.getSteps()) {
    System.out.printf("[步骤 %d] %s: %s%n", 
        step.getStepNumber(), 
        step.getAction(), 
        step.getObservation()
    );
}

System.out.println("\n=== 最终答案 ===");
System.out.println(response.getFinalAnswer());
```

---

## 📖 相关文档

- [EST AI Suite README](../README.md) - AI Suite 总览
- [EST RAG README](../est-rag/README.md) - RAG 模块文档
- [EST MCP README](../est-mcp/README.md) - MCP 模块文档

---

**文档生成时间**: 2026-03-10  
**文档作者**: EST AI Team
