package cn.healthcaredaas.data.cloud.auth.authentication.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.healthcaredaas.data.cloud.security.core.entity.OAuthUser;
import cn.healthcaredaas.data.cloud.security.core.entity.UserAuthenticationDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.*;

/**
 * <pre>抽象的认证Provider</pre>
 *
 * @ClassName： AbstractAuthenticationProvider.java
 * @Author： chenpan
 * @Date：2024/12/7 10:22
 * @Modify：
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    protected OAuth2AccessToken createOAuth2AccessToken(DefaultOAuth2TokenContext.Builder tokenContextBuilder, OAuth2Authorization.Builder authorizationBuilder, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, String errorUri) {
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", errorUri);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        return accessToken;
    }

    protected OAuth2RefreshToken creatOAuth2RefreshToken(DefaultOAuth2TokenContext.Builder tokenContextBuilder, OAuth2Authorization.Builder authorizationBuilder, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, String errorUri, OAuth2ClientAuthenticationToken clientPrincipal, RegisteredClient registeredClient) {
        OAuth2RefreshToken refreshToken = null;

        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", errorUri);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        return refreshToken;
    }

    protected OidcIdToken createOidcIdToken(DefaultOAuth2TokenContext.Builder tokenContextBuilder, OAuth2Authorization.Builder authorizationBuilder, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, String errorUri, Set<String> requestedScopes) {
        OidcIdToken idToken;

        if (requestedScopes.contains(OidcScopes.OPENID)) {
            OAuth2TokenContext tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build())
                    .build();
            OAuth2Token generatedIdToken = tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.", errorUri);
                throw new OAuth2AuthenticationException(error);
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }
        return idToken;
    }


    protected Map<String, Object> idTokenAdditionalParameters(OidcIdToken idToken) {
        Map<String, Object> additionalParameters = Collections.emptyMap();
        if (idToken != null) {
            additionalParameters = new HashMap<>();
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }
        return additionalParameters;
    }

    protected Set<String> validateScopes(Set<String> requestedScopes, RegisteredClient registeredClient) {
        Set<String> authorizedScopes = registeredClient.getScopes();
        if (CollectionUtil.isNotEmpty(requestedScopes)) {
            for (String requestedScope : requestedScopes) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }
        return authorizedScopes;
    }

    protected OAuth2AccessTokenAuthenticationToken createOAuth2AccessTokenAuthenticationToken(Authentication source, OAuth2AccessTokenAuthenticationToken destination) {
        if (source instanceof UsernamePasswordAuthenticationToken) {
            if (source.getPrincipal() instanceof OAuthUser) {
                OAuthUser user = (OAuthUser) source.getPrincipal();
                UserAuthenticationDetails details = new UserAuthenticationDetails();
                details.setUserId(user.getUserId());
                details.setUserName(user.getUsername());
                details.setRoles(user.getRoles());
                destination.setDetails(details);
            }
        }

        return destination;
    }
}
