package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//解决跨域问题：跨域问题是由于前端网页url端口号与后端url端口号不一致造成
@CrossOrigin
//swagger中的接口名称
@Api(tags="登录接口")
//标识这个类是一个控制器类,指示方法的返回值应该直接写入 HTTP 响应体中，而不是被解析为视图名称。
@RestController
//当接收到 "http://localhost:8080/admin/acl/index" 这个 URL 请求时，将由相应的控制器方法来处理该请求。
@RequestMapping("/admin/acl/index")
public class IndexController {

    //1.login 登录
    //url:/admin/acl/index/login
    @ApiOperation("登录")
    @PostMapping("login")
    public Result login() {
        //返回token值
        Map<String,String> map = new HashMap<String,String>();
        map.put("token","token-admin");
        return Result.ok(map);
    }

    //2.getInfo 获取信息
    //url:/admin/acl/index/info
    @ApiOperation("返回信息")
    @GetMapping("info")
    public Result info() {
        //返回用户名称和头像
        Map<String,String> map = new HashMap<String,String>();
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }


    //3.logout 退出
    //url:/admin/acl/index/logout
    @ApiOperation("退出")
    @PostMapping("logout")
    public Result logout() {
        //无返回值
        return Result.ok(null);
    }
}
