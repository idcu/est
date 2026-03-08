# EST 妗嗘灦閮ㄧ讲閰嶇疆

鏈洰褰曞寘鍚?EST 妗嗘灦鐨勯儴缃茬浉鍏抽厤缃枃浠讹紝鏀寔 Docker銆丏ocker Compose 鍜?Kubernetes 閮ㄧ讲鏂瑰紡銆?
## 鐩綍缁撴瀯

```
deploy/
鈹溾攢鈹€ docker/              # Docker 鐩稿叧閰嶇疆
鈹?  鈹溾攢鈹€ Dockerfile       # Docker 闀滃儚鏋勫缓鏂囦欢
鈹?  鈹溾攢鈹€ docker-compose.yml  # Docker Compose 閰嶇疆
鈹?  鈹斺攢鈹€ .dockerignore    # Docker 鏋勫缓蹇界暐鏂囦欢
鈹溾攢鈹€ k8s/                 # Kubernetes 鐩稿叧閰嶇疆
鈹?  鈹溾攢鈹€ namespace.yaml         # 鍛藉悕绌洪棿閰嶇疆
鈹?  鈹溾攢鈹€ serviceaccount.yaml    # 鏈嶅姟璐︽埛閰嶇疆
鈹?  鈹溾攢鈹€ rbac.yaml              # 鏉冮檺鎺у埗閰嶇疆
鈹?  鈹溾攢鈹€ configmap.yaml         # 搴旂敤閰嶇疆绠＄悊
鈹?  鈹溾攢鈹€ secret.yaml            # 鏁忔劅淇℃伅绠＄悊
鈹?  鈹溾攢鈹€ service.yaml           # 鏈嶅姟閰嶇疆
鈹?  鈹溾攢鈹€ deployment.yaml        # 閮ㄧ讲閰嶇疆
鈹?  鈹溾攢鈹€ hpa.yaml               # 姘村钩鑷姩鎵╃缉瀹?鈹?  鈹溾攢鈹€ pdb.yaml               # Pod 涓柇棰勭畻
鈹?  鈹溾攢鈹€ ingress.yaml           # 鍏ュ彛閰嶇疆
鈹?  鈹斺攢鈹€ kustomization.yaml     # Kustomize 閰嶇疆
鈹溾攢鈹€ servicemesh/         # Service Mesh (Istio) 閰嶇疆
鈹溾攢鈹€ scripts/             # 閮ㄧ讲鑴氭湰
鈹?  鈹溾攢鈹€ build-docker.bat      # Windows Docker 鏋勫缓鑴氭湰
鈹?  鈹溾攢鈹€ build-docker.sh        # Linux/Mac Docker 鏋勫缓鑴氭湰
鈹?  鈹溾攢鈹€ deploy-k8s.bat        # Windows Kubernetes 閮ㄧ讲鑴氭湰
鈹?  鈹溾攢鈹€ deploy-k8s.sh          # Linux/Mac Kubernetes 閮ㄧ讲鑴氭湰
鈹?  鈹溾攢鈹€ undeploy-k8s.bat      # Windows Kubernetes 鍗歌浇鑴氭湰
鈹?  鈹斺攢鈹€ undeploy-k8s.sh        # Linux/Mac Kubernetes 鍗歌浇鑴氭湰
鈹斺攢鈹€ README.md            # 鏈枃浠?```

---

## 蹇€熷紑濮?
### Docker 閮ㄧ讲

#### 鍓嶇疆瑕佹眰

