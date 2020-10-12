package net.zlw.cloud.budgeting.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.budgeting.service.BudgetingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/budgeting")
public class BudgetingController extends BaseController {
    @Resource
    private BudgetingService budgetingService;

    //添加预算信息
//    @PostMapping("/addBudgeting")
    @RequestMapping(value = "/budgeting/addBudgeting",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBudgeting(BudgetingVo budgetingVo){
        budgetingService.addBudgeting(budgetingVo,getLoginUser());
        return RestUtil.success("添加成功");
    }
    //根据ID查询预算信息
//    @GetMapping("/selectBudgetingById/{id}")
    @RequestMapping(value = "/budgeting/selectBudgetingById",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBudgetingById(@RequestParam(name = "id") String id){
        BudgetingVo budgetingVo = budgetingService.selectBudgetingById(id);
        return RestUtil.success(budgetingVo);
    }
    //编辑预算信息
//    @PutMapping("/updateBudgeting")
    @RequestMapping(value = "/budgeting/updateBudgeting",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateBudgeting(BudgetingVo budgetingVo){
        budgetingService.updateBudgeting(budgetingVo);
        return RestUtil.success("修改成功");
    }
    //批量审核
//    @PostMapping("/batchReview")
    @RequestMapping(value = "/budgeting/batchReview",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        budgetingService.batchReview(batchReviewVo);
        return RestUtil.success("审核成功");
    }

    //预算到账
    @RequestMapping(value = "/budgeting/intoAccount",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> intoAccount(@RequestBody String ids){
        budgetingService.intoAccount(ids);
        return RestUtil.success("成功");
    }
    //查询所有
    @RequestMapping(value = "/budgeting/findAllBudgeting",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllBudgeting(PageBVo pageBVo){
        PageInfo<BudgetingVo> allBudgeting = budgetingService.findAllBudgeting(pageBVo);
        return net.zlw.cloud.common.RestUtil.page(allBudgeting);
    }
    //预算合并查询
    @RequestMapping(value = "/budgeting/unionQuery",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> unionQuery(@RequestParam(name = "baseId") String id){
       UnionQueryVo unionQueryVo =  budgetingService.unionQuery(id);
       return RestUtil.success(unionQueryVo);
    }
    //预算编制单条审核
    @RequestMapping(value = "/budgeting/singleAudit",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> singleAudit(SingleAuditVo singleAuditVo){
        budgetingService.singleAudit(singleAuditVo);
        return RestUtil.success();
    }





}


