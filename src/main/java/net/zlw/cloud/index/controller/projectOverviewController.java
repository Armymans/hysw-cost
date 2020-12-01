package net.zlw.cloud.index.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.index.model.vo.ModuleNumber;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.index.model.vo.StatisticalData;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.index.service.MessageNotificationService;
import net.zlw.cloud.index.service.ProjectOverviewService;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
//@RequestMapping("/projectOverview")
public class projectOverviewController extends BaseController {
    @Resource
    private ProjectOverviewService projectOverviewService;
    @Resource
    private MessageNotificationService messageNotificationService;
    @Resource
    private BaseProjectService baseProjectService;

    //查询代办条数
//    @GetMapping("/findCommissionCount")
    @RequestMapping(value = "/projectOverview/findCommissionCount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findCommissionCount(){
       Integer i =  projectOverviewService.findCommissionCount(getLoginUser());
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("x1",i);
        return RestUtil.success(stringIntegerMap);
    }
    //消息提醒数据
//    message_notification
//    @GetMapping("/findMessage")
    @RequestMapping(value = "/projectOverview/findMessage",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findMessage(){
        List<MessageNotification> message = messageNotificationService.findMessage(getLoginUser());
        return RestUtil.success(message);
    }
    //造价部门和设计部门模块数量
//    @GetMapping("/moduleNumber")
    @RequestMapping(value = "/projectOverview/moduleNumber",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> moduleNumber(pageVo pageVo){

        if (pageVo.getDistrict().equals("芜湖")){
            pageVo.setDistrict("1");
        }else if(pageVo.getDistrict().equals("马鞍山")){
            pageVo.setDistrict("2");
        }else if(pageVo.getDistrict().equals("江北")){
            pageVo.setDistrict("3");
        }else if(pageVo.getDistrict().equals("吴江")){
            pageVo.setDistrict("4");
        }
        List<BaseProject> allBaseProject = baseProjectService.findAllBaseProject(pageVo);
        List<ModuleNumber> moduleNumbers = projectOverviewService.moduleNumber(allBaseProject);
        return RestUtil.success(moduleNumbers);
    }

    /**
     * 项目统计卡片 模块数量
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "/projectOverview/moduleNumber2",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> moduleNumber2(pageVo pageVo){

        if (pageVo.getDistrict().equals("芜湖")){
            pageVo.setDistrict("1");
        }else if(pageVo.getDistrict().equals("马鞍山")){
            pageVo.setDistrict("2");
        }else if(pageVo.getDistrict().equals("江北")){
            pageVo.setDistrict("3");
        }else if(pageVo.getDistrict().equals("吴江")){
            pageVo.setDistrict("4");
        }

        List<BaseProject> allBaseProject = baseProjectService.findAllBaseProject(pageVo);
        List<ModuleNumber> moduleNumbers = projectOverviewService.moduleNumber2(allBaseProject);
        return RestUtil.success(moduleNumbers);
    }
    //造价部门和设计部门统计数据
//    @GetMapping("/findStatisticalData")/projectOverview/findStatisticalData
    @RequestMapping(value = "/projectOverview/findStatisticalData",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findStatisticalData(pageVo pageVo) {
        StatisticalData statisticalData =  messageNotificationService.findStatisticalData(pageVo);
        return RestUtil.success(statisticalData);
    }
    //绩效发放统计图
    @RequestMapping(value = "/projectOverview/PerformanceDistributionChart",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPerformanceDistributionChart(pageVo pageVo){
        String performanceDistributionChart = messageNotificationService.findPerformanceDistributionChart(pageVo);
        JSONArray objects = JSON.parseArray(performanceDistributionChart);
        return RestUtil.success(objects);
    }
//    @RequestMapping(value = "/projectOverview/findStatisticalData",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)




}
