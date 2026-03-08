# est-core-module - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-core-module](#浠€涔堟槸-est-core-module)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-core-module

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-core-module 灏卞儚"妯″潡绠＄悊鍣?锛屽府浣犵鐞嗘鏋剁殑鍚勪釜妯″潡锛屾敮鎸佹ā鍧楃殑鍔犺浇銆佸嵏杞姐€佸惎鐢ㄣ€佺鐢ㄣ€?
### 鏍稿績鐗圭偣
- **妯″潡鍖栨灦鏋?*锛氭敮鎸佹ā鍧楀寲寮€鍙?- **妯″潡鍔犺浇**锛氳嚜鍔ㄥ彂鐜板拰鍔犺浇妯″潡
- **妯″潡渚濊禆**锛氭敮鎸佹ā鍧楅棿渚濊禆绠＄悊
- **妯″潡鐘舵€?*锛氭ā鍧楃敓鍛藉懆鏈熺鐞?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-module</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 瀹氫箟妯″潡
```java
@Module(name = "my-module", version = "1.0.0")
public class MyModule implements EstModule {
    
    @Override
    public void initialize(ModuleContext context) {
        System.out.println("妯″潡鍒濆鍖?);
    }
    
    @Override
    public void destroy(ModuleContext context) {
        System.out.println("妯″潡閿€姣?);
    }
}
```

---

## 鏍稿績鍔熻兘

### 妯″潡渚濊禆
```java
@Module(name = "order-module", dependencies = {"user-module", "product-module"})
public class OrderModule implements EstModule {
}
```

### 妯″潡閰嶇疆
```java
@Module(name = "configurable-module")
@ModuleProperties(prefix = "my-module")
public class ConfigurableModule implements EstModule {
    
    @Value("${my-module.enabled:true}")
    private boolean enabled;
    
    @Override
    public void initialize(ModuleContext context) {
        if (enabled) {
            System.out.println("妯″潡宸插惎鐢?);
        }
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [渚濊禆娉ㄥ叆瀹瑰櫒](../est-core-container/README.md)
