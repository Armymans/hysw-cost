package net.zlw.cloud.designProject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import javafx.geometry.Pos;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectService;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ProjectController extends BaseController {

    @Resource
    private ProjectService projectService;

    /**
     * 设计列表展示 条件查询
     *
     * @param designPageVo
     * @return
     */
    @RequestMapping(value = "/api/disproject/disProjectSelect", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectSelect(DesignPageVo designPageVo) {
        PageInfo<DesignInfo> designInfoPageInfo = projectService.designProjectSelect(designPageVo, getLoginUser());
        return RestUtil.page(designInfoPageInfo);
    }

    /**
     * 设计项目合并
     *
     * @param mergeName
     * @param mergeNum
     * @param idlist
     */
    @RequestMapping(value = "/margeProject", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> margeProject(String mergeName,String mergeNum,String idlist) {
        String[] split = idlist.split(",");
        for (String id : split) {
            boolean b = projectService.mergeProject(mergeName, mergeNum, id);
            if(!b){
                return RestUtil.error("操作失败");
            }
        }
        return RestUtil.success("操作成功");
    }

    /**
     * 根据id查询合并项目列表
     * @param idlist
     */
    @RequestMapping(value = "/api/disproject/mergeProjectList", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> mergeProjectList(String idlist) {
        ArrayList<DesignInfo> designInfos = new ArrayList<>();
        String[] split = idlist.split(",");
        for (String id : split) {
            DesignInfo designInfo = projectService.mergeProjectList(id);
            designInfos.add(designInfo);
        }
        return RestUtil.success(designInfos);
    }

    /**
     * 合并项目列表判断是否未主项目
     * @param idlist
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/mergeProjectListById", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> mergeProjectListById(String idlist,String id){
        ArrayList<DesignInfo> designInfos = new ArrayList<>();
        String projectName = "";
        String projectNum = "";
        String[] split = idlist.split(",");
        for (String s : split) {
            DesignInfo designInfo = projectService.mergeProjectList(s);
            if(designInfo.getId().equals(id)){
                projectName = designInfo.getProjectName();
                projectNum = designInfo.getProjectNum();
                projectService.updateMergeProject0(designInfo.getBaseProjectId());
                designInfos.add(designInfo);
            }else{
                projectService.updateMergeProject1(designInfo.getBaseProjectId());
            }
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("projectName",projectName);
        concurrentHashMap.put("projectNum",projectNum);
        concurrentHashMap.put("designInfos",designInfos);
        return RestUtil.success(concurrentHashMap);
    }


    /**
     * 批量审核接口
     *
     * @param projectVo2
     */
    @PostMapping("/batchAudit")
    public void batchAudit(@RequestBody ProjectVo2 projectVo2) {
        String[] split = projectVo2.getIdlist().split(",");
        for (String id : split) {
            projectService.batchAudit(id, projectVo2.getAuditInfo(), getLoginUser());

        }
    }

    /**
     * 还原合并状态
     *
     * @param id
     */
    @GetMapping("/reduction/{id}")
    public void reduction(@PathVariable("id") String id) {
        projectService.reduction(id);
    }

    /**
     * 删除项目
     *
     * @param id
     */
    @GetMapping("/deleteProject/{id}")
    public void deleteProject(@PathVariable("id") String id) {
        projectService.deleteProject(id);
    }

    /**
     * 提交或保存 项目
     *
     * @param projectVo
     */
    @RequestMapping(value = "/api/disproject/disProjectadd", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectadd(ProjectVo projectVo) {
        //@RequestBody BaseProject baseProject,
        //             @RequestBody DesignInfo designInfo,
        //             @RequestBody ProjectExploration projectExploration,
        //             @RequestBody PackageCame packageCame
        projectService.disProjectSubmit(projectVo, getLoginUser());
        return RestUtil.success();
    }

    /**
     * 设计项目编辑回显
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/disProjectByid", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectByid(String id) {
        ProjectVo projectVo = new ProjectVo();

        BaseProject baseProject = projectService.BaseProjectByid(id);
        projectVo.setBaseProject(baseProject);


        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        projectVo.setDesignInfo(designInfo);
        projectVo.setDesginStatus(baseProject.getDesginStatus());
        System.out.println(designInfo.getId());

        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        if(projectExploration==null){
            projectVo.setProjectExploration(new ProjectExploration());
        }else{
            projectVo.setProjectExploration(projectExploration);
        }
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        if(packageCame == null){
            projectVo.setPackageCame(new PackageCame());
        }else{
            projectVo.setPackageCame(packageCame);
        }

        List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
        projectVo.setAuditInfos(auditInfos);

        return RestUtil.success(projectVo);
    }

    /**
     * 计算安徽设计费
     *
     * @param anhuiMoneyinfo
     * @return
     */
    @PostMapping("/anhuiMoney")
    public Double anhuiMoney(@RequestBody AnhuiMoneyinfo anhuiMoneyinfo) {
        return projectService.anhuiMoney(anhuiMoneyinfo);
    }

    /**
     * 计算吴江设计费
     *
     * @param wujiangMoneyInfo
     * @return
     */
    @PostMapping("/wujiangMoneyInfo")
    public Double wujiangMoney(@RequestBody WujiangMoneyInfo wujiangMoneyInfo) {
        return projectService.wujiangMoney(wujiangMoneyInfo);
    }

    /**
     * 添加安徽信息
     *
     * @param anhuiMoneyinfo
     */
    @PostMapping("anhuiMoneyInfoAdd")
    public void anhuiMoneyInfoAdd(AnhuiMoneyinfo anhuiMoneyinfo) {
        projectService.anhuiMoneyInfoAdd(anhuiMoneyinfo, getLoginUser());
    }

    /**
     * 添加吴江信息
     *
     * @param wujiangMoneyInfo
     */
    @PostMapping("wujiangMoneyInfoAdd")
    public void wujiangMoneyInfoAdd(WujiangMoneyInfo wujiangMoneyInfo) {
        projectService.wujiangMoneyInfoAdd(wujiangMoneyInfo, getLoginUser());
    }

    /**
     * 出图中状态编辑信息提交
     *
     * @param projectVo
     */
    @RequestMapping(value = "/api/disproject/UpdateProjectSubmit1", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateProjectSubmit1(ProjectVo projectVo) {
        projectService.projectEdit(projectVo, getLoginUser());
        return RestUtil.success();
    }

    /**
     * 展示吴江到账信息
     *
     * @param id
     * @return
     */
    @GetMapping("WujiangMoneyInfoByid/{id}")
    public WujiangMoneyInfo WujiangMoneyInfoByid(@PathVariable("id") String id) {
        return projectService.wujiangMoneyInfopayterm(id);
    }

    /**
     * 展示安徽到账信息
     *
     * @param id
     * @return
     */
    @GetMapping("anhuiMoneyinfoByid/{id}")
    public AnhuiMoneyinfo anhuiMoneyinfoByid(@PathVariable("id") String id) {
        return projectService.anhuiMoneyInfopayterm(id);
    }

    /**
     * 设计变更项目编辑回显
     *
     * @return
     */
    @GetMapping("/disProjectChangeByid/{id}")
    public ProjectVo disProjectChangeByid(@PathVariable("id") String id) {
        //项目基本信息
        BaseProject baseProject = projectService.BaseProjectByid(id);
        //设计信息
        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        //方案会审
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        //项目勘探
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        //项目审核
        List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
        //设计变更累计
        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(designInfo.getId());
        //设计变更信息
        DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
        ProjectVo projectVo = new ProjectVo();
        //设计费用展示
        if (baseProject.getDistrict() != "4") {
            //设计费（安徽）
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
            projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
        } else {
            //设计费（吴江）
            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
            projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
        }
        projectVo.setDesignChangeInfo(designChangeInfo);
        projectVo.setDesignChangeInfos(designChangeInfos);
        projectVo.setDesginStatus(baseProject.getDesginStatus());
        projectVo.setBaseProject(baseProject);
        projectVo.setDesignInfo(designInfo);
        projectVo.setPackageCame(packageCame);
        projectVo.setProjectExploration(projectExploration);
        projectVo.setAuditInfos(auditInfos);
        return projectVo;
    }

    /**
     * 设计项目变更编辑
     *
     * @param projectVo
     */
    @PostMapping("/disProjectChangeEdit")
    public void disProjectChangeEdit(@RequestBody ProjectVo projectVo) {
        projectService.disProjectChangeEdit(projectVo, getLoginUser());
    }

    /**
     * 设计项目查看
     *
     * @return
     */
    @PostMapping("/DesProjectInfoSelect")
    public List<ProjectVo> DesProjectInfoSelect(@RequestBody BaseProject param) {
        ArrayList<ProjectVo> projectVos = new ArrayList<>();
        if (!"".equals(param.getVirtualCode())) {
            //根据虚拟id查询
            List<BaseProject> baseProjects = projectService.DesProjectInfoSelect(param.getVirtualCode());
            for (BaseProject baseProject : baseProjects) {
                ProjectVo projectVo = new ProjectVo();
                projectVo.setBaseProject(baseProject);
                //设计信息
                DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
                projectVo.setDesignInfo(designInfo);
                //方案会审
                ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
                projectVo.setProjectExploration(projectExploration);
                //项目勘探
                PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
                projectVo.setPackageCame(packageCame);
                //项目审核
                List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
                projectVo.setAuditInfos(auditInfos);
                //设计变更审核
                List<AuditInfo> auditChangeInfos = projectService.auditInfoList(designInfo.getId());
                projectVo.setAuditInfos2(auditChangeInfos);
                //设计费用展示
                if (baseProject.getDistrict() != "4") {
                    //设计费（安徽）
                    AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
                    projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
                } else {
                    //设计费（吴江）
                    WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
                    projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
                }
                //设计变更累计
                List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(designInfo.getId());
                projectVo.setDesignChangeInfos(designChangeInfos);

                //设计变更信息
                DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
                projectVo.setDesignChangeInfo(designChangeInfo);
                projectVos.add(projectVo);
            }
            return projectVos;
        }

        //如果虚拟编号为空 说明不是合并项目 正常展示
        ProjectVo projectVo = new ProjectVo();
        //项目基本信息
        BaseProject baseProject = projectService.BaseProjectByid(param.getId());
        projectVo.setBaseProject(baseProject);
        //设计信息
        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        projectVo.setDesignInfo(designInfo);
        //方案会审
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        projectVo.setProjectExploration(projectExploration);
        //项目勘探
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        projectVo.setPackageCame(packageCame);
        //项目审核
        List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
        projectVo.setAuditInfos(auditInfos);
        //设计变更审核
        List<AuditInfo> auditChangeInfos = projectService.auditInfoList(designInfo.getId());
        projectVo.setAuditInfos2(auditChangeInfos);
        //设计变更累计
        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(designInfo.getId());
        projectVo.setDesignChangeInfos(designChangeInfos);
        //设计变更信息
        DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
        projectVo.setDesignChangeInfo(designChangeInfo);

        //设计费用展示
        if (baseProject.getDistrict() != "4") {
            //设计费（安徽）
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
            projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
        } else {
            //设计费（吴江）
            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
            projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
        }
        projectVo.setDesginStatus(baseProject.getDesginStatus());
        projectVos.add(projectVo);
        return projectVos;
    }

    @PostMapping("/DesginAudandChangeAud")
    public void DesginAudandChangeAud(@RequestBody AuditInfo auditInfo) {
        projectService.DesginAudandChangeAud(auditInfo, getLoginUser());
    }

    //    @GetMapping("/desginStatusSensus")
    @RequestMapping(value = "/desginStatusSensus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> desginStatusSensus(@RequestParam(name = "id") String id) {
//    public String desginStatusSensus(@RequestParam(name = "id") String id){
        String s = projectService.desginStatusSensus(id);
        JSONArray objects = JSON.parseArray(s);
        return RestUtil.success(objects);
//        return s;
    }

    //        @GetMapping("/budgetStatusSensus/{id}")
    @RequestMapping(value = "/budgetStatusSensus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> budgetStatusSensus(@RequestParam("id") String id) {
        String s = projectService.budgetStatusSensus(id);
        JSONArray objects = JSON.parseArray(s);
        return RestUtil.success(objects);
    }

//    @GetMapping("/trackStatusSensus/{id}")
    @RequestMapping(value = "/trackStatusSensus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> trackStatusSensus(@RequestParam("id") String id) {
        String s = projectService.trackStatusSensus(id);
        JSONArray objects = JSON.parseArray(s);
        return RestUtil.success(objects);
    }

//    @GetMapping("/visaStatusSensus/{id}")
    @RequestMapping(value = "/visaStatusSensus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> visaStatusSensus(@RequestParam("id") String id) {
        String s = projectService.visaStatusSensus(id);
        JSONArray objects = JSON.parseArray(s);
        return RestUtil.success(objects);
    }

//    @GetMapping("/progressPaymentStatusSensus/{id}")
    @RequestMapping(value = "/progressPaymentStatusSensus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> progressPaymentStatusSensus(@RequestParam("id") String id) {
        String s = projectService.progressPaymentStatusSensus(id);
        JSONArray objects = JSON.parseArray(s);
        return RestUtil.success(objects);
    }

//    @GetMapping("/settleAccountsStatusSensus/{id}")
    @RequestMapping(value = "/settleAccountsStatusSensus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> settleAccountsStatusSensus(@RequestParam("id") String id) {
        String s = projectService.settleAccountsStatusSensus(id);
        JSONArray objects = JSON.parseArray(s);
        return RestUtil.success(objects);
    }

//    @GetMapping("/buildDay/{id}")
    @RequestMapping(value = "/projectOverviewVo", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> buildDay(@RequestParam("id") String id) throws ParseException {
        Long aLong = projectService.buildDay(id);
        String s = projectService.projectCount(id);
        int i = projectService.missionCount(id);

        ProjectOverviewVo projectOverviewVo = new ProjectOverviewVo(aLong,"3",i);


        return RestUtil.success(projectOverviewVo);
    }

//    @GetMapping("/projectCount/{id}")
    @RequestMapping(value = "/projectCount", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> projectCount(@RequestParam("id") String id) {
        String s = projectService.projectCount(id);
        return RestUtil.success(s);
    }

//    @GetMapping("/missionCount/{id}")
    @RequestMapping(value = "/missionCount", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> missionCount(@RequestParam("id") String id) {
        int i = projectService.missionCount(id);
        return RestUtil.success(i+"");
    }

    @GetMapping("/desMoneySum/{id}")
    public BigDecimal desMoneySum(String id) {
        return projectService.desMoneySum(id);
    }

    //    @GetMapping("/BuildSum/{id}")
    @RequestMapping(value = "/api/disproject/BuildSum", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> BuildSum(@RequestParam(name = "id") String id) {
        //设计费支出
        BigDecimal desMoneySum = projectService.desMoneySum(id);
        //招标控制价
        BigDecimal biddingPriceControlSum = projectService.biddingPriceControlSum(id);
        //成本总金额
        BigDecimal costTotalAmountSum = projectService.costTotalAmountSum(id);
        //造价金额
        BigDecimal amountCostAmountSum = projectService.amountCostAmountSum(id);
        //设计费委外支出
        BigDecimal outsourceMoneySum = projectService.outsourceMoneySum(id);
        SumVo sumVo = new SumVo();
        sumVo.setDesMoneySum(desMoneySum);
        sumVo.setBiddingPriceControlSum(biddingPriceControlSum);
        sumVo.setCostTotalAmountSum(costTotalAmountSum);
        sumVo.setAmountCostAmountSum(amountCostAmountSum);
        sumVo.setOutsourceMoneySum(outsourceMoneySum);
        return RestUtil.success(sumVo);
    }

    /**
     * 员工首页-设计部门 消息提醒(造价也能用)
     * @return
     */
    @RequestMapping(value = "/api/disproject/messageList", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> messageList() {
        //todo
        List<MessageNotification> messageNotifications = projectService.messageList(getLoginUser());
        return RestUtil.success(messageNotifications);
    }

    /**
     * 员工首页-设计部门  个人任务统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/disproject/censusList", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> censusList(CostVo2 costVo2) {
        //todo getLoginUser().getId()
        List<OneCensus> oneCensuses = projectService.OneCensusList(costVo2);
        String censusList = "[{\"companyName\":\"市政管道\"," +
                "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList +=
                    "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus.getMunicipalPipeline() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"管网改造\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                    "\"truckAmmount\": \"" + oneCensus.getNetworkReconstruction() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"新建小区\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList +=
                    "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus.getNewCommunity() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"二次供水改造项目\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList +=
                    "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus.getSecondaryWater() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"工商户\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                    "\"truckAmmount\": \"" + oneCensus.getCommercialHouseholds() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"居民装接水\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                    "\"truckAmmount\": \"" + oneCensus.getWaterResidents() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"行政事业\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                    "\"truckAmmount\": \"" + oneCensus.getAdministration() + "\"},";
        }
        censusList = censusList.substring(0, censusList.length() - 1);
        censusList += "]}]";
        JSONArray json = JSON.parseArray(censusList);
        return RestUtil.showJsonSuccess(json);
    }

    /**
     * 员工首页-设计部门  个人绩效分析
     * @param individualVo
     * @return
     */
    @RequestMapping(value = "/api/disproject/individualList", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> individualList(IndividualVo individualVo) {
//      TODO  getLoginUser().getId()
        individualVo.setId("user282");
        PageInfo<BaseProject> projects = projectService.individualList(individualVo);
        for (BaseProject project : projects.getList()) {
            DesignInfo designInfo = projectService.designInfoByid(project.getId());
            //设计费用展示
            if (project.getDistrict() != "4") {
                //设计费（安徽）
                AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
                if (anhuiMoneyinfo != null) {
                    project.setDesMoney(anhuiMoneyinfo.getOfficialReceipts());
                    //应计提金额
                    BigDecimal bigDecimal = projectService.accruedAmount(anhuiMoneyinfo.getOfficialReceipts());
                    project.setAccrualMoney(bigDecimal.doubleValue());
                    //建议金额
                    BigDecimal bigDecimal1 = projectService.proposedAmount(bigDecimal);
                    project.setAdviseMoney(bigDecimal1.doubleValue());
                    // 余额
                    BigDecimal surplus = projectService.surplus(bigDecimal, bigDecimal1);
                    project.setSurplus(surplus.doubleValue());
                }
            } else {
                //设计费（吴江）
                WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
                if (wujiangMoneyInfo != null) {
                    project.setDesMoney(wujiangMoneyInfo.getOfficialReceipts());
                    //应计提金额
                    BigDecimal bigDecimal = projectService.accruedAmount(wujiangMoneyInfo.getOfficialReceipts());
                    project.setAccrualMoney(bigDecimal.doubleValue());
                    //建议金额
                    BigDecimal bigDecimal1 = projectService.proposedAmount(bigDecimal);
                    project.setAdviseMoney(bigDecimal1.doubleValue());
                    // 余额
                    BigDecimal surplus = projectService.surplus(bigDecimal, bigDecimal1);
                    project.setSurplus(surplus.doubleValue());
                }
            }
            //造价费用
            Budgeting budgeting = projectService.budgetingByid(project.getId());
            project.setAmountCost(budgeting.getAmountCost());
            //应技提金额
        }
        return RestUtil.page(projects);
    }


    /**
     * 工程项目查看
     *
     * @return
     */
    @RequestMapping(value = "/api/costproject/selectByBaseprojectId", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> projectSelect(String id) {
        //基本数据信息
        ProjectVo3 projectVo3 = new ProjectVo3();
        BaseProject baseProject = projectService.BaseProjectByid(id);
        projectVo3.setBaseProject(baseProject);
        //设计信息
        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        if(designInfo == null){
            projectVo3.setDesignChangeInfo(new DesignChangeInfo());
        }else{
            projectVo3.setDesignInfo(designInfo);
        }
        //根据地区判断相应的设计费 应付金额 实付金额
        //如果为安徽
        if(!baseProject.getDistrict().equals("4")){
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyinfoByid(baseProject.getId());
            if(anhuiMoneyinfo!=null){
                designInfo.setRevenue(anhuiMoneyinfo.getRevenue());
                designInfo.setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts());
                designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                designInfo.setPayTerm(anhuiMoneyinfo.getPayTerm());
            }else{
                designInfo.setRevenue(new BigDecimal(0));
                designInfo.setOfficialReceipts(new BigDecimal(0));
                designInfo.setDisMoney(new BigDecimal(0));
                designInfo.setPayTerm("0");
            }
        }else{
            //如果为吴江
            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfoByid(baseProject.getId());
            if(wujiangMoneyInfo!=null){
                designInfo.setRevenue(wujiangMoneyInfo.getRevenue());
                designInfo.setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts());
                designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                designInfo.setPayTerm(wujiangMoneyInfo.getPayTerm());
            }
        }
        //项目探勘
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        if(projectExploration==null){
            projectVo3.setProjectExploration(new ProjectExploration());
        }else{
            projectVo3.setProjectExploration(projectExploration);
        }
        //方案会审
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        if(packageCame==null){
            projectVo3.setPackageCame(new PackageCame());
        }else{
            projectVo3.setPackageCame(packageCame);
        }
        //设计变更
        DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
        if(designChangeInfo == null){
            projectVo3.setDesignChangeInfo(new DesignChangeInfo());
        }else{
            projectVo3.setDesignChangeInfo(designChangeInfo);
        }

        //预算编制
        Budgeting budgeting = projectService.budgetingByid(baseProject.getId());
        if(budgeting != null){
            projectVo3.setBudgeting(budgeting);
            //成本编制
            CostPreparation costPreparation = projectService.costPreparationById(budgeting.getId());
            if(costPreparation==null){
                projectVo3.setCostPreparation(new CostPreparation());
            }else{
                projectVo3.setCostPreparation(costPreparation);
            }
            //控价编制
            VeryEstablishment veryEstablishment = projectService.veryEstablishmentById2(budgeting.getId());
            if(veryEstablishment == null){
                projectVo3.setVeryEstablishment(new VeryEstablishment());
            }else{
                projectVo3.setVeryEstablishment(veryEstablishment);
            }
        }else{
            Budgeting budgeting2 = new Budgeting();
            projectVo3.setBudgeting(budgeting2);
            projectVo3.setCostPreparation(new CostPreparation());
            projectVo3.setVeryEstablishment(new VeryEstablishment());
        }
        //跟踪审计信息
        TrackAuditInfo trackAuditInfo = projectService.trackAuditInfoByid(baseProject.getId());
        if(trackAuditInfo==null){
            projectVo3.setTrackAuditInfo(new TrackAuditInfo());
        }else{
            projectVo3.setTrackAuditInfo(trackAuditInfo);
        }

        //计算累计值
        ProjectVo3 projectVo31 = projectService.progressPaymentInformationSum(baseProject.getId());
        projectVo3.setNewcurrentPaymentInformation(projectVo31.getNewcurrentPaymentInformation());
        projectVo3.setSumcurrentPaymentInformation(projectVo31.getSumcurrentPaymentInformation());
        projectVo3.setCumulativePaymentTimes(projectVo31.getCumulativePaymentTimes());
        projectVo3.setCurrentPaymentRatio(projectVo31.getCurrentPaymentRatio());

        //计算签证累计值
        ProjectVo3 projectVo32 = projectService.visaApplyChangeInformationSum(baseProject.getId());
        projectVo3.setAmountVisaChangeSum(projectVo32.getAmountVisaChangeSum());
        projectVo3.setChangeCount(projectVo32.getChangeCount());
        projectVo3.setContractAmount(projectVo32.getContractAmount());


        //上家结算送审
        SettlementAuditInformation settlementAuditInformation = projectService.SettlementAuditInformationByid(baseProject.getId());
        if(settlementAuditInformation==null){
            projectVo3.setSettlementAuditInformation(new SettlementAuditInformation());
        }else{
            projectVo3.setSettlementAuditInformation(settlementAuditInformation);
        }
        //下家结算送审
        LastSettlementReview lastSettlementReview = projectService.lastSettlementReviewbyid(baseProject.getId());
        if(lastSettlementReview==null){
            projectVo3.setLastSettlementReview(new LastSettlementReview());
        }else{
            projectVo3.setLastSettlementReview(lastSettlementReview);
        }
        return RestUtil.success(projectVo3);
    }

    /**
     * 员工首页-造价部门 待办任务
     * @param district
     * @return
     */
    @RequestMapping(value = "/api/costproject/costSelectByid", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> costSelectByid(@RequestParam(name = "district") String district) {
        //todo getLoginUser().getId();
        String budgetingCount = projectService.budgetingCount("user282", district);
        String settleAccountsCount = projectService.settleAccountsCount("user282", district);
        String progressPaymentInformationCount = projectService.progressPaymentInformationCount("user282", district);
        String visaApplyChangeInformationCount = projectService.visaApplyChangeInformationCount("user282", district);
        String trackAuditInfoCount = projectService.trackAuditInfoCount("user282", district);
        CostVo costVo = new CostVo();
        costVo.setBudgetingCount(budgetingCount);
        costVo.setSettleAccountsCount(settleAccountsCount);
        costVo.setProgressPaymentInformation(progressPaymentInformationCount);
        costVo.setVisaApplyChangeInformationCount(visaApplyChangeInformationCount);
        costVo.setTrackAuditInfoCount(trackAuditInfoCount);
        return RestUtil.success(costVo);
    }

    /**
     * 员工首页-造价部门 造价任务统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/costproject/costCensus", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> costCensus(CostVo2 costVo2) {
        OneCensus2 oneCensus2 = projectService.costCensus(costVo2);
        String josn =
                "[" +
                        "{\"value1\":" + oneCensus2.getBudget() + ",\"name1\":\"预算编制\"}," +
                        "{\"value1\":" + oneCensus2.getSettleaccounts() + ",name1:\"结算编制\"}," +
                        "{\"value1\":" + oneCensus2.getProgresspayment() + ",name1:\"进度款支付\"}," +
                        "{\"value1\":" + oneCensus2.getVisa() + ",name1:\"签证/变更\"}," +
                        "{\"value1\":" + oneCensus2.getTrack() + ",name1:\"跟踪审计\"}" +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }


    @RequestMapping(value = "/api/costproject/costCensusList", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> costCensusList(CostVo2 costVo2) {
        List<OneCensus2> oneCensus2s = projectService.costCensusList(costVo2);
        String json2 =
                "[{" +
                        "\"companyName\": \"造价任务\"," +
                        "\"imageAmmount\": [";
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            json2 +=
                    "{\"time\": \"" + oneCensus2.getYeartime() + "-" + oneCensus2.getMonthTime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus2.getTotal() + "\"" +
                            "},";
        }
        json2 = json2.substring(0, json2.length() - 1);
        json2 += "]}]";
        JSONArray objects2 = JSON.parseArray(json2);
        return RestUtil.success(objects2);
    }

    @RequestMapping(value = "/api/costproject/costCensusAndSearch", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> costCensusAndSearch(CostVo2 costVo2) {
        OneCensus2 oneCensus2 = projectService.costCensus(costVo2);
        String josn =
                "[" +
                        "{\"value1\":" + oneCensus2.getBudget() + ",\"name1\":\"预算编制\"}," +
                        "{\"value1\":" + oneCensus2.getSettleaccounts() + ",name1:\"结算编制\"}," +
                        "{\"value1\":" + oneCensus2.getProgresspayment() + ",name1:\"进度款支付\"}," +
                        "{\"value1\":" + oneCensus2.getVisa() + ",name1:\"签证/变更\"}," +
                        "{\"value1\":" + oneCensus2.getTrack() + ",name1:\"跟踪审计\"}" +
                        "]";
        JSONArray objects = JSON.parseArray(josn);

        List<OneCensus2> oneCensus2s = projectService.costCensusList(costVo2);
        String json2 =
                "[{" +
                        "\"companyName\": \"造价任务\"," +
                        "\"imageAmmount\": [";
        for (OneCensus2 oneCensus21 : oneCensus2s) {
            json2 +=
                    "{\"time\": \"" + oneCensus21.getYeartime() + "-" + oneCensus21.getMonthTime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus21.getTotal() + "\"" +
                            "},";
        }
        json2 = json2.substring(0, json2.length() - 1);
        json2 += "]}]";
        JSONArray objects2 = JSON.parseArray(json2);

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("objects",objects);
        map.put("objects2",objects2);
        return RestUtil.success(map);
    }

    /**
     * 造价页面月任务总数
     *
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/costproject/mounthTaskCount", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> mounthTaskCount(CostVo2 costVo2) {
        String sysYear = projectService.getSysYear();
        String sysMouth = projectService.getSysMouth() + "";
        costVo2.setYear(sysYear);
        costVo2.setMonth(sysMouth);
        List<OneCensus2> oneCensus2s = projectService.costCensusList(costVo2);
        OneCensus2 census2 = oneCensus2s.get(0);
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("total",census2.getTotal());
        return RestUtil.success(map);
    }

    /**
     * 造价页面年度任务数
     *
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/costproject/yearTaskCount", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> yearTaskCount(CostVo2 costVo2) {
        Integer integer = projectService.yearTaskCount(costVo2);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("total",integer);
        return RestUtil.success(map);
    }

    /**
     * 设计页面月任务数
     *
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/costproject/yearDesCount", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> yearDesCount(CostVo2 costVo2) {
        String sysYear = projectService.getSysYear();
        costVo2.setYear(sysYear);
        Integer integer = projectService.yearDesCount(costVo2);
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("count",integer);
        return RestUtil.success(stringIntegerMap);
    }

    /**
     * 设计页面年任务数
     *
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/costproject/mounthDesCount", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> mounthDesCount(CostVo2 costVo2) {
        String sysYear = projectService.getSysYear();
        int sysMouth = projectService.getSysMouth();
        costVo2.setYear(sysYear);
        costVo2.setMonth(sysMouth + "");
        Integer integer = projectService.mouthDesCount(costVo2);
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("count",integer);
        return RestUtil.success(stringIntegerHashMap);
    }
}
