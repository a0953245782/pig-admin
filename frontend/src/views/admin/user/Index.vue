<template>
  <a-card :bordered="false">
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleNew" v-if="permissions.indexOf('sys_user_add') !== -1">新建</a-button>
    </div>
    <a-row :gutter="8">
    
      <a-col :span="7">
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
      </a-col>
      
      <a-col :span="17">
        <s-table
          ref="table"
          size="default"
          rowKey="userId"
          :columns="columns"
          :data="loadData">
          <span slot="serial" slot-scope="text, record, index">
            {{ index + 1 + (($refs.table.localPagination.current - 1) * $refs.table.localPagination.pageSize) }}
          </span>
          <span slot="action" slot-scope="text, record">
            <template>
              <a @click="handleEdit(record)" v-if="permissions.indexOf('sys_user_edit') !== -1"><a-icon slot="prefix" type="edit" /></a>
              <a-divider type="vertical" />
              <a-popconfirm title="确认删除?" @confirm="confirm(record)" okText="是" cancelText="否">
                <a v-if="permissions.indexOf('sys_user_del') !== -1"><a-icon slot="prefix" type="delete" /></a>
              </a-popconfirm>
            </template>
          </span>
        </s-table>
        
        <create-form ref="createModal" @ok="handleOk" :record="record" />
        
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import moment from 'moment'
import STable from '@/components/table/'
import { getUserList, getDeptTree } from '@/api/sysmgr'
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
      visible: false,
      queryParam: {},
      orgTree: [],
      searchValue: '',
      expandedKeys: [],
      dataList: [],
      autoExpandParent: true,
      treeId: '',
      columns: [
        {
          title: '#',
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '用户名',
          dataIndex: 'username'
        },
        {
          title: '手机号',
          dataIndex: 'phone'
        },
        {
          title: '角色',
          dataIndex: 'roleList',
          customRender: (text, row, index) => {
            const roleName = text[0].roleName
            return <a-tag color='blue'>{roleName}</a-tag>
          }
        },
        {
          title: '状态',
          dataIndex: 'lockFlag',
          customRender: (text, row, index) => {
            return text === '0' ? <a-tag color='blue'>有效</a-tag> : <a-tag color='orange'>锁定</a-tag>
          }
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
      loadData: parameter => {
        return getUserList(Object.assign({
              current: parameter.pageNo,
              size: parameter.pageSize,
              deptId: this.treeId[0]
            }, this.queryParam))
            .then(res => {
              return res
            })
      }
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
        this.generateList(res.data)
        this.orgTree = res.data
      })
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
    handleNew () {
      this.record = {}
      this.$refs.createModal.add()
    },
    handleEdit (record) {
      this.record = record
      this.$refs.createModal.edit()
    },
    handleOk () {
      this.$refs.table.refresh(true)
    },
    confirm (record) {
      axios({
        url: '/user/' + record.userId,
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
    handleCancel () {
      this.visible = false
    },
    resetSearchForm () {
      this.queryParam = {
        date: moment(new Date())
      }
    },
    onSelect (treeId) {
      this.treeId = treeId
      this.$refs.table.refresh(true)
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
        autoExpandParent: true,
      })
    }
  }
}
</script>
