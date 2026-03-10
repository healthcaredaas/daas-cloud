package cn.healthcaredaas.data.cloud.foundation.rbac.dto;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import lombok.Data;

import java.util.List;

/**

 * @ClassName： RbacUserDto.java
 * @Author： chenpan
 * @Date：2024/12/6 16:57
 * @Modify：
 */
@Data
public class RbacUserDTO extends RbacUser {

    private List<RbacRole> roles;

    private List<RbacResource> authorities;
}
