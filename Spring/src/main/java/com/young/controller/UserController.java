package com.young.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.young.common.Result;
import com.young.dto.UserDTO;
import com.young.pojo.User;
import com.young.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(userService.list());
    }

    //RequestBody：把前台传入的json数据转成User对象
    @PostMapping("/save")
    public boolean createOrUpdate(@RequestBody User user) {
        boolean result;
        try{
            result = userService.saveOrUpdate(user);
        }catch (Exception e){
            return false;
        }
        return result;
    }

    //@PathVariable：从路径中传递参数
    @DeleteMapping("/delete/{id}")
    public Boolean deleteById(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    //批量删除
    @PostMapping("/del/batch")
    public Boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService.removeBatchByIds(ids);
    }

    //@RequestParam：从路径中的?&传递参数，比如/page?pageNum=2&pageSize=4 表示第二页，每页显示4条数据
    //string代表传入的模糊查询的目标，可能是用户名、地址或者邮箱。type代表string传入的具体是那三个中的哪个
    @GetMapping("/page")
    public IPage<User> selectPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam String string,
                                  @RequestParam String type) {
        return userService.selectPage(pageNum, pageSize, string, type);
    }
    //通过在@RequestParam后面加(defaultValue = "")可以设置默认值为空，防止在多条件查询时没有对某条件限制而导致错误
    //或者加(required = "false")可以表示这个参数可以不传入


    //导出用户信息
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        userService.export(response);
    }

    //导入用户信息
    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        return userService.imp(file);
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO user){
        return userService.login(user);
    }
}
