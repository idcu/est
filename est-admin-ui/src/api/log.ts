import request from '@/utils/request'

export interface OperationLog {
  id: string
  userId?: string
  username: string
  module?: string
  operation?: string
  method?: string
  params?: string
  time?: number
  ip?: string
  userAgent?: string
  status: number
  errorMsg?: string
  createdAt: number
}

export interface LoginLog {
  id: string
  userId?: string
  username: string
  ip?: string
  userAgent?: string
  status: number
  errorMsg?: string
  createdAt: number
}

export function getOperationLogs() {
  return request.get<OperationLog[]>('/admin/api/logs/operations')
}

export function getOperationLog(id: string) {
  return request.get<OperationLog>(`/admin/api/logs/operations/${id}`)
}

export function deleteOperationLog(id: string) {
  return request.delete(`/admin/api/logs/operations/${id}`)
}

export function clearOldOperationLogs(beforeTime?: number) {
  const params = beforeTime ? { beforeTime } : {}
  return request.post('/admin/api/logs/operations/clear', null, { params })
}

export function getLoginLogs() {
  return request.get<LoginLog[]>('/admin/api/logs/logins')
}

export function getLoginLog(id: string) {
  return request.get<LoginLog>(`/admin/api/logs/logins/${id}`)
}

export function deleteLoginLog(id: string) {
  return request.delete(`/admin/api/logs/logins/${id}`)
}

export function clearOldLoginLogs(beforeTime?: number) {
  const params = beforeTime ? { beforeTime } : {}
  return request.post('/admin/api/logs/logins/clear', null, { params })
}
