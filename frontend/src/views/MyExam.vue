<template>
  <div class="page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的考试</span>
        </div>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="可参加的考试" name="available">
          <el-table :data="examList" border>
            <el-table-column prop="id" label="考试ID" width="100" />
            <el-table-column prop="examName" label="考试名称" />
            <el-table-column prop="startTime" label="开始时间" width="180" />
            <el-table-column prop="endTime" label="结束时间" width="180" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="{ 0: 'info', 1: 'success', 2: 'warning', 3: 'danger' }[row.status]">
                  {{ { 0: '未开始', 1: '进行中', 2: '已结束', 3: '已取消' }[row.status] }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleStartExam(row)">参加考试</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="我的成绩" name="scores">
          <el-table :data="recordList" border>
            <el-table-column prop="id" label="记录ID" width="100" />
            <el-table-column prop="examId" label="考试ID" width="100" />
            <el-table-column prop="startTime" label="开始时间" width="180" />
            <el-table-column prop="submitTime" label="提交时间" width="180" />
            <el-table-column prop="score" label="得分" width="100">
              <template #default="{ row }">
                <span :style="{ color: row.score >= 60 ? '#67C23A' : '#F56C6C' }">{{ row.score }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 3 ? 'success' : 'warning'">
                  {{ row.status === 0 ? '未开始' : row.status === 1 ? '进行中' : row.status === 2 ? '已提交' : '已批改' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="loadRecords"
            style="margin-top: 20px; justify-content: flex-end"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const activeTab = ref('available')
const examList = ref<any[]>([])
const recordList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadExams = async () => {
  try {
    const res: any = await request.get('/exam/page', { params: { pageNum: 1, pageSize: 100, status: 1 } })
    examList.value = res.data.records
  } catch {
  }
}

const loadRecords = async () => {
  try {
    const res: any = await request.get('/examRecord/my', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value }
    })
    recordList.value = res.data.records
    total.value = res.data.total
  } catch {
  }
}

const handleStartExam = async (row: any) => {
  try {
    const res: any = await request.post(`/examRecord/start/${row.id}`)
    ElMessage.success('开始考试')
    router.push(`/doExam/${res.data}`)
  } catch {
  }
}

onMounted(() => {
  loadExams()
  loadRecords()
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
