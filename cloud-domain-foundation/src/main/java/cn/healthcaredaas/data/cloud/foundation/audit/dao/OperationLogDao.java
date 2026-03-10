package cn.healthcaredaas.data.cloud.foundation.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.healthcaredaas.data.cloud.audit.model.OperationLog;

/**
 * @ClassName： OperationLogDao.java
 * @Description: 操作日志
 * @Author： chenpan
 * @Date：2025-06-06 21:26:06
 * @Modify：
 */
@Mapper
public interface OperationLogDao extends BaseMapper<OperationLog> {

}
