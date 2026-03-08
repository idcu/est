# est-core-tx - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-core-tx](#浠€涔堟槸-est-core-tx)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-core-tx

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-core-tx 灏卞儚"浜嬪姟绠″"锛屽府浣犵鐞嗘暟鎹簱浜嬪姟锛岃涔堝叏閮ㄦ垚鍔燂紝瑕佷箞鍏ㄩ儴澶辫触锛屼笉浼氬嚭鐜颁竴鍗婃垚鍔熶竴鍗婂け璐ョ殑鎯呭喌銆?
### 鏍稿績鐗圭偣
- **澹版槑寮忎簨鍔?*锛氭敞瑙ｉ┍鍔ㄧ殑浜嬪姟绠＄悊
- **浜嬪姟浼犳挱**锛氭敮鎸佸绉嶄簨鍔′紶鎾涓?- **浜嬪姟闅旂**锛氭敮鎸佸绉嶉殧绂荤骇鍒?- **鍥炴粴鎺у埗**锛氱伒娲荤殑鍥炴粴瑙勫垯

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-tx</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 浣跨敤浜嬪姟
```java
@Service
public class OrderService {
    
    @Inject
    private OrderRepository orderRepository;
    
    @Inject
    private InventoryService inventoryService;
    
    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        inventoryService.deductStock(order.getProductId(), order.getQuantity());
    }
}
```

---

## 鏍稿績鍔熻兘

### 浜嬪姟閰嶇疆
```java
@Transactional(
    propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED,
    timeout = 30,
    readOnly = false,
    rollbackFor = Exception.class
)
public void complexOperation() {
}
```

### 缂栫▼寮忎簨鍔?```java
@Service
public class ManualTransactionService {
    
    @Inject
    private TransactionTemplate transactionTemplate;
    
    public void doInTransaction() {
        transactionTemplate.execute(status -> {
            try {
                orderRepository.save(order);
                inventoryService.deduct(productId, quantity);
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [渚濊禆娉ㄥ叆瀹瑰櫒](../est-core-container/README.md)
