package cn.healthcaredaas.data.cloud.foundation.mgmt.model;

import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.annotation.SelectInColumn;
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
@TableName(value = "mgmt_dict_item")
@Data
@Schema(name = "DictItem", description = "系统字典内容")
@EnableSelectOption
@LogicUnique(columns = {"dictId", "value"}, message = "当前字典内容【{value}】已存在!")
public class DictItem extends BaseEntity {

    public DictItem() {
        this.setSortBy("orderSeq", "value");
    }

    @Schema(description = "字典主键")
    @TableField(value = "dict_id")
    private String dictId;

    @Schema(description = "编码")
    @TableField(value = "value")
    private String value;

    @Schema(description = "显示值")
    @TableField(value = "label")
    private String label;

    @Schema(description = "显示顺序")
    @TableField(value = "order_seq")
    private Integer orderSeq;

    @Schema(description = "编码")
    @TableField(exist = false)
    @SelectInColumn(column = "value")
    private String[] valueList;
}
