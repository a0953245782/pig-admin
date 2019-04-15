<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">

        <a-form 
          :form="form"
          layout="vertical"
          @submit="handleSubmit">
          <a-form-item
            label="用户名"
          >
            <a-input disabled :value="username" />
          </a-form-item>
          
          <a-form-item
            label="手机号"
            :required="true"
          >
            <a-input placeholder="请输入手机号"
              v-decorator="[ 'phone', {rules: [{ required: true, message: '手机号不能为空' },{ pattern: '^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$', message: '请输入正确的手机号'}], validateTrigger: 'blur'}]"/>
          </a-form-item>
          
          <a-form-item>
            <a-button type="primary"
              html-type="submit"
              :loading="state.loginBtn"
              :disabled="state.loginBtn">提交</a-button>
          </a-form-item>
        </a-form>

      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="showAvatar" >
          <a-icon type="cloud-upload-o" class="upload-icon"/>
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="option.img"/>
        </div>
      </a-col>

    </a-row>
    <update-avatar
      @cancel="handleUpdateAvatarCancel"
      @success="handleUpdateAvatarSuccess"
      :updateAvatarModelVisible="updateAvatarModelVisible">
    </update-avatar>
  </div>
</template>
<script>
import UpdateAvatar from './UpdateAvatar'
import { mapState, mapGetters } from 'vuex'
import { axios } from '@/utils/request'

export default {
  components: {
    UpdateAvatar
  },
  computed: {
    ...mapState({
      username: state => state.user.name,
      phone: state => state.user.phone
    })
  },
  mounted (){ 
    this.form.setFieldsValue({'phone': this.phone})
    this.option.img = this.avatar()
  },
  data () {
    return {
      updateAvatarModelVisible: false,
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false
      },
      option: {
        img: ''
      }
    }
  },
  methods: {
    ...mapGetters(['nickname', 'avatar']),
    handleSubmit (e) {
      e.preventDefault()
      const {
        form: { validateFields },
        state
      } = this
      state.loginBtn = true
      this.form.validateFields((err, values) => {
        if (!err) {
          const params = { ...values }
          params['username'] = this.username
          axios({
            url: '/user/edit',
            method: 'put',
            data: params
          }).then((res) => {
            this.$notification.success({
              message: '修改资料成功'
            })
          }).catch(err => {
            this.$notification.error({
              message: '修改资料失败'
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
      
    },
    handleUpdateAvatarCancel () {
      this.updateAvatarModelVisible = false
    },
    showAvatar () {
      this.updateAvatarModelVisible = true
    },
    handleUpdateAvatarSuccess (avatar) {
      this.updateAvatarModelVisible = false
      this.$notification.success({
        message: '更换头像成功'
      })
      
      this.option.img = `/avatar/` + avatar
      this.$store.commit('SET_AVATAR', this.option.img)
    }
  }
}
</script>
<style lang="less" scoped>
  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }

  .ant-upload-preview {
    position: relative;
    margin: 0 auto;
    width: 100%;
    max-width: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;

    .upload-icon {
      position: absolute;
      top: 0;
      right: 10px;
      font-size: 1.4rem;
      padding: 0.5rem;
      background: rgba(222, 221, 221, 0.7);
      border-radius: 50%;
      border: 1px solid rgba(0, 0, 0, 0.2);
    }
    .mask {
      opacity: 0;
      position: absolute;
      background: rgba(0,0,0,0.4);
      cursor: pointer;
      transition: opacity 0.4s;

      &:hover {
        opacity: 1;
      }

      i {
        font-size: 2rem;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -1rem;
        margin-top: -1rem;
        color: #d6d6d6;
      }
    }

    img, .mask {
      width: 100%;
      max-width: 180px;
      height: 100%;
      border-radius: 50%;
      overflow: hidden;
    }
  }
</style>



