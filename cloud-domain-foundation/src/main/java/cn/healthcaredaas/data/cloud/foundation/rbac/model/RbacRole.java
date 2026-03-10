package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.annotation.SelectLikeColumn;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色信息
 */
@TableName(value = "rbac_role")
@Data
@Schema(name = "RbacRole", description = "角色信息")
@LogicUnique(columns = {"roleCode"}, message = "角色已存在！")
@EnableSelectOption
public class RbacRole extends BaseEntity {

    public RbacRole() {
        this.setSortBy("roleCode");
    }

    @Schema(description = "角色编码")
    @TableField(value = "role_code")
    private String roleCode;

    @Schema(description = "角色名称")
    @TableField(value = "role_name")
    @SelectLikeColumn(wildcardPosition = SelectLikeColumn.WildcardPosition.BOTH)
    private String roleName;

    @Schema(description = "描述")
    @TableField(value = "description")
    private String description;
}
