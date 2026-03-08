import request from '@/utils/request'

export interface Role {
  id?: string
  name: string
  code: string
  sort: number
  status: number
  remark?: string
  menuIds?: string[]
  createTime?: string
}

export interface RoleQuery {
  page?: number
  size?: number
  name?: string
  code?: string
  status?: number
}

export interface RoleListResponse {
  list: Role[]
  total: number
}

export function listRoles(params?: RoleQuery) {
  return request<RoleListResponse>({
    url: '/admin/api/roles',
    method: 'get',
    params
  })
}

export function getRole(id: string) {
  return request<Role>({
    url: `/admin/api/roles/${id}`,
    method: 'get'
  })
}

export function createRole(data: Partial<Role>) {
  return request({
    url: '/admin/api/roles',
    method: 'post',
    data
  })
}

export function updateRole(data: Partial<Role>) {
  return request({
    url: `/admin/api/roles/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id: string) {
  return request({
    url: `/admin/api/roles/${id}`,
    method: 'delete'
  })
}

export function updateRoleStatus(id: string, status: number) {
  return request({
    url: `/admin/api/roles/${id}`,
    method: 'put',
    data: { status }
  })
}
