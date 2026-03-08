# EST GraalVM Native Image 绀轰緥

鏈ā鍧楀睍绀轰簡濡備綍浣跨敤GraalVM灏咵ST妗嗘灦搴旂敤缂栬瘧涓哄師鐢熼暅鍍忋€?

## 鍓嶇疆瑕佹眰

- GraalVM 23.1.2 鎴栨洿楂樼増鏈?
- Java 21+
- Maven 3.8+

## 瀹夎GraalVM

1. 涓嬭浇GraalVM: https://www.graalvm.org/downloads/
2. 閰嶇疆JAVA_HOME鐜鍙橀噺鎸囧悜GraalVM瀹夎鐩綍
3. 瀹夎native-image缁勪欢:
   ```bash
   gu install native-image
   ```

## 缂栬瘧鍘熺敓闀滃儚

### 鍩烘湰绀轰緥

```bash
cd est-examples/est-examples-graalvm
mvn clean package -Pnative
```

### 杩愯鍘熺敓闀滃儚

```bash
./target/est-examples-graalvm
```

## 绀轰緥椤圭洰

### 1. HelloWorldNative

绠€鍗曠殑渚濊禆娉ㄥ叆绀轰緥锛屽睍绀篍ST瀹瑰櫒鍦ㄥ師鐢熼暅鍍忎腑鐨勮繍琛屻€?

```bash
mvn clean package -Pnative -DmainClass=ltd.idcu.est.examples.graalvm.HelloWorldNative
./target/est-examples-graalvm
```

### 2. WebAppNative

Web搴旂敤绀轰緥锛屽睍绀篍ST Web妗嗘灦鍦ㄥ師鐢熼暅鍍忎腑鐨勮繍琛屻€?

```bash
mvn clean package -Pnative -DmainClass=ltd.idcu.est.examples.graalvm.WebAppNative
./target/est-examples-graalvm
```

鐒跺悗璁块棶:
- http://localhost:8080
- http://localhost:8080/api/hello
- http://localhost:8080/api/status

## 鎬ц兘瀵规瘮

| 鎸囨爣 | JVM妯″紡 | GraalVM鍘熺敓妯″紡 |
|------|---------|-----------------|
| 鍚姩鏃堕棿 | ~500ms | ~30ms |
| 鍐呭瓨鍗犵敤 | ~50MB | ~25MB |
| 闀滃儚澶у皬 | ~5MB (JAR) | ~25MB (鍘熺敓) |

## 閰嶇疆璇存槑

### 鍙嶅皠閰嶇疆

鍦?`src/main/resources/META-INF/native-image/` 鐩綍涓嬮厤缃?

- `reflect-config.json`: 鍙嶅皠閰嶇疆
- `resource-config.json`: 璧勬簮閰嶇疆
- `jni-config.json`: JNI閰嶇疆
- `serialization-config.json`: 搴忓垪鍖栭厤缃?
- `proxy-config.json`: 鍔ㄦ€佷唬鐞嗛厤缃?

### Maven鎻掍欢閰嶇疆

椤圭洰浣跨敤 `native-maven-plugin` 鏉ョ畝鍖栧師鐢熼暅鍍忕紪璇戣繃绋嬨€?

## 鎻愮ず

1. 棣栨缂栬瘧鍘熺敓闀滃儚鍙兘闇€瑕佽緝闀挎椂闂达紙鍑犲垎閽燂級
2. 鍘熺敓闀滃儚涓嶆敮鎸佸姩鎬佺被鍔犺浇锛屾墍鏈夐渶瑕佺殑绫诲繀椤诲湪缂栬瘧鏃跺彲鐭?
3. 浣跨敤 `--enable-http` 鍜?`--enable-https` 鏉ュ惎鐢ㄧ綉缁滄敮鎸?
4. 閬囧埌鍙嶅皠闂鏃讹紝闇€瑕佸湪 `reflect-config.json` 涓坊鍔犵浉搴旈厤缃?

## 鏁呴殰鎺掗櫎

濡傛灉閬囧埌缂栬瘧閿欒:

1. 纭繚浣跨敤姝ｇ‘鐗堟湰鐨凣raalVM
2. 妫€鏌ュ弽灏勯厤缃槸鍚﹀畬鏁?
3. 浣跨敤 `-H:+ReportExceptionStackTraces` 鑾峰彇鏇磋缁嗙殑閿欒淇℃伅
4. 鏌ョ湅GraalVM瀹樻柟鏂囨。: https://www.graalvm.org/reference-manual/native-image/
