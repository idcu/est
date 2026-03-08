# EST Scaffold - 鑴氭墜鏋跺伐鍏锋ā鍧?

涓?EST 妗嗘灦鎻愪緵椤圭洰妯℃澘鐢熸垚鍜屼唬鐮佺敓鎴愬伐鍏枫€?

## 鍔熻兘鐗规€?

- **澶氱椤圭洰妯℃澘** - 鏀寔 8 绉嶄笉鍚岀被鍨嬬殑椤圭洰妯℃澘
- **鐏垫椿鐨勯厤缃€夐」** - 鏀寔鑷畾涔?groupId銆佸寘鍚嶃€丣ava 鐗堟湰绛?
- **浜や簰寮忛厤缃悜瀵?* - 浣跨敤鍙嬪ソ鐨勪氦浜掑紡鐣岄潰鍒涘缓椤圭洰
- **Git 鍒濆鍖?* - 鍙€夎嚜鍔ㄥ垵濮嬪寲 Git 浠撳簱
- **鐩綍鍐茬獊妫€娴?* - 鑷姩妫€娴嬪苟闃叉瑕嗙洊宸插瓨鍦ㄧ殑鐩綍
- **褰╄壊杩涘害杈撳嚭** - 缇庤鐨勮繘搴︽樉绀哄拰褰╄壊杈撳嚭
- **瀹屽杽鐨勬祴璇曡鐩?* - 鍖呭惈鍗曞厓娴嬭瘯纭繚浠ｇ爜璐ㄩ噺

### 鏀寔鐨勯」鐩被鍨?

| 绫诲瀷 | 鎻忚堪 |
|------|------|
| `basic` | 鍩虹 EST 妗嗘灦搴旂敤 |
| `web` | Web 搴旂敤椤圭洰 |
| `api` | REST API 椤圭洰 |
| `cli` | 鍛戒护琛屽伐鍏?|
| `library` | 搴撻」鐩?|
| `plugin` | EST 鎻掍欢椤圭洰 |
| `microservice` | 寰湇鍔￠」鐩?|

## 蹇€熷紑濮?

### 缂栬瘧

```bash
mvn clean install
```

### 浣跨敤

#### 蹇€熸ā寮?

```bash
# 鐢熸垚鍩烘湰椤圭洰
java -jar est-scaffold.jar basic my-project

# 鐢熸垚 Web 搴旂敤椤圭洰
java -jar est-scaffold.jar web my-web-app

# 鐢熸垚 REST API 椤圭洰
java -jar est-scaffold.jar api my-api-service

# 鐢熸垚鍛戒护琛屽伐鍏?
java -jar est-scaffold.jar cli my-cli-tool

# 鐢熸垚搴撻」鐩?
java -jar est-scaffold.jar library my-library

# 鐢熸垚鎻掍欢椤圭洰
java -jar est-scaffold.jar plugin my-plugin

# 鐢熸垚寰湇鍔￠」鐩?
java -jar est-scaffold.jar microservice my-microservice
```

#### 浜や簰寮忔ā寮?

```bash
# 鍚姩浜や簰寮忛厤缃悜瀵?
java -jar est-scaffold.jar interactive
```

### 楂樼骇鐢ㄦ硶 - 鑷畾涔夐厤缃?

```bash
# 浣跨敤鑷畾涔?groupId
java -jar est-scaffold.jar web my-app --groupId=com.mycompany

# 浣跨敤鑷畾涔夊寘鍚?
java -jar est-scaffold.jar api my-api --package=myapi

# 鎸囧畾 Java 鐗堟湰
java -jar est-scaffold.jar basic my-project --java=17

# 鍒濆鍖?Git 浠撳簱
java -jar est-scaffold.jar web my-app --git

# 缁勫悎澶氫釜閫夐」
java -jar est-scaffold.jar web my-app --groupId=com.mycompany --package=mywebapp --java=17 --git
```

## 鍛戒护琛岄€夐」

| 閫夐」 | 璇存槑 | 榛樿鍊?|
|------|------|---------|
| `--groupId=<group>` | 璁剧疆椤圭洰鐨?groupId | `com.example` |
| `--version=<ver>` | 璁剧疆椤圭洰鐗堟湰 | `1.0.0-SNAPSHOT` |
| `--package=<pkg>` | 璁剧疆鑷畾涔夊寘鍚?| 浠庨」鐩悕鑷姩鐢熸垚 |
| `--java=<version>` | 璁剧疆 Java 鐗堟湰 | `21` |
| `--git` | 鍒濆鍖?Git 浠撳簱 | `false` |

