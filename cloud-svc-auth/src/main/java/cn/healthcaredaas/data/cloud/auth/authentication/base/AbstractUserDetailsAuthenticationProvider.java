package cn.healthcaredaas.data.cloud.auth.authentication.base;

import cn.healthcaredaas.data.cloud.auth.service.OAuth2UserDetailsService;
import cn.healthcaredaas.data.cloud.security.authentication.properties.OAuth2LimitedProperties;
import cn.healthcaredaas.data.cloud.security.authentication.utils.OAuth2EndpointUtils;
import cn.healthcaredaas.data.cloud.core.enums.ResultErrorCodes;
import cn.healthcaredaas.data.cloud.security.core.constants.OAuth2ErrorKeys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

/**
 * <pre>抽象的用户认证Provider</pre>
 *
 * @ClassName： AbstractUserDetailsAuthenticationProvider.java
 * @Author： chenpan
 * @Date：2024/12/7 10:35
 * @Modify：
 */
@Slf4j
public abstract class AbstractUserDetailsAuthenticationProvider extends AbstractAuthenticationProvider {

    public OAuth2UserDetailsService getUserDetailsService() {
        return (OAuth2UserDetailsService) userDetailsService;
    }

    private final UserDetailsService userDetailsService;

    private final OAuth2LimitedProperties limitedProperties;

    @Getter
    @Setter
    private PasswordEncoder passwordEncoder;

    public AbstractUserDetailsAuthenticationProvider(UserDetailsService userDetailsService, OAuth2LimitedProperties limitedProperties) {
        this.userDetailsService = userDetailsService;
        this.limitedProperties = limitedProperties;
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    protected abstract void additionalAuthenticationChecks(UserDetails userDetails, Map<String, Object> additionalParameters) throws AuthenticationException;

    protected abstract UserDetails retrieveUser(Map<String, Object> additionalParameters) throws AuthenticationException;

    private Authentication authenticateUserDetails(Map<String, Object> additionalParameters, String registeredClientId) throws AuthenticationException {
        UserDetails user = retrieveUser(additionalParameters);

        if (!user.isAccountNonLocked()) {
            log.debug("[DaaS] Failed to authenticate since user account is locked");
            throw new LockedException(ResultErrorCodes.ACCOUNT_LOCKED.getMessage());
        }
        if (!user.isEnabled()) {
            log.debug("[DaaS] Failed to authenticate since user account is disabled");
            throw new DisabledException(ResultErrorCodes.ACCOUNT_DISABLED.getMessage());
        }
        if (!user.isAccountNonExpired()) {
            log.debug("[DaaS] Failed to authenticate since user account has expired");
            throw new AccountExpiredException(ResultErrorCodes.ACCOUNT_EXPIRED.getMessage());
        }

        additionalAuthenticationChecks(user, additionalParameters);

        if (!user.isCredentialsNonExpired()) {
            log.debug("[DaaS] Failed to authenticate since user account credentials have expired");
            throw new CredentialsExpiredException(ResultErrorCodes.CREDENTIALS_EXPIRED.getMessage());
        }

        if (!limitedProperties.getSignInEndpointLimited().getEnabled() && limitedProperties.getSignInKickOutLimited().getEnabled()) {
            // todo 单端限制 下线踢出
        }

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    protected Authentication getUsernamePasswordAuthentication(Map<String, Object> additionalParameters, String registeredClientId) throws AuthenticationException {
        Authentication authentication = null;
        try {
            authentication = authenticateUserDetails(additionalParameters, registeredClientId);
        } catch (AccountStatusException ase) {
            String exceptionName = ase.getClass().getSimpleName();
            OAuth2EndpointUtils.throwError(
                    exceptionName,
                    ase.getMessage());
        } catch (BadCredentialsException bce) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorKeys.BAD_CREDENTIALS,
                    bce.getMessage());
        } catch (UsernameNotFoundException unfe) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorKeys.USERNAME_NOT_FOUND,
                    unfe.getMessage());
        } catch (Exception ex) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorKeys.SERVICE_ERROR,
                    ex.getMessage()
            );
        }

        return authentication;
    }
}
