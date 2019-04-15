<template>
  <a-modal
    class="update-icon"
    title="选择图标"
    @cancel="handleCancel"
    :width="710"
    :footer="null"
    v-model="show">
    <a-tabs defaultActiveKey="1" class="icon-tabs">
      <a-tab-pane tab="阿里云图标" key="1">
        <template v-for="(icon, index) in pmicons">
          <div class="icon-wrapper" :key="index">
            <a-icon :type="icon" :style="{ fontSize: '36px', width: '4rem', cursor: 'pointer' }" @click="change(icon)" />
          </div>
        </template>
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>
<script>
import { mapState } from 'vuex'
import { axios } from '@/utils/request'

const pmicons = ['bank', 'trophy', 'contacts', 'global', 'shake', 'dashboard', 'audit', 'fire', 'money-collect', 'property-safety', 'read', 'reconciliation', 'rest', 'security-scan', 'insurance', 'interation', 'safety-certificate', 'project', 'thunderbolt', 'block', 'cluster', 'deployment-unit','gold', 'robot']

export default {
  props: {
    updateIconModelVisible: {
      default: false
    },
    user: {
      required: false
    }
  },
  data () {
    return {
      pmicons,
      updating: false
    }
  },
  computed: {
    show: {
      get: function () {
        return this.updateIconModelVisible
      },
      set: function () {
      }
    }
  },
  methods: {
    handleCancel () {
      this.$emit('iconcancel')
    },
    change (icon) {
      if (this.updating) {
        this.$message.warning('更换图标中，请勿重复点击')
        return
      }
      this.updating = true
      this.$emit('iconsuccess', icon)
      this.updating = false
    }
  }
}
</script>
<style lang="less">
  .update-icon {
    .ant-modal-body {
      padding: 0 1rem 1rem 1rem!important;
      .icon-tabs {
        .icon-wrapper {
          display: inline-block;
        }
      }
    }
  }
</style>
