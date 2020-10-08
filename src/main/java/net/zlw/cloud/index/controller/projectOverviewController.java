package net.zlw.cloud.index.controller;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
       return RestUtil.success(i);
    }
    //消息提醒数据
//    message_notification
//    @GetMapping("/findMessage")
    @RequestMapping(value = "/projectOverview/findMessage",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findMessage(){
        List<MessageNotification> message = messageNotificationService.findMessage();
        return RestUtil.success(message);
    }
    //造价部门和设计部门模块数量
    @GetMapping("/moduleNumber")
    public List<ModuleNumber> moduleNumber(){
        List<BaseProject> allBaseProject = baseProjectService.findAllBaseProject();
       return projectOverviewService.moduleNumber(allBaseProject);
    }
    //造价部门和设计部门统计数据
//    @GetMapping("/findStatisticalData")/projectOverview/findStatisticalData
    @RequestMapping(value = "/projectOverview/findStatisticalData",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public StatisticalData findStatisticalData(pageVo pageVo) {
        StatisticalData statisticalData =  messageNotificationService.findStatisticalData(pageVo);
       return statisticalData;
    }
    //绩效发放统计图
    @RequestMapping(value = "/projectOverview/PerformanceDistributionChart/{id}",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public PerformanceDistributionChart findPerformanceDistributionChart(pageVo pageVo, @PathVariable(name = "id") String id){
        PerformanceDistributionChart performanceDistributionChart = messageNotificationService.findPerformanceDistributionChart(pageVo, id);
        return performanceDistributionChart;
    }


}
