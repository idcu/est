# est-cli - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-cli](#浠€涔堟槸-est-cli)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-cli

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-cli 灏卞儚"鍛戒护琛屽伐鍏风"锛岄€氳繃鍛戒护琛屽揩閫熷垱寤洪」鐩€佺敓鎴愪唬鐮併€佽繍琛屼换鍔°€?
### 鏍稿績鐗圭偣
- **椤圭洰鑴氭墜鏋?*锛氫竴閿垱寤?EST 椤圭洰
- **浠ｇ爜鐢熸垚**锛氭寜妯℃澘鐢熸垚浠ｇ爜
- **鍛戒护绠＄悊**锛氳嚜瀹氫箟鍛戒护鎵╁睍
- **浜や簰寮?*锛氬弸濂界殑浜や簰浣撻獙

---

## 蹇€熷叆闂?
### 1. 瀹夎 CLI
```bash
# 涓嬭浇骞跺畨瑁?curl -sSL https://est.example.com/install.sh | bash

# 楠岃瘉瀹夎
est --version
```

### 2. 鍒涘缓椤圭洰
```bash
# 鍒涘缓鏂伴」鐩?est new my-project

# 杩涘叆椤圭洰
cd my-project

# 杩愯椤圭洰
est run
```

---

## 鏍稿績鍔熻兘

### 甯哥敤鍛戒护
```bash
# 鍒涘缓椤圭洰
est new <project-name>

# 鐢熸垚浠ｇ爜
est generate controller User

# 杩愯搴旂敤
est run

# 鎵撳寘鏋勫缓
est build

# 鏁版嵁搴撹縼绉?est migrate

# 鏌ョ湅甯姪
est help
```

### 鑷畾涔夊懡浠?```java
@Command(name = "my-command", description = "鎴戠殑鑷畾涔夊懡浠?)
public class MyCommand {
    
    @Execute
    public void execute() {
        System.out.println("鎵ц鑷畾涔夊懡浠?);
    }
}
```

---

## 妯″潡缁撴瀯

```
est-cli/
鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ ltd/idcu/est/tools/cli/
鈹?      鈹溾攢鈹€ commands/
鈹?      鈹斺攢鈹€ EstCliMain.java
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [浠ｇ爜鐢熸垚](../est-codegen/README.md)
