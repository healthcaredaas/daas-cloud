package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.annotation.SelectLikeColumn;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: chenpan
 * @Date: 2020-06-16 17:03:46
 * @Description: 用户扩展属性配置
 */
@TableName(value = "rbac_user_ext_config")
@Data
@ToString(callSuper = true)
@EnableSelectOption
@LogicUnique(columns = {"extKey"}, message = "属性已存在！")
public class RbacUserExtConfig extends BaseEntity {

    @Schema(description = "属性标识")
    @TableField(value = "ext_key")
    @SelectLikeColumn(wildcardPosition = SelectLikeColumn.WildcardPosition.BOTH)
    private String extKey;

    @Schema(description = "属性名称")
    @TableField(value = "ext_name")
    @SelectLikeColumn(wildcardPosition = SelectLikeColumn.WildcardPosition.BOTH)
    private String extName;

    @Schema(description = "属性取值类型")
    @TableField(value = "ext_type")
    private String extType;

    @Schema(description = "字典取值URL")
    @TableField(value = "dic_url")
    private String dicUrl;

    @Schema(description = "本地字典 json数组")
    @TableField(value = "dic_data")
    private String dicData;

    @Schema(description = "字典value属性")
    @TableField(value = "dic_props_value")
    private String dicPropsValue;

    @Schema(description = "字典label属性")
    @TableField(value = "dic_props_label")
    private String dicPropsLabel;

    @Schema(description = "界面填写样式")
    @TableField(value = "fill_in_type")
    private String fillInType;

}
