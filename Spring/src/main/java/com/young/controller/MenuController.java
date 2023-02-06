package com.young.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.young.common.Constants;
import com.young.common.Result;
import com.young.mapper.DictMapper;
import com.young.pojo.Dict;
import com.young.pojo.Menu;
import com.young.pojo.Role;
import com.young.service.DictService;
import com.young.service.MenuService;
import com.young.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    MenuService menuService;
    @Resource
    DictService dictService;

    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(menuService.findMenus());
    }

    @PostMapping("/save")
    public Result createOrUpdate(@RequestBody Menu menu) {
        try {
            if (menuService.saveOrUpdate(menu)) {
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
        if (menuService.removeById(id)) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        if (menuService.removeBatchByIds(ids)) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //分页查询
    @GetMapping("/page")
    public Result selectPage(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize) {
        return menuService.selectPage(pageNum, pageSize);
    }

    //获取图标
    @GetMapping("/icon")
    public Result getIcon(){
        //查询所有icon类型的dict数据
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictService.list(queryWrapper));
    }

}
