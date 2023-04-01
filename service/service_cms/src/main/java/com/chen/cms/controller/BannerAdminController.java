package com.chen.cms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.cms.entity.CrmBanner;
import com.chen.cms.service.CrmBannerService;
import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-27
 */
@RestController
@RequestMapping("/cmsservice/banner")
@Api(tags = "轮播图管理")
public class BannerAdminController {
    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation("分页查询轮播图")
    @GetMapping("/page/{currentPage}/{pageSize}")
    public R getPage(@PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<CrmBanner> page = new Page<>(currentPage, pageSize);
        crmBannerService.page(page, null);
        List<CrmBanner> records = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("total", total).data("items", records);
    }

    @ApiOperation("增加")
    @PostMapping("/add")
    public R addBanner(@RequestBody CrmBanner crmBanner) {
        boolean save = crmBannerService.save(crmBanner);
        return save ? R.ok() : R.error();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除轮播图")
    public R delete(@PathVariable String id) {
        boolean b = crmBannerService.removeById(id);
        return b ? R.ok() : R.error();
    }

    @GetMapping("/getOne/{id}")
    @ApiOperation("根据id获取轮播图数据")
    public R getOneBanner(@PathVariable String id) {
        CrmBanner banner = crmBannerService.getById(id);
        return banner != null ? R.ok().data("item", banner) : R.error();
    }
    @PutMapping("/edit/{id}")
    @ApiOperation("编辑轮播图")
    public R editBanner(@RequestBody CrmBanner crmBanner,@PathVariable String id){
        crmBanner.setId(id);
        boolean b = crmBannerService.updateById(crmBanner);
        return b?R.ok():R.error();
    }
}

