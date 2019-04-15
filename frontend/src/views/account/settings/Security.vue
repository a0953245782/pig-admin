<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">

        <a-form layout="vertical"
          :form="form"
          @submit="handleSubmit">
          
          <a-form-item
            label="旧密码"
          >
            <a-input type="password" placeholder="请输入旧密码"
              v-decorator="[ 'password', {rules: [{ required: true, min: 6, message: '旧密码不能为空且不少于6位' }], validateTrigger: 'blur'}]"/>
          </a-form-item>
          
          <a-form-item
            label="新密码"
          >
            <a-input type="password" placeholder="请输入新密码" 
              v-decorator="[ 'newpassword1', {rules: [{ required: true, min: 6, message: '新密码不能为空且不少于6位' }], validateTrigger: 'blur'}]"/>
          </a-form-item>
          
          <a-form-item
            label="确认密码"
          >
            <a-input type="password" placeholder="请输入确认密码"
              v-decorator="[ 'newpassword2', {rules: [{ required: true, min: 6, message: '确认密码不能为空且不少于6位' }, {validator: validatePass}], validateTrigger: 'blur'}]"/>
          </a-form-item>

          <a-form-item>
            <a-button type="primary"
              html-type="submit"
              :loading="state.loginBtn"
              :disabled="state.loginBtn">提交</a-button>
          </a-form-item>
        </a-form>

      </a-col>

    </a-row>
  </div>
</template>
<script>
import { axios } from '@/utils/request'
import store from '@/store'
import { mapState } from 'vuex'

export default {
  data () {
    return {
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false
      }
    }
  },
  computed: {
    ...mapState({
      username: state => state.user.name
    })
  },
  methods: {
    validatePass (rule, value, callback) {
      const form = this.form;
      if (value && value !== form.getFieldValue('newpassword1')) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    },
    handleSubmit (e) {
      e.preventDefault()
      const {
        form: { validateFields },
        state
      } = this
      state.loginBtn = true
      this.form.validateFields((err, values) => {
        if (!err) {
          const pwdParams = { ...values }
          pwdParams['username'] = this.username
          axios({
            url: '/user/edit',
            method: 'put',
            data: pwdParams
          }).then((res) => {
              if(res.code === 0){
                this.$notification.error({
                  message: res.msg
                })
              }else{
                this.$notification.success({
                  message: '修改密码成功'
                })
                // 修改密码之后强制重新登录
                store.dispatch('Logout').then(() => {
                  setTimeout(() => {
                    window.location.reload()
                  }, 1500)
                  return
                })
              }
          }).catch(err => {
            this.$notification.error({
              message: '修改密码失败'
            })
          }).finally(() => {
              state.loginBtn = false
          })
          
        }else{
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
        
      })
      
    }
  }
}
</script>
<style lang="less" scoped>

 

 
</style>



