# est-console - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-console](#浠€涔堟槸-est-console)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-console

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-console 灏卞儚"搴旂敤鎺у埗鍙?锛屾彁渚涘簲鐢ㄧ洃鎺с€侀厤缃鐞嗐€佹棩蹇楁煡鐪嬬瓑杩愮淮鍔熻兘銆?
### 鏍稿績鐗圭偣
- **搴旂敤鐩戞帶**锛欽VM 鐩戞帶銆佺郴缁熺洃鎺?- **閰嶇疆绠＄悊**锛氬湪绾块厤缃€佺儹鏇存柊
- **鏃ュ織鏌ョ湅**锛氬疄鏃舵棩蹇椼€佹棩蹇楁煡璇?- **鍋ュ悍妫€鏌?*锛氬簲鐢ㄥ仴搴风姸鎬?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-console</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍚敤鎺у埗鍙?```java
@EnableConsole
@SpringBootApplication
public class ConsoleApplication {
    public static void main(String[] args) {
        EstApplication.run(ConsoleApplication.class, args);
    }
}
```

---

## 鏍稿績鍔熻兘

### 搴旂敤鐩戞帶
```java
@Controller
public class MonitorController {
    
    @Get("/console/health")
    public Health checkHealth() {
        return healthService.check();
    }
    
    @Get("/console/metrics")
    public Metrics getMetrics() {
        return metricsService.collect();
    }
}
```

### 閰嶇疆绠＄悊
```java
@Controller
public class ConfigController {
    
    @Get("/console/config")
    public Map<String, Object> getConfig() {
        return configService.getAll();
    }
    
    @Post("/console/config")
    public void updateConfig(@Body Map<String, Object> config) {
        configService.update(config);
    }
}
```

---

## 妯″潡缁撴瀯

```
est-console/
鈹溾攢鈹€ est-console-api/        # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/app/console/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-console-impl/       # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/app/console/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [鐩戞帶妯″潡](../../est-modules/est-foundation/est-monitor/README.md)
