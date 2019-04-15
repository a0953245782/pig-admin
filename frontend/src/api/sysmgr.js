import { axios } from '@/utils/request'

const api = {
  user: '/user/page',
  log: '/log/page',
  dict: '/dict/page',
  role: '/role/page',
  dept: '/dept/tree',
  menu: '/menu/tree',
  roleList: '/role/list'
}

export default api

export function getUserList (parameter) {
  return axios({
    url: api.user,
    method: 'get',
    params: parameter
  })
}

export function getLogList (parameter) {
  return axios({
    url: api.log,
    method: 'get',
    params: parameter
  })
}

export function getDictList (parameter) {
  return axios({
    url: api.dict,
    method: 'get',
    params: parameter
  })
}

export function getRoleList (parameter) {
  return axios({
    url: api.role,
    method: 'get',
    params: parameter
  })
}

export function deptRoleList () {
  return axios({
    url: api.roleList,
    method: 'get'
  })
}

export function getDeptTree (parameter) {
  return axios({
    url: api.dept,
    method: 'get',
    params: parameter
  })
}

export function getMenuTree (parameter) {
  return axios({
    url: api.menu,
    method: 'get',
    params: parameter
  })
}




