package net.zlw.cloud.budgetTask.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgetTask.domain.vo.BudgetVo;
import net.zlw.cloud.budgetTask.service.BudgetTaskService;
import net.zlw.cloud.common.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BudgetTaskController {


    @Autowired
    private BudgetTaskService budgetTaskService;

    @RequestMapping(value = "api/getBudgetEngineering",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getBudgetEngineering(@RequestBody BudgetVo budgetVo, String account){
        budgetTaskService.getBudgetEngineering(account, budgetVo);

        return RestUtil.success();
    }
}
