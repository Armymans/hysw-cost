package net.zlw.cloud.whDesignTask.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.whDesignTask.model.vo.DesignVoF;
import net.zlw.cloud.whDesignTask.service.DesinTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Classname 设计任务报装接口
 * @Description TODO
 * @Date 2020/10/29 9:18
 * @Created sjf
 */

@RestController
public class DesinTaskController extends BaseController {


    @Autowired
    private DesinTaskService desinTaskService;

    @RequestMapping(value = "/api/getDesignEngineering", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
        public Map<String,Object> designEngineering(@RequestBody DesignVoF designVoF){
        desinTaskService.getDesignEngineering(designVoF,request);
        return RestUtil.success();
    }

}