## 渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-scaffold</artifactId>
    <version>2.1.0</version>
</dependency>
```

## 妯″潡缁撴瀯

```
est-scaffold/
鈹溾攢鈹€ src/
鈹?  鈹溾攢鈹€ main/
鈹?  鈹?  鈹斺攢鈹€ java/ltd/idcu/est/scaffold/
鈹?  鈹?      鈹溾攢鈹€ ScaffoldGenerator.java    # 涓荤敓鎴愬櫒
鈹?  鈹?      鈹溾攢鈹€ ProjectConfig.java         # 椤圭洰閰嶇疆绫?
鈹?  鈹?      鈹溾攢鈹€ ProjectType.java           # 椤圭洰绫诲瀷鏋氫妇
鈹?  鈹?      鈹溾攢鈹€ ConsoleColors.java         # 鎺у埗鍙伴鑹插伐鍏?
鈹?  鈹?      鈹斺攢鈹€ FileWriterUtil.java        # 鏂囦欢宸ュ叿绫?
鈹?  鈹斺攢鈹€ test/
鈹?      鈹斺攢鈹€ java/ltd/idcu/est/scaffold/
鈹?          鈹溾攢鈹€ ProjectConfigTest.java     # 閰嶇疆绫绘祴璇?
鈹?          鈹溾攢鈹€ ProjectTypeTest.java       # 椤圭洰绫诲瀷娴嬭瘯
鈹?          鈹斺攢鈹€ ScaffoldTestsRunner.java   # 娴嬭瘯杩愯鍣?
鈹溾攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ OPTIMIZATION_SUGGESTIONS.md          # 浼樺寲寤鸿鏂囨。
```

## 杩愯娴嬭瘯

```bash
cd src/test/java/ltd/idcu/est/scaffold
javac ScaffoldTestsRunner.java
java ScaffoldTestsRunner
```

## 宸插畬鎴愮殑浼樺寲

鏍规嵁 OPTIMIZATION_SUGGESTIONS.md 鏂囨。锛屽凡瀹屾垚浠ヤ笅浼樺寲锛?

### 楂樹紭鍏堢骇

鉁?浜や簰寮忛厤缃悜瀵?- `interactive` 鍛戒护
鉁?鏇村椤圭洰妯℃澘绫诲瀷 - CLI銆丩ibrary銆丳lugin銆丮icroservice
鉁?Git 鍒濆鍖?- `--git` 閫夐」
鉁?褰╄壊杈撳嚭 - ConsoleColors 绫?
鉁?杩涘害鎸囩ず鍣?- 鏄剧ず鐢熸垚杩涘害
鉁?瀹屽杽鐨勯敊璇鐞?- 鍙傛暟楠岃瘉鍜屽紓甯稿鐞?

### 涓紭鍏堢骇

鉁?瀹屽杽鐨勫崟鍏冩祴璇?- ProjectConfigTest 鍜?ProjectTypeTest

### 鎶€鏈敼杩?

鉁?浠ｇ爜缁撴瀯浼樺寲 - 鍒犻櫎閲嶅浠ｇ爜锛岀粺涓€瀹炵幇
鉁?绫诲瀷瀹夊叏 - ProjectType 鏋氫妇
鉁?鐏垫椿閰嶇疆 - ProjectConfig 绫?

## 杩涗竴姝ヤ紭鍖栧缓璁?

濡傞渶杩涗竴姝ヤ紭鍖栵紝鍙弬鑰?OPTIMIZATION_SUGGESTIONS.md 鏂囨。锛屽叾涓寘鍚細

1. 妯℃澘鏂囦欢绯荤粺 - 浣跨敤澶栭儴妯℃澘鏂囦欢
2. 閰嶇疆鏂囦欢鏀寔 - YAML/JSON 閰嶇疆
3. 浠ｇ爜鐗囨鐢熸垚鍣?- 鐙珛鐨勪唬鐮佺墖娈电敓鎴?
4. Docker 鏀寔 - 鐢熸垚 Docker 鐩稿叧鏂囦欢
5. CI/CD 閰嶇疆 - 鐢熸垚 CI/CD 閰嶇疆鏂囦欢
6. 鎻掍欢绯荤粺 - 鍏佽绗笁鏂规墿灞曟ā鏉?
7. 椤圭洰鍗囩骇宸ュ叿 - 鍗囩骇鐜版湁椤圭洰
8. dry-run 妯″紡 - 棰勮鍔熻兘
9. 鍥介檯鍖栨敮鎸?- 澶氳瑷€杈撳嚭

## 璁稿彲璇?

鏈」鐩噰鐢?MIT 璁稿彲璇併€?
