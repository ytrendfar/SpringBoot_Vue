package com.young.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.common.Result;
import com.young.mapper.MenuMapper;
import com.young.pojo.Menu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    //使用mp来实现分页查询
    public Result selectPage(Integer pageNum, Integer pageSize) {
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        return Result.success(page(page, null));
    }

    //查询所有菜单和子菜单
    public List<Menu> findMenus() {
        //查询所有菜单
        List<Menu> list = list();
        //找出pid为null的一级菜单
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (Menu menu : parentNode) {
            //筛选出二级菜单，即pid等于menu的id的元素
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNode;
    }
}
