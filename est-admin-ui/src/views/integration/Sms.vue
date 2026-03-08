<template>
  <div class="sms-service">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>短信服务</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="发送短�? name="send">
          <el-form :model="smsForm" label-width="100px" style="max-width: 600px;">
            <el-form-item label="手机�?>
              <el-input v-model="smsForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="内容">
              <el-input
                v-model="smsForm.content"
                type="textarea"
                :rows="4"
                placeholder="请输入短信内�?
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSendSms" :loading="sending">
                发送短�?              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="短信模板" name="templates">
          <el-table :data="templates" style="width: 100%">
            <el-table-column prop="code" label="模板编码" width="150" />
            <el-table-column prop="name" label="模板名称" width="150" />
            <el-table-column prop="content" label="内容" show-overflow-tooltip />
            <el-table-column prop="provider" label="服务�? width="120" />
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
    ElMessage.warning('请填写完整的短信信息')
    return
  }
  
  sending.value = true
  try {
    const res = await sendSms(smsForm.value)
    if (res.data.success) {
      ElMessage.success('短信发送成�?)
      smsForm.value = {
        phone: '',
        content: ''
      }
    }
  } catch (error) {
    ElMessage.error('短信发送失�?)
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
.sms-service {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
