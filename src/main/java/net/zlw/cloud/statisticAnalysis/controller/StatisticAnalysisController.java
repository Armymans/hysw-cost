package net.zlw.cloud.statisticAnalysis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.statisticAnalysis.model.PerformanceAccrualAndSummaryList;
import net.zlw.cloud.statisticAnalysis.model.StatisticAnalysis;
import net.zlw.cloud.statisticAnalysis.service.StatusticAnalysisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticAnalysisController {
    @Resource
    private StatusticAnalysisService statusticAnalysisService;

    //造价绩效统计
    @RequestMapping(value = "/statisticAnalysis/findAnalysis",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAnalysis(pageVo pageVo){
        StatisticAnalysis analysis = statusticAnalysisService.findAnalysis(pageVo);
        return RestUtil.success(analysis);

    }
    //绩效计提汇总
    @RequestMapping(value = "/statisticAnalysis/performanceAccrualAndSummary",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> PerformanceAccrualAndSummary(pageVo pageVo){
       String summary =  statusticAnalysisService.performanceAccrualAndSummary(pageVo);
       return RestUtil.success(JSONArray.parseArray(summary));
    }
    //绩效计提汇总-列表数据
    @RequestMapping(value = "/statisticAnalysis/performanceAccrualAndSummaryList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> performanceAccrualAndSummaryList(pageVo pageVo){
        PerformanceAccrualAndSummaryList performanceAccrualAndSummaryList = statusticAnalysisService.performanceAccrualAndSummaryList(pageVo);
        return RestUtil.success(performanceAccrualAndSummaryList);
    }

}
