package cn.healthcaredaas.data.cloud.foundation.rbac.dao;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacDataAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**

 * @ClassName： DataAuthorityDao.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/7/20 16:07
 * @Modify：
 */
@Mapper
public interface RbacDataAuthorityDao extends BaseMapper<RbacDataAuthority> {
    List<RbacDataAuthority> selectUserDataAuth(String userId);

    List<RbacDataAuthority> selectRoleDataAuth(String roleId);

    List<RbacDataAuthority> selectUserAndRoleDataAuth(String userId);
}
