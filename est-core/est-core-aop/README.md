# est-core-aop - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-core-aop](#浠€涔堟槸-est-core-aop)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-core-aop

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-core-aop 灏卞儚"浠ｇ爜鎷︽埅鍣?锛屽彲浠ュ湪鏂规硶鎵ц鍓嶅悗鎻掑叆棰濆鐨勯€昏緫锛屾瘮濡傝褰曟棩蹇椼€佹鏌ユ潈闄愩€佸鐞嗕簨鍔★紝涓嶇敤淇敼鍘熸柟娉曚唬鐮併€?
### 鏍稿績鐗圭偣
- **澹版槑寮?AOP**锛氭敞瑙ｉ┍鍔ㄧ殑 AOP 閰嶇疆
- **鍒囬潰缂栫▼**锛氭敮鎸佸墠缃€佸悗缃€佺幆缁曢€氱煡
- **楂樻€ц兘**锛氬熀浜?ASM 鐨勫瓧鑺傜爜鐢熸垚
- **涓?DI 闆嗘垚**锛氫笌渚濊禆娉ㄥ叆瀹瑰櫒鏃犵紳闆嗘垚

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-aop</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍒涘缓鍒囬潰
```java
@Aspect
public class LoggingAspect {
    
    @Before("execution(* ltd.idcu.est.demo.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("鏂规硶鎵ц鍓? " + joinPoint.getSignature());
    }
    
    @AfterReturning(pointcut = "execution(* ltd.idcu.est.demo.service.*.*(..))", returning = "result")
    public void logAfterReturning(Object result) {
        System.out.println("鏂规硶杩斿洖: " + result);
    }
}
```

---

## 鏍稿績鍔熻兘

### 鐜粫閫氱煡
```java
@Aspect
public class PerformanceAspect {
    
    @Around("execution(* ltd.idcu.est.demo.service.*.*(..))")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long time = System.currentTimeMillis() - start;
            System.out.println(joinPoint.getSignature() + " 鑰楁椂: " + time + "ms");
        }
    }
}
```

### 鑷畾涔夋敞瑙ｅ垏闈?```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}

@Aspect
public class CustomLogAspect {
    
    @Around("@annotation(loggable)")
    public Object logMethod(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        System.out.println("鑷畾涔夋棩蹇? " + joinPoint.getSignature());
        return joinPoint.proceed();
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [渚濊禆娉ㄥ叆瀹瑰櫒](../est-core-container/README.md)
