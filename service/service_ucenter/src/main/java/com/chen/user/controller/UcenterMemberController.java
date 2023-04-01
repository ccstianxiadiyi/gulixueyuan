package com.chen.user.controller;




import com.chen.commonutils.JwtUtils;
import com.chen.commonutils.R;
import com.chen.user.entity.UcenterMember;
import com.chen.user.entity.vo.RegisterVo;
import com.chen.user.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-28
 */
@RestController
@RequestMapping("/userservice/ucentermember")
@Api(tags="前台用户")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    @PostMapping("/login")
    @ApiOperation("前台用户登录")
    public R login(@RequestBody UcenterMember ucenterMember){
        String token=ucenterMemberService.login(ucenterMember);
        return R.ok().data("token",token);
    }

    @PostMapping("/register")
    @ApiOperation("前台用户注册")
    public R register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @GetMapping("/getUser")
    @ApiOperation("根据token获取用户信息")
    public R getUserInfo(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = ucenterMemberService.getById(userId);
        return R.ok().data("user",member);

    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation("根据ID查询用户信息")
    public R getInfo(@PathVariable String id){
        UcenterMember user = ucenterMemberService.getById(id);
        return R.ok().data("user",user);
    }

    @GetMapping(value = "/countregister/{day}")
    public R registerCount(
            @PathVariable String day){
        Integer count = ucenterMemberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }

}

