package cn.healthcaredaas.data.cloud.foundation.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.healthcaredaas.data.cloud.audit.model.LoginLog;

/**
 * @ClassName： LoginLogDao.java
 * @Description: 登录日志
 * @Author： chenpan
 * @Date：2025-06-06 21:26:01
 * @Modify：
 */
@Mapper
public interface LoginLogDao extends BaseMapper<LoginLog> {

}
