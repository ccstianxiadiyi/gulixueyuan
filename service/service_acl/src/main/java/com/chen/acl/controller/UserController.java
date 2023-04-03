package com.chen.acl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.acl.entity.User;
import com.chen.acl.entity.UserRole;
import com.chen.acl.service.UserRoleService;
import com.chen.acl.service.UserService;
import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-04-03
 */
@RestController
@Api(tags = "后台用户管理")
@RequestMapping("/acl/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/getAllAdmin/{currentPage}/{pageSize}")
    @ApiOperation("分页加条件获取后台所有管理用户")
    public R getAllAdmin(@PathVariable Integer currentPage, @PathVariable Integer pageSize, @RequestBody(required = false) User user) {
        Page<User> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(user.getNickName()), User::getNickName, user.getNickName());
        userService.page(page, queryWrapper);
        List<User> records = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("userLists", records).data("total", total);
    }

    @PostMapping("/addUser")
    @ApiOperation("新增管理用户")
    public R addUser(@RequestBody User user) {
        boolean save = userService.save(user);
        return save ? R.ok() : R.error();
    }
    @ApiOperation("数据回显根据id查询用户")
    @GetMapping("/getOne/{id}")
    public R getOneAdmin(@PathVariable String id){
        User user = userService.getById(id);
        return user==null ? R.error().message("用户不存在") : R.ok().data("item",user);
    }
    @DeleteMapping("/delete/{id}")
    @ApiOperation("根据id删除用户")
    public R deleteUser(@PathVariable String id){
        boolean b = userService.removeById(id);
        return b?R.ok():R.error();
    }
    @DeleteMapping("/deleteMore")
    @ApiOperation("根据id批量删除用户")
    public R deleteByLists(@RequestBody List<String> ids){
        boolean b = userService.removeByIds(ids);
        return b?R.ok():R.error();
    }
    @PostMapping("/editAdmin/{id}")
    @ApiOperation("编辑用户")
    public R editAdmin(@RequestBody User user,@PathVariable String id){
        user.setId(id);
        boolean b = userService.updateById(user);
        return b?R.ok():R.error();
    }
    @GetMapping("/getRole/{userId}")
    @ApiOperation("根据用户id查询对应的角色")
    public R getRoleByUserId(@PathVariable String userId){
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,userId);
        List<UserRole> roles = userRoleService.list(queryWrapper);
        return R.ok().data("roles",roles);
    }
    @PostMapping("giveRole/{userId}")
    @ApiOperation("给用户分配对应的角色")
    public R giveRole(@PathVariable String userId,@RequestParam String[] roleId){
        UserRole userRole=new UserRole();
        for (String s : roleId) {
            userRole.setRoleId(s);
            userRole.setUserId(userId);
            userRoleService.save(userRole);
        }
        return R.ok();
    }

}

