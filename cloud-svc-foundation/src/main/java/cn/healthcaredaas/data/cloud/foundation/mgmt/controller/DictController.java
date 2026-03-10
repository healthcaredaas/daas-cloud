package cn.healthcaredaas.data.cloud.foundation.mgmt.controller;

import cn.healthcaredaas.data.cloud.foundation.mgmt.model.Dict;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

 * @ClassName： DictController.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/12 16:08
 * @Modify：
 */
@RestController
@RequestMapping(MgmtApi.PREFIX + "/dict")
@Tags({
        @Tag(name = "系统字典接口")
})
public class DictController extends BaseCRUDController<Dict, Dict, Dict> {
}