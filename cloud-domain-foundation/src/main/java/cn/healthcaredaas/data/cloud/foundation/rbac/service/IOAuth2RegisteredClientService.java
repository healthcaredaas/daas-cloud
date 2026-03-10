package cn.healthcaredaas.data.cloud.foundation.rbac.service;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.OAuth2RegisteredClient;

/**

 * @ClassName： IOAuth2RegisteredClientService.java
 * @Author： chenpan
 * @Date：2024/12/7 15:18
 * @Modify：
 */
public interface IOAuth2RegisteredClientService {

    boolean add(OAuth2RegisteredClient toEntity);

    OAuth2RegisteredClient getById(String id);

    OAuth2RegisteredClient getByClientId(String clientId);
}
