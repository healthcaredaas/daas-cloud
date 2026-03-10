package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import cn.healthcaredaas.data.cloud.foundation.rbac.enums.AccountStatusEnum;
import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户信息
 */
@TableName(value = "rbac_user")
@Data
@ToString(callSuper = true)
@Schema(name = "RbacUser", description = "用户信息")
@LogicUnique(columns = {"username"}, message = "用户名已存在！")
@LogicUnique(columns = {"phone"}, message = "电话已存在！")
@EnableSelectOption
public class RbacUser extends BaseEntity {

    public RbacUser() {
        this.setSortBy("username");
    }

    @Schema(description = "用户名")
    @TableField(value = "username")
    @NotNull(message = "用户名不能为空")
    private String username;

    @Schema(description = "昵称/姓名")
    @NotNull(message = "姓名不能为空")
    @TableField(value = "nick_name")
    private String nickName;

    @Schema(description = "密码")
    @TableField(value = "password")
    @JsonIgnore
    private String password;

    @Schema(description = "性别编码")
    @NotNull(message = "性别不能为空")
    @TableField(value = "gender")
    private String gender;

    @Schema(description = "身份证号")
    @TableField(value = "identification_no")
    private String identificationNo;

    @Schema(description = "邮箱")
    @TableField(value = "email")
    private String email;

    @Schema(description = "电话")
    @NotNull(message = "电话不能为空")
    @TableField(value = "phone")
    private String phone;

    @Schema(description = "im")
    @TableField(value = "im")
    private String im;

    @Schema(description = "账号状态")
    @TableField(value = "status")
    private AccountStatusEnum status;

    @Schema(description = "账户过期日期")
    @TableField(value = "account_expire_at")
    private LocalDateTime accountExpireAt;

    @Schema(description = "密码过期日期")
    @TableField(value = "credentials_expire_at")
    private LocalDateTime credentialsExpireAt;

    @Schema(description = "上次登录时间")
    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Schema(description = "主题")
    @TableField(value = "theme")
    private String theme;

    @Schema(description = "图像")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "备注")
    @TableField(value = "description")
    private String description;

    @Schema(description = "人员ID")
    @TableField(value = "practitioner_id")
    private String practitionerId;

}
