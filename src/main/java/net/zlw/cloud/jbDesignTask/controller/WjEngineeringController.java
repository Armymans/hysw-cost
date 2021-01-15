package net.zlw.cloud.jbDesignTask.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.jbDesignTask.domain.proVo.WjBudgetVoA;
import net.zlw.cloud.jbDesignTask.domain.proVo.WjDesignVoA;
import net.zlw.cloud.jbDesignTask.domain.proVo.WjSettlementVoA;
import net.zlw.cloud.jbDesignTask.domain.proVo.WjTrackVoA;
import net.zlw.cloud.jbDesignTask.domain.vo.WjBudgetVoF;
import net.zlw.cloud.jbDesignTask.service.WjEngineeringService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;


@RestController
public class WjEngineeringController extends BaseController {


    @Resource
    private WjEngineeringService wjEngineeringService;
    // 吴江工程管理
    @RequestMapping(value = "/api/getWjProjectEngineering", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getWjProjectEngineering(@RequestBody WjDesignVoA wjDesignVoA){
        try {
            wjEngineeringService.getWjProjectEngineering(wjDesignVoA,request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    // 预算任务接口
    @RequestMapping(value = "/api/getWjBudgetTask", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getWjBudgetTask(@RequestBody WjBudgetVoA wjBudgetVoA){
        try {
            wjEngineeringService.getWjBudgetTask(wjBudgetVoA,request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    // 结算任务接口
    @RequestMapping(value = "/api/getSettlementEngineering", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getSettlementEngineering(@RequestBody WjSettlementVoA wjSettlementVoA){
        try {
            wjEngineeringService.getSettlementEngineering(wjSettlementVoA,request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    // 跟踪审计任务接口
    @RequestMapping(value = "/api/getTrackEngineering", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getTrackEngineering(@RequestBody WjTrackVoA wjTrackVoA){
        try {
            wjEngineeringService.getTrackEngineering(wjTrackVoA,request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }
}
