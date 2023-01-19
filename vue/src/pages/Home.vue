<template>
  <div>这是主页的内容</div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Home",
  mounted() {
    this.$store.state.paths = [{
      name: '首页',
      path: '/home'
    }]
  }
}
</script>

<style scoped>

</style>