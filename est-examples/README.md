# EST Examples 绀轰緥妯″潡

鍖呭惈EST妗嗘灦鐨勫悇绉嶄娇鐢ㄧず渚嬨€?
## 妯″潡缁撴瀯

```
est-examples/
鈹溾攢鈹€ est-examples-basic/          # 鍩虹绀轰緥
鈹溾攢鈹€ est-examples-web/            # Web绀轰緥
鈹溾攢鈹€ est-examples-features/       # 鍔熻兘绀轰緥
鈹溾攢鈹€ est-examples-advanced/       # 楂樼骇绀轰緥
鈹溾攢鈹€ est-examples-ai/             # AI鍔╂墜绀轰緥
鈹溾攢鈹€ est-examples-graalvm/        # GraalVM绀轰緥
鈹溾攢鈹€ est-examples-microservices/  # 寰湇鍔＄ず渚?鈹斺攢鈹€ pom.xml
```

## 绀轰緥鍒楄〃

### 鍩虹绀轰緥 (est-examples-basic)

- 渚濊禆娉ㄥ叆瀹瑰櫒浣跨敤
- 閰嶇疆绠＄悊
- 闆嗗悎鎿嶄綔
- 璁捐妯″紡浣跨敤
- 宸ュ叿绫讳娇鐢?
### Web绀轰緥 (est-examples-web)

- Hello World Web搴旂敤
- RESTful API
- 璺敱鍜屾帶鍒跺櫒
- 涓棿浠朵娇鐢?- 浼氳瘽绠＄悊
- 妯℃澘寮曟搸
- Todo搴旂敤
- 鍗氬搴旂敤
- 鐪嬫澘搴旂敤
- 鑱婂ぉ搴旂敤
- 鏂囦欢涓婁紶
- Admin绠＄悊绯荤粺绀轰緥

### 鍔熻兘绀轰緥 (est-examples-features)

- 缂撳瓨绯荤粺
- 浜嬩欢鎬荤嚎
- 鏃ュ織绯荤粺
- 鏁版嵁璁块棶锛圝DBC銆佸唴瀛樸€丮ongoDB銆丷edis锛?- 瀹夊叏璁よ瘉
- 璋冨害绯荤粺
- 鐩戞帶绯荤粺
- 娑堟伅绯荤粺锛圓ctiveMQ銆並afka銆丷abbitMQ銆丷edis绛夛級
- 鐔旀柇鍣?- 宸ヤ綔娴佸紩鎿?
### 楂樼骇绀轰緥 (est-examples-advanced)

- 鎻掍欢绯荤粺
- 瀹屾暣搴旂敤
- 鎬ц兘浼樺寲
- 娴嬭瘯瀹炶返
- 妯″潡闆嗘垚
- 澶氭ā鍧楅泦鎴?- 鏂版灦鏋勭ず渚?
### AI鍔╂墜绀轰緥 (est-examples-ai)

- AI蹇€熷紑濮?- AI杈呭姪Web搴旂敤
- 浠ｇ爜鐢熸垚绀轰緥
- 鎻愮ず璇嶆ā鏉跨ず渚?- LLM闆嗘垚绀轰緥

### GraalVM绀轰緥 (est-examples-graalvm)

- Hello World鍘熺敓搴旂敤
- Web搴旂敤鍘熺敓闀滃儚

### 寰湇鍔＄ず渚?(est-examples-microservices)

- 寰湇鍔＄綉鍏?- 鐢ㄦ埛鏈嶅姟
- 璁㈠崟鏈嶅姟

### Admin绠＄悊绯荤粺绀轰緥 (est-app/est-admin + est-admin-ui)

涓€涓畬鏁寸殑鍓嶅悗绔垎绂荤鐞嗙郴缁燂紝鍖呭惈锛?- 鍚庣 RESTful API
- JWT Token 璁よ瘉
- Vue 3 + Element Plus 鍓嶇
- 鐢ㄦ埛銆佽鑹层€佽彍鍗曘€侀儴闂ㄣ€佺鎴风鐞?
璇︾粏鏂囨。璇峰弬鑰冿細[EST Admin 鍓嶅悗绔仈璋冩寚鍗梋(../docs/guides/admin-integration.md)

## 杩愯绀轰緥

```bash
# 杩愯鍩虹绀轰緥
cd est-examples-basic
mvn exec:java

# 杩愯Web绀轰緥
cd est-examples-web
mvn exec:java

# 杩愯鍔熻兘绀轰緥
cd est-examples-features
./run-examples.bat

# 杩愯AI绀轰緥
cd est-examples-ai
mvn exec:java

# 杩愯Admin绀轰緥
# 1. 鍚姩鍚庣
cd ../est-app/est-admin/est-admin-impl
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.admin.DefaultAdminApplication"

# 2. 鍚姩鍓嶇锛堟柊缁堢锛?cd ../../../est-admin-ui
npm install
npm run dev
```

## 渚濊禆

绀轰緥妯″潡渚濊禆EST妗嗘灦鐨勫叾浠栨ā鍧椼€?
## 鏂囨。

鏇村璇︾粏鏂囨。璇锋煡鐪嬶細
- [绀轰緥浠ｇ爜鏂囨。](../docs/examples/README.md)
- [鍏ラ棬鎸囧崡](../docs/getting-started/README.md)
- [Admin鑱旇皟鎸囧崡](../docs/guides/admin-integration.md)
