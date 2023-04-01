package com.chen.edu.controller;

import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags="用户相关模块")
@RestController
@RequestMapping("/edu/user")
public class EduLoginController {

    @PostMapping("/login")
    @ApiOperation("用户登录传回token")
    public R login(){
        return R.ok().data("token","admin");
    }


    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://ts4.cn.mm.bing.net/th?id=OIP-C.MO9kKuliwR3AlBcVAxgsTgHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2");
    }

}
