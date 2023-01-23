package com.young.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.young.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    @Select("select * from sys_user")
//    List<User> findAll();

//    @Insert("insert into sys_user(username,password,nickname,email,phone,address) values (#{username},#{password},#{nickname},#{email},#{phone},#{address})")
//    Boolean add(User user);
//
//    Boolean update(User user);
//
//    @Delete("delete from sys_user where id = #{id}")
//    int deleteById(@Param("id") Integer id);

    /**
     * 分页查询：使用limit对查询的数据进行限制。如果只有一个参数，默认第一个参数为0
     * @param pageNum   从查询结果的第几个开始显示
     * @param pageSize  要显示的查询结果的个数
     * @return  返回限制后的查询结果
     */
//    @Select("select * from sys_user limit #{pageNum},#{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize);
//
//    @Select("select count(*) from sys_user")
//    Integer selectAllCount();


    //注册
//    @Insert("insert into sys_user(username,password,nickname,email,phone,address) values (#{username},#{password},#{nickname},#{email},#{phone},#{address})")
//    boolean registerNew(User user);
}
