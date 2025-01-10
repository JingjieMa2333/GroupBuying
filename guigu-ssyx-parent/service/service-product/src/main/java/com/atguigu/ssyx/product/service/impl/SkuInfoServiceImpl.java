package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.SkuAttrValue;
import com.atguigu.ssyx.model.product.SkuImage;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.model.product.SkuPoster;
import com.atguigu.ssyx.product.mapper.SkuInfoMapper;
import com.atguigu.ssyx.product.service.SkuAttrValueService;
import com.atguigu.ssyx.product.service.SkuImageService;
import com.atguigu.ssyx.product.service.SkuInfoService;
import com.atguigu.ssyx.product.service.SkuPosterService;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    //查询sku列表
    @Override
    public IPage<SkuInfo> selectPage(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoVo) {
        String keyword = skuInfoVo.getKeyword();
        String skuType = skuInfoVo.getSkuType();
        Long categoryId = skuInfoVo.getCategoryId();
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            wrapper.like(SkuInfo::getSkuName, keyword);
        }
        if(!StringUtils.isEmpty(categoryId)){
            wrapper.eq(SkuInfo::getCategoryId, categoryId);
        }
        if(!StringUtils.isEmpty(skuType)){
            wrapper.like(SkuInfo::getSkuType, skuType);
        }
        Page<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, wrapper);
        return skuInfoPage;
    }


    //新增sku信息
    //每一个表对应一个实现类
    @Autowired
    //sku图片
    private SkuImageService skuImageService;
    @Autowired
    //sku平台属性
    private SkuAttrValueService skuAttrValueService;
    //sku宣传海报
    @Autowired
    private SkuPosterService skuPosterService;
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        //1 添加sku基本信息

        SkuInfo skuInfo = new SkuInfo();
        //复制属性
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        baseMapper.insert(skuInfo);

        //2 保存sku海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(!CollectionUtils.isEmpty(skuPosterList)){
            //每个海报对象中添加sku_id
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuInfo.getId());
            }
            //保存到海报那张表里
            skuPosterService.saveBatch(skuPosterList);
        }
        //3 保存sku图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImagesList)){
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(skuInfo.getId());
            }
            //保存到商品图片那张表里
            skuImageService.saveBatch(skuImagesList);
        }
        //4 保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    //根据id获取sku信息
    @Override
    public SkuInfoVo getSkuInfo(Long id) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        //1 根据id查询基本信息
        SkuInfo skuInfo = baseMapper.selectById(id);
        //2 根据id查询图片列表
         List<SkuImage> skuImageList =skuImageService.getImageListById(id);
        //3 根据id查询海报列表
        List<SkuPoster> skuPosterList = skuPosterService.getPosterListById(id);
        //4 根据id查询属性列表
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.getAttrValueListById(id);
        //封装信息
        BeanUtils.copyProperties(skuInfo,skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    //修改sku信息
    @Override
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        //修改sku基本信息
        baseMapper.updateById(skuInfo);
        //修改海报信息
        Long id = skuInfoVo.getId();
        //1 先删除海报
        LambdaQueryWrapper<SkuPoster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId, id);
        skuPosterService.remove(wrapper);
        //2 再添加新的海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(!CollectionUtils.isEmpty(skuPosterList)){
            //每个海报对象中添加sku_id
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(id);
            }
            //保存到海报那张表里
            skuPosterService.saveBatch(skuPosterList);
        }
        //修改图片信息
        LambdaQueryWrapper<SkuImage> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(SkuImage::getSkuId, id);
        skuImageService.remove(wrapper2);
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImagesList)){
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(id);
            }
            //保存到商品图片那张表里
            skuImageService.saveBatch(skuImagesList);
        }
        //修改属性
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, id));
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(id);
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    //商品审核,更新状态
    @Override
    public void check(Long id, Integer status) {
        SkuInfo skuInfo = baseMapper.selectById(id);
        skuInfo.setCheckStatus(status);
        baseMapper.updateById(skuInfo);
    }

    //商品上架
    @Override
    public void publish(Long id, Integer status) {
        SkuInfo skuInfo = baseMapper.selectById(id);
        skuInfo.setPublishStatus(status);
        baseMapper.updateById(skuInfo);
    }

    @Override
    public void isNewPerson(Long skuId, Integer status) {
        if(skuId == 1){
            SkuInfo skuInfo = new SkuInfo();
            skuInfo.setId(skuId);
            skuInfo.setIsNewPerson(status);
            baseMapper.updateById(skuInfo);
            //todo
        }else{
            SkuInfo skuInfo = new SkuInfo();
            skuInfo.setId(skuId);
            skuInfo.setIsNewPerson(status);
            baseMapper.updateById(skuInfo);
            //todo
        }

    }
}
