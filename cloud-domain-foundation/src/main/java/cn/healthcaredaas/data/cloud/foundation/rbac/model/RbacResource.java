package cn.healthcaredaas.data.cloud.foundation.rbac.model;

import cn.healthcaredaas.data.cloud.core.enums.HttpMethod;
import cn.healthcaredaas.data.cloud.data.core.annotation.EnableSelectOption;
import cn.healthcaredaas.data.cloud.data.core.annotation.SelectLikeColumn;
import cn.healthcaredaas.data.cloud.data.core.entity.BaseTreeEntity;
import cn.healthcaredaas.data.cloud.web.core.enums.ResourceType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**

 * @ClassName： RbacResource.java
 * @Author： chenpan
 * @Date：2024/12/7 15:38
 * @Modify：
 */
@TableName(value = "rbac_resource")
@Data
@Schema(name = "RbacResource", description = "系统资源")
@EnableSelectOption
public class RbacResource extends BaseTreeEntity<RbacResource> {

    public RbacResource() {
        this.setSortBy("parentId", "orderNo");
    }

    @TableField(value = "application_id")
    @Schema(description = "所属应用")
    private String applicationId;

    @TableField(value = "resource_code")
    @Schema(description = "资源编码")
    @SelectLikeColumn(wildcardPosition = SelectLikeColumn.WildcardPosition.BOTH)
    private String resourceCode;

    @TableField(value = "resource_name")
    @Schema(description = "资源名称")
    @SelectLikeColumn(wildcardPosition = SelectLikeColumn.WildcardPosition.BOTH)
    private String resourceName;

    @TableField(value = "resource_type")
    private ResourceType resourceType;

    @TableField(value = "request_method")
    @Schema(description = "请求方法")
    private HttpMethod requestMethod;

    @TableField(value = "url")
    private String url;

    @TableField(value = "class_name")
    private String className;

    @TableField(value = "method_name")
    private String methodName;

    @TableField(value = "icon")
    @Schema(description = "图标")
    private String icon;

    @TableField(value = "order_no")
    @Schema(description = "显示顺序")
    private Integer orderNo;

    @TableField(value = "hidden_menu")
    @Schema(description = "菜单是否隐藏")
    private Boolean hiddenMenu;

    @TableField(value = "view_component")
    @Schema(description = "前端组件")
    private String viewComponent;

    @TableField(value = "view_url")
    @Schema(description = "前端地址")
    private String viewUrl;
}
