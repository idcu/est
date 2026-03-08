<template>
  <div class="oss-service">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>对象存储</span>
        </div>
      </template>
      
      <div class="oss-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="bucket-card">
              <template #header>
                <div class="card-header">
                  <span>存储桶</span>
                  <el-button type="primary" size="small" @click="loadBuckets">
                    刷新
                  </el-button>
                </div>
              </template>
              <el-menu
                :default-active="currentBucket"
                @select="selectBucket"
              >
                <el-menu-item
                  v-for="bucket in buckets"
                  :key="bucket"
                  :index="bucket"
                >
                  <el-icon><Folder /></el-icon>
                  <span>{{ bucket }}</span>
                </el-menu-item>
              </el-menu>
            </el-card>
          </el-col>
          
          <el-col :span="18">
            <el-card class="file-card">
              <template #header>
                <div class="card-header">
                  <span>文件列表</span>
                  <div class="actions">
                    <el-upload
                      :show-file-list="false"
                      :before-upload="beforeUpload"
                      :http-request="handleUpload"
                    >
                      <el-button type="primary">
                        <el-icon><Upload /></el-icon>
                        上传文件
                      </el-button>
                    </el-upload>
                    <el-button @click="loadFiles">
                      <el-icon><Refresh /></el-icon>
                      刷新
                    </el-button>
                  </div>
                </div>
              </template>
              
              <el-table :data="files" style="width: 100%" v-loading="loading">
                <el-table-column prop="fileName" label="文件名" />
                <el-table-column prop="filePath" label="路径" show-overflow-tooltip />
                <el-table-column prop="size" label="大小" width="120">
                  <template #default="{ row }">
                    {{ formatBytes(row.size) }}
                  </template>
                </el-table-column>
                <el-table-column prop="contentType" label="类型" width="150" />
                <el-table-column prop="lastModified" label="修改时间" width="180">
                  <template #default="{ row }">
                    {{ formatDate(row.lastModified) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="{ row }">
                    <el-button link type="primary" @click="previewFile(row)">
                      预览
                    </el-button>
                    <el-button link type="danger" @click="handleDelete(row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Folder, Upload, Refresh } from '@element-plus/icons-vue'
import { listBuckets, listFiles, uploadFile, deleteFile, type OssFile } from '@/api/integration'

const buckets = ref<string[]>([])
const currentBucket = ref('')
const files = ref<OssFile[]>([])
const loading = ref(false)
const uploadFileData = ref<File | null>(null)

const loadBuckets = async () => {
  try {
    const res = await listBuckets()
    if (res.data.success) {
      buckets.value = res.data.data
      if (buckets.value.length > 0 && !currentBucket.value) {
        selectBucket(buckets.value[0])
      }
    }
  } catch (error) {
    ElMessage.error('加载存储桶失败')
  }
}

const selectBucket = async (bucketName: string) => {
  currentBucket.value = bucketName
  await loadFiles()
}

const loadFiles = async () => {
  if (!currentBucket.value) {
    return
  }
  
  loading.value = true
  try {
    const res = await listFiles({ bucketName: currentBucket.value })
    if (res.data.success) {
      files.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载文件列表失败')
  } finally {
    loading.value = false
  }
}

const beforeUpload = (file: File) => {
  uploadFileData.value = file
  return false
}

const handleUpload = async () => {
  if (!currentBucket.value || !uploadFileData.value) {
    ElMessage.warning('请选择存储桶和文件')
    return
  }
  
  try {
    const res = await uploadFile({
      bucketName: currentBucket.value,
      fileName: uploadFileData.value.name,
      file: uploadFileData.value
    })
    if (res.data.success) {
      ElMessage.success('文件上传成功')
      await loadFiles()
    }
  } catch (error) {
    ElMessage.error('文件上传失败')
  }
}

const previewFile = (file: OssFile) => {
  window.open(file.url, '_blank')
}

const handleDelete = async (file: OssFile) => {
  try {
    await ElMessageBox.confirm('确定要删除这个文件吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteFile({
      bucketName: file.bucketName,
      fileName: file.fileName
    })
    if (res.data.success) {
      ElMessage.success('文件删除成功')
      await loadFiles()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('文件删除失败')
    }
  }
}

const formatBytes = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

const formatDate = (timestamp: number) => {
  return new Date(timestamp).toLocaleString()
}

onMounted(() => {
  loadBuckets()
})
</script>

<style scoped>
.oss-service {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.oss-container {
  margin-top: 20px;
}

.bucket-card,
.file-card {
  height: calc(100vh - 200px);
}

.file-card :deep(.el-card__body) {
  height: calc(100% - 57px);
  overflow: auto;
}

.bucket-card :deep(.el-card__body) {
  height: calc(100% - 57px);
  overflow: auto;
  padding: 0;
}

.actions {
  display: flex;
  gap: 10px;
}
</style>
