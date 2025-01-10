package com.atguigu.ssyx.acl.service.impl;


import com.atguigu.ssyx.acl.mapper.RoleMapper;
import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
//IService<Role> 接口的代理实现类是 ServiceImpl<RoleMapper, Role>，由 MyBatis-Plus 提供。
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    /*
    ServiceImpl已经完成了自动注入
    @Autowired
    private RoleMapper baseMapper;
    */
    @Autowired
    AdminRoleService adminRoleService;
    //1.角色列表（条件分页查询）
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {


        //获取条件值
        String roleName = roleQueryVo.getRoleName();
        //创建mp条件对象
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //判断角色名是否为空
        if(!StringUtils.isEmpty(roleName)){
            //MP可以通过Role::getRoleName获得字段名，在这就是role_name
            //wrapper等同于SQL查询语句，这里是模糊查询
            wrapper.like(Role::getRoleName, roleName);
        }
        //调用方法实现分页查询，baseMapper是BaseMapper<Role>接口的实现类，
        IPage<Role> rolePage = baseMapper.selectPage(pageParam, wrapper);

        //返回分页对象
        return rolePage;
    }

    //根据用户id查询用户分配的角色列表
    @Override
    public Map<String, Object> getRoleByAdminId(Long adminId) {
        //1.查询所有角色
        List<Role> roles = baseMapper.selectList(null);
        //2.根据用户id查询用户分配的角色列表

            //2.1根据用户id查询 用户角色关系表，获得角色id列表List<Long>
                LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(AdminRole::getAdminId, adminId);
                //adminRoleService.list(wrapper)作用是根据指定的条件查询数据库表中的数据，并返回符合条件的结果列表。
                List<AdminRole> adminRoles = adminRoleService.list(wrapper);

            //2.2创建由于存储用户角色id的list集合
            List<Long> roleIdList = adminRoles
                    .stream().map(item -> item.getRoleId())
                    .collect(Collectors.toList());
            //2.3遍历角色列表rolses,得到每个角色
            List<Role> assignRoleList = new ArrayList<>();
            for(Role role : roles){
                if(roleIdList.contains(role.getId())){
                    assignRoleList.add(role);
                }
            }
        //3.封装map集合
        HashMap<String, Object> result = new HashMap<>();
        result.put("allRolesList", roles);
        result.put("assignRoles", assignRoleList);
        return result;
    }

    //为用户分配角色
    @Override
    public void saveAdminRole(Long adminId, Long[] roleIds) {
        //1 删除用户已经分配过的角色
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        adminRoleService.remove(wrapper);
        //2 重新进行分配
        /*for(Long roleId : roleIds){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleService.save(adminRole);
        }*/
        //批量保存数据
        List<AdminRole> adminRoles = new ArrayList<>();
        for(Long roleId : roleIds){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoles.add(adminRole);
        }
        adminRoleService.saveBatch(adminRoles);
    }
}
