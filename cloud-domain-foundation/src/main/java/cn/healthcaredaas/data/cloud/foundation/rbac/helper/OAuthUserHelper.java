package cn.healthcaredaas.data.cloud.foundation.rbac.helper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.healthcaredaas.data.cloud.foundation.rbac.dto.RbacUserDTO;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.enums.AccountStatusEnum;
import cn.healthcaredaas.data.cloud.security.core.entity.OAuth2GrantedAuthority;
import cn.healthcaredaas.data.cloud.security.core.entity.OAuthUser;
import cn.healthcaredaas.data.cloud.security.core.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**

 * @ClassName： OAuthUserHelper.java
 * @Author： chenpan
 * @Date：2024/12/7 15:24
 * @Modify：
 */
public class OAuthUserHelper {

    public static OAuthUser convertRbacUser2OAuthUser(RbacUserDTO user) {
        Set<OAuth2GrantedAuthority> authorities = new HashSet<>();
        Set<String> roles = new HashSet<>();
        for (RbacRole role : user.getRoles()) {
            roles.add(role.getRoleCode());
            authorities.add(new OAuth2GrantedAuthority(SecurityUtils.wellFormRolePrefix(role.getRoleCode())));
        }
        roles.add(SecurityUtils.DEFAULT_ROLE);
        authorities.add(new OAuth2GrantedAuthority(SecurityUtils.wellFormRolePrefix(SecurityUtils.DEFAULT_ROLE)));

        List<RbacResource> rbacAuthorities = user.getAuthorities();
        if (CollectionUtil.isNotEmpty(rbacAuthorities)) {
            rbacAuthorities.forEach(rbacAuthority -> authorities
                    .add(new OAuth2GrantedAuthority(rbacAuthority.getResourceCode())));
        }

        return new OAuthUser(String.valueOf(user.getId()), user.getUsername(), user.getPassword(),
                isEnabled(user),
                isAccountNonExpired(user),
                isCredentialsNonExpired(user),
                isNonLocked(user),
                authorities, roles);
    }

    private static boolean isEnabled(RbacUserDTO user) {
        return user.getStatus() == AccountStatusEnum.VALID;
    }

    private static boolean isNonLocked(RbacUserDTO user) {
        return user.getStatus() != AccountStatusEnum.LOCKED;
    }

    private static boolean isNonExpired(LocalDateTime localDateTime) {
        if (ObjectUtil.isEmpty(localDateTime)) {
            return true;
        } else {
            return localDateTime.isAfter(LocalDateTime.now());
        }
    }

    private static boolean isAccountNonExpired(RbacUserDTO user) {
        if (user.getStatus() == AccountStatusEnum.EXPIRED) {
            return false;
        }

        return isNonExpired(user.getAccountExpireAt());
    }

    private static boolean isCredentialsNonExpired(RbacUserDTO user) {
        return isNonExpired(user.getCredentialsExpireAt());
    }
}
