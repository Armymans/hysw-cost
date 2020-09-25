package net.zlw.cloud.costAssessmentDesign.controller;

import net.tec.cloud.common.util.RestUtil;
import net.zlw.cloud.costAssessmentDesign.model.CostAssessmentDesing;
import net.zlw.cloud.costAssessmentDesign.service.CostAssessmentDesignService;
import net.zlw.cloud.designAssessSettings.model.AssessmentDesign;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**  造价 -考核设计
 * Created by xulei on 2020/9/18.
 */
@RestController
@RequestMapping("/api/costAssessmentDesing")
public class CostAssessmentDesignController {

    @Resource
    private CostAssessmentDesignService costAssessmentDesignService;




    /**
     * @Author Armyman
     * @Description // 根据id查找
     * @Date 21:10 2020/9/18
     **/
    @GetMapping("/findById")
    public Map<String, Object> findById(String id){
        CostAssessmentDesing assessmentDesign = costAssessmentDesignService.findById(id);
        return RestUtil.success(assessmentDesign);
    }


    /**
     * @Author Armyman
     * @Description // 更新
     * @Date 21:10 2020/9/18
     **/
    @PostMapping("/update")
    public void update(@RequestBody AssessmentDesign assessmentDesign){
        costAssessmentDesignService.update(assessmentDesign);
    }

}
