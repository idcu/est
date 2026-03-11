<template>
  <div class="sms-service">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>SMS Service</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Send SMS" name="send">
          <el-form :model="smsForm" label-width="100px" style="max-width: 600px;">
            <el-form-item label="Phone">
              <el-input v-model="smsForm.phone" placeholder="Please enter phone number" />
            </el-form-item>
            <el-form-item label="Content">
              <el-input
                v-model="smsForm.content"
                type="textarea"
                :rows="4"
                placeholder="Please enter SMS content"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSendSms" :loading="sending">
                Send SMS
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="SMS Templates" name="templates">
          <el-table :data="templates" style="width: 100%">
            <el-table-column prop="code" label="Template Code" width="150" />
            <el-table-column prop="name" label="Template Name" width="150" />
            <el-table-column prop="content" label="Content" show-overflow-tooltip />
            <el-table-column prop="provider" label="Provider" width="120" />
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
import { sendSms, listSmsTemplates, type SmsTemplate } from '@/api/integration'

const activeTab = ref('send')
const sending = ref(false)
const templates = ref<SmsTemplate[]>([])
const smsForm = ref({
  phone: '',
  content: ''
})

const handleSendSms = async () => {
  if (!smsForm.value.phone || !smsForm.value.content) {
    ElMessage.warning('Please complete SMS information')
    return
  }
  
  sending.value = true
  try {
    const res = await sendSms(smsForm.value)
    if (res.data.success) {
      ElMessage.success('SMS sent successfully')
      smsForm.value = {
        phone: '',
        content: ''
      }
    }
  } catch (error) {
    ElMessage.error('Failed to send SMS')
  } finally {
    sending.value = false
  }
}

const loadTemplates = async () => {
  try {
    const res = await listSmsTemplates()
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
.sms-service {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
