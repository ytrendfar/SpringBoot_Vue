<template>
  <div>
    <div>
      <!--          四个按钮-->
      <el-button-group style="float: left;margin: 10px 0">
        <el-button type="danger" icon="el-icon-delete" @click="delBatch">批量删除</el-button>
        <el-upload
            class="upload-demo"
            style="display: inline-block"
            :show-file-list="false"
            action="http://localhost/file/upload"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :limit="1"
        >
          <el-button type="success" icon="el-icon-upload">上传文件</el-button>
        </el-upload>
      </el-button-group>
    </div>

    <!--          表格主体-->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="50"
          align="center">
      </el-table-column>
      <el-table-column prop="id" align="center" label="ID" width="50"></el-table-column>
      <el-table-column prop="name" label="文件名"></el-table-column>
      <el-table-column prop="type" label="文件类型" width="80"></el-table-column>
      <el-table-column prop="size" label="文件大小(KB)" width="100"></el-table-column>
      <el-table-column prop="enable" label="启用" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" @change="changeEnable(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column prop="edit" label="操作">
        <template slot-scope="scope">
          <el-button-group>
            <el-button type="primary" icon="el-icon-download" @click="downLoad(scope.row)">下载</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteData(scope.row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <!--        分页管理器-->
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 8, 10]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "File",
  data() {
    return {
      tableData: [],
      //表头背景样式
      headerBg: 'headerBg',
      //分页数据相关
      pageNum: 1,
      pageSize: 5,
      total: 0,
      //四个按钮相关
      multipleSelection: [],
    }
  },
  created() {
    this.sendRequestForData()
  },
  methods: {
    //  分页相关
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.sendRequestForData()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.sendRequestForData()
    },
    sendRequestForData() {
      //请求分页查询的数据结果
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(response => {
            if (response.code === '200') {
              // console.log("分页数据请求成功！", response)
              this.tableData = response.data.records
              this.total = response.data.total
            }else if(response.code === '401'){
              //由于已经在request.js中进行过全局配置了，所以不需要再进行提示，只需要push即可
              // this.$message.error(response.msg)
              console.log(response.msg,response)
              this.$router.push('/login')
            }
          }
      ).catch(error => {
        console.log("分页数据请求失败！", error)
      })
    },
    handleCommand(command) {
      this.searchBy = command
    },
    //删除按钮功能
    deleteData(scope) {
      this.$confirm(`你确定要永久删除文件 ${scope.name} 吗？`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete(`/file/delete/${scope.id}`).then(response => {
          if (response.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.sendRequestForData()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            });
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    //下载按钮功能
    downLoad(scope){
      //打开下载页面
      window.open(scope.url)
    },
    //两个按钮功能
    //新增
    createNew() {
      this.form = {}
      this.dialogFormVisible = true
    },
    //将选中的用户放入数组等待操作
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    //批量删除请求
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      let files = this.multipleSelection.map(v => v.name)
      this.$confirm(`你确定要永久删除以下文件吗？${files.toString()}`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post("/file/del/batch", ids).then(response => {
          if (response.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.sendRequestForData()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            });
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    //更新enable状态
    changeEnable(scope){
      this.request.post('/file/save',scope).then(response => {
        console.log('更新成功',response)
      }).catch( error => {
        console.log('更新失败',error)
      })
    },
    //上传成功和失败
    uploadSuccess(){
      this.$message.success('上传成功')
      this.sendRequestForData()
    },
    uploadError(){
      this.$message.error('上传失败')
    }
  }
}
</script>

<style scoped>
.headerBg {
  background-color: rgb(250, 250, 250) !important;
}
</style>