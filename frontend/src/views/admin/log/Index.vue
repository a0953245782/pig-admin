<template>
  <a-card :bordered="false">
    <component @onEdit="handleEdit" @onDel="handleDel" :record="record" :is="currentComponet"></component>
  </a-card>
</template>
<script>
import ATextarea from 'ant-design-vue/es/input/TextArea'
import AInput from 'ant-design-vue/es/input/Input'
import List from '@/views/admin/log/List'

export default {
  name: 'TableListWrapper',
  components: {
    AInput,
    ATextarea,
    List
  },
  data () {
    return {
      currentComponet: 'List',
      record: ''
    }
  },
  methods: {
    handleEdit (record) {
    },
    handleDel (record) {
    }
  },
  watch: {
    '$route.path' () {
      this.record = ''
      this.currentComponet = 'List'
    }
  }
}
</script>
