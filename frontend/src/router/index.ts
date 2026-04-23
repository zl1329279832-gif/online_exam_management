import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      component: () => import('@/layouts/MainLayout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/Dashboard.vue')
        },
        {
          path: 'questionBank',
          name: 'questionBank',
          component: () => import('@/views/QuestionBank.vue')
        },
        {
          path: 'question',
          name: 'question',
          component: () => import('@/views/Question.vue')
        },
        {
          path: 'examPaper',
          name: 'examPaper',
          component: () => import('@/views/ExamPaper.vue')
        },
        {
          path: 'exam',
          name: 'exam',
          component: () => import('@/views/Exam.vue')
        },
        {
          path: 'examRecord',
          name: 'examRecord',
          component: () => import('@/views/ExamRecord.vue')
        },
        {
          path: 'myExam',
          name: 'myExam',
          component: () => import('@/views/MyExam.vue')
        },
        {
          path: 'doExam/:id',
          name: 'doExam',
          component: () => import('@/views/DoExam.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.token) {
    next('/login')
  } else if (to.path === '/login' && userStore.token) {
    next('/')
  } else {
    next()
  }
})

export default router
