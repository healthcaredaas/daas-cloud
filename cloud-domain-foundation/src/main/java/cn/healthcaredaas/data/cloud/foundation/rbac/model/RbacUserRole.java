package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenpan
 * @ClassName: SysUserGroup
 * description:
 * @date 2018-04-02 14:24
 */
@TableName(value = "rbac_user_role")
@Data
public class RbacUserRole {

    @Schema(description = "用户id")
    @TableField(value = "user_id")
    private String userId;

    @Schema(description = "角色ID")
    @TableField(value = "role_id")
    private String roleId;


}
