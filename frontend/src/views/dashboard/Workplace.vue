<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总充电次数" :total="3000 | NumberFormat" unit="次">
          <mini-bar slot="icon"/>
          <div>
            <trend flag="down">
              <span slot="term">10%</span>
            </trend>
          </div>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总充电电量" :total="10356 | NumberFormat" unit="万度">
          <mini-area slot="icon"/>
          <div>
            <trend flag="up">
              <span slot="term">8%</span>
            </trend>
          </div>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总充电时长" :total="656 | NumberFormat" unit="万小时">
          <mini-bar slot="icon"/>
          <div>
            <trend flag="up">
              <span slot="term">2%</span>
            </trend>
          </div>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总充电金额" :total="4302 | NumberFormat" unit="万元">
          <mini-area slot="icon"/>
          <div>
            <trend flag="up">
              <span slot="term">5%</span>
            </trend>
          </div>
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{width: '256px'}" />
          </div>
          <a-tab-pane loading="true" tab="充电电量" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <a-line title="充电电量趋势" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="充电电量排行榜" :list="rankList"/>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="充电金额" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <a-line title="充电金额趋势" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="充电金额排行榜" :list="rankList"/>
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row :gutter="12">
      <a-col :xl="8" :lg="24" :md="24" :sm="24" :xs="24">
        <a-card :loading="loading" :bordered="false" title="最新告警信息" :style="{ marginTop: '24px' }">
          <a-dropdown :trigger="['click']" placement="bottomLeft" slot="extra">
            <a class="ant-dropdown-link" href="#">
              <a-icon type="ellipsis" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;">更多</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
          <p><a-badge status="error" text="松山湖电站 6号桩 通信失联"></a-badge></p>
          <p><a-badge status="error" text="广州南沙充电站 2号桩 通信中断"></a-badge></p>
          <p><a-badge status="warning" text="国宇充电 10号桩 通信中断"></a-badge></p>
          <p><a-badge status="warning" text="东华大厦充电站 10号桩 通信中断"></a-badge></p>
          <p><a-badge status="error" text="松山湖电站 6号桩 通信失联"></a-badge></p>
        </a-card>
      </a-col>
      <a-col :xl="16" :lg="24" :md="24" :sm="24" :xs="24">
        <a-card :loading="loading" :bordered="false" title="电枪状态统计" :style="{ marginTop: '24px' }">  
          <bar title="" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import ChartCard from '@/components/chart/ChartCard'
import ACol from 'ant-design-vue/es/grid/Col'
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import RankList from '@/components/chart/RankList'
import ALine from '@/components/chart/ALine'
import Bar from '@/components/chart/Bar'
import Trend from '@/components/Trend'

const rankList = []
for (let i = 0; i < 7; i++) {
  rankList.push({
    name:  '松山湖' + (i + 1) + '号电站',
    total: 1234.56 - i * 100
  })
}

export default {
  name: 'Workplace',
  components: {
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    RankList,
    Bar,
    ALine,
    Trend
  }, 
  data () {
    return {
      loading: true,
      rankList
    }
  },
  created () {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
  }
}
</script>
<style lang="less" scoped>
 
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }
</style>