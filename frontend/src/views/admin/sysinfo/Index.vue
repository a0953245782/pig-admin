<template>
  <div class="page-header-index-wide page-header-wrapper-grid-content-main">
    <a-row :gutter="24">
      <a-col :md="24" :lg="24">
        <a-card
          style="width:100%"
          :bordered="false"
          :tabList="tabListNoTitle"
          :activeTabKey="noTitleKey"
          @tabChange="key => handleTabChange(key, 'noTitleKey')"
        >
          <jvm-page v-if="noTitleKey === 'jvm'"></jvm-page>
          <tomcat-page v-else-if="noTitleKey === 'tomcat'"></tomcat-page>
          <server-page v-else-if="noTitleKey === 'server'"></server-page>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import { JvmPage, TomcatPage, ServerPage } from './page'

export default {
  components: {
    JvmPage,
    TomcatPage,
    ServerPage
  },
  data () {
    return {
      tabListNoTitle: [
        {
          key: 'jvm',
          tab: 'JVM'
        },
        {
          key: 'tomcat',
          tab: 'Tomcat'
        },
        {
          key: 'server',
          tab: 'Server'
        }
      ],
      noTitleKey: 'jvm'
    }
  },
  create(){
    console.log(JvmPage)
  },
  methods: {
    handleTabChange (key, type) {
      this[type] = key
    }
  }
}
</script>
<style lang="less" scoped>
.page-header-wrapper-grid-content-main {
  width: 100%;
  height: 100%;
  min-height: 100%;
  transition: 0.3s;



}
</style>

