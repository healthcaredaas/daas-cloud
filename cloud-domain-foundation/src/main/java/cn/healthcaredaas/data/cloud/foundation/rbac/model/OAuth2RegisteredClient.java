package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <pre>OAuth2 客户端</pre>
 * <p>
 *
 * @link OAuth2RegisteredClient
 * @ClassName： OAuth2RegisteredClient.java
 * @Author： chenpan
 * @Date：2024/12/7 14:53
 * @Modify：
 */
@TableName(value = "rbac_oauth2_registered_client")
@Data
@Schema(name = "OAuth2RegisteredClient", description = "客户端信息")
@EnableSelectOption
@LogicUnique(columns = "clientId", message = "客户端id已存在")
public class OAuth2RegisteredClient extends BaseEntity {

    /**
     * 客户端ID
     */
    @NotBlank(message = "client_id 不能为空")
    @Schema(description = "客户端id")
    @TableField(value = "client_id")
    private String clientId;

    @TableField(value = "client_name")
    private String clientName;

    @TableField(value = "client_id_issued_at")
    private LocalDateTime clientIdIssuedAt;

    @TableField(value = "client_secret")
    private String clientSecret;

    @TableField(value = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @TableField(value = "client_authentication_methods")
    private String clientAuthenticationMethods;

    @TableField(value = "authorization_grant_types")
    private String authorizationGrantTypes;

    @TableField(value = "redirect_uris")
    private String redirectUris;

    @TableField(value = "scopes")
    private String scopes;

    @TableField(value = "client_settings")
    private String clientSettings;

    @TableField(value = "token_settings")
    private String tokenSettings;
}
