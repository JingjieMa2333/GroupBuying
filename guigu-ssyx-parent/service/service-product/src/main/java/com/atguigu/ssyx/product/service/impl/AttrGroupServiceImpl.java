package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.AttrGroup;
import com.atguigu.ssyx.product.mapper.AttrGroupMapper;
import com.atguigu.ssyx.product.service.AttrGroupService;
import com.atguigu.ssyx.vo.product.AttrGroupQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 属性分组 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {


    @Override
    public IPage<AttrGroup> selectPage(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo) {
        String name = attrGroupQueryVo.getName();
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.like(AttrGroup::getName, name);
        }
        IPage<AttrGroup> page =  baseMapper.selectPage(pageParam,wrapper);
        return page;
    }

    @Override
    public List<AttrGroup> findAllList() {

        //LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        //wrapper.orderByDesc(AttrGroup::getId);
        QueryWrapper<AttrGroup> wrapper = new QueryWrapper<>();
        //可以直接通过字段名来生成sql语句
        wrapper.orderByDesc("id");
        List<AttrGroup> attrGroups = baseMapper.selectList(wrapper);
        return attrGroups;
    }


}
