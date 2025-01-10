package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.acl.service.RolePermissionService;
import com.atguigu.ssyx.acl.utils.PermissionHelper;
import com.atguigu.ssyx.model.acl.Permission;
import com.atguigu.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    RolePermissionService rolePermissionService;


    //查询所有菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1 查询所有菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2 转为要求的数据格式
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    //递归删除菜单
    @Override
    public void removeChildById(Long id) {
        //1 封装需要删除的所有菜单
        List<Long> idlist = new ArrayList<Long>();
        idlist.add(id);
        //根据当前菜单的id获取所有子菜单id，包括所有后代菜单，递归查询子菜单id
        this.getAllPermissionId(id,idlist);
        //批量删除菜单
        baseMapper.deleteBatchIds(idlist);
    }


    ////查询所有菜单和角色分配的菜单
    @Override
    public List<Permission> getPermissionByRoleId(Long roleId) {
        //1 查询所有菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2 根据角色id查询用户分配的菜单权限

        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);

        //3 创建由于存储菜单id的list集合
        List<Long> permissionIdList = rolePermissionList
                .stream().map(item->item.getPermissionId())
                .collect(Collectors.toList());
        //4 遍历菜单列表allPermissionList ,得到每个菜单

        for(Permission permission :allPermissionList){
            if(permissionIdList.contains(permission.getId())){
                permission.setSelect(true);
            }else{
                permission.setSelect(false);
            }
        }
        List<Permission> assignPermissionList = PermissionHelper.buildPermission(allPermissionList);
        return assignPermissionList;
    }

    //给角色分配菜单
    @Override
    public void doAssign(Long roleId, List<Long> permissionIds) {
        //1 删除角色已经分配过的菜单
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionService.remove(wrapper);
        //2 重新进行分配
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Long permissionid : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionid);
            rolePermission.setRoleId(roleId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
    }

    //递归查询子菜单id递归
    private void getAllPermissionId(Long id, List<Long> idlist) {
        //根据当前菜单查询子菜单
        //select * from permission where pid = 2
        LambdaQueryWrapper<Permission> wapper = new LambdaQueryWrapper<>();
        wapper.eq(Permission::getPid, id);
        List<Permission> childList = baseMapper.selectList(wapper);
        //递归查询是否还有子菜单
        childList.stream().forEach(item -> {
            idlist.add(item.getId());
            //递归
            this.getAllPermissionId(item.getId(),idlist);
        });
    }
}
