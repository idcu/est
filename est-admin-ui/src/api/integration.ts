import request from '@/utils/request'

export interface EmailTemplate {
  name: string
  subject: string
  content: string
  html: boolean
  createdAt: number
  updatedAt: number
}

export interface SmsTemplate {
  code: string
  name: string
  content: string
  provider: string
  createdAt: number
  updatedAt: number
}

export interface OssFile {
  bucketName: string
  fileName: string
  filePath: string
  size: number
  contentType: string
  lastModified: number
  etag: string
  url: string
}

export function sendEmail(data: {
  to: string
  subject: string
  content: string
  isHtml: boolean
}) {
  return request.post('/admin/api/integration/email/send', data)
}

export function listEmailTemplates() {
  return request.get<EmailTemplate[]>('/admin/api/integration/email/templates')
}

export function sendSms(data: {
  phone: string
  content: string
}) {
  return request.post('/admin/api/integration/sms/send', data)
}

export function listSmsTemplates() {
  return request.get<SmsTemplate[]>('/admin/api/integration/sms/templates')
}

export function listBuckets() {
  return request.get<string[]>('/admin/api/integration/oss/buckets')
}

export function listFiles(data: {
  bucketName: string
  prefix?: string
}) {
  return request.get<OssFile[]>('/admin/api/integration/oss/files', { params: data })
}

export function uploadFile(data: {
  bucketName: string
  fileName: string
  file: File
}) {
  const formData = new FormData()
  formData.append('bucketName', data.bucketName)
  formData.append('fileName', data.fileName)
  formData.append('file', data.file)
  return request.post<OssFile>('/admin/api/integration/oss/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function deleteFile(data: {
  bucketName: string
  fileName: string
}) {
  return request.post('/admin/api/integration/oss/delete', data)
}
