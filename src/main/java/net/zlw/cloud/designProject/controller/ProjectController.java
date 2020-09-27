package net.zlw.cloud.designProject.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectService;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/disproject")
public class ProjectController extends BaseController {

    @Resource
    private ProjectService projectService;

    /**
     * 设计列表展示 条件查询
     *
     * @param designPageVo
     * @return
     */
    @PostMapping("/disProjectSelect")
    public PageInfo<DesignInfo> disProjectSelect(@RequestBody DesignPageVo designPageVo) {
        PageInfo<DesignInfo> designInfoPageInfo = projectService.designProjectSelect(designPageVo,getLoginUser());
        return designInfoPageInfo;
    }

    /**
     * 设计项目合并
     *
     * @param mergeName
     * @param mergeNum
     * @param idlist
     */
    @GetMapping("/margeProject/{mergeName}/{mergeNum}/{idlist}")
    public void margeProject(@PathVariable(name = "mergeName") String mergeName, @PathVariable(name = "mergeNum") String mergeNum, @PathVariable(name = "idlist") String idlist) {
        String[] split = idlist.split(",");
        for (String id : split) {
            projectService.mergeProject(mergeName, mergeNum, id);
        }
    }

    /**
     * 批量审核接口
     * @param projectVo2
     */
    @PostMapping("/batchAudit")
    public void batchAudit(@RequestBody ProjectVo2 projectVo2){
        String[] split = projectVo2.getIdlist().split(",");
        for (String id : split) {
            projectService.batchAudit(id,projectVo2.getAuditInfo(),getLoginUser());

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
     * @param projectVo
     */
    @PostMapping("/disProjectadd")
    public void disProjectadd
            (@RequestBody ProjectVo projectVo) {
        //@RequestBody BaseProject baseProject,
        //             @RequestBody DesignInfo designInfo,
        //             @RequestBody ProjectExploration projectExploration,
        //             @RequestBody PackageCame packageCame

        projectService.disProjectSubmit(projectVo,getLoginUser());
    }

    /**
     * 设计项目编辑回显
     * @param id
     * @return
     */
    @GetMapping("/disProjectByid/{id}")
    public ProjectVo disProjectByid(@PathVariable("id") String id){
        BaseProject baseProject = projectService.BaseProjectByid(id);
        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        System.out.println(designInfo.getId());
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
        ProjectVo projectVo = new ProjectVo();
        projectVo.setDesginStatus(baseProject.getDesginStatus());
        projectVo.setBaseProject(baseProject);
        projectVo.setDesignInfo(designInfo);
        projectVo.setPackageCame(packageCame);
        projectVo.setProjectExploration(projectExploration);
        projectVo.setAuditInfos(auditInfos);
        return projectVo;
    }

    /**
     * 计算安徽设计费
     * @param anhuiMoneyinfo
     * @return
     */
    @PostMapping("/anhuiMoney")
    public Double anhuiMoney(@RequestBody AnhuiMoneyinfo anhuiMoneyinfo) {
        return projectService.anhuiMoney(anhuiMoneyinfo);
    }

    /**
     * 计算吴江设计费
     * @param wujiangMoneyInfo
     * @return
     */
    @PostMapping("/wujiangMoneyInfo")
    public Double wujiangMoney(@RequestBody WujiangMoneyInfo wujiangMoneyInfo){
        return projectService.wujiangMoney(wujiangMoneyInfo);
    }

    /**
     * 添加安徽信息
     * @param anhuiMoneyinfo
     */
    @PostMapping("anhuiMoneyInfoAdd")
    public void anhuiMoneyInfoAdd(AnhuiMoneyinfo anhuiMoneyinfo){
        projectService.anhuiMoneyInfoAdd(anhuiMoneyinfo,getLoginUser());
    }

    /**
     * 添加吴江信息
     * @param wujiangMoneyInfo
     */
    @PostMapping("wujiangMoneyInfoAdd")
    public void wujiangMoneyInfoAdd(WujiangMoneyInfo wujiangMoneyInfo){
        projectService.wujiangMoneyInfoAdd(wujiangMoneyInfo,getLoginUser());
    }

    /**
     * 出图中状态编辑信息提交
     * @param projectVo
     */
    @PostMapping("UpdateProjectSubmit1")
    public void updateProjectSubmit1(@RequestBody ProjectVo projectVo){
        projectService.projectEdit(projectVo,getLoginUser());
    }

    /**
     * 展示吴江到账信息
     * @param id
     * @return
     */
    @GetMapping("WujiangMoneyInfoByid/{id}")
    public WujiangMoneyInfo  WujiangMoneyInfoByid(@PathVariable("id") String id){
        return projectService.wujiangMoneyInfopayterm(id);
    }

    /**
     * 展示安徽到账信息
     * @param id
     * @return
     */
    @GetMapping("anhuiMoneyinfoByid/{id}")
    public AnhuiMoneyinfo anhuiMoneyinfoByid(@PathVariable("id") String id){
        return projectService.anhuiMoneyInfopayterm(id);
    }

    /**
     * 设计变更项目编辑回显
     * @return
     */
    @GetMapping("/disProjectChangeByid/{id}")
    public ProjectVo disProjectChangeByid(@PathVariable("id") String id){
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
        if(baseProject.getDistrict()!="4"){
            //设计费（安徽）
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
            projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
        }else{
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
     * @param projectVo
     */
    @PostMapping("/disProjectChangeEdit")
    public void disProjectChangeEdit(@RequestBody ProjectVo projectVo){
        projectService.disProjectChangeEdit(projectVo,getLoginUser());
    }

    /**
     * 设计项目查看
     * @return
     */
    @PostMapping("/DesProjectInfoSelect")
    public List<ProjectVo> DesProjectInfoSelect(@RequestBody BaseProject param){
        ArrayList<ProjectVo> projectVos = new ArrayList<>();
        if(!"".equals(param.getVirtualCode())){
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
                if(baseProject.getDistrict()!="4"){
                    //设计费（安徽）
                    AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
                    projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
                }else{
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
        if(baseProject.getDistrict()!="4"){
            //设计费（安徽）
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
            projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
        }else{
            //设计费（吴江）
            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
            projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
        }
        projectVo.setDesginStatus(baseProject.getDesginStatus());
        projectVos.add(projectVo);
        return projectVos;
    }

    @PostMapping("/DesginAudandChangeAud")
    public void DesginAudandChangeAud(@RequestBody AuditInfo auditInfo){
        projectService.DesginAudandChangeAud(auditInfo,getLoginUser());
    }

    @GetMapping("/desginStatusSensus/{id}")
    public String desginStatusSensus(@PathVariable("id") String id){
        return projectService.desginStatusSensus(id);
    }

    @GetMapping("/budgetStatusSensus/{id}")
    public String budgetStatusSensus(@PathVariable("id") String id){
        return projectService.budgetStatusSensus(id);
    }

    @GetMapping("/trackStatusSensus/{id}")
    public String trackStatusSensus(@PathVariable("id") String id){
        return projectService.trackStatusSensus(id);
    }

    @GetMapping("/visaStatusSensus/{id}")
    public String visaStatusSensus(@PathVariable("id") String id){
        return projectService.visaStatusSensus(id);
    }

    @GetMapping("/progressPaymentStatusSensus/{id}")
    public String progressPaymentStatusSensus(@PathVariable("id") String id){
        return projectService.progressPaymentStatusSensus(id);
    }

    @GetMapping("/settleAccountsStatusSensus/{id}")
    public String settleAccountsStatusSensus(@PathVariable("id") String id){
        return projectService.settleAccountsStatusSensus(id);
    }
    @GetMapping("/buildDay/{id}")
    public long buildDay(@PathVariable("id") String id) throws ParseException {
        return projectService.buildDay(id);
    }
    @GetMapping("/projectCount/{id}")
    public String projectCount(@PathVariable("id") String id){
        return projectService.projectCount(id);
    }
    @GetMapping("/missionCount/{id}")
    public int missionCount(@PathVariable("id") String id){
        return projectService.missionCount(id);
    }
    @GetMapping("/desMoneySum/{id}")
    public BigDecimal desMoneySum(String id){
        return projectService.desMoneySum(id);
    }

    @GetMapping("/BuildSum/{id}")
    public SumVo BuildSum(@PathVariable("id") String id){
        BigDecimal desMoneySum = projectService.desMoneySum(id);
        BigDecimal biddingPriceControlSum = projectService.biddingPriceControlSum(id);
        BigDecimal costTotalAmountSum = projectService.costTotalAmountSum(id);
        BigDecimal amountCostAmountSum = projectService.amountCostAmountSum(id);
        BigDecimal outsourceMoneySum = projectService.outsourceMoneySum(id);
        SumVo sumVo = new SumVo();
        sumVo.setDesMoneySum(desMoneySum);
        sumVo.setBiddingPriceControlSum(biddingPriceControlSum);
        sumVo.setCostTotalAmountSum(costTotalAmountSum);
        sumVo.setAmountCostAmountSum(amountCostAmountSum);
        sumVo.setOutsourceMoneySum(outsourceMoneySum);
        return sumVo;
    }
}
