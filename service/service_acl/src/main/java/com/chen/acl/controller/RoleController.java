package com.chen.acl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.acl.entity.Role;
import com.chen.acl.service.RoleService;
import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-04-03
 */
@RestController
@Api(tags="后台角色管理")
@RequestMapping("/acl/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/getAll/{currentPage}/{pageSize}")
    @ApiOperation("分页加条件获取角色列表")
    public R getRoleByCondition(@PathVariable Integer currentPage, @PathVariable Integer pageSize, @RequestBody(required = false) Role role){
        Page<Role> page=new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(role.getRoleName()),Role::getRoleName,role.getRoleName());
        roleService.page(page,queryWrapper);
        List<Role> records = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("roles",records).data("total",total);
    }

    @GetMapping("/getOne/{id}")
    @ApiOperation("数据回显根据id获取角色")
    public R getOne(@PathVariable String id){
        Role role = roleService.getById(id);
        return R.ok().data("role",role);
    }
    @PostMapping("/add")
    @ApiOperation("新增角色")
    public R addRole(@RequestBody Role role){
        roleService.save(role);
        return R.ok();
    }
    @PostMapping("/edit/{id}")
    @ApiOperation("编辑角色")
    public R editRole(@RequestBody Role role,@PathVariable String id){
        role.setId(id);
        roleService.updateById(role);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除角色")
    public R deleteRole(@PathVariable String id){
        roleService.removeById(id);
        return R.ok();
    }
    @DeleteMapping("/deleteMore")
    @ApiOperation("批量删除角色")
    public R deleteRoles(@PathVariable List<String> ids){
        roleService.removeByIds(ids);
        return R.ok();
    }

}

