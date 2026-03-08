import request from '@/utils/request'

export interface MenuInfo {
  id: string
  parentId: string | null
  name: string
  path: string
  component: string
  icon: string
  type: number
  sort: number
  permissions: string[]
  visible: boolean
  cache: boolean
  children?: MenuInfo[]
}

export function getMenuList() {
  return request.get<MenuInfo[]>('/admin/api/menus')
}

export function getMenuTree() {
  return request.get<MenuInfo[]>('/admin/api/menus/tree')
}

export function getUserMenus() {
  return request.get<MenuInfo[]>('/admin/api/menus/user')
}

export function getMenu(id: string) {
  return request.get<MenuInfo>(`/admin/api/menus/${id}`)
}

export function createMenu(data: any) {
  return request.post('/admin/api/menus', data)
}

export function updateMenu(id: string, data: any) {
  return request.put(`/admin/api/menus/${id}`, data)
}

export function deleteMenu(id: string) {
  return request.delete(`/admin/api/menus/${id}`)
}
