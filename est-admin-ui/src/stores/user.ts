import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi, getCurrentUser, type LoginRequest, type UserInfo } from '@/api/auth'

const TOKEN_KEY = 'est_admin_token'
const USER_KEY = 'est_admin_user'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) || '')
  const userInfo = ref<UserInfo | null>(() => {
    const saved = localStorage.getItem(USER_KEY)
    return saved ? JSON.parse(saved) : null
  })

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem(TOKEN_KEY, newToken)
  }

  function setUserInfo(info: UserInfo) {
    userInfo.value = info
    localStorage.setItem(USER_KEY, JSON.stringify(info))
  }

  async function login(data: LoginRequest) {
    const res = await loginApi(data)
    if (res.data?.token) {
      setToken(res.data.token)
      if (res.data.user) {
        setUserInfo(res.data.user as UserInfo)
      }
    }
    return res
  }

  async function fetchUserInfo() {
    const res = await getCurrentUser()
    if (res.data) {
      setUserInfo(res.data)
    }
    return res
  }

  async function logout() {
    try {
      await logoutApi()
    } catch (e) {
      console.error('Logout API error:', e)
    }
    token.value = ''
    userInfo.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  function isLoggedIn() {
    return !!token.value
  }

  function hasPermission(permission: string) {
    if (!userInfo.value) return false
    if (userInfo.value.permissions.includes('*')) return true
    return userInfo.value.permissions.includes(permission)
  }

  function hasRole(role: string) {
    if (!userInfo.value) return false
    return userInfo.value.roles.includes(role)
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    login,
    logout,
    fetchUserInfo,
    isLoggedIn,
    hasPermission,
    hasRole
  }
})
