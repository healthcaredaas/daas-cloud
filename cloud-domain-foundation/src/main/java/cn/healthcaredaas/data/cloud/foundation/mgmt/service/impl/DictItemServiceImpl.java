package cn.healthcaredaas.data.cloud.foundation.mgmt.service.impl;

import cn.healthcaredaas.data.cloud.core.utils.CheckUtils;
import cn.healthcaredaas.data.cloud.foundation.mgmt.dao.DictItemDao;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Dict;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.DictItem;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IDictItemService;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IDictService;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**

 * @ClassName： DictItemServiceIMpl.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/18 17:59
 * @Modify：
 */
@Service
public class DictItemServiceImpl extends BaseServiceImpl<DictItemDao, DictItem>
        implements IDictItemService {

    @Autowired
    private IDictService dictService;

    @Autowired
    private DictItemDao dictItemDao;

    /**
     * 根据字典编码获取字典内容
     *
     * @param code
     * @return
     */
    @Override
    public List<DictItem> getItemsByDictCode(@NotNull String code) {
        Dict dict = dictService.getDictByCode(code);
        CheckUtils.notEmpty(dict, String.format("字典【%s】不存在", code));

        LambdaQueryWrapper<DictItem> qw = new LambdaQueryWrapper<>();
        qw.eq(DictItem::getDictId, dict.getId())
                .orderByAsc(DictItem::getOrderSeq)
                .orderByAsc(DictItem::getValue);

        return dictItemDao.selectList(qw);
    }
}
