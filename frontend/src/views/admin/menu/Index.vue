<template>
  <a-card :bordered="false">
    <div class="table-operator">
      <a-button type="primary" v-if="permissions.indexOf('sys_menu_add') !== -1" @click="handleNew">新建</a-button>
      <a-button type="primary" v-if="permissions.indexOf('sys_menu_edit') !== -1" @click="handleEdit">编辑</a-button>
      <a-button type="primary" v-if="permissions.indexOf('sys_menu_del') !== -1" @click="handleDel">删除</a-button>
    </div>
    <a-row :gutter="8">
      <a-col :span="7">
        <a-tree
          ref="tree"
          :autoExpandParent="true"
          :treeData="orgTree"
          title="label"
          @select="onSelect"
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
            <a-input disabled="disabled" placeholder="请输入父级节点" v-decorator="[ 'parentId' ]"/>
          </a-form-item>
          <a-form-item
            label="节点ID"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            >
            <a-input-number :min="1" :disabled="disabled" placeholder="节点ID" v-decorator="[ 'menuId' ]" />
          </a-form-item>
          <a-form-item
            label="标题"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            >
            <a-input :disabled="disabled" placeholder="请输入标题" v-decorator="[ 'name', {rules: [{ required: true, message: '请输入标题' }]}]" />
          </a-form-item>
          <a-form-item
            label="类型"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            >
            <a-input-group compact>
              <a-select :disabled="disabled" style="width:20%;" v-decorator="[ 'type', {rules: [{ required: true, message: '请输入类型' }]}]">
                <a-select-option value="0">菜单</a-select-option>
                <a-select-option value="1">按钮</a-select-option>
              </a-select>
            </a-input-group>
          </a-form-item>
          <a-form-item
            label="权限标识"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            v-if="this.form.getFieldValue('type') === '1'"
            >
            <a-input :disabled="disabled" placeholder="请输入权限标识" v-decorator="[ 'permission' ]" />
          </a-form-item>
          <a-form-item
             label="图标"
             :labelCol="{span: 4}"
             :wrapperCol="{span: 18, offset: 1}"
             v-if="this.form.getFieldValue('type') === '0'"
             >
            <a-input :addonBefore="addonBefore" readOnly placeholder="请输入图标" v-decorator="[ 'icon' ]" @click="showIcon">
            </a-input>
          </a-form-item>
          <a-form-item
            label="排序"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            v-if="this.form.getFieldValue('type') === '0'"
            >
            <a-input :disabled="disabled" placeholder="请输入排序" v-decorator="[ 'sort' ]"/>
          </a-form-item>
          <a-form-item
            label="前端组件"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            v-if="this.form.getFieldValue('type') === '0'"
            >
            <a-input :disabled="disabled" placeholder="请输入前端组件" v-decorator="[ 'component' ]"/>
          </a-form-item>
          <a-form-item
            label="前端地址"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            v-if="this.form.getFieldValue('type') === '0'"
            >
            <a-input :disabled="disabled" placeholder="iframe嵌套地址" v-decorator="[ 'path' ]"/>
          </a-form-item>
          <a-form-item
            label="路由缓冲"
            :labelCol="{span: 4}"
            :wrapperCol="{span: 18, offset: 1}"
            v-if="this.form.getFieldValue('type') === '0'">
            <a-switch defaultChecked :disabled="disabled" v-decorator="[ 'keepAlive' ]"/>
          </a-form-item>
          <a-form-item v-if="formStatus === 'create'">
            <div class="table-operator">
              <a-button type="primary"
                html-type="submit"
                :loading="state.loginBtn"
                :disabled="state.loginBtn">保存</a-button>
              <a-button @click="cancel">取消</a-button>
            </div>
          </a-form-item>
          <a-form-item v-if="formStatus === 'edit'">
            <div class="table-operator">
              <a-button type="primary"
                html-type="submit"
                :loading="state.loginBtn"
                :disabled="state.loginBtn">更新</a-button>
              <a-button @click="cancel">取消</a-button>
            </div>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
    <update-icon
      @iconcancel="handleUpdateIconCancel"
      @iconsuccess="handleUpdateIconSuccess"
      :updateIconModelVisible="updateIconModelVisible">
    </update-icon>
  </a-card>
</template>

<script>
import moment from 'moment'
import { getMenuTree } from '@/api/sysmgr'
import { mapGetters } from 'vuex'
import { axios } from '@/utils/request'
import UpdateIcon from './UpdateIcon'

export default {
  components: {
    UpdateIcon
  },
  data () {
    return {
      updateIconModelVisible: false,
      orgTree: [],
      formStatus: '',
      disabled: true,
      currentId: -1,
      type: 'setting',
      form: this.$form.createForm(this),
      form1: {
        permission: undefined,
        name: undefined,
        menuId: undefined,
        parentId: this.currentId,
        icon: undefined,
        sort: undefined,
        component: undefined,
        type: undefined,
        path: undefined
      },
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
      getMenuTree().then(res => {
        this.orgTree = res.data
      })
    },
    addonBefore() {
      return (<a-icon type="setting" style="width: 30px"/>)
    },
    handleNew () {
      this.disabled = false
      this.formStatus = 'create'
      this.form.resetFields()
      this.resetForm()
      this.$nextTick(() => {
        this.form.setFieldsValue(this.form1)
      })
    },
    handleEdit () {
      if (this.currentId === -1){
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
      if (this.currentId === -1){
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
            url: '/menu/' + current.currentId,
            method: 'delete'
          }).then(() => {
            current.$notification.success({
              message: '删除成功'
            })
            current.currentId = -1
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
          // console.log(params)
          if (this.formStatus === 'edit') {
            method = 'put'
            msg = '更新'
          }
          state.loginBtn = false
          axios({
            url: '/menu',
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
        url: '/menu/' + this.currentId,
        method: 'get'
      }).then((res) => {
          this.form.resetFields()
          this.resetForm()
          this.addonBefore = () => {
            return (<a-icon type={res.data.icon} style="width: 30px"/>)
          }
          this.form.setFieldsValue(res.data)
          this.$nextTick(() => {
          this.form.setFieldsValue({'icon': res.data.icon})
        })
      })
    },
    cancel () {
      this.disabled = true
      this.formStatus = ''
      if (this.currentId !== -1){
        this.onSelect([this.currentId])
      }
    },
    resetForm() {
      this.form1 = {
        permission: undefined,
        name: undefined,
        menuId: undefined,
        parentId: this.currentId,
        icon: undefined,
        sort: undefined,
        component: undefined,
        type: undefined,
        path: undefined
      }
    },
    showIcon () {
      this.updateIconModelVisible = true
    },
    handleUpdateIconCancel () {
      this.updateIconModelVisible = false
    },
    handleUpdateIconSuccess (icon) {
      this.addonBefore = () => {
        return (<a-icon type={icon} style="width: 30px"/>)
      }
      this.form.setFieldsValue({'icon': icon})
      this.updateIconModelVisible = false
    }
  }
}
</script>
