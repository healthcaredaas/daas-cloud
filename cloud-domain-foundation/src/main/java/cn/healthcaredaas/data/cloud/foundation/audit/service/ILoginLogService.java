package cn.healthcaredaas.data.cloud.foundation.audit.service;

import cn.healthcaredaas.data.cloud.audit.model.LoginLog;

/**
 * @ClassName： ILoginLogService.java
 * @Description: 登录日志 服务接口
 * @Author： chenpan
 * @Date：2025-06-06 21:26:01
 * @Modify：
 */
public interface ILoginLogService {

    boolean add(LoginLog loginLog);

}
