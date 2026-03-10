package cn.healthcaredaas.data.cloud.foundation.mgmt.service.impl;

import cn.healthcaredaas.data.cloud.foundation.mgmt.dao.ConfigPropertyDao;
import cn.healthcaredaas.data.cloud.foundation.mgmt.enums.ConfigType;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Application;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.ConfigProperty;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IApplicationService;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IConfigPropertyService;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 * @ClassName： ConfigPropertyServiceImpl.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/3 14:17
 * @Modify：
 */
@Service
public class ConfigPropertyServiceImpl extends BaseServiceImpl<ConfigPropertyDao, ConfigProperty>
        implements IConfigPropertyService {

    @Autowired
    private IApplicationService appService;

    @Override
    public Object getPropertyByAppAndCode(String appCode, String configCode) {
        Application app = appService.getAppByCode(appCode);
        if (app == null) {
            return null;
        }
        LambdaQueryWrapper<ConfigProperty> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigProperty::getAppId, app.getId())
                .eq(ConfigProperty::getCode, configCode);
        ConfigProperty config = baseMapper.selectOne(queryWrapper);
        if (config != null) {
            return config.getType() == ConfigType.JSON ? JSON.parse(config.getContent()) : config.getContent();
        }
        return null;
    }
}
