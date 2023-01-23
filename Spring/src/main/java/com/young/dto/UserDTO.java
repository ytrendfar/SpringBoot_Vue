package com.young.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * 用于接收前端登录请求的参数
 */
@Data
public class UserDTO {
    //使用Alias注解给各个字段设置成与User对象相同的别名，这样在使用BeanUtil的copyProperties方法时就不会出错了
    @Alias("用户名")
    private String username;
    @Alias("密码")
    private String password;
    @Alias("昵称")
    private String nickname;
    @Alias("头像")
    private String avatar;
    @Alias("邮箱")
    private String email;
    @Alias("电话")
    private String phone;
    @Alias("地址")
    private String address;

    private String token;

}
