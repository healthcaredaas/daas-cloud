package cn.healthcaredaas.data.cloud.foundation.mgmt.model;

import cn.healthcaredaas.data.cloud.core.enums.EnableStatusEnum;
import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseTreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**

 * @ClassName： Application.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/3/14 17:08
 * @Modify：
 */
@TableName(value = "mgmt_application")
@Data
@Schema(name = "Application", description = "系统应用")
@EnableSelectOption
public class Application extends BaseTreeEntity<Application> {

    public Application() {
        this.setSortBy("orderNo", "appCode");
    }

    @Schema(description = "应用编码")
    @TableField(value = "app_code")
    private String appCode;

    @Schema(description = "应用名称")
    @TableField(value = "app_name")
    private String appName;

    @Schema(description = "应用英文名")
    @TableField(value = "app_name_en")
    private String appNameEn;

    @Schema(description = "应用缩写名称")
    @TableField(value = "app_abbr")
    private String appAbbr;

    @Schema(description = "应用图标")
    @TableField(value = "app_icon")
    private String appIcon;

    @Schema(description = "应用路径")
    @TableField(value = "app_path")
    private String appPath;

    @Schema(description = "是否微应用")
    @TableField(value = "is_micro_app")
    private Boolean isMicroApp;

    @Schema(description = "应用地址")
    @TableField(value = "app_entry")
    private String appEntry;

    @Schema(description = "接口前缀")
    @TableField(value = "api_prefix")
    private String apiPrefix;

    @Schema(description = "状态")
    @TableField(value = "status")
    private EnableStatusEnum status;

    @Schema(description = "版本")
    @TableField(value = "app_version")
    private String appVersion;

    @Schema(description = "描述")
    @TableField(value = "memo")
    private String memo;

    @Schema(description = "序号")
    @TableField(value = "order_no")
    private Integer orderNo;
}
