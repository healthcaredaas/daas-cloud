package cn.healthcaredaas.data.cloud.auth.authentication.password;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <pre>自定义密码模式认证Token</pre>
 *
 * @ClassName： OAuth2ResourceOwnerPasswordAuthenticationToken.java
 * @Author： chenpan
 * @Date：2024/12/7 11:32
 * @Modify：
 */
public class OAuth2ResourceOwnerPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Getter
    private final Set<String> scopes;

    protected OAuth2ResourceOwnerPasswordAuthenticationToken(Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
        this.scopes = Collections.unmodifiableSet(CollectionUtils.isNotEmpty(scopes) ? new HashSet<>(scopes) : Collections.emptySet());
    }
}
