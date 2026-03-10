package cn.healthcaredaas.data.cloud.auth.authentication.password;

import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.auth.authentication.base.AbstractUserDetailsAuthenticationProvider;
import cn.healthcaredaas.data.cloud.auth.service.OAuth2UserDetailsService;
import cn.healthcaredaas.data.cloud.security.authentication.properties.OAuth2LimitedProperties;
import cn.healthcaredaas.data.cloud.security.authentication.utils.OAuth2AuthenticationProviderUtils;
import cn.healthcaredaas.data.cloud.security.authentication.utils.OAuth2EndpointUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;
import java.util.Set;

/**
 * <pre>自定义 OAuth2 密码模式认证 Provider</pre>
 *
 * @ClassName： OAuth2ResourceOwnerPasswordAuthenticationProvider.java
 * @Author： chenpan
 * @Date：2024/12/7 11:19
 * @Modify：
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String ERROR_URI = OAuth2EndpointUtils.ERROR_URI;

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public OAuth2ResourceOwnerPasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailsService, OAuth2LimitedProperties limitedProperties) {
        super(userDetailsService, limitedProperties);
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Map<String, Object> additionalParameters) throws AuthenticationException {
        String presentedPassword = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
        if (!this.getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    protected UserDetails retrieveUser(Map<String, Object> additionalParameters) throws AuthenticationException {
        String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
        try {
            OAuth2UserDetailsService userDetailsService = getUserDetailsService();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return userDetails;
        } catch (UsernameNotFoundException ex) {
            log.error("[DaaS] User name can not found ：[{}]", username);
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            log.error("[DaaS] 系统异常  ：{}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("[DaaS] 系统异常  ：{}", ex.getMessage());
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthentication =
                (OAuth2ResourceOwnerPasswordAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(resourceOwnerPasswordAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        Authentication usernamePasswordAuthentication = getUsernamePasswordAuthentication(resourceOwnerPasswordAuthentication.getAdditionalParameters(), registeredClient.getId());

        // Default to configured scopes
        Set<String> authorizedScopes = validateScopes(resourceOwnerPasswordAuthentication.getScopes(), registeredClient);

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizedScopes(authorizedScopes);

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrant(resourceOwnerPasswordAuthentication);
        // @formatter:on

        // ----- Access token -----
        OAuth2AccessToken accessToken = createOAuth2AccessToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI);

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = creatOAuth2RefreshToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI, clientPrincipal, registeredClient);

        // ----- ID token -----
        OidcIdToken idToken = createOidcIdToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI, resourceOwnerPasswordAuthentication.getScopes());

        OAuth2Authorization authorization = authorizationBuilder.build();

        this.authorizationService.save(authorization);

        Map<String, Object> additionalParameters = idTokenAdditionalParameters(idToken);

        OAuth2AccessTokenAuthenticationToken accessTokenAuthenticationToken = new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
        return createOAuth2AccessTokenAuthenticationToken(usernamePasswordAuthentication, accessTokenAuthenticationToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
