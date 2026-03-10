package cn.healthcaredaas.data.cloud.foundation.audit.service.impl;

import cn.healthcaredaas.data.cloud.foundation.audit.dao.OperationLogDao;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import cn.healthcaredaas.data.cloud.audit.model.OperationLog;
import cn.healthcaredaas.data.cloud.foundation.audit.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * @ClassName： OperationLogServiceImpl.java
 * @Description: 操作日志 服务实现
 * @Author： chenpan
 * @Date：2025-06-06 21:26:06
 * @Modify：
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogDao, OperationLog>
        implements IOperationLogService {

}
