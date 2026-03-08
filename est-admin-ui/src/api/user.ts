import request from '@/utils/request'

export interface User {
  id: string
  username: string
  nickname?: string
  email: string
  phone?: string
  roles: string[]
  permissions: string[]
  status: number
  remark?: string
  createTime?: string
}

export interface UserQuery {
  page?: number
  size?: number
  username?: string
  nickname?: string
  status?: number
}

export interface UserListResponse {
  list: User[]
  total: number
}

export function listUsers(params?: UserQuery) {
  return request<UserListResponse>({
    url: '/admin/api/users',
    method: 'get',
    params
  })
}

export function getUser(id: string) {
  return request<User>({
    url: `/admin/api/users/${id}`,
    method: 'get'
  })
}

export function createUser(data: Partial<User>) {
  return request({
    url: '/admin/api/users',
    method: 'post',
    data
  })
}

export function updateUser(data: Partial<User>) {
  return request({
    url: `/admin/api/users/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id: string) {
  return request({
    url: `/admin/api/users/${id}`,
    method: 'delete'
  })
}

export function updateUserStatus(id: string, status: number) {
  return request({
    url: `/admin/api/users/${id}`,
    method: 'put',
    data: { status }
  })
}

