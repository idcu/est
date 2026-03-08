import request from '@/utils/request'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: {
    id: string
    username: string
    email: string
    roles: string[]
    permissions: string[]
  }
}

export interface UserInfo {
  id: string
  username: string
  email: string
  roles: string[]
  permissions: string[]
  active: boolean
}

export function login(data: LoginRequest) {
  return request<LoginResponse>({
    url: '/admin/api/auth/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/admin/api/auth/logout',
    method: 'post'
  })
}

export function getCurrentUser() {
  return request<UserInfo>({
    url: '/admin/api/auth/current',
    method: 'get'
  })
}

export function refreshToken() {
  return request<{ token: string }>({
    url: '/admin/api/auth/refresh-token',
    method: 'post'
  })
}
