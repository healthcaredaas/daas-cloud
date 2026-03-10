package cn.healthcaredaas.data.cloud.auth.controller;

import cn.healthcaredaas.data.cloud.auth.vo.RegisteredClientVo;
import cn.healthcaredaas.data.cloud.auth.helper.RegisteredClientHelper;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.OAuth2RegisteredClient;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**

 * @ClassName： OAuth2RegisteredClientController.java
 * @Author： chenpan
 * @Date：2024/12/14 14:41
 * @Modify：
 */
@RestController
@RequestMapping("client")
@Tags({
        @Tag(name = "客户端管理")
})
public class OAuth2RegisteredClientController extends BaseCRUDController<OAuth2RegisteredClient, RegisteredClientVo, OAuth2RegisteredClient> {

    @Resource
    private PasswordEncoder passwordEncoder;

    private final RegisteredClientHelper clientHelper;

    /**
     * 是否转换结果对象
     *
     * @return
     */
    @Override
    public boolean isConvertEntity() {
        return true;
    }

    public OAuth2RegisteredClientController() {
        clientHelper = new RegisteredClientHelper(passwordEncoder);
    }

    @Override
    public OAuth2RegisteredClient vo2Entity(RegisteredClientVo registeredClientVo) {
        return clientHelper.vo2Model(registeredClientVo);
    }

    @Override
    public RegisteredClientVo entity2Vo(OAuth2RegisteredClient oAuth2RegisteredClient) {
        return clientHelper.model2Vo(oAuth2RegisteredClient);
    }
}