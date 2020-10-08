package net.zlw.cloud.statisticAnalysis.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.statisticAnalysis.model.StatisticAnalysis;
import net.zlw.cloud.statisticAnalysis.service.StatusticAnalysisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StatisticAnalysisController {
    @Resource
    private StatusticAnalysisService statusticAnalysisService;

    //造价绩效统计
    @RequestMapping(value = "/statisticAnalysis/findAnalysis",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public StatisticAnalysis findAnalysis(pageVo pageVo){
       return statusticAnalysisService.findAnalysis(pageVo);

    }
}
