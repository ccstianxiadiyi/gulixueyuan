package com.chen.statistics.mapper;

import com.chen.statistics.entity.Daily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 网站统计日数据 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
public interface DailyMapper extends BaseMapper<Daily> {

    Integer selectRegisterCount(String day);
}
