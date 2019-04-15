<template>
  <div>
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="类型">
              <a-select v-model="queryParam.type" placeholder="请选择" default-value="0" :allowClear="allowClear">
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="9">异常</a-select-option>
              </a-select>
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
          <a @click="handleEdit(record)" v-if="permissions.indexOf('sys_log_edit') !== -1"><a-icon slot="prefix" type="eye" /></a>
          <a-divider type="vertical" />
          <a-popconfirm title="确认删除?" @confirm="confirm(record)" @cancel="cancel" okText="是" cancelText="否">
            <a v-if="permissions.indexOf('sys_log_del') !== -1"><a-icon slot="prefix" type="delete" /></a>
          </a-popconfirm>
        </template>
      </span>
    </s-table>

    <a-modal
      title="查看"
      :width="800"
      v-model="visible"
      :footer="footer"
    >
      <a-form>
        <a-row>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="类型"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 1}"
             >
              <a-select v-model="mdl.type" disabled="disabled" >
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="9">异常</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="标题"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input v-model="mdl.title" disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="ip地址"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input v-model="mdl.remoteAddr" disabled="disabled" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="请求方式"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input v-model="mdl.method" disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="请求时间"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input v-model="mdl.time" disabled="disabled" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24" >
            <a-form-item
              label="创建时间"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input v-model="mdl.createTime" disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
       
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import moment from 'moment'
import STable from '@/components/table/'
import { getLogList } from '@/api/sysmgr'
import { mapGetters } from 'vuex'
import { axios } from '@/utils/request'
  
export default {
  name: 'TableList',
  components: {
    STable
  },
  data () {
    return {
      mdl: {},
      footer: [<a-button key='back' onClick={this.handleCancel}>取消</a-button>],
      visible: false,
      allowClear: true,
      queryParam: {},
      columns: [
        {
          title: '#',
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '类型',
          dataIndex: 'type',
          width: '10%',
          customRender: (text) => text == 0 ? '正常' : '异常'
        },
        {
          title: '标题',
          width: '18%',
          dataIndex: 'title'
        },
        {
          title: 'ip地址',
          dataIndex: 'remoteAddr'
        },
        {
          title: '请求方式',
          dataIndex: 'method',
          width: '12%'
        },
        {
          title: '请求时间',
          dataIndex: 'time',
          width: '12%',
          customRender: (text, row, index) => {
            if (text < 500) {
              return <a-tag color="green">{text} ms</a-tag>
            } else if (text < 1000) {
              return <a-tag color="cyan">{text} ms</a-tag>
            } else if (text < 1500) {
              return <a-tag color="orange">{text} ms</a-tag>
            } else {
              return <a-tag color="red">{text} ms</a-tag>
            }
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
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        // console.log('loadData.parameter', parameter)
        return getLogList(Object.assign({
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
    handleEdit (record) {
      this.visible = true
      this.mdl = record
      this.mdl['createTime'] = moment(record.createTime).format('YYYY-MM-DD HH:mm:ss')  
    },
    handleDel (record) {
      this.$emit('onDel', record)
    },
    handleCancel () {
      this.visible = false
    },
    confirm (record) {
      this.$emit('onDel', record)

      axios({
        url: '/log/' + record.id,
        method: 'delete'
      }).then((res) => {
        this.$notification.success({
          message: '删除成功'
        })
        this.$refs.table.refresh(true)
      }).catch(err => {
        this.$notification.error({
          message: '删除失败'
        })
      })
    },
    cancel (e) {
    },
    resetSearchForm () {
      this.queryParam = {
        date: moment(new Date())
      }
    }
  }
}
</script>
