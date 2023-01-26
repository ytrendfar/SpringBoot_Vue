<template>
  <div>
    <el-row :gutter="20" style="margin-top:10px;">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>个人中心</span>
            </div>
            <el-upload
                style="text-align: center"
                class="avatar-uploader"
                action="http://localhost/file/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
              <img v-if="user.avatar" :src="user.avatar" class="avatar" alt="">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
<!--            <div style="text-align: center">-->
<!--              <el-image :src="imgSrc" alt=""-->
<!--                        style="width: 200px;height: 200px;border-radius: 50%;margin: auto"-->
<!--                        :preview-src-list="[user.avatar]">-->
<!--                <div slot="placeholder" class="image-slot">-->
<!--                  未上传<span class="dot">...</span>-->
<!--                </div>-->
<!--              </el-image>-->
<!--            </div>-->
            <el-divider/>
            <div class="personal-relation">
              <div class="relation-item">用户名:  <div style="float: right; padding-right:20px;">{{user.username}}</div></div>
            </div>
            <div class="personal-relation">
              <div class="relation-item">ID:  <div style="float: right; padding-right:20px;">{{user.id}}</div></div>
            </div>
            <div class="personal-relation">
              <div class="relation-item">注册时间:  <div style="float: right; padding-right:20px;">{{user.createTime}}</div></div>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="16">
        <div class="grid-content bg-purple">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>基本资料</span>
            </div>
            <div>
              <el-form label-width="80px" v-model="user" size="small" label-position="right">
                <el-form-item label="用户昵称" prop="nickname">
                  <el-input  auto-complete="off" v-model="user.nickname"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="phone">
                  <el-input auto-complete="off" v-model="user.email"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input auto-complete="off" v-model="user.phone"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="homeUrl">
                  <el-input type="textarea" maxlength="18" v-model="user.address"></el-input>
                </el-form-item>

              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button size="mini" type="primary" @click="updateUser">提交</el-button>
                <el-button size="mini" type="warning" @click="$router.push('/')">关闭</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>

    </el-row>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Mine.vue",
  data(){
    return {
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')): {},
      imageUrl: ''
    }
  },
  methods:{
    updateUser(){
      this.$confirm('此操作将保存信息更改, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post('/user/save',this.user).then(res =>{
          if (res.code === '200'){
            this.$message.success('修改成功')
            //同时修改本地内存中的数据
            localStorage.setItem('user',JSON.stringify(this.user))
            //触发全局事件总线
            this.$bus.$emit('updateAvatar',this.user.avatar,this.user.nickname)
          }else{
            this.$message.error('保存失败')
          }
        }).catch(err =>{
          this.$message.error(err.msg)
        })
      })
    },
    //头像上传相关
    handleAvatarSuccess(res) {
      this.user.avatar = res
      this.request.post('/user/save',this.user).then(res =>{
        if (res.code === '200'){
          this.$message.success('头像修改成功')
          //同时修改本地内存中的数据
          localStorage.setItem('user',JSON.stringify(this.user))
          //触发全局事件总线
          this.$bus.$emit('updateAvatar',this.user.avatar,this.user.nickname)
        }else{
          this.$message.error('头像保存失败')
        }
      }).catch(err =>{
        this.$message.error(err.msg)
      })
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 100%;
}
.name-role {
  font-size: 16px;
  padding: 5px;
  text-align:center;
}
.sender{
  text-align:center;
}
.registe-info{
  text-align: center;
  padding-top:10px;
}
.personal-relation {
  font-size: 16px;
  padding: 0 5px 15px;
  margin-right: 1px;
  width: 100%
}

.relation-item {
  padding: 12px;

}
.dialog-footer{
  padding-top:10px ;
  padding-left: 10%;
}
.el-row {
  margin-bottom: 20px;

}
.el-row:last-child{
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}


/*头像相关*/
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 200px;
  height: 200px;
  display: block;
  border-radius: 50%
}
</style>