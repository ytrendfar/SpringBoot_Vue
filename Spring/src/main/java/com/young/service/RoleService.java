package com.young.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.common.Result;
import com.young.mapper.RoleMapper;
import com.young.pojo.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    //使用mp来实现分页查询
    public Result selectPage(Integer pageNum, Integer pageSize) {
        IPage<Role> page = new Page<>(pageNum, pageSize);
        return Result.success(page(page, null));
    }

}
