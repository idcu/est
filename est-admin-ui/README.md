# EST Admin UI - 鍚庡彴绠＄悊绯荤粺鍓嶇

鍩轰簬 Vue 3 + Element Plus + Vite 鐨勭幇浠ｅ寲鍚庡彴绠＄悊绯荤粺鍓嶇銆?
## 鎶€鏈爤

- **妗嗘灦**: Vue 3.x (Composition API)
- **UI 缁勪欢搴?*: Element Plus
- **鐘舵€佺鐞?*: Pinia
- **璺敱**: Vue Router 4.x
- **HTTP 瀹㈡埛绔?*: Axios
- **鏋勫缓宸ュ叿**: Vite
- **璇█**: TypeScript

## 椤圭洰缁撴瀯

```
est-admin-ui/
鈹溾攢鈹€ public/                 # 闈欐€佽祫婧?鈹溾攢鈹€ src/
鈹?  鈹溾攢鈹€ api/               # API 鎺ュ彛灏佽
鈹?  鈹?  鈹溾攢鈹€ auth.ts        # 璁よ瘉 API
鈹?  鈹?  鈹溾攢鈹€ user.ts        # 鐢ㄦ埛绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ role.ts        # 瑙掕壊绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ menu.ts        # 鑿滃崟绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ department.ts  # 閮ㄩ棬绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ tenant.ts      # 绉熸埛绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ log.ts         # 鏃ュ織绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ monitor.ts     # 鐩戞帶绠＄悊 API
鈹?  鈹?  鈹溾攢鈹€ integration.ts # 绗笁鏂归泦鎴?API
鈹?  鈹?  鈹斺攢鈹€ ai.ts          # AI 鍔╂墜 API
鈹?  鈹溾攢鈹€ assets/            # 璧勬簮鏂囦欢
鈹?  鈹溾攢鈹€ components/        # 鍏叡缁勪欢
鈹?  鈹?  鈹斺攢鈹€ layout/       # 甯冨眬缁勪欢
鈹?  鈹?      鈹斺攢鈹€ Layout.vue
鈹?  鈹溾攢鈹€ directives/        # 鑷畾涔夋寚浠?鈹?  鈹?  鈹斺攢鈹€ permission.ts  # 鏉冮檺鎸囦护
鈹?  鈹溾攢鈹€ router/            # 璺敱閰嶇疆
鈹?  鈹?  鈹斺攢鈹€ index.ts
鈹?  鈹溾攢鈹€ stores/            # Pinia 鐘舵€佺鐞?鈹?  鈹?  鈹溾攢鈹€ app.ts         # 搴旂敤鐘舵€?鈹?  鈹?  鈹斺攢鈹€ user.ts        # 鐢ㄦ埛鐘舵€?鈹?  鈹溾攢鈹€ utils/             # 宸ュ叿鍑芥暟
鈹?  鈹?  鈹斺攢鈹€ request.ts     # Axios 灏佽
鈹?  鈹溾攢鈹€ views/             # 椤甸潰缁勪欢
鈹?  鈹?  鈹溾攢鈹€ login/         # 鐧诲綍椤甸潰
鈹?  鈹?  鈹溾攢鈹€ dashboard/     # 浠〃鏉?鈹?  鈹?  鈹溾攢鈹€ system/        # 绯荤粺绠＄悊
鈹?  鈹?  鈹?  鈹溾攢鈹€ User.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ Role.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ Menu.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ Department.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ Tenant.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ OperationLog.vue
鈹?  鈹?  鈹?  鈹斺攢鈹€ LoginLog.vue
鈹?  鈹?  鈹溾攢鈹€ monitor/       # 绯荤粺鐩戞帶
鈹?  鈹?  鈹?  鈹溾攢鈹€ ServiceMonitor.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ OnlineUser.vue
鈹?  鈹?  鈹?  鈹斺攢鈹€ CacheMonitor.vue
鈹?  鈹?  鈹溾攢鈹€ integration/   # 绗笁鏂归泦鎴?鈹?  鈹?  鈹?  鈹溾攢鈹€ Email.vue
鈹?  鈹?  鈹?  鈹溾攢鈹€ Sms.vue
鈹?  鈹?  鈹?  鈹斺攢鈹€ Oss.vue
鈹?  鈹?  鈹斺攢鈹€ ai/            # AI 鍔╂墜
鈹?  鈹?      鈹溾攢鈹€ Chat.vue
鈹?  鈹?      鈹溾攢鈹€ Code.vue
鈹?  鈹?      鈹溾攢鈹€ Reference.vue
鈹?  鈹?      鈹斺攢鈹€ Template.vue
鈹?  鈹溾攢鈹€ App.vue            # 鏍圭粍浠?鈹?  鈹斺攢鈹€ main.ts            # 搴旂敤鍏ュ彛
鈹溾攢鈹€ .env.development       # 寮€鍙戠幆澧冨彉閲?鈹溾攢鈹€ .env.production        # 鐢熶骇鐜鍙橀噺
鈹溾攢鈹€ index.html
鈹溾攢鈹€ package.json
鈹溾攢鈹€ tsconfig.json
鈹溾攢鈹€ tsconfig.node.json
鈹溾攢鈹€ vite.config.ts
鈹斺攢鈹€ README.md
```

