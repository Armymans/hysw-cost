package net.zlw.cloud.jbDesignTask.controller;


import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.jbDesignTask.domain.vo.JbDesignVoF;
import net.zlw.cloud.jbDesignTask.domain.vo.WjBudgetVoF;
import net.zlw.cloud.jbDesignTask.domain.vo.WjDesignVoF;
import net.zlw.cloud.jbDesignTask.service.WjDesignTaskService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/***
 * 吴江设计报装
 */
@RestController
public class WjDesignController extends BaseController {

    @Resource
    private WjDesignTaskService wjDesignTaskService;

    // 吴江设计报装
    @RequestMapping(value = "/api/getWjDesign", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getWjDesignEngineering(@RequestBody WjDesignVoF wjDesignVoF){
        wjDesignTaskService.getWjDesignTask(wjDesignVoF,request);
        return RestUtil.success();
    }
    // 吴江预算报装
    @RequestMapping(value = "/api/getWjBudget", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getWjBudgetEngineering(@RequestBody WjBudgetVoF wjBudgetVoF){
        wjDesignTaskService.getWjBudgetEngineering(wjBudgetVoF,request);
        return RestUtil.success();
    }
}
