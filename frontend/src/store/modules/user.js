import Vue from 'vue'
import { login, getInfo, logout, getMenu } from '@/api/login'
import { welcome, deepClone } from '@/utils/util'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { constantRouterMap, accountRouterMap } from '@/config/router.config'
import { BasicLayout, RouteView, PageView } from '@/components/layouts'

function regenRouter(menu){
  const rootRouter = [
    {
      path: '/',
      name: 'index',
      component: BasicLayout,
      meta: { title: '首页' },
      redirect: '/dashboard/workplace',
      children: []
    }
  ]

  const newRouters = []
  menu.forEach(d => {
    const children = []
    d.children.forEach(child => {
      const c = {
        path: '/' + child.path,
        name: child.name,
        component: () => import('@/' + child.component),
        meta: { title: child.label, icon: child.icon, keepAlive: child.keepAlive }
      }
      children.push(c)
    })
    const newRouter = {
      path: d.path,
      name: d.name,
      meta: { title: d.label, icon: d.icon, keepAlive: d.keepAlive },
      component: PageView,
      children: children
    }
    if (d.path === '/dashboard'){
      newRouter.redirect = '/' + d.component
    }
    newRouters.push(newRouter)
  })
  newRouters.push(accountRouterMap)
  rootRouter[0].children = newRouters
  return rootRouter
}


const user = {
  state: {
    token: '',
    name: '',
    phone: '',
    welcome: '',
    avatar: '',
    roles: [],
    permissions: [],
    info: {},
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_PHONE: (state, phone) => {
      state.phone = phone
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_MENU: (state, menu) => {
      state.menu = menu
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          const result = response.access_token
          Vue.ls.set(ACCESS_TOKEN, result, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', result)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const data = response.data

          commit('SET_AVATAR', `/avatar/` + data.sysUser.avatar)
          commit('SET_PHONE', data.sysUser.phone)
          commit('SET_NAME', { name: data.sysUser.username, welcome: welcome() })
          commit('SET_INFO', data.sysUser)
          commit('SET_ROLES', data.roles || [])
          commit('SET_PERMISSIONS', data.permissions || [])
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    Logout ({ commit, state } ) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {  
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          Vue.ls.remove(ACCESS_TOKEN)
        })
      })
    },
    GenerateRoutes ({ commit }) {
      return new Promise((resolve, reject) => {
        getMenu().then(response => {
          const data = response.data
          let menu = deepClone(data)
          const accessedRouters = regenRouter(menu)
          commit('SET_ROUTERS', accessedRouters)
          
          resolve()
        }).catch(() => {
          reject()
        })
      })
    }
  }

}

export default user