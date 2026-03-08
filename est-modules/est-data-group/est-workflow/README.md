# EST Workflow - 宸ヤ綔娴佺紪鎺掔郴锟?
## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆锟?
- [鍩虹绡嘳(#鍩虹锟?
- [杩涢樁绡嘳(#杩涢樁锟?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄锟?

---

## 馃殌 蹇€熷叆锟?
### 浠€涔堟槸宸ヤ綔娴佺郴缁燂紵

鎯宠薄涓€涓嬶紝浣犳湁涓€鏉″伐鍘傜敓浜х嚎锟?- 绗竴姝ワ細鍑嗗鍘熸潗锟?- 绗簩姝ワ細鍔犲伐鍒讹拷?- 绗笁姝ワ細璐ㄩ噺妫€锟?- 绗洓姝ワ細鍖呰鍙戣揣

**宸ヤ綔娴佺郴锟?*灏卞儚绋嬪簭鐨勭敓浜х嚎锛屽畠鍙互锟?- 鎸夐『搴忔墽琛屽涓换锟?- 鑷姩澶勭悊浠诲姟涔嬮棿鐨勪紶锟?- 绠＄悊鎵ц鐘舵€佸拰閿欒
- 鏀寔鏆傚仠銆佹仮澶嶃€侀噸锟?
璁╃▼搴忚嚜鍔ㄥ寲鎵ц澶嶆潅娴佺▼锛屾彁楂樻晥鐜囷紒

### 绗竴涓緥锟?
璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑宸ヤ綔娴佺▼搴忥紒

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-workflow-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-workflow-core</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.core.Workflows;

public class WorkflowFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 宸ヤ綔娴佺郴缁熺ず锟?===\n");
        
        // 1. 鍒涘缓宸ヤ綔娴佸紩锟?        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 2. 鍒涘缓浠诲姟鑺傜偣
        var node1 = Workflows.newTaskNode("step1", "鍑嗗鏁版嵁", ctx -> {
            System.out.println("鎵ц姝ラ1: 鍑嗗鏁版嵁");
            ctx.setVariable("data", "Hello, Workflow!");
        });
        
        var node2 = Workflows.newTaskNode("step2", "澶勭悊鏁版嵁", ctx -> {
            System.out.println("鎵ц姝ラ2: 澶勭悊鏁版嵁");
            String data = ctx.getVariable("data", String.class).orElse("");
            ctx.setVariable("result", data.toUpperCase());
        });
        
        var node3 = Workflows.newTaskNode("step3", "杈撳嚭缁撴灉", ctx -> {
            System.out.println("鎵ц姝ラ3: 杈撳嚭缁撴灉");
            String result = ctx.getVariable("result", String.class).orElse("");
            System.out.println("鏈€缁堢粨锟? " + result);
        });
        
        // 3. 鏋勫缓宸ヤ綔娴佸畾锟?        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("my-first-workflow")
                .name("鎴戠殑绗竴涓伐浣滄祦")
                .description("涓€涓畝鍗曠殑绀轰緥宸ヤ綔锟?)
                .startNode(node1)
                .addNode(node2)
                .endNode(node3)
                .build();
        
        // 4. 娉ㄥ唽骞跺惎鍔ㄥ伐浣滄祦
        engine.registerWorkflow(workflow);
        WorkflowInstance instance = engine.startWorkflow("my-first-workflow");
        
        // 5. 鏌ョ湅鎵ц缁撴灉
        System.out.println("\n宸ヤ綔娴佹墽琛屽畬鎴愶紒");
        System.out.println("鐘讹拷? " + instance.getStatus());
        System.out.println("鑰楁椂: " + instance.getDuration() + "ms");
        
        engine.shutdown();
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒板伐浣滄祦鎸夐『搴忔墽琛屼笁涓楠わ紒

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤宸ヤ綔娴佺郴缁燂紒

---

## 馃摉 鍩虹锟?
### 1. 鏍稿績姒傚康

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **宸ヤ綔娴佸畾涔夛紙WorkflowDefinition锟?* | 宸ヤ綔娴佺殑钃濆浘 | 鐢熶骇娴佺▼锟?|
| **宸ヤ綔娴佸疄渚嬶紙WorkflowInstance锟?* | 涓€娆″叿浣撶殑鎵ц | 涓€娆＄敓浜ц繃锟?|
| **鑺傜偣锛圢ode锟?* | 宸ヤ綔娴佷腑鐨勫崟涓换锟?| 鐢熶骇绾夸笂鐨勫伐锟?|
| **涓婁笅鏂囷紙WorkflowContext锟?* | 鑺傜偣闂翠紶閫掔殑鏁版嵁 | 浼犻€佸甫涓婄殑浜у搧 |
| **宸ヤ綔娴佸紩鎿庯紙WorkflowEngine锟?* | 绠＄悊鍜屾墽琛屽伐浣滄祦 | 鐢熶骇绾跨鐞嗗憳 |

### 2. 鍒涘缓浠诲姟鑺傜偣

```java
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.core.Workflows;

public class TaskNodeExample {
    public static void main(String[] args) {
        // 鍒涘缓涓€涓畝鍗曠殑浠诲姟鑺傜偣
        var taskNode = Workflows.newTaskNode("task1", "鎴戠殑浠诲姟", ctx -> {
            System.out.println("鎵ц浠诲姟...");
            // 鍦ㄤ笂涓嬫枃涓缃彉锟?            ctx.setVariable("key", "value");
        });
    }
}
```

### 3. 浣跨敤宸ヤ綔娴佷笂涓嬫枃

```java
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.core.Workflows;

public class ContextExample {
    public static void main(String[] args) {
        var node1 = Workflows.newTaskNode("step1", "璁剧疆鏁版嵁", ctx -> {
            // 璁剧疆鍙橀噺
            ctx.setVariable("username", "寮犱笁");
            ctx.setVariable("age", 25);
        });
        
        var node2 = Workflows.newTaskNode("step2", "浣跨敤鏁版嵁", ctx -> {
            // 鑾峰彇鍙橀噺
            String username = ctx.getVariable("username", String.class).orElse("鏈煡");
            Integer age = ctx.getVariable("age", Integer.class).orElse(0);
            
            System.out.println("鐢ㄦ埛锟? " + username);
            System.out.println("骞撮緞: " + age);
        });
    }
}
```

---

## 馃敡 杩涢樁锟?
### 1. 宸ヤ綔娴佺洃鍚櫒

```java
import ltd.idcu.est.workflow.api.WorkflowListener;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class WorkflowListenerExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 娣诲姞宸ヤ綔娴佺洃鍚櫒
        engine.addWorkflowListener(new WorkflowListener() {
            @Override
            public void onWorkflowStart(WorkflowInstance instance) {
                System.out.println("宸ヤ綔娴佸紑锟? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowComplete(WorkflowInstance instance) {
                System.out.println("宸ヤ綔娴佸畬锟? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowFail(WorkflowInstance instance, Throwable error) {
                System.out.println("宸ヤ綔娴佸け锟? " + instance.getInstanceId());
                System.out.println("閿欒: " + error.getMessage());
            }
            
            @Override
            public void onWorkflowPause(WorkflowInstance instance) {
                System.out.println("宸ヤ綔娴佹殏锟? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowResume(WorkflowInstance instance) {
                System.out.println("宸ヤ綔娴佹仮锟? " + instance.getInstanceId());
            }
            
            @Override
            public void onWorkflowCancel(WorkflowInstance instance) {
                System.out.println("宸ヤ綔娴佸彇锟? " + instance.getInstanceId());
            }
        });
    }
}
```

### 2. 鑺傜偣鐩戝惉锟?
```java
import ltd.idcu.est.workflow.api.NodeListener;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class NodeListenerExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 娣诲姞鑺傜偣鐩戝惉锟?        engine.addNodeListener(new NodeListener() {
            @Override
            public void onNodeStart(WorkflowInstance instance, Node node) {
                System.out.println("鑺傜偣寮€锟? " + node.getName());
            }
            
            @Override
            public void onNodeComplete(WorkflowInstance instance, Node node) {
                System.out.println("鑺傜偣瀹屾垚: " + node.getName());
            }
            
            @Override
            public void onNodeFail(WorkflowInstance instance, Node node, Throwable error) {
                System.out.println("鑺傜偣澶辫触: " + node.getName());
                System.out.println("閿欒: " + error.getMessage());
            }
            
            @Override
            public void onNodeSkip(WorkflowInstance instance, Node node) {
                System.out.println("鑺傜偣璺宠繃: " + node.getName());
            }
        });
    }
}
```

### 3. 寮傛鎵ц宸ヤ綔锟?
```java
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.core.Workflows;

import java.util.concurrent.CompletableFuture;

public class AsyncWorkflowExample {
    public static void main(String[] args) throws Exception {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        // 鍋囪宸叉敞鍐屽伐浣滄祦...
        
        // 寮傛鍚姩宸ヤ綔锟?        CompletableFuture<WorkflowInstance> future = 
                engine.startWorkflowAsync("my-workflow");
        
        // 鍋氬叾浠栦簨锟?..
        
        // 绛夊緟缁撴灉
        WorkflowInstance instance = future.get();
        System.out.println("宸ヤ綔娴佺姸锟? " + instance.getStatus());
        
        engine.shutdown();
    }
}
```

---

## 馃挕 鏈€浣冲疄锟?
### 1. 鍚堢悊浣跨敤宸ヤ綔娴佷笂涓嬫枃

```java
// 锟?鎺ㄨ崘锛氬彧鍦ㄤ笂涓嬫枃涓瓨鍌ㄥ繀瑕佺殑鏁版嵁
var node = Workflows.newTaskNode("task", "浠诲姟", ctx -> {
    ctx.setVariable("userId", 123);
    ctx.setVariable("userName", "寮犱笁");
});

// 锟?涓嶆帹鑽愶細瀛樺偍杩囧ぇ鎴栦笉闇€瑕佺殑鏁版嵁
var badNode = Workflows.newTaskNode("task", "浠诲姟", ctx -> {
    // 涓嶈瀛樺偍鏁翠釜澶у锟?    // ctx.setVariable("hugeObject", hugeObject);
});
```

### 2. 閿欒澶勭悊

```java
var node = Workflows.newTaskNode("task", "鍙兘澶辫触鐨勪换锟?, ctx -> {
    try {
        // 鎵ц鍙兘澶辫触鐨勬搷锟?        riskyOperation();
    } catch (Exception e) {
        // 璁板綍閿欒淇℃伅鍒颁笂涓嬫枃
        ctx.setVariable("error", e.getMessage());
        throw e; // 閲嶆柊鎶涘嚭浠ヨ宸ヤ綔娴佸紩鎿庡锟?    }
});
```

### 3. 浼橀泤鍏抽棴寮曟搸

```java
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class GracefulShutdownExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        try {
            // 浣跨敤宸ヤ綔娴佸紩锟?..
        } finally {
            // 纭繚鍏抽棴寮曟搸
            engine.shutdown();
            System.out.println("宸ヤ綔娴佸紩鎿庡凡鍏抽棴锟?);
        }
    }
}
```

---

---

## 馃殌 楂樼骇鐗癸拷?
### 1. 缃戝叧鑺傜偣

EST Workflow 鏀寔涓夌绫诲瀷鐨勭綉鍏筹細

#### 鎺掍粬缃戝叧锛圗xclusive Gateway锟?
```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class ExclusiveGatewayExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "寮€锟?, ctx -> {
            ctx.setVariable("amount", 1000);
        });
        
        var gateway = Workflows.newExclusiveGateway("gateway", "閲戦鍒ゆ柇");
        
        var highPath = Workflows.newTaskNode("high", "楂橀澶勭悊", ctx -> {
            System.out.println("澶勭悊楂橀璁㈠崟");
        });
        
        var lowPath = Workflows.newTaskNode("low", "浣庨澶勭悊", ctx -> {
            System.out.println("澶勭悊浣庨璁㈠崟");
        });
        
        var endNode = Workflows.newTaskNode("end", "缁撴潫", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("gateway-example")
                .name("缃戝叧绀轰緥")
                .startNode(startNode)
                .addNode(gateway)
                .addNode(highPath)
                .addNode(lowPath)
                .endNode(endNode)
                .connect("start", "gateway")
                .connect("gateway", "high", "楂橀", ctx -> 
                    ctx.getVariable("amount", Integer.class).orElse(0) > 500)
                .connect("gateway", "low", "浣庨", ctx -> 
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

#### 骞惰缃戝叧锛圥arallel Gateway锛?```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class ParallelGatewayExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "寮€濮?, ctx -> {
            System.out.println("寮€濮嬪苟琛屽鐞?);
        });
        
        var splitGateway = Workflows.newParallelGateway("split", "鍒嗘敮缃戝叧");
        
        var task1 = Workflows.newTaskNode("task1", "浠诲姟1", ctx -> {
            System.out.println("鎵ц浠诲姟1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        var task2 = Workflows.newTaskNode("task2", "浠诲姟2", ctx -> {
            System.out.println("鎵ц浠诲姟2");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        var task3 = Workflows.newTaskNode("task3", "浠诲姟3", ctx -> {
            System.out.println("鎵ц浠诲姟3");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        var joinGateway = Workflows.newParallelGateway("join", "姹囧悎缃戝叧");
        
        var endNode = Workflows.newTaskNode("end", "缁撴潫", ctx -> {
            System.out.println("鎵€鏈変换鍔″畬鎴?);
        });
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("parallel-workflow")
                .name("骞惰缃戝叧绀轰緥")
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

#### 鍖呭缃戝叧锛圛nclusive Gateway锛?```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;

public class InclusiveGatewayExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        
        var startNode = Workflows.newTaskNode("start", "寮€濮?, ctx -> {
            ctx.setVariable("score", 85);
        });
        
        var gateway = Workflows.newInclusiveGateway("gateway", "璇勫垎鍒ゆ柇");
        
        var excelPath = Workflows.newTaskNode("excel", "浼樼澶勭悊", ctx -> {
            System.out.println("澶勭悊浼樼妗堜緥");
        });
        
        var goodPath = Workflows.newTaskNode("good", "鑹ソ澶勭悊", ctx -> {
            System.out.println("澶勭悊鑹ソ妗堜緥");
        });
        
        var passPath = Workflows.newTaskNode("pass", "鍙婃牸澶勭悊", ctx -> {
            System.out.println("澶勭悊鍙婃牸妗堜緥");
        });
        
        var endNode = Workflows.newTaskNode("end", "缁撴潫", ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("inclusive-workflow")
                .name("鍖呭缃戝叧绀轰緥")
                .startNode(startNode)
                .connect("start", "gateway")
                .connect("gateway", "excel", "浼樼", ctx -> 
                    ctx.getVariable("score", Integer.class).orElse(0) >= 90)
                .connect("gateway", "good", "鑹ソ", ctx -> 
                    ctx.getVariable("score", Integer.class).orElse(0) >= 80)
                .connect("gateway", "pass", "鍙婃牸", ctx -> 
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

### 2. 宸ヤ綔娴佹寔涔呭寲

浣跨敤鍐呭瓨鎸佷箙鍖栦粨搴擄細

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
        
        var startNode = Workflows.newTaskNode("start", "寮€濮?, ctx -> {});
        
        WorkflowDefinition workflow = Workflows.newWorkflowBuilder()
                .id("persistent-workflow")
                .name("鎸佷箙鍖栧伐浣滄祦")
                .startNode(startNode)
                .build();
        
        repository.saveDefinition(workflow);
        
        WorkflowInstance instance = engine.startWorkflow(workflow);
        
        Optional<WorkflowInstance> foundInstance = 
                repository.findInstanceById(instance.getInstanceId());
        
        if (foundInstance.isPresent()) {
            System.out.println("鎵惧埌宸ヤ綔娴佸疄渚? " + foundInstance.get().getInstanceId());
            System.out.println("鐘舵€? " + foundInstance.get().getStatus());
        }
        
        var allInstances = repository.findInstancesByDefinitionId("persistent-workflow");
        System.out.println("璇ュ畾涔夌殑鎵€鏈夊疄渚嬫暟: " + allInstances.size());
        
        engine.shutdown();
    }
}
```

### 3. JSON 娴佺▼瀹氫箟

```java
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.core.Workflows;

public class JsonDefinitionExample {
    public static void main(String[] args) {
        WorkflowDefinitionParser parser = Workflows.newJsonParser();
        
        // 锟?JSON 瑙ｆ瀽宸ヤ綔娴佸畾锟?        String json = "{\n" +
                "  \"id\": \"json-workflow\",\n" +
                "  \"name\": \"JSON 瀹氫箟鐨勫伐浣滄祦\",\n" +
                "  \"nodes\": [\n" +
                "    {\"id\": \"task1\", \"name\": \"浠诲姟1\", \"type\": \"TASK\"},\n" +
                "    {\"id\": \"task2\", \"name\": \"浠诲姟2\", \"type\": \"TASK\"}\n" +
                "  ],\n" +
                "  \"startNode\": \"task1\",\n" +
                "  \"endNode\": \"task2\"\n" +
                "}";
        
        WorkflowDefinition workflow = parser.parse(json);
        
        // 灏嗗伐浣滄祦瀹氫箟搴忓垪鍖栦负 JSON
        String jsonOutput = parser.serialize(workflow);
        System.out.println(jsonOutput);
    }
}
```

### 4. 瀹氭椂瑙﹀彂宸ヤ綔锟?
锟?est-scheduler 闆嗘垚锟?
```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;
import ltd.idcu.est.workflow.core.integration.ScheduledWorkflowTrigger;

import java.util.concurrent.TimeUnit;

public class ScheduledWorkflowExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        Scheduler scheduler = ...; // 锟?EST 妗嗘灦鑾峰彇璋冨害锟?        
        ScheduledWorkflowTrigger trigger = Workflows.newScheduledTrigger(engine, scheduler);
        
        // 姣忛殧 1 灏忔椂鎵ц涓€娆″伐浣滄祦
        String taskId = trigger.scheduleAtFixedRate(
                "my-workflow", 
                0, 
                1, 
                TimeUnit.HOURS);
        
        // 鍙栨秷瀹氭椂浠诲姟
        // trigger.cancelSchedule(taskId);
    }
}
```

### 5. 浜嬩欢椹卞姩宸ヤ綔锟?
锟?est-event 闆嗘垚锟?
```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.core.Workflows;
import ltd.idcu.est.workflow.core.integration.EventDrivenWorkflowTrigger;

public class EventDrivenWorkflowExample {
    public static void main(String[] args) {
        WorkflowEngine engine = Workflows.newWorkflowEngine();
        EventBus eventBus = ...; // 锟?EST 妗嗘灦鑾峰彇浜嬩欢鎬荤嚎
        
        EventDrivenWorkflowTrigger trigger = Workflows.newEventDrivenTrigger(engine, eventBus);
        
        // 锟?"order.created" 浜嬩欢鍙戠敓鏃讹紝瑙﹀彂宸ヤ綔锟?        trigger.registerEventTrigger("order.created", "order-processing-workflow");
        
        // 甯﹁嚜瀹氫箟杞借嵎鎻愬彇锟?        trigger.registerEventTrigger("payment.received", "payment-workflow", event -> {
            Map<String, Object> variables = new HashMap<>();
            variables.put("paymentId", event);
            return variables;
        });
        
        // 鍙栨秷浜嬩欢瑙﹀彂锟?        // trigger.unregisterEventTrigger("order.created");
    }
}
```

---

## 馃幆 鎬荤粨

宸ヤ綔娴佺郴缁熷氨鍍忕▼搴忕殑"鏅鸿兘鐢熶骇锟?锛岃绋嬪簭鑷姩鍖栨墽琛屽鏉傛祦绋嬶紝鎻愰珮鏁堢巼锟?
EST Workflow 鐜板湪鏀寔锟?- 锟?椤哄簭宸ヤ綔娴佹墽锟?- 锟?缃戝叧鑺傜偣锛堟帓浠栥€佸苟琛屻€佸寘瀹癸級
- 锟?宸ヤ綔娴佹寔涔呭寲
- 锟?JSON 娴佺▼瀹氫箟
- 锟?锟?est-scheduler 闆嗘垚锛堝畾鏃惰Е鍙戯級
- 锟?锟?est-event 闆嗘垚锛堜簨浠堕┍鍔級

涓嬩竴绔狅紝鎴戜滑灏嗗涔犳洿澶氶珮绾х壒鎬э紒馃帀
