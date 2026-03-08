import request from '@/utils/request'

export interface Department {
  id?: string
  parentId: string
  name: string
  code: string
  sort: number
  leader?: string
  phone?: string
  email?: string
  status: number
  remark?: string
  children?: Department[]
}

export interface DepartmentQuery {
  name?: string
  status?: number
}

export function listDepartments(params?: DepartmentQuery) {
  return request<Department[]>({
    url: '/admin/api/departments',
    method: 'get',
    params
  })
}

export function getDepartment(id: string) {
  return request<Department>({
    url: `/admin/api/departments/${id}`,
    method: 'get'
  })
}

export function createDepartment(data: Partial<Department>) {
  return request({
    url: '/admin/api/departments',
    method: 'post',
    data
  })
}

export function updateDepartment(data: Partial<Department>) {
  return request({
    url: `/admin/api/departments/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteDepartment(id: string) {
  return request({
    url: `/admin/api/departments/${id}`,
    method: 'delete'
  })
}
