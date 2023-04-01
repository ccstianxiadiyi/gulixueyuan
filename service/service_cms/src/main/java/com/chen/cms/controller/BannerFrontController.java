package com.chen.cms.controller;

import com.chen.cms.service.CrmBannerService;
import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cmsservice/front")
@Api(tags="轮播图前台")
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

    @GetMapping("/getall")
    @ApiOperation("获取所有轮播图数据")
    public R getAllData(){
        return R.ok().data("lists",crmBannerService.list(null));
    }
}
