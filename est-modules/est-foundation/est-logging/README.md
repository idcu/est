# EST Logging - 鏃ュ織绯荤粺

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸鏃ュ織绯荤粺锛?

鎯宠薄涓€涓嬶紝浣犲湪寮€椋炴満銆傞琛岃繃绋嬩腑锛岄鏈轰笂鏈変竴涓?榛戠洅瀛?锛屽畠浼氳褰曚笅鎵€鏈夐噸瑕佺殑淇℃伅锛?
- 浠€涔堟椂鍊欒捣椋炵殑锛?
- 椋炶楂樺害鏄灏戯紵
- 寮曟搸杩愯浆姝ｅ父鍚楋紵
- 鏈夋病鏈夐亣鍒颁粈涔堥棶棰橈紵

濡傛灉椋炴満鍑轰簡闂锛屾垜浠彲浠ユ煡鐪?榛戠洅瀛?鐨勮褰曪紝鎵惧嚭闂鍑哄湪鍝噷銆?

**鏃ュ織绯荤粺**灏辨槸绋嬪簭鐨?榛戠洅瀛?锛屽畠鍙互璁板綍锛?
- 绋嬪簭浠€涔堟椂鍊欏惎鍔ㄧ殑锛?
- 鐢ㄦ埛鍋氫簡浠€涔堟搷浣滐紵
- 鏁版嵁澶勭悊寰楁€庝箞鏍凤紵
- 鏈夋病鏈夋姤閿欙紵

