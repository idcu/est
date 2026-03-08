import request from '@/utils/request'

export interface Tenant {
  id?: string
  name: string
  code: string
  type: number
  contactName?: string
  contactPhone?: string
  contactEmail?: string
  expireTime?: string
  status: number
  remark?: string
  createTime?: string
}

export interface TenantQuery {
  page?: number
  size?: number
  name?: string
  code?: string
  status?: number
}

export interface TenantListResponse {
  list: Tenant[]
  total: number
}

export function listTenants(params?: TenantQuery) {
  return request<TenantListResponse>({
    url: '/admin/api/tenants',
    method: 'get',
    params
  })
}

export function getTenant(id: string) {
  return request<Tenant>({
    url: `/admin/api/tenants/${id}`,
    method: 'get'
  })
}

export function createTenant(data: Partial<Tenant>) {
  return request({
    url: '/admin/api/tenants',
    method: 'post',
    data
  })
}

export function updateTenant(data: Partial<Tenant>) {
  return request({
    url: `/admin/api/tenants/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteTenant(id: string) {
  return request({
    url: `/admin/api/tenants/${id}`,
    method: 'delete'
  })
}

export function updateTenantStatus(id: string, status: number) {
  return request({
    url: `/admin/api/tenants/${id}`,
    method: 'put',
    data: { status }
  })
}
