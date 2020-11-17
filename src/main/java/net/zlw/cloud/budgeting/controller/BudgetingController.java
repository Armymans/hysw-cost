package net.zlw.cloud.budgeting.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.budgeting.service.BudgetingService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.cyberneko.html.filters.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/budgeting")
public class BudgetingController extends BaseController {
    @Resource
    private BudgetingService budgetingService;
    private static String founderId;



    @Autowired
    private MemberManageDao memberManageDao;
    @Autowired
    private AuditInfoDao auditInfoDao;

    //添加预算信息
//    @PostMapping("/addBudgeting")
    @RequestMapping(value = "/budgeting/addBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBudgeting(BudgetingVo budgetingVo){
        UserInfo loginUser = getLoginUser();
        budgetingService.addBudgeting(budgetingVo,loginUser);
        return RestUtil.success("添加成功");
    }
    //根据ID查询预算信息
//    @GetMapping("/selectBudgetingById/{id}")
    @RequestMapping(value = "/budgeting/selectBudgetingById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBudgetingById(@RequestParam(name = "id") String id){
        BudgetingVo budgetingVo = budgetingService.selectBudgetingById(id,getLoginUser());
        return RestUtil.success(budgetingVo);
    }
    //编辑预算信息
//    @PutMapping("/updateBudgeting")
    @RequestMapping(value = "/budgeting/updateBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateBudgeting(BudgetingVo budgetingVo){
        budgetingService.updateBudgeting(budgetingVo);
        return RestUtil.success("修改成功");
    }
    //批量审核
//    @PostMapping("/batchReview")
    @RequestMapping(value = "/budgeting/batchReview",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        budgetingService.batchReview(batchReviewVo);
        return RestUtil.success("审核成功");
    }

