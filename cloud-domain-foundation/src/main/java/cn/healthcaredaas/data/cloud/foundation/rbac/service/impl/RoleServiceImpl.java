package cn.healthcaredaas.data.cloud.foundation.rbac.service.impl;

import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacRoleResourceDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRoleResource;
import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacRoleDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRoleService;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**

 * @ClassName： RoleServiceImpl.java
 * @Author： chenpan
 * @Date：2024/12/1 15:55
 * @Modify：
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RbacRoleDao, RbacRole>
        implements IRoleService {

    @Autowired
    private RbacRoleDao rbacRoleDao;

    @Autowired
    private RbacRoleResourceDao roleResourceDao;

    @Override
    public List<RbacRole> findUserRoles(String userId) {

        return rbacRoleDao.selectUserRole(userId);
    }

    @Override
    public Set<String> findUserRoleIds(String userId) {
        List<RbacRole> authorities = rbacRoleDao.selectUserRole(userId);
        return authorities.stream().map(RbacRole::getRoleCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
    }

    @Override
    public List<RbacRoleResource> getRoleResources(String roleId) {
        LambdaQueryWrapper<RbacRoleResource> queryWrapper = Wrappers.lambdaQuery(RbacRoleResource.class);
        queryWrapper.eq(RbacRoleResource::getRoleId, roleId);
        return roleResourceDao.selectList(queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void updateRoleResources(String roleId, List<String> resourceIds) {
        LambdaQueryWrapper<RbacRoleResource> queryWrapper = Wrappers.lambdaQuery(RbacRoleResource.class);
        queryWrapper.eq(RbacRoleResource::getRoleId, roleId);
        roleResourceDao.delete(queryWrapper);

        if (resourceIds != null) {
            List<RbacRoleResource> roleResourceList = new ArrayList<>();
            for (String resourceId : resourceIds) {
                if(resourceId==null){
                    continue;
                }
                RbacRoleResource roleResource = new RbacRoleResource();
                roleResource.setResourceId(resourceId);
                roleResource.setRoleId(roleId);
                roleResourceList.add(roleResource);
            }
            roleResourceDao.insert(roleResourceList);
        }
    }
}
