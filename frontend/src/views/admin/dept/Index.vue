<template>
  <a-card :bordered="false">
    <div class="table-operator">
      <a-button type="primary" v-if="permissions.indexOf('sys_dept_add') !== -1" @click="handleNew">新建</a-button>
      <a-button type="primary" v-if="permissions.indexOf('sys_dept_edit') !== -1" @click="handleEdit">编辑</a-button>
      <a-button type="primary" v-if="permissions.indexOf('sys_dept_del') !== -1" @click="handleDel">删除</a-button>
    </div>
    <a-row :gutter="8">
      <a-col :span="7">
        <a-tree
          ref="tree"
          :defaultExpandAll="true"
          :autoExpandParent="true"
          :treeData="orgTree"
          @select="onSelect"
          v-if="orgTree.length > 0"
        />
      </a-col>
     
      <a-col :span="17">
        <a-form
          :form="form"
          @submit="handleSubmit">
          <a-form-item
            label="父级节点"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            >
            <a-input :disabled="disabled" placeholder="请输入父级节点" v-decorator="[ 'parentId', {rules: [{ required: true, message: '请输入父级节点' }]}]"/>
          </a-form-item>
          <a-form-item
            label="节点编号"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            v-if="formStatus === ''"
            >
            <a-input :disabled="disabled" placeholder="请输入节点编号" v-decorator="[ 'deptId' ]"/>
          </a-form-item>
          <a-form-item
            label="部门名称"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            >
            <a-input :disabled="disabled" placeholder="请输入部门名称" v-decorator="[ 'name', {rules: [{ required: true, message: '请输入部门名称' }]}]"/>
          </a-form-item>
          <a-form-item
            label="排序"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            >
            <a-input :disabled="disabled" placeholder="请输入排序" v-decorator="[ 'sort' ]"/>
          </a-form-item>
          <a-form-item v-if="formStatus !== ''">
	          <div class="table-operator">
              <a-button type="primary"
                html-type="submit"
                :loading="state.loginBtn"
                :disabled="state.loginBtn">提交</a-button>
                <a-button @click="cancel">取消</a-button>
            </div>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import moment from 'moment'
import { getDeptTree } from '@/api/sysmgr'
import { mapGetters } from 'vuex'
import { axios } from '@/utils/request'

  
export default {
  name: 'TableList',
  data () {
    return {
      orgTree: [],
      form: this.$form.createForm(this),
      formStatus: '',
      disabled: true,
      currentId: 0,
      state: {
        time: 60,
        loginBtn: false
      },
      columns: [
        {
          title: '#',
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '键',
          dataIndex: 'keyy'
        },
        {
          title: '值',
          dataIndex: 'valuee',
          width: '15%'
        },
        {
          title: '表名',
          dataIndex: 'tableName',
          width: '20%'
        },
        {
          title: '字段',
          dataIndex: 'fieldName',
          width: '20%'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          width: '20%',
          customRender: (text, row, index) => {
            return moment(text).format('YYYY-MM-DD HH:mm:ss')  
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          scopedSlots: { customRender: 'action' }
        }
      ]
    }
  },
  created () {
    this.reloadTree()
  },
  computed: {
    ...mapGetters(['permissions'])
  },
  methods: {
    reloadTree () {
      getDeptTree().then(res => {
        this.orgTree = res.data
      })
    },
    handleNew () {
      this.disabled = false
      this.formStatus = 'create'
      this.form.resetFields()
      this.$nextTick(() => {
        this.form.setFieldsValue({'parentId': (this.currentId + '') })
      })
    },
    handleEdit () {
      if (this.currentId === 0){
        this.$notification.error({
          message: '请选择一个节点'
        })
        return
      }
      this.formStatus = 'edit'
      this.disabled = false
      this.onSelect([this.currentId])
    },
    handleDel () {
      if (this.currentId === 0){
        this.$notification.error({
          message: '请选择一个节点'
        })
        return
      }
      const current = this
      this.$confirm({
        title: '提示',
        content: '真的要删除节点吗 ?',
        onOk () {
          axios({
            url: '/dept/' + current.currentId,
            method: 'delete'
          }).then(() => {
            current.$notification.success({
              message: '删除成功'
            })
            current.currentId = 0
            current.form.resetFields()
            current.reloadTree()
          }).catch(() => {
            current.$notification.error({
              message: '删除失败'
            })
          })
        }
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      const {
        form: { validateFields },
        state
      } = this
      state.loginBtn = true
      let method = 'post'
      let msg = '新增'
      this.form.validateFields((err, values) => {
        if (!err) {
          const params = { ...values }
          if (this.formStatus === 'edit') {
            method = 'put'
            msg = '修改'
            params.deptId = this.currentId
          }
          state.loginBtn = false
          axios({
            url: '/dept',
            method: method,
            data: params
          }).then(() => {
            this.reloadTree()
            this.$notification.success({
              message: msg + '成功'
            })
            this.cancel()
          }).catch(() => {
            this.$notification.error({
              message: msg + '失败'
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
    onSelect (treeId) {
      if(!treeId.length){
        return
      }
      this.currentId = treeId[0]
      if (this.formStatus === 'create'){
        this.form.setFieldsValue({'parentId': (this.currentId + '')})
        return
      }
      axios({
        url: '/dept/' + this.currentId,
        method: 'get'
      }).then(res => {
        this.form.resetFields()
        this.$nextTick(() => {
          this.form.setFieldsValue(res.data)
        })
      })
    },
    cancel () {
      this.disabled = true
      this.formStatus = ''
      if (this.currentId !== 0){
        this.onSelect([this.currentId])
      }
    }
  }
}
</script>
