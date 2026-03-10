package cn.healthcaredaas.data.cloud.foundation.mgmt.model;

import cn.healthcaredaas.data.cloud.data.core.annotation.*;
import cn.healthcaredaas.data.cloud.foundation.mgmt.dao.DictItemDao;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**

 * @ClassName： Dict.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/12 15:52
 * @Modify：
 */
@TableName(value = "mgmt_dict")
@Data
@Schema(name = "Dict", description = "系统字典")
@EnableSelectOption
@LogicUnique(columns = {"code"}, message = "当前字典【{code}】已存在!")
@DeleteOption(relations = {
        @Relation(entity = DictItem.class, relationMapper = DictItemDao.class,
                conditions = {@RelationCondition(relationField = "dictId")})
})
public class Dict extends BaseEntity {

    public Dict() {
        this.setSortBy("code");
    }

    @Schema(description = "字典编码")
    @TableField(value = "code")
    private String code;

    @Schema(description = "字典名称")
    @TableField(value = "name")
    private String name;
}
