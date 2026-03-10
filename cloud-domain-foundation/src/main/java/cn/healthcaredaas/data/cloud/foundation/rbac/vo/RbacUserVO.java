package cn.healthcaredaas.data.cloud.foundation.rbac.vo;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**

 * @ClassName： RbacUserVO.java
 * @Author： chenpan
 * @Date：2024/12/15 09:54
 * @Modify：
 */
@Data
@Schema(name = "RbacUserVO", title = "用户信息")
public class RbacUserVO extends RbacUser {

    @Schema(description = "角色id列表")
    private String[] roleIds;

    @Schema(description = "科室id列表")
    private String[] deptIds;
}
