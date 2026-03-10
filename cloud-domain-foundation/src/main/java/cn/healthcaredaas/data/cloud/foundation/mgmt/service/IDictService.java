package cn.healthcaredaas.data.cloud.foundation.mgmt.service;

import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Dict;

/**

 * @ClassName： IDictService.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/12 16:01
 * @Modify：
 */
public interface IDictService {
    /**
     * 根据编码获取字典信息
     *
     * @param code
     * @return
     */
    Dict getDictByCode(String code);
}