    //预算到账
    @RequestMapping(value = "/budgeting/intoAccount",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> intoAccount(@RequestParam(name = "ids") String ids){
        budgetingService.intoAccount(ids);
        return RestUtil.success("成功");
    }
    //查询所有
    @RequestMapping(value = "/budgeting/findAllBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllBudgeting(PageBVo pageBVo){
        UserInfo id1 = getLoginUser();
        if (id1!=null){
            founderId = id1.getId();
        }
        if (founderId!=null){
            pageBVo.setFounderId(founderId);
        }
        PageHelper.startPage(pageBVo.getPageNum(),pageBVo.getPageSize());
        List<BudgetingListVo> list = budgetingService.findAllBudgeting(pageBVo);
        //待审核
        List<BudgetingListVo> budgetingListVos = new ArrayList<>();
        //处理中
        ArrayList<BudgetingListVo> budgetingListVos1 = new ArrayList<>();
        //未通过
        ArrayList<BudgetingListVo> budgetingListVos2 = new ArrayList<>();
        //已完成
        ArrayList<BudgetingListVo> budgetingListVos3 = new ArrayList<>();
        for (BudgetingListVo budgetingListVo : list) {
            //待审核
            if (pageBVo.getBudgetingStatus().equals("1")){

                    //根据条件查找当前处理人
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId",budgetingListVo.getId())
                                            .andEqualTo("auditResult","0");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage !=null){
                        budgetingListVo.setCurrentHandler(memberManage.getMemberName());
                    }

                UserInfo loginUser = getLoginUser();
                String id = getLoginUser().getId();



                if ( auditInfo.getAuditResult().equals("0")){
                    if(auditInfo.getAuditorId().equals(id) || budgetingListVo.getFounderId().equals("user309") || budgetingListVo.getFounderId().equals("user308") || budgetingListVo.getFounderId().equals(id)){

                        budgetingListVos.add(budgetingListVo);
                    }
                }
            }
            //处理中
            if (budgetingListVo.getBudgetStatus()!=null && budgetingListVo.getBudgetStatus().equals("处理中")){
                if (budgetingListVo.getFounderId().equals(getLoginUser().getId())){
                    budgetingListVos1.add(budgetingListVo);
                }
            }
            //未通过
            if (budgetingListVo.getAuditResult()!=null && budgetingListVo.getAuditResult().equals("2")){
                if (budgetingListVo.getFounderId().equals(getLoginUser().getId())){
                    budgetingListVos2.add(budgetingListVo);
                }
            }
            //已完成
            if (budgetingListVo.getBudgetStatus() != null && budgetingListVo.getBudgetStatus().equals("已完成")){
                budgetingListVos3.add(budgetingListVo);
            }
        }
        //待审核
        if (pageBVo.getBudgetingStatus().equals("1")){
            PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(budgetingListVos);
            return RestUtil.page(budgetingListVoPageInfo);
        }
//        //处理中
        if (pageBVo.getBudgetingStatus().equals("2")){
            PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(budgetingListVos1);
            return RestUtil.page(budgetingListVoPageInfo);
        }
//        //未通过
        if (pageBVo.getBudgetingStatus().equals("3")){
            PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(budgetingListVos2);
            return RestUtil.page(budgetingListVoPageInfo);
        }
//        //已完成
        if (pageBVo.getBudgetingStatus().equals("4")){
            ArrayList<BudgetingListVo> budgetingListVos4 = new ArrayList<>();
            for (BudgetingListVo budgetingListVo : budgetingListVos3) {
                if (!budgetingListVos4.contains(budgetingListVo)){
                    budgetingListVos4.add(budgetingListVo);
                }
            }
            for (BudgetingListVo budgetingListVo : budgetingListVos4) {
                if (budgetingListVo.getFounderId().equals(getLoginUser().getId()) || budgetingListVo.getFounderId().equals("user309") || budgetingListVo.getFounderId().equals("user308")){
                    budgetingListVo.setShowWhether("1");
                }else{
                    budgetingListVo.setShowWhether("2");
                }
            }
            PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(budgetingListVos4);
            return RestUtil.page(budgetingListVoPageInfo);
        }
        ArrayList<BudgetingListVo> budgetingListVos4 = new ArrayList<>();
        for (BudgetingListVo budgetingListVo : list) {
            if (budgetingListVo.getFounderId().equals(getLoginUser().getId())){
                if (!budgetingListVos4.contains(budgetingListVo)){
                    budgetingListVos4.add(budgetingListVo);
                }
            }
        }
        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(budgetingListVos4);
        return RestUtil.page(budgetingListVoPageInfo);
    }
    //选择项目查看所有预算
    @RequestMapping(value = "/budgeting/findBudgetingAll",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBudgetingAll(PageBVo pageBVo,@RequestParam(name = "sid",required = false) String sid){
        PageHelper.startPage(pageBVo.getPageNum(),pageBVo.getPageSize());
        List<BudgetingListVo> list = budgetingService.findBudgetingAll(pageBVo,sid);
        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(list);
        return net.zlw.cloud.common.RestUtil.page(budgetingListVoPageInfo);
    }
    //预算合并查询
    @RequestMapping(value = "/budgeting/unionQuery",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> unionQuery(@RequestParam(name = "baseId") String id){
       UnionQueryVo unionQueryVo =  budgetingService.unionQuery(id,getLoginUser());
       return RestUtil.success(unionQueryVo);
    }
    //预算编制单条审核
    @RequestMapping(value = "/budgeting/singleAudit",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> singleAudit(SingleAuditVo singleAuditVo){
        budgetingService.singleAudit(singleAuditVo);
        return RestUtil.success();
    }
    //归属
    @RequestMapping(value = "/budgeting/addAttribution",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addAttribution(@RequestParam(name = "baseId") String id,@RequestParam(name = "designCategory") String designCategory,@RequestParam(name = "district") String district){
        budgetingService.addAttribution(id,designCategory,district);
        return RestUtil.success();
    }
    //选择项目查看设计
    @RequestMapping(value = "/budgeting/findDesignAll",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findDesignAll(PageBVo pageBVo){
        PageHelper.startPage(pageBVo.getPageNum(),pageBVo.getPageSize());
       List<DesignInfo> list = budgetingService.findDesignAll(pageBVo);
        PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(list);
        return RestUtil.page(designInfoPageInfo);
    }
    //删除预算
    @RequestMapping(value = "/budgeting/deleteBudgeting",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteBudgeting(@RequestParam(name = "id") String id){
        budgetingService.deleteBudgeting(id);
        return RestUtil.success();
    }
    //新建删除所有文件
    @RequestMapping(value = "/budgeting/deleteBudgetingFile",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteBudgetingFile(@RequestParam(name = "id") String id){
        budgetingService.deleteBudgetingFile(id);
        return RestUtil.success();
    }

}


