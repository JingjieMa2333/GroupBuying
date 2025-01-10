package com.atguigu.ssyx.product.controller;

import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/admin/product")
@Api(tags = "文件上传接口" )
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    //图片上传方法
    @ApiOperation("图片上传")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        String url = fileUploadService.uploadFile(file);
        return Result.ok(url);
    }
}
