package com.atguigu.ssyx.sys.service.impl;

import com.atguigu.ssyx.common.exception.SsyxException;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.model.sys.RegionWare;
import com.atguigu.ssyx.sys.mapper.RegionWareMapper;
import com.atguigu.ssyx.sys.service.RegionWareService;
import com.atguigu.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 城市仓库关联表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2025-01-05
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    //开通区域列表方法
    @Override
    public IPage<RegionWare> selectAll(Page<RegionWare> pages, RegionWareQueryVo regionWareQueryVo) {
        //1 获取查询条件值
        String keyword = regionWareQueryVo.getKeyword();
        //2 条件判断是否为空
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<RegionWare>();
        if(!StringUtils.isEmpty(keyword)){
            //根据区域名称或者仓库名称查询
            wrapper.like(RegionWare::getRegionName,keyword).or().like(RegionWare::getWareName,keyword);
        }
        Page<RegionWare> regionWarePage = baseMapper.selectPage(pages, wrapper);
        return regionWarePage;
    }


    //添加开通区域
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        //判断区域是否开通
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        //构造一个查询条件:WHERE region_id = <regionWare.getRegionId() 的值>;
        wrapper.eq(RegionWare::getRegionId,regionWare.getRegionId());
        //统计满足条件的记录总数
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            //开通过了则抛出设定好的异常
            throw new SsyxException(ResultCodeEnum.REGION_OPEN);
        }else{
            baseMapper.insert(regionWare);
        }
    }

    //取消开通区域
    @Override
    public void updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            throw new IllegalArgumentException("参数 id 或 status 不能为空");
        }
        RegionWare regionWare = baseMapper.selectById(id);
        if (regionWare == null) {
            throw new NullPointerException("未找到对应的 RegionWare 对象，id = " + id);
        }
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);

    }
}
