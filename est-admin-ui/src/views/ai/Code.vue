<template>
  <div class="ai-code">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>代码生成</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="生成代码" name="generate">
          <el-form label-width="100px">
            <el-form-item label="需求描述">
              <el-input
                v-model="requirement"
                type="textarea"
                :rows="6"
                placeholder="请描述您想要生成的代码需求..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGenerateCode" :loading="loading">
                生成代码
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="result" class="result-container">
            <div class="result-header">
              <span>生成结果</span>
              <el-button link @click="copyResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
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
        
        <el-tab-pane label="代码建议" name="suggest">
          <el-form label-width="100px">
            <el-form-item label="需求描述">
              <el-input
                v-model="suggestRequirement"
                type="textarea"
                :rows="6"
                placeholder="请描述您的需求，获取代码建议..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSuggestCode" :loading="suggestLoading">
                获取建议
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="suggestResult" class="result-container">
            <div class="result-header">
              <span>建议结果</span>
              <el-button link @click="copySuggestResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
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
        
        <el-tab-pane label="代码解释" name="explain">
          <el-form label-width="100px">
            <el-form-item label="代码">
              <el-input
                v-model="codeToExplain"
                type="textarea"
                :rows="8"
                placeholder="请输入需要解释的代码..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleExplainCode" :loading="explainLoading">
                解释代码
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="explainResult" class="result-container">
            <div class="result-header">
              <span>解释结果</span>
              <el-button link @click="copyExplainResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
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
        
        <el-tab-pane label="代码优化" name="optimize">
          <el-form label-width="100px">
            <el-form-item label="代码">
              <el-input
                v-model="codeToOptimize"
                type="textarea"
                :rows="8"
                placeholder="请输入需要优化的代码..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleOptimizeCode" :loading="optimizeLoading">
                优化代码
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="optimizeResult" class="result-container">
            <div class="result-header">
              <span>优化结果</span>
              <el-button link @click="copyOptimizeResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
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
    ElMessage.warning('请输入需求描述')
    return
  }
  
  loading.value = true
  try {
    const res = await generateCode(requirement.value)
    if (res.data.success) {
      result.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('生成代码失败')
  } finally {
    loading.value = false
  }
}

const handleSuggestCode = async () => {
  if (!suggestRequirement.value.trim()) {
    ElMessage.warning('请输入需求描述')
    return
  }
  
  suggestLoading.value = true
  try {
    const res = await suggestCode(suggestRequirement.value)
    if (res.data.success) {
      suggestResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取建议失败')
  } finally {
    suggestLoading.value = false
  }
}

const handleExplainCode = async () => {
  if (!codeToExplain.value.trim()) {
    ElMessage.warning('请输入需要解释的代码')
    return
  }
  
  explainLoading.value = true
  try {
    const res = await explainCode(codeToExplain.value)
    if (res.data.success) {
      explainResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('解释代码失败')
  } finally {
    explainLoading.value = false
  }
}

const handleOptimizeCode = async () => {
  if (!codeToOptimize.value.trim()) {
    ElMessage.warning('请输入需要优化的代码')
    return
  }
  
  optimizeLoading.value = true
  try {
    const res = await optimizeCode(codeToOptimize.value)
    if (res.data.success) {
      optimizeResult.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('优化代码失败')
  } finally {
    optimizeLoading.value = false
  }
}

const copyResult = () => {
  navigator.clipboard.writeText(result.value)
  ElMessage.success('已复制到剪贴板')
}

const copySuggestResult = () => {
  navigator.clipboard.writeText(suggestResult.value)
  ElMessage.success('已复制到剪贴板')
}

const copyExplainResult = () => {
  navigator.clipboard.writeText(explainResult.value)
  ElMessage.success('已复制到剪贴板')
}

const copyOptimizeResult = () => {
  navigator.clipboard.writeText(optimizeResult.value)
  ElMessage.success('已复制到剪贴板')
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
