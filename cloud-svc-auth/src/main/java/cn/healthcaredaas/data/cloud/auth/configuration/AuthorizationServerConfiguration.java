package cn.healthcaredaas.data.cloud.auth.configuration;

import cn.healthcaredaas.data.cloud.auth.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import cn.healthcaredaas.data.cloud.auth.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import cn.healthcaredaas.data.cloud.auth.handler.OAuth2AuthenticationFailureHandler;
import cn.healthcaredaas.data.cloud.auth.handler.OAuth2AuthenticationSuccessHandler;
import cn.healthcaredaas.data.cloud.security.authentication.customizer.JwtTokenCustomizer;
import cn.healthcaredaas.data.cloud.security.authentication.customizer.OpaqueTokenCustomizer;
import cn.healthcaredaas.data.cloud.security.authentication.properties.OAuth2LimitedProperties;
import cn.healthcaredaas.data.cloud.security.core.handler.DefaultOAuth2AuthenticationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.Arrays;

/**
 * <pre>认证服务器配置</pre>
 * <br/>
 * 1. 权限核心处理 {@link AuthorizationFilter}
 * <br/>
 * 2. 权限判断 {@link AuthorizationManager}
 * <br/>
 * 3. 模式决策 {@link org.springframework.security.authentication.ProviderManager}
 *
 * @ClassName： AuthorizationServerConfiguration.java
 * @Author： chenpan
 * @Date：2024/12/1 16:25
 * @Modify：
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

    @Resource
    private OAuth2AuthorizationService authorizationService;

    @Resource
    private JwtDecoder jwtDecoder;

    @Resource
    private JwtEncoder jwtEncoder;

    @Resource
    private OAuth2LimitedProperties limitedProperties;

    @Resource
    private UserDetailsService userDetailsService;

    @PostConstruct
    public void postConstruct() {
        log.debug("[DaaS] SDK [OAuth2 Authorization Server] Auto Configure.");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        httpSecurity.with(authorizationServerConfigurer, configurer -> {
            try {
                configureEndpoints(configurer);
                configureSecurity(httpSecurity, configurer);
                configureAuthenticationManager(httpSecurity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return httpSecurity.build();
    }

    private void configureEndpoints(OAuth2AuthorizationServerConfigurer authorizationServerConfigurer) {
        OAuth2AuthenticationFailureHandler failureHandler = new OAuth2AuthenticationFailureHandler();

        // 授权端点   /oauth/authorize
        authorizationServerConfigurer.authorizationEndpoint(endpoint -> {
            endpoint.errorResponseHandler(failureHandler);
        });

        // 令牌撤销
        authorizationServerConfigurer.tokenRevocationEndpoint(endpoint -> endpoint.errorResponseHandler(failureHandler));

        // 令牌自省
        authorizationServerConfigurer.tokenIntrospectionEndpoint(endpoint -> endpoint.errorResponseHandler(failureHandler));

        // 处理客户端认证异常
        authorizationServerConfigurer.clientAuthentication(endpoint -> {
            endpoint.errorResponseHandler(failureHandler);
        });

        // 认证端点 /oauth/token
        authorizationServerConfigurer.tokenEndpoint(endpoint -> {
            // 自定义的授权认证Converter
            endpoint.accessTokenRequestConverter(accessTokenRequestConverter());
            // 登录成功处理器
            endpoint.accessTokenResponseHandler(new OAuth2AuthenticationSuccessHandler());
            // 登录失败处理器
            endpoint.errorResponseHandler(failureHandler);
        });

        authorizationServerConfigurer.oidc(oidc -> oidc.clientRegistrationEndpoint(Customizer.withDefaults()));
    }

    private void configureSecurity(HttpSecurity httpSecurity, OAuth2AuthorizationServerConfigurer authorizationServerConfigurer) throws Exception {
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        httpSecurity.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher));

        // 使用最新的API配置授权服务
        authorizationServerConfigurer.authorizationService(authorizationService);
        authorizationServerConfigurer.authorizationServerSettings(AuthorizationServerSettings.builder().build());
    }

    private void configureAuthenticationManager(HttpSecurity httpSecurity) {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
        authenticationManagerBuilder.authenticationEventPublisher(new DefaultOAuth2AuthenticationEventPublisher(applicationContext));

        addCustomOAuth2GrantAuthenticationProvider(httpSecurity);
    }

    /**
     * Token转换器
     *
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter(),
                new OAuth2ResourceOwnerPasswordAuthenticationConverter())
        );
    }


    /**
     * 自定义token生成
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<?> oAuth2TokenGenerator() {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtTokenCustomizer());

        // 使用最新的API创建访问令牌和刷新令牌生成器
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return new JwtTokenCustomizer();
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueTokenCustomizer() {
        return new OpaqueTokenCustomizer();
    }

    /**
     * 注入授权模式实现提供方
     * <p>
     * 1. 密码模式 </br>
     * 2. 客户端模式 </br>
     * 2. 其他模式扩展增加 </br>
     */
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                authorizationService, oAuth2TokenGenerator(), userDetailsService, limitedProperties);
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

        // 客户端凭证模式的认证提供者
        OAuth2ClientCredentialsAuthenticationProvider clientCredentialsAuthenticationProvider = new OAuth2ClientCredentialsAuthenticationProvider(
                authorizationService, oAuth2TokenGenerator());
        // 处理 OAuth2ClientCredentialsAuthenticationToken (客户端凭证模式)
        http.authenticationProvider(clientCredentialsAuthenticationProvider);

    }
}