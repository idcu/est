<template>
  <div class="ai-reference">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>开发参�?/span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="快速参�? name="reference">
          <el-form label-width="100px">
            <el-form-item label="主题">
              <el-input v-model="referenceTopic" placeholder="请输入主�? />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetReference" :loading="referenceLoading">
                获取参�?              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="referenceResult" class="result-container">
            <div class="result-header">
              <span>参考内�?/span>
              <el-button link @click="copyReferenceResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
              </el-button>
            </div>
            <el-input
              v-model="referenceResult"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="最佳实�? name="bestpractice">
          <el-form label-width="100px">
            <el-form-item label="分类">
              <el-input v-model="practiceCategory" placeholder="请输入分�? />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetBestPractice" :loading="practiceLoading">
                获取最佳实�?              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="practiceResult" class="result-container">
            <div class="result-header">
              <span>最佳实�?/span>
              <el-button link @click="copyPracticeResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
              </el-button>
            </div>
            <el-input
              v-model="practiceResult"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="教程" name="tutorial">
          <el-form label-width="100px">
            <el-form-item label="主题">
              <el-input v-model="tutorialTopic" placeholder="请输入主�? />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetTutorial" :loading="tutorialLoading">
                获取教程
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="tutorialResult" class="result-container">
            <div class="result-header">
              <span>教程内容</span>
              <el-button link @click="copyTutorialResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
              </el-button>
            </div>
            <el-input
              v-model="tutorialResult"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { DocumentCopy } from '@element-plus/icons-vue'
import { getQuickReference, getBestPractice, getTutorial } from '@/api/ai'

const activeTab = ref('reference')
const referenceLoading = ref(false)
const practiceLoading = ref(false)
const tutorialLoading = ref(false)

const referenceTopic = ref('')
const practiceCategory = ref('')
const tutorialTopic = ref('')

const referenceResult = ref('')
const practiceResult = ref('')
const tutorialResult = ref('')

const handleGetReference = async () => {
  if (!referenceTopic.value.trim()) {
    ElMessage.warning('请输入主�?)
    return
  }
  
  referenceLoading.value = true
  try {
    const res = await getQuickReference(referenceTopic.value)
    if (res.data.success) {
      referenceResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取参考失�?)
  } finally {
    referenceLoading.value = false
  }
}

const handleGetBestPractice = async () => {
  if (!practiceCategory.value.trim()) {
    ElMessage.warning('请输入分�?)
    return
  }
  
  practiceLoading.value = true
  try {
    const res = await getBestPractice(practiceCategory.value)
    if (res.data.success) {
      practiceResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取最佳实践失�?)
  } finally {
    practiceLoading.value = false
  }
}

const handleGetTutorial = async () => {
  if (!tutorialTopic.value.trim()) {
    ElMessage.warning('请输入主�?)
    return
  }
  
  tutorialLoading.value = true
  try {
    const res = await getTutorial(tutorialTopic.value)
    if (res.data.success) {
      tutorialResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取教程失败')
  } finally {
    tutorialLoading.value = false
  }
}

const copyReferenceResult = () => {
  navigator.clipboard.writeText(referenceResult.value)
  ElMessage.success('已复制到剪贴�?)
}

const copyPracticeResult = () => {
  navigator.clipboard.writeText(practiceResult.value)
  ElMessage.success('已复制到剪贴�?)
}

const copyTutorialResult = () => {
  navigator.clipboard.writeText(tutorialResult.value)
  ElMessage.success('已复制到剪贴�?)
}
</script>

<style scoped>
.ai-reference {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-container {
  margin-top: 20px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}
</style>
