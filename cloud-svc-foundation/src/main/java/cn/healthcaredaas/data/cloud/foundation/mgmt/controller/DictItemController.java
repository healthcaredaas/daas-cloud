package cn.healthcaredaas.data.cloud.foundation.mgmt.controller;

import cn.healthcaredaas.data.cloud.core.rest.RestResult;
import cn.healthcaredaas.data.cloud.foundation.mgmt.model.DictItem;
import cn.healthcaredaas.data.cloud.foundation.mgmt.service.IDictItemService;
import cn.healthcaredaas.data.cloud.web.rest.controller.BaseCRUDController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**

 * @ClassName： DictItemController.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/12 16:08
 * @Modify：
 */
@RestController
@RequestMapping(MgmtApi.PREFIX + "/dictItem")
@Tags({
        @Tag(name = "系统字典内容接口")
})
public class DictItemController extends BaseCRUDController<DictItem, DictItem, DictItem> {

    @Autowired
    private IDictItemService itemService;

    @GetMapping("/content")
    @Operation(summary = "查询字典内容")
    public RestResult<List<DictItem>> content(@RequestParam("code") String code) {
        List<DictItem> items = itemService.getItemsByDictCode(code);
        return result(items);
    }
}