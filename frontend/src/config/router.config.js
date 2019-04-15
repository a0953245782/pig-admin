import { UserLayout, RouteView } from '@/components/layouts'


export const constantRouterMap = [
  {
      path: '/user',
      component: UserLayout,
      redirect: '/user/login',
      hidden: true,
      children: [
        {
          path: 'login',
          name: 'login',
          component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
        }
      ]
  },
  {
    path: '/404',
    name: '404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },
  {
    path: '/403',
    name: '403',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/403')
  },
  {
    path: '/500',
    name: '500',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/500')
  }
]

export const accountRouterMap =
{
  path: '/account',
  component: RouteView,
  redirect: '/account/settings',
  name: 'account',
  meta: { title: '个人页', icon: 'user', keepAlive: true},
  children: [
    {
      path: '/account/settings',
      name: 'settings',
      component: () => import('@/views/account/settings/Index'),
      meta: { title: '个人设置', hideHeader: true, keepAlive: true },
      redirect: '/account/settings/base',
      hideChildrenInMenu: true,
      children: [
        {
          path: '/account/settings/base',
          name: 'BaseSettings',
          component: () => import('@/views/account/settings/BaseSetting'),
          meta: { title: '基本设置', hidden: true, keepAlive: true }
        },
        {
            path: '/account/settings/security',
            name: 'SecuritySettings',
            component: () => import('@/views/account/settings/Security'),
            meta: { title: '密码管理', hidden: true, keepAlive: true }
        }
      ]
    }
  ]
}              

export const notFoundRouterMap = [
  {
    path: '*',
    redirect: '/404'
  }                             
]