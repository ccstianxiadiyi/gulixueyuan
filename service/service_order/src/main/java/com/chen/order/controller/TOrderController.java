package com.chen.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.commonutils.GuliException;
import com.chen.commonutils.JwtUtils;
import com.chen.commonutils.R;
import com.chen.order.entity.TOrder;
import com.chen.order.service.TOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
@RestController
@Api(tags="订单")
@RequestMapping("/orderservice/order")
public class TOrderController {

    @Autowired
    private TOrderService orderService;

    @PostMapping("/createOrder/{courseId}")
    @ApiOperation("创建订单")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        String jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        if(!StringUtils.hasLength(jwtToken)){
            throw new GuliException(20001,"还没有登陆");
        }
        String orderNo=orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderNo);
    }

    @GetMapping("/getone/{orderNo}")
    @ApiOperation("根据订单id查询信息")
    public R getOne(@PathVariable String orderNo){
        LambdaQueryWrapper<TOrder> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TOrder::getOrderNo,orderNo);
        TOrder one = orderService.getOne(queryWrapper);
        return R.ok().data("order",one);
    }

    @GetMapping("isBuyCourse/{courseId}/{memeberId}")
    @ApiOperation("查询用户是否购买课程")
    public Boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memeberId){
        int count = orderService.count(new QueryWrapper<TOrder>().eq("member_id",
                memeberId).eq("course_id", courseId).eq("status", 1));
        if(count>0) {
            return true;
        } else {
            return false;
        }
    }

}

