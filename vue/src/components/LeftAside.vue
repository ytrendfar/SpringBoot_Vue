<template>
  <el-aside :width="(isCollapse?64:200).toString()+'px'" style="height: 100%;overflow-x: hidden;">
    <el-menu :default-openeds="['1', '3']"
             style="min-height: 100%; overflow-x: hidden"
             background-color="rgb(48,65,86)"
             text-color="#DDD"
             active-text-color="#FFD04B"
             :collapse-transition="false"
             :collapse="isCollapse"
             router
    >
      <div style="height: 60px; line-height: 60px; text-align: center">
        <img src="../assets/logo.png" alt="logo" style="height: 20px;position: relative;top: 5px">
        <b style="color: #DDD;margin-left: 10px" v-show="!isCollapse">后台管理系统</b>
      </div>
      <el-menu-item index="/home">
        <i class="el-icon-s-home"></i>
        <span slot="title">主页</span>
      </el-menu-item>
      <el-submenu index="2">
        <template slot="title"><i class="el-icon-menu"></i>
          <span slot="title">系统管理</span>
        </template>
        <el-menu-item index="/user">
          <template slot="title"><i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/file">
          <template slot="title"><i class="el-icon-document"></i>
            <span slot="title">文件管理</span>
          </template>
        </el-menu-item>
      </el-submenu>
      <el-menu-item index="/mine">
        <i class="el-icon-s-custom"></i>
        <span slot="title">个人中心</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "LeftAside",
  computed: {
    isCollapse() {
      return this.$store.state.isCollapse
    }
  }
}
</script>

<style scoped>
/deep/ .el-submenu__title:hover,.el-menu-item:hover{
    background-color: rgb(88,105,126) !important;
  }
</style>