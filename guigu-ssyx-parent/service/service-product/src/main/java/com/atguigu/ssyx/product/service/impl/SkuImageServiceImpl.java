package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.SkuImage;
import com.atguigu.ssyx.product.mapper.SkuImageMapper;
import com.atguigu.ssyx.product.service.SkuImageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品图片 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {

    //根据id查询图片列表
    @Override
    public List<SkuImage> getImageListById(Long id) {
        LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getId, id);
        List<SkuImage> skuImages = baseMapper.selectList(wrapper);
        return skuImages;
    }
}
