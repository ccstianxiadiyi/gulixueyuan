package com.chen.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.commonutils.GuliException;
import com.chen.commonutils.R;
import com.chen.order.entity.TOrder;
import com.chen.order.service.TOrderService;
import com.chen.order.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 * 支付日志表
 * @author ccs
 * @since 2023-03-31
 */
@RestController
@Api(tags="支付功能")
@RequestMapping("/orderservice/pay")
public class TPayLogController {
    @Autowired
    private TOrderService orderService;
    @Autowired
    private TPayLogService tPayLogService;


    @GetMapping("/getPhoto/{orderId}")
    @ApiOperation("生成微信支付二维码")
    public R getPhoto(@PathVariable String orderId){
        Map map=tPayLogService.createPhoto(orderId);
        return R.ok().data(map);
    }

    @GetMapping("/getStatu/{orderNo}")
    @ApiOperation("查询用户支付状态")
    public R searchStatu(@PathVariable String orderNo){

        Map<String,String> map = orderService.getStatu(orderNo);
        if(map==null){
            return R.error().message("支付出错了");
        }
       if(map.get("trade_state").equals("SUCCESS")){
           tPayLogService.updateOrderStatus(map);
           return R.ok();
       }
       return R.ok().message("支付中");
    }

}

