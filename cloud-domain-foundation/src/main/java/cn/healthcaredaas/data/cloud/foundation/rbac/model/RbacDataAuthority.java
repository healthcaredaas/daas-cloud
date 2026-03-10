package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**

 * @ClassName： DataAuthority.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/7/20 16:03
 * @Modify：
 */
@TableName(value = "rbac_data_authority")
@Data
@Schema(name = "RbacDataAuthority", description = "数据权限")
@LogicUnique(columns = {"authCode"}, message = "数据权限已存在！")
@EnableSelectOption
public class RbacDataAuthority extends BaseEntity {

    @TableField(value = "auth_code")
    private String authCode;

    @TableField(value = "auth_name")
    private String authName;

    @TableField(value = "description")
    private String description;
}
