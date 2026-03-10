package cn.healthcaredaas.data.cloud.foundation.audit.controller;



import cn.healthcaredaas.data.cloud.web.core.annotation.Api;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.healthcaredaas.data.cloud.audit.model.LoginLog;

/**
* @ClassName： LoginLogController.java
* @Description: 登录日志 API
* @Author： chenpan
* @Date：2025-06-23 15:24:24
* @Modify：
*/
@Api(value = AuditApi.PREFIX + "/loginLog", name = "登录日志接口")
public class LoginLogController extends BaseCRUDController<LoginLog, LoginLog, LoginLog> {

}