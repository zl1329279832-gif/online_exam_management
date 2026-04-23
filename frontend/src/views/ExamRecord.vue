<template>
  <div class="page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试记录</span>
        </div>
      </template>
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="考试ID">
          <el-input-number v-model="searchForm.examId" :min="0" placeholder="请输入考试ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="记录ID" width="100" />
        <el-table-column prop="examId" label="考试ID" width="100" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="submitTime" label="提交时间" width="180" />
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
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleDetail(row)">详情</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const tableData = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  examId: null as number | null
})

const loadData = async () => {
  try {
    const res: any = await request.get('/examRecord/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        examId: searchForm.examId
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch {
  }
}

const handleReset = () => {
  searchForm.examId = null
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
