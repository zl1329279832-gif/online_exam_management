<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409EFF">
              <el-icon><Collection /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.questionBankCount }}</div>
              <div class="stat-label">题库数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67C23A">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.questionCount }}</div>
              <div class="stat-label">题目数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #E6A23C">
              <el-icon><Notebook /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.examPaperCount }}</div>
              <div class="stat-label">试卷数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #F56C6C">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.examCount }}</div>
              <div class="stat-label">考试数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>欢迎使用在线考试管理系统</span>
        </div>
      </template>
      <div>
        <p>系统功能：</p>
        <ul>
          <li>题库管理：创建和管理题库</li>
          <li>题目管理：添加单选、多选、判断等题目</li>
          <li>试卷管理：创建试卷并添加题目</li>
          <li>考试发布：发布考试并设置时间</li>
          <li>考试记录：查看所有考试记录</li>
          <li>我的考试：参加考试和查看成绩</li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import request from '@/utils/request'

const stats = reactive({
  questionBankCount: 0,
  questionCount: 0,
  examPaperCount: 0,
  examCount: 0
})

const loadStats = async () => {
  try {
    const [bankRes, questionRes, paperRes, examRes] = await Promise.all([
      request.get('/questionBank/page', { params: { pageNum: 1, pageSize: 1 } }),
      request.get('/question/page', { params: { pageNum: 1, pageSize: 1 } }),
      request.get('/examPaper/page', { params: { pageNum: 1, pageSize: 1 } }),
      request.get('/exam/page', { params: { pageNum: 1, pageSize: 1 } })
    ])
    stats.questionBankCount = bankRes.data.total || 0
    stats.questionCount = questionRes.data.total || 0
    stats.examPaperCount = paperRes.data.total || 0
    stats.examCount = examRes.data.total || 0
  } catch {
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 30px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

ul {
  margin-left: 20px;
  margin-top: 10px;
}

li {
  line-height: 2;
}
</style>
