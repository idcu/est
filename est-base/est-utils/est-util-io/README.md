# est-util-io - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-util-io](#浠€涔堟槸-est-util-io)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-util-io

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-util-io 灏卞儚"鏂囦欢鎿嶄綔灏忓姪鎵?锛屽府浣犺鍐欐枃浠躲€佸鍒舵枃浠躲€佸鐞嗘祦锛岀畝鍗曞張瀹夊叏銆?
### 鏍稿績鐗圭偣
- **鏂囦欢鎿嶄綔**锛氳鍐欍€佸鍒躲€佸垹闄?- **娴佸鐞?*锛欼nputStream/OutputStream 宸ュ叿
- **璧勬簮绠＄悊**锛氳嚜鍔ㄥ叧闂祫婧?- **寮傚父澶勭悊**锛氱粺涓€鐨?IO 寮傚父

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-io</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍩烘湰浣跨敤
```java
@Service
public class FileService {
    
    public String readFile(String path) {
        return IoUtil.readString(path);
    }
    
    public void writeFile(String path, String content) {
        IoUtil.writeString(path, content);
    }
    
    public void copyFile(String from, String to) {
        IoUtil.copy(from, to);
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [EST 宸ュ叿鎸囧崡](../../docs/utils/README.md)
