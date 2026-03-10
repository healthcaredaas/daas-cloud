package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 * @ClassName： RbacRoleDataAuthority.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/7/20 16:04
 * @Modify：
 */
@TableName(value = "rbac_role_data_authority")
@Data
@NoArgsConstructor
public class RbacRoleDataAuthority {

    @TableField(value = "role_id")
    private String roleId;

    @TableField(value = "data_auth_id")
    private String dataAuthId;
}
