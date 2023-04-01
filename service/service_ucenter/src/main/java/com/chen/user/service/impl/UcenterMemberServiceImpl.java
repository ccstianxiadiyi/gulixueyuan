package com.chen.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.commonutils.GuliException;
import com.chen.commonutils.JwtUtils;
import com.chen.user.entity.UcenterMember;
import com.chen.user.entity.vo.RegisterVo;
import com.chen.user.mapper.UcenterMemberMapper;
import com.chen.user.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-28
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Override
    public String login(UcenterMember ucenterMember) {
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
        Boolean isDisabled = ucenterMember.getIsDisabled();
        if(!StringUtils.hasLength(mobile)||!StringUtils.hasLength(password)){
            throw new GuliException(20001,"登陆失败");
        }

        LambdaQueryWrapper<UcenterMember> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UcenterMember::getMobile,ucenterMember.getMobile());
        queryWrapper.eq(UcenterMember::getPassword,ucenterMember.getPassword());
        UcenterMember one = getOne(queryWrapper);
        if(one==null){
            throw new GuliException(20001,"登陆失败");
        }

        String token= JwtUtils.getJwtToken(one.getId(),one.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickName = registerVo.getNickName();
        String password = registerVo.getPassword();
        if(!StringUtils.hasLength(code)){
            throw new GuliException(20001,"注册失败");
        }
        if(!StringUtils.hasLength(mobile)){
            throw new GuliException(20001,"注册失败");
        }

        if(!StringUtils.hasLength(password)){
            throw new GuliException(20001,"注册失败");
        }
        if(!code.equals("1234")){
            throw new GuliException(20001,"注册失败");
        }
        LambdaQueryWrapper<UcenterMember> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UcenterMember::getMobile,mobile);
        Integer integer = baseMapper.selectCount(queryWrapper);
        if(integer>0){
            throw new GuliException(20001,"注册失败");
        }
        UcenterMember member=new UcenterMember();
        member.setMobile(mobile);
        member.setPassword(password);
        member.setNickname(nickName);
        baseMapper.insert(member);

    }

    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.selectRegisterCount(day);
    }




}
