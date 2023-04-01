package com.chen.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.commonutils.GuliException;
import com.chen.commonutils.JwtUtils;
import com.chen.user.entity.UcenterMember;
import com.chen.user.service.UcenterMemberService;
import com.chen.user.utils.ConstantWxUtils;
import com.chen.user.utils.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@Api(tags="微信扫码登录")
@RequestMapping("/api/uceneter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService ucenterMemberService;
    @ApiOperation("前台扫码登录")
    @GetMapping("/login")
    public void genQrConnect(HttpSession session, HttpServletResponse httpServletResponse){
        String baseUrl="https://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s"+
                "&redirect_uri=%s"+
                "&response_type=code"+
                "&scope=snsapi_login"+
                "&state=%s"+
                "#wechat_redirect";
        String redirectUrl= ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            URLEncoder.encode(redirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String url=String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );
        try {
            httpServletResponse.sendRedirect(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @ApiOperation("扫码后跳转")
    @GetMapping("/callback")
    public void callback(HttpServletResponse response,String code,String state){
        try {
            String baseAccessTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token"+
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);
            String s = HttpClientUtils.get(accessTokenUrl);
            Gson gson=new Gson();
            HashMap hashMap = gson.fromJson(s, HashMap.class);
            String access_token = (String) hashMap.get("access_token");
            String open_id = (String) hashMap.get("openid");
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl=String.format(
                    baseUserInfoUrl,
                    access_token,
                    open_id
            );
            LambdaQueryWrapper<UcenterMember>queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(UcenterMember::getOpenid,open_id);
            UcenterMember one = ucenterMemberService.getOne(queryWrapper);
            if(one==null){
                String userInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(userInfo);
                HashMap userMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userMap.get("nickname");
                String avator = (String) userMap.get("headimgurl");
                String openid = (String) userMap.get("openid");
                UcenterMember ucenterMember=new UcenterMember();
                ucenterMember.setNickname(nickname);
                ucenterMember.setAvatar(avator);
                ucenterMember.setOpenid(openid);
                ucenterMemberService.save(ucenterMember);
                LambdaQueryWrapper<UcenterMember> queryWrapper1=new LambdaQueryWrapper<>();
                queryWrapper1.eq(UcenterMember::getOpenid,openid);
                one = ucenterMemberService.getOne(queryWrapper1);
            }
            String jwtToken = JwtUtils.getJwtToken(one.getId(), one.getNickname());
            String realUrl="http://localhost:3000?token="+jwtToken;
            response.sendRedirect(realUrl);
        } catch (Exception e) {
            throw new GuliException(20001,"扫码失败");
        }
    }
}
