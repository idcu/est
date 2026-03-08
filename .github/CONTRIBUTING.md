# EST 妗嗘灦璐＄尞鎸囧崡

娆㈣繋鍙備笌 EST 妗嗘灦鐨勫紑鍙戯紒鎴戜滑闈炲父鎰熻阿鎮ㄧ殑璐＄尞銆?
## 琛屼负鍑嗗垯

鍦ㄥ弬涓庢湰椤圭洰涔嬪墠锛岃闃呰鎴戜滑鐨?[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)銆?
## 濡備綍璐＄尞

### 鎶ュ憡闂

1. 棣栧厛锛屾悳绱㈢幇鏈夌殑 [Issues](../../issues)锛岀‘淇濇病鏈夐噸澶嶇殑闂
2. 濡傛灉娌℃湁鎵惧埌锛孾鍒涘缓涓€涓柊鐨?Issue](../../issues/new/choose)锛岄€夋嫨鍚堥€傜殑妯℃澘

### 鎻愪氦浠ｇ爜

1. Fork 鏈粨搴?2. 鍏嬮殕鎮ㄧ殑 Fork 浠撳簱鍒版湰鍦?3. 鍒涘缓涓€涓柊鐨勫垎鏀細`git checkout -b feature/your-feature-name` 鎴?`git checkout -b fix/your-fix-name`
4. 杩涜鎮ㄧ殑淇敼
5. 纭繚浠ｇ爜閫氳繃鎵€鏈夋祴璇曪細`mvn clean test`
6. 鎻愪氦鎮ㄧ殑鍙樻洿锛歚git commit -m 'feat: 娣诲姞浜嗘柊鍔熻兘'`锛堣浣跨敤绾﹀畾寮忔彁浜ゆ牸寮忥級
7. 鎺ㄩ€佸埌鎮ㄧ殑 Fork 浠撳簱锛歚git push origin feature/your-feature-name`
8. 鍒涘缓涓€涓?Pull Request

## 寮€鍙戠幆澧冭缃?
### 鍓嶇疆瑕佹眰

- **JDK 21 鎴栨洿楂樼増鏈?*
- **Maven 3.6 鎴栨洿楂樼増鏈?*
- **Git**

### 鍏嬮殕椤圭洰

```bash
git clone https://github.com/[your-username]/est.git
cd est
```

### 鏋勫缓椤圭洰

```bash
# 瀹屾暣鏋勫缓锛堝寘鍚祴璇曪級
mvn clean install

# 蹇€熸瀯寤猴紙璺宠繃娴嬭瘯锛?mvn clean install -DskipTests

# 杩愯娴嬭瘯
mvn test

# 杩愯浠ｇ爜璐ㄩ噺妫€鏌?mvn checkstyle:check
mvn pmd:check
mvn spotbugs:check
```

## 浠ｇ爜椋庢牸

鎴戜滑浣跨敤浠ヤ笅宸ュ叿鏉ョ‘淇濅唬鐮佽川閲忥細

- **Checkstyle**: 浠ｇ爜椋庢牸妫€鏌?- **PMD**: 浠ｇ爜闈欐€佸垎鏋?- **SpotBugs**: Bug 妫€娴?
閰嶇疆鏂囦欢浣嶄簬 `.config/` 鐩綍涓嬨€?
## 鎻愪氦淇℃伅瑙勮寖

鎴戜滑浣跨敤 **绾﹀畾寮忔彁浜わ紙Conventional Commits锛?* 瑙勮寖锛?
```
<绫诲瀷>(<鑼冨洿>): <鎻忚堪>

<姝ｆ枃>

<椤佃剼>
```

### 绫诲瀷

- `feat`: 鏂板姛鑳?- `fix`: Bug 淇
- `docs`: 鏂囨。鏇存柊
- `style`: 浠ｇ爜鏍煎紡淇敼锛堜笉褰卞搷鍔熻兘锛?- `refactor`: 浠ｇ爜閲嶆瀯
- `perf`: 鎬ц兘浼樺寲
- `test`: 娴嬭瘯鐩稿叧
- `chore`: 鏋勫缓/宸ュ叿鐩稿叧

### 绀轰緥

```
feat(ai): 娣诲姞鏂扮殑 LLM 鎻愪緵鍟嗘敮鎸?
- 娣诲姞閫氫箟鍗冮棶 LLM 闆嗘垚
- 鏇存柊鐩稿叧鏂囨。

Closes #123
```

## Pull Request 娴佺▼

1. **鍒涘缓 PR**: 鎸夌収 [Pull Request 妯℃澘](PULL_REQUEST_TEMPLATE.md) 濉啓
2. **鑷姩妫€鏌?*: GitHub Actions 浼氳嚜鍔ㄨ繍琛屾祴璇曞拰浠ｇ爜妫€鏌?3. **浠ｇ爜瀹℃煡**: 椤圭洰缁存姢鑰呬細杩涜浠ｇ爜瀹℃煡
4. **鍚堝苟**: 閫氳繃瀹℃煡鍚庯紝鎮ㄧ殑 PR 浼氳鍚堝苟

## 鑾峰彇甯姪

濡傛灉鎮ㄩ渶瑕佸府鍔╋紝鍙互锛?
- 鍦?[Discussions](../../discussions) 涓彁闂?- 鏌ョ湅 [鏂囨。](../docs/)
- 鑱旂郴缁存姢鑰?
## 璁稿彲璇?
閫氳繃璐＄尞浠ｇ爜锛屾偍鍚屾剰鎮ㄧ殑璐＄尞灏嗘牴鎹」鐩殑 [MIT 璁稿彲璇乚(../LICENSE) 杩涜璁稿彲銆?
---

鍐嶆鎰熻阿鎮ㄥ EST 妗嗘灦鐨勮础鐚紒馃帀
