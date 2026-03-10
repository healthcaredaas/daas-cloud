package cn.healthcaredaas.data.cloud.auth.log;

import cn.healthcaredaas.data.cloud.audit.model.LoginLog;
import cn.healthcaredaas.data.cloud.foundation.audit.service.ILoginLogService;
import cn.healthcaredaas.data.cloud.core.enums.SuccessStatusEnum;
import cn.healthcaredaas.data.cloud.web.core.utils.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * @Description: TOOD
 * @Version: V1.0
 * @Author： chenpan
 * @Date：2025/6/7 10:30
 * @Modify：
 */
@Component
@EnableAsync
public class AuthenticationLogger {

    @Autowired
    private ILoginLogService loginLogService;

    @Async
    public void saveLog(HttpServletRequest request, String username, String clientId, SuccessStatusEnum status, String message) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setClientId(clientId);
        loginLog.setStatus(status);
        loginLog.setFailureReason(message);
        loginLog.setIpAddress(HttpUtils.getIP(request));
        loginLog.setUserAgent(HttpUtils.getAgent(request));
        loginLog.setRequestUri(request.getRequestURI());

        loginLogService.add(loginLog);
    }       
}
