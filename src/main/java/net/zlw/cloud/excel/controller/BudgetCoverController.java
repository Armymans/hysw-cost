package net.zlw.cloud.excel.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.service.BudgetCoverService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class BudgetCoverController {
    @Resource
    private BudgetCoverService budgetCoverService;

    @RequestMapping(value = "/budgetCover/coverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> coverImport(){
        budgetCoverService.coverImport();
        return RestUtil.success();
    }


}
