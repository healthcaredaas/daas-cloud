package cn.healthcaredaas.data.cloud.foundation.audit.service;

import cn.healthcaredaas.data.cloud.audit.model.OperationLog;

/**
 * @ClassName： IOperationLogService.java
 * @Description: 操作日志 服务接口
 * @Author： chenpan
 * @Date：2025-06-06 21:26:06
 * @Modify：
 */
public interface IOperationLogService {

    boolean add(OperationLog log);
}
