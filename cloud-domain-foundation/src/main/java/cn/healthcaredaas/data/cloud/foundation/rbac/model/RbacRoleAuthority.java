package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author chenpan
 * @date 2020/6/24 10:41
 */
@TableName(value = "rbac_role_authority")
@Data
@NoArgsConstructor
public class RbacRoleAuthority {

    @Schema(description = "角色id")
    @TableField(value = "role_id")
    private String roleId;

    @Schema(description = "权限ID")
    @TableField(value = "authority_id")
    private String authorityId;

    public RbacRoleAuthority(String roleId, String authorityId) {
        this.roleId = roleId;
        this.authorityId = authorityId;
    }
}
