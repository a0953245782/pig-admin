import api from './index'
import { axios } from '@/utils/request'

export function login (params) {
  let url = '/auth/login?'
  url += 'username=' + params['username'] + '&password=' + params['password']
    + '&code=' + params['code'] + '&grant_type=password&scope=ui'
  return axios({
    url: url,
    method: 'post',
    headers: {
      'Authorization': 'Basic dGVzdDp0ZXN0'
    }
  })
}

export function getInfo () {
  return axios({
    url: '/user/info',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function logout () {
  return axios({
    url: api.Logout,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getMenu () {
  return axios({
    url: '/menu',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}