<template>
  <a-card
    style="width:100%"
    :bordered="false"
  >
      <a-row :gutter="10">
        <a-col :span="10">
          <v-chart height="350" width="400" :data="memorydata" :scale="memoryscale">
            <v-tooltip />
            <v-axis />
            <v-line position="time*memory" shape="smooth"></v-line>
          </v-chart>
        </a-col>
        <a-col :span="10">
          <v-chart height="350" width="400" :data="sizedata" :scale="sizescale">
            <v-tooltip />
            <v-axis />
            <v-line position="time*size" shape="smooth" color="#ff7f0e"></v-line>
          </v-chart>
        </a-col>
      </a-row>
      <a-row>
        <a-divider orientation="left">Redis详细信息</a-divider>
        <div class="redis-info">
          <table style="border-bottom: 1px solid #f1f1f1;">
            <tr v-for="(info, index) in redisInfo" :key="index" style="border-top: 1px solid #f1f1f1;">
              <td style="padding: .7rem 1rem">{{info.key}}</td>
              <td style="padding: .7rem 1rem">{{info.description}}</td>
              <td style="padding: .7rem 1rem">{{info.value}}</td>
            </tr>
          </table>
        </div>
      </a-row>
  </a-card>
</template>
<script>
import { axios } from '@/utils/request'

const memoryscale = [
    {
        dataKey: 'time',
        alias: '时间',
        type: 'time',
        mask: 'HH:MM:ss',
        tickCount: 20,
        nice: false 
    },
    {
        dataKey: 'memory',
        alias: '内存实时占用(kb)'
    }
]
const sizescale = [
    {
        dataKey: 'time',
        alias: '时间',
        type: 'time',
        mask: 'HH:MM:ss',
        tickCount: 20,
        nice: false 
    },
    {
        dataKey: 'size',
        alias: '实时数量(个)'
    }
]


export default {
  data () {
    return {
      loading: true,
      memorydata: [0],
      sizedata: [0],
      memoryscale,
      sizescale,
      memory: {
        series: [],
        chartOptions: {
          chart: {
            animations: {
              enabled: true,
              easing: 'linear',
              dynamicAnimation: {
                speed: 3000
              }
            },
            toolbar: {
              show: false
            },
            zoom: {
              enabled: false
            }
          },
          dataLabels: {
            enabled: false
          },
          stroke: {
            curve: 'smooth'
          },
          title: {
            text: 'Redis内存实时占用情况（kb）',
            align: 'left'
          },
          markers: {
            size: 0
          },
          xaxis: {
          },
          yaxis: {},
          legend: {
            show: false
          }
        },
        data: [],
        xdata: []
      },
      key: {
        series: [],
        chartOptions: {
          chart: {
            animations: {
              enabled: true,
              easing: 'linear',
              dynamicAnimation: {
                speed: 3000
              }
            },
            toolbar: {
              show: false
            },
            zoom: {
              enabled: false
            }
          },
          dataLabels: {
            enabled: false
          },
          colors: ['#f5564e'],
          stroke: {
            curve: 'smooth'
          },
          title: {
            text: 'Redis key实时数量（个）',
            align: 'left'
          },
          markers: {
            size: 0
          },
          xaxis: {
          },
          yaxis: {},
          legend: {
            show: false
          }
        },
        data: [],
        xdata: []
      },
      redisInfo: [],
      timer: null
    }
  },
  beforeDestroy () {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  mounted () {
    
    this.timer = setInterval(this.updateData, 3000)
    
    axios({url: 'redis/info'}).then((r) => {
      this.redisInfo = r.data
    })
  },
  methods: {
    updateData(){
      let minMemory = 1e10
      let minSize = 1e10
      let maxMemory = -1e10
      let maxSize = -1e10 
      if (this.$route.path.indexOf('redis') !== -1) {
        Promise.all([
          axios({url: 'redis/keysSize'}),
          axios({url: 'redis/memoryInfo'})
        ]).then((r) => {
          let currentMemory = r[1].data.used_memory / 1000
          let currentSize = r[0].data.dbSize
          if (currentMemory < minMemory) {
            minMemory = currentMemory
          }
          if (currentMemory > maxMemory) {
            maxMemory = currentMemory
          }
          if (currentSize < minSize) {
            minSize = currentSize
          }
          if (currentSize > maxSize) {
            maxSize = currentSize
          }
          const now = new Date();
          const time = now.getTime();
          this.memory.data.push(
            {
              time: time,
              memory: currentMemory
            })
          
          this.key.data.push({
              time: time,
              size: currentSize
            })
          
          if (this.memory.data.length >= 6) {
            this.memory.data.shift()
          }
          if (this.key.data.length >= 6) {
            this.key.data.shift()
          }
          this.memorydata = this.memory.data.slice()
          this.sizedata = this.key.data.slice()
          
          if (this.loading) {
            this.loading = false
          }
        }).catch((r) => {
          console.error(r)
          this.$message.error('获取Redis信息失败')
          if (this.timer) {
            clearInterval(this.timer)
          }
        })
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .redis-info {
    width: 100%;
    table {
      width: 100%;
      tr {
        line-height: 1.5rem;
        border-bottom: 1px solid #f1f1f1;
        th {
          background: #fafafa;
          padding: .5rem;
        }
        td {
          padding: .5rem;
          .ant-tag {
            font-size: .8rem !important;
          }
        }
      }
    }
  }
</style>

