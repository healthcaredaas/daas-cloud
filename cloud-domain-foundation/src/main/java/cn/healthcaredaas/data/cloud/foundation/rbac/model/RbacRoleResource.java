package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 角色权限
 * @Version: V1.0
 * @Author： chenpan
 * @Date：2025/6/5 16:23
 * @Modify：
 */
@TableName(value = "rbac_role_resource")
@Data
@NoArgsConstructor
public class RbacRoleResource {

    @Schema(description = "角色id")
    @TableField(value = "role_id")
    private String roleId;

    @Schema(description = "权限ID")
    @TableField(value = "resource_id")
    private String resourceId;

    public RbacRoleResource(String roleId, String resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
}
