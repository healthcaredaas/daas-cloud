package cn.healthcaredaas.data.cloud.auth.vo;

import cn.healthcaredaas.data.cloud.core.utils.DateFormatterConstant;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**

 * @ClassName： RegisteredClientDto.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/15 14:06
 * @Modify：
 */
@Data
@Builder
public class RegisteredClientVo extends BaseEntity {

    @NotBlank(message = "客户端id不能为空")
    @Schema(description = "客户端id")
    private String clientId;

    @NotBlank(message = "客户端名称不能为空")
    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "注册时间")
    @DateTimeFormat(pattern = DateFormatterConstant.DATETIME_FORMAT)
    @JsonFormat(pattern = DateFormatterConstant.DATETIME_FORMAT,  timezone = "GMT+8")
    private LocalDateTime clientIdIssuedAt;

    @Schema(description = "过期时间")
    @DateTimeFormat(pattern = DateFormatterConstant.DATETIME_FORMAT)
    @JsonFormat(pattern = DateFormatterConstant.DATETIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime clientSecretExpiresAt;

    @NotBlank(message = "客户端密钥不能为空")
    @Schema(description = "客户端密钥")
    private String clientSecret;

    @NotEmpty(message = "客户端认证方法不能为空")
    @Schema(description = "客户端认证方法")
    private Set<String> clientAuthenticationMethods = new HashSet<>();

    @NotEmpty(message = "授权类型不能为空")
    @Schema(description = "授权类型")
    private Set<String> authorizationGrantTypes = new HashSet<>();

    @NotEmpty(message = "回调地址不能为空")
    @Schema(description = "回调地址")
    private Set<String> redirectUris = new HashSet<>();

    @NotEmpty(message = "访问范围不能为空")
    @Schema(description = "访问范围")
    private Set<String> scopes = new HashSet<>();

    @Schema(description = "客户端设置")
    private ClientSettingsConvert clientSettings;

    @Schema(description = "token设置")
    private TokenSettingsConvert tokenSettings;

    @Data
    @AllArgsConstructor
    public static class ClientSettingsConvert {

        @Schema(description = "是否验证密钥")
        private Boolean requireProofKey;

        @Schema(description = "是否需要授权同意")
        private Boolean requireAuthorizationConsent = true;
    }

    @Data
    @Builder
    public static class TokenSettingsConvert {

        @Schema(description = "是否重复使用refresh_token")
        private Boolean reuseRefreshTokens = true;

        @Schema(description = "token签名算法")
        private String idTokenSignatureAlgorithm = SignatureAlgorithm.RS256.getName();

        @Schema(description = "refresh_token有效期（单位：秒）")
        private Long refreshTokenTimeToLive = 864000L;

        @Schema(description = "token有效期（单位：秒）")
        private Long accessTokenTimeToLive = 7200L;

        @Schema(description = "token格式")
        private String accessTokenFormat = OAuth2TokenFormat.SELF_CONTAINED.getValue();

        @Schema(description = "授权码有效期（单位：秒）")
        private Long authorizationCodeTimeToLive = 300L;
    }
}
