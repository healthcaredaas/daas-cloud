package cn.healthcaredaas.data.cloud.foundation.rbac.controller;

import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRoleResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRoleService;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 * @ClassName： RbacRoleController.java
 * @Author： chenpan
 * @Date：2024/12/16 11:20
 * @Modify：
 */
@RestController
@RequestMapping(RbacApi.PREFIX + "/role")
@Tags({
        @Tag(name = "系统角色管理接口")
})
@Slf4j
public class RbacRoleController extends BaseCRUDController<RbacRole, RbacRole, RbacRole> {

    @Autowired
    private IRoleService roleService;

    @GetMapping("/{roleId}/resource")
    @Operation(summary = "获取角色可访问的资源")
    public RestResult<List<RbacRoleResource>> getRoleResources(@PathVariable("roleId") String roleId) {
        return RestResult.content(roleService.getRoleResources(roleId));
    }

    @PutMapping("/{roleId}/resource")
    @Operation(summary = "更新角色可访问的资源")
    public RestResult<String> updateRoleResources(@PathVariable("roleId") String roleId,
                                                  @RequestBody List<String> resourceIds) {
        roleService.updateRoleResources(roleId, resourceIds);
        return RestResult.success("角色权限更新成功", roleId);
    }
}