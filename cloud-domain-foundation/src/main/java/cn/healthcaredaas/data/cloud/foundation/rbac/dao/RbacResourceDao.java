package cn.healthcaredaas.data.cloud.foundation.rbac.dao;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.web.core.enums.ResourceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @ClassName： RbacResourceDao.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/13 15:53
 * @Modify：
 */
@Mapper
public interface RbacResourceDao extends BaseMapper<RbacResource> {
    List<String> findResouceRoles(@Param("url") String url,
                                  @Param("method") String method,
                                  @Param("type") ResourceType type);

    List<RbacResource> selectRoleResource(@Param("role") String role,
                                          @Param("type") ResourceType type);

    List<RbacResource> findUserResource(@Param("userId") String userId);

    List<RbacResource> selectUserResources(@Param("appIds") String[] appIds,
                                           @Param("userId") String userId,
                                           @Param("types") ResourceType[] resourceTypes);
}
