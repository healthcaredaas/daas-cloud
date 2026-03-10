package cn.healthcaredaas.data.cloud.foundation.mgmt.controller;

import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Application;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IApplicationService;
import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseTreeCRUDController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

 * @ClassName： ApplicationController.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/14 17:41
 * @Modify：
 */
@RestController
@RequestMapping(MgmtApi.PREFIX + "/application")
@Tags({
        @Tag(name = "系统应用管理接口")
})
public class ApplicationController extends BaseTreeCRUDController<Application, Application, Application> {

    @Autowired
    private IApplicationService applicationService;

    @GetMapping(value = "/code/{appCode}")
    @Operation(summary = "查询系统应用")
    public RestResult<Application> getApp(@PathVariable("appCode") String appCode) {
        Application app = applicationService.getAppByCode(appCode);
        return queryResult(app);
    }
}