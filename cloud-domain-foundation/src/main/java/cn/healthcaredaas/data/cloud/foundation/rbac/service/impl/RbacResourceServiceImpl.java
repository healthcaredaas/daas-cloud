package cn.healthcaredaas.data.cloud.foundation.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacResourceDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRbacResourceService;
import cn.healthcaredaas.data.cloud.data.core.utils.tree.TreeNode;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import cn.healthcaredaas.data.cloud.web.core.enums.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**

 * @ClassName： RbacResourceServiceImpl.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/13 15:50
 * @Modify：
 */
@Service
public class RbacResourceServiceImpl extends BaseServiceImpl<RbacResourceDao, RbacResource>
        implements IRbacResourceService {

    @Autowired
    private RbacResourceDao authorityDao;


    /**
     * 获取可访问资源的角色列表
     *
     * @param url
     * @param method
     * @return
     */
    @Override
    public List<String> resourceRoles(String url, String method) {
        return authorityDao.findResouceRoles(url, method, ResourceType.API);
    }

    /**
     * 获取角色api资源
     *
     * @param role
     * @return
     */
    @Override
    public Map<String, String> getApiResources(String role) {
        List<RbacResource> authorities = authorityDao.selectRoleResource(role, ResourceType.API);
        if (CollectionUtil.isEmpty(authorities)) {
            return null;
        }
        return authorities.stream()
                .collect(Collectors.toMap(RbacResource::getUrl, RbacResource::getMethodName));
    }

    /**
     * 获取用户应用菜单
     *
     * @param appIds
     * @param userId
     * @return
     */
    @Override
    public List<RbacResource> getMenus(String[] appIds, String userId) {
        ResourceType[] types = new ResourceType[]{ResourceType.MENU};
        List<RbacResource> menus = authorityDao.selectUserResources(appIds, userId, types);
        if (CollectionUtil.isEmpty(menus)) {
            return Collections.emptyList();
        }
        return TreeNode.toTree(menus);
    }

}
