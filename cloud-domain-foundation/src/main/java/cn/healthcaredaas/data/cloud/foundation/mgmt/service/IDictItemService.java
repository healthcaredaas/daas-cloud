package cn.healthcaredaas.data.cloud.foundation.mgmt.service;

import cn.healthcaredaas.data.cloud.foundation.mgmt.model.DictItem;

import java.util.List;

/**

 * @ClassName： IDictItemService.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/18 17:58
 * @Modify：
 */
public interface IDictItemService {
    /**
     * 根据字典编码获取字典内容
     *
     * @param code
     * @return
     */
    List<DictItem> getItemsByDictCode(String code);
}
