package cn.healthcaredaas.data.cloud.foundation.mgmt.model;

import cn.healthcaredaas.data.cloud.foundation.mgmt.enums.ConfigType;
import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.LogicUnique;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <pre>配置参数</pre>
 *
 * @ClassName： ConfigProperty.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/3 11:30
 * @Modify：
 */
@TableName(value = "mgmt_config_property")
@Data
@Schema(name = "ConfigProperty", description = "配置参数")
@EnableSelectOption
@LogicUnique(columns = {"appId", "code"}, message = "当前应用的配置编码【{code}】已存在!")
public class ConfigProperty extends BaseEntity {

    public ConfigProperty() {
        this.setSortBy("appId", "code");
    }

    @Schema(description = "所属应用")
    @TableField(value = "app_id")
    private String appId;

    @Schema(description = "配置编码")
    @TableField(value = "code")
    private String code;

    @Schema(description = "配置内容")
    @TableField(value = "content")
    private String content;

    @Schema(description = "配置格式")
    @TableField(value = "type")
    @EnumValue
    private ConfigType type;

    @Schema(description = "描述")
    @TableField(value = "memo")
    private String memo;
}
