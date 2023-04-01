package com.chen.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.commonutils.R;
import com.chen.order.client.CourseClient;
import com.chen.order.client.UserClinet;
import com.chen.order.entity.TOrder;
import com.chen.order.mapper.TOrderMapper;
import com.chen.order.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.order.utils.HttpClient;
import com.chen.order.utils.OrderNoUtil;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-31
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
    @Autowired
    private CourseClient courseClient;
    @Autowired
    private UserClinet userClinet;

    @Override
    public String createOrders(String courseId, String memberIdByJwtToken) {
        R chapter = courseClient.getChapter(courseId);
        R info = userClinet.getInfo(memberIdByJwtToken);
        Map<String, Object> data = chapter.getData();

        String s = JSON.toJSONString(data.get("courseWebVo"));
        Map<String, Object> data1 = info.getData();
        String s1 = JSON.toJSONString(data1.get("user"));

//        String s = (String) chapter.getData().get("courseWebVo");
//        String s1 = (String) info.getData().get("user");
        JSONObject courseInfos = JSON.parseObject(s);
        JSONObject userInfos = JSON.parseObject(s1);
        /*
         * 远程调用 用户服务 & 课程服务 获取信息
         * */
        String courseTitle = courseInfos.get("title").toString();
        String courseCover = courseInfos.get("cover").toString();
        String userId = userInfos.get("id").toString();
        String nickname = userInfos.get("nickname").toString();
        String mobile = userInfos.get("mobile").toString();
        Double price = Double.valueOf(courseInfos.get("price").toString());
        BigDecimal finalPrice = BigDecimal.valueOf(price);
        String teacherName = courseInfos.get("teacherName").toString();
        /*
         * 新建订单
         * */
        TOrder order = new TOrder();
        String orderNo = OrderNoUtil.getOrderNo();
        order.setOrderNo(orderNo);
        order.setCourseTitle(courseTitle);
        order.setCourseCover(courseCover);
        order.setMemberId(userId);
        order.setNickname(nickname);
        order.setMobile(mobile);
        order.setPayType(1);
        order.setTotalFee(finalPrice);
        order.setTeacherName(teacherName);
        order.setCourseTitle(courseTitle);
        order.setPayType(0);
        baseMapper.insert(order);
        return orderNo;
    }

    @Override
    public Map<String, String> getStatu(String orderNo) {

        try {
        //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
        //2、设置请求
            HttpClient client = new
                    HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
        //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
        //6、转成Map
        //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
