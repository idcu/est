import request from '@/utils/request'

export interface JvmMetrics {
  metrics: Record<string, any>
  uptime: number
  jvmInfo: string
  health: any
}

export interface SystemMetrics {
  metrics: Record<string, any>
  osInfo: string
  health: any
}

export interface HealthChecks {
  jvm: any[]
  system: any[]
}

export interface OnlineUser {
  sessionId: string
  userId: string
  username: string
  ip: string
  browser: string
  loginTime: number
  lastActivityTime: number
}

export interface CacheStatistics {
  caches: Array<{
    name: string
    size: number
    hitCount: number
    missCount: number
    hitRate: number
  }>
  totalSize: number
  totalHitCount: number
  totalMissCount: number
  totalHitRate: number
}

export interface AllMetrics {
  jvm: JvmMetrics
  system: SystemMetrics
  health: HealthChecks
}

export function getJvmMetrics() {
  return request.get<JvmMetrics>('/admin/api/monitor/jvm')
}

export function getSystemMetrics() {
  return request.get<SystemMetrics>('/admin/api/monitor/system')
}

export function getHealthChecks() {
  return request.get<HealthChecks>('/admin/api/monitor/health')
}

export function getAllMetrics() {
  return request.get<AllMetrics>('/admin/api/monitor/all')
}

export function getOnlineUsers() {
  return request.get<{ data: OnlineUser[], count: number }>('/admin/api/monitor/online-users')
}

export function forceLogout(sessionId: string) {
  return request.post(`/admin/api/monitor/online-users/${sessionId}/force-logout`)
}

export function getCacheStatistics() {
  return request.get<CacheStatistics>('/admin/api/monitor/cache')
}

export function getCacheKeys() {
  return request.get<Record<string, string[]>>('/admin/api/monitor/cache/keys')
}

export function clearCache(cacheName: string) {
  return request.post(`/admin/api/monitor/cache/${cacheName}/clear`)
}

export function clearAllCaches() {
  return request.post('/admin/api/monitor/cache/clear-all')
}
