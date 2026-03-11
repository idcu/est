<template>
  <div class="ai-reference">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Development Reference</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Quick Reference" name="reference">
          <el-form label-width="100px">
            <el-form-item label="Topic">
              <el-input v-model="referenceTopic" placeholder="Please enter a topic" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetReference" :loading="referenceLoading">
                Get Reference
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="referenceResult" class="result-container">
            <div class="result-header">
              <span>Reference Content</span>
              <el-button link @click="copyReferenceResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
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
        
        <el-tab-pane label="Best Practice" name="bestpractice">
          <el-form label-width="100px">
            <el-form-item label="Category">
              <el-input v-model="practiceCategory" placeholder="Please enter a category" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetBestPractice" :loading="practiceLoading">
                Get Best Practice
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="practiceResult" class="result-container">
            <div class="result-header">
              <span>Best Practice</span>
              <el-button link @click="copyPracticeResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
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
        
        <el-tab-pane label="Tutorial" name="tutorial">
          <el-form label-width="100px">
            <el-form-item label="Topic">
              <el-input v-model="tutorialTopic" placeholder="Please enter a topic" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetTutorial" :loading="tutorialLoading">
                Get Tutorial
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="tutorialResult" class="result-container">
            <div class="result-header">
              <span>Tutorial Content</span>
              <el-button link @click="copyTutorialResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
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
    ElMessage.warning('Please enter a topic')
    return
  }
  
  referenceLoading.value = true
  try {
    const res = await getQuickReference(referenceTopic.value)
    if (res.data.success) {
      referenceResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to get reference')
  } finally {
    referenceLoading.value = false
  }
}

const handleGetBestPractice = async () => {
  if (!practiceCategory.value.trim()) {
    ElMessage.warning('Please enter a category')
    return
  }
  
  practiceLoading.value = true
  try {
    const res = await getBestPractice(practiceCategory.value)
    if (res.data.success) {
      practiceResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to get best practice')
  } finally {
    practiceLoading.value = false
  }
}

const handleGetTutorial = async () => {
  if (!tutorialTopic.value.trim()) {
    ElMessage.warning('Please enter a topic')
    return
  }
  
  tutorialLoading.value = true
  try {
    const res = await getTutorial(tutorialTopic.value)
    if (res.data.success) {
      tutorialResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to get tutorial')
  } finally {
    tutorialLoading.value = false
  }
}

const copyReferenceResult = () => {
  navigator.clipboard.writeText(referenceResult.value)
  ElMessage.success('Copied to clipboard')
}

const copyPracticeResult = () => {
  navigator.clipboard.writeText(practiceResult.value)
  ElMessage.success('Copied to clipboard')
}

const copyTutorialResult = () => {
  navigator.clipboard.writeText(tutorialResult.value)
  ElMessage.success('Copied to clipboard')
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
