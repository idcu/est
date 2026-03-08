# EST Data Group 鏁版嵁妯″潡缁?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Data Group锛焆(#浠€涔堟槸-est-data-group)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Data Group锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Data Group 灏卞儚鏄竴涓?鏁版嵁宸ュ叿绠?銆傛兂璞′竴涓嬩綘闇€瑕佸鐞嗗悇绉嶆暟鎹細

**浼犵粺鏂瑰紡**锛氭瘡绉嶆暟鎹簱閮借鍐欎笉鍚岀殑浠ｇ爜锛岃繕瑕佽嚜宸卞鐞嗕簨鍔°€佽繛鎺ユ睜... 寰堥夯鐑︼紒

**EST Data Group 鏂瑰紡**锛氱粰浣犱竴濂楃粺涓€鐨勬暟鎹闂伐鍏凤紝閲岄潰鏈夛細
- 馃捑 **鏁版嵁璁块棶鎶借薄** - 鏀寔 JDBC銆佸唴瀛樸€丮ongoDB銆丷edis 绛夊绉嶅瓨鍌?- 馃攧 **宸ヤ綔娴佸紩鎿?* - 瀹氫箟鍜屾墽琛屼笟鍔℃祦绋?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 缁熶竴鐨?API锛屼笉鐢ㄥ叧蹇冨簳灞傚瓨鍌?- 鈿?**楂樻€ц兘** - 浼樺寲鐨勬暟鎹闂紝鏀寔杩炴帴姹?- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夋暟鎹闂疄鐜?- 馃帹 **鍔熻兘瀹屾暣** - 鏀寔澶氱鏁版嵁搴擄紝杩樻湁宸ヤ綔娴佸紩鎿?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-data</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-data-jdbc</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓暟鎹簲鐢?
```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.data.query.Query;
import ltd.idcu.est.collection.api.Seq;

public class FirstDataApp {
    public static void main(String[] args) {
        System.out.println("=== EST Data Group 绗竴涓ず渚?===\n");
        
        Repository<User> userRepo = JdbcRepository.create(User.class);
        
        User user = new User();
        user.setName("寮犱笁");
        user.setEmail("zhangsan@example.com");
        user.setAge(25);
        userRepo.save(user);
        System.out.println("鐢ㄦ埛宸蹭繚瀛? " + user);
        
        User found = userRepo.findById(user.getId());
        System.out.println("鎵惧埌鐢ㄦ埛: " + found);
        
        Seq<User> adults = userRepo.query()
            .where("age > ?", 18)
            .orderBy("name")
            .list();
        System.out.println("鎴愬勾鐢ㄦ埛: " + adults.toList());
        
        userRepo.delete(user);
        System.out.println("鐢ㄦ埛宸插垹闄?);
    }
}

class User {
    private Long id;
    private String name;
    private String email;
    private int age;
    
    public User() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

---

## 鍩虹绡?
### 1. est-data 鏁版嵁璁块棶鎶借薄

#### Repository 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.collection.api.Seq;

Repository<User> userRepo = JdbcRepository.create(User.class);

User user = new User();
user.setName("寮犱笁");
user.setEmail("zhangsan@example.com");
userRepo.save(user);

User found = userRepo.findById(user.getId());

Seq<User> all = userRepo.findAll();

userRepo.delete(user);

userRepo.deleteAll();
```

#### 鏌ヨ鏁版嵁

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.query.Query;
import ltd.idcu.est.collection.api.Seq;

Repository<User> userRepo = JdbcRepository.create(User.class);

Seq<User> users = userRepo.query()
    .where("age > ?", 18)
    .and("name LIKE ?", "寮?")
    .orderBy("age", true)
    .limit(10)
    .list();

User first = userRepo.query()
    .where("email = ?", "zhangsan@example.com")
    .first()
    .orElse(null);

long count = userRepo.query()
    .where("age > ?", 18)
    .count();
```

### 2. 澶氱瀛樺偍瀹炵幇

#### 鍐呭瓨瀛樺偍

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.memory.MemoryRepository;

Repository<User> userRepo = MemoryRepository.create(User.class);

User user = new User();
user.setName("寮犱笁");
userRepo.save(user);

System.out.println("鐢ㄦ埛宸蹭繚瀛樺埌鍐呭瓨");
```

