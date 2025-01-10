package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.utils.MD5;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin
@Api(tags="用户接口")
//标注控制器类，该类的主要职责是处理 HTTP 请求并返回数据
@RestController
@RequestMapping("/admin/acl/user")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;

    //-1 为用户分配角色
    @ApiOperation("为用户分配角色")
    @PostMapping("doAssign")
    public Result doAssign(@RequestParam Long adminId,
                           @RequestParam Long[] roleId){
            roleService.saveAdminRole(adminId,roleId);
            return Result.ok(null);
    }

    //0 获取所有角色，根据用户id查询用户分配的角色列表
    @ApiOperation("获取用户角色")
    @GetMapping("toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId) {
        //map包含两部分数据，1获取所有角色，2.根据用户id查询用户分配的角色列表
          Map<String,Object> map  = roleService.getRoleByAdminId(adminId);
          return Result.ok(map);
    }


    //1 用户列表
    @ApiOperation("用户列表（条件分页查询）")
    @GetMapping("{page}/{limit}")
    public Result pageList(@PathVariable Long page,
                           @PathVariable Long limit,
                           AdminQueryVo adminQueryVo) {
        Page<Admin> PageParam = new Page<>(page, limit);
        IPage<Admin> pageModel = adminService.selectAdminPage(PageParam,adminQueryVo);
        return Result.ok(pageModel);
    }
    //2 id查询用户
    @ApiOperation("根据id查询用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {

        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }
    //3 添加用户
    @ApiOperation("添加用户")
    @PostMapping("save")
    public Result save(@RequestBody Admin admin) {
        //获取输入的密码
        String password = admin.getPassword();
        //对密码进行加密 MD5
        String passwordMD5 = MD5.encrypt(password);
        //设置到admin对象里
        admin.setPassword(MD5.encrypt(passwordMD5));
        //调用方法添加
        boolean save = adminService.save(admin);
        if(save){
            return Result.ok(null);
        }else{
            return Result.fail(null);
        }
    }
    //4 修改用户
    @ApiOperation("修改用户")
    @PutMapping("update")
    //Admin admin前端传递过来包含id值
    public Result update(@RequestBody Admin admin) {
        boolean update = adminService.updateById(admin);
        if(update){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //5 根据id删除
    @ApiOperation("根据id删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean remove = adminService.removeById(id);
        if(remove){
            return Result.ok(null);
        }  else{
            return Result.fail(null);
        }
    }
    //6 批量删除
    @ApiOperation("批量删除用户")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean removeByIds = adminService.removeByIds(ids);
        if(removeByIds){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
}
