package com.chen.acl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.acl.entity.Permission;
import com.chen.acl.entity.RolePermission;
import com.chen.acl.service.PermissionService;
import com.chen.acl.service.RolePermissionService;
import com.chen.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-04-03
 */
@RestController
@Api(tags="后台权限管理")
@RequestMapping("/acl/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;


    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public R save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public R updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }
    //获取全部菜单
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public R indexAllPermission() {
        List<Permission> list =  permissionService.queryAllMenu();
        return R.ok().data("children",list);
    }
    @DeleteMapping("/remove/{id}")
    @ApiOperation("递归删除菜单")
    public R remove(@PathVariable String id){
        permissionService.removeChildById(id);
        return R.ok();
    }
    @PostMapping("/givePermission")
    @ApiOperation("给角色分配权限")
    public R givePermission(String roleId,String[] permissionId){
        permissionService.saveRolePermissionRelation(roleId,permissionId);
        return R.ok();
    }

}

