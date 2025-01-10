package com.atguigu.ssyx.product.service;

import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
public interface SkuInfoService extends IService<SkuInfo> {

    IPage<SkuInfo> selectPage(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoVo);

    void saveSkuInfo(com.atguigu.ssyx.vo.product.SkuInfoVo skuInfoVo);

    SkuInfoVo getSkuInfo(Long id);

    void updateSkuInfo(SkuInfoVo skuInfoVo);

    void check(Long id, Integer status);

    void publish(Long id, Integer status);

    void isNewPerson(Long skuId, Integer status);
}
