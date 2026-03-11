<template>
  <div class="data-permission-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="Rule Name">
          <el-input v-model="searchForm.name" placeholder="Please enter rule name" clearable />
        </el-form-item>
        <el-form-item label="Rule Type">
          <el-select v-model="searchForm.ruleType" placeholder="Please select rule type" clearable>
            <el-option label="Row Level" value="ROW_LEVEL" />
            <el-option label="Field Level" value="FIELD_LEVEL" />
            <el-option label="Combined" value="COMBINED" />
          </el-select>
        </el-form-item>
        <el-form-item label="Resource Type">
          <el-input v-model="searchForm.resourceType" placeholder="Please enter resource type" clearable />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="Please select status" clearable>
            <el-option label="Enabled" :value="1" />
            <el-option label="Disabled" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>Data Permission Rule List</span>
          <el-button type="primary" @click="handleAdd">Add Rule</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Rule Name" width="180" />
        <el-table-column prop="description" label="Description" show-overflow-tooltip />
        <el-table-column prop="ruleType" label="Rule Type" width="120">
          <template #default="{ row }">
            <el-tag :type="getRuleTypeTag(row.ruleType)">
              {{ getRuleTypeText(row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resourceType" label="Resource Type" width="150" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Create Time" width="180" />
        <el-table-column label="Actions" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="info" size="small" @click="handleAssignRoles(row)">Assign Roles</el-button>
            <el-button link type="warning" size="small" @click="handleStatus(row)">
              {{ row.status === 1 ? 'Disable' : 'Enable' }}
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">Delete</el-button>
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
        <el-form-item label="Rule Name" prop="name">
          <el-input v-model="formData.name" placeholder="Please enter rule name" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="Please enter description" />
        </el-form-item>
        <el-form-item label="Rule Type" prop="ruleType">
          <el-radio-group v-model="formData.ruleType">
            <el-radio label="ROW_LEVEL">Row Level</el-radio>
            <el-radio label="FIELD_LEVEL">Field Level</el-radio>
            <el-radio label="COMBINED">Combined</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Resource Type" prop="resourceType">
          <el-input v-model="formData.resourceType" placeholder="Please enter resource type, e.g. user, order" />
        </el-form-item>
        
        <el-divider content-position="left" v-if="['ROW_LEVEL', 'COMBINED'].includes(formData.ruleType)">
          Row Level Conditions
        </el-divider>
        <div v-if="['ROW_LEVEL', 'COMBINED'].includes(formData.ruleType)" class="conditions-section">
          <el-button type="primary" size="small" @click="addRowCondition">Add Condition</el-button>
          <div v-for="(condition, index) in formData.rowConditions" :key="index" class="condition-item">
            <el-input v-model="condition.fieldName" placeholder="Field Name" style="width: 150px; margin-right: 10px;" />
            <el-select v-model="condition.operator" placeholder="Operator" style="width: 120px; margin-right: 10px;">
              <el-option label="=" value="EQ" />
              <el-option label="!=" value="NE" />
              <el-option label=">" value="GT" />
              <el-option label=">=" value="GE" />
              <el-option label="<" value="LT" />
              <el-option label="<=" value="LE" />
              <el-option label="IN" value="IN" />
              <el-option label="LIKE" value="LIKE" />
            </el-select>
            <el-input v-model="condition.value" placeholder="Value" style="width: 200px; margin-right: 10px;" />
            <el-button link type="danger" size="small" @click="removeRowCondition(index)">Delete</el-button>
          </div>
        </div>

        <el-divider content-position="left" v-if="['FIELD_LEVEL', 'COMBINED'].includes(formData.ruleType)">
          Field Level Masks
        </el-divider>
        <div v-if="['FIELD_LEVEL', 'COMBINED'].includes(formData.ruleType)" class="masks-section">
          <el-button type="primary" size="small" @click="addFieldMask">Add Field Mask</el-button>
          <div v-for="(mask, index) in formData.fieldMasks" :key="index" class="mask-item">
            <el-input v-model="mask.fieldName" placeholder="Field Name" style="width: 200px; margin-right: 10px;" />
            <el-select v-model="mask.maskType" placeholder="Mask Type" style="width: 150px; margin-right: 10px;">
              <el-option label="Hidden" value="HIDDEN" />
              <el-option label="Read Only" value="READ_ONLY" />
              <el-option label="Masked" value="MASKED" />
            </el-select>
            <el-button link type="danger" size="small" @click="removeFieldMask(index)">Delete</el-button>
          </div>
        </div>

        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">Enabled</el-radio>
            <el-radio :label="0">Disabled</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit">Confirm</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="assignRolesDialogVisible"
      title="Assign Roles"
      width="600px"
    >
      <el-transfer
        v-model="selectedRoleIds"
        :data="roleList"
        :titles="['Available Roles', 'Selected Roles']"
        :button-texts="['Remove', 'Add']"
      />
      <template #footer>
        <el-button @click="assignRolesDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleAssignRolesSubmit">Confirm</el-button>
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
  name: [{ required: true, message: 'Please enter rule name', trigger: 'blur' }],
  ruleType: [{ required: true, message: 'Please select rule type', trigger: 'change' }],
  resourceType: [{ required: true, message: 'Please enter resource type', trigger: 'blur' }]
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
    ROW_LEVEL: 'Row Level',
    FIELD_LEVEL: 'Field Level',
    COMBINED: 'Combined'
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
    console.error('Load roles failed', error)
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
    console.error('Load data failed', error)
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
  dialogTitle.value = 'Add Data Permission Rule'
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
    dialogTitle.value = 'Edit Data Permission Rule'
    Object.assign(formData, { ...res.data })
    dialogVisible.value = true
  } catch (error) {
    console.error('Get details failed', error)
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
    ElMessage.success('Assigned successfully')
    assignRolesDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Assign failed', error)
  }
}

const handleStatus = async (row: DataPermissionRule) => {
  try {
    await ElMessageBox.confirm(
      `Are you sure you want to ${row.status === 1 ? 'disable' : 'enable'} this rule?`,
      'Warning',
      { confirmButtonText: 'Confirm', cancelButtonText: 'Cancel', type: 'warning' }
    )
    await updateDataPermissionRule({
      id: row.id,
      status: row.status === 1 ? 0 : 1
    })
    ElMessage.success('Operation successful')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Operation failed', error)
    }
  }
}

const handleDelete = async (row: DataPermissionRule) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this rule?', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await deleteDataPermissionRule(row.id)
    ElMessage.success('Deleted successfully')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete failed', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value) {
      await updateDataPermissionRule(formData)
      ElMessage.success('Updated successfully')
    } else {
      await createDataPermissionRule(formData)
      ElMessage.success('Created successfully')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Submit failed', error)
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
