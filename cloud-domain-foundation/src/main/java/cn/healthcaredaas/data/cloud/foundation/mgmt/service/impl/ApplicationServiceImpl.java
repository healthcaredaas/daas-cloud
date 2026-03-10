package cn.healthcaredaas.data.cloud.foundation.mgmt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.healthcaredaas.data.cloud.foundation.mgmt.dao.ApplicationDao;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Application;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IApplicationService;
import cn.healthcaredaas.data.cloud.data.core.utils.tree.TreeNode;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import cn.healthcaredaas.data.cloud.web.core.enums.AuthorityType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**

 * @ClassName： MgmtApplicationServiceImpl.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/14 17:26
 * @Modify：
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<ApplicationDao, Application>
        implements IApplicationService {

    @Resource
    private ApplicationDao applicationDao;

    /**
     * 更加应用编码获取应用信息
     *
     * @param appCode
     * @return
     */
    @Override
    public Application getAppByCode(String appCode) {
        LambdaQueryWrapper<Application> qw = new LambdaQueryWrapper();
        qw.eq(Application::getAppCode, appCode);
        return applicationDao.selectOne(qw);
    }

    /**
     * 获取用户的子应用
     *
     * @param appId
     * @param userId
     * @param isMirco
     * @return
     */
    @Override
    public List<Application> getSubApp(String appId, String userId, Boolean isMicro) {
        List<Application> appList =  applicationDao.selectUserApps(appId, isMicro,
                userId, AuthorityType.APPLICATION);
        if (CollectionUtil.isEmpty(appList)) {
            return Collections.emptyList();
        }
        return TreeNode.toTree(appList);
    }
}
