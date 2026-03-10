package cn.healthcaredaas.data.cloud.auth.handler;

import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.security.core.exception.SecurityGlobalExceptionHandler;
import cn.healthcaredaas.data.cloud.web.core.utils.HttpUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>认证失败处理器</pre>
 *
 * @ClassName： OAuth2AuthenticationFailureHandler.java
 * @Author： chenpan
 * @Date：2024/12/1 11:44
 * @Modify：
 */
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        RestResult<String> result = SecurityGlobalExceptionHandler.resolveSecurityException(exception, request.getRequestURI());
        response.setStatus(result.getStatus());
        HttpUtils.renderJson(response, result);
    }
}
