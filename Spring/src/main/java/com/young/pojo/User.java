package com.young.pojo;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@ToString

//通过@Alias注解给字段起别名，可以在excel中通过中文别名的表头识别数据
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Alias("用户名")
    private String username;
    //通过JsonIgnore注解，忽略某个字段，在json找那个不会显示。通常用在密码中
    @JsonIgnore
    @Alias("密码")
    private String password;
    @Alias("昵称")
    private String nickname;
    @Alias("邮箱")
    private String email;
    @Alias("电话")
    private String phone;
    @Alias("地址")
    private String address;
    //通过注解指定字段别名
    @TableField(value = "avatar_url")
    @Alias("头像")
    private String avatar;
    @Alias("创建时间")
    private Date createTime;
    @Alias("角色")
    private String role;
}
