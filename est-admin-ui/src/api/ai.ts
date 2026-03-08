import request from '@/utils/request'

export interface PromptTemplate {
  name: string
  category: string
  description: string
  template: string
  requiredVariables: Record<string, string>
}

export function chat(message: string) {
  return request.post<string>('/admin/api/ai/chat', null, {
    params: { message }
  })
}

export function generateCode(requirement: string) {
  return request.post<string>('/admin/api/ai/code/generate', null, {
    params: { requirement }
  })
}

export function suggestCode(requirement: string) {
  return request.post<string>('/admin/api/ai/code/suggest', null, {
    params: { requirement }
  })
}

export function explainCode(code: string) {
  return request.post<string>('/admin/api/ai/code/explain', null, {
    params: { code }
  })
}

export function optimizeCode(code: string) {
  return request.post<string>('/admin/api/ai/code/optimize', null, {
    params: { code }
  })
}

export function getQuickReference(topic: string) {
  return request.get<string>('/admin/api/ai/reference', {
    params: { topic }
  })
}

export function getBestPractice(category: string) {
  return request.get<string>('/admin/api/ai/bestpractice', {
    params: { category }
  })
}

export function getTutorial(topic: string) {
  return request.get<string>('/admin/api/ai/tutorial', {
    params: { topic }
  })
}

export function listTemplates() {
  return request.get<PromptTemplate[]>('/admin/api/ai/templates')
}

export function generatePrompt(data: {
  templateName: string
  variableNames?: string
  variableValues?: string
}) {
  return request.post<string>('/admin/api/ai/templates/generate', null, {
    params: data
  })
}
