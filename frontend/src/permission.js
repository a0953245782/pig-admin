import Vue from 'vue'
import router from './router'
import store from './store'

import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style

import { ACCESS_TOKEN } from '@/store/mutation-types'
import { notFoundRouterMap } from '@/config/router.config'

NProgress.configure({ showSpinner: false })

const whiteList = ['login']

router.beforeEach((to, from, next) => {
    NProgress.start() // start progress bar
    if(Vue.ls.get(ACCESS_TOKEN)) {
        if(to.path === '/user/login'){
          next({ path: '/' })
          NProgress.done()
        }else{
          if (store.getters.roles.length === 0){
            store.dispatch('GetInfo')
              .then(() => {
                // next({...to, replace: true})
                store.dispatch('GenerateRoutes').then(() => {
                  router.addRoutes(store.getters.addRouters)
                  router.addRoutes(notFoundRouterMap) 
                  const redirect = decodeURIComponent(from.query.redirect || to.path)
                  if (to.path === redirect) {
                    // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                    // next({ ...to, replace: true })
                    next({ path: redirect })
                  } else {
                    // 跳转到目的路由
                    next({ path: redirect })
                  }
                })
              })
          } else {
            next()
          }
        }
    }else{
      if (whiteList.includes(to.name)) {
        // 在免登录白名单，直接进入
        next()
      } else {
        next({ path: '/user/login', query: { redirect: to.fullPath } })
        NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
      }
    }
})

router.afterEach(() => {
    NProgress.done() // finish progress bar
})