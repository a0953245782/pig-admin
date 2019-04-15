<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart
      height="128"
      :data="data"
      :forceFit="true"
      :padding="['auto', 'auto', '20', '20']">
      <v-tooltip />
      <v-axis />
      <v-bar position="x*y" color="x"/>
    </v-chart>
  </div>
</template>

<script>
const data = [
  {
    x: '插枪未充电',
    y: 20
  },
  {
    x: '充完未拔枪',
    y: 50
  },
  {
    x: '充电',
    y: 20
  },
  {
    x: '维护',
    y: 30
  },
  {
    x: '空闲',
    y: 10
  },
  {
    x: '离线',
    y: 10
  }
]

const tooltip = [
  'x*y',
  (x, y) => ({
    name: x,
    value: y
  })
]
const scale = [{
  dataKey: 'x',
  min: 2
}, {
  dataKey: 'y',
  title: '时间',
  min: 1,
  max: 22
}]

export default {
  name: 'Bar',
  props: {
    title: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      data,
      scale,
      tooltip
    }
  }
}
</script>
