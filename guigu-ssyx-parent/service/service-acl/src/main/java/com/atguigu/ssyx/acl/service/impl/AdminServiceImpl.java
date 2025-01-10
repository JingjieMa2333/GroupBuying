package com.atguigu.ssyx.acl.service.impl;


import com.atguigu.ssyx.acl.mapper.AdminMapper;
import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public IPage<Admin> selectAdminPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo) {
        //获取条件值,用户名和昵称
        String username = adminQueryVo.getUsername();
        String name = adminQueryVo.getName();
        //创建mp条件对象
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        //判断条件值是否为空
        if(!StringUtils.isEmpty(username)){
            //第一个参数 Admin::getUsername 是一个 Lambda 表达式，表示数据库表中对应的字段 username。
            //第二个参数 userName 是一个变量，表示用户提供的值
            //SELECT * FROM admin WHERE username = '用户名';
            wrapper.eq(Admin::getUsername, username);
        }
        if(!StringUtils.isEmpty(name)){
            //SELECT * FROM admin WHERE name = '昵称';
            wrapper.like(Admin::getName, name);
        }
        IPage<Admin> adminPage = baseMapper.selectPage(pageParam, wrapper);
        return adminPage;
    }
}
