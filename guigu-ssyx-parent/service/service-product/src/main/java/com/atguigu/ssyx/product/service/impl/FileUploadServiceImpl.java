package com.atguigu.ssyx.product.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.ssyx.product.service.FileUploadService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${aliyun.endpoint}")
    private String endPoint;
    @Value("${aliyun.keyid}")
    private String accessKey;
    @Value("${aliyun.keysecret}")
    private String secreKey;
    @Value("${aliyun.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, secreKey);

        try {
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            //创建唯一文件名
            String objectName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");//得到一个长度为 32 的字符串
            objectName = uuid + objectName;
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            //  2021/02/02/01.jpg
            String currentdate = new DateTime().toString("yyyy/MM/dd");
            objectName = currentdate +"/"+ objectName;
            //objectName:Object完整路径,会存在bucket里
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            putObjectRequest.setProcess("true");
            //创建PutObject请求
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //获取状态码，如果成功返回200
            System.out.println(result.getResponse().getStatusCode());
            System.out.println(result.getResponse().getErrorResponseAsString());
            System.out.println(result.getResponse().getUri());
            //返回文件在阿里云的路径
            String url = result.getResponse().getUri();
            return url;
        } catch (Exception ce) {
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
