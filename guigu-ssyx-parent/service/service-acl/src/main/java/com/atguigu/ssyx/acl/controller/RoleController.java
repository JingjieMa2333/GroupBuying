package com.atguigu.ssyx.acl.controller;


import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*@CrossOrigin(origins = "http://localhost:3000") // 只允许来自 http://localhost:3000 的请求*/
@CrossOrigin// 允许所有来源访问
@Api(tags="角色接口")
@RestController
@RequestMapping("/admin/acl/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //1.角色列表（条件分页查询）
    @ApiOperation("角色列表（条件分页查询）")
    //{current} 和 {limit} 是路径参数，表示从 URL 中动态传入的值，和@PathVariable配合使用
    @GetMapping("{current}/{limit}")
    /*
    例子：前端使用params: searchObj 生成getPageList(1, 10, { roleName: 'Admin' });
    转化后的 URL：GET /api/roles/1/10?roleName=Admin

    Spring 在绑定 RoleQueryVo 时隐式使用了 @ModelAttribute 注解。
    @ModelAttribute注解告诉 Spring：
    从请求的查询参数中提取数据。
    根据参数名匹配 RoleQueryVo 的字段，完成赋值。
    相当于@ModelAttribute RoleQueryVo roleQueryVo
    */
    public Result pageList(@PathVariable Long current,
                           @PathVariable Long limit,
                           RoleQueryVo roleQueryVo){

        //1.创建page对象，传递当current:前页 和 limit:每页记录数
        Page<Role> pageParam = new Page<>(current, limit);

        //2.调用service实现条件分页查询，返回分页对象
        IPage<Role> pageModel = roleService.selectRolePage(pageParam,roleQueryVo);

        return Result.ok(pageModel);
        /*
            Page<Role> pageParam = new Page<>(1, 10); // 查询第 1 页，每页 10 条
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("role_name", "Admin"); // 添加查询条件

            IPage<Role> pageResult = baseMapper.selectPage(pageParam, queryWrapper);

            System.out.println("总记录数：" + pageResult.getTotal());
            System.out.println("总页数：" + pageResult.getPages());
            System.out.println("当前页数据：" + pageResult.getRecords());
         */
    }
    //2.根据id查询角色
    @ApiOperation("根据id查询角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Role role = roleService.getById(id);
        return Result.ok(roleService.getById(id));
    }
    //3.添加角色
    @ApiOperation("添加角色")
    @PostMapping("save")
    //@RequestBody接受json格式数据，前端使用data: role
    public Result save(@RequestBody Role role){
        boolean is_success = roleService.save(role);
        if(is_success){
            return Result.ok(null);
        }else{
            return Result.fail(null);
        }
    }
    //4.修改角色
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody Role role){
        boolean is_success = roleService.updateById(role);
        if(is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //5.根据id删除角色
    @ApiOperation("根据id删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean is_success = roleService.removeById(id);
        if(is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //6.批量删除角色
    //ids=[1,2,3]是list集合
    @ApiOperation("批量删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean is_success = roleService.removeByIds(ids);
        if(is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }


}
