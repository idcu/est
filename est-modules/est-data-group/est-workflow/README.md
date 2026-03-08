# EST Workflow - 工作流编排系�?
## 📚 目录

- [快速入门](#快速入�?
- [基础篇](#基础�?
- [进阶篇](#进阶�?
- [最佳实践](#最佳实�?

---

## 🚀 快速入�?
### 什么是工作流系统？

想象一下，你有一条工厂生产线�?- 第一步：准备原材�?- 第二步：加工制�?- 第三步：质量检�?- 第四步：包装发货

**工作流系�?*就像程序的生产线，它可以�?- 按顺序执行多个任�?- 自动处理任务之间的传�?- 管理执行状态和错误
- 支持暂停、恢复、重�?
让程序自动化执行复杂流程，提高效率！

### 第一个例�?
让我们用 3 分钟写一个简单的工作流程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-workflow-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-workflow-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.core.Workflows;

public class WorkflowFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 工作流系统示�?===\n");
        
        // 1. 创建工作流引�?        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 2. 创建任务节点
        var node1 = Workflows.newTaskNode("step1", "准备数据", ctx -> {
            System.out.println("执行步骤1: 准备数据");
            ctx.setVariable("data", "Hello, Workflow!");
        });
        
        var node2 = Workflows.newTaskNode("step2", "处理数据", ctx -> {
            System.out.println("执行步骤2: 处理数据");
            String data = ctx.getVariable("data", String.class).orElse("");
            ctx.setVariable("result", data.toUpperCase());
        });
        
        var node3 = Workflows.newTaskNode("step3", "输出结果", ctx -> {
            System.out.println("执行步骤3: 输出结果");
            String result = ctx.getVariable("result", String.class).orElse("");
            System.out.println("最终结�? " + result);
        });
        
        // 3. 构建工作流定�?        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("my-first-workflow")
                .name("我的第一个工作流")
                .description("一个简单的示例工作�?)
                .startNode(node1)
                .addNode(node2)
                .endNode(node3)
                .build();
        
        // 4. 注册并启动工作流
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("my-first-workflow");
        
        // 5. 查看执行结果
        System.out.println("\n工作流执行完成！");
        System.out.println("状�? " + instance.getStatus());
        System.out.println("耗时: " + instance.getDuration() + "ms");
        
        engine.shutdown();
    }
}
```

运行这个程序，你会看到工作流按顺序执行三个步骤！

🎉 恭喜你！你已经学会了使用工作流系统！

---

## 📖 基础�?
### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **工作流定义（WorkflowDefinition�?* | 工作流的蓝图 | 生产流程�?|
| **工作流实例（WorkflowInstance�?* | 一次具体的执行 | 一次生产过�?|
| **节点（Node�?* | 工作流中的单个任�?| 生产线上的工�?|
| **上下文（WorkflowContext�?* | 节点间传递的数据 | 传送带上的产品 |
| **工作流引擎（WorkflowEngine�?* | 管理和执行工作流 | 生产线管理员 |

### 2. 创建任务节点

```java
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.core.Workflows;

public class TaskNodeExample {
    public static void main(String[] args) {
        // 创建一个简单的任务节点
        var taskNode = Workflows.newTaskNode("task1", "我的任务", ctx -> {
            System.out.println("执行任务...");
            // 在上下文中设置变�?            ctx.setVariable("key", "value");
        });
    }
}
```

### 3. 使用工作流上下文

```java
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.core.Workflows;

public class ContextExample {
    public static void main(String[] args) {
        var node1 = Workflows.newTaskNode("step1", "设置数据", ctx -> {
            // 设置变量
            ctx.setVariable("username", "张三");
            ctx.setVariable("age", 25);
        });
        
        var node2 = Workflows.newTaskNode("step2", "使用数据", ctx -> {
            // 获取变量
            String username = ctx.getVariable("username", String.class).orElse("未知");
            Integer age = ctx.getVariable("age", Integer.class).orElse(0);
            
            System.out.println("用户�? " + username);
            System.out.println("年龄: " + age);
        });
    }
}
```

---

## 🔧 进阶�?
### 1. 工作流监听器

```java
import ltd.idcu.est.workflow.api.WorkflowListener;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class WorkflowListenerExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 添加工作流监听器
        engine.addWorkflowListener(new WorkflowListener() {
            @Override
            public void onWorkflowStart(WorkflowInstance instance) {
                System.out.println("工作流开�? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowComplete(WorkflowInstance instance) {
                System.out.println("工作流完�? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowFail(WorkflowInstance instance, Throwable error) {
                System.out.println("工作流失�? " + instance.getInstanceId());
                System.out.println("错误: " + error.getMessage());
            }
            
            @Override
            public void onWorkflowPause(WorkflowInstance instance) {
                System.out.println("工作流暂�? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowResume(WorkflowInstance instance) {
                System.out.println("工作流恢�? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowCancel(WorkflowInstance instance) {
                System.out.println("工作流取�? " + instance.getInstanceId());
            }
        });
    }
}
```

### 2. 节点监听�?
```java
import ltd.idcu.est.workflow.api.NodeListener;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class NodeListenerExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 添加节点监听�?        engine.addNodeListener(new NodeListener() {
            @Override
            public void onNodeStart(WorkflowInstance instance, Node node) {
                System.out.println("节点开�? " + node.getName());
            }
            
            @Override
            public void onNodeComplete(WorkflowInstance instance, Node node) {
                System.out.println("节点完成: " + node.getName());
            }
            
            @Override
            public void onNodeFail(WorkflowInstance instance, Node node, Throwable error) {
                System.out.println("节点失败: " + node.getName());
                System.out.println("错误: " + error.getMessage());
            }
            
            @Override
            public void onNodeSkip(WorkflowInstance instance, Node node) {
                System.out.println("节点跳过: " + node.getName());
            }
        });
    }
}
```

### 3. 异步执行工作�?
```java
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.core.Workflows;

import java.util.concurrent.CompletableFuture;

public class AsyncWorkflowExample {
    public static void main(String[] args) throws Exception {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 假设已注册工作流...
        
        // 异步启动工作�?        CompletableFuture<WorkflowInstance> future = 
                engine.startWorkflowAsync("my-workflow");
        
        // 做其他事�?..
        
        // 等待结果
        WorkflowInstance instance = future.get();
        System.out.println("工作流状�? " + instance.getStatus());
        
        engine.shutdown();
    }
}
```

---

## 💡 最佳实�?
### 1. 合理使用工作流上下文

```java
// �?推荐：只在上下文中存储必要的数据
var node = Workflows.newTaskNode("task", "任务", ctx -> {
    ctx.setVariable("userId", 123);
    ctx.setVariable("userName", "张三");
});

// �?不推荐：存储过大或不需要的数据
var badNode = Workflows.newTaskNode("task", "任务", ctx -> {
    // 不要存储整个大对�?    // ctx.setVariable("hugeObject", hugeObject);
});
```

### 2. 错误处理

```java
var node = Workflows.newTaskNode("task", "可能失败的任�?, ctx -> {
    try {
        // 执行可能失败的操�?        riskyOperation();
    } catch (Exception e) {
        // 记录错误信息到上下文
        ctx.setVariable("error", e.getMessage());
        throw e; // 重新抛出以让工作流引擎处�?    }
});
```

### 3. 优雅关闭引擎

```java
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class GracefulShutdownExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        try {
            // 使用工作流引�?..
        } finally {
            // 确保关闭引擎
            engine.shutdown();
            System.out.println("工作流引擎已关闭�?);
        }
    }
}
```

---

---

## 🚀 高级特�?
### 1. 网关节点

EST Workflow 支持三种类型的网关：

#### 排他网关（Exclusive Gateway�?
```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class ExclusiveGatewayExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "开�?, ctx -> {
            ctx.setVariable("amount", 1000);
        });
        
        var gateway = Workflows.newExclusiveGateway("gateway", "金额判断");
        
        var highPath = Workflows.newTaskNode("high", "高额处理", ctx -> {
            System.out.println("处理高额订单");
        });
        
        var lowPath = Workflows.newTaskNode("low", "低额处理", ctx -> {
            System.out.println("处理低额订单");
        });
        
        var endNode = Workflows.newTaskNode("end", "结束", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("gateway-example")
                .name("网关示例")
                .startNode(startNode)
                .addNode(gateway)
                .addNode(highPath)
                .addNode(lowPath)
                .endNode(endNode)
                .connect("start", "gateway")
                .connect("gateway", "high", "高额", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) > 500)
                .connect("gateway", "low", "低额", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) <= 500)
                .connect("high", "end")
                .connect("low", "end")
                .build();
        
        engine.registerWorkflow(workflow);
        engine.startWorkflow("gateway-example");
        engine.shutdown();
    }
}
```

#### 并行网关（Parallel Gateway）
```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class ParallelGatewayExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "开始", ctx -> {
            System.out.println("开始并行处理");
        });
        
        var splitGateway = Workflows.newParallelGateway("split", "分支网关");
        
        var task1 = Workflows.newTaskNode("task1", "任务1", ctx -> {
            System.out.println("执行任务1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        var task2 = Workflows.newTaskNode("task2", "任务2", ctx -> {
            System.out.println("执行任务2");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        var task3 = Workflows.newTaskNode("task3", "任务3", ctx -> {
            System.out.println("执行任务3");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        var joinGateway = Workflows.newParallelGateway("join", "汇合网关");
        
        var endNode = Workflows.newTaskNode("end", "结束", ctx -> {
            System.out.println("所有任务完成");
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("parallel-workflow")
                .name("并行网关示例")
                .startNode(startNode)
                .connect("start", "split")
                .connect("split", "task1")
                .connect("split", "task2")
                .connect("split", "task3")
                .connect("task1", "join")
                .connect("task2", "join")
                .connect("task3", "join")
                .connect("join", "end")
                .build();
        
        engine.startWorkflow(workflow);
        engine.shutdown();
    }
}
```

#### 包容网关（Inclusive Gateway）
```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class InclusiveGatewayExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "开始", ctx -> {
            ctx.setVariable("score", 85);
        });
        
        var gateway = Workflows.newInclusiveGateway("gateway", "评分判断");
        
        var excelPath = Workflows.newTaskNode("excel", "优秀处理", ctx -> {
            System.out.println("处理优秀案例");
        });
        
        var goodPath = Workflows.newTaskNode("good", "良好处理", ctx -> {
            System.out.println("处理良好案例");
        });
        
        var passPath = Workflows.newTaskNode("pass", "及格处理", ctx -> {
            System.out.println("处理及格案例");
        });
        
        var endNode = Workflows.newTaskNode("end", "结束", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("inclusive-workflow")
                .name("包容网关示例")
                .startNode(startNode)
                .connect("start", "gateway")
                .connect("gateway", "excel", "优秀", ctx -> 
                    ctx.getVariable("score", Integer.class).orElse(0) >= 90)
                .connect("gateway", "good", "良好", ctx -> 
                    ctx.getVariable("score", Integer.class).orElse(0) >= 80)
                .connect("gateway", "pass", "及格", ctx -> 
                    ctx.getVariable("score", Integer.class).orElse(0) >= 60)
                .connect("excel", "end")
                .connect("good", "end")
                .connect("pass", "end")
                .build();
        
        engine.startWorkflow(workflow);
        engine.shutdown();
    }
}
```

### 2. 工作流持久化

使用内存持久化仓库：

```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowRepository;
import ltd.idcu.est.workflow.core.Workflows;

import java.util.Optional;

public class PersistenceExample {
    public static void main(String[] args) {
        WorkflowRepository repository = Workflows.newMemoryRepository();
        WorkflowEngine engine = Workflows.newWorkflowEngine(repository);
        
        var startNode = Workflows.newTaskNode("start", "开始", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("persistent-workflow")
                .name("持久化工作流")
                .startNode(startNode)
                .build();
        
        repository.saveDefinition(workflow);
        
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Optional<WorkflowInstance> foundInstance = 
                repository.findInstanceById(instance.getInstanceId());
        
        if (foundInstance.isPresent()) {
            System.out.println("找到工作流实例: " + foundInstance.get().getInstanceId());
            System.out.println("状态: " + foundInstance.get().getStatus());
        }
        
        var allInstances = repository.findInstancesByDefinitionId("persistent-workflow");
        System.out.println("该定义的所有实例数: " + allInstances.size());
        
        engine.shutdown();
    }
}
```

### 3. JSON 流程定义

```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.core.Workflows;

public class JsonDefinitionExample {
    public static void main(String[] args) {
        WorkflowDefinitionParser parser = Workflows.newJsonParser();
        
        // �?JSON 解析工作流定�?        String json = "{\n" +
                "  \"id\": \"json-workflow\",\n" +
                "  \"name\": \"JSON 定义的工作流\",\n" +
                "  \"nodes\": [\n" +
                "    {\"id\": \"task1\", \"name\": \"任务1\", \"type\": \"TASK\"},\n" +
                "    {\"id\": \"task2\", \"name\": \"任务2\", \"type\": \"TASK\"}\n" +
                "  ],\n" +
                "  \"startNode\": \"task1\",\n" +
                "  \"endNode\": \"task2\"\n" +
                "}";
        
        WorkflowDefinition workflow = parser.parse(json);
        
        // 将工作流定义序列化为 JSON
        String jsonOutput = parser.serialize(workflow);
        System.out.println(jsonOutput);
    }
}
```

### 4. 定时触发工作�?
�?est-scheduler 集成�?
```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;
import ltd.idcu.est.workflow.core.integration.ScheduledWorkflowTrigger;

import java.util.concurrent.TimeUnit;

public class ScheduledWorkflowExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        Scheduler scheduler = ...; // �?EST 框架获取调度�?        
        ScheduledWorkflowTrigger trigger = Workflows.newScheduledTrigger(engine, scheduler);
        
        // 每隔 1 小时执行一次工作流
        String taskId = trigger.scheduleAtFixedRate(
                "my-workflow", 
                0, 
                1, 
                TimeUnit.HOURS);
        
        // 取消定时任务
        // trigger.cancelSchedule(taskId);
    }
}
```

### 5. 事件驱动工作�?
�?est-event 集成�?
```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;
import ltd.idcu.est.workflow.core.integration.EventDrivenWorkflowTrigger;

public class EventDrivenWorkflowExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        EventBus eventBus = ...; // �?EST 框架获取事件总线
        
        EventDrivenWorkflowTrigger trigger = Workflows.newEventDrivenTrigger(engine, eventBus);
        
        // �?"order.created" 事件发生时，触发工作�?        trigger.registerEventTrigger("order.created", "order-processing-workflow");
        
        // 带自定义载荷提取�?        trigger.registerEventTrigger("payment.received", "payment-workflow", event -> {
            Map<String, Object> variables = new HashMap<>();
            variables.put("paymentId", event);
            return variables;
        });
        
        // 取消事件触发�?        // trigger.unregisterEventTrigger("order.created");
    }
}
```

---

## 🎯 总结

工作流系统就像程序的"智能生产�?，让程序自动化执行复杂流程，提高效率�?
EST Workflow 现在支持�?- �?顺序工作流执�?- �?网关节点（排他、并行、包容）
- �?工作流持久化
- �?JSON 流程定义
- �?�?est-scheduler 集成（定时触发）
- �?�?est-event 集成（事件驱动）

下一章，我们将学习更多高级特性！🎉
