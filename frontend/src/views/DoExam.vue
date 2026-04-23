<template>
  <div class="do-exam">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试中...</span>
          <div class="timer">
            剩余时间: {{ formatTime(remainingTime) }}
          </div>
        </div>
      </template>
      <div v-if="questions.length === 0" class="empty">
        暂无题目
      </div>
      <div v-else class="questions">
        <div v-for="(question, index) in questions" :key="question.id" class="question-item">
          <div class="question-title">
            <span class="index">{{ index + 1 }}.</span>
            <span class="type">[{{ { 1: '单选', 2: '多选', 3: '判断' }[question.questionType] }}]</span>
            <span class="content">{{ question.questionContent }}</span>
            <span class="score">({{ question.questionScore }}分)</span>
          </div>
          <div class="question-options" v-if="question.options">
            <div v-for="(opt, optIndex) in question.optionsList" :key="optIndex" class="option">
              <el-radio
                v-if="question.questionType === 1 || question.questionType === 3"
                :label="String(optIndex)"
                v-model="answers[question.id]"
              >
                {{ opt }}
              </el-radio>
              <el-checkbox
                v-if="question.questionType === 2"
                :label="String(optIndex)"
                v-model="answerList[question.id]"
              >
                {{ opt }}
              </el-checkbox>
            </div>
          </div>
        </div>
      </div>
      <div class="actions">
        <el-button type="primary" size="large" @click="handleSubmit" :loading="submitLoading">提交试卷</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const recordId = Number(route.params.id)
const remainingTime = ref(3600)
const questions = ref<any[]>([])
const answers = reactive<Record<string, string>>({})
const answerList = reactive<Record<string, string[]>>({})
const submitLoading = ref(false)

let timer: any = null

const formatTime = (seconds: number) => {
  const h = Math.floor(seconds / 3600)
  const m = Math.floor((seconds % 3600) / 60)
  const s = seconds % 60
  return `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

const loadData = async () => {
  try {
    const recordRes: any = await request.get(`/examRecord/${recordId}`)
    const record = recordRes.data
    const paperId = record.paperId
    const questionsRes: any = await request.get(`/examPaper/${paperId}/questions`)
    const paperQuestions = questionsRes.data

    const questionPromises = paperQuestions.map((pq: any) =>
      request.get(`/question/${pq.questionId}`)
    )
    const questionResults = await Promise.all(questionPromises)

    questions.value = paperQuestions.map((pq: any, index: number) => {
      const q = questionResults[index].data
      return {
        ...pq,
        questionContent: q.questionContent,
        questionType: q.questionType,
        options: q.options,
        optionsList: q.options ? JSON.parse(q.options) : []
      }
    })

    questions.value.forEach(q => {
      if (!answers[q.id]) {
        if (q.questionType === 2) {
          answerList[q.id] = []
        } else {
          answers[q.id] = ''
        }
      }
    })
  } catch (e) {
    console.error(e)
  }
}

const handleSubmit = async () => {
  try {
    await ElMessageBox.confirm('确定要提交试卷吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    submitLoading.value = true

    const finalAnswers: Record<string, string> = {}
    questions.value.forEach(q => {
      if (q.questionType === 2) {
        finalAnswers[q.id] = answerList[q.id].sort().join(',')
      } else {
        finalAnswers[q.id] = answers[q.id] || ''
      }
    })

    await request.post(`/examRecord/submit/${recordId}`, finalAnswers, {
      headers: { 'Content-Type': 'application/json' }
    })

    ElMessage.success('提交成功')
    router.push('/myExam')
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadData()
  timer = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
    } else {
      handleSubmit()
    }
  }, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.do-exam {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.timer {
  font-weight: bold;
  color: #F56C6C;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #999;
}

.questions {
  max-width: 900px;
  margin: 0 auto;
}

.question-item {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
}

.question-title {
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 16px;
}

.index {
  font-weight: bold;
  margin-right: 8px;
}

.type {
  color: #409EFF;
  margin-right: 8px;
}

.score {
  color: #999;
  margin-left: 8px;
}

.question-options {
  margin-left: 20px;
}

.option {
  margin-bottom: 12px;
}

.actions {
  text-align: center;
  margin-top: 40px;
}
</style>
