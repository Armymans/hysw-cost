package net.zlw.cloud.budgetTask.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgetTask.domain.vo.BudgetVoF;
import net.zlw.cloud.budgetTask.domain.vo.PriceControlVoF;
import net.zlw.cloud.budgetTask.service.BudgetTaskService;
import net.zlw.cloud.budgetTask.service.PriceInfoSevice;
import net.zlw.cloud.budgetTask.service.StatusSynchronService;
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

    @Autowired
    private PriceInfoSevice priceInfoSevice;

    @Autowired
    private StatusSynchronService statusSynchronService;

    /***
     * 预算成本信息报装接口
     * @param budgetVoF
     * @return
     */
    @RequestMapping(value = "api/getBudgetEngineering",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getBudgetEngineering(@RequestBody BudgetVoF budgetVoF){
        budgetTaskService.getBudgetEngineering(budgetVoF);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description 控价信息接口
        * @Date 19:45 2020/11/6
        * @Param
        * @return
     **/
    @RequestMapping(value = "api/getPriceControlEngineering",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getPriceControlEngineering(@RequestBody PriceControlVoF priceControlVoF){
       priceInfoSevice.getPriceInfo(priceControlVoF);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description 工单状态同步接口
        * @Date 22:44 2020/11/6
        * @Param
        * @return
     **/
    @RequestMapping(value = "/api/updateStatusSynchron",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateStatusSynchron(@RequestBody String application_num,String account){
        statusSynchronService.updateStatusSynchron(application_num,account);
        return RestUtil.success();
    }
}