## 蹇€熷紑濮?
### 瀹夎渚濊禆

```bash
npm install
```

### 寮€鍙戞ā寮?
```bash
npm run dev
```

寮€鍙戞湇鍔″櫒灏嗗湪 http://localhost:3000 鍚姩

### 鏋勫缓鐢熶骇鐗堟湰

```bash
npm run build
```

### 棰勮鐢熶骇鏋勫缓

```bash
npm run preview
```

## 榛樿璐﹀彿

- 鐢ㄦ埛鍚? `admin`
- 瀵嗙爜: `admin123`

## 鍔熻兘鐗规€?
### 璁よ瘉鎺堟潈
- JWT Token 璁よ瘉
- 鑷姩 Token 鍒锋柊
- 璺敱瀹堝崼
- 鏈湴瀛樺偍鎸佷箙鍖?
### 绯荤粺绠＄悊
- 鐢ㄦ埛绠＄悊 (CRUD)
- 瑙掕壊绠＄悊 (CRUD + 鏉冮檺鍒嗛厤)
- 鑿滃崟绠＄悊 (鏍戝舰缁撴瀯)
- 閮ㄩ棬绠＄悊 (鏍戝舰缁撴瀯)
- 绉熸埛绠＄悊 (涓夌妯″紡)
- 鎿嶄綔鏃ュ織绠＄悊
- 鐧诲綍鏃ュ織绠＄悊

