<template>
  <el-header style="display: flex; font-size: 20px;width: 100%">
    <div style="flex: 1; font-size: 25px;">
          <span :class="isCollapse?'el-icon-s-unfold':'el-icon-s-fold'" style="cursor: pointer"
                @click="changeIcon"></span>
    </div>
    <el-breadcrumb separator-class="el-icon-d-arrow-right" style="flex: 10; line-height: 60px; height: 100%">
      <el-breadcrumb-item :to="{name:'Home'}" v-if="$store.state.currentPath !== '首页'">首页</el-breadcrumb-item>
      <el-breadcrumb-item><b>{{$store.state.currentPath}}</b></el-breadcrumb-item>
    </el-breadcrumb>
    <span>Young</span>
    <el-dropdown trigger="click">
      <span class="el-icon-arrow-down" style="margin-left: 15px;cursor: pointer;font-size: 18px"></span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>个人中心</el-dropdown-item>
        <el-dropdown-item>
          <router-link style="text-decoration: none;color: #333333" to="/login">退出登录</router-link>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-header>
</template>

<script>

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Header",
  methods: {
    changeIcon() {
      this.$store.commit('REVERSE')
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
</style>