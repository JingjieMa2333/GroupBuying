package com.atguigu.ssyx.acl.controller;


import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Api(tags="菜单管理")
@RestController
@RequestMapping("/admin/acl/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    //查询所有菜单和角色分配的菜单
    @ApiOperation("查询所有菜单和角色分配的菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<Permission> list = permissionService.getPermissionByRoleId(roleId);
        return Result.ok(list);
    }

    //给角色分配菜单
    @ApiOperation("给角色分配菜单")
    @PostMapping("doAssign")
    public Result doAssign(@RequestParam Long roleId, @RequestParam List<Long> permissionId) {
        permissionService.doAssign(roleId,permissionId);
        return Result.ok(null);
    }

    //查询所有菜单，树形结构
    @ApiOperation("查询所有菜单")
    @GetMapping()
    public Result getPermission() {
        List<Permission> list = permissionService.queryAllPermission();
        return Result.ok(list);

    }

    //添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("save")
    public Result savePermission(@RequestBody Permission permission) {
        boolean save = permissionService.save(permission);
        if(save){
            return Result.ok(null);
        }else{
            return  Result.fail(null);
        }
    }

    //修改菜单
    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result updatePermission(@RequestBody Permission permission) {
        boolean update = permissionService.updateById(permission);
        if(update){
            return Result.ok(null);
        }else{
            return Result.fail(null);
        }
    }
    //递归删除菜单
    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result removePermission(@PathVariable Long id) {
        permissionService.removeChildById(id);
        return Result.ok(null);
    }
}
