package cn.healthcaredaas.data.cloud.foundation.mgmt.service;

import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Application;

import java.util.List;

/**

 * @ClassName： IMgmtApplicationService.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/14 17:25
 * @Modify：
 */
public interface IApplicationService {
    /**
     * 更加应用编码获取应用信息
     *
     * @param appCode
     * @return
     */
    Application getAppByCode(String appCode);

    /**
     * 获取用户的子应用
     *
     * @param appId
     * @param userId
     * @return
     */
    List<Application> getSubApp(String appId, String userId, Boolean isMicro);
}
