package com.young.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.common.Constants;
import com.young.common.Result;
import com.young.dto.UserDTO;
import com.young.exception.ServiceException;
import com.young.mapper.UserMapper;
import com.young.pojo.Menu;
import com.young.pojo.Role;
import com.young.pojo.RoleMenu;
import com.young.pojo.User;
import com.young.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleMenuService roleMenuService;

    //使用mp来实现分页查询
    public Result selectPage(Integer pageNum, Integer pageSize, String string, String type) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //将把type转化为具体字段名
        type = type.equals("0") ? "username" : type.equals("1") ? "email" : "address";
        //添加某字段的模糊查询
        queryWrapper.like(type, string);
        return Result.success(page(page, queryWrapper));
    }
    //如果是多条件查询，需要先对传入的参数进行判断，如果是空的话不能进行queryWrapper的添加

    //实现导出
    public void export(HttpServletResponse response) throws IOException {
        //从数据库中查询出所有数据
        List<User> users = list();
        //生成Excel。如果参数传入路径，会自动生成excel文件至指定路径中。这里没有指定，将生成的文件写入浏览器下载
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatar", "头像");
        //将查询结果直接写入writer
        writer.write(users, true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        System.out.println("导出结束");
    }

    //实现导入import
    public boolean imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> users = reader.readAll(User.class);
        System.out.println(users);
        //将文件中的user添加到数据库中
        return saveBatch(users);
    }

    //登录
    public Result login(UserDTO user) {
        String password = user.getPassword();
        String username = user.getUsername();
        //排除字符串为空的情况
        if (StrUtil.isBlank(password) || StrUtil.isBlank(username)) {
            return Result.error(Constants.CODE_400, "用户名或密码为空");
        }
        //进行判断
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        User one = getOne(queryWrapper);
        if (one == null) {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        } else {
            //把查询到的对应的用户上的信息copy到只有用户名和密码的userDTO对象上
            BeanUtil.copyProperties(one, user, true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            user.setToken(token);
            //设置menus
            //调用方法获取menu集合
            user.setMenus(getMenusByRoleId(one));
            return Result.success(user);
        }
    }

    /**
     * 获取用户可用的菜单集合
     *
     * @param user 传入数据库中查询到的用户
     * @return 返回对应的menu集合
     */
    private List<Menu> getMenusByRoleId(User user) {
        //获取角色字符串
        String roleString = user.getRole();
        //获取对应flag的角色
        Role role = roleService.getOne(new QueryWrapper<Role>().eq("flag", roleString));
        //获取所有菜单
        List<Menu> menus = menuService.findMenus();
        //获取角色对应的菜单集合
        QueryWrapper<RoleMenu> qw = new QueryWrapper<>();
        qw.eq("role_id", role.getId());
        List<Integer> menuIds = menuService.list(new QueryWrapper<Menu>()
                        .in("id", roleMenuService.list(qw).stream().map(RoleMenu::getMenuId).collect(Collectors.toList())))
                .stream().map(Menu::getId).collect(Collectors.toList());
        //删除多余的菜单
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                result.add(menu);
            } else {
                //该项不符合，则检查该项的子项
                if (menu.getChildren().size() != 0) {
                    List<Menu> childrenMenu = new ArrayList<>();
                    for (Menu child : menu.getChildren()) {
                        if (menuIds.contains(child.getId())) {
                            childrenMenu.add(child);
                        }
                    }
                    if(childrenMenu.size() != 0){
                        //设置新的子项
                        menu.setChildren(childrenMenu);
                        result.add(menu);
                    }
                }
            }
            //再移出不在ids中的二级菜单
            List<Menu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));       //如果ids中不包含children的遍历项的id，就移除此项
        }
        return result;
    }

    public Result register(UserDTO user) {
        String username = user.getUsername();
        //进行判断
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User one = getOne(queryWrapper);
        System.out.println(user);
        if (one != null) {
            throw new ServiceException(Constants.CODE_400, "用户名已存在");
        } else {
            one = new User();
            BeanUtil.copyProperties(user, one, true);
            if (save(one)) {
                return Result.success();
            } else {
                return Result.error();
            }
        }
    }
}
