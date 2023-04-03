package com.chen.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.acl.entity.Permission;
import com.chen.acl.entity.RolePermission;
import com.chen.acl.mapper.PermissionMapper;
import com.chen.acl.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.acl.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-04-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private RolePermissionService rolePermissionService;
    /*
    * 查询所有菜单
    * */
    @Override
    public List<Permission> queryAllMenu() {
        LambdaQueryWrapper<Permission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Permission::getId);
        List<Permission> permissions = baseMapper.selectList(queryWrapper);
        List<Permission> list=buildPermission(permissions);
        return list;
    }



    private List<Permission> buildPermission(List<Permission> permissions) {
        List<Permission> list=new ArrayList<>();
        for (Permission permission : permissions) {
            if(permission.getPid().equals("0")){
                permission.setLevel(1);
                list.add(selectChildren(permission,permissions));
            }
        }
        return list;
    }

    private Permission selectChildren(Permission permission, List<Permission> permissions) {

        permission.setChildren(new ArrayList<Permission>());
        for (Permission it : permissions) {
            if(permission.getId().equals(it.getPid())){
                it.setLevel(permission.getLevel()+1);
                permission.getChildren().add(selectChildren(it,permissions));
            }

        }
        return permission;
    }

    /*
    * 递归删除菜单
    * */

    @Override
    public void removeChildById(String id) {
        List<String> removeIds=new ArrayList<>();
        this.selectChildrenById(id,removeIds);
        removeIds.add(id);
        baseMapper.deleteBatchIds(removeIds);
    }



    private void selectChildrenById(String id, List<String> removeIds) {
        LambdaQueryWrapper<Permission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getPid,id);
        queryWrapper.select(Permission::getId);
        List<Permission> childrenLists=baseMapper.selectList(queryWrapper);
        childrenLists.stream().forEach((item)->{
            removeIds.add(item.getId());
            selectChildrenById(item.getId(),removeIds);
        });
    }
    @Override
    public void saveRolePermissionRelation(String roleId, String[] permissionId) {
        List<RolePermission> list=new ArrayList<>();
        for (String s : permissionId) {
            RolePermission rolePermission=new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(s);
            list.add(rolePermission);
        }
        rolePermissionService.saveBatch(list);
    }
}