#### JDBC 瀛樺偍

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.data.jdbc.JdbcConfig;

JdbcConfig config = JdbcConfig.builder()
    .url("jdbc:mysql://localhost:3306/mydb")
    .username("root")
    .password("password")
    .build();

Repository<User> userRepo = JdbcRepository.create(User.class, config);
```

#### MongoDB 瀛樺偍

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.mongodb.MongoRepository;
import ltd.idcu.est.data.mongodb.MongoConfig;

MongoConfig config = MongoConfig.builder()
    .connectionString("mongodb://localhost:27017")
    .database("mydb")
    .build();

Repository<User> userRepo = MongoRepository.create(User.class, config);
```

#### Redis 瀛樺偍

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.redis.RedisRepository;
import ltd.idcu.est.data.redis.RedisConfig;

RedisConfig config = RedisConfig.builder()
    .host("localhost")
    .port(6379)
    .build();

Repository<User> userRepo = RedisRepository.create(User.class, config);
```

### 3. est-workflow 宸ヤ綔娴佸紩鎿?
#### 瀹氫箟宸ヤ綔娴?
```java
import ltd.idcu.est.workflow.Workflow;
import ltd.idcu.est.workflow.WorkflowBuilder;
import ltd.idcu.est.workflow.Step;
import ltd.idcu.est.workflow.Condition;

Workflow orderWorkflow = WorkflowBuilder.create("order-process")
    .addStep("validate", ValidateOrderStep.class)
    .addStep("payment", ProcessPaymentStep.class)
        .when(Condition.success("validate"))
    .addStep("inventory", ReserveInventoryStep.class)
        .when(Condition.success("payment"))
    .addStep("shipping", ArrangeShippingStep.class)
        .when(Condition.success("inventory"))
    .addStep("notification", SendNotificationStep.class)
        .when(Condition.anySuccess())
    .build();
```

#### 鎵ц宸ヤ綔娴?
```java
import ltd.idcu.est.workflow.WorkflowEngine;
import ltd.idcu.est.workflow.WorkflowInstance;
import ltd.idcu.est.workflow.WorkflowContext;

WorkflowEngine engine = WorkflowEngine.create();
engine.registerWorkflow(orderWorkflow);

WorkflowContext context = new WorkflowContext();
context.set("orderId", "ORDER001");
context.set("amount", 99.99);

WorkflowInstance instance = engine.start("order-process", context);

System.out.println("宸ヤ綔娴佺姸鎬? " + instance.getStatus());
System.out.println("褰撳墠姝ラ: " + instance.getCurrentStep());

while (instance.isRunning()) {
    Thread.sleep(1000);
    instance = engine.getInstance(instance.getId());
}

System.out.println("宸ヤ綔娴佺粨鏋? " + instance.getStatus());
```

---

## 杩涢樁绡?
### 1. 鑷畾涔?Repository

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.data.query.Query;
import ltd.idcu.est.collection.api.Seq;

public interface UserRepository extends Repository<User> {
    Seq<User> findByAgeGreaterThan(int age);
    User findByEmail(String email);
    Seq<User> findByNameContaining(String name);
}

public class JdbcUserRepository extends JdbcRepository<User> implements UserRepository {
    
    public JdbcUserRepository() {
        super(User.class);
    }
    
    @Override
    public Seq<User> findByAgeGreaterThan(int age) {
        return query()
            .where("age > ?", age)
            .list();
    }
    
    @Override
    public User findByEmail(String email) {
        return query()
            .where("email = ?", email)
            .first()
            .orElse(null);
    }
    
    @Override
    public Seq<User> findByNameContaining(String name) {
        return query()
            .where("name LIKE ?", "%" + name + "%")
            .list();
    }
}

UserRepository userRepo = new JdbcUserRepository();
```

### 2. 浜嬪姟绠＄悊

```java
import ltd.idcu.est.data.Transaction;
import ltd.idcu.est.data.TransactionManager;
import ltd.idcu.est.data.jdbc.JdbcTransactionManager;

TransactionManager txManager = JdbcTransactionManager.create(config);

try (Transaction tx = txManager.begin()) {
    userRepo.save(user1);
    userRepo.save(user2);
    orderRepo.save(order);
    tx.commit();
    System.out.println("浜嬪姟宸叉彁浜?);
} catch (Exception e) {
    tx.rollback();
    System.out.println("浜嬪姟宸插洖婊?);
    throw e;
}
```

