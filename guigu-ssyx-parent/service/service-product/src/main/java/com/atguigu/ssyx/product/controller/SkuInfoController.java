package com.atguigu.ssyx.product.controller;


import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.product.service.SkuInfoService;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
@RestController
@RequestMapping("/admin/product/skuInfo")
@CrossOrigin
@Api(tags = "sku接口" )
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation("sku列表")
    @GetMapping("{page}/{limit}")
    public Result page(@PathVariable int page,
                       @PathVariable int limit,
                       SkuInfoQueryVo skuInfoVo) {
        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> pageResult = skuInfoService.selectPage(pageParam, skuInfoVo);
        return Result.ok(pageResult);
    }

    @ApiOperation("新增sku信息")
    @PostMapping("save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation("根据id获取sku信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SkuInfoVo skuInfoVo= skuInfoService.getSkuInfo(id);
        return Result.ok(skuInfoVo);
    }

    @ApiOperation("修改sku信息")
    @PutMapping("update")
    public Result update(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据一个id删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        skuInfoService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        skuInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation("商品审核")
    @GetMapping("check/{id}/{status}")
    public Result check(@PathVariable Long id, @PathVariable Integer status) {
        skuInfoService.check(id,status);
        return Result.ok(null);
    }

    @ApiOperation("商品上架")
    @GetMapping("publish/{id}/{status}")
    public Result publish(@PathVariable Long id, @PathVariable Integer status) {
        skuInfoService.publish(id,status);
        return Result.ok(null);
    }

    @ApiOperation("新人专享")
    @GetMapping("isNewPerson/{skuId}/{status}")
    public Result isNewPerson(@PathVariable("skuId") Long skuId,
                              @PathVariable("status") Integer status) {
        skuInfoService.isNewPerson(skuId, status);
        return Result.ok(null);
    }

}

