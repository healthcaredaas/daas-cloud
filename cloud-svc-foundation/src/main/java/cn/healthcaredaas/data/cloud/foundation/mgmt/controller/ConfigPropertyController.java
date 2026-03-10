package cn.healthcaredaas.data.cloud.foundation.mgmt.controller;

import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.ConfigProperty;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IConfigPropertyService;
import cn.healthcaredaas.data.cloud.web.core.annotation.Api;
import cn.healthcaredaas.data.cloud.web.core.annotation.GetOperation;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

/**

 * @ClassName： ConfigPropertyController.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/18 15:52
 * @Modify：
 */
@Api(value = MgmtApi.PREFIX + "/property", name = "配置参数管理接口")
public class ConfigPropertyController extends BaseCRUDController<ConfigProperty, ConfigProperty, ConfigProperty> {

    @Autowired
    private IConfigPropertyService configPropertyService;

    @GetOperation(value = "/content", name = "根据系统编码和配置编码获取配置内容")
    public RestResult<Object> getContent(@RequestParam(value = "appCode") String appCode,
                                         @RequestParam(value = "configCode") String configCode) {
        Object config = configPropertyService.getPropertyByAppAndCode(appCode, configCode);
        return result(config);
    }
}