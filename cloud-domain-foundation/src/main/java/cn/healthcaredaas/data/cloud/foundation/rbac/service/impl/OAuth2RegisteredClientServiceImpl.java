package cn.healthcaredaas.data.cloud.foundation.rbac.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.OAuth2RegisteredClientDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.OAuth2RegisteredClient;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IOAuth2RegisteredClientService;
import cn.healthcaredaas.data.cloud.security.core.utils.SecurityUtils;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**

 * @ClassName： OAuth2RegisteredClientServiceImpl.java
 * @Author： chenpan
 * @Date：2024/12/7 15:18
 * @Modify：
 */
@Service
public class OAuth2RegisteredClientServiceImpl extends BaseServiceImpl<OAuth2RegisteredClientDao, OAuth2RegisteredClient>
        implements IOAuth2RegisteredClientService {

    @Resource
    private OAuth2RegisteredClientDao registeredClientDao;

    @Override
    public OAuth2RegisteredClient getByClientId(String clientId) {
        LambdaQueryWrapper<OAuth2RegisteredClient> qw = new LambdaQueryWrapper<>();
        qw.eq(OAuth2RegisteredClient::getClientId, clientId);
        return registeredClientDao.selectOne(qw);
    }

    /**
     * 保存对象
     *
     * @param entity
     * @return
     */
    @Override
    public boolean add(OAuth2RegisteredClient entity) {
        if (StrUtil.isNotBlank(entity.getClientSecret())) {
            entity.setClientSecret(SecurityUtils.encrypt(entity.getClientSecret()));
        }
        if (StrUtil.isBlank(entity.getClientSettings())) {

        }
        if (StrUtil.isBlank(entity.getTokenSettings())) {

        }
        return super.saveOrUpdate(entity);
    }
}
