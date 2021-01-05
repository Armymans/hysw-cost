package net.zlw.cloud.jbDesignTask.controller;


import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.jbDesignTask.domain.vo.*;
import net.zlw.cloud.jbDesignTask.service.JbDesignTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/***
 * 江北外部接口
 */
@RestController
public class JbDesignTaskController extends BaseController {

    @Autowired
    private JbDesignTaskService jbDesignTaskService;


    /***
     * 江北设计报装
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/api/getDesignEngineeringTwo", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getDesignEngineering(@RequestBody JbDesignVoF jbDesignVoF){
        jbDesignTaskService.getDesignEngineering(jbDesignVoF,request);
        return RestUtil.success();
    }


    /***
     * 江北造价报装
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/api/getBudgetEngineeringTwo", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getBudgetEngineering(@RequestBody JbBudgetVoF jbBudgetVoF){
        jbDesignTaskService.getBudgetEngineering(jbBudgetVoF,request);
        return RestUtil.success();
    }

    // 预算金额更新接口
    @RequestMapping(value = "/api/updateBudgetAmount", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateBudgetAmount(@RequestBody AmountVo amountVo){
        jbDesignTaskService.updateBudgetAmount(amountVo,request);
        return RestUtil.success("操作成功");
    }

    // CEA更新接口
    @RequestMapping(value = "/api/updateCea", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateCea(@RequestBody CEAVo ceaVo){
        jbDesignTaskService.updateCea(ceaVo,request);
        return RestUtil.success("操作成功");
    }
}
