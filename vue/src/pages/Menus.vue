<template>
  <div>
    <div>
      <!--          四个按钮-->
      <el-button-group style="float: left;margin: 10px 0">
        <el-button type="danger" icon="el-icon-delete" @click="delBatch">批量删除</el-button>
        <el-button type="success" icon="el-icon-plus" @click="createNew">新增菜单</el-button>
      </el-button-group>

    </div>

    <!--          表格主体-->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange"
              row-key="id"
              default-expand-all>
      <el-table-column
          type="selection"
          width="50">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="75"></el-table-column>
      <el-table-column label="图标" width="50px" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" style="font-size: 20px"/>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="菜单名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="edit" label="操作" align="center" width="275">
        <template slot-scope="scope">
          <el-button-group style="display: flex;justify-content: flex-end">
            <el-button type="success" icon="el-icon-plus" @click="addChild(scope.row.id)" v-if="!scope.row.pid">新增子菜单</el-button>
            <el-button type="primary" icon="el-icon-download" @click="edit(scope.row)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteData(scope.row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>


    <!--          点击按钮产生的对话框-->
    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="菜单名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标"><br>
          <el-select clearable v-model="form.icon" placeholder="请选择图标..." style="width:100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value" >
              <i :class="item.value"/><span style="margin-left: 5px">{{item.name}}</span>
            </el-option>
          </el-select>
<!--          <el-input v-model="form.icon" autocomplete="off"></el-input>-->
        </el-form-item>

        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="sendUpdateRequest">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "menus",
  data() {
    return {
      tableData: [],
      //表头背景样式
      headerBg: 'headerBg',
      //四个按钮相关
      multipleSelection: [],
      dialogFormVisible: false,
      form: {},
      options:[]
    }
  },
  created() {
    this.sendRequestForData()
  },
  methods: {
    sendUpdateRequest() {
      this.request.post("/menu/save", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.dialogFormVisible = false
          this.sendRequestForData()
        } else {
          this.$message.error(res.msg)
        }
      }).catch(err => {
        console.log('异常', err)
      })
    },
    sendRequestForData() {
      //请求查询所有的数据结果
      this.request.get("/menu/selectAll").then(response => {
            if (response.code === '200') {
              console.log("树形数据请求成功！", response)
              this.tableData = response.data
            } else if (response.code === '401') {
              //由于已经在request.js中进行过全局配置了，所以不需要再进行提示，只需要push即可
              // this.$message.error(response.msg)
              console.log(response.msg, response)
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
      this.$confirm(`你确定要永久删除菜单 ${scope.name} 吗？`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete(`/menu/delete/${scope.id}`).then(response => {
          if (response.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.sendRequestForData()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    //编辑按钮功能
    edit(scope) {
      this.form = scope
      this.dialogFormVisible = true
      //请求图标数据
      this.sendRequestForIcon()
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
      this.$confirm(`你确定要永久删除以下菜单吗？${files.toString()}`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post("/menu/del/batch", ids).then(response => {
          if (response.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.sendRequestForData()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    //新增子菜单
    addChild(pid){
      this.dialogFormVisible = true
      this.form = {}
      if(pid){
        this.form.pid = pid
      }
      //请求图标数据
      this.sendRequestForIcon()
    },
    //请求图标数据。在新增子菜单和编辑时使用
    sendRequestForIcon(){
      this.request.get('/menu/icon').then(res=>{
        this.options = res.data
        console.log('图标数据请求成功！',res)
      }).catch(err=>{
        console.log('图标数据请求失败！', err)
      })
    }
  }
}
</script>

<style scoped>
.headerBg {
  background-color: rgb(250, 250, 250) !important;
}
</style>