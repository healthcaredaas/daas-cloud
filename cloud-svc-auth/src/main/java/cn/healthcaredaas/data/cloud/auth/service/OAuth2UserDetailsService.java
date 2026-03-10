package cn.healthcaredaas.data.cloud.auth.service;

import cn.healthcaredaas.data.cloud.security.core.entity.AccessPrincipal;
import cn.healthcaredaas.data.cloud.security.core.entity.OAuthUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <pre>自定义认证逻辑</pre>
 *
 * @ClassName： OAuth2UserDetailsService.java
 * @Author： chenpan
 * @Date：2024/12/6 16:49
 * @Modify：
 */
public interface OAuth2UserDetailsService extends UserDetailsService {


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
    UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal) throws AuthenticationException;

    /**
     * 系统用户名
     *
     * @param username 用户账号
     * @return {@link OAuthUser}
     * @throws UsernameNotFoundException 用户不存在
     */
    OAuthUser loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 系统用户名
     *
     * @param phone    手机号
     * @return {@link OAuthUser}
     * @throws UsernameNotFoundException 用户不存在
     */
    OAuthUser loadUserByPhone(String phone) throws UsernameNotFoundException;

}