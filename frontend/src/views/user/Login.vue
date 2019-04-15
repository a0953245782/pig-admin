<template>
  <div class="main">
    <a-form 
      id="formLogin"
      class="user-layout-login"
      ref="formLogin"
      :form="form"
      @submit="handleSubmit"
    >
      <a-tabs
        :activeKey="customActiveKey"
        :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick"
      >
        <a-tab-pane 
          tab="用户登录"
          key="tab1">
          <a-form-item>
              <a-input 
                size="large"
                type="text"
                placeholder="帐户名"
                v-decorator="[ 'username', {rules: [{ required: true, message: '请输入帐户名' }], validateTrigger: 'change'}]">
              <a-icon 
                slot="prefix"
                type="user"
                :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>
          <a-form-item>
              <a-input 
                size="large"
                type="password"
                placeholder="密码"
                v-decorator="[ 'password', {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'change'}]">
              <a-icon 
                slot="prefix"
                type="lock"
                :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>
          <a-row :gutter="16">
            <a-col class="gutter-row" :span="16">
              <a-form-item>
                <a-input
                  size="large"
                  type="text"
                  placeholder="验证码"
                  maxlength="4"
                  v-decorator="[
                    'code',
                    {rules: [{ required: true, message: '请输入验证码' }], validateTrigger: 'blur'}
                  ]"
                >
                  <a-icon slot="prefix" type="safety" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col class="gutter-row" :span="8">
              <img :src="src" class="login-code-img" width="130" height="37" @click.stop.prevent="refreshCode" />
            </a-col>
          </a-row>
          <a-form-item style="margin-top:24px">
              <a-button
                size="large"
                type="primary"
                html-type="submit"
                class="login-button"
                :loading="state.loginBtn"
                :disabled="state.loginBtn"
              >
              登录
              </a-button>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>
    </a-form>
  </div>
</template> 
<script> 
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util';

export default {
  data () {
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      form: this.$form.createForm(this),
      src: '',
      state: {
        time: 60,
        loginBtn: false
      }
    }
  },
  created () {
    this.refreshCode()
  },
  methods: {
    ...mapActions(['Login']),
    refreshCode () {
      this.code = ''
      this.src = '/code?d=' + new Date().getTime()
    },
    handleTabClick (key) {
      this.customActiveKey = key
    },
    handleSubmit (e) {
      e.preventDefault()
      const {
        form: { validateFields },
        state,
        customActiveKey,
        Login
      } = this

      state.loginBtn = true
      const validateFieldsKey = ['username', 'password', 'code']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if(!err){
            const loginParams = { ...values }
            Login(loginParams)
              .then((res) => this.loginSuccess(res))
              .catch(err => this.requestFailed(err))
              .finally(() => {
                state.loginBtn = false
            })
        }else{
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    loginSuccess (res) {
      this.$router.push({ name: 'index' })
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)
    },
    requestFailed (err) {
      if (err.response.status === 426) {
        this.$notification.error({ message: '登录错误', description: err.response.data.msg })
      	return
      }
      this.$notification['error']({
        message: '错误',
        description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
    }
  }
}
</script>
<style lang="less" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}
</style>


