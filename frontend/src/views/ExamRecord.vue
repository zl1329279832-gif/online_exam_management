<template>
  <div class="page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试记录</span>
        </div>
      </template>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="考试">
          <el-select v-model="searchForm.examId" placeholder="请选择考试" clearable style="width: 200px;">
            <el-option v-for="item in examList" :key="item.id" :label="item.examName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户">
          <el-select v-model="searchForm.userId" placeholder="请选择用户" clearable style="width: 200px;">
            <el-option v-for="item in userList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" border style="width: 100%;">
        <el-table-column prop="id" label="记录ID" width="100" />
        <el-table-column prop="examName" label="考试名称" min-width="150" />
        <el-table-column prop="userName" label="用户名称" min-width="120" />
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="score" label="得分" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.score && row.score >= 60 ? '#67C23A' : '#F56C6C' }">
              {{ row.score }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 3 ? 'success' : row.status === 2 ? 'info' : 'warning'">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        @current-change="loadData"
        @size-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { formatDateTime } from '@/utils/format'

const tableData = ref<any[]>([])
const examList = ref<any[]>([])
const userList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  examId: null as number | null,
  userId: null as number | null
})

const loadExams = async () => {
  try {
    const res: any = await request.get('/exam/page', { params: { pageNum: 1, pageSize: 100 } })
    examList.value = res.data.records
  } catch {
  }
}

const loadUsers = async () => {
  try {
    const res: any = await request.get('/user/list')
    userList.value = res.data
  } catch {
  }
}

const loadData = async () => {
  try {
    const res: any = await request.get('/examRecord/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        examId: searchForm.examId,
        userId: searchForm.userId
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch {
  }
}

const handleReset = () => {
  searchForm.examId = null
  searchForm.userId = null
  pageNum.value = 1
  loadData()
}

const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '未开始',
    1: '进行中',
    2: '已提交',
    3: '已批改'
  }
  return statusMap[status] || '未知'
}

const handleDetail = (row: any) => {
  console.log('查看详情', row)
}

onMounted(async () => {
  await Promise.all([loadExams(), loadUsers()])
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
