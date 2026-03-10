package cn.healthcaredaas.data.cloud.auth.helper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.healthcaredaas.data.cloud.auth.vo.RegisteredClientVo;
import cn.healthcaredaas.data.cloud.core.utils.IdUtils;
import cn.healthcaredaas.data.cloud.security.authentication.utils.OAuth2AuthorizationUtils;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.OAuth2RegisteredClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**

 * @ClassName： RegisteredClientHelper.java
 * @Author： chenpan
 * @Date：2024/12/9 10:18
 * @Modify：
 */
public class RegisteredClientHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PasswordEncoder passwordEncoder;

    public RegisteredClientHelper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        ClassLoader classLoader = RegisteredClientHelper.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    public RegisteredClient toObject(OAuth2RegisteredClient client) {
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
                client.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
                client.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
                client.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(
                client.getScopes());

        RegisteredClient.Builder builder = RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientIdIssuedAt(DateUtil.toInstant(client.getClientIdIssuedAt()))
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(DateUtil.toInstant(client.getClientSecretExpiresAt()))
                .clientName(client.getClientName())
                .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(OAuth2AuthorizationUtils.resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(OAuth2AuthorizationUtils.resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));

        Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
        builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

        return builder.build();
    }

    public OAuth2RegisteredClient toEntity(RegisteredClient registeredClient) {
        List<String> clientAuthenticationMethods = registeredClient.getClientAuthenticationMethods()
                .stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.toList());

        List<String> authorizationGrantTypes = registeredClient.getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.toList());

        OAuth2RegisteredClient entity = new OAuth2RegisteredClient();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(DateUtil.toLocalDateTime(registeredClient.getClientIdIssuedAt()));
        entity.setClientSecret(encode(registeredClient.getClientSecret()));
        entity.setClientSecretExpiresAt(DateUtil.toLocalDateTime(registeredClient.getClientSecretExpiresAt()));
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    public RegisteredClientVo model2Vo(OAuth2RegisteredClient client) {
        if (client == null) {
            return RegisteredClientVo.builder().build();
        }
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
                client.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
                client.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
                client.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(
                client.getScopes());

        Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
        ClientSettings clientSettings = ClientSettings.withSettings(clientSettingsMap).build();

        Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
        TokenSettings tokenSettings = TokenSettings.withSettings(tokenSettingsMap).build();

        RegisteredClientVo.ClientSettingsConvert clientSettingsConvert = new
                RegisteredClientVo.ClientSettingsConvert(clientSettings.isRequireProofKey(), clientSettings.isRequireAuthorizationConsent());


        RegisteredClientVo.TokenSettingsConvert tokenSettingsConvert = RegisteredClientVo.TokenSettingsConvert
                .builder()
                .idTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm().getName())
                .accessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive().toMillis() / 1000)
                .reuseRefreshTokens(tokenSettings.isReuseRefreshTokens())
                .refreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive().toMillis() / 1000)
                .authorizationCodeTimeToLive(tokenSettings.getAuthorizationCodeTimeToLive().toMillis() / 1000)
                .accessTokenFormat(tokenSettings.getAccessTokenFormat().getValue())
                .build();

        RegisteredClientVo dto = RegisteredClientVo.builder()
                .clientId(client.getClientId())
                //.clientSecret(client.getClientSecret())
                .clientName(client.getClientName())
                .authorizationGrantTypes(authorizationGrantTypes)
                .scopes(clientScopes)
                .clientAuthenticationMethods(clientAuthenticationMethods)
                .redirectUris(redirectUris)
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientSettings(clientSettingsConvert)
                .tokenSettings(tokenSettingsConvert)
                .build();
        dto.setId(client.getId());
        return dto;
    }

    public OAuth2RegisteredClient vo2Model(RegisteredClientVo dto) {
        OAuth2RegisteredClient entity = new OAuth2RegisteredClient();
        if (ObjectUtil.isEmpty(dto)) {
            return entity;
        }
        Set<AuthorizationGrantType> authorizationGrantTypes = Optional.ofNullable(dto.getAuthorizationGrantTypes())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(AuthorizationGrantType::new)
                .collect(Collectors.toSet());
        Set<ClientAuthenticationMethod> clientAuthenticationMethods = Optional.ofNullable(dto.getClientAuthenticationMethods())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(ClientAuthenticationMethod::new)
                .collect(Collectors.toSet());

        if (dto.getClientSettings() != null) {
            RegisteredClientVo.ClientSettingsConvert clientSettingsConvert = dto.getClientSettings();
            ClientSettings clientSettings = ClientSettings.builder()
                    .requireProofKey(clientSettingsConvert.getRequireProofKey())
                    .requireAuthorizationConsent(clientSettingsConvert.getRequireAuthorizationConsent())
                    .build();
            entity.setClientSettings(writeMap(clientSettings.getSettings()));
        }

        if (dto.getTokenSettings() != null) {
            RegisteredClientVo.TokenSettingsConvert tokenSettingsConvert = dto.getTokenSettings();
            TokenSettings tokenSettings = TokenSettings.builder()
                    .accessTokenFormat(new OAuth2TokenFormat(tokenSettingsConvert.getAccessTokenFormat()))
                    .accessTokenTimeToLive(Duration.ofSeconds(tokenSettingsConvert.getAccessTokenTimeToLive()))
                    .refreshTokenTimeToLive(Duration.ofSeconds(tokenSettingsConvert.getRefreshTokenTimeToLive()))
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.from(tokenSettingsConvert.getIdTokenSignatureAlgorithm()))
                    .reuseRefreshTokens(tokenSettingsConvert.getReuseRefreshTokens())
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.from(tokenSettingsConvert.getIdTokenSignatureAlgorithm()))
                    .build();
            entity.setTokenSettings(writeMap(tokenSettings.getSettings()));
        }

        entity.setId(dto.getId());
        entity.setClientId(dto.getClientId());
        entity.setClientIdIssuedAt(dto.getClientIdIssuedAt());
        entity.setClientSecret(encode(dto.getClientSecret()));
        entity.setClientSecretExpiresAt(dto.getClientSecretExpiresAt());
        entity.setClientName(dto.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(dto.getRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(dto.getScopes()));

        return entity;

    }

    public RegisteredClientVo toVo(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            return RegisteredClientVo.builder().build();
        }

        Set<String> grantTypesConvertList = registeredClient.getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.toSet());

        Set<String> authenticationMethodsConverts = registeredClient.getClientAuthenticationMethods()
                .stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.toSet());

        ClientSettings clientSettings = registeredClient.getClientSettings();
        RegisteredClientVo.ClientSettingsConvert clientSettingsConvert = new
                RegisteredClientVo.ClientSettingsConvert(clientSettings.isRequireProofKey(), clientSettings.isRequireAuthorizationConsent());

        TokenSettings tokenSettings = registeredClient.getTokenSettings();
        RegisteredClientVo.TokenSettingsConvert tokenSettingsConvert = RegisteredClientVo.TokenSettingsConvert
                .builder()
                .idTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm().getName())
                .accessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive().toMillis() / 1000)
                .reuseRefreshTokens(tokenSettings.isReuseRefreshTokens())
                .refreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive().toMillis() / 1000)
                .authorizationCodeTimeToLive(tokenSettings.getAuthorizationCodeTimeToLive().toMillis() / 1000)
                .build();

        RegisteredClientVo dto = RegisteredClientVo.builder()
                .clientId(registeredClient.getClientId())
                .clientSecret(registeredClient.getClientSecret())
                .clientName(registeredClient.getClientName())
                .authorizationGrantTypes(grantTypesConvertList)
                .scopes(registeredClient.getScopes())
                .clientAuthenticationMethods(authenticationMethodsConverts)
                .redirectUris(registeredClient.getRedirectUris())
                .clientIdIssuedAt(registeredClient.getClientIdIssuedAt() == null ? null : DateUtil.toLocalDateTime(registeredClient.getClientIdIssuedAt()))
                .clientSecretExpiresAt(registeredClient.getClientSecretExpiresAt() == null ? null : DateUtil.toLocalDateTime(registeredClient.getClientSecretExpiresAt()))
                .clientSettings(clientSettingsConvert)
                .tokenSettings(tokenSettingsConvert)
                .build();
        dto.setId(registeredClient.getId());
        return dto;
    }

    public RegisteredClient vo2Object(RegisteredClientVo dto) {
        if (dto == null) {
            return null;
        }
        String id = dto.getId() != null ? dto.getId() : String.valueOf(IdUtils.nextId());
        RegisteredClient.Builder builder = RegisteredClient.withId(String.valueOf(id));

        Set<AuthorizationGrantType> authorizationGrantTypes = Optional.ofNullable(dto.getAuthorizationGrantTypes())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(AuthorizationGrantType::new)
                .collect(Collectors.toSet());
        Set<ClientAuthenticationMethod> clientAuthenticationMethods = Optional.ofNullable(dto.getClientAuthenticationMethods())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(ClientAuthenticationMethod::new)
                .collect(Collectors.toSet());

        if (dto.getClientSettings() != null) {
            RegisteredClientVo.ClientSettingsConvert clientSettingsConvert = dto.getClientSettings();
            ClientSettings clientSettings = ClientSettings.builder()
                    .requireProofKey(clientSettingsConvert.getRequireProofKey())
                    .requireAuthorizationConsent(clientSettingsConvert.getRequireAuthorizationConsent())
                    .build();
            builder.clientSettings(clientSettings);
        }

        if (dto.getTokenSettings() != null) {
            RegisteredClientVo.TokenSettingsConvert tokenSettingsConvert = dto.getTokenSettings();
            TokenSettings tokenSettings = TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofSeconds(tokenSettingsConvert.getAccessTokenTimeToLive()))
                    .refreshTokenTimeToLive(Duration.ofSeconds(tokenSettingsConvert.getRefreshTokenTimeToLive()))
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.from(tokenSettingsConvert.getIdTokenSignatureAlgorithm()))
                    .reuseRefreshTokens(tokenSettingsConvert.getReuseRefreshTokens())
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.from(tokenSettingsConvert.getIdTokenSignatureAlgorithm()))
                    .build();
            builder.tokenSettings(tokenSettings);
        }

        return builder.clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .clientSecret(dto.getClientSecret())
                .clientIdIssuedAt(Instant.now().atZone(ZoneId.systemDefault()).toInstant())
                .clientSecretExpiresAt(DateUtil.toInstant(dto.getClientSecretExpiresAt()))
                .clientAuthenticationMethods(v -> v.addAll(clientAuthenticationMethods))
                .authorizationGrantTypes(v -> v.addAll(authorizationGrantTypes))
                .redirectUris(v -> v.addAll(dto.getRedirectUris()))
                .scopes(v -> v.addAll(dto.getScopes()))
                .build();
    }

    private String encode(String value) {
        if (value != null) {
            return this.passwordEncoder.encode(value);
        }
        return null;
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
