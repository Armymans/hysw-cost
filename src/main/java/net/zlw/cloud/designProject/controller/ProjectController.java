package net.zlw.cloud.designProject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.mapper.AnhuiMoneyinfoMapper;
import net.zlw.cloud.designProject.mapper.BudgetingMapper;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.WujiangMoneyInfoMapper;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectService;
import net.zlw.cloud.followAuditing.mapper.DesignUnitManagementDao;
import net.zlw.cloud.followAuditing.model.DesignUnitManagement;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.mapper.SettlementInfoMapper;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.settleAccounts.model.SettlementInfo;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ProjectController extends BaseController {

    @Value("${app.attachPath}")
    private String LixAttachDir;
    @Value("${app.testPath}")
    private String WinAttachDir;
    @Resource
    private SettlementInfoMapper settlementInfoMapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private WujiangMoneyInfoMapper wujiangMoneyInfoMapper;
    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;
    @Resource
    private BudgetingMapper budgetingMapper;
    @Resource
    private AnhuiMoneyinfoMapper anhuiMoneyinfoMapper;
    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;
    @Resource
    private DesignUnitManagementDao designUnitManagementDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private DesignInfoMapper designInfoMapper;


    /**
     * 建设项目提交 _ 保存
     * @param buildingProject
     * @return
     */
    @RequestMapping(value = "/build/buildSubmit", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> buildSubmit(BuildingProject buildingProject) throws Exception {

        try {
            String uid = projectService.buildSubmit(buildingProject,getLoginUser(),request);
            //更改userid 为 建设项目id
            String id = getLoginUser().getId();
            List<FileInfo> build = fileInfoService.findByFreignAndType2(id, "build");
            if(build.size()>0){
                for (FileInfo fileInfo : build) {
                    fileInfo.setPlatCode(uid);
                    projectService.updateFileInfo(fileInfo);
                }
            }
        }catch (Exception e){
            return RestUtil.error(e.getMessage());
        }


        return RestUtil.success();
    }

    /***
     * 编辑建设项目
     * @param buildingProject
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update/buildSubmit", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> buildUpdate(BuildingProject buildingProject) throws Exception {

        try {
             projectService.updateSubmit(buildingProject);
        }catch (Exception e){
            return RestUtil.error(e.getMessage());
        }

        //更改userid 为 建设项目id
//        String id = getLoginUser().getId();
//        List<FileInfo> xmxgt = fileInfoService.findByFreignAndType2(id, "xmxgt");
//        if(xmxgt.size()>0){
//            for (FileInfo fileInfo : xmxgt) {
//                fileInfo.setPlatCode(uid);
//                projectService.updateFileInfo(fileInfo);
//            }
//        }
        return RestUtil.success();
    }


    /**
     * 建设项目查看
     * @param
     * @return
     */
    @RequestMapping(value = "/build/findOne", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> buildSubmit(@Param("id") String id){
        BuildingProject buildingProject = projectService.findOne(id);
        return RestUtil.success(buildingProject);
    }
    /**
     * 查看 查询轮播图
     * @param
     * @return
     */
    @RequestMapping(value = "/build/findImage", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> findImage(@Param("id") String id) {

        List<FileInfo> fileInfo = fileInfoService.findByPlatCode(id);
        if (fileInfo.size() > 0) {
            for (FileInfo info : fileInfo) {
                String url = "/hysw/cost/api/common/file/view/" +info.getId()+".png";
                info.setImgurl(url);
            }
        }
        return RestUtil.success(fileInfo);
    }


    /**
     * 设计列表展示
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
     * 设计列表 条件查询
     *
     * @param designPageVo
     * @return
     */
    @RequestMapping(value = "/api/disproject/disProjectSelectAll", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectSelectAll(DesignPageVo designPageVo) {
        //全部
        Page page = new Page();
        designPageVo.setDesginStatus("");
        PageInfo<DesignInfo> designInfoPageInfo = projectService.designProjectSelect(designPageVo, getLoginUser());
        page.setData(designInfoPageInfo.getList());
        page.setPageNum(designInfoPageInfo.getPageNum());
        page.setPageSize(designInfoPageInfo.getPageSize());
        page.setTotalCount(designInfoPageInfo.getTotal());
        //待审核
        Page page2 = new Page();
        designPageVo.setDesginStatus("1");
        PageInfo<DesignInfo> designInfoPageInfo2 = projectService.designProjectSelect(designPageVo, getLoginUser());
        page2.setData(designInfoPageInfo2.getList());
        page2.setPageNum(designInfoPageInfo2.getPageNum());
        page2.setPageSize(designInfoPageInfo2.getPageSize());
        page2.setTotalCount(designInfoPageInfo2.getTotal());
        //出图中
        Page page3 = new Page();
        designPageVo.setDesginStatus("2");
        PageInfo<DesignInfo> designInfoPageInfo3 = projectService.designProjectSelect(designPageVo, getLoginUser());
        page3.setData(designInfoPageInfo3.getList());
        page3.setPageNum(designInfoPageInfo3.getPageNum());
        page3.setPageSize(designInfoPageInfo3.getPageSize());
        page3.setTotalCount(designInfoPageInfo3.getTotal());
        //未通过
        Page page4 = new Page();
        designPageVo.setDesginStatus("3");
        PageInfo<DesignInfo> designInfoPageInfo4 = projectService.designProjectSelect(designPageVo, getLoginUser());
        page4.setData(designInfoPageInfo4.getList());
        page4.setPageNum(designInfoPageInfo4.getPageNum());
        page4.setPageSize(designInfoPageInfo4.getPageSize());
        page4.setTotalCount(designInfoPageInfo4.getTotal());
        //已完成
        Page page5 = new Page();
        designPageVo.setDesginStatus("4");
        PageInfo<DesignInfo> designInfoPageInfo5 = projectService.designProjectSelect(designPageVo, getLoginUser());
        page5.setData(designInfoPageInfo5.getList());
        page5.setPageNum(designInfoPageInfo5.getPageNum());
        page5.setPageSize(designInfoPageInfo5.getPageSize());
        page5.setTotalCount(designInfoPageInfo5.getTotal());

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("table1",page);
        hashMap.put("table2",page2);
        hashMap.put("table3",page3);
        hashMap.put("table4",page4);
        hashMap.put("table5",page5);
        return RestUtil.success(hashMap);
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
            if(!  b){
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
//    @PostMapping("/batchAudit")
    @RequestMapping(value = "/api/disproject/batchAudit", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchAudit(ProjectVo2 projectVo2) {
        String[] split = projectVo2.getIdlist().split(",");
        if(projectVo2.getIdlist()!=null&&!"".equals(projectVo2.getIdlist())){
            for (String id : split) {
                projectService.batchAudit(id, projectVo2.getAuditInfo(), getLoginUser(),request);
            }
            return RestUtil.success();
        }else{
            return RestUtil.error();
        }
    }

    /**
     * 查看审核按钮
     * @param projectVo2
     * @return
     */
    @RequestMapping(value = "/api/disproject/oneAudit", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> oneAudit(ProjectVo2 projectVo2) {
        if (projectVo2.getAuditInfo()!=null){
            projectService.batchAudit(projectVo2.getIdlist(), projectVo2.getAuditInfo(), getLoginUser(),request);
        }
        return RestUtil.success();
    }

    /**
     * 还原合并状态
     *
     * @param id
     */
    @RequestMapping(value = "/api/disproject/reduction", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> reduction(String id) {
        projectService.reduction(id);
        return RestUtil.success();
    }

    /**
     * 删除项目
     *
     * @param id
     */
//    @GetMapping("/deleteProject/{id}")
    @RequestMapping(value = "/api/disproject/deleteProject", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteProject(String id) {
        projectService.deleteProject(id,getLoginUser(),request);
        return RestUtil.success();
    }

    /**
     * 提交或保存 项目
     *
     * @param projectVo
     */
    @RequestMapping(value = "/api/disproject/disProjectadd", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectadd(ProjectVo projectVo) {
        if (projectVo.getDesignInfo().getOutsourceMoney() == null || projectVo.getDesignInfo().getOutsourceMoney().equals("")){
            projectVo.getDesignInfo().setOutsourceMoney("0");
        }
        //@RequestBody BaseProject baseProject,
        //             @RequestBody DesignInfo designInfo,
        //             @RequestBody ProjectExploration projectExploration,
        //             @RequestBody PackageCame packageCame
        try {
            projectService.disProjectSubmit(projectVo, getLoginUser(),request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }
    /**
     * 新建回显设计人
     *
     * @param
     */
    @RequestMapping(value = "/api/disproject/disCreator", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disCreator() {
        DesignInfo designPageVo = projectService.selectDisCreator(getLoginUser().getId());
        return RestUtil.success(designPageVo);
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

        BaseProject baseProject = projectService.BaseProjectByid2(id);
        projectVo.setBaseProject(baseProject);

        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(designInfo.getDesigner());
        if (memberManage != null){
            designInfo.setDesigner(memberManage.getMemberName());
        }
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
//
//
//        Example example1 = new Example(MemberManage.class);
//        example1.createCriteria().andEqualTo("id",projectVo.getDesignInfo().getDesigner());
//        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//        projectVo.getDesignInfo().setDesigner(memberManage.getMemberName());

        return RestUtil.success(projectVo);
    }

    /**
     * 计算安徽设计费
     *
     * @param anhuiMoneyinfo
     * @return
     */
    @RequestMapping(value = "/api/disproject/anhuiMoney", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> anhuiMoney(AnhuiMoneyinfo anhuiMoneyinfo) {
        Double money = projectService.anhuiMoney(anhuiMoneyinfo);
        return RestUtil.success(money);
    }

    /**
     * 计算吴江设计费
     *
     * @param wujiangMoneyInfo
     * @return
     */
    @RequestMapping(value = "/api/disproject/wujiangMoneyInfo", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> wujiangMoney(WujiangMoneyInfo wujiangMoneyInfo) {
        Double money = projectService.wujiangMoney(wujiangMoneyInfo);
        return RestUtil.success(money);
    }

    /**
     * 添加安徽信息
     *
     * @param anhuiMoneyinfo
     */
    @RequestMapping(value = "/api/disproject/anhuiMoneyInfoAdd", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> anhuiMoneyInfoAdd(AnhuiMoneyinfo anhuiMoneyinfo) {
        if (anhuiMoneyinfo.getPumpRoomCost().equals("")){
            anhuiMoneyinfo.setPumpRoomCost(null);
        }
        if ("".equals(anhuiMoneyinfo.getBim())){
            anhuiMoneyinfo.setBim(null);
        }
        if ("".equals(anhuiMoneyinfo.getPipelineCost())){

            anhuiMoneyinfo.setPipelineCost(null);
        }
        if ("".equals(anhuiMoneyinfo.getProfessionalAdjustmentFactor())){
            anhuiMoneyinfo.setProfessionalAdjustmentFactor(null);
        }
        if ("".equals(anhuiMoneyinfo.getComplexAdjustmentFactor())){
            anhuiMoneyinfo.setComplexAdjustmentFactor(null);
        }
        if ("".equals(anhuiMoneyinfo.getPreferentialPolicy())){
            anhuiMoneyinfo.setPreferentialPolicy(null);
        }

        try {
            DesignInfo designInfo1 = designInfoMapper.selectByPrimaryKey(anhuiMoneyinfo.getBaseProjectId());
            net.zlw.cloud.progressPayment.model.BaseProject baseProject = baseProjectDao.selectByPrimaryKey(designInfo1.getBaseProjectId());

            if (baseProject!=null){
                String district = baseProject.getDistrict();
                String projectCategory = baseProject.getDesignCategory();
                Example example = new Example(DesignInfo.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",baseProject.getId());
                c.andEqualTo("status","0");
                DesignInfo designInfo = designInfoMapper.selectOneByExample(example);
                String designer = designInfo.getDesigner();
                if (district == null || "".equals(district)){
                    throw new RuntimeException("请先填写归属");
                }
                if (projectCategory == null || "".equals(projectCategory)){
                    throw new RuntimeException("请先填写归属");
                }
                if (designer == null || "".equals(designer)){
                    throw new RuntimeException("请先填写归属");
                }
            }
            projectService.anhuiMoneyInfoAdd(anhuiMoneyinfo, getLoginUser());
        } catch (Exception e) {
            e.printStackTrace();
          return  RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    /**
     * 安徽信息回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/anhuiMoneyInfoSelect", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> anhuiMoneyInfoSelect(String id) {
        AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfoSelect(id);
        if (anhuiMoneyinfo.getRevenue()!=null){
            if (anhuiMoneyinfo.getPumpRoomCost() == null && anhuiMoneyinfo.getBim()==null){
                anhuiMoneyinfo.setUnShow("1");
            }
        }
        return RestUtil.success(anhuiMoneyinfo);
    }

    /**
     * 吴江信息回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/wujiangMoneyInfoSelect", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> wujiangMoneyInfoSelect(String id) {
        WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfoSelect(id);
        return RestUtil.success(wujiangMoneyInfo);
    }

    /**
     * 安徽代收列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/anhuiCollectionMoney", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> anhuiCollectionMoney(String id) {
        List<CollectionMoney> collectionMonies = projectService.anhuiCollectionMoney(id);
        return RestUtil.success(collectionMonies);
    }

    /**
     * 吴江代收列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/wujiangCollectionMoney", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> wujiangCollectionMoney(String id) {
        List<CollectionMoney> collectionMonies = projectService.wujiangCollectionMoney(id);
        return RestUtil.success(collectionMonies);
    }


    /**
     * 回显委外金额弹窗
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/echoOutsourceMoney", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> echoOutsourceMoney(@RequestParam("id") String id) {
        DesignInfo designInfo = projectService.echoOutsourceMoney(id);
        return RestUtil.success(designInfo);
    }
    /**
     * 编辑委外金额弹窗
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/editOutsourceMoney", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editOutsourceMoney(@RequestParam(name = "id") String id, @RequestParam(name = "outsourceMoney") String outsourceMoney,@RequestParam(name = "totalMoney") String totalMoney) {
         projectService.editOutsourceMoney(id,outsourceMoney,totalMoney);
        return RestUtil.success("编辑成功");
    }


    /**
     * 添加吴江信息
     *
     * @param wujiangMoneyInfo
     */
    @RequestMapping(value = "/api/disproject/wujiangMoneyInfoAdd", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> wujiangMoneyInfoAdd(WujiangMoneyInfo wujiangMoneyInfo) {
        try {
            DesignInfo designInfo1 = designInfoMapper.selectByPrimaryKey(wujiangMoneyInfo.getBaseProjectId());
            net.zlw.cloud.progressPayment.model.BaseProject baseProject = baseProjectDao.selectByPrimaryKey(designInfo1.getBaseProjectId());
            if (baseProject!=null){
                String district = baseProject.getDistrict();
                String projectCategory = baseProject.getDesignCategory();
                Example example = new Example(DesignInfo.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",baseProject.getId());
                c.andEqualTo("status","0");
                DesignInfo designInfo = designInfoMapper.selectOneByExample(example);
                String designer = designInfo.getDesigner();
                if (district == null || "".equals(district)){
                    throw new RuntimeException("请先填写归属");
                }
                if (projectCategory == null || "".equals(projectCategory)){
                    throw new RuntimeException("请先填写归属");
                }
                if (designer == null || "".equals(designer)){
                    throw new RuntimeException("请先填写归属");
                }
            }

            projectService.wujiangMoneyInfoAdd(wujiangMoneyInfo, getLoginUser());
        } catch (Exception e) {
            RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    /**
     * 出图中状态编辑信息提交
     *
     * @param projectVo
     */
    @RequestMapping(value = "/api/disproject/UpdateProjectSubmit1", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateProjectSubmit1(ProjectVo projectVo) {
        try {
            projectService.projectEdit(projectVo, getLoginUser(),request);

        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
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
    @RequestMapping(value = "/api/disproject/disProjectChangeByid", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectChangeByid(String id) {
        ProjectVo projectVo = new ProjectVo();
        //设计信息
        DesignInfo designInfo = projectService.designInfoByPrimaryKey(id);
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(designInfo.getDesigner());
        if (memberManage != null){
            designInfo.setDesigner(memberManage.getMemberName());
        }
        //项目基本信息
        BaseProject baseProject = projectService.BaseProjectByid(designInfo.getBaseProjectId());
        projectVo.setDesignInfo(designInfo);
        projectVo.setBaseProject(baseProject);
        //各种费用
        if(!"4".equals(baseProject.getDistrict())){
            //设计费（安徽）
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
            if(anhuiMoneyinfo!=null){
                //如果为实收
                if("0".equals(anhuiMoneyinfo.getPayTerm())){
                    projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
                    projectVo.getMoneyInfo().setRevenue(anhuiMoneyinfo.getRevenue()+"");
                    projectVo.getMoneyInfo().setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts()+"");
                    projectVo.getMoneyInfo().setCostTime(anhuiMoneyinfo.getCollectionTime()+"");
                }else{
                    projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
//                    projectVo.getMoneyInfo().setRevenue(anhuiMoneyinfo.getRevenue()+"");
                    //代收金额添加
//                    String[] collectionMoney = anhuiMoneyinfo.getCollectionMoney().split(",");
//                    BigDecimal total = new BigDecimal(0);
//                    if("5".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                        projectVo.getMoneyInfo().setCollection05(new BigDecimal(collectionMoney[4]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1])).add
//                                (new BigDecimal(collectionMoney[2])).add
//                                (new BigDecimal(collectionMoney[3])).add
//                                (new BigDecimal(collectionMoney[4]));
//
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("4".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1])).add
//                                (new BigDecimal(collectionMoney[2])).add
//                                (new BigDecimal(collectionMoney[3]));
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("3".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1])).add
//                                (new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("2".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("1".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setTotal(new BigDecimal(collectionMoney[0]));
//                    }
//
//                    //代收时间添加
//                    String[] collectionMoneyTime = anhuiMoneyinfo.getCollectionMoneyTime().split(",");
//                    if("5".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                        projectVo.getMoneyInfo().setCollection05(new BigDecimal(collectionMoney[4]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1])).add
//                                (new BigDecimal(collectionMoney[2])).add
//                                (new BigDecimal(collectionMoney[3])).add
//                                (new BigDecimal(collectionMoney[4]));
//
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("4".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1])).add
//                                (new BigDecimal(collectionMoney[2])).add
//                                (new BigDecimal(collectionMoney[3]));
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("3".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1])).add
//                                (new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("2".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//
//                        total = new BigDecimal(collectionMoney[0]).add
//                                (new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setTotal(total);
//                    }else if("1".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setTotal(new BigDecimal(collectionMoney[0]));
//                    }
                }
            }else{
                projectVo.setAnhuiMoneyinfo(new AnhuiMoneyinfo());
                projectVo.setMoneyInfo(new MoneyInfo());
            }
            projectVo.setWujiangMoneyInfo(new WujiangMoneyInfo());
            projectVo.setMoneyInfo(new MoneyInfo());
        }else{
            //设计费（吴江）
            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
            if(wujiangMoneyInfo!=null){
                if("0".equals(wujiangMoneyInfo.getPayTerm())){

                    projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
                    projectVo.getMoneyInfo().setCostType(wujiangMoneyInfo.getCostType());
                    projectVo.getMoneyInfo().setDesignRate(wujiangMoneyInfo.getDesignRate()+"");
                    projectVo.getMoneyInfo().setOneDesMoney(wujiangMoneyInfo.getOneDesmoney()+"");
                    projectVo.getMoneyInfo().setRevenue(wujiangMoneyInfo.getRevenue()+"");
                    projectVo.getMoneyInfo().setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts()+"");
                    projectVo.getMoneyInfo().setCostTime(wujiangMoneyInfo.getCollectionTime());
                }else{

                    projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
//                    projectVo.getMoneyInfo().setRevenue(wujiangMoneyInfo.getRevenue()+"");


                    //代收金额
//                    String[] collectionMoney = wujiangMoneyInfo.getCollectionMoney().split(",");
//                    if("5".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                        projectVo.getMoneyInfo().setCollection05(new BigDecimal(collectionMoney[4]));
//                    }else if("4".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                    }else if("3".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                    }else if("2".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                    }else if("1".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                    }
//
//
//                    //代收时间添加
//                    String[] collectionMoneyTime = wujiangMoneyInfo.getCollectionMoneyTime().split(",");
//                    if("5".equals(collectionMoneyTime.length)){
//                        projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                        projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                        projectVo.getMoneyInfo().setCollection03Time(collectionMoneyTime[2]);
//                        projectVo.getMoneyInfo().setCollection04Time(collectionMoneyTime[3]);
//                        projectVo.getMoneyInfo().setCollection05Time(collectionMoneyTime[4]);
//                    }else if ("4".equals(collectionMoneyTime.length)){
//                        projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                        projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                        projectVo.getMoneyInfo().setCollection03Time(collectionMoneyTime[2]);
//                        projectVo.getMoneyInfo().setCollection04Time(collectionMoneyTime[3]);
//                    }else if ("3".equals(collectionMoneyTime.length)){
//                        projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                        projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                        projectVo.getMoneyInfo().setCollection03Time(collectionMoneyTime[2]);
//                    }else if ("2".equals(collectionMoneyTime.length)){
//                        projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                        projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                    }else if ("1".equals(collectionMoneyTime.length)){
//                        projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                    }
                }
            }else{
                projectVo.setWujiangMoneyInfo(new WujiangMoneyInfo());
                projectVo.setMoneyInfo(new MoneyInfo());
            }
            projectVo.setAnhuiMoneyinfo(new AnhuiMoneyinfo());
            projectVo.setMoneyInfo(new MoneyInfo());
        }
        //方案会审
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        if(packageCame!=null){
            projectVo.setPackageCame(packageCame);
        }else{
            projectVo.setPackageCame(new PackageCame());
        }

        //项目踏勘
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        if (projectExploration!=null){
            projectVo.setProjectExploration(projectExploration);
        }else{
            projectVo.setProjectExploration(new ProjectExploration());
        }

        //设计变更信息
        DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
        if(designChangeInfo!=null){
            //如果当前状态为已完成 则不展示数据
            if("4".equals(baseProject.getDesginStatus())){
                projectVo.setDesignChangeInfo(new DesignChangeInfo());
            }else{
                projectVo.setDesignChangeInfo(designChangeInfo);
            }
        }else{
            projectVo.setDesignChangeInfo(new DesignChangeInfo());
        }

        //设计变更累计
        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(id);
        if(designChangeInfos.size()>0){
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(designChangeInfo.getDesigner());
            designChangeInfo.setDesigner(memberManage1.getMemberName());
            projectVo.setDesignChangeCountFlag("0");  //说明之前进行过设计变更
        }else{
            projectVo.setDesignChangeCountFlag("1");
        }

//        //审核信息回显 根据当前用户判断
//        AuditInfo auditInfo = projectService.auditInfoByYes(getLoginUser(),designInfo.getId());
//        if(auditInfo!=null){
//            projectVo.setAuditInfo(auditInfo);
//        }else{
//            projectVo.setAuditInfo(new AuditInfo());
//        }

//        Example example1 = new Example(MemberManage.class);
//        example1.createCriteria().andEqualTo("id",projectVo.getDesignInfo().getDesigner());
//        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//        projectVo.getDesignInfo().setDesigner(memberManage.getMemberName());

        return RestUtil.success(projectVo);
    }

    @RequestMapping(value = "/api/disproject/disProjectChangeCountList", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectChangeCountList(String id) {
        //设计变更累计
        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(id);
        int number = 0;
        for (DesignChangeInfo designChangeInfo : designChangeInfos) {
            number++;
            designChangeInfo.setIdNumber(number+"");
        }
        return RestUtil.success(designChangeInfos);
    }

    @RequestMapping(value = "/api/disproject/disProjectChangeCount", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectChangeCount(String id) {
        //设计变更累计
        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(id);
        int size = designChangeInfos.size();
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("size",size);
        return RestUtil.success(map);
    }

    /**
     * 设计项目变更编辑
     *
     * @param projectVo
     */
    @RequestMapping(value = "/api/disproject/disProjectChangeEdit", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> disProjectChangeEdit(ProjectVo projectVo) {
        projectService.disProjectChangeEdit(projectVo, getLoginUser(),request);
        return RestUtil.success();
    }

    /**
     * 设计项目查看 第一版
     *
     * @return
     */
//    @PostMapping("/DesProjectInfoSelect")
//    public List<ProjectVo> DesProjectInfoSelect(@RequestBody BaseProject param) {
//        ArrayList<ProjectVo> projectVos = new ArrayList<>();
//        if (!"".equals(param.getVirtualCode())) {
//            //根据虚拟id查询
//            List<BaseProject> baseProjects = projectService.DesProjectInfoSelect(param.getVirtualCode());
//            for (BaseProject baseProject : baseProjects) {
//                ProjectVo projectVo = new ProjectVo();
//                projectVo.setBaseProject(baseProject);
//                //设计信息
//                DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
//                projectVo.setDesignInfo(designInfo);
//                //方案会审
//                ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
//                projectVo.setProjectExploration(projectExploration);
//                //项目勘探
//                PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
//                projectVo.setPackageCame(packageCame);
//                //项目审核
//                List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
//                projectVo.setAuditInfos(auditInfos);
//                //设计变更审核
//                List<AuditInfo> auditChangeInfos = projectService.auditInfoList(designInfo.getId());
//                projectVo.setAuditInfos2(auditChangeInfos);
//                //设计费用展示
//                if (baseProject.getDistrict() != "4") {
//                    //设计费（安徽）
//                    AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
//                    projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
//                } else {
//                    //设计费（吴江）
//                    WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
//                    projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
//                }
//                //设计变更累计
//                List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(designInfo.getId());
//                projectVo.setDesignChangeInfos(designChangeInfos);
//
//                //设计变更信息
//                DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
//                projectVo.setDesignChangeInfo(designChangeInfo);
//                projectVos.add(projectVo);
//            }
//            return projectVos;
//        }
//
//        //如果虚拟编号为空 说明不是合并项目 正常展示
//        ProjectVo projectVo = new ProjectVo();
//        //项目基本信息
//        BaseProject baseProject = projectService.BaseProjectByid(param.getId());
//        projectVo.setBaseProject(baseProject);
//        //设计信息
//        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
//        projectVo.setDesignInfo(designInfo);
//        //方案会审
//        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
//        projectVo.setProjectExploration(projectExploration);
//        //项目勘探
//        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
//        projectVo.setPackageCame(packageCame);
//        //项目审核
//        List<AuditInfo> auditInfos = projectService.auditInfoList(designInfo.getId());
//        projectVo.setAuditInfos(auditInfos);
//        //设计变更审核
//        List<AuditInfo> auditChangeInfos = projectService.auditInfoList(designInfo.getId());
//        projectVo.setAuditInfos2(auditChangeInfos);
//        //设计变更累计
//        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(designInfo.getId());
//        projectVo.setDesignChangeInfos(designChangeInfos);
//        //设计变更信息
//        DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
//        projectVo.setDesignChangeInfo(designChangeInfo);
//
//        //设计费用展示
//        if (baseProject.getDistrict() != "4") {
//            //设计费（安徽）
//            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
//            projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
//        } else {
//            //设计费（吴江）
//            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
//            projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
//        }
//        projectVo.setDesginStatus(baseProject.getDesginStatus());
//        projectVos.add(projectVo);
//        return projectVos;
//    }

    /**
     * 设计项目查看 新
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/disproject/DesProjectInfoSelect", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> DesProjectInfoSelect(String id) {
        ProjectVo projectVo = new ProjectVo();
        //根据id查找设计信息
        DesignInfo designInfo = projectService.designInfoByPrimaryKey(id);
        /**
         * 如果id找不到说明传过来得是baseProjectid
         */
        if(designInfo==null){
            BaseProject baseProject = projectService.BaseProjectByid(id);
            DesignInfo designInfo1 = projectService.designInfoByid(baseProject.getId());
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(designInfo.getDesigner());
            designInfo = designInfo1;
            if (memberManage != null){
                designInfo.setDesigner(memberManage.getMemberName());
            }
        }else {
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(designInfo.getDesigner());
            if (memberManage != null){
                designInfo.setDesigner(memberManage.getMemberName());
            }
        }
        projectVo.setDesignInfo(designInfo);
        //根据设计信息查找基本信息
        BaseProject baseProject = projectService.BaseProjectByid(designInfo.getBaseProjectId());
        ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(baseProject.getConstructionOrganization());
        if (constructionUnitManagement != null){
            baseProject.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
        }
        projectVo.setBaseProject(baseProject);
        //各种费用
        if(!"吴江".equals(baseProject.getDistrict())){
            //设计费（安徽）
            Example example = new Example(AnhuiMoneyinfo.class);
            Example.Criteria c = example.createCriteria();
            //根据设计表id 查询数据取出代收金额
            c.andEqualTo("baseProjectId",designInfo.getId());
            AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(example);
//            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyInfopayterm(designInfo.getId());
            if(anhuiMoneyinfo!=null){
                //如果为实收
               if("0".equals(anhuiMoneyinfo.getPayTerm())){
                   projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);

//                   projectVo.getMoneyInfo().setRevenue(anhuiMoneyinfo.getRevenue()+"");
//                   projectVo.getMoneyInfo().setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts()+"");
//                   projectVo.getMoneyInfo().setCostTime(anhuiMoneyinfo.getCollectionTime()+"");
               }else{
                   // 总金额累加
                   String collectionMoney = anhuiMoneyinfo.getCollectionMoney();
                   String[] split = collectionMoney.split(",");
                   BigDecimal num = new BigDecimal(0);
                   if (split != null){
                       for (String s : split) {
                           num = num.add(new BigDecimal(s));
                       }
                   }
                   anhuiMoneyinfo.setOfficialReceipts(num);
                   projectVo.setAnhuiMoneyinfo(anhuiMoneyinfo);
//                   projectVo.getMoneyInfo().setRevenue(anhuiMoneyinfo.getRevenue()+"");
                   //代收金额添加
//                   String[] collectionMoney = anhuiMoneyinfo.getCollectionMoney().split(",");
//                   BigDecimal total = new BigDecimal(0);
//                   if("5".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                       projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                       projectVo.getMoneyInfo().setCollection05(new BigDecimal(collectionMoney[4]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1])).add
//                               (new BigDecimal(collectionMoney[2])).add
//                               (new BigDecimal(collectionMoney[3])).add
//                               (new BigDecimal(collectionMoney[4]));
//
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("4".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                       projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1])).add
//                               (new BigDecimal(collectionMoney[2])).add
//                               (new BigDecimal(collectionMoney[3]));
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("3".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1])).add
//                               (new BigDecimal(collectionMoney[2]));
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("2".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("1".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setTotal(new BigDecimal(collectionMoney[0]));
//                   }
//
//                   //代收时间添加
//                   String[] collectionMoneyTime = anhuiMoneyinfo.getCollectionMoneyTime().split(",");
//                   if("5".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                       projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                       projectVo.getMoneyInfo().setCollection05(new BigDecimal(collectionMoney[4]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1])).add
//                               (new BigDecimal(collectionMoney[2])).add
//                               (new BigDecimal(collectionMoney[3])).add
//                               (new BigDecimal(collectionMoney[4]));
//
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("4".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                       projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1])).add
//                               (new BigDecimal(collectionMoney[2])).add
//                               (new BigDecimal(collectionMoney[3]));
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("3".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1])).add
//                               (new BigDecimal(collectionMoney[2]));
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("2".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//
//                       total = new BigDecimal(collectionMoney[0]).add
//                               (new BigDecimal(collectionMoney[1]));
//                       projectVo.getMoneyInfo().setTotal(total);
//                   }else if("1".equals(collectionMoney.length)){
//                       projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                       projectVo.getMoneyInfo().setTotal(new BigDecimal(collectionMoney[0]));
//                   }
               }
            }else{
                projectVo.setAnhuiMoneyinfo(new AnhuiMoneyinfo());
                projectVo.setMoneyInfo(new MoneyInfo());
            }
            projectVo.setWujiangMoneyInfo(new WujiangMoneyInfo());
            projectVo.setMoneyInfo(new MoneyInfo());
        }else{
            //设计费（吴江）
            Example example = new Example(WujiangMoneyInfo.class);
            Example.Criteria c = example.createCriteria();
            //根据设计表id 查询数据取出代收金额
            c.andEqualTo("baseProjectId", designInfo.getId());
            WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(example);
//            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
            if(wujiangMoneyInfo!=null){
                if("0".equals(wujiangMoneyInfo.getPayTerm())){

                    projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
//                    projectVo.getMoneyInfo().setCostType(wujiangMoneyInfo.getCostType());
//                    projectVo.getMoneyInfo().setDesignRate(wujiangMoneyInfo.getDesignRate()+"");
//                    projectVo.getMoneyInfo().setOneDesMoney(wujiangMoneyInfo.getOneDesmoney()+"");
//                    projectVo.getMoneyInfo().setRevenue(wujiangMoneyInfo.getRevenue()+"");
//                    projectVo.getMoneyInfo().setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts()+"");
//                    projectVo.getMoneyInfo().setCostTime(wujiangMoneyInfo.getCollectionTime());
                }else{
                    // 总金额累加
                    String collectionMoney = wujiangMoneyInfo.getCollectionMoney();
                    String[] split = collectionMoney.split(",");
                    BigDecimal num = new BigDecimal(0);
                    if (split != null){
                        for (String s : split) {
                            num = num.add(new BigDecimal(s));
                        }
                    }
                   wujiangMoneyInfo.setOfficialReceipts(num);
                    projectVo.setWujiangMoneyInfo(wujiangMoneyInfo);
//                    projectVo.getMoneyInfo().setRevenue(wujiangMoneyInfo.getRevenue()+"");


                    //代收金额
//                    String[] collectionMoney = wujiangMoneyInfo.getCollectionMoney().split(",");
//                    if("5".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                        projectVo.getMoneyInfo().setCollection05(new BigDecimal(collectionMoney[4]));
//                    }else if("4".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                        projectVo.getMoneyInfo().setCollection04(new BigDecimal(collectionMoney[3]));
//                    }else if("3".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                        projectVo.getMoneyInfo().setCollection03(new BigDecimal(collectionMoney[2]));
//                    }else if("2".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                        projectVo.getMoneyInfo().setCollection02(new BigDecimal(collectionMoney[1]));
//                    }else if("1".equals(collectionMoney.length)){
//                        projectVo.getMoneyInfo().setCollection01(new BigDecimal(collectionMoney[0]));
//                    }


                    //代收时间添加
//                    if(wujiangMoneyInfo.getCollectionMoneyTime()!=null){
//                        String[] collectionMoneyTime = wujiangMoneyInfo.getCollectionMoneyTime().split(",");
//                        if("5".equals(collectionMoneyTime.length)){
//                            projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                            projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                            projectVo.getMoneyInfo().setCollection03Time(collectionMoneyTime[2]);
//                            projectVo.getMoneyInfo().setCollection04Time(collectionMoneyTime[3]);
//                            projectVo.getMoneyInfo().setCollection05Time(collectionMoneyTime[4]);
//                        }else if ("4".equals(collectionMoneyTime.length)){
//                            projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                            projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                            projectVo.getMoneyInfo().setCollection03Time(collectionMoneyTime[2]);
//                            projectVo.getMoneyInfo().setCollection04Time(collectionMoneyTime[3]);
//                        }else if ("3".equals(collectionMoneyTime.length)){
//                            projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                            projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                            projectVo.getMoneyInfo().setCollection03Time(collectionMoneyTime[2]);
//                        }else if ("2".equals(collectionMoneyTime.length)){
//                            projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                            projectVo.getMoneyInfo().setCollection02Time(collectionMoneyTime[1]);
//                        }else if ("1".equals(collectionMoneyTime.length)){
//                            projectVo.getMoneyInfo().setCollection01Time(collectionMoneyTime[0]);
//                        }
//                    }
                }
            }else{
                projectVo.setWujiangMoneyInfo(new WujiangMoneyInfo());
                projectVo.setMoneyInfo(new MoneyInfo());
            }
            projectVo.setAnhuiMoneyinfo(new AnhuiMoneyinfo());
            projectVo.setMoneyInfo(new MoneyInfo());
        }

        //方案会审
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        if(packageCame!=null){
            projectVo.setPackageCame(packageCame);
        }else{
            projectVo.setPackageCame(new PackageCame());
        }

        //项目踏勘
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        if (projectExploration!=null){
            projectVo.setProjectExploration(projectExploration);
        }else{
            projectVo.setProjectExploration(new ProjectExploration());
        }

        //设计变更信息
        DesignChangeInfo designChangeInfo = projectService.designChangeInfoByid(designInfo.getId());
        if(designChangeInfo!=null){
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(designChangeInfo.getDesigner());
            if (memberManage1 != null){
                designChangeInfo.setDesigner(memberManage1.getMemberName());
            }
            projectVo.setDesignChangeInfo(designChangeInfo);
        }else{
            projectVo.setDesignChangeInfo(new DesignChangeInfo());
        }

        //审核信息回显 根据当前用户判断
        AuditInfo auditInfo = projectService.auditInfoByYes(getLoginUser(),designInfo.getId());
        if(auditInfo!=null){
            projectVo.setAuditInfo(auditInfo);
        }else{
            projectVo.setAuditInfo(new AuditInfo());
        }

        //设计变更累计
        List<DesignChangeInfo> designChangeInfos = projectService.designChangeInfosByid(id);
        if(designChangeInfos.size()>0){
            projectVo.setDesignChangeCountFlag("0");  //说明之前进行过设计变更
        }else{
            projectVo.setDesignChangeCountFlag("1");
        }

        String designUnit = projectVo.getDesignInfo().getDesignUnit();
       String desiUnit =  projectService.findDesignUnit(designUnit);
        projectVo.getDesignInfo().setDesignUnit(desiUnit);
        return RestUtil.success(projectVo);
    }

    /**
     * 设计页面 合并列表
     * @param vid
     * @return
     */
    @RequestMapping(value = "/api/disproject/DesProjectInfoSelectList", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> DesProjectInfoSelectList(String vid) {
        //通过虚拟编号查找
        if(vid!=null&&!"".equals(vid)){
            List<BaseProject> baseProjects = projectService.DesProjectInfoSelect(vid);
            return RestUtil.success(baseProjects);
        }else{
            return RestUtil.error("vid为空");
        }
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
        ProjectOverviewVo projectOverviewVo = new ProjectOverviewVo(aLong,s,i);
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

    /**
     * 项目概况 项目收支动态
     * @param id
     * @return
     */
    //    @GetMapping("/BuildSum/{id}")
    @RequestMapping(value = "/api/disproject/BuildSum", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> BuildSum(@RequestParam(name = "id") String id) {
        SumVo sumVo = new SumVo();
        //设计费支出
        BigDecimal desMoneySum = projectService.desMoneySum(id);
        if(StringUtil.isEmpty(desMoneySum.toString())){
            sumVo.setDesMoneySum("--");
        }else{
            sumVo.setDesMoneySum(desMoneySum.toString());
        }

        //招标控制价
        BigDecimal biddingPriceControlSum = projectService.biddingPriceControlSum(id);
        if(StringUtil.isEmpty(biddingPriceControlSum.toString())){
            sumVo.setBiddingPriceControlSum("--");
        }else{
            sumVo.setBiddingPriceControlSum(biddingPriceControlSum.toString());
        }

        //成本总金额
        BigDecimal costTotalAmountSum = projectService.costTotalAmountSum(id);
        if(StringUtil.isEmpty(costTotalAmountSum.toString())){
            sumVo.setCostTotalAmountSum("--");
        }else {
            sumVo.setCostTotalAmountSum(costTotalAmountSum.toString());
        }

        //造价金额
        BigDecimal amountCostAmountSum = projectService.amountCostAmountSum(id);
        if(StringUtil.isEmpty(amountCostAmountSum.toString())){
            sumVo.setAmountCostAmountSum("--");
        }else{
            sumVo.setAmountCostAmountSum(amountCostAmountSum.toString());
        }

        //设计费委外支出
        BigDecimal outsourceMoneySum = projectService.outsourceMoneySum(id);
        if(StringUtil.isEmpty(outsourceMoneySum.toString())){
            sumVo.setOutsourceMoneySum("--");
        }else{
            sumVo.setOutsourceMoneySum(outsourceMoneySum.toString());
        }

        //造价咨询费收入
        BigDecimal consultingIncome = projectService.consultingIncome(id);
        if(StringUtil.isEmpty(consultingIncome.toString())){
            sumVo.setConsultingIncome("--");
        }else {
            sumVo.setConsultingIncome(consultingIncome.toString());
        }

        //造价咨询费支出
        BigDecimal consultingExpenditure = projectService.consultingExpenditure(id);
        if(StringUtil.isEmpty(consultingIncome.toString())){
            sumVo.setConsultingExpenditure("--");
        }else {
            sumVo.setConsultingExpenditure(consultingExpenditure.toString());
        }
        return RestUtil.success(sumVo);
    }

    /**
     * 员工首页-设计部门 消息提醒(造价也能用)
     * @return
     */
    @RequestMapping(value = "/api/disproject/messageList", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> messageList() {
        List<MessageNotification> messageNotifications = projectService.messageList(getLoginUser());
        return RestUtil.success(messageNotifications);
    }

    /**
     * 员工首页-设计部门 设计项目代办任务
     * @return
     */
    @RequestMapping(value = "/api/disproject/designReviewedCount", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> designReviewedCount() {
        Integer integer = projectService.designReviewedCount(getLoginUser());
        return RestUtil.success(integer);
    }
    /**
     * 员工首页-设计部门 设计变更项目代办任务
     * @return
     */
    @RequestMapping(value = "/api/disproject/designChangeReviewedCount", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> designChangeReviewedCount() {
        Integer integer = projectService.designChangeReviewedCount(getLoginUser());
        return RestUtil.success(integer);
    }

    /**
     * 员工首页-设计部门  个人任务统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/disproject/censusList", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> censusList(CostVo2 costVo2) {
        //todo getLoginUser().getId()
        String userId = getLoginUser().getId();
        //将当前用户填入
        if(userId!=null){
            costVo2.setId(userId);
        }
        List<OneCensus> oneCensuses = projectService.OneCensusList(costVo2);

        String censusList = "[{\"companyName\":\"市政管道\"," +
                "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList +=
                        "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                                "\"truckAmmount\": \"" + oneCensus.getMunicipalPipeline() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"管网改造\"," +
                        "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                        "\"truckAmmount\": \"" + oneCensus.getNetworkReconstruction() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"新建小区\"," +
                        "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList +=
                        "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                                "\"truckAmmount\": \"" + oneCensus.getNewCommunity() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"二次供水改造项目\"," +
                        "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList +=
                        "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                                "\"truckAmmount\": \"" + oneCensus.getSecondaryWater() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"工商户\"," +
                        "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                        "\"truckAmmount\": \"" + oneCensus.getCommercialHouseholds() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"居民装接水\"," +
                        "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                        "\"truckAmmount\": \"" + oneCensus.getWaterResidents() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"行政事业\"," +
                        "\"imageAmmount\": [";
        if(oneCensuses.size()>0){
            for (OneCensus oneCensus : oneCensuses) {
                censusList += "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                        "\"truckAmmount\": \"" + oneCensus.getAdministration() + "\"},";
            }
            censusList = censusList.substring(0, censusList.length() - 1);
        }else{
            censusList+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

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
        individualVo.setId(getLoginUser().getId());
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
                    if(bigDecimal!=null){
                        project.setAccrualMoney(bigDecimal.doubleValue());
                    }else{
                        project.setAccrualMoney(0.0);
                    }
                    //建议金额
                    BigDecimal bigDecimal1 = projectService.proposedAmount(bigDecimal);
                    if(bigDecimal1!=null){
                        project.setAdviseMoney(bigDecimal1.doubleValue());
                    }else{
                        project.setAdviseMoney(0.0);
                    }
                    // 余额
                    BigDecimal surplus = projectService.surplus(bigDecimal, bigDecimal1);
                    if(surplus!=null){
                        project.setSurplus(surplus.doubleValue());
                    }else{
                        project.setSurplus(0.0);
                    }
                }else{
                    project.setDesMoney(new BigDecimal(0));
                    project.setAccrualMoney(0.0);
                    project.setAdviseMoney(0.0);
                    project.setSurplus(0.0);
                }
            } else {
                //设计费（吴江）
                WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfopayterm(designInfo.getId());
                if (wujiangMoneyInfo != null) {
                    project.setDesMoney(wujiangMoneyInfo.getOfficialReceipts());
                    //应计提金额
                    BigDecimal bigDecimal = projectService.accruedAmount(wujiangMoneyInfo.getOfficialReceipts());
                    if(bigDecimal!=null){
                        project.setAccrualMoney(bigDecimal.doubleValue());
                    }else{
                        project.setAccrualMoney(0.0);
                    }

                    //建议金额
                    BigDecimal bigDecimal1 = projectService.proposedAmount(bigDecimal);
                    if(bigDecimal1!=null){
                        project.setAdviseMoney(bigDecimal1.doubleValue());
                    }else{
                        project.setAdviseMoney(0.0);
                    }

                    // 余额
                    BigDecimal surplus = projectService.surplus(bigDecimal, bigDecimal1);
                    if(surplus!=null){
                        project.setSurplus(surplus.doubleValue());
                    }else{
                        project.setSurplus(0.0);
                    }
                }else{
                    project.setDesMoney(new BigDecimal(0));
                    project.setAccrualMoney(0.0);
                    project.setAdviseMoney(0.0);
                    project.setSurplus(0.0);
                }
            }
            //造价费用
            Budgeting budgeting = projectService.budgetingByid(project.getId());
            if(budgeting!=null){
                if(budgeting.getAmountCost()!=null){
                    project.setAmountCost(budgeting.getAmountCost());
                }else{
                    project.setAmountCost(new BigDecimal(0));
                }
            }else{
                project.setAmountCost(new BigDecimal(0));
            }
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
        ConstructionUnitManagement unitManagement = constructionUnitManagementMapper.selectByPrimaryKey(baseProject.getConstructionOrganization());
        if (unitManagement != null){
            baseProject.setConstructionOrganization(unitManagement.getConstructionUnitName());
        }

        projectVo3.setBaseProject(baseProject);
        //设计信息
        DesignInfo designInfo = projectService.designInfoByid(baseProject.getId());
        projectVo3.setDesignInfo(designInfo);
        if(designInfo == null){
            if (designInfo.getDesignChangeTime() != null){
                projectVo3.getDesignInfo().setDesignChangeTime(designInfo.getDesignChangeTime());
            }else {
                projectVo3.getDesignInfo().setDesignChangeTime("-");
            }
            projectVo3.setDesignChangeInfo(new DesignChangeInfo());
        }else{
            if (designInfo.getDesigner() != null){
                Example example = new Example(MemberManage.class);
                example.createCriteria().andEqualTo("id",designInfo.getDesigner());
                MemberManage memberManage = memberManageDao.selectOneByExample(example);
                if (memberManage != null){
                    projectVo3.getDesignInfo().setDesigner(memberManage.getMemberName());
                }else {
                    projectVo3.getDesignInfo().setDesigner("-");
                }
            }

            //正式出图时间
            if (designInfo.getBlueprintStartTime() != null && !"".equals(designInfo.getBlueprintStartTime())){
                projectVo3.getDesignInfo().setBlueprintStartTime(designInfo.getBlueprintStartTime());
            }else {
                projectVo3.getDesignInfo().setBlueprintStartTime("-");
            }
            //设计单位名称
            if (designInfo.getDesignUnit() != null && !"".equals(designInfo.getDesignUnit())){
                projectVo3.getDesignInfo().setDesignUnit(designInfo.getDesignUnit());
            }else {
                projectVo3.getDesignInfo().setDesignUnit("-");
            }
            projectVo3.setDesignInfo(designInfo);
        }
        //根据地区判断相应的设计费 应付金额 实付金额
        //如果为安徽
        if(!baseProject.getDistrict().equals("4")){
            AnhuiMoneyinfo anhuiMoneyinfo = projectService.anhuiMoneyinfoByid(designInfo.getId());
            if(anhuiMoneyinfo!=null){
                //实收
                if ("0".equals(anhuiMoneyinfo.getPayTerm())){
                    designInfo.setRevenue(anhuiMoneyinfo.getRevenue()+"");
                    designInfo.setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts());
                }else {
                    // 累加实收金额
                    String collectionMoney = anhuiMoneyinfo.getCollectionMoney();
                    String[] split = collectionMoney.split(",");
                    BigDecimal num = new BigDecimal(0);
                    for (String thisNum : split) {
                        num = num.add(new BigDecimal(thisNum));
                    }
                    designInfo.setRevenue(anhuiMoneyinfo.getRevenue() + "");
                    designInfo.setOfficialReceipts(num);
                    designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                    designInfo.setPayTerm(anhuiMoneyinfo.getPayTerm());
                }
            }else{
                designInfo.setRevenue("0");
                designInfo.setOfficialReceipts(new BigDecimal(0));
                designInfo.setDisMoney(new BigDecimal(0));
                designInfo.setPayTerm("0");
            }
        }else{
            //如果为吴江
            WujiangMoneyInfo wujiangMoneyInfo = projectService.wujiangMoneyInfoByid(designInfo.getId());
            if(wujiangMoneyInfo!=null) {
                //实收
                if ("0".equals(wujiangMoneyInfo.getPayTerm())) {
                    designInfo.setRevenue(wujiangMoneyInfo.getRevenue()+"");
                    designInfo.setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts());
                }else {
                    String collectionMoney = wujiangMoneyInfo.getCollectionMoney();
                    String[] split = collectionMoney.split(",");
                    BigDecimal num = new BigDecimal(0);
                    for (String thisNum : split) {
                        num = num.add(new BigDecimal(thisNum));
                    }
                    designInfo.setRevenue(wujiangMoneyInfo.getRevenue() + "");
                    designInfo.setOfficialReceipts(num);
                    designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                    designInfo.setPayTerm(wujiangMoneyInfo.getPayTerm());
                }
            }
        }
        //项目探勘
        ProjectExploration projectExploration = projectService.ProjectExplorationByid(designInfo.getId());
        projectVo3.setProjectExploration(projectExploration);
        if(projectExploration==null){
            projectVo3.setProjectExploration(new ProjectExploration());
        }else{
            // 探勘人
            if (projectExploration.getScout() != null && !"".equals(projectExploration.getScout())){
                projectVo3.getProjectExploration().setScout(projectExploration.getScout());
            }else {
                projectVo3.getProjectExploration().setScout("-");
            }
            // 探勘时间
            if (projectExploration.getExplorationTime() != null && !"".equals(projectExploration.getExplorationTime())){
                projectVo3.getProjectExploration().setExplorationTime(projectExploration.getExplorationTime());
            }else {
                projectVo3.getProjectExploration().setExplorationTime("-");
            }

            projectVo3.setProjectExploration(projectExploration);}
        //方案会审
        PackageCame packageCame = projectService.PackageCameByid(designInfo.getId());
        projectVo3.setPackageCame(packageCame);
        if(packageCame==null){
            projectVo3.setPackageCame(new PackageCame());
        }else{
            // 会审时间
            if (packageCame.getPartTime() != null && !"".equals(packageCame.getPartTime())){
                packageCame.setPartTime(packageCame.getPartTime());
            }else {
                packageCame.setPartTime("-");
            }
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

//        Budgeting budgeting = projectService.budgetingByid(baseProject.getId());
        Budgeting budgeting = budgetingMapper.selectOneBudgeting(baseProject.getId());
        projectVo3.setBudgeting(budgeting);
        if(budgeting != null){

            //造价金额
            if (budgeting.getAmountCost() != null && !"".equals(budgeting.getAmountCost())){
                budgeting.setAmountCost(budgeting.getAmountCost());
            }else {
                budgeting.setAmountCost(new BigDecimal(0));
            }
            //预算编制人
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(budgeting.getBudgetingPeople());
            String memberName = memberManage.getMemberName();
            if (memberName != null ){
                budgeting.setBudgetingPeople(memberName);
            }else {
                budgeting.setBudgetingPeople("-");
            }
            //预算编制时间
            if (budgeting.getBudgetingTime() != null && !"".equals(budgeting.getBudgetingTime())){
                budgeting.setBudgetingTime(budgeting.getBudgetingTime());
            }else {
                budgeting.setBudgetingTime("-");
            }
            //造价增值税金额
            if (budgeting.getAddedTaxAmount() != null && !"".equals(budgeting.getAddedTaxAmount())){
                budgeting.setAddedTaxAmount(budgeting.getAddedTaxAmount());
            }else {
                budgeting.setAddedTaxAmount("0");
            }
            // 造价单位名称
            if (budgeting.getCostUnitName() != null && !"".equals(budgeting.getCostUnitName())){
                budgeting.setCostUnitName(budgeting.getCostUnitName());
            }else {
                budgeting.setCostUnitName("-");
            }
            // 委外金额
            if (budgeting.getOutsourcing() != null && !"".equals(budgeting.getOutsourcing())){
                budgeting.setOutsourcing(budgeting.getOutsourcing());
            }else {
                budgeting.setOutsourcing("-");
            }
            projectVo3.setBudgeting(budgeting);
            //成本编制
            CostPreparation costPreparation = projectService.costPreparationById(budgeting.getId());
            projectVo3.setCostPreparation(costPreparation);
            if(costPreparation==null){
                projectVo3.setCostPreparation(new CostPreparation());
            }else{
                // 成本编制人
                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(costPreparation.getCostTogether());
                if (memberManage1 != null) {
                    costPreparation.setCostTogether(memberManage1.getMemberName());
                }else {
                    costPreparation.setCostTogether("/");
                }
                // 成本编制时间
                if (costPreparation.getCostPreparationTime() != null && !"".equals(costPreparation.getCostPreparationTime())){
                    costPreparation.setCostPreparationTime(costPreparation.getCostPreparationTime());
                }else {
                    costPreparation.setCostPreparationTime("-");
                }
                // 成本总金额
                if (costPreparation.getCostTotalAmount() != null && !"".equals(costPreparation.getCostTotalAmount())){
                    costPreparation.setCostTotalAmount(costPreparation.getCostTotalAmount());
                }else {
                    costPreparation.setCostTotalAmount(new BigDecimal(0));
                }
                projectVo3.setCostPreparation(costPreparation);
            }
            //控价编制
            VeryEstablishment veryEstablishment = projectService.veryEstablishmentById2(budgeting.getId());
            if(veryEstablishment == null){
                projectVo3.setVeryEstablishment(new VeryEstablishment());
            }else{
                //其中增值税
                if (veryEstablishment.getVatAmount() != null && !"".equals(veryEstablishment.getVatAmount())){
                    veryEstablishment.setVatAmount(veryEstablishment.getVatAmount());
                }else {
                    veryEstablishment.setVatAmount(new BigDecimal(0));
                }
                //控价编制人
                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(veryEstablishment.getPricingTogether());
                if (memberManage1 != null){
                    veryEstablishment.setPricingTogether(memberManage1.getMemberName());
                }else {
                    veryEstablishment.setPricingTogether("/");
                }
                //控价编制时间
                if (veryEstablishment.getEstablishmentTime() != null && !"".equals(veryEstablishment.getEstablishmentTime())){
                    veryEstablishment.setEstablishmentTime(veryEstablishment.getEstablishmentTime());
                }else {
                    veryEstablishment.setEstablishmentTime("-");
                }
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
            //CEA总金额
            if (trackAuditInfo.getCeaTotalMoney() != null && !"".equals(trackAuditInfo.getCeaTotalMoney())){
                trackAuditInfo.setCeaTotalMoney(trackAuditInfo.getCeaTotalMoney());
            }else {
                trackAuditInfo.setCeaTotalMoney(new BigDecimal(0));
            }
            //项目经理
            if (trackAuditInfo.getPm() != null && !"".equals(trackAuditInfo.getPm())){
                trackAuditInfo.setPm(trackAuditInfo.getPm());
            }else {
                trackAuditInfo.setPm("-");
            }
            //设计单位名称
            DesignUnitManagement designUnitManagement = designUnitManagementDao.selectByPrimaryKey(trackAuditInfo.getDesignOrganizationId());
            if (designUnitManagement != null){
                trackAuditInfo.setDesignOrganizationId(designUnitManagement.getDesignUnitName());
            }else {
                trackAuditInfo.setDesignOrganizationId("/");
            }
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(trackAuditInfo.getAuditUnitNameId());
            if (costUnitManagement != null){
                trackAuditInfo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
            }else {
                trackAuditInfo.setAuditUnitNameId("-");
            }
            // 合同金额
            if (trackAuditInfo.getContractAmount() != null && !"".equals(trackAuditInfo.getContractAmount())){
                trackAuditInfo.setContractAmount(trackAuditInfo.getContractAmount());
            }else {
                trackAuditInfo.setContractAmount(new BigDecimal(0));
            }
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
        projectVo3.setAmountVisaChangeSum(projectVo32.getAmountVisaChangeSum()); //次数
        projectVo3.setChangeCount(projectVo32.getChangeCount());
        projectVo3.setUpContractAmount(projectVo32.getUpContractAmount());
        projectVo3.setDownContractAmount(projectVo32.getDownContractAmount());


        //下家结算送审
        SettlementAuditInformation settlementAuditInformation = projectService.SettlementAuditInformationByid(baseProject.getId());
        if(settlementAuditInformation==null){
            projectVo3.setSettlementAuditInformation(new SettlementAuditInformation());
        }else{
            // 核减率
            Example example = new Example(SettlementInfo.class);
            example.createCriteria().andEqualTo("baseProjectId",baseProject.getId())
                                    .andEqualTo("upAndDown","2");
            SettlementInfo settlementInfo = settlementInfoMapper.selectOneByExample(example);
            if (settlementInfo != null){
                String sumbitMoney = settlementInfo.getSumbitMoney();
                BigDecimal subtractTheNumber = settlementAuditInformation.getSubtractTheNumber();
                BigDecimal bigDecimal = new BigDecimal(sumbitMoney);
                if (subtractTheNumber!=null && bigDecimal!=null){
                    BigDecimal divide = subtractTheNumber.divide(bigDecimal,4,RoundingMode.HALF_UP);
                    BigDecimal multiply = divide.multiply(new BigDecimal(100));
                    BigDecimal bigDecimal1 = multiply.setScale(2, RoundingMode.HALF_UP);
                    settlementAuditInformation.setSubtractRate(bigDecimal1);
                }
            }
            // 造价单位名称
            CostUnitManagement costUnit = costUnitManagementMapper.selectByPrimaryKey(settlementAuditInformation.getNameOfTheCost());
            if (costUnit != null){
                settlementAuditInformation.setNameOfTheCost(costUnit.getCostUnitName());
            }else {
                settlementAuditInformation.setNameOfTheCost("-");
            }
            // 审定数
            if (settlementAuditInformation.getAuthorizedNumber() != null && !"".equals(settlementAuditInformation.getAuthorizedNumber())){
                settlementAuditInformation.setAuthorizedNumber(settlementAuditInformation.getAuthorizedNumber());
            }else {
                settlementAuditInformation.setAuthorizedNumber(new BigDecimal(0));
            }
            // 核减数
            if (settlementAuditInformation.getSubtractTheNumber() != null && !"".equals(settlementAuditInformation.getSubtractTheNumber())){
                settlementAuditInformation.setSubtractTheNumber(settlementAuditInformation.getSubtractTheNumber());
            }else {
                settlementAuditInformation.setSubtractTheNumber(new BigDecimal(0));
            }
            // 核增数
            if (settlementAuditInformation.getNuclearNumber() != null && !"".equals(settlementAuditInformation.getNuclearNumber())){
                settlementAuditInformation.setNuclearNumber(settlementAuditInformation.getNuclearNumber());
            }else {
                settlementAuditInformation.setNuclearNumber(new BigDecimal(0));
            }
            // 委外金额amountOutsourcing
            if (settlementAuditInformation.getAmountOutsourcing() != null && !"".equals(settlementAuditInformation.getAmountOutsourcing())){
                settlementAuditInformation.setAmountOutsourcing(settlementAuditInformation.getAmountOutsourcing());
            }else {
                settlementAuditInformation.setAmountOutsourcing(new BigDecimal("0"));
            }
            projectVo3.setSettlementAuditInformation(settlementAuditInformation);
        }
        //上家结算送审
        LastSettlementReview lastSettlementReview = projectService.lastSettlementReviewbyid(baseProject.getId());
        if(lastSettlementReview==null){
            projectVo3.setLastSettlementReview(new LastSettlementReview());
        }else{
            // 送审数
            if (lastSettlementReview.getReviewNumber() == null){
                lastSettlementReview.setReviewNumber(new BigDecimal(0));
            }
            // 编制人
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(lastSettlementReview.getPreparePeople());
            if (memberManage != null){
                lastSettlementReview.setPreparePeople(memberManage.getMemberName());
            }else {
                lastSettlementReview.setPreparePeople("-");
            }
            // 造价单位名称
            CostUnitManagement costUnit = costUnitManagementMapper.selectByPrimaryKey(lastSettlementReview.getNameOfTheCost());
            if (costUnit != null){
                lastSettlementReview.setNameOfTheCost(costUnit.getCostUnitName());
            }else {
                lastSettlementReview.setNameOfTheCost("-");
            }
            // 编制时间
            if (lastSettlementReview.getCompileTime() == null){
                lastSettlementReview.setCompileTime("-");
            }
            // 合同金额
            if (lastSettlementReview.getContractAmount() == null){
                lastSettlementReview.setContractAmount(new BigDecimal(0));
            }
            projectVo3.setLastSettlementReview(lastSettlementReview);
        }
        // 如果上下家其中一个有值的话就返回1 给低代码做显隐判断
        if (projectVo3.getLastSettlementReview() != null || projectVo3.getSettlementAuditInformation() != null ){
            projectVo3.setWori(1);
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
        String userId = "";
        String budgetingCount = projectService.budgetingCount(userId, district);
        String settleAccountsCount = projectService.settleAccountsCount(userId, district);
        String progressPaymentInformationCount = projectService.progressPaymentInformationCount(userId, district);
        String visaApplyChangeInformationCount = projectService.visaApplyChangeInformationCount(userId, district);
        String trackAuditInfoCount = projectService.trackAuditInfoCount(userId, district);
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
        if(oneCensus2s.size()>0){
            for (OneCensus2 oneCensus2 : oneCensus2s) {
                json2 +=
                        "{\"time\": \"" + oneCensus2.getYeartime() + "-" + oneCensus2.getMonthTime() + "\"," +
                                "\"truckAmmount\": \"" + oneCensus2.getTotal() + "\"" +
                                "},";
            }
        }else{
            json2+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        json2 = json2.substring(0, json2.length() - 1);
        json2 += "]}]";
        JSONArray objects2 = JSON.parseArray(json2);
        return RestUtil.success(objects2);
    }

    @RequestMapping(value = "/api/costproject/costCensusAndSearch", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> costCensusAndSearch(CostVo2 costVo2) {
        OneCensus2 oneCensus2 = projectService.costCensus(costVo2);
        if(oneCensus2==null){
            OneCensus2 oneCensus =new OneCensus2();
            oneCensus.setBudget(0);
            oneCensus.setSettleaccounts(0);
            oneCensus.setProgresspayment(0);
            oneCensus.setVisa(0);
            oneCensus.setTrack(0);
            oneCensus2 = oneCensus;
        }
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
        if(oneCensus2s.size()>0){
            for (OneCensus2 oneCensus21 : oneCensus2s) {
                json2 +=
                        "{\"time\": \"" + oneCensus21.getYeartime() + "-" + oneCensus21.getMonthTime() + "\"," +
                                "\"truckAmmount\": \"" + oneCensus21.getTotal() + "\"" +
                                "},";
            }
            json2 = json2.substring(0, json2.length() - 1);
        }else{
            json2+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }
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
        CostVo2 costVo21 = projectService.NowMonth(costVo2);
        List<OneCensus2> oneCensus2s = projectService.costCensusList(costVo21);
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
        //todo getLoginUser().getId();
        String id = getLoginUser().getId();
        costVo2.setId(id);
        //获取当前月
        CostVo2 costVo21 = projectService.NowMonth(costVo2);
        //根据当前月 求和
        Integer integer = projectService.yearDesCount(costVo21);
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
        //todo getLoginUser().getId();
        String id = getLoginUser().getId();
        costVo2.setId(id);
        //获取当前年的时间
        CostVo2 costVo21 = projectService.NowYear(costVo2);
        //得到当前年的总数
        Integer integer = projectService.yearDesCount(costVo21);
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("count",integer);
        return RestUtil.success(stringIntegerHashMap);
    }
    //查询当前处理人设计
    @RequestMapping(value = "/api/costproject/findCurrentDesign", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findCurrent(){
        List<MkyUser> list =  projectService.findCurrent(getLoginUser().getId());
        return RestUtil.success(list);
    }
    //查询当前处理人造价
    @RequestMapping(value = "/api/costproject/findCurrentCost", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findCurrentCost(){
       List<MkyUser> list =  projectService.findCurrentCost(getLoginUser().getId());
       return RestUtil.success(list);
    }
    //查询设计人下拉

    /*
    * ("SELECT   " +
            "            id  id,   " +
            "            user_name userName " +
            "            FROM mky_user   " +
            "            WHERE   " +
            "            del_flag = '0'   " +
            "            and ( role_id = 'role7618' or role_id = 'role7616' or role_id = 'role7636' or role_id = 'role7637')   " +
            "            and ( job_id = (select job_id from mky_user where id = #{userId} ) or #{userId} = '198910006' or #{userId} = '201803018' )")
    * */
    @RequestMapping(value = "/api/costproject/findDesignAll", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findDesignAll(){
       List<MkyUser> list =  projectService.findDesignAll(getLoginUser().getId());
       return RestUtil.success(list);
    }
    @RequestMapping(value = "/project/affiliationProject", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> affiliationProject(@RequestParam(name = "baseProjectId") String baseId,@RequestParam(name = "district") String district,@RequestParam(name = "designCategory") String designCategory,@RequestParam(name = "designer") String designer){
        projectService.affiliationProject(baseId,district,designCategory,designer);
        return RestUtil.success();
    }




}
