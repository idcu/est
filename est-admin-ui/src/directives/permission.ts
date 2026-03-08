import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores/user'

const permission: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding
    const userStore = useUserStore()
    
    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = value.some((perm: string) => {
        return userStore.hasPermission(perm)
      })
      
      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('需要权限！�?v-permission="[\'system:user:add\']"')
    }
  }
}

export default permission