### 3. 鑷畾涔夊伐浣滄祦姝ラ

```java
import ltd.idcu.est.workflow.Step;
import ltd.idcu.est.workflow.StepContext;
import ltd.idcu.est.workflow.StepResult;

public class ValidateOrderStep implements Step {
    
    @Override
    public StepResult execute(StepContext context) {
        String orderId = context.get("orderId");
        Double amount = context.get("amount");
        
        if (amount == null || amount <= 0) {
            return StepResult.failure("Invalid amount");
        }
        
        System.out.println("璁㈠崟楠岃瘉閫氳繃: " + orderId);
        return StepResult.success();
    }
}

public class ProcessPaymentStep implements Step {
    
    @Override
    public StepResult execute(StepContext context) {
        String orderId = context.get("orderId");
        Double amount = context.get("amount");
        
        System.out.println("澶勭悊鏀粯: " + orderId + ", 閲戦: " + amount);
        
        boolean success = paymentService.process(orderId, amount);
        
        if (success) {
            return StepResult.success();
        } else {
            return StepResult.failure("Payment failed");
        }
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. Repository 鍛藉悕瑙勮寖

```java
// 鉁?鎺ㄨ崘锛氭竻鏅扮殑鏂规硶鍚?public interface UserRepository extends Repository<User> {
    Seq<User> findByAgeGreaterThan(int age);
    User findByEmail(String email);
    Seq<User> findByNameContaining(String name);
}

// 鉂?涓嶆帹鑽愶細妯＄硦鐨勬柟娉曞悕
public interface BadUserRepository extends Repository<User> {
    Seq<User> getUsers(int age);
    User getUser(String email);
    Seq<User> search(String name);
}
```

### 2. 鍚堢悊浣跨敤绱㈠紩

```java
// 鉁?鎺ㄨ崘锛氫负甯哥敤鏌ヨ瀛楁鍒涘缓绱㈠紩
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_age ON users(age);

// 鐒跺悗鏌ヨ浼氭洿蹇?User user = userRepo.findByEmail("zhangsan@example.com");
```

### 3. 鎵归噺鎿嶄綔

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.collection.api.Seq;

Repository<User> userRepo = JdbcRepository.create(User.class);

// 鉁?鎺ㄨ崘锛氭壒閲忎繚瀛?Seq<User> users = Seqs.of(user1, user2, user3);
userRepo.saveAll(users);

// 鉂?涓嶆帹鑽愶細閫愪釜淇濆瓨
userRepo.save(user1);
userRepo.save(user2);
userRepo.save(user3);
```

### 4. 宸ヤ綔娴侀敊璇鐞?
```java
import ltd.idcu.est.workflow.WorkflowEngine;
import ltd.idcu.est.workflow.WorkflowInstance;

WorkflowEngine engine = WorkflowEngine.create();
WorkflowInstance instance = engine.start("order-process", context);

while (instance.isRunning()) {
    Thread.sleep(1000);
    instance = engine.getInstance(instance.getId());
    
    if (instance.hasFailed()) {
        System.out.println("宸ヤ綔娴佸け璐? " + instance.getError());
        engine.retry(instance.getId());
        break;
    }
}
```

---

## 妯″潡缁撴瀯

```
est-data-group/
鈹溾攢鈹€ est-data/         # 鏁版嵁璁块棶鎶借薄锛圝DBC銆丮emory銆丮ongoDB銆丷edis锛?鈹斺攢鈹€ est-workflow/     # 宸ヤ綔娴佸紩鎿?```

---

## 鐩稿叧璧勬簮

- [est-data README](./est-data/README.md) - 鏁版嵁璁块棶璇︾粏鏂囨。
- [est-workflow README](./est-workflow/README.md) - 宸ヤ綔娴佽缁嗘枃妗?- [绀轰緥浠ｇ爜](../../est-examples/est-examples-basic/) - 鍩虹绀轰緥
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-advanced/) - 楂樼骇绀轰緥
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
