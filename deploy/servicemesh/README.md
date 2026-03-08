# EST 妗嗘灦鏈嶅姟缃戞牸閰嶇疆

鏈洰褰曞寘鍚?EST 妗嗘灦鐨?Istio 鏈嶅姟缃戞牸閰嶇疆锛屾彁渚涘畬鏁寸殑娴侀噺绠＄悊銆佸畨鍏ㄣ€佸彲瑙傛祴鎬х瓑鏈嶅姟缃戞牸鑳藉姏銆?

## 鐩綍缁撴瀯

```
deploy/servicemesh/
鈹溾攢鈹€ gateway.yaml              # Istio Gateway - 鍏ュ彛缃戝叧
鈹溾攢鈹€ virtualservice.yaml       # VirtualService - 娴侀噺璺敱
鈹溾攢鈹€ destinationrule.yaml      # DestinationRule - 鐩爣瑙勫垯
鈹溾攢鈹€ serviceentry.yaml         # ServiceEntry - 澶栭儴鏈嶅姟
鈹溾攢鈹€ peerauthentication.yaml   # PeerAuthentication - mTLS 瀹夊叏
鈹溾攢鈹€ authorizationpolicy.yaml  # AuthorizationPolicy - 鎺堟潈绛栫暐
鈹溾攢鈹€ sidecar.yaml              # Sidecar - 杈硅溅閰嶇疆
鈹溾攢鈹€ kustomization.yaml        # Kustomize 閰嶇疆
鈹斺攢鈹€ README.md                 # 鏈枃浠?
```

---

## 鍓嶇疆瑕佹眰

- Kubernetes 1.24+
- Istio 1.18+
- kubectl 1.24+
- 宸查儴缃茬殑 EST 妗嗘灦搴旂敤 (deploy/k8s/)

### 瀹夎 Istio

```bash
# 涓嬭浇 Istio
curl -L https://istio.io/downloadIstio | sh -
cd istio-*

# 瀹夎 Istio (demo 閰嶇疆鐢ㄤ簬娴嬭瘯, production 鐢ㄤ簬鐢熶骇)
istioctl install --set profile=demo -y

# 鎴栦娇鐢ㄧ敓浜ч厤缃?
istioctl install --set profile=default -y

# 楠岃瘉瀹夎
istioctl verify-install
```

### 涓哄懡鍚嶇┖闂村惎鐢?Sidecar 娉ㄥ叆

```bash
# 涓?est 鍛藉悕绌洪棿鍚敤鑷姩娉ㄥ叆
kubectl label namespace est istio-injection=enabled

# 楠岃瘉
kubectl get namespace -L istio-injection
```

---

## 蹇€熷紑濮?

### 鏂瑰紡涓€: 浣跨敤 Kustomize (鎺ㄨ崘)

```bash
# 閮ㄧ讲鏈嶅姟缃戞牸閰嶇疆
kubectl apply -k deploy/servicemesh

# 鏌ョ湅璧勬簮
kubectl get gateway,virtualservice,destinationrule -n est
```

### 鏂瑰紡浜? 閫愪釜閮ㄧ讲

```bash
cd deploy/servicemesh

# 閮ㄧ讲 Gateway
kubectl apply -f gateway.yaml

# 閮ㄧ讲 VirtualService
kubectl apply -f virtualservice.yaml

# 閮ㄧ讲 DestinationRule
kubectl apply -f destinationrule.yaml

# 閮ㄧ讲 ServiceEntry
kubectl apply -f serviceentry.yaml

# 閮ㄧ讲瀹夊叏绛栫暐
kubectl apply -f peerauthentication.yaml
kubectl apply -f authorizationpolicy.yaml

# 閮ㄧ讲 Sidecar 閰嶇疆
kubectl apply -f sidecar.yaml
```

---

## 閰嶇疆璇存槑

### 1. Gateway (gateway.yaml)

Istio Gateway 绠＄悊缃戞牸鐨勫叆鍙ｅ拰鍑哄彛娴侀噺銆?

**鐗规€?**
- HTTP/HTTPS 鍏ュ彛
- TLS 缁堟 (浣跨敤 cert-manager)
- HTTP 鈫?HTTPS 鑷姩閲嶅畾鍚?
- 瀹夊叏鐨?TLS 閰嶇疆 (TLS 1.2/1.3)

**璁块棶搴旂敤:**
```bash
# 鑾峰彇 Ingress Gateway IP
export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')

# 璁块棶搴旂敤
curl -H "Host: est-app.example.com" http://$INGRESS_HOST:$INGRESS_PORT
```