- Docker 20.10+
- Docker Compose 2.0+ (鍙€?

#### 鏂瑰紡涓€锛氫娇鐢ㄨ剼鏈瀯寤猴紙鎺ㄨ崘锛?
**Windows:**
```bash
cd deploy/scripts
build-docker.bat 2.1.0
```

**Linux/Mac:**
```bash
cd deploy/scripts
chmod +x build-docker.sh
./build-docker.sh 2.1.0
```

#### 鏂瑰紡浜岋細鎵嬪姩鏋勫缓

```bash
cd deploy/docker
docker build -t est-demo:2.1.0 -f Dockerfile ../..
```

#### 杩愯瀹瑰櫒

```bash
docker run -d \
  --name est-demo \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xms512m -Xmx2g" \
  -e APP_ENV=production \
  est-demo:2.1.0
```

#### 浣跨敤 Docker Compose

```bash
cd deploy/docker
docker-compose up -d
```

### 鐜鍙橀噺

| 鍙橀噺鍚?| 榛樿鍊?| 璇存槑 |
|--------|--------|------|
| JAVA_OPTS | -Xms512m -Xmx2g -XX:+UseG1GC | JVM 鍙傛暟 |
| APP_ENV | production | 搴旂敤鐜 |
| APP_NAME | est-demo | 搴旂敤鍚嶇О |
| APP_VERSION | 2.1.0 | 搴旂敤鐗堟湰 |
| LOG_LEVEL | INFO | 鏃ュ織绾у埆 |

---

## Kubernetes 閮ㄧ讲

### 鍓嶇疆瑕佹眰

- Kubernetes 1.24+
- kubectl 1.24+
- 鍙€? nginx-ingress-controller
- 鍙€? cert-manager (鐢ㄤ簬 TLS)
- 鍙€? prometheus (鐢ㄤ簬鐩戞帶)

### 蹇€熷紑濮?
#### 鏂瑰紡涓€锛氫娇鐢ㄨ剼鏈儴缃诧紙鎺ㄨ崘锛?
**Windows:**
```bash
cd deploy/scripts
deploy-k8s.bat
```

**Linux/Mac:**
```bash
cd deploy/scripts
chmod +x deploy-k8s.sh
./deploy-k8s.sh
```

#### 鏂瑰紡浜岋細浣跨敤 Kustomize 閮ㄧ讲

```bash
cd deploy/k8s

# 棰勮閰嶇疆
kubectl kustomize .

# 搴旂敤閰嶇疆
kubectl apply -k .
```

#### 鏂瑰紡涓夛細浣跨敤 kubectl 鐩存帴搴旂敤

```bash
cd deploy/k8s

# 1. 鍒涘缓鍛藉悕绌洪棿
kubectl apply -f namespace.yaml

# 2. 鍒涘缓鏈嶅姟璐︽埛鍜屾潈闄?kubectl apply -f serviceaccount.yaml
kubectl apply -f rbac.yaml

# 3. 鍒涘缓閰嶇疆鍜屽瘑閽?kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml

# 4. 鍒涘缓鏈嶅姟
kubectl apply -f service.yaml

# 5. 鍒涘缓閮ㄧ讲
kubectl apply -f deployment.yaml

# 6. 鍒涘缓鑷姩鎵╃缉瀹?kubectl apply -f hpa.yaml

# 7. 鍒涘缓 Pod 涓柇棰勭畻
kubectl apply -f pdb.yaml

# 8. 鍒涘缓鍏ュ彛 (鍙€?
kubectl apply -f ingress.yaml
```

### 閰嶇疆璇存槑

#### 1. namespace.yaml
鍒涘缓鐙珛鐨?`est` 鍛藉悕绌洪棿锛岀敤浜庨殧绂?EST 妗嗘灦搴旂敤銆?
#### 2. serviceaccount.yaml & rbac.yaml
涓哄簲鐢ㄥ垱寤轰笓鐢ㄧ殑鏈嶅姟璐︽埛锛屽苟閰嶇疆鏈€灏忔潈闄愬師鍒欑殑 RBAC 瑙勫垯銆?
#### 3. configmap.yaml
绠＄悊搴旂敤閰嶇疆锛屽寘鎷?
- 搴旂敤鍩烘湰淇℃伅
- 鏈嶅姟鍣ㄩ厤缃?- 鏃ュ織閰嶇疆
- 缂撳瓨閰嶇疆
- 鐩戞帶閰嶇疆
- 鍋ュ悍妫€鏌ラ厤缃?
#### 4. secret.yaml
绠＄悊鏁忔劅淇℃伅锛屽寘鎷?
- 鏁版嵁搴撹繛鎺ヤ俊鎭?- JWT 瀵嗛挜
- API 瀵嗛挜
- 鍔犲瘑瀵嗛挜

鈿狅笍 **閲嶈**: 鐢熶骇鐜璇峰姟蹇呬慨鏀归粯璁ゅ瘑鐮佸拰瀵嗛挜!

#### 5. service.yaml
閰嶇疆 ClusterIP 绫诲瀷鐨勬湇鍔★紝鏆撮湶 HTTP 鍜?Metrics 绔彛銆?
#### 6. deployment.yaml
鏍稿績閮ㄧ讲閰嶇疆锛岀壒鎬у寘鎷?
- 婊氬姩鏇存柊绛栫暐 (maxSurge=1, maxUnavailable=0)
- 鍋ュ悍妫€鏌?(liveness/readiness/startup probes)
- 璧勬簮闄愬埗鍜岃姹?- 瀹夊叏涓婁笅鏂?(闈?root 鐢ㄦ埛杩愯)
- Pod 鍙嶄翰鍜屾€?(楂樺彲鐢?
- 瀹瑰繊鍜岃妭鐐归€夋嫨鍣?- 閰嶇疆鍗锋寕杞?
#### 7. hpa.yaml
姘村钩鑷姩鎵╃缉瀹归厤缃?
- 鏈€灏忓壇鏈暟: 2
- 鏈€澶у壇鏈暟: 10
- 鎵╃缉瀹规寚鏍? CPU 70%, 鍐呭瓨 80%
- 鏅鸿兘鎵╃缉瀹硅涓洪厤缃?
#### 8. pdb.yaml
Pod 涓柇棰勭畻锛岀‘淇濊嚦灏?1 涓?Pod 鍙敤銆?
#### 9. ingress.yaml
Ingress 閰嶇疆锛岀壒鎬у寘鎷?
- TLS 鏀寔 (浣跨敤 cert-manager)
- 瀹夊叏澶撮厤缃?- 璇锋眰浣撳ぇ灏忛檺鍒?- 瓒呮椂閰嶇疆

### 楠岃瘉閮ㄧ讲

```bash
# 妫€鏌?Pod 鐘舵€?kubectl get pods -n est

# 鏌ョ湅 Pod 鏃ュ織
kubectl logs -f deployment/est-demo -n est

# 妫€鏌ユ湇鍔?kubectl get svc -n est

# 妫€鏌?HPA
kubectl get hpa -n est

# 妫€鏌?Ingress
kubectl get ingress -n est

# 绔彛杞彂璁块棶搴旂敤
kubectl port-forward svc/est-demo-service 8080:80 -n est
```

璁块棶 http://localhost:8080 鎴?http://localhost:8080/health 楠岃瘉搴旂敤鏄惁姝ｅ父杩愯銆?
### 鎵╃缉瀹?
#### 鎵嬪姩鎵╃缉瀹?```bash
kubectl scale deployment est-demo --replicas=5 -n est
```

#### 鏌ョ湅 HPA 鐘舵€?```bash
kubectl describe hpa est-demo-hpa -n est
```

### 鏇存柊閮ㄧ讲

#### 鏇存柊闀滃儚
```bash
kubectl set image deployment/est-demo est-demo=est-demo:2.1.0 -n est
```

#### 鏇存柊閰嶇疆
```bash
# 淇敼 configmap.yaml
kubectl apply -f configmap.yaml

# 瑙﹀彂婊氬姩鏇存柊
kubectl rollout restart deployment/est-demo -n est
```

#### 鏌ョ湅鏇存柊鐘舵€?```bash
kubectl rollout status deployment/est-demo -n est
kubectl rollout history deployment/est-demo -n est
```

#### 鍥炴粴
```bash
kubectl rollout undo deployment/est-demo -n est
```

### 鍗歌浇閮ㄧ讲

#### 浣跨敤鑴氭湰鍗歌浇锛堟帹鑽愶級

**Windows:**
```bash
cd deploy/scripts
undeploy-k8s.bat
```

**Linux/Mac:**
```bash
cd deploy/scripts
chmod +x undeploy-k8s.sh
./undeploy-k8s.sh
```

#### 鎵嬪姩鍗歌浇
```bash
# 浣跨敤 Kustomize 鍒犻櫎
kubectl delete -k deploy/k8s

# 鎴栭€愪釜鍒犻櫎
kubectl delete -f deploy/k8s/
```

---

## 鐩戞帶涓庡彲瑙傛祴鎬?
### Prometheus 闆嗘垚

閮ㄧ讲宸查厤缃?Prometheus 娉ㄨВ锛屽彲鑷姩鍙戠幇:

```yaml
annotations:
  prometheus.io/scrape: "true"
  prometheus.io/port: "8080"
  prometheus.io/path: "/metrics"
```

### 鍋ュ悍妫€鏌ョ鐐?
- `/health` - 搴旂敤鍋ュ悍鐘舵€?- `/metrics` - Prometheus 鎸囨爣

---

## 瀹夊叏寤鸿

### 1. 淇敼榛樿瀵嗙爜
```bash
# 鐢熸垚寮哄瘑鐮?kubectl create secret generic est-demo-secret \
  --from-literal=db.password=$(openssl rand -base64 32) \
  --from-literal=jwt.secret=$(openssl rand -base64 64) \
  -n est \
  --dry-run=client -o yaml | kubectl apply -f -
```

### 2. 缃戠粶绛栫暐
寤鸿鍒涘缓 NetworkPolicy 闄愬埗 Pod 闂撮€氫俊銆?
### 3. 闀滃儚瀹夊叏
- 浣跨敤绉佹湁闀滃儚浠撳簱
- 鎵弿闀滃儚婕忔礊
- 浣跨敤鏈€灏忓熀纭€闀滃儚

### 4. 璧勬簮闄愬埗
纭繚閰嶇疆浜嗗悎鐞嗙殑 resources.requests 鍜?resources.limits銆?
---

## 鏁呴殰鎺掓煡

### Pod 鏃犳硶鍚姩
```bash
kubectl describe pod <pod-name> -n est
kubectl logs <pod-name> -n est
```

### 鍋ュ悍妫€鏌ュけ璐?```bash
kubectl exec -it <pod-name> -n est -- curl http://localhost:8080/health
```

### 鏈嶅姟鏃犳硶璁块棶
```bash
kubectl get endpoints est-demo-service -n est
kubectl port-forward svc/est-demo-service 8080:80 -n est
```

---

## 鐢熶骇鐜妫€鏌ユ竻鍗?
- [ ] 淇敼鎵€鏈夐粯璁ゅ瘑鐮佸拰瀵嗛挜
- [ ] 閰嶇疆 TLS (cert-manager)
- [ ] 閰嶇疆璧勬簮闄愬埗
- [ ] 閰嶇疆 Pod 瀹夊叏绛栫暐
- [ ] 閰嶇疆缃戠粶绛栫暐
- [ ] 閰嶇疆澶囦唤绛栫暐
- [ ] 閰嶇疆鐩戞帶鍛婅
- [ ] 閰嶇疆鏃ュ織鑱氬悎
- [ ] 娴嬭瘯鎵╃缉瀹?- [ ] 娴嬭瘯鍥炴粴娴佺▼
- [ ] 鐏鹃毦鎭㈠婕旂粌

---

## 鍙傝€冭祫婧?
- [Kubernetes 瀹樻柟鏂囨。](https://kubernetes.io/docs/)
- [EST 妗嗘灦鏂囨。](../docs/README.md)
- [Docker 瀹樻柟鏂囨。](https://docs.docker.com/)
- [Istio 瀹樻柟鏂囨。](https://istio.io/docs/)

---

## 鐗堟湰淇℃伅

- **EST 妗嗘灦鐗堟湰**: 2.1.0
- **鏂囨。鐗堟湰**: 2.1.0
- **鏈€鍚庢洿鏂?*: 2026-03-08
