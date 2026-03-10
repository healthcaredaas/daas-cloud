package cn.healthcaredaas.data.cloud.foundation.audit.controller;

import cn.healthcaredaas.data.cloud.audit.model.OperationLog;
import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.foundation.audit.service.IOperationLogService;
import cn.healthcaredaas.data.cloud.web.core.annotation.Api;
import cn.healthcaredaas.data.cloud.web.core.annotation.PostOperation;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: 操作日志
 * @Version: V1.0
 * @Author： chenpan
 * @Date：2025/6/9 14:28
 * @Modify：
 */
@Api(value = AuditApi.PREFIX + "/operationLog", name = "操作日志")
public class OperationLogController extends BaseCRUDController<OperationLog, OperationLog, OperationLog> {

    @Autowired
    private IOperationLogService operationLogService;

    @PostOperation(value = "/event", name = "操作日志事件保存")
    public RestResult<String> event(@RequestBody OperationLog log) {
        operationLogService.add(log);
        return RestResult.success("操作日志事件保存成功");
    }
}