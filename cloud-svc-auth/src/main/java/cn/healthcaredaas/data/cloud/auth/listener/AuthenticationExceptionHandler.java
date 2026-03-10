package cn.healthcaredaas.data.cloud.auth.listener;

import cn.healthcaredaas.data.cloud.core.enums.ResultErrorCodes;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Description: 登录认证异常信息获取
 * @Version: V1.0
 * @Author： chenpan
 * @Date：2025/6/8 09:59
 * @Modify：
 */
public class AuthenticationExceptionHandler {

    public static String resolveException(Exception ex) {
    if (ex instanceof BadCredentialsException) {
        if (ex.getMessage() != null && ex.getMessage().contains("Client authentication failed")) {
            return ResultErrorCodes.INVALID_CLIENT.getMessage();
        }
        return ResultErrorCodes.BAD_CREDENTIALS.getMessage();
    }
    if (ex instanceof UsernameNotFoundException) {
        return ResultErrorCodes.USERNAME_NOT_FOUND.getMessage();
    }
    if (ex instanceof AccountExpiredException) {
        return ResultErrorCodes.ACCOUNT_EXPIRED.getMessage();
    }
    if (ex instanceof LockedException) {
        return ResultErrorCodes.ACCOUNT_LOCKED.getMessage();
    }

    return null;
}

}
