package cn.healthcaredaas.data.cloud.foundation.rbac.dao;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author chenpan
 * @date 2021/1/19 14:58
 */
@Mapper
public interface RbacRoleDao extends BaseMapper<RbacRole> {

    List<RbacRole> selectUserRole(@Param("userId") String userId);
}
