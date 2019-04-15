<template>
<div>
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
        <a-row v-if="status === 'edit'">
          <a-col :md="24" :sm="24">
            <a-form-item
              label="id"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="[ 'userId' ]" disabled />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-item
              label="用户名"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input :disabled="disabled" v-decorator="['username', {rules: [{required: true, message: '请输入用户名！'}]}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-item
              label="密码"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              v-if="status === 'new'"
            >
              <a-input type="password" v-decorator="['password', { rules: [{required: true, message: '请输入密码！'}]}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" >
            <a-form-item
              label="所属部门"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['deptName', {rules: [{required: true, message: '请输入所属部门！'}]}]" readOnly @click="showDeptTree" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" >
            <a-form-item
              label="手机号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input v-decorator="['phone']" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" >
            <a-form-item
              label="角色"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-select
                mode="multiple"
                placeholder="请选择"
                v-decorator="['role', {rules: [{required: true, message: '请输入角色！'}]}]"
                @change="handleChange"
                >
                <a-select-option v-for="item in rolesOptions" :key="item.roleId">
                  {{item.roleName}}
                </a-select-option>
              </a-select>
              <!--
              <a-input v-decorator="['roleId', {rules: [{required: true, message: '请输入角色！'}]}]" />
              -->
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" >
            <a-form-item
              label="状态"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-input-group compact>
                <a-select v-model="lockFlag">
                  <a-select-option value="0">正常</a-select-option>
                  <a-select-option value="9">锁定</a-select-option>
                </a-select>
              </a-input-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" v-if="this.title === '修改'">
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
  
  <a-modal
    title="请选择"
    :visible="treeVisible"
    @ok="handleTreeSubmit"
    @cancel="handleTreeCancel">
      <a-input-search style="margin-bottom: 8px" placeholder="Search" @change="onChange" />
      <a-tree
        ref="tree"
        :autoExpandParent="autoExpandParent"
        :treeData="orgTree"
        @select="onSelect"
        :expandedKeys="expandedKeys"
        v-if="orgTree.length > 0"
        >
          <template slot="title" slot-scope="{title}">
            <span v-if="title.indexOf(searchValue) > -1">
              {{title.substr(0, title.indexOf(searchValue))}}
              <span style="color: #f50">{{searchValue}}</span>
              {{title.substr(title.indexOf(searchValue) + searchValue.length)}}
            </span>
            <span v-else>{{title}}</span>
          </template>
        </a-tree>
  </a-modal>
</div>
</template>

<script>
import { axios } from '@/utils/request'
import { deepClone } from '@/utils/util'
import { getDeptTree, deptRoleList } from '@/api/sysmgr'
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
      treeVisible: false,
      confirmLoading: false,
      rolesOptions: [],
      orgTree: [],
      searchValue: '',
      expandedKeys: [],
      dataList: [],
      autoExpandParent: true,
      treeId: '',
      role: '',
      lockFlag: '0',
      status: 'new',
      disabled: false,
      form: this.$form.createForm(this)
    }
  },
  created () {
    deptRoleList().then(res => {
      this.rolesOptions = res.data
    })
    getDeptTree().then(res => {
      this.generateList(res.data)
      this.orgTree = res.data
    })
  },
  methods: {
    add () {
      this.visible = true
      this.title = '新增'
      this.form.resetFields()
      this.status = 'new'
      this.disabled = false
    },
    edit () {
      this.visible = true
      this.title = '修改'
      this.status = 'edit'
      this.disabled = true
      this.$nextTick(() => {
        this.loadEditInfo(this.record)
      })
    },
    loadEditInfo (data) {
      const { form } = this
      const formData = pick(data, [ 'userId', 'username', 'deptId', 'phone', 'role', 'createTime'])
      formData.createTime = moment(data.createTime).format('YYYY-MM-DD HH-mm-ss')
      form.setFieldsValue(formData)
      const roleArr = []
      data.roleList.forEach(function(item){
        roleArr.push(item.roleId)
      })
      this.treeId = [data.deptId]
      axios({
        url: '/dept/' + data.deptId,
        method: 'get'
      }).then(res => {
        this.lockFlag = data.lockFlag
        this.form.setFieldsValue({ 'deptName': res.data.name, 'role': roleArr })
      })
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
            params['deptId'] = this.treeId[0]
            data = Object.assign(params, values)
            data['lockFlag'] = this.lockFlag 
          }else{
            data = Object.assign({'createTime': '', userId: '', deptId: this.treeId[0], lockFlag: this.lockFlag}, values)
          }
          axios({
            url: '/user',
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
    },
    handleChange(value) {
      this.role = value
    },
    showDeptTree () {
      this.treeVisible = true
    },
    generateList (data) {
      for (let i = 0; i < data.length; i++) {
        const node = data[i]
        const key = node.key
        const title = node.title
        this.dataList.push({ key, title: title })
        if (node.children) {
          this.generateList(node.children, node.key)
        }
      }
	},
    getParentKey (key, tree) {
      let parentKey
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node.children) {
          if (node.children.some(item => item.key === key)) {
            parentKey = node.key
          } else if (this.getParentKey(key, node.children)) {
            parentKey = this.getParentKey(key, node.children)
          }
        }
      }
      return parentKey
    },
    onExpand  (expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    onChange (e) {
      const value = e.target.value
      const expandedKeys = this.dataList.map((item) => {
        if (item.title.indexOf(value) > -1) {
          return this.getParentKey(item.key, this.orgTree)
        }
        return null
      }).filter((item, i, self) => item && self.indexOf(item) === i)
      Object.assign(this, {
        expandedKeys,
        searchValue: value,
        autoExpandParent: true
      })
    },
    onSelect (treeId) {
      if (!treeId){
        return
      }
      this.treeId = treeId
      axios({
        url: '/dept/' + this.treeId[0],
        method: 'get'
      }).then(res => {
        this.treeVisible = false
        this.form.setFieldsValue({ 'deptName': res.data.name })
      })
    },
    handleTreeSubmit () {
      this.treeVisible = false
    },
    handleTreeCancel () {
      this.treeVisible = false
    }
  }
}
</script>
