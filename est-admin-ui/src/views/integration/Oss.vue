<template>
  <div class="oss-service">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Object Storage</span>
        </div>
      </template>
      
      <div class="oss-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="bucket-card">
              <template #header>
                <div class="card-header">
                  <span>Buckets</span>
                  <el-button type="primary" size="small" @click="loadBuckets">
                    Refresh
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
                  <span>File List</span>
                  <div class="actions">
                    <el-upload
                      :show-file-list="false"
                      :before-upload="beforeUpload"
                      :http-request="handleUpload"
                    >
                      <el-button type="primary">
                        <el-icon><Upload /></el-icon>
                        Upload File
                      </el-button>
                    </el-upload>
                    <el-button @click="loadFiles">
                      <el-icon><Refresh /></el-icon>
                      Refresh
                    </el-button>
                  </div>
                </div>
              </template>
              
              <el-table :data="files" style="width: 100%" v-loading="loading">
                <el-table-column prop="fileName" label="File Name" />
                <el-table-column prop="filePath" label="Path" show-overflow-tooltip />
                <el-table-column prop="size" label="Size" width="120">
                  <template #default="{ row }">
                    {{ formatBytes(row.size) }}
                  </template>
                </el-table-column>
                <el-table-column prop="contentType" label="Type" width="150" />
                <el-table-column prop="lastModified" label="Modified At" width="180">
                  <template #default="{ row }">
                    {{ formatDate(row.lastModified) }}
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="150">
                  <template #default="{ row }">
                    <el-button link type="primary" @click="previewFile(row)">
                      Preview
                    </el-button>
                    <el-button link type="danger" @click="handleDelete(row)">
                      Delete
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
    ElMessage.error('Failed to load buckets')
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
    ElMessage.error('Failed to load file list')
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
    ElMessage.warning('Please select bucket and file')
    return
  }
  
  try {
    const res = await uploadFile({
      bucketName: currentBucket.value,
      fileName: uploadFileData.value.name,
      file: uploadFileData.value
    })
    if (res.data.success) {
      ElMessage.success('File uploaded successfully')
      await loadFiles()
    }
  } catch (error) {
    ElMessage.error('Failed to upload file')
  }
}

const previewFile = (file: OssFile) => {
  window.open(file.url, '_blank')
}

const handleDelete = async (file: OssFile) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this file?', 'Confirm', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    const res = await deleteFile({
      bucketName: file.bucketName,
      fileName: file.fileName
    })
    if (res.data.success) {
      ElMessage.success('File deleted successfully')
      await loadFiles()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Failed to delete file')
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