褰撶▼搴忓嚭闂鏃讹紝鎴戜滑鍙互鏌ョ湅鏃ュ織锛屾壘鍑洪棶棰樻墍鍦紒

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑鏃ュ織绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-logging-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-logging-console</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LoggingFirstExample {
    public static void main(String[] args) {
        // 1. 鑾峰彇涓€涓棩蹇楄褰曞櫒锛堝氨鍍忔嬁鍒伴粦鐩掑瓙锛?
        Logger logger = ConsoleLogs.getLogger("MyFirstLogger");
        
        // 2. 璁板綍涓嶅悓绾у埆鐨勬棩蹇?
        logger.trace("杩欐槸涓€鏉?TRACE 绾у埆鐨勬棩蹇?- 闈炲父璇︾粏鐨勪俊鎭?);
        logger.debug("杩欐槸涓€鏉?DEBUG 绾у埆鐨勬棩蹇?- 璋冭瘯淇℃伅");
        logger.info("杩欐槸涓€鏉?INFO 绾у埆鐨勬棩蹇?- 涓€鑸俊鎭?);
        logger.warn("杩欐槸涓€鏉?WARN 绾у埆鐨勬棩蹇?- 璀﹀憡淇℃伅");
        logger.error("杩欐槸涓€鏉?ERROR 绾у埆鐨勬棩蹇?- 閿欒淇℃伅");
        
        System.out.println("\n鉁?鏃ュ織璁板綍瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒扮被浼艰繖鏍风殑杈撳嚭锛?

```
[2024-01-15 10:30:00] [INFO] MyFirstLogger - 杩欐槸涓€鏉?INFO 绾у埆鐨勬棩蹇?- 涓€鑸俊鎭?
[2024-01-15 10:30:00] [WARN] MyFirstLogger - 杩欐槸涓€鏉?WARN 绾у埆鐨勬棩蹇?- 璀﹀憡淇℃伅
[2024-01-15 10:30:00] [ERROR] MyFirstLogger - 杩欐槸涓€鏉?ERROR 绾у埆鐨勬棩蹇?- 閿欒淇℃伅

鉁?鏃ュ織璁板綍瀹屾垚锛?
```

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤鏃ュ織绯荤粺锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

鍦ㄦ繁鍏ュ涔犱箣鍓嶏紝璁╂垜浠厛鐞嗚В鍑犱釜鏍稿績姒傚康锛?

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **鏃ュ織绾у埆** | 鏃ュ織鐨勯噸瑕佺▼搴︼紝浠庝綆鍒伴珮锛歍RACE &lt; DEBUG &lt; INFO &lt; WARN &lt; ERROR | 澶╂皵璀︽姤锛氭櫘閫氶€氱煡 &lt; 娉ㄦ剰 &lt; 鎻愰啋 &lt; 璀﹀憡 &lt; 绱ф€?|
| **鏃ュ織璁板綍鍣?* | 璁板綍鏃ュ織鐨勫伐鍏?| 榛戠洅瀛?|
| **Appender** | 鏃ュ織杈撳嚭鐨勭洰鐨勫湴锛堟帶鍒跺彴銆佹枃浠剁瓑锛?| 鎵撳嵃鏈猴紝鍙互鎵撳嵃鍒扮焊涓婃垨灞忓箷涓?|
| **Formatter** | 鏃ュ織鐨勬牸寮?| 鎺掔増鏍峰紡 |

### 2. 鏃ュ織绾у埆

EST Logging 鎻愪緵浜?5 涓棩蹇楃骇鍒紝璁╂垜浠€愪竴浜嗚В锛?

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogLevelExample {
    public static void main(String[] args) {
        // 鍒涘缓涓€涓?DEBUG 绾у埆鐨勬棩蹇楄褰曞櫒
        Logger logger = ConsoleLogs.getLogger("LevelExample", LogLevel.DEBUG);
        
        System.out.println("=== 鏃ュ織绾у埆绀轰緥 ===\n");
        
        // TRACE - 鏈€璇︾粏鐨勪俊鎭紝閫氬父鐢ㄤ簬杩借釜浠ｇ爜鎵ц娴佺▼
        logger.trace("杩涘叆浜嗘柟娉?A");
        logger.trace("鍙橀噺 x 鐨勫€兼槸: 100");
        
        // DEBUG - 璋冭瘯淇℃伅锛岀敤浜庡紑鍙戦樁娈?
        logger.debug("姝ｅ湪澶勭悊鐢ㄦ埛璇锋眰");
        logger.debug("鏌ヨ鏁版嵁搴擄紝杩斿洖浜?50 鏉¤褰?);
        
        // INFO - 涓€鑸俊鎭紝璁板綍绋嬪簭鐨勬甯歌繍琛岀姸鎬?
        logger.info("绋嬪簭鍚姩鎴愬姛");
        logger.info("鐢ㄦ埛寮犱笁鐧诲綍浜嗙郴缁?);
        
        // WARN - 璀﹀憡淇℃伅锛岃〃绀哄彲鑳芥湁闂锛屼絾涓嶅奖鍝嶇▼搴忚繍琛?
        logger.warn("鍐呭瓨浣跨敤鐜囪秴杩?80%");
        logger.warn("鎺ュ彛鍝嶅簲鏃堕棿瓒呰繃 1 绉?);
        
        // ERROR - 閿欒淇℃伅锛岃〃绀哄嚭鐜颁簡闂
        logger.error("鏁版嵁搴撹繛鎺ュけ璐?);
        logger.error("鏃犳硶璇诲彇閰嶇疆鏂囦欢");
    }
}
```

**鏃ュ織绾у埆鐨勪娇鐢ㄥ満鏅細**

| 绾у埆 | 浣跨敤鍦烘櫙 | 绀轰緥 |
|------|----------|------|
| **TRACE** | 闈炲父璇︾粏鐨勬祦绋嬭拷韪?| "杩涘叆鏂规硶 calculateTotal()"銆?寰幆绗?5 娆? |
| **DEBUG** | 寮€鍙戣皟璇曚俊鎭?| "鏌ヨ缁撴灉: [User(id=1, name='寮犱笁')]" |
| **INFO** | 閲嶈鐨勪笟鍔′簨浠?| "鐢ㄦ埛鐧诲綍鎴愬姛"銆?璁㈠崟鍒涘缓瀹屾垚" |
| **WARN** | 娼滃湪闂锛屼絾绋嬪簭缁х画杩愯 | "鍐呭瓨浣跨敤鐜囪揪鍒?85%"銆?閲嶈瘯绗?2 娆? |
| **ERROR** | 閿欒锛岄渶瑕佸叧娉?| "鏁版嵁搴撹繛鎺ュけ璐?銆?鏀粯澶勭悊寮傚父" |

### 3. 鍒涘缓鏃ュ織璁板綍鍣?

EST Logging 鎻愪緵浜嗗绉嶅垱寤烘棩蹇楄褰曞櫒鐨勬柟寮忥細

```java
import ltd.idcu.est.features.logging.api.LogConfig;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class CreateLoggerExample {
    public static void main(String[] args) {
        // 鏂瑰紡涓€锛氶€氳繃鍚嶇О鍒涘缓
        Logger logger1 = ConsoleLogs.getLogger("MyApplication");
        
        // 鏂瑰紡浜岋細閫氳繃绫诲垱寤猴紙鎺ㄨ崘锛?
        Logger logger2 = ConsoleLogs.getLogger(CreateLoggerExample.class);
        
        // 鏂瑰紡涓夛細鎸囧畾鏃ュ織绾у埆
        Logger logger3 = ConsoleLogs.getLogger("DebugLogger", LogLevel.DEBUG);
        
        // 鏂瑰紡鍥涳細浣跨敤 DEBUG 绾у埆鐨勫揩鎹锋柟娉?
        Logger logger4 = ConsoleLogs.debugLogger("DebugOnly");
        
        // 鏂瑰紡浜旓細浣跨敤 TRACE 绾у埆鐨勫揩鎹锋柟娉?
        Logger logger5 = ConsoleLogs.traceLogger("TraceEverything");
        
        // 鏂瑰紡鍏細浣跨敤鑷畾涔夐厤缃?
        LogConfig config = LogConfig.defaultConfig()
                .setLevel(LogLevel.INFO);
        Logger logger6 = ConsoleLogs.getLogger("CustomConfig", config);
        
        // 鏂瑰紡涓冿細浣跨敤 Builder 妯″紡
        Logger logger7 = ConsoleLogs.builder()
                .name("BuilderLogger")
                .level(LogLevel.DEBUG)
                .build();
        
        System.out.println("鉁?7 绉嶆棩蹇楄褰曞櫒鍒涘缓鏂瑰紡閮芥垚鍔熶簡锛?);
    }
}
```

### 4. 璁板綍鏃ュ織

#### 4.1 鍩烘湰鏃ュ織璁板綍

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class BasicLoggingExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger(BasicLoggingExample.class);
        
        // 绠€鍗曠殑娑堟伅
        logger.info("绋嬪簭鍚姩");
        
        // 浣跨敤鍗犱綅绗︼紙鎺ㄨ崘锛?
        String username = "寮犱笁";
        int age = 25;
        logger.info("鐢ㄦ埛 {} 鐧诲綍锛屽勾榫?{}", username, age);
        
        // 澶氫釜鍗犱綅绗?
        logger.info("璁㈠崟 {} 璐拱浜?{} 浠跺晢鍝侊紝鎬讳环 {} 鍏?, 
                    "ORD-001", 3, 299.99);
    }
}
```

#### 4.2 璁板綍寮傚父

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class ExceptionLoggingExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger(ExceptionLoggingExample.class);
        
        try {
            // 鍙兘浼氬嚭閿欑殑浠ｇ爜
            int result = 10 / 0;
        } catch (Exception e) {
            // 璁板綍寮傚父
            logger.error("璁＄畻鍑洪敊浜?, e);
        }
        
        try {
            String text = null;
            text.length();
        } catch (Exception e) {
            // 甯︽秷鎭殑寮傚父璁板綍
            logger.error("澶勭悊鏂囨湰鏃跺嚭閿? 鏂囨湰涓?null", e);
        }
    }
}
```

#### 4.3 妫€鏌ユ棩蹇楃骇鍒槸鍚﹀惎鐢?

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LevelCheckExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger("LevelCheck", LogLevel.INFO);
        
        // 妫€鏌ョ骇鍒槸鍚﹀惎鐢?
        System.out.println("TRACE 鍚敤: " + logger.isTraceEnabled());  // false
        System.out.println("DEBUG 鍚敤: " + logger.isDebugEnabled());  // false
        System.out.println("INFO 鍚敤: " + logger.isInfoEnabled());    // true
        System.out.println("WARN 鍚敤: " + logger.isWarnEnabled());    // true
        System.out.println("ERROR 鍚敤: " + logger.isErrorEnabled());  // true
        
        // 瀵逛簬澶嶆潅鐨勬棩蹇楁秷鎭紝鍏堟鏌ョ骇鍒彲浠ユ彁楂樻€ц兘
        if (logger.isDebugEnabled()) {
            String expensiveInfo = calculateExpensiveInfo();
            logger.debug("璋冭瘯淇℃伅: {}", expensiveInfo);
        }
    }
    
    private static String calculateExpensiveInfo() {
        // 妯℃嫙鑰楁椂鎿嶄綔
        return "涓€浜涢渶瑕佽绠楃殑淇℃伅";
    }
}
```

### 5. 鎺у埗鍙版棩蹇?

鎺у埗鍙版棩蹇楁槸鏈€绠€鍗曠殑鏃ュ織杈撳嚭鏂瑰紡锛?

```java
import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.time.format.DateTimeFormatter;

public class ConsoleLoggingExample {
    public static void main(String[] args) {
        // 1. 鏅€氭帶鍒跺彴鏃ュ織
        Logger logger1 = ConsoleLogs.getLogger("NormalLogger");
        logger1.info("杩欐槸鏅€氱殑鎺у埗鍙版棩蹇?);
        
        // 2. 甯﹂鑹茬殑鎺у埗鍙版棩蹇?
        LogFormatter coloredFormatter = ConsoleLogs.coloredFormatter();
        Logger logger2 = ConsoleLogs.builder()
                .name("ColoredLogger")
                .formatter(coloredFormatter)
                .build();
        logger2.info("杩欐槸甯﹂鑹茬殑鎺у埗鍙版棩蹇?);
        
        // 3. 鑷畾涔夋椂闂存牸寮?
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LogFormatter customFormatter = ConsoleLogs.formatter(customFormat, true);
        Logger logger3 = ConsoleLogs.builder()
                .name("CustomFormatLogger")
                .formatter(customFormatter)
                .build();
        logger3.info("杩欐槸鑷畾涔夋椂闂存牸寮忕殑鏃ュ織");
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. 鏂囦欢鏃ュ織

闄や簡鎺у埗鍙帮紝鎴戜滑杩樺彲浠ユ妸鏃ュ織杈撳嚭鍒版枃浠讹細

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.file.FileLogs;

import java.nio.file.Paths;

public class FileLoggingExample {
    public static void main(String[] args) {
        // 鍒涘缓鏂囦欢鏃ュ織璁板綍鍣?
        Logger fileLogger = FileLogs.builder()
                .name("FileLogger")
                .filePath(Paths.get("app.log"))
                .build();
        
        // 璁板綍鏃ュ織鍒版枃浠?
        fileLogger.info("杩欐潯鏃ュ織浼氬啓鍏ュ埌 app.log 鏂囦欢");
        fileLogger.warn("杩欐槸涓€鏉¤鍛?);
        fileLogger.error("杩欐槸涓€鏉￠敊璇?);
        
        System.out.println("鉁?鏃ュ織宸插啓鍏ユ枃浠?app.log");
    }
}
```

### 2. 鏃ュ織閰嶇疆

鎴戜滑鍙互閫氳繃 LogConfig 鏉ラ厤缃棩蹇楃郴缁燂細

```java
import ltd.idcu.est.features.logging.api.LogConfig;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogConfigExample {
    public static void main(String[] args) {
        // 鍒涘缓鑷畾涔夐厤缃?
        LogConfig config = LogConfig.defaultConfig()
                .setLevel(LogLevel.DEBUG)  // 璁剧疆鏃ュ織绾у埆
                .setIncludeTimestamp(true)   // 鍖呭惈鏃堕棿鎴?
                .setIncludeLoggerName(true); // 鍖呭惈鏃ュ織璁板綍鍣ㄥ悕绉?
        
        // 浣跨敤閰嶇疆鍒涘缓鏃ュ織璁板綍鍣?
        Logger logger = ConsoleLogs.getLogger("ConfiguredLogger", config);
        
        logger.trace("TRACE 淇℃伅");
        logger.debug("DEBUG 淇℃伅");
        logger.info("INFO 淇℃伅");
        
        System.out.println("鉁?浣跨敤鑷畾涔夐厤缃殑鏃ュ織璁板綍鍣ㄥ垱寤烘垚鍔燂紒");
    }
}
```

### 3. 鑷畾涔夋棩蹇楁牸寮?

浣犲彲浠ュ垱寤鸿嚜宸辩殑鏃ュ織鏍煎紡锛?

```java
import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.LogRecord;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

// 鑷畾涔夋棩蹇楁牸寮忓櫒
class MyLogFormatter implements LogFormatter {
    @Override
    public String format(LogRecord record) {
        return String.format("[%s] [%s] %s", 
                record.getLevel(), 
                record.getLoggerName(), 
                record.getMessage());
    }
}

public class CustomFormatterExample {
    public static void main(String[] args) {
        // 浣跨敤鑷畾涔夋牸寮忓櫒
        Logger logger = ConsoleLogs.builder()
                .name("CustomFormatLogger")
                .formatter(new MyLogFormatter())
                .build();
        
        logger.info("杩欐槸浣跨敤鑷畾涔夋牸寮忕殑鏃ュ織");
        logger.warn("杩欐槸璀﹀憡淇℃伅");
    }
}
```

### 4. 鏃ュ織缁熻

EST Logging 鎻愪緵浜嗘棩蹇楃粺璁″姛鑳斤細

```java
import ltd.idcu.est.features.logging.api.LogStats;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogStatsExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger("StatsLogger");
        
        // 璁板綍涓€浜涙棩蹇?
        for (int i = 0; i < 10; i++) {
            logger.info("杩欐槸绗?{} 鏉?INFO 鏃ュ織", i);
        }
        logger.warn("杩欐槸涓€鏉?WARN 鏃ュ織");
        logger.error("杩欐槸涓€鏉?ERROR 鏃ュ織");
        
        // 鑾峰彇缁熻淇℃伅
        if (logger instanceof ltd.idcu.est.features.logging.api.AbstractLogger) {
            LogStats stats = ((ltd.idcu.est.features.logging.api.AbstractLogger) logger).getStats();
            
            System.out.println("=== 鏃ュ織缁熻淇℃伅 ===");
            System.out.println("鎬绘棩蹇楁暟: " + stats.getTotalCount());
            System.out.println("TRACE 鏁? " + stats.getTraceCount());
            System.out.println("DEBUG 鏁? " + stats.getDebugCount());
            System.out.println("INFO 鏁? " + stats.getInfoCount());
            System.out.println("WARN 鏁? " + stats.getWarnCount());
            System.out.println("ERROR 鏁? " + stats.getErrorCount());
        }
    }
}
```

### 5. 涓?EST Collection 闆嗘垚

鏃ュ織绯荤粺鍙互鍜?EST Collection 瀹岀編缁撳悎锛?

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.List;

public class LoggingCollectionIntegrationExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger(LoggingCollectionIntegrationExample.class);
        
        List<String> users = List.of("寮犱笁", "鏉庡洓", "鐜嬩簲", "璧靛叚");
        
        logger.info("寮€濮嬪鐞?{} 涓敤鎴?, users.size());
        
        // 浣跨敤 Collection 鎵归噺澶勭悊骞惰褰曟棩蹇?
        Seqs.of(users)
                .filter(user -> {
                    logger.debug("妫€鏌ョ敤鎴? {}", user);
                    return !user.equals("鐜嬩簲");
                })
                .forEach(user -> {
                    logger.info("澶勭悊鐢ㄦ埛: {}", user);
                });
        
        logger.info("鐢ㄦ埛澶勭悊瀹屾垚锛?);
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 鏃ュ織璁板綍鍣ㄥ懡鍚嶈鑼?

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class UserService {
    // 鉁?鎺ㄨ崘锛氫娇鐢ㄧ被鍚嶄綔涓烘棩蹇楄褰曞櫒鍚嶇О
    private static final Logger logger = ConsoleLogs.getLogger(UserService.class);
    
    public void registerUser(String username) {
        logger.info("鐢ㄦ埛娉ㄥ唽: {}", username);
        // ...
    }
}
```

### 2. 鏃ュ織娑堟伅瑙勮寖

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogMessageBestPractice {
    private static final Logger logger = ConsoleLogs.getLogger(LogMessageBestPractice.class);
    
    public void processOrder(String orderId) {
        // 鉁?濂界殑鏃ュ織娑堟伅锛氭竻鏅般€佹弿杩版€у己
        logger.info("寮€濮嬪鐞嗚鍗? {}", orderId);
        
        // 鉂?涓嶅ソ鐨勬棩蹇楁秷鎭細淇℃伅澶皯
        logger.info("澶勭悊涓?..");
        
        // 鉁?浣跨敤鍗犱綅绗︼紝涓嶈鐢ㄥ瓧绗︿覆鎷兼帴
        String user = "寮犱笁";
        int amount = 100;
        logger.info("鐢ㄦ埛 {} 娑堣垂浜?{} 鍏?, user, amount);  // 鎺ㄨ崘
        
        // 鉂?涓嶈杩欐牱鍋氾紙鎬ц兘宸級
        logger.info("鐢ㄦ埛 " + user + " 娑堣垂浜?" + amount + " 鍏?);  // 涓嶆帹鑽?
    }
}
```

### 3. 寮傚父澶勭悊瑙勮寖

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class ExceptionHandlingBestPractice {
    private static final Logger logger = ConsoleLogs.getLogger(ExceptionHandlingBestPractice.class);
    
    public void doSomething() {
        try {
            // 鍙兘鍑洪敊鐨勪唬鐮?
        } catch (Exception e) {
            // 鉁?濂界殑鍋氭硶锛氳褰曞紓甯?
            logger.error("鎿嶄綔澶辫触", e);
        }
        
        try {
            // 鍙兘鍑洪敊鐨勪唬鐮?
        } catch (Exception e) {
            // 鉂?涓嶅ソ鐨勫仛娉曪細鍚炴帀寮傚父
        }
        
        try {
            // 鍙兘鍑洪敊鐨勪唬鐮?
        } catch (Exception e) {
            // 鉂?涓嶅ソ鐨勫仛娉曪細鍙褰曟秷鎭紝涓嶈褰曞紓甯?
            logger.error("鎿嶄綔澶辫触");
        }
    }
}
```

### 4. 鎬ц兘浼樺寲寤鸿

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class PerformanceBestPractice {
    private static final Logger logger = ConsoleLogs.getLogger(PerformanceBestPractice.class);
    
    public void processData() {
        // 鉁?瀵逛簬澶嶆潅鐨勬棩蹇楁秷鎭紝鍏堟鏌ョ骇鍒?
        if (logger.isDebugEnabled()) {
            String expensiveData = computeExpensiveData();
            logger.debug("澶勭悊鏁版嵁: {}", expensiveData);
        }
        
        // 鉂?涓嶈杩欐牱鍋氾紙鍗充娇 DEBUG 绾у埆绂佺敤锛屼篃浼氭墽琛?computeExpensiveData锛?
        logger.debug("澶勭悊鏁版嵁: {}", computeExpensiveData());
    }
    
    private String computeExpensiveData() {
        // 妯℃嫙鑰楁椂鎿嶄綔
        return "涓€浜涢渶瑕佽绠楃殑鏁版嵁";
    }
}
```

### 5. 瀹屾暣绀轰緥锛氱敤鎴锋湇鍔?

璁╂垜浠敤鏃ュ織绯荤粺鏋勫缓涓€涓敤鎴锋湇鍔★細

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.HashMap;
import java.util.Map;

class User {
    private String id;
    private String username;
    private String email;
    
    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

public class UserServiceWithLogging {
    private static final Logger logger = ConsoleLogs.getLogger(UserServiceWithLogging.class);
    private Map<String, User> users = new HashMap<>();
    
    public User registerUser(String id, String username, String email) {
        logger.info("寮€濮嬫敞鍐岀敤鎴? username={}, email={}", username, email);
        
        try {
            if (users.containsKey(id)) {
                logger.warn("鐢ㄦ埛宸插瓨鍦? id={}", id);
                throw new IllegalArgumentException("鐢ㄦ埛宸插瓨鍦?);
            }
            
            User user = new User(id, username, email);
            users.put(id, user);
            
            logger.info("鐢ㄦ埛娉ㄥ唽鎴愬姛: id={}, username={}", id, username);
            logger.debug("褰撳墠鐢ㄦ埛鏁? {}", users.size());
            
            return user;
        } catch (Exception e) {
            logger.error("鐢ㄦ埛娉ㄥ唽澶辫触: username={}", username, e);
            throw e;
        }
    }
    
    public User getUser(String id) {
        logger.debug("鏌ヨ鐢ㄦ埛: id={}", id);
        
        User user = users.get(id);
        if (user == null) {
            logger.warn("鐢ㄦ埛涓嶅瓨鍦? id={}", id);
        } else {
            logger.info("鏌ヨ鍒扮敤鎴? id={}, username={}", id, user.getUsername());
        }
        
        return user;
    }
    
    public static void main(String[] args) {
        logger.info("=== 鐢ㄦ埛鏈嶅姟绀轰緥 ===");
        
        UserServiceWithLogging service = new UserServiceWithLogging();
        
        // 娉ㄥ唽鐢ㄦ埛
        service.registerUser("1", "寮犱笁", "zhangsan@example.com");
        service.registerUser("2", "鏉庡洓", "lisi@example.com");
        
        // 鏌ヨ鐢ㄦ埛
        service.getUser("1");
        service.getUser("3");  // 涓嶅瓨鍦ㄧ殑鐢ㄦ埛
        
        logger.info("=== 绀轰緥鎵ц瀹屾垚 ===");
    }
}
```

---

## 馃幆 鎬荤粨

鎭枩浣狅紒浣犲凡缁忓畬鏁村涔犱簡 EST Logging 鏃ュ織绯荤粺锛?

璁╂垜浠洖椤句竴涓嬮噸鐐癸細

1. **鏍稿績姒傚康**锛氭棩蹇楃骇鍒€佹棩蹇楄褰曞櫒銆丄ppender銆丗ormatter
2. **鏃ュ織绾у埆**锛歍RACE &lt; DEBUG &lt; INFO &lt; WARN &lt; ERROR
3. **鍩烘湰鎿嶄綔**锛氬垱寤烘棩蹇楄褰曞櫒銆佽褰曟棩蹇椼€佽褰曞紓甯?
4. **楂樼骇鍔熻兘**锛氭枃浠舵棩蹇椼€佽嚜瀹氫箟閰嶇疆銆佹棩蹇楃粺璁?
5. **鏈€浣冲疄璺?*锛氬懡鍚嶈鑼冦€佹秷鎭鑼冦€佸紓甯稿鐞嗐€佹€ц兘浼樺寲

鏃ュ織鏄▼搴忕殑"榛戠洅瀛?锛屽ソ濂藉埄鐢ㄥ畠锛屼綘鍙互鏇村揩鍦板彂鐜板拰瑙ｅ喅闂锛?

涓嬩竴绔狅紝鎴戜滑灏嗗涔?EST Monitor 鐩戞帶绯荤粺锛屼笉瑙佷笉鏁ｏ紒馃帀
