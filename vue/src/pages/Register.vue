<template>
  <div class="wrapper">
    <div style="margin: 60px auto;width: 600px;padding: 20px 80px;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 25px;">
      <div style="margin: 10px 0 30px 0;text-align: center;font-size: 24px"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm" label-position="right">
        <el-form-item prop="username" label="用户名" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-user" v-model="user.username"/>
        </el-form-item>
        <br>
        <el-form-item prop="password" label="密 码" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"/>
        </el-form-item>
        <br>
        <el-form-item prop="confirm" label="确认密码" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-warning-outline" show-password
                    v-model="confirm"/>
        </el-form-item>
        <br>
        <el-form-item label="昵 称" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-postcard"
                    v-model="user.nickname"/>
        </el-form-item>
        <br>
        <el-form-item label="邮 箱" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-message"
                    v-model="user.email"/>
        </el-form-item>
        <br>
        <el-form-item label="地 址" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-position"
                    v-model="user.address"/>
        </el-form-item>
        <br>
        <el-form-item label="电 话" label-width="80px">
          <el-input size="medium" prefix-icon="el-icon-phone"
                    v-model="user.phone"/>
        </el-form-item>
        <br>
        <el-form-item style="margin: 10px 0;text-align: right">
          <el-button style="background-color: rgb(194, 230, 194)" size="small" autocomplete="off" @click="register">
            立即注册
          </el-button>
          <el-button style="background-color: rgb(231, 194, 231)" size="small" autocomplete="off"
                     @click="$router.push('/login')">返回登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Register",
  data() {
    //定义输入密码检查规则
    let passwordCheck = (rule, value, callback) => {
      if (this.confirm === '') {
        callback(new Error('请再次输入密码'))
      } else if (this.confirm !== this.user.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      user: {},
      confirm: '',
      //用户名密码再次输入密码的检查规则
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirm: [
          {validator: passwordCheck, trigger: 'blur',required:'true'}
        ]
      }
    }
  },
  methods: {
    register() {
      this.$refs["userForm"].validate((valid) => {
        //发送请求前先检查是否合法
        if (valid) {
          this.request.post("/user/register", this.user).then(response => {
            if (response.code !== '200') {
              this.$message({
                showClose: true,
                message: response.msg,
                type: 'error'
              })
            } else {
              this.$message({
                showClose: true,
                message: `注册成功，欢迎用户${this.user.nickname}`,
                type: 'success'
              })
              //进行注册操作成功后
              this.$store.state.afterRegisterUsername = this.user.username
              this.$router.push('/login')
            }
          })
        }
      })
    },

  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  /*background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);*/
  background-image: linear-gradient(to bottom right, #AAFFAA, #FFAAFF);
  overflow: hidden;
}
</style>