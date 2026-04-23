<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">
        <h3>在线考试</h3>
      </div>
      <el-menu
        :default-active="route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/questionBank">
          <el-icon><Collection /></el-icon>
          <span>题库管理</span>
        </el-menu-item>
        <el-menu-item index="/question">
          <el-icon><Document /></el-icon>
          <span>题目管理</span>
        </el-menu-item>
        <el-menu-item index="/examPaper">
          <el-icon><Notebook /></el-icon>
          <span>试卷管理</span>
        </el-menu-item>
        <el-menu-item index="/exam">
          <el-icon><Calendar /></el-icon>
          <span>考试发布</span>
        </el-menu-item>
        <el-menu-item index="/examRecord">
          <el-icon><List /></el-icon>
          <span>考试记录</span>
        </el-menu-item>
        <el-menu-item index="/myExam">
          <el-icon><Edit /></el-icon>
          <span>我的考试</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-right">
          <span>{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    userStore.logout()
    router.push('/login')
  } catch {
  }
}
</script>

<style scoped>
.layout-container {
  height: 100%;
}

.el-aside {
  background-color: #304156;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  border-bottom: 1px solid #1f2d3d;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.el-main {
  background-color: #f0f2f5;
}
</style>
