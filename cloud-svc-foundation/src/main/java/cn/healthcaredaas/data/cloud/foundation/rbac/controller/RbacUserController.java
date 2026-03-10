package cn.healthcaredaas.data.cloud.foundation.rbac.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.healthcaredaas.data.cloud.core.context.CurrentUserInfo;
import cn.healthcaredaas.data.cloud.core.exception.BizException;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRbacDataAuthorityService;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRoleService;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IUserService;
import cn.healthcaredaas.data.cloud.foundation.rbac.vo.RbacUserVO;
import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.security.core.properties.SecurityProperties;
import cn.healthcaredaas.data.cloud.web.core.annotation.GetOperation;
import cn.healthcaredaas.data.cloud.web.core.utils.HeaderSecurityUtils;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;

/**

 * @ClassName： RbacUserController.java
 * @Author： chenpan
 * @Date：2024/12/12 16:34
 * @Modify：
 */
@RestController
@RequestMapping(RbacApi.PREFIX + "/user")
@Tags({
        @Tag(name = "系统用户管理接口")
})
public class RbacUserController extends BaseCRUDController<RbacUser, RbacUserVO, RbacUser> {

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private IUserService userService;

    @Resource
    private IRoleService roleService;

    @Resource
    private IRbacDataAuthorityService dataAuthorityService;

    @Override
    @Operation(summary = "新增用户")
    public RestResult<String> add(@RequestBody @Validated RbacUserVO user) {
        user.setCreateBy(HeaderSecurityUtils.getUserId());
        userService.addUser(user);
        return result(user.getId());
    }

    @Override
    @Operation(summary = "更新用户")
    public RestResult<String> update(@PathVariable("id") String id, @RequestBody @Validated RbacUserVO user) {
        if (user.getId() == null) {
            user.setId(id);
        }
        user.setUpdateBy(HeaderSecurityUtils.getUserId());
        userService.updateUser(user);
        return result(user.getId());
    }

    @GetMapping("roles/{userId}")
    @Operation(summary = "获取用户角色列表")
    public RestResult<List<RbacRole>> listUserRoles(@PathVariable("userId") String userId) {
        List<RbacRole> roles = roleService.findUserRoles(userId);
        return result(roles);
    }

    @GetMapping("departments/{userId}")
    @Operation(summary = "获取用户科室列表")
    public RestResult<List<String>> listUserDepartments(@PathVariable("userId") String userId) {
        List<String> deptIds = userService.getUserDepartments(userId);
        return result(deptIds);
    }

    @GetOperation(value = "info")
    public RestResult<CurrentUserInfo> getUserInfo(@RequestParam("userId") String userId) {
        RbacUser user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        CurrentUserInfo userInfo = new CurrentUserInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(user.getUsername());

        Set<String> roles = roleService.findUserRoleIds(userId);
        if (CollectionUtil.isNotEmpty(roles)) {
            boolean isAdmin = roles.contains(securityProperties.getRoleSuperAdmin());
            userInfo.setAdmin(isAdmin);
        }
        Set<String> permissions = userService.getUserAuthorities(userId);
        userInfo.setPermissions(permissions);

        Set<String> dataAuth = dataAuthorityService.getUserAndRoleDataAuth(userId);
        userInfo.setDataAuthorities(dataAuth);

        List<String> deptIds = userService.getUserDepartments(userId);
        userInfo.setDeptIds(deptIds);

        return result(userInfo);
    }
}