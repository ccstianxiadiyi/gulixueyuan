package com.chen.user.service;

import com.chen.user.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.user.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-28
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    Integer countRegisterByDay(String day);

}
