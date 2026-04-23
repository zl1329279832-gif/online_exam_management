<template>
  <div class="page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>试卷管理</span>
          <el-button type="primary" @click="handleAdd">新增试卷</el-button>
        </div>
      </template>
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="paperName" label="试卷名称" />
        <el-table-column prop="totalScore" label="总分" width="100" />
        <el-table-column prop="passScore" label="及格分" width="100" />
        <el-table-column prop="duration" label="时长(分钟)" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleViewQuestions(row)">查看题目</el-button>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑试卷' : '新增试卷'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="试卷名称">
          <el-input v-model="form.paperName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="总分">
          <el-input-number v-model="form.totalScore" :min="0" />
        </el-form-item>
        <el-form-item label="及格分">
          <el-input-number v-model="form.passScore" :min="0" />
        </el-form-item>
        <el-form-item label="考试时长">
          <el-input-number v-model="form.duration" :min="1" placeholder="分钟" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="questionDialogVisible" title="试卷题目" width="800px">
      <el-button type="primary" size="small" @click="showAddQuestionDialog" style="margin-bottom: 10px">添加题目</el-button>
      <el-table :data="paperQuestions" border>
        <el-table-column prop="questionOrder" label="序号" width="80" />
        <el-table-column prop="questionId" label="题目ID" width="100" />
        <el-table-column prop="questionScore" label="分值" width="100" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" size="small" link @click="handleRemoveQuestion(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="questionDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="addQuestionDialogVisible" title="添加题目" width="600px">
      <el-form :model="questionForm" label-width="100px">
        <el-form-item label="题目ID">
          <el-input-number v-model="questionForm.questionId" :min="1" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="questionForm.questionOrder" :min="1" />
        </el-form-item>
        <el-form-item label="分值">
          <el-input-number v-model="questionForm.questionScore" :min="0" :step="0.5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addQuestionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddQuestion">添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const tableData = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const questionDialogVisible = ref(false)
const addQuestionDialogVisible = ref(false)
const isEdit = ref(false)
const saveLoading = ref(false)
const currentPaperId = ref<number | null>(null)
const paperQuestions = ref<any[]>([])

const form = reactive({
  id: null as number | null,
  paperName: '',
  description: '',
  totalScore: 100,
  passScore: 60,
  duration: 60
})

const questionForm = reactive({
  paperId: null as number | null,
  questionId: null as number | null,
  questionOrder: 1,
  questionScore: 5
})

const loadData = async () => {
  try {
    const res: any = await request.get('/examPaper/page', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch {
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, paperName: '', description: '', totalScore: 100, passScore: 60, duration: 60 })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(form, { id: row.id, paperName: row.paperName, description: row.description, totalScore: row.totalScore, passScore: row.passScore, duration: row.duration })
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.paperName) {
    ElMessage.warning('请输入试卷名称')
    return
  }
  saveLoading.value = true
  try {
    if (isEdit.value) {
      await request.put('/examPaper', form)
    } else {
      await request.post('/examPaper', form)
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
    await request.delete(`/examPaper/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch {
  }
}

const handleViewQuestions = async (row: any) => {
  currentPaperId.value = row.id
  try {
    const res: any = await request.get(`/examPaper/${row.id}/questions`)
    paperQuestions.value = res.data
  } catch {
  }
  questionDialogVisible.value = true
}

const showAddQuestionDialog = () => {
  questionForm.paperId = currentPaperId.value
  questionForm.questionId = null
  questionForm.questionOrder = 1
  questionForm.questionScore = 5
  addQuestionDialogVisible.value = true
}

const handleAddQuestion = async () => {
  try {
    await request.post(`/examPaper/${currentPaperId.value}/addQuestion`, questionForm)
    ElMessage.success('添加成功')
    addQuestionDialogVisible.value = false
    handleViewQuestions({ id: currentPaperId.value })
  } catch {
  }
}

const handleRemoveQuestion = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要移除此题目吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/examPaper/question/${row.id}`)
    ElMessage.success('移除成功')
    handleViewQuestions({ id: currentPaperId.value })
  } catch {
  }
}

onMounted(() => {
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
