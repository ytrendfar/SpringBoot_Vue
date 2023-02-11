<template>
  <div class="wrapper">
    <div style="margin: 15% auto;width: 45vh;padding: 20px;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 25px;
">
      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登 录</b></div>

      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 20px 0 5px" prefix-icon="el-icon-user" v-model="user.username" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password" @keydown.enter.native="login"/>
        </el-form-item>
        <el-form-item style="margin: 10px 0;text-align: right">
          <el-button style="background-color: rgb(177, 170, 79);color: #EEE" size="small" autocomplete="off" @click="login">登录</el-button>
          <el-button style="background-color: rgb(88, 170, 167);color: #EEE" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  data() {
    return {
      //如果总线中存储了刚注册完的用户名那么就直接赋值
      user: {username: this.$store.state.afterRegisterUsername ? this.$store.state.afterRegisterUsername : '' },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },
    }
  },
  methods: {
    login() {
      this.$refs["userForm"].validate((valid)=>{
        //发送请求前先检查是否合法
        if(valid){
          this.request.post("/user/login", this.user).then(response => {
            if (response.code !== '200') {
              this.$message({
                showClose: true,
                message: response.msg,
                type: 'error'
              })
            } else {
              //登录成功
              //将传入的用户信息存入本地内存
              localStorage.setItem('user',JSON.stringify(response.data))
              //设置动态路由
              setRoutes()
              this.$router.push("/")
              //弹窗
              this.$message({
                showClose: true,
                message: '登录成功',
                type: 'success'
              })
            }
          })
        }
      })
    },
  },
  beforeDestroy() {
    this.$store.state.afterRegisterUsername = ''
  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  /*background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);*/
  background-image: linear-gradient(to bottom right, #FFAA00, #00AAFF);
  overflow: hidden;
}
</style>