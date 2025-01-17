package com.atguigu.ssyx.sys.controller;


import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.sys.RegionWare;
import com.atguigu.ssyx.sys.service.RegionWareService;
import com.atguigu.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 城市仓库关联表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2025-01-05
 */
@Api(tags="开通区域接口")
@RestController
@RequestMapping("/admin/sys/regionWare")
@CrossOrigin
public class RegionWareController {
    @Autowired
    private RegionWareService regionWareService;

    //查询开通的区域列表方法
    /*
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
     */
    @ApiOperation("根据关键字查询开通的区域列表")
    @GetMapping("/{page}/{limit}")
    public Result page(@PathVariable Long page,
                       @PathVariable Long limit,
                       RegionWareQueryVo regionWareQueryVo) {
        Page<RegionWare> pages = new Page<>(page, limit);
        IPage<RegionWare> pageModel =  regionWareService.selectAll(pages,regionWareQueryVo);
        return Result.ok(pageModel);
    }

    //添加开通区域
    /*
      url: `${api_name}/save`,
      method: 'post',
      data: role
    */
    @ApiOperation("添加开通区域")
    @PostMapping("save")
    public Result save(@RequestBody RegionWare regionWare) {
        regionWareService.saveRegionWare(regionWare);
        return Result.ok(null);
    }

    //删除开通区域
    /*
      url: `${api_name}/remove/${id}`,
      method: 'delete'
     */
    @ApiOperation("删除开通区域")
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable Long id) {
        regionWareService.removeById(id);
        return Result.ok(null);
    }

    //取消开通区域
    /*
          url: `${api_name}/updateStatus/${id}/${status}`,
      method: 'post'
     */
    @ApiOperation("取消开通区域")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        regionWareService.updateStatus(id,status);
        return Result.ok(null);
    }
}

