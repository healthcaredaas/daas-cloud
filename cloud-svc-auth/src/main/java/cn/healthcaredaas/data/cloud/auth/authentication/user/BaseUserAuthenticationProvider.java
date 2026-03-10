package cn.healthcaredaas.data.cloud.auth.authentication.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**

 * @ClassName： BaseUserAuthenticationProvider.java
 * @Author： chenpan
 * @Date：2024/12/6 17:01
 * @Modify：
 */
@Slf4j
public class BaseUserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return null;
    }
}
