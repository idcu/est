# EST Demo 蹇€熷惎鍔ㄦ寚鍗?
## 椤圭洰宸叉垚鍔熷垱寤猴紒

EST Demo搴旂敤宸茬粡鎴愬姛鍒涘缓骞剁紪璇戙€傜敱浜嶱owerShell鎵ц绛栫暐鍜孧aven鍙傛暟瑙ｆ瀽鐨勯檺鍒讹紝
鎴戜滑鎻愪緵浠ヤ笅鍑犵鏂瑰紡鏉ヨ繍琛岃繖涓紨绀哄簲鐢細

## 鏂瑰紡涓€锛氫娇鐢↖DE杩愯锛堟帹鑽愶級

1. 鍦ㄦ偍鐨処DE锛圛ntelliJ IDEA, Eclipse绛夛級涓墦寮€椤圭洰
2. 瀵艰埅鍒?`est-demo/src/main/java/ltd/idcu/est/demo/EstDemoApplication.java`
3. 鍙抽敭鐐瑰嚮 `main` 鏂规硶锛岄€夋嫨 "Run" 鎴?"Debug"
4. 搴旂敤灏嗗湪 http://localhost:8080 鍚姩

## 鏂瑰紡浜岋細浣跨敤Maven鍛戒护锛堥渶瑕乧md.exe锛?
濡傛灉鎮ㄦ湁cmd.exe璁块棶鏉冮檺锛屽彲浠ワ細

```bash
cd est-demo
mvn exec:java -Dexec.mainClass="ltd.idcu.est.demo.EstDemoApplication"
```

## 鏂瑰紡涓夛細鏋勫缓鍙墽琛孞AR

```bash
cd est-demo
mvn clean package
java -jar target/est-demo-2.1.0.jar
```

## 鍙敤鐨凙PI绔偣

鍚姩鎴愬姛鍚庯紝鎮ㄥ彲浠ヨ闂互涓嬬鐐癸細

- `GET /` - 娆㈣繋椤甸潰
- `GET /api/users` - 鑾峰彇鎵€鏈夌敤鎴峰垪琛?- `GET /api/users/1` - 鑾峰彇ID涓?鐨勭敤鎴蜂俊鎭?- `GET /hello?name=YourName` - 鎵撴嫑鍛兼帴鍙?
## 绀轰緥璇锋眰

```bash
# 鑾峰彇娆㈣繋淇℃伅
curl http://localhost:8080

# 鑾峰彇鐢ㄦ埛鍒楄〃
curl http://localhost:8080/api/users

# 鑾峰彇鐗瑰畾鐢ㄦ埛
curl http://localhost:8080/api/users/2

# 鎵撴嫑鍛?curl http://localhost:8080/hello?name=EST
```

## 椤圭洰缁撴瀯

```
est-demo/
鈹溾攢鈹€ pom.xml                          # Maven閰嶇疆鏂囦欢
鈹溾攢鈹€ README.md                        # 椤圭洰璇存槑
鈹溾攢鈹€ QUICKSTART.md                    # 鏈枃浠?鈹溾攢鈹€ run.bat                          # Windows鎵瑰鐞嗗惎鍔ㄨ剼鏈?鈹溾攢鈹€ run.ps1                          # PowerShell鍚姩鑴氭湰
鈹溾攢鈹€ run-with-classes.ps1            # 浣跨敤缂栬瘧绫荤殑鍚姩鑴氭湰
鈹斺攢鈹€ src/
    鈹斺攢鈹€ main/
        鈹斺攢鈹€ java/
            鈹斺攢鈹€ ltd/
                鈹斺攢鈹€ idcu/
                    鈹斺攢鈹€ est/
                        鈹斺攢鈹€ demo/
                            鈹斺攢鈹€ EstDemoApplication.java  # 涓诲簲鐢ㄧ被
```

## 鎶€鏈爤

- EST Web妗嗘灦 - 闆朵緷璧朩eb鏈嶅姟鍣?- EST JSON宸ュ叿 - JSON搴忓垪鍖?鍙嶅簭鍒楀寲
- EST 鏃ュ織 - 鎺у埗鍙版棩蹇楄緭鍑?- EST 缂撳瓨 - 鍐呭瓨缂撳瓨鏀寔

浜彈浣跨敤EST妗嗘灦锛?