### 2. VirtualService (virtualservice.yaml)

VirtualService 閰嶇疆娴侀噺璺敱瑙勫垯銆?

**鐗规€?**
- 鐏板害鍙戝竷 (90% v1, 10% v2)
- 璇锋眰瓒呮椂璁剧疆
- 鑷姩閲嶈瘯
- CORS 绛栫暐
- 瀹夊叏澶存敞鍏?
- 鏁呴殰娉ㄥ叆 (鐢ㄤ簬娴嬭瘯)
- 鍐呴儴鏈嶅姟璺敱鍒嗙

**鐏板害鍙戝竷璋冩暣:**
```bash
# 缂栬緫 virtualservice.yaml 璋冩暣鏉冮噸
# v1: 90%, v2: 10% 鈫?v1: 50%, v2: 50%
kubectl apply -f virtualservice.yaml
```

### 3. DestinationRule (destinationrule.yaml)

DestinationRule 閰嶇疆鐩爣鏈嶅姟鐨勭瓥鐣ャ€?

**鐗规€?**
- 璐熻浇鍧囪　绛栫暐 (LEAST_CONN)
- 杩炴帴姹犻厤缃?
- 寮傚父妫€娴?(鐔旀柇)
- 鍙屽悜 TLS (mTLS)
- 鐗堟湰瀛愰泦 (v1, v2)

**寮傚父妫€娴?**
- 杩炵画 5 娆?5xx 閿欒
- 30s 妫€鏌ラ棿闅?
- 30s 鍩哄噯椹遍€愭椂闂?
- 鏈€澶?50% 椹遍€愭瘮渚?

### 4. ServiceEntry (serviceentry.yaml)

ServiceEntry 閰嶇疆澶栭儴鏈嶅姟璁块棶銆?

**宸查厤缃殑鏈嶅姟:**
- PostgreSQL (鍐呴儴鏈嶅姟)
- Redis (鍐呴儴鏈嶅姟)
- 澶栭儴 API (api.example.com)
- 鐩戞帶鏈嶅姟 (Prometheus, Grafana, Jaeger)

**娣诲姞鏂扮殑澶栭儴鏈嶅姟:**
```yaml
apiVersion: networking.istio.io/v1alpha3
kind: ServiceEntry
metadata:
  name: my-external-service
spec:
  hosts:
  - myservice.com
  ports:
  - number: 443
    name: https
    protocol: HTTPS
  resolution: DNS
  location: MESH_EXTERNAL
```

### 5. PeerAuthentication (peerauthentication.yaml)

PeerAuthentication 閰嶇疆 mTLS 绛栫暐銆?

**绛栫暐:**
- 鍛藉悕绌洪棿绾у埆: STRICT mTLS
- 鍋ュ悍妫€鏌ョ鐐? PERMISSIVE (鍏煎 kubelet)

**mTLS 妯″紡:**
- `STRICT`: 鍙帴鍙?mTLS 娴侀噺
- `PERMISSIVE`: 鎺ュ彈鏄庢枃鍜?mTLS 娴侀噺
- `DISABLE`: 鍙帴鍙楁槑鏂囨祦閲?

### 6. AuthorizationPolicy (authorizationpolicy.yaml)

AuthorizationPolicy 閰嶇疆鏈嶅姟闂存巿鏉冦€?

