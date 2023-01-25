package com.young.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.young.common.Result;
import com.young.mapper.FileMapper;
import com.young.pojo.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class FileService extends ServiceImpl<FileMapper, Files> {

    //传入配置文件中的路径
    @Value("${files.upload.path}")
    private String fileUploadPath;


    /**
     * 文件上传接口
     *
     * @param file 前端传递过来的文件
     * @return 返回在数据库存储的链接字符串
     * @throws IOException 捕获在磁盘中存储时抛出的异常
     */
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //使用hutool中的文件工具通过文件名来获取文件类型
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //使用hutool为每个文件定义唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUuid = uuid + StrUtil.DOT + type;
        //新建文件
        File uploadFile = new File(fileUploadPath + fileUuid);
        //判断，如果父目录不存在就创建
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        String url;
        String md5;

        //将文件存入磁盘中
        file.transferTo(uploadFile);
        //获取文件的md5。然后根据md5先去数据库中查询文件
        md5 = SecureUtil.md5(uploadFile);
        QueryWrapper<Files> qw = new QueryWrapper<>();
        qw.eq("md5", md5);
        Files md5File = getOne(qw);
        //进行判断
        if (md5File != null) {
            //如果已存在相同md5的文件，删除新存入的文件
            url = md5File.getUrl();
            uploadFile.delete();
        } else {
            url = "http://localhost/file/" + fileUuid;
        }


        //新建一个pojo类，设置名称类型和大小
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setMd5(md5);
        saveFile.setUrl(url);

        //存储至数据库中
        save(saveFile);
        return url;
    }


    /**
     * 下载文件接口
     *
     * @param fileUuid 文件后缀，包括uid和后缀名
     * @param response http响应流
     * @throws IOException 抛出获取输出流和操作流时的异常
     */
    public void download(String fileUuid, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码来获取文件
        File uploadFile = new File(fileUploadPath + fileUuid);
        //设置输出流的格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode((fileUuid), "UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();

        String fileName = URLEncoder.encode("用户信息", "UTF-8");
    }

    //使用mp来实现分页查询
    public Result selectPage(Integer pageNum, Integer pageSize) {
        IPage<Files> page = new Page<>(pageNum, pageSize);
        //只选择假删除属性为0的
        QueryWrapper<Files> qw = new QueryWrapper<>();
        qw.eq("is_delete", 0);
        return Result.success(page(page, qw));
    }

    //将指定id的文件的is_delete改为 1
    public Result deleteById(Integer id) {
        //新建file，先把要修改的字段给赋值
        Files file = new Files();
        file.setIsDelete(true);
        //然后通过update的第一个参数给新建的file传进去，第二个参数传入updateWrapper
        if (update(file, new UpdateWrapper<Files>().eq("id", id))) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //批量删除
    public Result deleteBatch(List<Integer> ids) {
        Files file = new Files();
        file.setIsDelete(true);
        //通过in来实现在参数为数组的情况下的更新
        if (update(file, new UpdateWrapper<Files>().in("id", ids))) {
            return Result.success();
        } else {
            return Result.error();
        }
    }
}
