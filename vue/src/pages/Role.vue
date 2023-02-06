<template>
  <div>
    <div>
      <!--          四个按钮-->
      <el-button-group style="float: left;margin: 10px 0">
        <el-button type="danger" icon="el-icon-delete" @click="delBatch">批量删除</el-button>
        <el-button type="success" icon="el-icon-plus" @click="createNew">新增角色</el-button>
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
      <el-table-column prop="id" label="ID" align="center" width="50"></el-table-column>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="edit" label="操作" align="center" width="260">
        <template slot-scope="scope">
          <el-button-group>
            <el-button type="success" icon="el-icon-menu" @click="giveMenu(scope.row.id)">分配菜单</el-button>
            <el-button type="primary" icon="el-icon-download" @click="edit(scope.row)">编辑</el-button>
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


    <!--          点击编辑按钮产生的对话框-->
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="角色名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
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

    <!--          点击分配菜单产生的对话框-->
    <el-dialog title="菜单分配" :visible.sync="menuVisible">
      <el-tree
          :data="menuData"
          show-checkbox
          node-key="id"
          :default-expanded-keys="expandedArr"
          :default-checked-keys="checks"
          :props="defaultProps"
          ref="tree">
        <span class="custom-tree-node" slot-scope="{data}">
          <span><i :class="data.icon" style="margin: 0 10px"/>{{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuVisible = false">取 消</el-button>
        <el-button type="primary" @click="divideMenus">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Role",
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
      dialogFormVisible: false,
      form: {},
      //分配菜单
      menuVisible: false,
      menuData: [
        //假数据
        // {
        //   id: 1,
        //   label: '主页',
        // },
        // {
        //   id: 2,
        //   label: '系统管理',
        //   children: [
        //     {
        //       id: 4,
        //       label: '用户管理'
        //     },
        //     {
        //       id: 5,
        //       label: '文件管理'
        //     },
        //     {
        //       id: 6,
        //       label: '角色管理'
        //     },
        //     {
        //       id: 7,
        //       label: '菜单管理'
        //     }
        //   ]
        // },
        // {
        //   id: 3,
        //   label: '个人中心',
        // }
      ],
      defaultProps: {
        label: 'name'
      },
      expandedArr: [],
      checks: [],
      roleId: 0,
    }
  },
  created() {
    this.sendRequestForData()
  },
  methods: {
    sendUpdateRequest() {
      this.request.post("/role/save", this.form).then(res => {
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
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(response => {
            if (response.code === '200') {
              // console.log("分页数据请求成功！", response)
              this.tableData = response.data.records
              this.total = response.data.total
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
      this.$confirm(`你确定要永久删除角色 ${scope.name} 吗？`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete(`/role/delete/${scope.id}`).then(response => {
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
      this.$confirm(`你确定要永久删除以下角色吗？${files.toString()}`, "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post("/role/del/batch", ids).then(response => {
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
    //分配菜单
    giveMenu(id) {
      this.roleId = id
      this.checks = []
      //请求菜单数据
      this.request.get('/menu/selectAll').then(res => {
        if (res.code === '200') {
          // console.log('菜单数据请求成功！', res)
          this.menuData = res.data
          //将id映射至默认展开的数组中，使所有选项默认展开
          this.expandedArr = this.menuData.map(v => v.id)
        } else {
          this.$message.error('菜单数据请求失败')
        }
      }).catch(err => {
        console.log('菜单数据请求失败', err)
      })
      //请求关系数据
      this.request.get('/role/roleMenu/'+this.roleId).then(res => {
        if (res.code === '200') {
          // console.log('关系数据请求成功！', res)
          this.checks = res.data
        } else {
          this.$message.error('角色数据请求失败')
        }
      }).catch(err => {
        console.log('角色数据请求失败', err)
      })
      this.menuVisible = true
    },
    divideMenus() {
      //发送请求，在数据库中存储角色和菜单的关系
      this.request.post(`/role/roleMenu/${this.roleId}`, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '200') {
          this.$message.success('分配成功')
          this.menuVisible = false
        } else {
          this.$message.error('分配失败')
        }
      }).catch(err => {
        this.$message.success('分配失败')
        console.log(err)
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