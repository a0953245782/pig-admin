<template>
  <a-modal
    :title="title"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <template slot="footer">
        <a-button key="submit" type="primary" @click="handleSubmit">
          {{ this.title }}
        </a-button>
        <a-button key="back" @click="handleCancel">取消</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-item
              label="键"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['keyy', {rules: [{required: true, message: '请输入键！'}]}]" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item
              label="值"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['valuee', { rules: [{required: true, message: '请输入值！'}]}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="表名"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['tableName', {rules: [{required: true, message: '请输入表名！'}]}]" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="字段"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['fieldName', {rules: [{required: true, message: '请输入字段名！'}]}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24" v-if="this.title === '修改'">
            <a-form-item
              label="创建时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['createTime']" disabled/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { axios } from '@/utils/request'
import { deepClone } from '@/utils/util'
import moment from 'moment'
import pick from 'lodash.pick'

export default {
  props: {
    record: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      title: '',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 13 }
      },
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this)
    }
  },
  methods: {
    add () {
      this.visible = true
      this.title = '新增'
      this.form.resetFields()
    },
    edit () {
      this.visible = true
      this.title = '修改'
    
      this.$nextTick(() => {
        this.loadEditInfo(this.record)
      })
    },
    loadEditInfo (data) {
      const { form } = this
      const formData = pick(data, ['keyy', 'valuee', 'tableName', 'fieldName', 'createTime'])
      formData.createTime = moment(data.createTime).format('YYYY-MM-DD HH-mm-ss')
      form.setFieldsValue(formData)
    },
    handleSubmit () {
      const { form: { validateFields } } = this
      this.confirmLoading = true
      validateFields((errors, values) => {
        if (!errors) {
          let method = 'post'
          let data = values
          if(this.record.createTime){
            method = 'put'
            values.createTime = ''
            let params = deepClone(this.record)
            data = Object.assign(params, values)
          }else{
            data = Object.assign({'createTime': ''}, values)
          }
          axios({
            url: '/dict',
            method: method,
            data: data
          }).then(() => {
            this.visible = false
            this.confirmLoading = false
            this.$notification.success({
              message: this.title + '成功'
            })
            this.$emit('ok', values)
          }).catch(() => {
            this.$notification.error({
              message: this.title +  '失败'
            })
          })
        } else {
          this.confirmLoading = false
        }
      })
    },
    handleCancel () {
      this.visible = false
    }
  }
}
</script>
