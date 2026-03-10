package cn.healthcaredaas.data.cloud.foundation.rbac.dao;

import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author chenpan
 * @date 2021/1/19 14:34
 */
@Mapper
public interface RbacUserDao extends BaseMapper<RbacUser> {

}
