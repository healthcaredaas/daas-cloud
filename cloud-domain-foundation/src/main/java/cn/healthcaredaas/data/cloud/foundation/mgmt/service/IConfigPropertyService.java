package cn.healthcaredaas.data.cloud.foundation.mgmt.service;

/**

 * @ClassName： IConfigPropertyService.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/3 14:16
 * @Modify：
 */
public interface IConfigPropertyService {

    Object getPropertyByAppAndCode(String appCode, String configCode);
}
