import request from '@/utils/request'

export interface Menu {
  id?: string
  parentId: string
  name: string
  icon?: string
  sort: number
  path?: string
  component?: string
  permission?: string
  type: number
  status: number
  remark?: string
  children?: Menu[]
}

export function listMenus() {
  return request<Menu[]>({
    url: '/admin/api/menus',
    method: 'get'
  })
}

export function getMenu(id: string) {
  return request<Menu>({
    url: `/admin/api/menus/${id}`,
    method: 'get'
  })
}

export function createMenu(data: Partial<Menu>) {
  return request({
    url: '/admin/api/menus',
    method: 'post',
    data
  })
}

export function updateMenu(data: Partial<Menu>) {
  return request({
    url: `/admin/api/menus/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteMenu(id: string) {
  return request({
    url: `/admin/api/menus/${id}`,
    method: 'delete'
  })
}
