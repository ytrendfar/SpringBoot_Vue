package com.young.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.young.common.Result;
import com.young.pojo.Role;
import com.young.pojo.RoleMenu;
import com.young.service.RoleMenuService;
import com.young.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    RoleService roleService;
    @Resource
    RoleMenuService roleMenuService;

    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(roleService.list());
    }


    @PostMapping("/save")
    public Result createOrUpdate(@RequestBody Role role) {
        try {
            if (roleService.saveOrUpdate(role)) {
                return Result.success();
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            return Result.error("400", "已存在相同名称的角色");
        }
    }

    //@PathVariable：从路径中传递参数
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        if (roleService.removeById(id)) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        if (roleService.removeBatchByIds(ids)) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //@RequestParam：从路径中的?&传递参数，比如/page?pageNum=2&pageSize=4 表示第二页，每页显示4条数据
    //string代表传入的模糊查询的目标，可能是用户名、地址或者邮箱。type代表string传入的具体是那三个中的哪个
    @GetMapping("/page")
    public Result selectPage(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize) {
        return roleService.selectPage(pageNum, pageSize);
    }


    //将role和menu关系存储数据库
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        return roleMenuService.roleMenu(roleId,menuIds);
    }

    //获取已经绑定的关系
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return roleMenuService.getRoleMenu(roleId);
    }


}
