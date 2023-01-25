package com.young.pojo;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("sys_file")
public class Files {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Alias("文件名")
    private String name;
    @Alias("文件类型")
    private String type;
    @Alias("文件大小")
    private Long size;
    @Alias("下载链接")
    private String url;
    @Alias("是否删除")
    private Boolean isDelete;
    @Alias("是否禁用链接")
    private Boolean enable;
    private String md5;
}
