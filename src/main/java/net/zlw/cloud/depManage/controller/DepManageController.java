package net.zlw.cloud.depManage.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.depManage.domain.DepManage;
import net.zlw.cloud.depManage.mapper.MemberManageMapper;
import net.zlw.cloud.depManage.service.DepManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**   部门管理列表数据  事物层
 * Created by xulei on 2020/9/18.
 */
@RestController
@RequestMapping("/api/depManage")
public class DepManageController extends BaseController {
    @Resource
    private DepManageService depManageService;

    @RequestMapping(value = "/depManagefindAll",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAll(){
        List<DepManage> list =  depManageService.findAll();
        return RestUtil.success(list);
    }


}
