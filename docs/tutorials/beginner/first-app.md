# 鏁欑▼ 1: 绗竴涓?EST 搴旂敤

娆㈣繋鏉ュ埌 EST 妗嗘灦鐨勭涓€涓暀绋嬶紒鏈暀绋嬪皢甯︿綘鍒涘缓浣犵殑绗竴涓?EST 搴旂敤绋嬪簭銆?
## 鍓嶇疆鏉′欢

- JDK 21 鎴栨洿楂樼増鏈?- Maven 3.6 鎴栨洿楂樼増鏈?- 涓€涓枃鏈紪杈戝櫒鎴?IDE

## 绗竴姝? 鍒涘缓椤圭洰

棣栧厛锛岃鎴戜滑鍒涘缓涓€涓?Maven 椤圭洰銆?
### 浣跨敤 Maven Archetype锛堟帹鑽愶級

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-first-est-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### 鎴栬€呮墜鍔ㄥ垱寤?
鍒涘缓浠ヤ笅鐩綍缁撴瀯锛?
```
my-first-est-app/
鈹溾攢鈹€ pom.xml
鈹斺攢鈹€ src/
    鈹斺攢鈹€ main/
        鈹斺攢鈹€ java/
            鈹斺攢鈹€ com/
                鈹斺攢鈹€ example/
                    鈹斺攢鈹€ App.java
```

## 绗簩姝? 閰嶇疆 pom.xml

缂栬緫 `pom.xml`锛屾坊鍔?EST 妗嗘灦鐨勪緷璧栵細

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-first-est-app</artifactId>
    <version>1.0-SNAPSHOT</version>

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
</project>
```

## 绗笁姝? 缂栧啓浠ｇ爜

鍒涘缓 `src/main/java/com/example/App.java`锛?
```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class App {
    public static void main(String[] args) {
        // 1. 鍒涘缓 Web 搴旂敤
        WebApplication app = Web.create("鎴戠殑绗竴涓?EST 搴旂敤", "1.0.0");
        
        // 2. 娣诲姞璺敱
        app.get("/", (req, res) -> {
            res.send("Hello, EST World!");
        });
        
        app.get("/hello/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.send("Hello, " + name + "!");
        });
        
        // 3. 鍚姩鏈嶅姟鍣?        System.out.println("搴旂敤鍚姩涓?..");
        app.run(8080);
        System.out.println("搴旂敤宸插惎鍔紝璁块棶 http://localhost:8080");
    }
}
```

## 绗洓姝? 缂栬瘧鍜岃繍琛?
### 缂栬瘧椤圭洰

```bash
mvn clean compile
```

### 杩愯搴旂敤

```bash
mvn exec:java
```

鎴栬€呯洿鎺ヨ繍琛岋細

```bash
java -cp target/classes com.example.App
```

## 绗簲姝? 娴嬭瘯搴旂敤

鎵撳紑娴忚鍣紝璁块棶浠ヤ笅 URL锛?
1. **棣栭〉**: http://localhost:8080
   
   浣犲簲璇ヤ細鐪嬪埌锛歚Hello, EST World!`

2. **涓€у寲闂€?*: http://localhost:8080/hello/寮犱笁
   
   浣犲簲璇ヤ細鐪嬪埌锛歚Hello, 寮犱笁!`

## 浠ｇ爜瑙ｉ噴

璁╂垜浠€愯瑙ｉ噴浠ｇ爜锛?
```java
WebApplication app = Web.create("鎴戠殑绗竴涓?EST 搴旂敤", "1.0.0");
```

杩欒浠ｇ爜鍒涘缓浜嗕竴涓?Web 搴旂敤瀹炰緥锛屾寚瀹氫簡搴旂敤鍚嶇О鍜岀増鏈彿銆?
```java
app.get("/", (req, res) -> {
    res.send("Hello, EST World!");
});
```

杩欒浠ｇ爜娣诲姞浜嗕竴涓?GET 璺敱锛屽綋璁块棶鏍硅矾寰?`/` 鏃讹紝杩斿洖 "Hello, EST World!"銆?
```java
app.get("/hello/{name}", (req, res) -> {
    String name = req.pathParam("name");
    res.send("Hello, " + name + "!");
});
```

杩欒浠ｇ爜娣诲姞浜嗕竴涓甫璺緞鍙傛暟鐨勮矾鐢憋紝`{name}` 鏄竴涓矾寰勫弬鏁帮紝鍙互閫氳繃 `req.pathParam("name")` 鑾峰彇銆?
```java
app.run(8080);
```

杩欒浠ｇ爜鍚姩浜?Web 鏈嶅姟鍣紝鐩戝惉 8080 绔彛銆?
## 涓嬩竴姝?
鎭枩浣狅紒浣犲凡缁忔垚鍔熷垱寤轰簡浣犵殑绗竴涓?EST 搴旂敤锛?
鎺ヤ笅鏉ワ紝浣犲彲浠ュ涔狅細
- [渚濊禆娉ㄥ叆](./dependency-injection.md) - 瀛︿範 EST 鐨勪緷璧栨敞鍏ュ鍣?- [璺敱鍜屾帶鍒跺櫒](../web/routing.md) - 娣卞叆瀛︿範 Web 璺敱
- [閰嶇疆绠＄悊](./configuration.md) - 瀛︿範濡備綍绠＄悊搴旂敤閰嶇疆

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
