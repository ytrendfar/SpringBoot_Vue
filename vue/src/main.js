import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import router from "@/router"
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/global.css'
import request from "@/utils/request"
import store from './store'

Vue.config.productionTip = false
Vue.use(VueRouter)
Vue.use(ElementUI,{size:"small"})
//设置配置后的axios
Vue.prototype.request = request

new Vue({
  render: h => h(App),
  router,
  store,
  //安装事件总线
  beforeCreate() {
    Vue.prototype.$bus = this
  }
}).$mount('#app')
