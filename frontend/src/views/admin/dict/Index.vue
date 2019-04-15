<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="值">
              <a-input v-model="queryParam.valuee" placeholder="值">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => queryParam = {}">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleNew" v-if="permissions.indexOf('sys_dict_add') !== -1">新建</a-button>
    </div>

    <s-table
      ref="table"
      size="default"
      :columns="columns"
      :data="loadData"
    >
      <span slot="serial" slot-scope="text, record, index">
        {{ index + 1 + (($refs.table.localPagination.current - 1) * $refs.table.localPagination.pageSize) }}
      </span>
      <span slot="action" slot-scope="text, record">
        <template>
          <a @click="handleEdit(record)" v-if="permissions.indexOf('sys_dict_edit') !== -1"><a-icon slot="prefix" type="edit" /></a>
          <a-divider type="vertical" />
          <a-popconfirm title="确认删除?" @confirm="confirm(record)" okText="是" cancelText="否">
            <a v-if="permissions.indexOf('sys_dict_del') !== -1"><a-icon slot="prefix" type="delete" /></a>
          </a-popconfirm>
        </template>
      </span>
    </s-table>

    <create-form ref="createModal" @ok="handleOk" :record="record" />

  </a-card>
</template>

<script>
import moment from 'moment'
import STable from '@/components/table/'
import { getDictList } from '@/api/sysmgr'
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
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        // console.log('loadData.parameter', parameter)
        if(!this.queryParam.valuee){
          this.queryParam = {}
        }
        return getDictList(Object.assign({
            descs: 'create_time',
            current: parameter.pageNo,
            size: parameter.pageSize
            }, this.queryParam))
          .then(res => {
            return res
          })
      }
    }
  },
  computed: {
    ...mapGetters(['permissions'])
  },
  methods: {
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
        url: '/dict/' + record.id + '/' + record.valuee,
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
    }
  }
}
</script>
