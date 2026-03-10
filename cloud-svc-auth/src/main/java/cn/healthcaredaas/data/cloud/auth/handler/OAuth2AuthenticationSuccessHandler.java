package cn.healthcaredaas.data.cloud.auth.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.healthcaredaas.data.cloud.auth.entity.OAuth2AccessTokenDto;
import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.web.core.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * <pre>自定义 Security 认证成功处理器</pre>
 *
 * @ClassName： OAuth2AuthenticationSuccessHandler.java
 * @Author： chenpan
 * @Date：2024/12/1 18:01
 * @Modify：
 */
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        returnAccessTokenResponse(request, response, authentication);
    }

    private void returnAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
                                           Authentication authentication) throws IOException {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenDto token = new OAuth2AccessTokenDto();
        token.setAccessToken(accessToken.getTokenValue());
        token.setTokenType(accessToken.getTokenType().getValue());
        token.setScope(CollectionUtil.join(accessToken.getScopes(), ","));
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            token.setExpiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            token.setRefreshToken(refreshToken.getTokenValue());
        }

        /*OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                        .tokenType(accessToken.getTokenType())
                        .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }

        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }

        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        //自定义返回可修改此处
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);*/

        RestResult<OAuth2AccessTokenDto> result = RestResult.success("认证成功！");
        result.data(token);
        HttpUtils.renderJson(response, result);
    }
}
