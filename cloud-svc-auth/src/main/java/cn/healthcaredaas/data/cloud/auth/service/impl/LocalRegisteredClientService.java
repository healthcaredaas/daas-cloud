package cn.healthcaredaas.data.cloud.auth.service.impl;

import cn.healthcaredaas.data.cloud.auth.helper.RegisteredClientHelper;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.OAuth2RegisteredClient;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IOAuth2RegisteredClientService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**

 * @ClassName： LocalClientDetailsService.java
 * @Author： chenpan
 * @Date：2024/12/7 13:43
 * @Modify：
 */
@Service
public class LocalRegisteredClientService implements RegisteredClientRepository {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IOAuth2RegisteredClientService registeredClientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientHelper clientHelper = new RegisteredClientHelper(passwordEncoder);
        registeredClientService.add(clientHelper.toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        OAuth2RegisteredClient client = registeredClientService.getById(id);
        if (ObjectUtils.isNotEmpty(client)) {
            RegisteredClientHelper clientHelper = new RegisteredClientHelper(passwordEncoder);
            return clientHelper.toObject(client);
        }
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OAuth2RegisteredClient client = registeredClientService.getByClientId(clientId);
        if (ObjectUtils.isNotEmpty(client)) {
            RegisteredClientHelper clientHelper = new RegisteredClientHelper(passwordEncoder);
            return clientHelper.toObject(client);
        }
        return null;
    }
}
