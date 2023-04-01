package com.chen.statistics.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.commonutils.R;
import com.chen.statistics.clients.UcenterClient;
import com.chen.statistics.entity.Daily;
import com.chen.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
@RestController
@Api(tags="数据统计")
@RequestMapping("/statisticsservice/daily")
public class DailyController {
    @Autowired
    private DailyService dailyService;


    @PostMapping("/registerCount/{day}")
    @ApiOperation("统计某一天注册人数")
    public R getRegisterDataByDay(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String day){
        dailyService.countRegister(day);
        return R.ok();
    }
    @ApiOperation("查询某一天的数据返回")
    @GetMapping("/show-chart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String
            end,@PathVariable String type){
        QueryWrapper<Daily> queryWrapper=new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.select("date_calculated",type);
        List<Daily> dayList = dailyService.list(queryWrapper);
        Map<String,Object> map=new HashMap<>();
        List<String> dateList= new ArrayList<>();
        List<Integer> dataList=new ArrayList<>();
        map.put("dataList",dataList);
        map.put("dateList",dateList);
        dayList.stream().map((item)->{
            dateList.add(item.getDateCalculated());
            switch (type){
                case "register_num":
                    dataList.add(item.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(item.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(item.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(item.getCourseNum());
                    break;
                default:
                    break;
            }
            return null;
        }).close();

        return R.ok().data(map);
    }
}

