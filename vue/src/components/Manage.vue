<template>
  <el-container style="height: 100vh">
    <!--    左边栏-->
    <LeftAside/>
    <!--    右侧内容-->
    <el-container style="flex-direction: column;">
      <!--      上边框内容-->
      <Header/>
      <!--      表格内容-->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import LeftAside from "@/components/LeftAside";
import Header from "@/components/Header";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Manage',
  // eslint-disable-next-line vue/no-unused-components
  components: {LeftAside, Header},

}
</script>

<style>

</style>