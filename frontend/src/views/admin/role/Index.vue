<template>
  <a-card :bordered="false">
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleNew" v-if="permissions.indexOf('sys_role_add') !== -1">新建</a-button>
    </div>
    <s-table
      ref="table"
      size="default"
      :columns="columns"
      rowKey="roleId"
      :data="loadData"
    >
      <span slot="serial" slot-scope="text, record, index">
        {{ index + 1 + (($refs.table.localPagination.current - 1) * $refs.table.localPagination.pageSize) }}
      </span>
      <span slot="action" slot-scope="text, record">
        <template>
          <a @click="handleView(record)"><a-icon slot="prefix" type="eye" /></a>
          <a-divider type="vertical" v-if="permissions.indexOf('sys_role_edit') !== -1" />
          <a @click="handleEdit(record)" v-if="permissions.indexOf('sys_role_edit') !== -1"><a-icon slot="prefix" type="edit" /></a>
          <a-divider type="vertical" v-if="permissions.indexOf('sys_role_del') !== -1"/>
          <a-popconfirm v-if="permissions.indexOf('sys_role_del') !== -1" title="确认删除?" @confirm="confirm(record)" okText="是" cancelText="否">
            <a><a-icon slot="prefix" type="delete" /></a>
          </a-popconfirm>
          <a-divider type="vertical" v-if="permissions.indexOf('sys_role_perm') !== -1"/>
          <a @click="handlePerm(record)" v-if="permissions.indexOf('sys_role_perm') !== -1"><a-icon slot="prefix" type="plus" /></a>
        </template>
      </span>
    </s-table>
    
    <create-form ref="createModal" @ok="handleOk" :record="record" />
    
    <a-modal
      title="分配权限"
      :width="640"
      :visible="treeVisible"
      @ok="handlePermSubmit"
      @cancel="handlePermCancel">
      <a-tree
        ref="tree"
        checkable
        :defaultExpandAll="true"
        :autoExpandParent="true"
        :treeData="orgTree"
        :checkedKeys="checkedKeys"
        @check="onCheck"
        :checkStrictly="true"
        v-if="orgTree.length > 0"
        />
    </a-modal>
  </a-card>
</template>

<script>
import moment from 'moment'
import STable from '@/components/table/'
import { getRoleList, getMenuTree } from '@/api/sysmgr'
import { mapGetters } from 'vuex'
import { axios } from '@/utils/request'
import CreateForm from './modules/CreateForm'
  
export default {
  name: 'TableList',
  components: {
    STable,
    CreateForm
  },
  data () {
    return {
      record: {},
      orgTree: [],
      visible: false,
      treeVisible: false,
      checkedKeys: [],
      columns: [
        {
          title: '#',
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '角色名称',
          dataIndex: 'roleName'
        },
        {
          title: '角色标识',
          dataIndex: 'roleCode',
          width: '15%'
        },
        {
          title: '角色描述',
          dataIndex: 'roleDesc',
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
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return getRoleList(Object.assign({
          current: parameter.pageNo,
          size: parameter.pageSize
        }))
        .then(res => {
          return res
        })
      }
    }
  },
  computed: {
    ...mapGetters(['permissions'])
  },
  created () {
    this.reloadTree()
  },
  methods: {
    reloadTree () {
      getMenuTree().then(res => {
        this.orgTree = res.data
      })
    },
    handleNew () {
      this.record = {}
      this.$refs.createModal.add()
    },
    handleEdit (record) {
      this.record = record
      this.$refs.createModal.edit()
    },
    handleView (record) {
      this.record = record
      this.$refs.createModal.view()
    },
    handlePerm (record) {
      this.record = record
      axios({
        url: '/menu/tree/' + record.roleId,
      }).then(res => {
		this.checkedKeys = res
		this.treeVisible = true      
      })
    },
    handleOk () {
      this.$refs.table.refresh(true)
    },
    confirm (record) {
      axios({
        url: '/role/' + record.roleId,
        method: 'delete'
      }).then(() => {
        this.$notification.success({
          message: '删除成功'
        })
        this.$refs.table.refresh(true)
      }).catch(() => {
        this.$notification.error({
          message: '删除失败'
        })
      })
    },
    handlePermSubmit () {
      this.treeVisible = false
      let menuIds = ''
      this.checkedKeys.forEach((item, index) => {
        menuIds += item
        if (index < (this.checkedKeys.length - 1)){
          menuIds += ','
        }
      })
      axios({
        url: '/role/menu?roleId=' + this.record.roleId + '&menuIds=' + menuIds,
        method: 'put'
      }).then(() => {
        this.$notification.success({
          message: '更新成功'
        })
      }).catch(() => {
        this.$notification.error({
          message: '更新失败'
        })
      })
    },
    handlePermCancel () {
      this.treeVisible = false
    },
    onCheck (treeId) {
      this.checkedKeys = treeId.checked
    }
  }
}
</script>
