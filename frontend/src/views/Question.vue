<template>
  <div class="page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>题目管理</span>
          <el-button type="primary" @click="handleAdd">新增题目</el-button>
        </div>
      </template>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="题库">
          <el-select v-model="searchForm.bankId" placeholder="请选择题库" clearable>
            <el-option v-for="item in bankList" :key="item.id" :label="item.bankName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.questionType" placeholder="请选类型" clearable>
            <el-option label="单选题" :value="1" />
            <el-option label="多选题" :value="2" />
            <el-option label="判断题" :value="3" />
            <el-option label="填空题" :value="4" />
            <el-option label="简答题" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="questionType" label="类型" width="100">
          <template #default="{ row }">
            {{ { 1: '单选', 2: '多选', 3: '判断', 4: '填空', 5: '简答' }[row.questionType] }}
          </template>
        </el-table-column>
        <el-table-column prop="questionContent" label="题目内容" show-overflow-tooltip />
        <el-table-column prop="score" label="分值" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑题目' : '新增题目'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="所属题库">
          <el-select v-model="form.bankId" placeholder="请选择题库">
            <el-option v-for="item in bankList" :key="item.id" :label="item.bankName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目类型">
          <el-select v-model="form.questionType" placeholder="请选类型">
            <el-option label="单选题" :value="1" />
            <el-option label="多选题" :value="2" />
            <el-option label="判断题" :value="3" />
            <el-option label="填空题" :value="4" />
            <el-option label="简答题" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容">
          <el-input v-model="form.questionContent" type="textarea" :rows="3" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="选项" v-if="form.questionType === 1 || form.questionType === 2">
          <el-input v-model="form.options" type="textarea" :rows="3" placeholder="JSON数组格式，如：[\"A选项\", \"B选项\"]" />
        </el-form-item>
        <el-form-item label="正确答案">
          <el-input v-model="form.answer" placeholder="请输入答案" />
        </el-form-item>
        <el-form-item label="分值">
          <el-input-number v-model="form.score" :min="0" :step="1" />
        </el-form-item>
        <el-form-item label="解析">
          <el-input v-model="form.analysis" type="textarea" :rows="2" />
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
const bankList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saveLoading = ref(false)

const searchForm = reactive({
  bankId: null as number | null,
  questionType: null as number | null
})

const form = reactive({
  id: null as number | null,
  bankId: null as number | null,
  questionType: 1,
  difficulty: 1,
  questionContent: '',
  options: '',
  answer: '',
  analysis: '',
  score: 5
})

const loadBanks = async () => {
  try {
    const res: any = await request.get('/questionBank/page', { params: { pageNum: 1, pageSize: 100 } })
    bankList.value = res.data.records
  } catch {
  }
}

const loadData = async () => {
  try {
    const res: any = await request.get('/question/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        bankId: searchForm.bankId,
        questionType: searchForm.questionType
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch {
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    bankId: null,
    questionType: 1,
    difficulty: 1,
    questionContent: '',
    options: '',
    answer: '',
    analysis: '',
    score: 5
  })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(form, {
    id: row.id,
    bankId: row.bankId,
    questionType: row.questionType,
    difficulty: row.difficulty,
    questionContent: row.questionContent,
    options: row.options,
    answer: row.answer,
    analysis: row.analysis,
    score: row.score
  })
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.bankId || !form.questionContent || !form.answer) {
    ElMessage.warning('请填写必要信息')
    return
  }
  saveLoading.value = true
  try {
    if (isEdit.value) {
      await request.put('/question', form)
    } else {
      await request.post('/question', form)
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
    await request.delete(`/question/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch {
  }
}

onMounted(() => {
  loadBanks()
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
