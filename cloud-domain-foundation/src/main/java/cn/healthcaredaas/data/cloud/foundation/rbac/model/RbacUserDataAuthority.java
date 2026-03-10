package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**

 * @ClassName： RbacUserDataAuthority.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/7/20 16:05
 * @Modify：
 */
@TableName(value = "rbac_user_data_authority")
@Data
@ToString(callSuper = true)
public class RbacUserDataAuthority {

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "data_auth_id")
    private String dataAuthId;
}
