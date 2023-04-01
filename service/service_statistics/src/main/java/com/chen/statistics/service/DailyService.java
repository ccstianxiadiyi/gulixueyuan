package com.chen.statistics.service;

import com.chen.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
public interface DailyService extends IService<Daily> {


    void countRegister(String day);


}
