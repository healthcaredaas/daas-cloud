package cn.healthcaredaas.data.cloud.foundation.mgmt.dao;

import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Application;
import cn.healthcaredaas.data.cloud.web.core.enums.AuthorityType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @ClassName： MgmtApplicationDao.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/14 17:17
 * @Modify：
 */
@Mapper
public interface ApplicationDao extends BaseMapper<Application> {
    List<Application> selectUserApps(@Param("appId") String appId,
                                     @Param("isMicro") Boolean isMicro,
                                     @Param("userId") String userId,
                                     @Param("type") AuthorityType authorityType);
}