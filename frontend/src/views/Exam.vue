<template>
  <div class="page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试发布</span>
          <el-button type="primary" @click="handleAdd">发布考试</el-button>
        </div>
      </template>
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="examName" label="考试名称" />
        <el-table-column prop="paperId" label="试卷ID" width="100" />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="{ 0: 'info', 1: 'success', 2: 'warning', 3: 'danger' }[row.status]">
              {{ { 0: '未开始', 1: '进行中', 2: '已结束', 3: '已取消' }[row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑考试' : '发布考试'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="考试名称">
          <el-input v-model="form.examName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="试卷">
          <el-select v-model="form.paperId" placeholder="请选择试卷">
            <el-option v-for="item in paperList" :key="item.id" :label="item.paperName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const tableData = ref<any[]>([])
const paperList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saveLoading = ref(false)

const form = reactive({
  id: null as number | null,
  examName: '',
  paperId: null as number | null,
  startTime: null as string | null,
  endTime: null as string | null,
  status: 1,
  description: ''
})

const loadPapers = async () => {
  try {
    const res: any = await request.get('/examPaper/page', { params: { pageNum: 1, pageSize: 100 } })
    paperList.value = res.data.records
  } catch {
  }
}

const loadData = async () => {
  try {
    const res: any = await request.get('/exam/page', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch {
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, examName: '', paperId: null, startTime: null, endTime: null, status: 1, description: '' })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(form, {
    id: row.id,
    examName: row.examName,
    paperId: row.paperId,
    startTime: row.startTime,
    endTime: row.endTime,
    status: row.status,
    description: row.description
  })
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.examName || !form.paperId || !form.startTime || !form.endTime) {
    ElMessage.warning('请填写必要信息')
    return
  }
  saveLoading.value = true
  try {
    if (isEdit.value) {
      await request.put('/exam', form)
    } else {
      await request.post('/exam', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch {
  } finally {
    saveLoading.value = false
  }
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/exam/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch {
  }
}

onMounted(() => {
  loadPapers()
  loadData()
})
</script>

<style scoped>
.page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
