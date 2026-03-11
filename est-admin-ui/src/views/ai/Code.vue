<template>
  <div class="ai-code">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Code Generation</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Generate Code" name="generate">
          <el-form label-width="100px">
            <el-form-item label="Requirement">
              <el-input
                v-model="requirement"
                type="textarea"
                :rows="6"
                placeholder="Please describe the code you want to generate..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGenerateCode" :loading="loading">
                Generate Code
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="result" class="result-container">
            <div class="result-header">
              <span>Generated Result</span>
              <el-button link @click="copyResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
              </el-button>
            </div>
            <el-input
              v-model="result"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="Code Suggestion" name="suggest">
          <el-form label-width="100px">
            <el-form-item label="Requirement">
              <el-input
                v-model="suggestRequirement"
                type="textarea"
                :rows="6"
                placeholder="Please describe your requirement to get code suggestions..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSuggestCode" :loading="suggestLoading">
                Get Suggestions
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="suggestResult" class="result-container">
            <div class="result-header">
              <span>Suggestion Result</span>
              <el-button link @click="copySuggestResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
              </el-button>
            </div>
            <el-input
              v-model="suggestResult"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="Code Explanation" name="explain">
          <el-form label-width="100px">
            <el-form-item label="Code">
              <el-input
                v-model="codeToExplain"
                type="textarea"
                :rows="8"
                placeholder="Please enter the code to explain..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleExplainCode" :loading="explainLoading">
                Explain Code
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="explainResult" class="result-container">
            <div class="result-header">
              <span>Explanation Result</span>
              <el-button link @click="copyExplainResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
              </el-button>
            </div>
            <el-input
              v-model="explainResult"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="Code Optimization" name="optimize">
          <el-form label-width="100px">
            <el-form-item label="Code">
              <el-input
                v-model="codeToOptimize"
                type="textarea"
                :rows="8"
                placeholder="Please enter the code to optimize..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleOptimizeCode" :loading="optimizeLoading">
                Optimize Code
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="optimizeResult" class="result-container">
            <div class="result-header">
              <span>Optimization Result</span>
              <el-button link @click="copyOptimizeResult">
                <el-icon><DocumentCopy /></el-icon>
                Copy
              </el-button>
            </div>
            <el-input
              v-model="optimizeResult"
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
import { generateCode, suggestCode, explainCode, optimizeCode } from '@/api/ai'

const activeTab = ref('generate')
const loading = ref(false)
const suggestLoading = ref(false)
const explainLoading = ref(false)
const optimizeLoading = ref(false)

const requirement = ref('')
const suggestRequirement = ref('')
const codeToExplain = ref('')
const codeToOptimize = ref('')

const result = ref('')
const suggestResult = ref('')
const explainResult = ref('')
const optimizeResult = ref('')

const handleGenerateCode = async () => {
  if (!requirement.value.trim()) {
    ElMessage.warning('Please enter the requirement')
    return
  }
  
  loading.value = true
  try {
    const res = await generateCode(requirement.value)
    if (res.data.success) {
      result.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to generate code')
  } finally {
    loading.value = false
  }
}

const handleSuggestCode = async () => {
  if (!suggestRequirement.value.trim()) {
    ElMessage.warning('Please enter the requirement')
    return
  }
  
  suggestLoading.value = true
  try {
    const res = await suggestCode(suggestRequirement.value)
    if (res.data.success) {
      suggestResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to get suggestions')
  } finally {
    suggestLoading.value = false
  }
}

const handleExplainCode = async () => {
  if (!codeToExplain.value.trim()) {
    ElMessage.warning('Please enter the code to explain')
    return
  }
  
  explainLoading.value = true
  try {
    const res = await explainCode(codeToExplain.value)
    if (res.data.success) {
      explainResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to explain code')
  } finally {
    explainLoading.value = false
  }
}

const handleOptimizeCode = async () => {
  if (!codeToOptimize.value.trim()) {
    ElMessage.warning('Please enter the code to optimize')
    return
  }
  
  optimizeLoading.value = true
  try {
    const res = await optimizeCode(codeToOptimize.value)
    if (res.data.success) {
      optimizeResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to optimize code')
  } finally {
    optimizeLoading.value = false
  }
}

const copyResult = () => {
  navigator.clipboard.writeText(result.value)
  ElMessage.success('Copied to clipboard')
}

const copySuggestResult = () => {
  navigator.clipboard.writeText(suggestResult.value)
  ElMessage.success('Copied to clipboard')
}

const copyExplainResult = () => {
  navigator.clipboard.writeText(explainResult.value)
  ElMessage.success('Copied to clipboard')
}

const copyOptimizeResult = () => {
  navigator.clipboard.writeText(optimizeResult.value)
  ElMessage.success('Copied to clipboard')
}
</script>

<style scoped>
.ai-code {
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
