package cn.healthcaredaas.data.cloud.foundation.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**

 * @ClassName： AccoutStatusEnum.java
 * @Author： chenpan
 * @Date：2024/12/1 15:15
 * @Modify：
 */
@Schema(description = "账号状态")
public enum AccountStatusEnum {

    LOCKED("0", "锁定"),
    VALID("1", "有效"),
    EXPIRED("2", "过期");

    @Getter
    @EnumValue
    @JsonValue
    private String code;

    @Getter
    private String label;

    AccountStatusEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
