package cn.healthcaredaas.data.cloud.foundation.rbac.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacDataAuthorityDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacDataAuthority;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRbacDataAuthorityService;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**

 * @ClassName： RbacDataAuthorityServiceImpl.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/7/20 16:11
 * @Modify：
 */
@Service
public class RbacDataAuthorityServiceImpl extends BaseServiceImpl<RbacDataAuthorityDao, RbacDataAuthority>
        implements IRbacDataAuthorityService {

    @Resource
    private RbacDataAuthorityDao dataAuthorityDao;

    @Override
    public List<RbacDataAuthority> getUserDataAuth(String userId) {
        return dataAuthorityDao.selectUserDataAuth(userId);
    }

    @Override
    public List<RbacDataAuthority> getRoleDataAuth(String roleId) {
        return dataAuthorityDao.selectRoleDataAuth(roleId);
    }

    @Override
    public Set<String> getUserAndRoleDataAuth(String userId) {
        List<RbacDataAuthority> authorities = dataAuthorityDao.selectUserAndRoleDataAuth(userId);
        return authorities.stream().map(RbacDataAuthority::getAuthCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
    }
}
