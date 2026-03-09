import request from '@/utils/request'

export interface WorkflowDefinition {
  id: string
  name: string
  description?: string
  definition: string
  version: number
  status: number
  createTime?: string
  updateTime?: string
}

export interface WorkflowInstance {
  id: string
  definitionId: string
  definitionName?: string
  businessKey?: string
  status: string
  currentNode?: string
  variables: Record<string, any>
  createTime?: string
  updateTime?: string
  startTime?: string
  endTime?: string
}

export interface WorkflowDefinitionQuery {
  page?: number
  size?: number
  name?: string
  status?: number
}

export interface WorkflowInstanceQuery {
  page?: number
  size?: number
  definitionId?: string
  status?: string
}

export interface WorkflowDefinitionListResponse {
  list: WorkflowDefinition[]
  total: number
}

export interface WorkflowInstanceListResponse {
  list: WorkflowInstance[]
  total: number
}

export function listWorkflowDefinitions(params?: WorkflowDefinitionQuery) {
  return request<WorkflowDefinitionListResponse>({
    url: '/admin/api/workflow/definitions',
    method: 'get',
    params
  })
}

export function getWorkflowDefinition(id: string) {
  return request<WorkflowDefinition>({
    url: `/admin/api/workflow/definitions/${id}`,
    method: 'get'
  })
}

export function createWorkflowDefinition(data: Partial<WorkflowDefinition>) {
  return request({
    url: '/admin/api/workflow/definitions',
    method: 'post',
    data
  })
}

export function updateWorkflowDefinition(data: Partial<WorkflowDefinition>) {
  return request({
    url: `/admin/api/workflow/definitions/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteWorkflowDefinition(id: string) {
  return request({
    url: `/admin/api/workflow/definitions/${id}`,
    method: 'delete'
  })
}

export function listWorkflowInstances(params?: WorkflowInstanceQuery) {
  return request<WorkflowInstanceListResponse>({
    url: '/admin/api/workflow/instances',
    method: 'get',
    params
  })
}

export function getWorkflowInstance(id: string) {
  return request<WorkflowInstance>({
    url: `/admin/api/workflow/instances/${id}`,
    method: 'get'
  })
}

export function startWorkflowInstance(data: {
  definitionId: string
  businessKey?: string
  variables?: Record<string, any>
}) {
  return request({
    url: '/admin/api/workflow/instances/start',
    method: 'post',
    data
  })
}

export function pauseWorkflowInstance(id: string) {
  return request({
    url: `/admin/api/workflow/instances/${id}/pause`,
    method: 'post'
  })
}

export function resumeWorkflowInstance(id: string) {
  return request({
    url: `/admin/api/workflow/instances/${id}/resume`,
    method: 'post'
  })
}

export function cancelWorkflowInstance(id: string) {
  return request({
    url: `/admin/api/workflow/instances/${id}/cancel`,
    method: 'post'
  })
}

export function retryWorkflowInstance(id: string) {
  return request({
    url: `/admin/api/workflow/instances/${id}/retry`,
    method: 'post'
  })
}

export function getWorkflowInstanceVariables(id: string) {
  return request<Record<string, any>>({
    url: `/admin/api/workflow/instances/${id}/variables`,
    method: 'get'
  })
}

export function setWorkflowInstanceVariables(id: string, variables: Record<string, any>) {
  return request({
    url: `/admin/api/workflow/instances/${id}/variables`,
    method: 'post',
    data: variables
  })
}
