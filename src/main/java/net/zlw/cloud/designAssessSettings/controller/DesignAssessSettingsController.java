package net.zlw.cloud.designAssessSettings.controller;

import net.tec.cloud.common.util.RestUtil;
import net.zlw.cloud.designAssessSettings.model.AssessmentDesign;
import net.zlw.cloud.designAssessSettings.service.DesignAssessSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author Armyman
 * @Description 设计-考核设计 分配层
 * @Date 2020/9/18 21:05
 **/

@RestController
@RequestMapping("/api/assessmentDesign")
public class DesignAssessSettingsController {


    @Autowired
    private DesignAssessSettingsService designAssessSettingsService;


    /**
     * @Author Armyman
     * @Description // 根据id查找
     * @Date 21:10 2020/9/18
     **/
    @GetMapping("/findById")
    public Map<String, Object> findById(String id){
        AssessmentDesign assessmentDesign = designAssessSettingsService.findById(id);
        return RestUtil.success(assessmentDesign);
    }


    /**
     * @Author Armyman
     * @Description // 更新
     * @Date 21:10 2020/9/18
     **/
    @PostMapping("/update")
    public void update(@RequestBody AssessmentDesign assessmentDesign){
        designAssessSettingsService.update(assessmentDesign);
    }

}
