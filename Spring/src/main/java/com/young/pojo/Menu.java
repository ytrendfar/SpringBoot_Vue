package com.young.pojo;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("sys_menu")
public class Menu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Alias("名称")
    private String name;
    @Alias("路径")
    private String path;
    @Alias("图标")
    private String icon;
    @Alias("组件")
    private String component;
    @Alias("描述")
    private String description;
    //字段在数据库中不存在
    @TableField(exist = false)
    private List<Menu> children;
    @Alias("pid")
    private Integer pid;
}
