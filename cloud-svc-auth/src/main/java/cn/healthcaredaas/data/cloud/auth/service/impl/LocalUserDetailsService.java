package cn.healthcaredaas.data.cloud.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.healthcaredaas.data.cloud.auth.service.OAuth2UserDetailsService;
import cn.healthcaredaas.data.cloud.foundation.rbac.dto.RbacUserDTO;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import cn.healthcaredaas.data.cloud.foundation.rbac.helper.OAuthUserHelper;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRoleService;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IUserService;
import cn.healthcaredaas.data.cloud.security.core.entity.AccessPrincipal;
import cn.healthcaredaas.data.cloud.security.core.entity.OAuthUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**

 * @ClassName： LocalUserDetailsService.java
 * @Author： chenpan
 * @Date：2024/12/7 13:36
 * @Modify：
 */
@Service
public class LocalUserDetailsService implements OAuth2UserDetailsService {

    @Resource
    private IUserService userService;

    @Resource
    private IRoleService roleService;

    /**
     * 通过社交集成的唯一id，获取用户信息
     * <p>
     * 如果是短信验证码，openId就是手机号码
     *
     * @param source          社交集成提供商类型
     * @param accessPrincipal 社交登录提供的相关信息
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal) throws AuthenticationException {
        return null;
    }

    /**
     * 系统用户名
     *
     * @param username 用户账号
     * @return {@link OAuthUser}
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public OAuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        RbacUser rbacUser = userService.findUser(username);
        return convertUser(rbacUser);
    }

    /**
     * 系统用户名
     *
     * @param phone    手机号
     * @return {@link OAuthUser}
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public OAuthUser loadUserByPhone(String phone) throws UsernameNotFoundException {
        RbacUser rbacUser = userService.findUserByPhone(phone);
        return convertUser(rbacUser);
    }

    private OAuthUser convertUser(RbacUser rbacUser) {
        if (ObjectUtil.isEmpty(rbacUser)) {
            throw new UsernameNotFoundException("用户不存在!");
        }
        List<RbacRole> roles = roleService.findUserRoles(rbacUser.getId());
        RbacUserDTO user = new RbacUserDTO();
        BeanUtil.copyProperties(rbacUser, user);
        user.setRoles(roles);
        return OAuthUserHelper.convertRbacUser2OAuthUser(user);
    }
}