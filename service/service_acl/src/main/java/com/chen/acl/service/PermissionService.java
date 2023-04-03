package com.chen.acl.service;

import com.chen.acl.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-04-03
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> queryAllMenu();

    void removeChildById(String id);

    void saveRolePermissionRelation(String roleId, String[] permissionId);
}
