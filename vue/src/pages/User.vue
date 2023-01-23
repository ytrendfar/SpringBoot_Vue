<template>
  <div>
    <!--        搜索框-->
    <div style="padding: 10px 0;">
      <el-input style="width: 200px;" suffix-icon="el-icon-search" :placeholder="'请输入'+searchByWords+'进行搜索'"
                v-model="input"
                clearable
                @keydown.enter.native="sendRequestForData"
      />
      <el-dropdown style="margin-left: 8px" trigger="click" split-button type="primary" @click="sendRequestForData"
                   @command="handleCommand">
        <span>{{ searchByString }}</span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="0">根据名称搜索</el-dropdown-item>
          <el-dropdown-item command="1">根据邮箱搜索</el-dropdown-item>
          <el-dropdown-item command="2">根据地址搜索</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <!--          四个按钮-->
      <el-button-group style="float: right">
        <el-button type="success" icon="el-icon-plus" @click="createNew">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" @click="delBatch">删除</el-button>
        <el-button type="info" icon="el-icon-upload2" @click="exportUser">导出</el-button>
        <el-upload
            class="upload-demo"
            style="display: inline-block"
            :show-file-list="false"
            action="http://localhost/user/import"
            :accept="'.xlsx'"
            :on-success="importSuccess"
            :on-error="importFailed"
        >
          <el-button type="warning" icon="el-icon-download">导入</el-button>
        </el-upload>
      </el-button-group>

      <!--          点击按钮产生的对话框-->
      <el-dialog title="用户信息" :visible.sync="dialogFormVisible">
        <el-form :model="form">
          <el-form-item label="用户名">
            <el-input v-model="form.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="sendCreateOrUpdateRequest">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--        表格主体-->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="140"></el-table-column>
      <el-table-column prop="phone" label="电话" width="120"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="edit" label="操作">
        <template slot-scope="scope">
          <el-button-group>
            <el-button type="primary" icon="el-icon-edit" @click="editData(scope.row)">编辑</el-button>
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
  name: "User",
  data() {
    return {
      tableData: [],
      //搜索框
      input: '',
      searchBy: '0',        //0:name;1:email;2:address
      searchByWords: '名称',
      //表头背景样式
      headerBg: 'headerBg',
      //分页数据相关
      pageNum: 1,
      pageSize: 5,
      total: 0,
      //四个按钮相关
      dialogFormVisible: false,
      form: {},
      multipleSelection: [],
      showImportDialog: false
    }
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
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          string: this.input,
          type: this.searchBy
        }
      }).then(response => {
            if (response.code === '200') {
              console.log("分页数据请求成功！", response)
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
    //编辑和删除按钮功能
    editData(scope) {
      this.form = scope
      this.dialogFormVisible = true
    },
    deleteData(scope) {
      this.$confirm(`你确定要永久删除用户 ${scope.username} 的信息吗？`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete(`/user/delete/${scope.id}`).then(response => {
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
    //四个按钮功能
    //新增
    createNew() {
      this.form = {}
      this.dialogFormVisible = true
    },
    //发送新增或者更新请求
    sendCreateOrUpdateRequest() {
      this.request.post("/user/save", this.form).then(response => {
        if (response.code === '200') {
          this.dialogFormVisible = false
          if (this.form.id) {
            this.$message({
              showClose: true,
              message: `${this.form.username}的信息已成功更新!`,
              type: 'success'
            })
          } else {
            this.$message({
              showClose: true,
              message: `已成功添加${this.form.username}的信息！`,
              type: 'success'
            })
          }
          this.sendRequestForData()
        } else {
          this.$message({
            showClose: true,
            message: '该用户名已存在！',
            type: 'error'
          })
        }
      }).catch(error => {
        console.log(error)
        this.$message({
          showClose: true,
          message: '添加失败！',
          type: 'error'
        })
      })
    },
    //将选中的用户放入数组等待操作
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    //批量删除请求
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      let users = this.multipleSelection.map(v => v.username)
      this.$confirm(`你确定要永久删除以下用户的信息吗？${users.toString()}`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post("/user/del/batch", ids).then(response => {
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
    //导出
    exportUser() {
      this.$confirm('您将要导出表格中所有信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '导出成功!'
        });
        window.open("http://localhost/user/export")
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消导出'
        });
      });
    },
    //导入
    importSuccess() {
      console.log("#导入", "导入成功")
      this.$message({
        type: 'success',
        message: '导入成功！'
      })
      this.sendRequestForData()
    },
    importFailed() {
      console.log("#导入", "导出失败")
      this.$message({
        type: 'error',
        message: '导入失败！'
      })
    }
  },
  computed: {
    //搜索按钮文字显示
    searchByString() {
      if (this.searchBy === '0') {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        this.searchByWords = '名称'
        return '根据名称搜索'
      } else if (this.searchBy === '1') {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        this.searchByWords = '邮箱'
        return '根据邮箱搜索'
      } else {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        this.searchByWords = '地址'
        return '根据地址搜索'
      }
    }
  },
  created() {
    this.sendRequestForData()
  },
}
</script>

<style>
.headerBg {
  background-color: rgb(250, 250, 250) !important;
}
</style>