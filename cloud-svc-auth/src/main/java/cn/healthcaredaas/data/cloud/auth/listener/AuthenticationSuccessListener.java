package cn.healthcaredaas.data.cloud.auth.listener;

import cn.healthcaredaas.data.cloud.auth.cache.SignInFailureLimitedContainer;
import cn.healthcaredaas.data.cloud.auth.log.AuthenticationLogger;
import cn.healthcaredaas.data.cloud.core.enums.SuccessStatusEnum;
import cn.healthcaredaas.data.cloud.security.core.entity.UserAuthenticationDetails;
import org.redisson.api.RedissonClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>登录成功事件监听</pre>
 *
 * @ClassName： AuthenticationSuccessListener.java
 * @Author： chenpan
 * @Date：2024/12/15 17:21
 * @Modify：
 */
@Component
@Slf4j
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private AuthenticationLogger authenticationLogger;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        Authentication authentication = event.getAuthentication();

        if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
            OAuth2AccessTokenAuthenticationToken authenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;
            Object details = authentication.getDetails();

            String username = null;
            if (ObjectUtils.isNotEmpty(details) && details instanceof UserAuthenticationDetails) {
                UserAuthenticationDetails user = (UserAuthenticationDetails) details;
                username = user.getUserName();
            }

            String clientId = authenticationToken.getRegisteredClient().getClientId();

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (ObjectUtils.isNotEmpty(requestAttributes) && requestAttributes instanceof ServletRequestAttributes) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest request = servletRequestAttributes.getRequest();

                if (ObjectUtils.isNotEmpty(request) && StringUtils.isNotBlank(username)) {
                    log.debug("登录成功：用户名[{}], 客户端[{}], 请求内容[{}]", username, clientId, request);
                    authenticationLogger.saveLog(request, username, clientId, SuccessStatusEnum.SUCCESS, null);

                    SignInFailureLimitedContainer signInFailureLimitedContainer = new SignInFailureLimitedContainer(redissonClient);
                    signInFailureLimitedContainer.delete(username);
                }
            }
        }
    }
}