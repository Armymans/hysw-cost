package net.zlw.cloud.jbDesignTask.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.jbDesignTask.domain.vo.JbBudgetVo;
import net.zlw.cloud.jbDesignTask.domain.vo.JbDesignVo;
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
public class JbDesignTaskController {

    @Autowired
    private JbDesignTaskService jbDesignTaskService;


    /***
     * 江北设计报装
     * @param designVo
     * @param account
     * @return
     */
    @RequestMapping(value = "/api/getDesignEngineeringTwo", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getDesignEngineering(@RequestBody JbDesignVo designVo, String account){
        jbDesignTaskService.getDesignEngineering(designVo,account);
        return RestUtil.success();
    }


    /***
     * 江北造价报装
     * @param budgetVo
     * @param account
     * @return
     */
    @RequestMapping(value = "/api/getBudgetEngineeringTwo", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getBudgetEngineering(@RequestBody JbBudgetVo budgetVo, String account){
        jbDesignTaskService.getBudgetEngineering(budgetVo,account);
        return RestUtil.success();
    }
}
