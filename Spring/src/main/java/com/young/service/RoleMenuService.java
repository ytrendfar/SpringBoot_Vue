package com.young.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.common.Result;
import com.young.mapper.DictMapper;
import com.young.mapper.RoleMenuMapper;
import com.young.pojo.Dict;
import com.young.pojo.RoleMenu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> {

    /**
     * 绑定角色和菜单的关系
     * @param roleId  角色id
     * @param menuIds 菜单id数组
     * @return 返回绑定成功与否的结果
     */
    public Result roleMenu(Integer roleId, List<Integer> menuIds) {
        //先删除再绑定
        QueryWrapper<RoleMenu> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId);
        remove(qw);
        for (Integer menuId : menuIds) {
            save(new RoleMenu(roleId,menuId));
        }
        return Result.success();
    }

    /**
     * 获取指定roleId的关系
     * @param roleId 指定角色id
     * @return 返回结果
     */
    public Result getRoleMenu(Integer roleId) {
        QueryWrapper<RoleMenu> qw = new QueryWrapper<>();
        qw.eq("role_id",roleId);
        //映射出id数组
        List<Integer> menuIds = list(qw).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        System.out.println("!!!"+ menuIds);
        return Result.success(menuIds);
    }
}
