package net.zlw.cloud.budgeting.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.budgeting.service.BudgetingService;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
        budgetingService.updateBudgeting(budgetingVo,getLoginUser());
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

        try {
            budgetingService.intoAccount(ids,getLoginUser().getId());
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
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
        List<BudgetingListVo> list = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
//        PageInfo<BudgetingListVo> list = budgetingService.findAllBudgeting(pageBVo,"user324");
        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(list);


        return RestUtil.page(budgetingListVoPageInfo);
    }

    /**
        * @Author sjf
        * @Description //预算列表 模糊搜索
        * @Date 9:41 2020/11/22
        * @Param
        * @return
     **/
    @RequestMapping(value = "/budgeting/selectByBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectByBudgeting(PageBVo pageBVo){
        //全部
        Page page = new Page();
        pageBVo.setBudgetingStatus("");
        List<BudgetingListVo> allBudgeting = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo = new PageInfo<>(allBudgeting);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());

        //待审核
        Page page1 = new Page();
        pageBVo.setBudgetingStatus("1");
        List<BudgetingListVo> allBudgeting1 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo1 = new PageInfo<>(allBudgeting1);
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());

        //处理中
        Page page2 = new Page();
        pageBVo.setBudgetingStatus("2");
        List<BudgetingListVo> allBudgeting2 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo2 = new PageInfo<>(allBudgeting2);
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());

        //未通过
        Page page3 = new Page();
        pageBVo.setBudgetingStatus("3");
        List<BudgetingListVo> allBudgeting3 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo3 = new PageInfo<>(allBudgeting3);
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());

        //已完成
        Page page4 = new Page();
        pageBVo.setBudgetingStatus("4");
        List<BudgetingListVo> allBudgeting4 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo4 = new PageInfo<>(allBudgeting4);
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);
        map.put("table2",page1);
        map.put("table3",page2);
        map.put("table4",page3);
        map.put("table5",page4);

        return RestUtil.success(map);

    }

    //选择项目查看所有预算
    @RequestMapping(value = "/budgeting/findBudgetingAll",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBudgetingAll(PageBVo pageBVo,@RequestParam(name = "sid",required = false) String sid){

        PageHelper.startPage(pageBVo.getPageNum(),999);
        List<BudgetingListVo> list = budgetingService.findBudgetingAll(pageBVo,sid);
        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(list);
        return RestUtil.page(budgetingListVoPageInfo);
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
    //编辑CEA编号
    @RequestMapping(value = "/budgeting/updateCEA",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateCEA(@RequestParam(name = "baseId") String baseId,@RequestParam(name = "ceaNum") String ceaNum){
        budgetingService.updateCEA(baseId,ceaNum);
        return RestUtil.success();
    }


}


