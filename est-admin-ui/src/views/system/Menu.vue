<template>
  <div class="menu-page">
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>菜单列表</span>
          <el-button type="primary" @click="handleAdd">新增菜单</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe row-key="id" default-expand-all style="width: 100%">
        <el-table-column prop="name" label="菜单名称" width="200" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column prop="path" label="路由路径" width="200" />
        <el-table-column prop="component" label="组件路径" width="200" />
        <el-table-column prop="permission" label="权限标识" width="200" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 0 ? 'primary' : row.type === 1 ? 'success' : 'info'">
              {{ row.type === 0 ? '目录' : row.type === 1 ? '菜单' : '按钮' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状�? width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleAdd(row)">新增</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
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
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="menuTreeData"
            :props="treeProps"
            check-strictly
            placeholder="选择上级菜单"
            clearable
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio :label="0">目录</el-radio>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入菜单名�? />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="formData.type !== 2">
          <el-input v-model="formData.icon" placeholder="请输入图标名�? />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path" v-if="formData.type !== 2">
          <el-input v-model="formData.path" placeholder="请输入路由路�? />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="formData.type === 1">
          <el-input v-model="formData.component" placeholder="请输入组件路�? />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="formData.permission" placeholder="请输入权限标�? />
        </el-form-item>
        <el-form-item label="显示状�? prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备�? />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
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
    { required: true, message: '请输入菜单名�?, trigger: 'blur' }
  ]
}

const loadData = async () => {
  try {
    const res = await listMenus()
    tableData.value = res.data || []
    menuTreeData.value = [
      { id: 0, name: '根目�?, children: res.data || [] }
    ]
  } catch (error) {
    console.error('加载菜单列表失败', error)
  }
}

const handleAdd = (row?: any) => {
  dialogTitle.value = '新增菜单'
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
  dialogTitle.value = '编辑菜单'
  isEdit.value = true
  try {
    const res = await getMenu(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('获取菜单信息失败', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateMenu(formData)
          ElMessage.success('更新成功')
        } else {
          await createMenu(formData)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('提交失败', error)
      }
    }
  })
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败', error)
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
