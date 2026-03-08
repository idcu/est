<template>
  <div class="ai-chat">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>AI 对话</span>
          <el-button link @click="clearChat">
            <el-icon><Delete /></el-icon>
            清空对话
          </el-button>
        </div>
      </template>
      
      <div class="chat-container" ref="chatContainer">
        <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
          <div class="message-avatar">
            <el-icon v-if="msg.role === 'user'"><User /></el-icon>
            <el-icon v-else><Cpu /></el-icon>
          </div>
          <div class="message-content">
            <div class="message-role">{{ msg.role === 'user' ? '�? : 'AI 助手' }}</div>
            <div class="message-text">{{ msg.content }}</div>
          </div>
        </div>
      </div>
      
      <div class="chat-input">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入您的问题..."
          @keydown.enter.ctrl="sendMessage"
        />
        <div class="input-actions">
          <el-button type="primary" @click="sendMessage" :loading="loading">
            发�?(Ctrl+Enter)
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Cpu, Delete } from '@element-plus/icons-vue'
import { chat } from '@/api/ai'

interface Message {
  role: 'user' | 'assistant'
  content: string
}

const messages = ref<Message[]>([])
const inputMessage = ref('')
const loading = ref(false)
const chatContainer = ref<HTMLElement | null>(null)

const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入消�?)
    return
  }
  
  const userMessage: Message = {
    role: 'user',
    content: inputMessage.value
  }
  
  messages.value.push(userMessage)
  const userInput = inputMessage.value
  inputMessage.value = ''
  scrollToBottom()
  
  loading.value = true
  try {
    const res = await chat(userInput)
    if (res.data.success) {
      const assistantMessage: Message = {
        role: 'assistant',
        content: res.data.data
      }
      messages.value.push(assistantMessage)
      scrollToBottom()
    }
  } catch (error) {
    ElMessage.error('发送消息失�?)
  } finally {
    loading.value = false
  }
}

const clearChat = () => {
  messages.value = []
}

onMounted(() => {
  messages.value = [
    {
      role: 'assistant',
      content: '您好！我�?EST AI 助手，有什么可以帮助您的吗�?
    }
  ]
})
</script>

<style scoped>
.ai-chat {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-container {
  height: 500px;
  overflow-y: auto;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 20px;
}

.message {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.message.user .message-avatar {
  background: #67c23a;
}

.message-content {
  flex: 1;
  max-width: 70%;
}

.message.user .message-content {
  text-align: right;
}

.message-role {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.message-text {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 8px;
  line-height: 1.6;
  white-space: pre-wrap;
}

.message.user .message-text {
  background: #ecf5ff;
}

.chat-input {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
}
</style>
