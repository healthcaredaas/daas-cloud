package cn.healthcaredaas.data.cloud.foundation.rbac.controller;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRbacResourceService;
import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.web.core.annotation.Api;
import cn.healthcaredaas.data.cloud.web.core.annotation.GetOperation;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseTreeCRUDController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**

 * @ClassName： RbacResourceController.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/13 15:37
 * @Modify：
 */
@RestController
@Api(value = RbacApi.PREFIX + "/resource", name = "系统资源权限管理接口")
@Slf4j
public class RbacResourceController extends BaseTreeCRUDController<RbacResource, RbacResource, RbacResource> {

    @Autowired
    private IRbacResourceService authorityService;

    @GetOperation(value = "/roles", name = "获取可访问资源的角色")
    public RestResult<List<String>> resourceRoles(@RequestParam("url") String url,
                                                   @RequestParam("method") String method) {
        List<String> roles = authorityService.resourceRoles(url, method);
        return result(roles);
    }

    @GetOperation(value = "/map", name = "查询角色api资源权限")
    public RestResult<Map<String, String>> getApiResources(@RequestParam("role") String role) {
        log.debug("查询角色api资源权限，角色：{}", role);
        Map<String, String> authorities = authorityService.getApiResources(role);
        return result(authorities);
    }
}