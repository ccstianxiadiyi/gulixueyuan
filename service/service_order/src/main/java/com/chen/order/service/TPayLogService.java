package com.chen.order.service;

import com.chen.order.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
public interface TPayLogService extends IService<TPayLog> {

    Map createPhoto(String orderId);

    void updateOrderStatus(Map<String,String> map);
}
