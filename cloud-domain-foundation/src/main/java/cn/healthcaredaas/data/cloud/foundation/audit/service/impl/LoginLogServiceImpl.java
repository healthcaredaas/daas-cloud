package cn.healthcaredaas.data.cloud.foundation.audit.service.impl;

import cn.healthcaredaas.data.cloud.foundation.audit.dao.LoginLogDao;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import cn.healthcaredaas.data.cloud.audit.model.LoginLog;
import cn.healthcaredaas.data.cloud.foundation.audit.service.ILoginLogService;
import org.springframework.stereotype.Service;

/**
 * @ClassName： LoginLogServiceImpl.java
 * @Description: 登录日志 服务实现
 * @Author： chenpan
 * @Date：2025-06-06 21:26:01
 * @Modify：
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLogDao, LoginLog>
        implements ILoginLogService {

}
