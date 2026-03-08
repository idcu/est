# EST Core 鏍稿績妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?

## 鐩綍
1. [浠€涔堟槸 EST Core锛焆(#浠€涔堟槸-est-core)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓?est-collection 闆嗘垚](#涓?est-collection-闆嗘垚)
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Core锛?

### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Core 灏卞儚鏄竴涓?鏅鸿兘绠″"銆傛兂璞′竴涓嬩綘缁忚惀涓€瀹跺叕鍙革紝鏈夊緢澶氫笉鍚岀殑閮ㄩ棬锛氬競鍦洪儴銆佹妧鏈儴銆佽储鍔￠儴... 姣忎釜閮ㄩ棬閮介渶瑕佺敤鍒板叾浠栭儴闂ㄧ殑宸ヤ綔銆?

**浼犵粺鏂瑰紡**锛氭瘡涓儴闂ㄩ渶瑕佷粈涔堥兘鑷繁鍘诲垱寤猴紝瀵艰嚧閲嶅宸ヤ綔锛屽叧绯绘贩涔便€?

**EST Core 鏂瑰紡**锛氫綘鎶婃墍鏈夐儴闂ㄩ兘鍛婅瘔绠″锛岀瀹朵細璐熻矗锛?
- 璁颁綇姣忎釜閮ㄩ棬鐨勪綅缃拰鑱岃矗
- 褰撲竴涓儴闂ㄩ渶瑕佸彟涓€涓儴闂ㄥ府蹇欐椂锛岀瀹朵細鑷姩瀹夋帓
- 纭繚姣忎釜浜烘嬁鍒扮殑閮芥槸姝ｇ‘鐨勩€侀渶瑕佺殑涓滆タ

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓鍜岀鐞嗙粍浠?
- 鈿?**楂樻€ц兘** - 涓夊眰 Map 瀹炵幇锛屽揩閫熸煡鎵剧粍浠?
- 馃敀 **绫诲瀷瀹夊叏** - 缂栬瘧鏃跺氨鑳藉彂鐜扮被鍨嬮敊璇?
- 馃帹 **鐏垫椿寮哄ぇ** - 鏀寔澶氱娉ㄥ叆鏂瑰紡銆佷綔鐢ㄥ煙銆佺敓鍛藉懆鏈熺鐞?

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-api</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓▼搴?

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class FirstExample {
    public static void main(String[] args) {
        // 1. 鍒涘缓涓€涓?绠″"锛堝鍣級
        Container container = new DefaultContainer();
        
        // 2. 鍛婅瘔绠″鏈変粈涔堜笢瑗?
        container.register(MyService.class, MyServiceImpl.class);
        
        // 3. 浠庣瀹堕偅閲岃幏鍙栭渶瑕佺殑涓滆タ
        MyService service = container.get(MyService.class);
        
        // 4. 浣跨敤瀹冿紒
        System.out.println(service.hello("灏忕櫧"));
    }
}

interface MyService {
    String hello(String name);
}

class MyServiceImpl implements MyService {
    @Override
    public String hello(String name) {
        return "浣犲ソ锛? + name + "锛佹杩庝娇鐢?EST Core锛?;
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
浣犲ソ锛屽皬鐧斤紒娆㈣繋浣跨敤 EST Core锛?
```

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Core 浜嗭紒

---

## 鍩虹绡?

### 1. 浠€涔堟槸渚濊禆娉ㄥ叆锛?

#### 涓嶇敤渚濊禆娉ㄥ叆鐨勬儏鍐?

```java
class OrderService {
    private UserService userService;
    private ProductService productService;
    
    public OrderService() {
        // 鑷繁鍒涘缓渚濊禆 - 楹荤儲锛?
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
    }
}
```

#### 鐢ㄤ緷璧栨敞鍏ョ殑鎯呭喌

```java
class OrderService {
    private UserService userService;
    private ProductService productService;
    
    // 鐢辩瀹舵彁渚涗緷璧?- 杞绘澗锛?
    public OrderService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
}
```

### 2. 娉ㄥ唽缁勪欢鐨勫熀鏈柟寮?

#### 鏂瑰紡涓€锛氭敞鍐屽疄鐜扮被

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class BasicRegistration {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 娉ㄥ唽锛氭帴鍙?-> 瀹炵幇绫?
        container.register(UserService.class, UserServiceImpl.class);
        
        // 鑾峰彇缁勪欢
        UserService userService = container.get(UserService.class);
        System.out.println("鐢ㄦ埛鏈嶅姟锛? + userService);
    }
}

interface UserService {
    String getUserInfo();
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserInfo() {
        return "鐢ㄦ埛淇℃伅锛氬紶涓?;
    }
}
```

#### 鏂瑰紡浜岋細娉ㄥ唽鍗曚緥锛堝凡缁忓垱寤哄ソ鐨勫璞★級

```java
// 鍒涘缓涓€涓厤缃璞?
Config myConfig = new Config("app", "1.0");

// 鐩存帴娉ㄥ唽杩欎釜瀵硅薄
container.registerSingleton(Config.class, myConfig);

// 鑾峰彇鐨勫氨鏄綘鍒氭墠娉ㄥ唽鐨勯偅涓璞?
Config config = container.get(Config.class);
System.out.println(config); // 杈撳嚭锛欳onfig{name='app', version='1.0'}
```

### 3. 鑾峰彇缁勪欢

```java
// 鏂瑰紡涓€锛氱洿鎺ヨ幏鍙栵紙濡傛灉涓嶅瓨鍦ㄤ細鎶ラ敊锛?
UserService service = container.get(UserService.class);

// 鏂瑰紡浜岋細瀹夊叏鑾峰彇锛堝鏋滀笉瀛樺湪杩斿洖绌猴級
Optional<UserService> optional = container.getIfPresent(UserService.class);
if (optional.isPresent()) {
    UserService service = optional.get();
    System.out.println("鎵惧埌浜嗘湇鍔★紒");
} else {
    System.out.println("鏈嶅姟涓嶅瓨鍦?);
}

// 鏂瑰紡涓夛細妫€鏌ユ槸鍚﹀瓨鍦?
if (container.contains(UserService.class)) {
    System.out.println("鏈嶅姟宸叉敞鍐?);
}
```

---

## 杩涢樁绡?

### 1. 鏋勯€犲嚱鏁版敞鍏?

EST Core 浼氳嚜鍔ㄨ瘑鍒瀯閫犲嚱鏁帮紝骞舵敞鍏ラ渶瑕佺殑渚濊禆锛?

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ConstructorInjection {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 娉ㄥ唽鎵€鏈夐渶瑕佺殑缁勪欢
        container.register(UserService.class, UserServiceImpl.class);
        container.register(ProductService.class, ProductServiceImpl.class);
        container.register(OrderService.class, OrderServiceImpl.class);
        
        // 鑾峰彇璁㈠崟鏈嶅姟 - 瀹冧細鑷姩鑾峰緱鐢ㄦ埛鏈嶅姟鍜屼骇鍝佹湇鍔★紒
        OrderService orderService = container.get(OrderService.class);
        System.out.println(orderService.createOrder("寮犱笁", "iPhone 15"));
    }
}

interface UserService {
    String getUserName();
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserName() {
        return "寮犱笁";
    }
}

interface ProductService {
    String getProductName();
}

class ProductServiceImpl implements ProductService {
    @Override
    public String getProductName() {
        return "iPhone 15";
    }
}

interface OrderService {
    String createOrder(String user, String product);
}

class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ProductService productService;
    
    // 鏋勯€犲嚱鏁版敞鍏?- EST Core 浼氳嚜鍔ㄥ～鍏咃紒
    public OrderServiceImpl(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    
    @Override
    public String createOrder(String user, String product) {
        return "璁㈠崟鍒涘缓鎴愬姛锛佺敤鎴凤細" + userService.getUserName() + 
               "锛屼骇鍝侊細" + productService.getProductName();
    }
}
```

### 2. @Inject 娉ㄨВ - 瀛楁娉ㄥ叆鍜屾柟娉曟敞鍏?

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.impl.DefaultContainer;

public class AnnotationInjection {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.register(UserService.class, UserServiceImpl.class);
        container.register(EmailService.class, EmailServiceImpl.class);
        container.register(NotificationService.class, NotificationServiceImpl.class);
        
        NotificationService notificationService = container.get(NotificationService.class);
        notificationService.sendNotification("浣犳湁鏂版秷鎭紒");
    }
}

class NotificationServiceImpl implements NotificationService {
    @Inject
    private UserService userService;
    
    private EmailService emailService;
    
    @Inject
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @Override
    public void sendNotification(String message) {
        String userName = userService.getUserName();
        emailService.sendEmail(userName, message);
    }
}
```

### 3. @Component 娉ㄨВ - 鑷姩娉ㄥ唽

浣跨敤娉ㄨВ鍚庯紝浣犲彲浠ヨ EST Core 鑷姩鎵弿鍜屾敞鍐岀粍浠讹細

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ComponentScan {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 鑷姩鎵弿鎸囧畾鍖呬笅鐨勬墍鏈?@Component銆丂Service銆丂Repository
        container.scan("com.example.myapp");
        
        // 鐩存帴浣跨敤锛?
        MyService service = container.get(MyService.class);
        System.out.println(service.doSomething());
    }
}

@Service
class MyServiceImpl implements MyService {
    @Override
    public String doSomething() {
        return "鑷姩鎵弿鎴愬姛锛?;
    }
}
```

### 4. @Qualifier - 澶氫釜瀹炵幇绫绘椂鐨勯€夋嫨

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.impl.DefaultContainer;

public class QualifierExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 娉ㄥ唽涓や釜瀹炵幇
        container.register(PaymentService.class, WeChatPayment.class, "wechat");
        container.register(PaymentService.class, AliPayment.class, "alipay");
        
        // 閫氳繃闄愬畾绗﹁幏鍙?
        PaymentService wechat = container.get(PaymentService.class, "wechat");
        PaymentService alipay = container.get(PaymentService.class, "alipay");
        
        System.out.println(wechat.pay(100));
        System.out.println(alipay.pay(100));
    }
}

interface PaymentService {
    String pay(double amount);
}

class WeChatPayment implements PaymentService {
    @Override
    public String pay(double amount) {
        return "寰俊鏀粯锛? + amount + " 鍏?;
    }
}

class AliPayment implements PaymentService {
    @Override
    public String pay(double amount) {
        return "鏀粯瀹濇敮浠橈細" + amount + " 鍏?;
    }
}
```

---

## 楂樼骇绡?

### 1. 浣滅敤鍩燂紙Scope锛?- 鎺у埗瀵硅薄鐨勭敓鍛藉懆鏈?

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ScopeExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 鍗曚緥妯″紡锛堥粯璁わ級锛氭瘡娆¤幏鍙栭兘鏄悓涓€涓璞?
        container.register(SingletonService.class, SingletonServiceImpl.class, Scope.SINGLETON);
        SingletonService s1 = container.get(SingletonService.class);
        SingletonService s2 = container.get(SingletonService.class);
        System.out.println("鍗曚緥鏄惁鐩稿悓锛? + (s1 == s2)); // true
        
        // 鍘熷瀷妯″紡锛氭瘡娆¤幏鍙栭兘鏄柊瀵硅薄
        container.register(PrototypeService.class, PrototypeServiceImpl.class, Scope.PROTOTYPE);
        PrototypeService p1 = container.get(PrototypeService.class);
        PrototypeService p2 = container.get(PrototypeService.class);
        System.out.println("鍘熷瀷鏄惁鐩稿悓锛? + (p1 == p2)); // false
    }
}
```

### 2. 鐢熷懡鍛ㄦ湡绠＄悊

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.impl.DefaultContainer;

public class LifecycleExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.register(DatabaseService.class, DatabaseServiceImpl.class);
        
        // 鑾峰彇鏃朵細璋冪敤 @PostConstruct
        DatabaseService db = container.get(DatabaseService.class);
        
        // 浣跨敤鏈嶅姟
        db.query("SELECT * FROM users");
        
        // 鍏抽棴瀹瑰櫒鏃朵細璋冪敤 @PreDestroy
        container.close();
    }
}

interface DatabaseService {
    void query(String sql);
}

class DatabaseServiceImpl implements DatabaseService {
    
    @PostConstruct
    public void init() {
        System.out.println("鏁版嵁搴撹繛鎺ュ凡寤虹珛");
    }
    
    @Override
    public void query(String sql) {
        System.out.println("鎵ц SQL锛? + sql);
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("鏁版嵁搴撹繛鎺ュ凡鍏抽棴");
    }
}
```

### 3. 閰嶇疆绠＄悊

```java
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.HashMap;
import java.util.Map;

public class ConfigExample {
    public static void main(String[] args) {
        // 鍒涘缓閰嶇疆
        Map<String, Object> props = new HashMap<>();
        props.put("app.name", "鎴戠殑搴旂敤");
        props.put("app.version", "1.0.0");
        props.put("server.port", 8080);
        props.put("debug", true);
        
        Config config = new DefaultConfig(props);
        
        // 浣跨敤閰嶇疆
        String appName = config.getString("app.name");
        int port = config.getInt("server.port", 8080);
        boolean debug = config.getBoolean("debug", false);
        
        System.out.println("搴旂敤鍚嶏細" + appName);
        System.out.println("绔彛锛? + port);
        System.out.println("璋冭瘯妯″紡锛? + debug);
        
        // 涔熷彲浠ユ妸閰嶇疆鏀惧埌瀹瑰櫒涓?
        Container container = new DefaultContainer(config);
        container.registerSingleton(Config.class, config);
        
        Config containerConfig = container.get(Config.class);
        System.out.println("浠庡鍣ㄨ幏鍙栫殑搴旂敤鍚嶏細" + containerConfig.getString("app.name"));
    }
}
```

---

## 涓?est-collection 闆嗘垚

EST Core 鍜?est-collection 鍙互瀹岀編閰嶅悎浣跨敤锛?

### 鍦烘櫙锛氱敤鎴风鐞嗙郴缁?

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class CollectionIntegration {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.scan("com.example");
        
        UserController controller = container.get(UserController.class);
        
        System.out.println("=== 鎵€鏈夋垚骞寸敤鎴?===");
        controller.printAdultUsers();
        
        System.out.println("\n=== 鎸夊勾榫勬帓搴?===");
        controller.printUsersSortedByAge();
        
        System.out.println("\n=== 骞冲潎骞撮緞 ===");
        controller.printAverageAge();
    }
}

class User {
    private String name;
    private int age;
    private String city;
    
    public User(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    
    @Override
    public String toString() {
        return name + " (" + age + "宀? " + city + ")";
    }
}

@Service
class UserRepository {
    public Seq<User> findAll() {
        return Seqs.of(
            new User("寮犱笁", 25, "鍖椾含"),
            new User("鏉庡洓", 17, "涓婃捣"),
            new User("鐜嬩簲", 30, "骞垮窞"),
            new User("璧靛叚", 16, "娣卞湷"),
            new User("閽变竷", 28, "鍖椾含")
        );
    }
}

@Service
class UserService {
    @Inject
    private UserRepository userRepository;
    
    public Seq<User> getAdultUsers() {
        return userRepository.findAll()
            .where(user -> user.getAge() >= 18);
    }
    
    public Seq<User> getUsersSortedByAge() {
        return userRepository.findAll()
            .sortBy(User::getAge);
    }
    
    public double getAverageAge() {
        return userRepository.findAll()
            .mapToInt(User::getAge)
            .average()
            .orElse(0);
    }
}

@Component
class UserController {
    @Inject
    private UserService userService;
    
    public void printAdultUsers() {
        userService.getAdultUsers().forEach(System.out::println);
    }
    
    public void printUsersSortedByAge() {
        userService.getUsersSortedByAge().forEach(System.out::println);
    }
    
    public void printAverageAge() {
        System.out.println("骞冲潎骞撮緞锛? + userService.getAverageAge() + " 宀?);
    }
}
```

杩愯杈撳嚭锛?
```
=== 鎵€鏈夋垚骞寸敤鎴?===
寮犱笁 (25宀? 鍖椾含)
鐜嬩簲 (30宀? 骞垮窞)
閽变竷 (28宀? 鍖椾含)

=== 鎸夊勾榫勬帓搴?===
璧靛叚 (16宀? 娣卞湷)
鏉庡洓 (17宀? 涓婃捣)
寮犱笁 (25宀? 鍖椾含)
閽变竷 (28宀? 鍖椾含)
鐜嬩簲 (30宀? 骞垮窞)

=== 骞冲潎骞撮緞 ===
骞冲潎骞撮緞锛?3.2 宀?
```

---

## 鏈€浣冲疄璺?

### 1. 浼樺厛浣跨敤鏋勯€犲嚱鏁版敞鍏?

```java
// 鉁?鎺ㄨ崘锛氭瀯閫犲嚱鏁版敞鍏?
class GoodService {
    private final Dependency dependency;
    
    public GoodService(Dependency dependency) {
        this.dependency = dependency;
    }
}

// 鉂?涓嶆帹鑽愶細瀛楁娉ㄥ叆
class BadService {
    @Inject
    private Dependency dependency;
}
```

### 2. 浣跨敤鎺ュ彛缂栫▼

```java
// 鉁?鎺ㄨ崘锛氫緷璧栨帴鍙?
interface UserRepository {
    User findById(String id);
}

class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// 鉂?涓嶆帹鑽愶細渚濊禆鍏蜂綋瀹炵幇
class BadUserService {
    private final JdbcUserRepository repository;
    
    public BadUserService(JdbcUserRepository repository) {
        this.repository = repository;
    }
}
```

### 3. 鍚堢悊浣跨敤鍗曚緥

```java
// 鉁?閫傚悎鍗曚緥锛氭棤鐘舵€佺殑鏈嶅姟
@Service
class StatelessService {
    public String process(String input) {
        return input.toUpperCase();
    }
}

// 鉂?涓嶉€傚悎鍗曚緥锛氭湁鐘舵€佺殑瀵硅薄
class StatefulObject {
    private int counter;
    
    public void increment() {
        counter++;
    }
}
```

### 4. 妯″潡鍖栨敞鍐?

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.impl.AbstractModule;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ModuleExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 鎸夋ā鍧楃粍缁?
        container.install(new UserModule());
        container.install(new OrderModule());
        container.install(new PaymentModule());
    }
}

class UserModule extends AbstractModule {
    @Override
    protected void configure() {
        register(UserService.class, UserServiceImpl.class);
        register(UserRepository.class, UserRepositoryImpl.class);
    }
}
```

## 甯歌闂

### Q: 寰幆渚濊禆鎬庝箞鍔烇紵

A: EST Core 浼氭娴嬪惊鐜緷璧栧苟鎶涘嚭寮傚父銆傝В鍐虫柟妗堬細
1. 閲嶆瀯浠ｇ爜锛屾秷闄ゅ惊鐜緷璧?
2. 浣跨敤鎳掑姞杞?
3. 浣跨敤 setter 娉ㄥ叆鏇夸唬鏋勯€犲嚱鏁版敞鍏?

### Q: 鎬ц兘濡備綍锛?

A: EST Core 浣跨敤涓夊眰 Map 瀹炵幇锛屾€ц兘闈炲父濂斤紝瀹屽叏鍙互婊¤冻鐢熶骇鐜闇€姹傘€?

### Q: 鍜?Spring 鏈変粈涔堝尯鍒紵

A: EST Core 鏇磋交閲忋€佹洿绠€鍗曪紝涓撴敞浜庢牳蹇冪殑 DI 鍔熻兘锛屾病鏈?Spring 閭ｄ箞澶嶆潅銆?

## 涓嬩竴姝?

- 瀛︿範 [est-collection](../est-foundation/est-collection/README.md) 杩涜鏁版嵁澶勭悊
- 鏌ョ湅 [est-features-data](../est-features/est-features-data/) 杩涜鏁版嵁鎸佷箙鍖?
- 闃呰 [API 鏂囨。](../docs/api/core/) 浜嗚В鏇村缁嗚妭
