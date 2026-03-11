<template>
  <div class="menu-page">
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>Menu List</span>
          <el-button type="primary" @click="handleAdd">Add Menu</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe row-key="id" default-expand-all style="width: 100%">
        <el-table-column prop="name" label="Menu Name" width="200" />
        <el-table-column prop="icon" label="Icon" width="100">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="Sort" width="100" />
        <el-table-column prop="path" label="Route Path" width="200" />
        <el-table-column prop="component" label="Component Path" width="200" />
        <el-table-column prop="permission" label="Permission" width="200" />
        <el-table-column prop="type" label="Type" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 0 ? 'primary' : row.type === 1 ? 'success' : 'info'">
              {{ row.type === 0 ? 'Directory' : row.type === 1 ? 'Menu' : 'Button' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Show' : 'Hide' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleAdd(row)">Add</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="Parent Menu" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="menuTreeData"
            :props="treeProps"
            check-strictly
            placeholder="Select Parent Menu"
            clearable
          />
        </el-form-item>
        <el-form-item label="Menu Type" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio :label="0">Directory</el-radio>
            <el-radio :label="1">Menu</el-radio>
            <el-radio :label="2">Button</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Menu Name" prop="name">
          <el-input v-model="formData.name" placeholder="Please enter menu name" />
        </el-form-item>
        <el-form-item label="Icon" prop="icon" v-if="formData.type !== 2">
          <el-input v-model="formData.icon" placeholder="Please enter icon name" />
        </el-form-item>
        <el-form-item label="Sort" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="Route Path" prop="path" v-if="formData.type !== 2">
          <el-input v-model="formData.path" placeholder="Please enter route path" />
        </el-form-item>
        <el-form-item label="Component Path" prop="component" v-if="formData.type === 1">
          <el-input v-model="formData.component" placeholder="Please enter component path" />
        </el-form-item>
        <el-form-item label="Permission" prop="permission">
          <el-input v-model="formData.permission" placeholder="Please enter permission" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">Show</el-radio>
            <el-radio :label="0">Hide</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Remark" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="Please enter remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { listMenus, getMenu, createMenu, updateMenu, deleteMenu } from '@/api/menu'

const tableData = ref<any[]>([])
const menuTreeData = ref<any[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const treeProps = {
  children: 'children',
  label: 'name',
  value: 'id'
}

const formData = reactive({
  id: undefined as number | undefined,
  parentId: 0,
  name: '',
  icon: '',
  sort: 0,
  path: '',
  component: '',
  permission: '',
  type: 1,
  status: 1,
  remark: ''
})

const formRules: FormRules = {
  name: [
    { required: true, message: 'Please enter menu name', trigger: 'blur' }
  ]
}

const loadData = async () => {
  try {
    const res = await listMenus()
    tableData.value = res.data || []
    menuTreeData.value = [
      { id: 0, name: 'Root', children: res.data || [] }
    ]
  } catch (error) {
    console.error('Failed to load menu list', error)
  }
}

const handleAdd = (row?: any) => {
  dialogTitle.value = 'Add Menu'
  isEdit.value = false
  Object.assign(formData, {
    id: undefined,
    parentId: row ? row.id : 0,
    name: '',
    icon: '',
    sort: 0,
    path: '',
    component: '',
    permission: '',
    type: row ? (row.type === 0 ? 1 : 2) : 1,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = async (row: any) => {
  dialogTitle.value = 'Edit Menu'
  isEdit.value = true
  try {
    const res = await getMenu(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('Failed to get menu info', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateMenu(formData)
          ElMessage.success('Updated successfully')
        } else {
          await createMenu(formData)
          ElMessage.success('Created successfully')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('Failed to submit', error)
      }
    }
  })
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('Are you sure you want to delete this menu?', 'Warning', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('Deleted successfully')
      loadData()
    } catch (error) {
      console.error('Failed to delete', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.menu-page {
  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
</style>
