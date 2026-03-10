package cn.healthcaredaas.data.cloud.auth.entity;

import lombok.Data;

/**

 * @ClassName： OAuth2AccessTokenResponse.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/10 10:56
 * @Modify：
 */
@Data
public class OAuth2AccessTokenDto {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiresIn;

    private String scope;
}
