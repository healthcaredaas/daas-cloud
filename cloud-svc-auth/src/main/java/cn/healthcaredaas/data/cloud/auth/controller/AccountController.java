package cn.healthcaredaas.data.cloud.auth.controller;

import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Application;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IApplicationService;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IRbacResourceService;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IUserService;
import cn.healthcaredaas.data.cloud.core.exception.BizException;
import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.security.core.constants.OAuthEndpoint;
import cn.healthcaredaas.data.cloud.security.core.properties.SecurityProperties;
import cn.healthcaredaas.data.cloud.security.core.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * @ClassName： OAuthController.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/16 15:47
 * @Modify：
 */
@RestController
@Tags({
        @Tag(name = "登录账户管理")
})
public class AccountController {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRbacResourceService authorityService;

    @Autowired
    private IApplicationService applicationService;

    private boolean hasAllData() {
        return securityProperties.getAdminHasAllPermission() &&
                SecurityUtils.hasRole(securityProperties.getRoleSuperAdmin());
    }

    private String getUserId() {
        String uId = SecurityUtils.getUserId();
        if (StrUtil.isBlank(uId)) {
            throw new BizException("未获取到登录信息");
        }
        return uId;
    }

    @GetMapping(OAuthEndpoint.USER_PROFILE)
    @Operation(summary = "查询登录账户信息")
    public RestResult<Map<String, Object>> getUserProfile() {
        String userId = getUserId();
        RbacUser user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        Map<String, Object> profile = new HashMap<>(4);
        profile.put("user", user);
        profile.put("roles", SecurityUtils.getRoles());
        profile.put("permissions", userService.getUserAuthorities(userId));
        profile.put("depts", userService.getUserDepartments(userId));
        return RestResult.content(profile);
    }

    @GetMapping(OAuthEndpoint.USER_MENU)
    @Operation(summary = "账户菜单信息")
    public RestResult<Map<String, Object>> getUserSysMenu(@RequestParam(value = "appId") String appId) {
        String userId = hasAllData() ? null : getUserId();

        List<Application> modules = applicationService.getSubApp(appId, userId, null);
        List<String> appIds = new ArrayList<>(modules.size() + 1);
        appIds.add(appId);
        modules.forEach(app -> appIds.add(app.getId()));
        List<RbacResource> menus = authorityService.getMenus(appIds.toArray(new String[appIds.size()]), userId);

        Map<String, Object> moduleMenus = new HashMap<>(2);
        moduleMenus.put("modules", modules);
        moduleMenus.put("menus", menus);
        return RestResult.content(moduleMenus);
    }

    @GetMapping(OAuthEndpoint.USER_APP)
    @Operation(summary = "账户应用信息")
    public RestResult<List<Application>> getUserApp(@RequestParam(value = "appId") String appId) {
        String userId = hasAllData() ? null : getUserId();

        List<Application> modules = applicationService.getSubApp(appId, userId, true);
        return RestResult.content(modules);
    }

    @PutMapping(OAuthEndpoint.CHANGE_PASSWORD)
    @Operation(summary = "修改密码")
    public RestResult<String> updatePasswd(@RequestParam("oldPass") String oldPass,
                                           @RequestParam("newPass") String newPass) {

        RbacUser oldUser = userService.getById(getUserId());

        if (SecurityUtils.matches(oldPass, oldUser.getPassword())) {
            userService.updatePassword(oldUser.getId(), newPass);
            return RestResult.success("密码修改成功");
        } else {
            throw new BizException("旧密码不正确");
        }
    }

    @PutMapping(OAuthEndpoint.USER_PROFILE)
    @Operation(summary = "更新账户信息")
    public RestResult<String> updateUser(@RequestBody @Validated RbacUser user) {
        userService.updateUserProfile(user);
        return RestResult.success("账户信息更新成功");
    }
}
