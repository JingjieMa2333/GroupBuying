package com.atguigu.ssyx.product.controller;


import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.product.service.CategoryService;
import com.atguigu.ssyx.vo.product.CategoryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品三级分类 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2025-01-06
 */
@RestController
@RequestMapping("/admin/product/category")
@CrossOrigin
@Api(tags = "商品分类接口" )
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //商品分类列表
    @ApiOperation("商品分类列表")
    @GetMapping("{page}/{limit}")
    public Result getCategory(@PathVariable Long page,
                              @PathVariable Long limit,
                              CategoryVo categoryVo) {
        Page<Category> pageParam = new Page<>(page, limit);
        IPage<Category> pageModel = categoryService.selectPage(pageParam,categoryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取商品分类信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return Result.ok(category);
    }

    @ApiOperation(value = "新增商品分类")
    @PostMapping("save")
    public Result save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改商品分类")
    @PutMapping("update")
    public Result updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除商品分类")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除商品分类")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        categoryService.removeByIds(idList);
        return Result.ok(null);
    }

    //      url: `${api_name}/findAllList`,
    //      method: 'get'
    //查询所有商品分类
    @ApiOperation("查询所有商品分类")
    @GetMapping("findAllList")
    public Result findAllList() {
        //相当于 SELECT * FROM category WHERE is_deleted = 0
        List<Category> list = categoryService.list();
        return Result.ok(list);
    }

}

