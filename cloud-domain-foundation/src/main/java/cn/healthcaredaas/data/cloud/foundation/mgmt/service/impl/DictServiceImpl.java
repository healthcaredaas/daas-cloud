package cn.healthcaredaas.data.cloud.foundation.mgmt.service.impl;

import cn.healthcaredaas.data.cloud.foundation.mgmt.dao.DictDao;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Dict;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IDictService;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

/**

 * @ClassName： DictServiceImpl.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/12 16:01
 * @Modify：
 */
@Service
public class DictServiceImpl extends BaseServiceImpl<DictDao, Dict>
        implements IDictService {
    /**
     * 根据编码获取字典信息
     *
     * @param code
     * @return
     */
    @Override
    public Dict getDictByCode(String code) {
        LambdaQueryWrapper<Dict> qw = new LambdaQueryWrapper<>();
        qw.eq(Dict::getCode, code);
        return super.getOne(qw);
    }
}
