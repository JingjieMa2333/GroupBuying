package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.product.mapper.CategoryMapper;
import com.atguigu.ssyx.product.service.CategoryService;
import com.atguigu.ssyx.vo.product.CategoryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 商品三级分类 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public IPage<Category> selectPage(Page<Category> pageParam, CategoryVo categoryVo) {
        //获取分类id和名称
        String name = categoryVo.getName();

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        if(!StringUtils.isEmpty(name)){
            wrapper.like(Category::getName, name);
        }
        IPage<Category> categoryIPage = baseMapper.selectPage(pageParam, wrapper);
        return categoryIPage;


    }
}
