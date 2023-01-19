package com.young.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.dto.UserDTO;
import com.young.mapper.UserMapper;
import com.young.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
//    @Autowired
//    private UserMapper userMapper;

    //查询所有用户
//    public List<User> findAll() {
//        return list();
//    }

    //    插入和更新：如果传入了id则为更新，未传入id则为插入
//    public boolean save(User user) {
//        if (user.getId() == null) {
//            return save(user);
//        } else {
//            return updateById(user);
//        }
//    }

//    //新增和更新
//    public boolean createOrUpdate(User user) {
//        return saveOrUpdate(user);
//    }


//    //根据id删除用户
//    public boolean deleteById(Integer id) {
//        return removeById(id);
//    }

    /**
     * 分页查询
     *
     * @param pageNum  页数。通过计算：（页数-1）乘 每页显示个数 求得索引
     * @param pageSize 每页要展示的数量
     * @return 返回查询结果和查询结果总数组成的map集合
     */
//    public Map<String, Object> selectPage(Integer pageNum, Integer pageSize) {
//        pageNum = (pageNum - 1) * pageSize;
//        List<User> data = userMapper.selectPage(pageNum, pageSize);
//        Integer total = userMapper.selectAllCount();
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", data);
//        map.put("total", total);
//        return map;
//    }

    //使用mp来实现分页查询
    public IPage<User> selectPage(Integer pageNum, Integer pageSize, String string, String type) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //将把type转化为具体字段名
        type = type.equals("0") ? "username" : type.equals("1") ? "email" : "address";
        //添加某字段的模糊查询
        queryWrapper.like(type, string);
        return page(page, queryWrapper);
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
    public boolean login(UserDTO user) {
        String password = user.getPassword();
        String username = user.getUsername();
        //排除字符串为空的情况
        if(StrUtil.isBlank(password) || StrUtil.isBlank(username)){
            return false;
        }
        //进行判断
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",password);
        User one = getOne(queryWrapper);
        return one != null;
    }
}
