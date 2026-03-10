package cn.healthcaredaas.data.cloud.foundation.rbac.dto;

import lombok.Data;

/**

 * @ClassName： DepartmentDto.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/20 11:02
 * @Modify：
 */
@Data
public class DepartmentDTO {

    private String orgId;
    private String orgCode;
    private String orgName;

    private String deptId;
    private String deptCode;
    private String deptName;
}
