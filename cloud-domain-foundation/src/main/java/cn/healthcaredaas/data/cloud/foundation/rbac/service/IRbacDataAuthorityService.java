package cn.healthcaredaas.data.cloud.foundation.rbac.service;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacDataAuthority;

import java.util.List;
import java.util.Set;

/**

 * @ClassName： IRbacDataAuthorityService.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/7/20 16:11
 * @Modify：
 */
public interface IRbacDataAuthorityService {

    List<RbacDataAuthority> getUserDataAuth(String userId);

    List<RbacDataAuthority> getRoleDataAuth(String roleId);

    Set<String> getUserAndRoleDataAuth(String userId);
}