**绛栫暐璇存槑:**
1. `est-allow-health-checks`: 鍏佽鎵€鏈変汉璁块棶 /health 鍜?/metrics
2. `est-allow-api-from-gateway`: 鍏佽浠?Ingress Gateway 璁块棶 /api/*
3. `est-deny-all-by-default`: 榛樿鎷掔粷鎵€鏈夊叾浠栬姹?

**娣诲姞鑷畾涔夋巿鏉?**
```yaml
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: allow-specific-service
spec:
  selector:
    matchLabels:
      app: est-web-app
  action: ALLOW
  rules:
  - from:
    - source:
        principals: ["cluster.local/ns/est/sa/other-service-sa"]
    to:
    - operation:
        methods: ["POST"]
        paths: ["/api/v1/*"]
```

### 7. Sidecar (sidecar.yaml)

Sidecar 閰嶇疆杈硅溅浠ｇ悊鐨勮涓恒€?

**鐗规€?**
- 鍑哄彛娴侀噺闄愬埗 (REGISTRY_ONLY)
- 鍑忓皯杈硅溅鍐呭瓨鍗犵敤
- 鎸囧畾鍏佽璁块棶鐨勬湇鍔?

**缃戠粶绛栫暐:**
- `REGISTRY_ONLY`: 鍙厑璁歌闂凡娉ㄥ唽鐨勬湇鍔?
- `ALLOW_ANY`: 鍏佽璁块棶浠讳綍鏈嶅姟 (榛樿)

---

## 娴侀噺绠＄悊

### 1. 鐏板害鍙戝竷 (Canary Release)

```bash
# 缂栬緫 virtualservice.yaml 璋冩暣娴侀噺鏉冮噸
# v1: 70%, v2: 30%
kubectl apply -f deploy/servicemesh/virtualservice.yaml

# 閫愭澧炲姞 v2 娴侀噺
# v1: 50%, v2: 50%
# v1: 30%, v2: 70%
# v1: 0%, v2: 100%
```

### 2. 娴侀噺闀滃儚 (Traffic Mirroring)

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: est-web-app-vs
spec:
  hosts:
  - "est-app.example.com"
  http:
  - route:
    - destination:
        host: est-web-app-service
        subset: v1
      weight: 100
    mirror:
      host: est-web-app-service
      subset: v2
    mirrorPercentage:
      value: 100.0
```

### 3. 璇锋眰瓒呮椂

```yaml
http:
- timeout: 5s
  route:
  - destination:
      host: est-web-app-service
```

### 4. 閲嶈瘯绛栫暐

```yaml
retries:
  attempts: 3
  perTryTimeout: 2s
  retryOn: "connect-failure,refused-stream"
```

### 5. 鐔旀柇 (Circuit Breaking)

鍦?destinationrule.yaml 涓凡閰嶇疆:
- 鏈€澶?100 涓?TCP 杩炴帴
- 鏈€澶?1024 涓?HTTP 璇锋眰
- 杩炵画 5 娆?5xx 閿欒瑙﹀彂鐔旀柇

---

## 瀹夊叏

### 1. 鍙屽悜 TLS (mTLS)

```bash
# 楠岃瘉 mTLS 鐘舵€?
istioctl pc security <pod-name> -n est

# 妫€鏌?PeerAuthentication
kubectl get peerauthentication -n est
```

### 2. 鎺堟潈绛栫暐

```bash
# 鏌ョ湅鎺堟潈绛栫暐
kubectl get authorizationpolicy -n est

# 娴嬭瘯鎺堟潈 (搴旇琚嫆缁?
kubectl exec -it <other-pod> -n other-ns -- curl est-web-app-service.est.svc.cluster.local/api/v1/data
```

### 3. JWT 璁よ瘉

娣诲姞 RequestAuthentication:
```yaml
apiVersion: security.istio.io/v1beta1
kind: RequestAuthentication
metadata:
  name: est-jwt-auth
  namespace: est
spec:
  selector:
    matchLabels:
      app: est-web-app
  jwtRules:
  - issuer: "https://est-app.example.com"
    jwksUri: "https://est-app.example.com/.well-known/jwks.json"
```

---

## 鍙娴嬫€?

### 1. 鐩戞帶 (Prometheus + Grafana)

```bash
# 瀹夎 Prometheus 鍜?Grafana (濡傛灉鏈畨瑁?
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/prometheus.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/grafana.yaml

# 璁块棶 Grafana
kubectl port-forward -n istio-system svc/grafana 3000:3000
# 鎵撳紑娴忚鍣? http://localhost:3000
```

### 2. 鍒嗗竷寮忚拷韪?(Jaeger)

```bash
# 瀹夎 Jaeger
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/jaeger.yaml

# 璁块棶 Jaeger
kubectl port-forward -n istio-system svc/jaeger-query 16686:16686
# 鎵撳紑娴忚鍣? http://localhost:16686
```

### 3. 璁块棶鏃ュ織 (Kiali)

```bash
# 瀹夎 Kiali
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.18/samples/addons/kiali.yaml

# 璁块棶 Kiali
kubectl port-forward -n istio-system svc/kiali 20001:20001
# 鎵撳紑娴忚鍣? http://localhost:20001
```

### 4. 鏌ョ湅娴侀噺鎸囨爣

```bash
# 鏌ョ湅鏈嶅姟鎸囨爣
kubectl exec -it <pod-name> -c istio-proxy -n est -- pilot-agent request GET /stats/prometheus

# 鎴栦娇鐢?istioctl
istioctl proxy-config stats <pod-name> -n est
```

---

## 鏁呴殰娉ㄥ叆

### 1. 寤惰繜娉ㄥ叆

宸插湪 virtualservice.yaml 涓厤缃?0.1% 娴侀噺寤惰繜 5s銆?

### 2. 閿欒娉ㄥ叆

```yaml
fault:
  abort:
    percentage:
      value: 0.1
    httpStatus: 500
```

---

## 甯哥敤鍛戒护

### 閮ㄧ讲鍜岄獙璇?

```bash
# 閮ㄧ讲鎵€鏈夐厤缃?
kubectl apply -k deploy/servicemesh

# 妫€鏌ヨ祫婧愮姸鎬?
kubectl get gateway,virtualservice,destinationrule,authorizationpolicy -n est

# 鏌ョ湅閰嶇疆璇︽儏
kubectl describe virtualservice est-web-app-vs -n est
```

### 娴侀噺绠＄悊

```bash
# 鏌ョ湅璺敱瑙勫垯
istioctl pc routes <pod-name> -n est -o json

# 鏌ョ湅绔偣
istioctl pc endpoints <pod-name> -n est

# 鏌ョ湅闆嗙兢淇℃伅
istioctl proxy-status
```

### 璋冭瘯

```bash
# 鏌ョ湅璁块棶鏃ュ織
kubectl logs <pod-name> -c istio-proxy -n est

# 杩涘叆杈硅溅浠ｇ悊
kubectl exec -it <pod-name> -c istio-proxy -n est -- bash

# 楠岃瘉閰嶇疆
istioctl analyze -n est
```

### 娓呯悊

```bash
# 鍒犻櫎鏈嶅姟缃戞牸閰嶇疆
kubectl delete -k deploy/servicemesh

# 绂佺敤 Sidecar 娉ㄥ叆
kubectl label namespace est istio-injection-

# 閲嶅惎 Pod 浠ョЩ闄?Sidecar
kubectl rollout restart deployment est-web-app -n est
```

---

## 鎬ц兘浼樺寲

### 1. 闄愬埗 Sidecar 璧勬簮

鏇存柊 deployment.yaml 娣诲姞璧勬簮闄愬埗:
```yaml
template:
  metadata:
    annotations:
      sidecar.istio.io/proxyCPU: "100m"
      sidecar.istio.io/proxyMemory: "128Mi"
      sidecar.istio.io/proxyCPULimit: "500m"
      sidecar.istio.io/proxyMemoryLimit: "512Mi"
```

### 2. 浣跨敤 Sidecar 璧勬簮闄愬埗

宸插湪 sidecar.yaml 涓厤缃?`REGISTRY_ONLY` 妯″紡锛屽噺灏戝唴瀛樹娇鐢ㄣ€?

### 3. 绂佺敤涓嶉渶瑕佺殑鍗忚

```yaml
apiVersion: meshconfig/v1alpha1
kind: MeshConfig
spec:
  defaultConfig:
    proxyStatsMatcher:
      inclusionPrefixes:
      - "http."
      - "listener."
```

---

## 鐢熶骇鐜妫€鏌ユ竻鍗?

- [ ] Istio 鐢熶骇閰嶇疆 (profile=default)
- [ ] mTLS 涓ユ牸妯″紡 (STRICT)
- [ ] 鎺堟潈绛栫暐 (榛樿鎷掔粷)
- [ ] Sidecar 璧勬簮闄愬埗
- [ ] 鐩戞帶鍜屽憡璀?(Prometheus + Grafana)
- [ ] 鍒嗗竷寮忚拷韪?(Jaeger)
- [ ] 璁块棶鏃ュ織鏀堕泦
- [ ] 鐔旀柇閰嶇疆
- [ ] 閲嶈瘯绛栫暐
- [ ] 瓒呮椂璁剧疆
- [ ] 鍋ュ悍妫€鏌ラ厤缃?
- [ ] 璇佷功绠＄悊 (cert-manager)
- [ ] 瀹氭湡 Istio 鐗堟湰鏇存柊
- [ ] 鐏鹃毦鎭㈠婕旂粌

---

## 鍙傝€冭祫婧?

- [Istio 瀹樻柟鏂囨。](https://istio.io/latest/docs/)
- [Istio 鏈€浣冲疄璺礭(https://istio.io/latest/docs/ops/best-practices/)
- [EST 妗嗘灦 Kubernetes 閮ㄧ讲](../k8s/README.md)
- [Kubernetes 瀹樻柟鏂囨。](https://kubernetes.io/docs/)
