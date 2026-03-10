package cn.healthcaredaas.data.cloud.foundation.rbac.service;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;

import java.util.List;
import java.util.Map;

/**

 * @ClassName： IRbacResourceService.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/13 15:50
 * @Modify：
 */
public interface IRbacResourceService {

    /**
     * 获取可访问资源的角色列表
     *
     * @param url
     * @param method
     * @return
     */
    List<String> resourceRoles(String url, String method);

    /**
     * 获取角色api资源
     *
     * @param role
     * @return
     */
    Map<String, String> getApiResources(String role);

    /**
     * 获取用户应用菜单
     *
     * @param appIds
     * @param userId
     * @return
     */
    List<RbacResource> getMenus(String[] appIds, String userId);
}
