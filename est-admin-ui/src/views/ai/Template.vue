<template>
  <div class="ai-template">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>提示模板</span>
          <el-button type="primary" @click="loadTemplates">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="template-list-card">
            <template #header>
              <span>模板列表</span>
            </template>
            <el-menu
              :default-active="selectedTemplate"
              @select="selectTemplate"
            >
              <el-menu-item
                v-for="template in templates"
                :key="template.name"
                :index="template.name"
              >
                {{ template.name }}
              </el-menu-item>
            </el-menu>
          </el-card>
        </el-col>
        
        <el-col :span="16">
          <el-card class="template-detail-card">
            <template #header>
              <span>模板详情</span>
            </template>
            
            <div v-if="currentTemplate" class="template-detail">
              <el-descriptions :column="1" border>
                <el-descriptions-item label="名称">{{ currentTemplate.name }}</el-descriptions-item>
                <el-descriptions-item label="分类">{{ currentTemplate.category }}</el-descriptions-item>
                <el-descriptions-item label="描述">{{ currentTemplate.description }}</el-descriptions-item>
              </el-descriptions>
              
              <el-divider />
              
              <div class="template-content">
                <h4>模板内容</h4>
                <el-input
                  v-model="currentTemplate.template"
                  type="textarea"
                  :rows="8"
                  readonly
                />
              </div>
              
              <el-divider />
              
              <div class="template-variables">
                <h4>变量</h4>
                <el-tag
                  v-for="(desc, name) in currentTemplate.requiredVariables"
                  :key="name"
                  style="margin-right: 10px; margin-bottom: 10px;"
                >
                  {{ name }}: {{ desc }}
                </el-tag>
              </div>
              
              <el-divider />
              
              <div class="generate-prompt">
                <h4>生成提示</h4>
                <el-form label-width="100px">
                  <el-form-item
                    v-for="(desc, name) in currentTemplate.requiredVariables"
                    :key="name"
                    :label="name"
                  >
                    <el-input v-model="variables[name]" :placeholder="'请输入 ' + name" />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="handleGeneratePrompt" :loading="generateLoading">
                      生成提示
                    </el-button>
                  </el-form-item>
                </el-form>
                
                <div v-if="generatedPrompt" class="generated-prompt">
                  <div class="result-header">
                    <span>生成结果</span>
                    <el-button link @click="copyGeneratedPrompt">
                      <el-icon><DocumentCopy /></el-icon>
                      复制
                    </el-button>
                  </div>
                  <el-input
                    v-model="generatedPrompt"
                    type="textarea"
                    :rows="10"
                    readonly
                  />
                </div>
              </div>
            </div>
            
            <div v-else class="no-template">
              <el-empty description="请选择一个模板" />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, DocumentCopy } from '@element-plus/icons-vue'
import { listTemplates, generatePrompt, type PromptTemplate } from '@/api/ai'

const templates = ref<PromptTemplate[]>([])
const selectedTemplate = ref('')
const currentTemplate = ref<PromptTemplate | null>(null)
const variables = reactive<Record<string, string>>({})
const generatedPrompt = ref('')
const generateLoading = ref(false)

const loadTemplates = async () => {
  try {
    const res = await listTemplates()
    if (res.data.success) {
      templates.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载模板失败')
  }
}

const selectTemplate = (name: string) => {
  selectedTemplate.value = name
  currentTemplate.value = templates.value.find(t => t.name === name) || null
  if (currentTemplate.value) {
    Object.keys(variables).forEach(key => delete variables[key])
    Object.keys(currentTemplate.value.requiredVariables).forEach(key => {
      variables[key] = ''
    })
    generatedPrompt.value = ''
  }
}

const handleGeneratePrompt = async () => {
  if (!currentTemplate.value) {
    ElMessage.warning('请选择一个模板')
    return
  }
  
  const variableNames = Object.keys(variables).join(',')
  const variableValues = Object.values(variables).join(',')
  
  generateLoading.value = true
  try {
    const res = await generatePrompt({
      templateName: currentTemplate.value.name,
      variableNames,
      variableValues
    })
    if (res.data.success) {
      generatedPrompt.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('生成提示失败')
  } finally {
    generateLoading.value = false
  }
}

const copyGeneratedPrompt = () => {
  navigator.clipboard.writeText(generatedPrompt.value)
  ElMessage.success('已复制到剪贴板')
}

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.ai-template {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-list-card,
.template-detail-card {
  height: calc(100vh - 200px);
}

.template-list-card :deep(.el-card__body),
.template-detail-card :deep(.el-card__body) {
  height: calc(100% - 57px);
  overflow: auto;
}

.template-detail h4 {
  margin: 20px 0 10px 0;
  color: #303133;
}

.no-template {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}
</style>
