package cn.healthcaredaas.data.cloud.auth.authentication.password;

import cn.healthcaredaas.data.cloud.auth.authentication.base.AbstractAuthenticationConverter;
import cn.healthcaredaas.data.cloud.security.authentication.utils.OAuth2EndpointUtils;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * <pre>自定义密码模式认证转换器</pre>
 *
 * @ClassName： OAuth2ResourceOwnerPasswordAuthenticationConverter.java
 * @Author： chenpan
 * @Date：2024/12/7 11:36
 * @Modify：
 */
@NoArgsConstructor
public class OAuth2ResourceOwnerPasswordAuthenticationConverter extends AbstractAuthenticationConverter<OAuth2ResourceOwnerPasswordAuthenticationToken> {
    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     * @return
     */
    @Override
    public boolean support(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    /**
     * 校验参数
     *
     * @param request 请求
     */
    @Override
    public void checkParams(HttpServletRequest request, MultiValueMap<String, String> parameters) {
        OAuth2EndpointUtils.checkRequiredParameter(parameters, OAuth2ParameterNames.USERNAME);
        OAuth2EndpointUtils.checkRequiredParameter(parameters, OAuth2ParameterNames.PASSWORD);
    }

    /**
     * 构建具体类型的token
     *
     * @param clientPrincipal
     * @param requestedScopes
     * @param additionalParameters
     * @return
     */
    @Override
    public OAuth2ResourceOwnerPasswordAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new OAuth2ResourceOwnerPasswordAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
    }
}
