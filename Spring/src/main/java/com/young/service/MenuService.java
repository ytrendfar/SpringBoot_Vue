package com.young.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.common.Result;
import com.young.mapper.MenuMapper;
import com.young.pojo.Menu;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    //使用mp来实现分页查询
    public Result selectPage(Integer pageNum, Integer pageSize) {
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        return Result.success(page(page, null));
    }

}
