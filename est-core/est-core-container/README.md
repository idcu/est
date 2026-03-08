# est-core-container - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-core-container](#浠€涔堟槸-est-core-container)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-core-container

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-core-container 灏卞儚"瀵硅薄绠″"锛屽府浣犲垱寤哄拰绠＄悊鎵€鏈夊璞★紝鑷姩缁勮瀵硅薄涔嬮棿鐨勪緷璧栧叧绯伙紝涓嶇敤浣犳墜鍔?new 瀵硅薄銆?
### 鏍稿績鐗圭偣
- **渚濊禆娉ㄥ叆**锛氭瀯閫犲櫒銆佸瓧娈点€丼etter 娉ㄥ叆
- **鐢熷懡鍛ㄦ湡绠＄悊**锛欱ean 鐨勫垱寤恒€佸垵濮嬪寲銆侀攢姣?- **浣滅敤鍩熸敮鎸?*锛氬崟渚嬨€佸師鍨嬨€佽姹傘€佷細璇濅綔鐢ㄥ煙
- **AOP 闆嗘垚**锛氫笌 AOP 妯″潡鏃犵紳闆嗘垚

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-container</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 瀹氫箟 Bean
```java
@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    public User findById(Long id) {
        return userRepository.findById(id);
    }
}

@Repository
public class UserRepository {
    public User findById(Long id) {
        return new User(id);
    }
}
```

### 3. 鍚姩瀹瑰櫒
```java
public class Application {
    public static void main(String[] args) {
        Container container = EstContainer.run(Application.class);
        
        UserService userService = container.getBean(UserService.class);
        User user = userService.findById(1L);
    }
}
```

---

## 鏍稿績鍔熻兘

### 渚濊禆娉ㄥ叆鏂瑰紡
```java
// 鏋勯€犲櫒娉ㄥ叆锛堟帹鑽愶級
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    
    @Inject
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}

// 瀛楁娉ㄥ叆
@Service
public class ProductService {
    @Inject
    private ProductRepository productRepository;
}

// Setter 娉ㄥ叆
@Service
public class CartService {
    private CartRepository cartRepository;
    
    @Inject
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
```

### 鐢熷懡鍛ㄦ湡鍥炶皟
```java
@Service
public class MyService {
    
    @PostConstruct
    public void init() {
        System.out.println("Bean 鍒濆鍖?);
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("Bean 閿€姣?);
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [AOP 妯″潡](../est-core-aop/README.md)
