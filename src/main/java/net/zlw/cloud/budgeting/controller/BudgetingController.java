package net.zlw.cloud.budgeting.controller;

import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.budgeting.model.vo.BudgetingVo;
import net.zlw.cloud.budgeting.service.BudgetingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/budgeting")
public class BudgetingController extends BaseController {
    @Resource
    private BudgetingService budgetingService;

    //添加预算信息
    @PostMapping("/addBudgeting")
    public void addBudgeting(@RequestBody BudgetingVo budgetingVo){
        budgetingService.addBudgeting(budgetingVo,getLoginUser());
    }
    //根据ID查询预算信息
    @GetMapping("/selectBudgetingById/{id}")
    public BudgetingVo selectBudgetingById(@PathVariable(name = "id") String id){
       return budgetingService.selectBudgetingById(id);
    }
    //编辑预算信息
    @PutMapping("/updateBudgeting")
    public String updateBudgeting(@RequestBody BudgetingVo budgetingVo){
        budgetingService.updateBudgeting(budgetingVo);
        return "修改成功";
    }
    //批量审核
    @PostMapping("/batchReview")
    public String batchReview(BatchReviewVo batchReviewVo){
        budgetingService.batchReview(batchReviewVo);
        return "审核成功";
    }


}


