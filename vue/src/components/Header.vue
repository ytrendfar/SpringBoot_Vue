<template>
  <el-header style="display: flex; font-size: 20px;width: 100%">
    <div style="flex: 1; font-size: 25px;">
          <span :class="isCollapse?'el-icon-s-unfold':'el-icon-s-fold'" style="cursor: pointer"
                @click="changeIcon"></span>
    </div>
    <el-breadcrumb separator-class="el-icon-d-arrow-right" style="flex: 20; line-height: 60px; height: 100%">
      <el-breadcrumb-item :to="{name:'Home'}" v-if="$store.state.currentPath !== '首页'">首页</el-breadcrumb-item>
      <el-breadcrumb-item><b>{{$store.state.currentPath}}</b></el-breadcrumb-item>
    </el-breadcrumb>
    <img :src="user.avatar" alt="" style="width: 40px;height: 40px;border-radius: 50%;position: relative;top: 10px;right: 10px">
    <span>{{user.nickname}}</span>
    <el-dropdown trigger="click">
      <span class="el-icon-arrow-down" style="margin-left: 15px;cursor: pointer;font-size: 18px"></span>
      <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
        <el-dropdown-item style="font-size: 15px;padding: 5px 0">个人中心</el-dropdown-item>
        <el-dropdown-item class="exitLogin" style="font-size: 15px;padding: 5px 0">
          <span @click="logout">退出登录</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-header>
</template>

<script>

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Header",
  data(){
    return {
      //如果内存中已保存了就将保存的json转为对象，如果没有保存就返回空对象
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {nickname: '未登录'}
    }
  },
  methods: {
    changeIcon() {
      this.$store.commit('REVERSE')
    },
    logout(){
      this.$router.push('/login')
      localStorage.removeItem('user')
      this.$message({
        showClose: true,
        message: '账号已退出',
        type: 'warning'
      })
    }
  },
  computed: {
    isCollapse() {
      return this.$store.state.isCollapse
    },
  },
}
</script>

<style>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-breadcrumb__separator {
  color: #333333 !important;
}
.exitLogin:hover *{
  color: #409EFF !important;
}

</style>