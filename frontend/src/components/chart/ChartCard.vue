<template>
  <a-card :loading="loading" :body-style="{ padding: '20px 24px 8px' }" :bordered="false">
      <a-row :gutter="16">
        <a-col :span="8">
          <div class="chart-card-content">
            <div class="content-fix">
              <slot name="icon"></slot>
            </div>
          </div>
        </a-col>
        <a-col :span="2"></a-col>
        <a-col :span="14">
          <div class="meta">
            <span class="chart-card-title">{{ title }}</span>
          </div>
          <div><span class="total">{{ total }}</span><span class="unit">{{ unit }}</span></div>
          <slot></slot>
        </a-col>
      </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'ChartCard',
  props: {
    title: {
      type: String,
      default: ''
    },
    total: {
      type: String,
      default: ''
    },
    unit: {
      type: String,
      default: ''
    },
    loading: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style lang="less" scoped>
  
  .chart-card-header {
    position: relative;
    overflow: hidden;
    width: 100%;

    .meta {
      position: relative;
      overflow: hidden;
      width: 100%;
      color: rgba(0, 0, 0, .45);
      font-size: 14px;
      line-height: 22px;
    }
  }

  .chart-card-content {
    margin-top: 5px;
    position: relative;
    height: 46px;
    width: 100%;

    .content-fix {
      position: absolute;
      left: 0;
      bottom: 0;
      width: 100%;
    }
  }

  .total {
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    white-space: nowrap;
    color: #000;
    margin-top: 4px;
    margin-bottom: 0;
    font-size: 20px;
    line-height: 30px;
    height: 38px;
  }
  .unit {
    font-size: 10px;
    line-height: 10px;
    height: 10px;
    margin-left: 10px;
  }
</style>
