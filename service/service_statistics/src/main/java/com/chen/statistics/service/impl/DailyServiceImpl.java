package com.chen.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.commonutils.R;
import com.chen.statistics.clients.UcenterClient;
import com.chen.statistics.entity.Daily;
import com.chen.statistics.mapper.DailyMapper;
import com.chen.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {
    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void countRegister(String day) {
        R r = ucenterClient.registerCount(day);
        Map<String, Object> data = r.getData();
        Object count = data.get("count");
        String s = JSON.toJSONString(count);
        JSONObject jsonObject = JSON.parseObject(s);
        int registerSum = Integer.parseInt(jsonObject.get("count").toString());
        Daily daily=new Daily();
        daily.setRegisterNum(registerSum);
        daily.setDateCalculated(day);
        Random random=new Random(1000);
        daily.setCourseNum(random.nextInt());
        daily.setVideoViewNum(random.nextInt());
        daily.setLoginNum(random.nextInt());
        baseMapper.insert(daily);
    }


}
