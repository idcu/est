import request from '@/utils/request'

export type PermissionRuleType = 'ROW_LEVEL' | 'FIELD_LEVEL' | 'COMBINED'

export type FieldMaskType = 'HIDDEN' | 'READ_ONLY' | 'MASKED'

export interface RowCondition {
  id?: string
  fieldName: string
  operator: string
  value: string
}

export interface FieldMask {
  id?: string
  fieldName: string
  maskType: FieldMaskType
}

export interface DataPermissionRule {
  id: string
  name: string
  description?: string
  ruleType: PermissionRuleType
  resourceType: string
  rowConditions: RowCondition[]
  fieldMasks: FieldMask[]
  roleIds: string[]
  status: number
  createTime?: string
  updateTime?: string
}

export interface DataPermissionRuleQuery {
  page?: number
  size?: number
  name?: string
  ruleType?: PermissionRuleType
  resourceType?: string
  status?: number
}

export interface DataPermissionRuleListResponse {
  list: DataPermissionRule[]
  total: number
}

export function listDataPermissionRules(params?: DataPermissionRuleQuery) {
  return request<DataPermissionRuleListResponse>({
    url: '/admin/api/data-permission/rules',
    method: 'get',
    params
  })
}

export function getDataPermissionRule(id: string) {
  return request<DataPermissionRule>({
    url: `/admin/api/data-permission/rules/${id}`,
    method: 'get'
  })
}

export function createDataPermissionRule(data: Partial<DataPermissionRule>) {
  return request({
    url: '/admin/api/data-permission/rules',
    method: 'post',
    data
  })
}

export function updateDataPermissionRule(data: Partial<DataPermissionRule>) {
  return request({
    url: `/admin/api/data-permission/rules/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteDataPermissionRule(id: string) {
  return request({
    url: `/admin/api/data-permission/rules/${id}`,
    method: 'delete'
  })
}

export function assignRolePermissions(ruleId: string, roleIds: string[]) {
  return request({
    url: `/admin/api/data-permission/rules/${ruleId}/roles`,
    method: 'put',
    data: { roleIds }
  })
}

export function checkRowPermission(
  resourceType: string,
  data: Record<string, any>
) {
  return request<{ hasPermission: boolean; allowedFields?: string[] }>({
    url: '/admin/api/data-permission/check-row',
    method: 'post',
    data: { resourceType, data }
  })
}

export function getAccessibleFields(resourceType: string) {
  return request<string[]>({
    url: `/admin/api/data-permission/accessible-fields/${resourceType}`,
    method: 'get'
  })
}
