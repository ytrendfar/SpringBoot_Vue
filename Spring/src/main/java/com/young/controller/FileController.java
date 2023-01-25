package com.young.controller;

import com.young.common.Result;
import com.young.pojo.Files;
import com.young.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件上传接口
 */

@RestController
@RequestMapping("/file")
public class FileController {


    //传入fileService
    @Resource
    private FileService fileService;


    //删除指定文件：假删除，将is_delete改为 1即可
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return fileService.deleteById(id);
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return fileService.deleteBatch(ids);
    }

    //@RequestParam：从路径中的?&传递参数，比如/page?pageNum=2&pageSize=4 表示第二页，每页显示4条数据
    @GetMapping("/page")
    public Result selectPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return fileService.selectPage(pageNum, pageSize);
    }

    //上传文件
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    //下载指定文件
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        fileService.download(fileUuid, response);
    }

    //更新文件
    @PostMapping("/save")
    public Result save(@RequestBody Files file) {
        if (fileService.saveOrUpdate(file)) return Result.success();
        else return Result.error();
    }

}
