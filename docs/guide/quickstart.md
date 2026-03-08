# 蹇€熷紑濮嬫寚鍗楋細5鍒嗛挓涓婃墜 EST

娆㈣繋鏉ュ埌 EST 妗嗘灦鐨勫揩閫熷紑濮嬫寚鍗楋紒鏈寚鍗楀皢甯姪浣犲湪 5 鍒嗛挓鍐呭揩閫熶笂鎵?EST 妗嗘灦銆?
## 鐜瑕佹眰

鍦ㄥ紑濮嬩箣鍓嶏紝璇风‘淇濅綘鐨勫紑鍙戠幆澧冩弧瓒充互涓嬭姹傦細

- **JDK**: 21 鎴栨洿楂樼増鏈?- **Maven**: 3.6 鎴栨洿楂樼増鏈?- **鎿嶄綔绯荤粺**: Windows銆乵acOS 鎴?Linux
- **IDE**: IntelliJ IDEA銆丒clipse 鎴?VS Code锛堟帹鑽?IntelliJ IDEA锛?
### 妫€鏌ョ幆澧?
鎵撳紑缁堢锛岃繍琛屼互涓嬪懡浠ゆ鏌ョ幆澧冿細

```bash
# 妫€鏌?Java 鐗堟湰
java -version

# 妫€鏌?Maven 鐗堟湰
mvn -version
```

濡傛灉鐗堟湰涓嶇鍚堣姹傦紝璇峰厛瀹夎鎴栧崌绾с€?
## 绗竴姝ワ細鍒涘缓椤圭洰锛?鍒嗛挓锛?
### 鏂瑰紡涓€锛氫娇鐢?EST 鑴氭墜鏋讹紙鎺ㄨ崘锛?
```bash
# 鍏嬮殕 EST 椤圭洰
git clone https://github.com/idcu/est.git
cd est

# 鎴栬€呯洿鎺ヤ娇鐢ㄧず渚嬮」鐩?cd est-examples/est-examples-basic
```

### 鏂瑰紡浜岋細鎵嬪姩鍒涘缓 Maven 椤圭洰

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-est-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

cd my-est-app
```

## 绗簩姝ワ細閰嶇疆渚濊禆锛?鍒嗛挓锛?
缂栬緫 `pom.xml`锛屾坊鍔?EST 妗嗘灦渚濊禆锛?
```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <est.version>2.1.0</est.version>
</properties>

<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web</artifactId>
        <version>${est.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <mainClass>com.example.App</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 绗笁姝ワ細缂栧啓浠ｇ爜锛?鍒嗛挓锛?
鍒涘缓 `src/main/java/com/example/App.java`锛?
```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class App {
    public static void main(String[] args) {
        WebApplication app = Web.create("鎴戠殑绗竴涓?EST 搴旂敤", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello, EST!"));
        
        app.get("/api/greet/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.json(Map.of(
                "message", "Hello, " + name + "!",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        app.run(8080);
        System.out.println("搴旂敤宸插惎鍔? http://localhost:8080");
    }
}
```

## 绗洓姝ワ細杩愯搴旂敤锛?鍒嗛挓锛?
### 缂栬瘧椤圭洰

```bash
mvn clean compile
```

### 杩愯搴旂敤

```bash
mvn exec:java
```

浣犲簲璇ヤ細鐪嬪埌绫讳技浠ヤ笅鐨勮緭鍑猴細

```
搴旂敤宸插惎鍔? http://localhost:8080
```

## 绗簲姝ワ細娴嬭瘯搴旂敤锛?鍒嗛挓锛?
鎵撳紑娴忚鍣ㄦ垨浣跨敤 curl 璁块棶浠ヤ笅 URL锛?
### 1. 娴嬭瘯棣栭〉

璁块棶锛歨ttp://localhost:8080

浣犲簲璇ヤ細鐪嬪埌锛?```
Hello, EST!
```

### 2. 娴嬭瘯 API

璁块棶锛歨ttp://localhost:8080/api/greet/寮犱笁

浣犲簲璇ヤ細鐪嬪埌绫讳技浠ヤ笅鐨?JSON 鍝嶅簲锛?```json
{
  "message": "Hello, 寮犱笁!",
  "timestamp": 1709876543210
}
```

### 浣跨敤 curl 娴嬭瘯

```bash
# 娴嬭瘯棣栭〉
curl http://localhost:8080

# 娴嬭瘯 API
curl http://localhost:8080/api/greet/寮犱笁
```

## 鎭枩锛侌煄?
浣犲凡缁忔垚鍔熷垱寤哄苟杩愯浜嗕綘鐨勭涓€涓?EST 搴旂敤锛?
## 涓嬩竴姝?
鐜板湪浣犲凡缁忔帉鎻′簡鍩虹锛屽彲浠ョ户缁涔狅細

- 馃摉 **鏁欑▼绯诲垪**: 鏌ョ湅 [鏁欑▼](../tutorials/README.md) 娣卞叆瀛︿範
- 馃摎 **API 鏂囨。**: 鏌ョ湅 [API 鏂囨。](../api/README.md) 浜嗚В瀹屾暣 API
- 馃挕 **鏈€浣冲疄璺?*: 鏌ョ湅 [鏈€浣冲疄璺礭(../best-practices/README.md) 瀛︿範鏈€浣冲疄璺?- 馃敡 **绀轰緥浠ｇ爜**: 鏌ョ湅 [绀轰緥浠ｇ爜](../../est-examples/README.md) 瀛︿範鏇村鐢ㄦ硶

## 甯歌闂

### Q: 绔彛 8080 琚崰鐢ㄦ€庝箞鍔烇紵

A: 淇敼 `app.run(8080)` 涓殑绔彛鍙凤紝渚嬪鏀逛负 `app.run(8081)`銆?
### Q: 濡備綍鐑噸杞戒唬鐮侊紵

A: 浣跨敤 IDE 鐨勭儹閲嶈浇鍔熻兘锛屾垨鑰呬娇鐢?`est-hotreload` 妯″潡銆?
### Q: 濡備綍娣诲姞鏁版嵁搴撴敮鎸侊紵

A: 娣诲姞 `est-data-jdbc` 渚濊禆锛屽弬鑰?[鏁版嵁搴撴暀绋媇(../tutorials/data/jdbc.md)銆?
### Q: 濡備綍娣诲姞 AI 鍔熻兘锛?
A: 娣诲姞 `est-ai-assistant` 渚濊禆锛屽弬鑰?[AI 鏁欑▼](../tutorials/ai/assistant-quickstart.md)銆?
---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
