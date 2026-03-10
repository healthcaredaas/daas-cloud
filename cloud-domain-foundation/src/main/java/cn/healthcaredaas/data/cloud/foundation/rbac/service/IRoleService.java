package cn.healthcaredaas.data.cloud.foundation.rbac.service;


import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRoleResource;

import java.util.List;
import java.util.Set;

/**
 *
 * @author chenpan
 * @date 2020/6/11 13:39
 */
public interface IRoleService {

    List<RbacRole> findUserRoles(String userId);

    Set<String> findUserRoleIds(String userId);

    List<RbacRoleResource> getRoleResources(String roleId);

    void updateRoleResources(String roleId, List<String> resourceIds);
}

