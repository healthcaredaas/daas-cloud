package cn.healthcaredaas.data.cloud.foundation.rbac.dao;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.OAuth2RegisteredClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**

 * @ClassName： OAuth2RegisteredClientDao.java
 * @Author： chenpan
 * @Date：2024/12/7 15:17
 * @Modify：
 */
@Mapper
public interface OAuth2RegisteredClientDao extends BaseMapper<OAuth2RegisteredClient> {
}
