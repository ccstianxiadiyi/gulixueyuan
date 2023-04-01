package com.chen.controller;

import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/servicemsm/message")
@RestController
@Slf4j
@Api(tags="短信管理")
public class MessageController {
    @GetMapping("/get/{iphone}")
    public R getCode(@PathVariable String iphone){
        /*
        * TODO
        * 模拟使用阿里云短信服务发送短息
        * */
        return R.ok().data("code","1234");
    }

}
