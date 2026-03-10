package cn.healthcaredaas.data.cloud.auth.configuration;

import cn.healthcaredaas.data.cloud.auth.service.impl.RedisOAuth2AuthorizationService;
import cn.healthcaredaas.data.cloud.security.core.handler.DefaultOAuth2AuthenticationEventPublisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**

 * @ClassName： AuthorizationConfiguration.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/9 18:20
 * @Modify：
 */
@Configuration
public class AuthorizationConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationContext applicationContext) {
        return new DefaultOAuth2AuthenticationEventPublisher(applicationContext);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate);
    }
}
