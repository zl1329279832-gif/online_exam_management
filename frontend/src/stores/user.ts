import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

interface UserInfo {
  userId: number
  username: string
  realName: string
  roles: string[]
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)
  
  // 从localStorage恢复userInfo
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    try {
      userInfo.value = JSON.parse(savedUserInfo)
    } catch {
      localStorage.removeItem('userInfo')
    }
  }

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const login = async (username: string, password: string) => {
    const res: any = await request.post('/auth/login', { username, password })
    setToken(res.data.token)
    setUserInfo(res.data)
    return res
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    login,
    logout
  }
})
