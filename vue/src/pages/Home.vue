<template>
  <div>
    <div id="main" style="height: 500px;width: 800px"></div>
  </div>
</template>

<script>
//引入Echarts
import * as echarts from 'echarts'

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Home",
  data(){
    return{

    }
  },
  mounted() {
    //绘制图表
    let chartDom = document.getElementById('main')
    let myChart = echarts.init(chartDom)
    let option = {
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'line'
        }
      ]
    }
    myChart.setOption(option)
  }

}



</script>

<style scoped>

</style>