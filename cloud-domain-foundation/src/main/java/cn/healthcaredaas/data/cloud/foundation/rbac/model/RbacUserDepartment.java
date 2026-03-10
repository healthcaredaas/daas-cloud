package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author chenpan
 * @date 2020/6/12 11:14
 */
@TableName(value = "rbac_user_department")
@Data
public class RbacUserDepartment {

    @Schema(description = "用户id")
    @TableField(value = "user_id")
    private String userId;

    @Schema(description = "科室ID")
    @TableField(value = "dept_id")
    private String deptId;
}
