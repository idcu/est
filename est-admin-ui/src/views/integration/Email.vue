<template>
  <div class="email-service">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Email Service</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Send Email" name="send">
          <el-form :model="emailForm" label-width="100px" style="max-width: 600px;">
            <el-form-item label="Recipient">
              <el-input v-model="emailForm.to" placeholder="Please enter recipient email" />
            </el-form-item>
            <el-form-item label="Subject">
              <el-input v-model="emailForm.subject" placeholder="Please enter email subject" />
            </el-form-item>
            <el-form-item label="Content">
              <el-input
                v-model="emailForm.content"
                type="textarea"
                :rows="8"
                placeholder="Please enter email content"
              />
            </el-form-item>
            <el-form-item label="HTML Format">
              <el-switch v-model="emailForm.isHtml" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSendEmail" :loading="sending">
                Send Email
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="Email Templates" name="templates">
          <el-table :data="templates" style="width: 100%">
            <el-table-column prop="name" label="Template Name" width="200" />
            <el-table-column prop="subject" label="Subject" width="200" />
            <el-table-column prop="content" label="Content" show-overflow-tooltip />
            <el-table-column prop="html" label="HTML Format" width="100">
              <template #default="{ row }">
                <el-tag :type="row.html ? 'success' : 'info'">
                  {{ row.html ? 'Yes' : 'No' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="Created At" width="180">
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
    ElMessage.warning('Please complete the email information')
    return
  }
  
  sending.value = true
  try {
    const res = await sendEmail(emailForm.value)
    if (res.data.success) {
      ElMessage.success('Email sent successfully')
      emailForm.value = {
        to: '',
        subject: '',
        content: '',
        isHtml: false
      }
    }
  } catch (error) {
    ElMessage.error('Failed to send email')
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
    ElMessage.error('Failed to load templates')
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