### 绯荤粺鐩戞帶
- 鏈嶅姟鐩戞帶 (JVM銆佺郴缁熸寚鏍?
- 鍦ㄧ嚎鐢ㄦ埛绠＄悊
- 缂撳瓨鐩戞帶

### 绗笁鏂归泦鎴?- 閭欢鏈嶅姟閰嶇疆
- 鐭俊鏈嶅姟閰嶇疆
- 瀵硅薄瀛樺偍 (OSS) 閰嶇疆

### AI 鍔╂墜
- AI 瀵硅瘽鑱婂ぉ
- 浠ｇ爜鐢熸垚
- 浠ｇ爜瑙ｉ噴
- 浠ｇ爜浼樺寲
- 寮€鍙戝弬鑰冩煡璇?- 鏈€浣冲疄璺垫煡璇?- 鏁欑▼鏌ヨ
- 鎻愮ず妯℃澘绠＄悊

### 鍏朵粬鐗规€?- 鍝嶅簲寮忓竷灞€
- 鐘舵€佺鐞?- API 璇锋眰灏佽
- 缁熶竴閿欒澶勭悊
- 鏉冮檺鎸囦护
- 鏉冮檺妫€鏌?
## 鐜鍙橀噺

### .env.development
```env
VITE_API_BASE_URL=/admin/api
```

### .env.production
```env
VITE_API_BASE_URL=https://your-api-domain.com/admin/api
```

## 鍚庣鎺ュ彛

鍚庣闇€瑕佽繍琛屽湪 8080 绔彛锛屾彁渚涗互涓?API:

### 璁よ瘉鎺ュ彛
- `POST /admin/api/auth/login` - 鐧诲綍
- `POST /admin/api/auth/logout` - 鐧诲嚭
- `GET /admin/api/auth/current` - 鑾峰彇褰撳墠鐢ㄦ埛
- `POST /admin/api/auth/refresh-token` - 鍒锋柊 Token

### 绯荤粺绠＄悊鎺ュ彛
- `GET /admin/api/users` - 鐢ㄦ埛鍒楄〃
- `POST /admin/api/users` - 鍒涘缓鐢ㄦ埛
- `PUT /admin/api/users/:id` - 鏇存柊鐢ㄦ埛
- `DELETE /admin/api/users/:id` - 鍒犻櫎鐢ㄦ埛
- `GET /admin/api/roles` - 瑙掕壊鍒楄〃
- `POST /admin/api/roles` - 鍒涘缓瑙掕壊
- `PUT /admin/api/roles/:id` - 鏇存柊瑙掕壊
- `DELETE /admin/api/roles/:id` - 鍒犻櫎瑙掕壊
- `GET /admin/api/menus` - 鑿滃崟鍒楄〃
- `POST /admin/api/menus` - 鍒涘缓鑿滃崟
- `PUT /admin/api/menus/:id` - 鏇存柊鑿滃崟
- `DELETE /admin/api/menus/:id` - 鍒犻櫎鑿滃崟
- `GET /admin/api/departments` - 閮ㄩ棬鍒楄〃
- `POST /admin/api/departments` - 鍒涘缓閮ㄩ棬
- `PUT /admin/api/departments/:id` - 鏇存柊閮ㄩ棬
- `DELETE /admin/api/departments/:id` - 鍒犻櫎閮ㄩ棬
- `GET /admin/api/tenants` - 绉熸埛鍒楄〃
- `POST /admin/api/tenants` - 鍒涘缓绉熸埛
- `PUT /admin/api/tenants/:id` - 鏇存柊绉熸埛
- `DELETE /admin/api/tenants/:id` - 鍒犻櫎绉熸埛

### 鏃ュ織绠＄悊鎺ュ彛
- `GET /admin/api/operation-logs` - 鎿嶄綔鏃ュ織鍒楄〃
- `GET /admin/api/operation-logs/:id` - 鑾峰彇鎿嶄綔鏃ュ織璇︽儏
- `DELETE /admin/api/operation-logs/:id` - 鍒犻櫎鎿嶄綔鏃ュ織
- `DELETE /admin/api/operation-logs` - 娓呯┖鎿嶄綔鏃ュ織
- `GET /admin/api/login-logs` - 鐧诲綍鏃ュ織鍒楄〃
- `GET /admin/api/login-logs/:id` - 鑾峰彇鐧诲綍鏃ュ織璇︽儏
- `DELETE /admin/api/login-logs/:id` - 鍒犻櫎鐧诲綍鏃ュ織
- `DELETE /admin/api/login-logs` - 娓呯┖鐧诲綍鏃ュ織

### 鐩戞帶鎺ュ彛
- `GET /admin/api/monitor/jvm` - JVM 鐩戞帶鎸囨爣
- `GET /admin/api/monitor/system` - 绯荤粺鐩戞帶鎸囨爣
- `GET /admin/api/monitor/health` - 鍋ュ悍妫€鏌?- `GET /admin/api/monitor/metrics` - 鎵€鏈夌洃鎺ф寚鏍?- `GET /admin/api/online-users` - 鍦ㄧ嚎鐢ㄦ埛鍒楄〃
- `GET /admin/api/online-users/count` - 鍦ㄧ嚎鐢ㄦ埛鏁?- `GET /admin/api/cache/statistics` - 缂撳瓨缁熻
- `GET /admin/api/cache/keys` - 缂撳瓨閿垪琛?
### 绗笁鏂归泦鎴愭帴鍙?- `GET /admin/api/integration/email` - 鑾峰彇閭欢閰嶇疆
- `PUT /admin/api/integration/email` - 鏇存柊閭欢閰嶇疆
- `GET /admin/api/integration/sms` - 鑾峰彇鐭俊閰嶇疆
- `PUT /admin/api/integration/sms` - 鏇存柊鐭俊閰嶇疆
- `GET /admin/api/integration/oss` - 鑾峰彇 OSS 閰嶇疆
- `PUT /admin/api/integration/oss` - 鏇存柊 OSS 閰嶇疆

### AI 鍔╂墜鎺ュ彛
- `POST /admin/api/ai/chat` - AI 瀵硅瘽
- `POST /admin/api/ai/code/generate` - 浠ｇ爜鐢熸垚
- `POST /admin/api/ai/code/suggest` - 浠ｇ爜寤鸿
- `POST /admin/api/ai/code/explain` - 浠ｇ爜瑙ｉ噴
- `POST /admin/api/ai/code/optimize` - 浠ｇ爜浼樺寲
- `GET /admin/api/ai/reference` - 寮€鍙戝弬鑰?- `GET /admin/api/ai/bestpractice` - 鏈€浣冲疄璺?- `GET /admin/api/ai/tutorial` - 鏁欑▼
- `GET /admin/api/ai/templates` - 鎻愮ず妯℃澘鍒楄〃
- `POST /admin/api/ai/templates/generate` - 鐢熸垚鎻愮ず

## 寮€鍙戞寚鍗?
### 娣诲姞鏂伴〉闈?
1. 鍦?`src/views/` 涓嬪垱寤洪〉闈㈢粍浠?2. 鍦?`src/router/index.ts` 涓坊鍔犺矾鐢?3. 濡傛灉闇€瑕?API锛屽湪 `src/api/` 涓嬪垱寤哄搴旂殑 API 鏂囦欢

### 娣诲姞鏂扮殑 API 鎺ュ彛

```typescript
import request from '@/utils/request'

export interface YourData {
  id: string
  name: string
}

export function listYourData() {
  return request<YourData[]>({
    url: '/admin/api/your-data',
    method: 'get'
  })
}
```

### 浣跨敤 Store

```typescript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 妫€鏌ョ櫥褰曠姸鎬?if (userStore.isLoggedIn()) {
  // 宸茬櫥褰?}

// 妫€鏌ユ潈闄?if (userStore.hasPermission('system:user:add')) {
  // 鏈夋潈闄?}
```

## 璁稿彲璇?
MIT License
