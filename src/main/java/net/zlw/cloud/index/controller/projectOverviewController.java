package net.zlw.cloud.index.controller;

import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.index.model.vo.ModuleNumber;
import net.zlw.cloud.index.model.vo.StatisticalData;
import net.zlw.cloud.index.service.MessageNotificationService;
import net.zlw.cloud.index.service.ProjectOverviewService;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/projectOverview")
public class projectOverviewController extends BaseController {
    @Resource
    private ProjectOverviewService projectOverviewService;
    @Resource
    private MessageNotificationService messageNotificationService;
    @Resource
    private BaseProjectService baseProjectService;

    //查询代办条数
    @GetMapping("/findCommissionCount")
    public Integer findCommissionCount(){
       Integer i =  projectOverviewService.findCommissionCount(getLoginUser());
       return i;
    }
    //消息提醒数据
//    message_notification
    @GetMapping("/findMessage")
    public List<MessageNotification> findMessage(){
      return  messageNotificationService.findMessage();
    }
    //造价部门和设计部门模块数量
    @GetMapping("/moduleNumber")
    public List<ModuleNumber> moduleNumber(){
        List<BaseProject> allBaseProject = baseProjectService.findAllBaseProject();
       return projectOverviewService.moduleNumber(allBaseProject);
    }
    //造价部门和设计部门统计数据

    @GetMapping("/findStatisticalData")
    public StatisticalData findStatisticalData(PageVo pageVo) throws ParseException {
        if (!pageVo.getDistrict().equals("") && pageVo.getDistrict()!=null){
            if (pageVo.getDistrict().equals("4")){
               return messageNotificationService.findStatisticalDataWujiang(pageVo);
            }else{
                return messageNotificationService.findStatisticalDataAnhui(pageVo);
            }
        }else{
          return   messageNotificationService.findAllStatisticalData(pageVo);
        }
    }


}
