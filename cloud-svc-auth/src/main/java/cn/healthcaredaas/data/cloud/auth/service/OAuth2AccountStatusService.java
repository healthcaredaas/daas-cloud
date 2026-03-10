package cn.healthcaredaas.data.cloud.auth.service;

import cn.healthcaredaas.data.cloud.foundation.rbac.enums.AccountStatusEnum;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IUserService;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**

 * @ClassName： OAuth2AccountStatusService.java
 * @Author： chenpan
 * @Date：2024/12/15 17:14
 * @Modify：
 */
@Service
public class OAuth2AccountStatusService {

    @Resource
    private IUserService userService;

    @Resource
    private RedissonClient redissonClient;

    public void lock(String username) {
        userService.changeStatusByUsername(username, AccountStatusEnum.LOCKED);
    }

    public void unlock(String username) {
        userService.changeStatusByUsername(username, AccountStatusEnum.VALID);
    }
}