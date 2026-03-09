<template>
  <div class="data-permission-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="规则名称">
          <el-input v-model="searchForm.name" placeholder="请输入规则名称" clearable />
        </el-form-item>
        <el-form-item label="规则类型">
          <el-select v-model="searchForm.ruleType" placeholder="请选择规则类型" clearable>
            <el-option label="行级权限" value="ROW_LEVEL" />
            <el-option label="字段级权限" value="FIELD_LEVEL" />
            <el-option label="组合权限" value="COMBINED" />
          </el-select>
        </el-form-item>
        <el-form-item label="资源类型">
          <el-input v-model="searchForm.resourceType" placeholder="请输入资源类型" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>数据权限规则列表</span>
          <el-button type="primary" @click="handleAdd">新增规则</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="规则名称" width="180" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="ruleType" label="规则类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getRuleTypeTag(row.ruleType)">
              {{ getRuleTypeText(row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resourceType" label="资源类型" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="info" size="small" @click="handleAssignRoles(row)">分配角色</el-button>
            <el-button link type="warning" size="small" @click="handleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="规则名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-radio-group v-model="formData.ruleType">
            <el-radio label="ROW_LEVEL">行级权限</el-radio>
            <el-radio label="FIELD_LEVEL">字段级权限</el-radio>
            <el-radio label="COMBINED">组合权限</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-input v-model="formData.resourceType" placeholder="请输入资源类型，如：user、order" />
        </el-form-item>
        
        <el-divider content-position="left" v-if="['ROW_LEVEL', 'COMBINED'].includes(formData.ruleType)">
          行级权限条件
        </el-divider>
        <div v-if="['ROW_LEVEL', 'COMBINED'].includes(formData.ruleType)" class="conditions-section">
          <el-button type="primary" size="small" @click="addRowCondition">添加条件</el-button>
          <div v-for="(condition, index) in formData.rowConditions" :key="index" class="condition-item">
            <el-input v-model="condition.fieldName" placeholder="字段名" style="width: 150px; margin-right: 10px;" />
            <el-select v-model="condition.operator" placeholder="操作符" style="width: 120px; margin-right: 10px;">
              <el-option label="=" value="EQ" />
              <el-option label="!=" value="NE" />
              <el-option label=">" value="GT" />
              <el-option label=">=" value="GE" />
              <el-option label="<" value="LT" />
              <el-option label="<=" value="LE" />
              <el-option label="IN" value="IN" />
              <el-option label="LIKE" value="LIKE" />
            </el-select>
            <el-input v-model="condition.value" placeholder="值" style="width: 200px; margin-right: 10px;" />
            <el-button link type="danger" size="small" @click="removeRowCondition(index)">删除</el-button>
          </div>
        </div>

        <el-divider content-position="left" v-if="['FIELD_LEVEL', 'COMBINED'].includes(formData.ruleType)">
          字段级权限掩码
        </el-divider>
        <div v-if="['FIELD_LEVEL', 'COMBINED'].includes(formData.ruleType)" class="masks-section">
          <el-button type="primary" size="small" @click="addFieldMask">添加字段掩码</el-button>
          <div v-for="(mask, index) in formData.fieldMasks" :key="index" class="mask-item">
            <el-input v-model="mask.fieldName" placeholder="字段名" style="width: 200px; margin-right: 10px;" />
            <el-select v-model="mask.maskType" placeholder="掩码类型" style="width: 150px; margin-right: 10px;">
              <el-option label="隐藏" value="HIDDEN" />
              <el-option label="只读" value="READ_ONLY" />
              <el-option label="脱敏" value="MASKED" />
            </el-select>
            <el-button link type="danger" size="small" @click="removeFieldMask(index)">删除</el-button>
          </div>
        </div>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="assignRolesDialogVisible"
      title="分配角色"
      width="600px"
    >
      <el-transfer
        v-model="selectedRoleIds"
        :data="roleList"
        :titles="['待选角色', '已选角色']"
        :button-texts="['移除', '添加']"
      />
      <template #footer>
        <el-button @click="assignRolesDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignRolesSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules, type TransferDataItem } from 'element-plus'
import {
  listDataPermissionRules,
  getDataPermissionRule,
  createDataPermissionRule,
  updateDataPermissionRule,
  deleteDataPermissionRule,
  assignRolePermissions,
  type DataPermissionRule,
  type RowCondition,
  type FieldMask
} from '@/api/dataPermission'
import { listRoles, type Role } from '@/api/role'

const searchForm = reactive({
  name: '',
  ruleType: undefined as string | undefined,
  resourceType: '',
  status: undefined as number | undefined
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref<DataPermissionRule[]>([])
const roleList = ref<TransferDataItem[]>([])
const selectedRoleIds = ref<string[]>([])
const dialogVisible = ref(false)
const assignRolesDialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const currentRuleId = ref('')

const formData = reactive<Partial<DataPermissionRule>>({
  name: '',
  description: '',
  ruleType: 'ROW_LEVEL',
  resourceType: '',
  rowConditions: [],
  fieldMasks: [],
  roleIds: [],
  status: 1
})

const formRules: FormRules = {
  name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
  resourceType: [{ required: true, message: '请输入资源类型', trigger: 'blur' }]
}

const getRuleTypeTag = (type: string) => {
  const tagMap: Record<string, any> = {
    ROW_LEVEL: 'primary',
    FIELD_LEVEL: 'success',
    COMBINED: 'warning'
  }
  return tagMap[type] || 'info'
}

const getRuleTypeText = (type: string) => {
  const textMap: Record<string, string> = {
    ROW_LEVEL: '行级权限',
    FIELD_LEVEL: '字段级权限',
    COMBINED: '组合权限'
  }
  return textMap[type] || type
}

const loadRoles = async () => {
  try {
    const res = await listRoles({ page: 1, size: 1000 })
    roleList.value = (res.data.list || []).map((role: Role) => ({
      key: role.id,
      label: role.name
    }))
  } catch (error) {
    console.error('加载角色失败', error)
  }
}

const loadData = async () => {
  try {
    const res = await listDataPermissionRules({
      ...searchForm,
      page: pagination.page,
      size: pagination.size
    })
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.ruleType = undefined
  searchForm.resourceType = ''
  searchForm.status = undefined
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增数据权限规则'
  Object.assign(formData, {
    name: '',
    description: '',
    ruleType: 'ROW_LEVEL',
    resourceType: '',
    rowConditions: [],
    fieldMasks: [],
    roleIds: [],
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = async (row: DataPermissionRule) => {
  try {
    const res = await getDataPermissionRule(row.id)
    isEdit.value = true
    dialogTitle.value = '编辑数据权限规则'
    Object.assign(formData, { ...res.data })
    dialogVisible.value = true
  } catch (error) {
    console.error('获取详情失败', error)
  }
}

const handleAssignRoles = (row: DataPermissionRule) => {
  currentRuleId.value = row.id
  selectedRoleIds.value = [...(row.roleIds || [])]
  assignRolesDialogVisible.value = true
}

const handleAssignRolesSubmit = async () => {
  try {
    await assignRolePermissions(currentRuleId.value, selectedRoleIds.value)
    ElMessage.success('分配成功')
    assignRolesDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('分配失败', error)
  }
}

const handleStatus = async (row: DataPermissionRule) => {
  try {
    await ElMessageBox.confirm(
      `确定要${row.status === 1 ? '禁用' : '启用'}该规则吗？`,
      '提示',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
    await updateDataPermissionRule({
      id: row.id,
      status: row.status === 1 ? 0 : 1
    })
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败', error)
    }
  }
}

const handleDelete = async (row: DataPermissionRule) => {
  try {
    await ElMessageBox.confirm('确定要删除该规则吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteDataPermissionRule(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value) {
      await updateDataPermissionRule(formData)
      ElMessage.success('更新成功')
    } else {
      await createDataPermissionRule(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败', error)
  }
}

const addRowCondition = () => {
  formData.rowConditions?.push({
    fieldName: '',
    operator: 'EQ',
    value: ''
  })
}

const removeRowCondition = (index: number) => {
  formData.rowConditions?.splice(index, 1)
}

const addFieldMask = () => {
  formData.fieldMasks?.push({
    fieldName: '',
    maskType: 'HIDDEN'
  })
}

const removeFieldMask = (index: number) => {
  formData.fieldMasks?.splice(index, 1)
}

onMounted(() => {
  loadRoles()
  loadData()
})
</script>

<style scoped lang="scss">
.data-permission-page {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .conditions-section,
  .masks-section {
    margin-bottom: 20px;

    .condition-item,
    .mask-item {
      display: flex;
      align-items: center;
      margin-top: 10px;
    }
  }
}
</style>
