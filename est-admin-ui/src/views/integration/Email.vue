<template>
  <div class="email-service">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>邮件服务</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="发送邮件" name="send">
          <el-form :model="emailForm" label-width="100px" style="max-width: 600px;">
            <el-form-item label="收件人">
              <el-input v-model="emailForm.to" placeholder="请输入收件人邮箱" />
            </el-form-item>
            <el-form-item label="主题">
              <el-input v-model="emailForm.subject" placeholder="请输入邮件主题" />
            </el-form-item>
            <el-form-item label="内容">
              <el-input
                v-model="emailForm.content"
                type="textarea"
                :rows="8"
                placeholder="请输入邮件内容"
              />
            </el-form-item>
            <el-form-item label="HTML格式">
              <el-switch v-model="emailForm.isHtml" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSendEmail" :loading="sending">
                发送邮件
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="邮件模板" name="templates">
          <el-table :data="templates" style="width: 100%">
            <el-table-column prop="name" label="模板名称" width="200" />
            <el-table-column prop="subject" label="主题" width="200" />
            <el-table-column prop="content" label="内容" show-overflow-tooltip />
            <el-table-column prop="html" label="HTML格式" width="100">
              <template #default="{ row }">
                <el-tag :type="row.html ? 'success' : 'info'">
                  {{ row.html ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180">
              <template #default="{ row }">
                {{ formatDate(row.createdAt) }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { sendEmail, listEmailTemplates, type EmailTemplate } from '@/api/integration'

const activeTab = ref('send')
const sending = ref(false)
const templates = ref<EmailTemplate[]>([])
const emailForm = ref({
  to: '',
  subject: '',
  content: '',
  isHtml: false
})

const handleSendEmail = async () => {
  if (!emailForm.value.to || !emailForm.value.subject || !emailForm.value.content) {
    ElMessage.warning('请填写完整的邮件信息')
    return
  }
  
  sending.value = true
  try {
    const res = await sendEmail(emailForm.value)
    if (res.data.success) {
      ElMessage.success('邮件发送成功')
      emailForm.value = {
        to: '',
        subject: '',
        content: '',
        isHtml: false
      }
    }
  } catch (error) {
    ElMessage.error('邮件发送失败')
  } finally {
    sending.value = false
  }
}

const loadTemplates = async () => {
  try {
    const res = await listEmailTemplates()
    if (res.data.success) {
      templates.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载模板失败')
  }
}

const formatDate = (timestamp: number) => {
  return new Date(timestamp).toLocaleString()
}

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.email-service {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
