package cn.healthcaredaas.data.cloud.auth.listener;

import cn.healthcaredaas.data.cloud.auth.log.AuthenticationLogger;
import cn.healthcaredaas.data.cloud.core.enums.SuccessStatusEnum;
import cn.healthcaredaas.data.cloud.web.core.utils.HttpUtils;
import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.auth.cache.SignInFailureLimitedContainer;
import cn.healthcaredaas.data.cloud.auth.service.OAuth2AccountStatusService;
import cn.healthcaredaas.data.cloud.security.authentication.properties.OAuth2LimitedProperties;
import org.redisson.api.RedissonClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureProviderNotFoundEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.Map;

/**
 * <pre>登录失败事件监听</pre>
 *
 * @ClassName： AuthenticationFailureListener.java
 * @Author： chenpan
 * @Date：2024/12/15 16:21
 * @Modify：
 */
@Component
@Slf4j
public class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Resource
    private OAuth2LimitedProperties limitedProperties;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private OAuth2AccountStatusService accountStatusService;

    @Resource
    private AuthenticationLogger authenticationLogger;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            return;
        }

        Authentication authentication = event.getAuthentication();

        String username = null;
        String clientId = null;

        if (authentication instanceof OAuth2ClientAuthenticationToken token) {
            clientId = String.valueOf(token.getPrincipal());
            username = clientId;
        }
        if (authentication instanceof OAuth2AuthorizationGrantAuthenticationToken token) {
            Map<String, Object> params = token.getAdditionalParameters();
            username = getPrincipal(params);
            clientId = getClientId(token.getPrincipal());
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken token) {
            Object principal = token.getPrincipal();
            if (principal instanceof String) {
                username = (String) principal;
            }
            Authentication clientAuth = SecurityContextHolder.getContext().getAuthentication();
            clientId = getClientId(clientAuth);
        }
        if (authentication instanceof BearerTokenAuthenticationToken token) {
            return;
        }

        HttpServletRequest request = HttpUtils.getRequest();

        String errorMessage = AuthenticationExceptionHandler.resolveException(event.getException());
        //登陆失败 （登录日志,审计）
        authenticationLogger.saveLog(request, username, clientId, SuccessStatusEnum.FAIL, errorMessage);

        // 用户名密码错误
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            // 密码连续错误锁定
            if (StrUtil.isNotBlank(username) && limitedProperties.getSignInFailureLimited().getEnabled()) {
                SignInFailureLimitedContainer signInFailureLimitedContainer = new SignInFailureLimitedContainer(redissonClient);
                int times = signInFailureLimitedContainer.increment(username, limitedProperties.getSignInFailureLimited().getExpire());
                if (times >= limitedProperties.getSignInFailureLimited().getMaxTimes()) {
                    accountStatusService.lock(username);
                }
            }
        }
    }

    private String getPrincipal(Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            if (params.containsKey(OAuth2ParameterNames.USERNAME)) {
                Object value = params.get(OAuth2ParameterNames.USERNAME);
                if (ObjectUtils.isNotEmpty(value)) {
                    return (String) value;
                }
            }
        }

        return null;
    }

    private String getClientId(Object principal) {
        if (principal instanceof OAuth2ClientAuthenticationToken token) {
            return String.valueOf(token.getPrincipal());
        }

        return null;
    }
}