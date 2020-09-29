package net.zlw.cloud.designProject.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.Budgeting;
import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.LastSettlementReview;
import net.zlw.cloud.designProject.model.SettlementAuditInformation;
import net.zlw.cloud.designProject.service.ProjectSumService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectSumController extends BaseController {
    @Resource
    private ProjectSumService projectSumService;
    @RequestMapping(value = "/api/projectCount/AllprojectCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer AllprojectCount(){
        Integer integer = projectSumService.AllprojectCount();
        return integer;
    }
    @RequestMapping(value = "/api/projectCount/withAuditCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer withAuditCount(){
        return  projectSumService.withAuditCount();
    }

    @RequestMapping(value = "/api/projectCount/conductCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer conductCount(){
        return  projectSumService.conductCount();
    }

    @RequestMapping(value = "/api/projectCount/completeCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer completeCount(){
        return  projectSumService.completeCount();
    }

    @RequestMapping(value = "/api/projectCount/budgetingList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> budgetingList(CostVo2 costVo2){
//        getLoginUser().getId();
        List<Budgeting> budgetings = projectSumService.budgetingList(costVo2);
        for (Budgeting budgeting : budgetings) {
            if(!"4".equals(budgeting.getDistrict())){
                //预算编制造价咨询金额
                Double money = projectSumService.anhuiBudgetingMoney(budgeting.getAmountCost());
                money = (double)Math.round(money*100)/100;
                budgeting.setBudgetingCost(money);
                //预算编制标底咨询金额
                Double money1 = projectSumService.anhuiBudgetingMoney(budgeting.getBiddingPriceControl());
                money1 = (double)Math.round(money1*100)/100;
                budgeting.setBudgetingStandard(money1);
                //预算编制咨询费计算基数
                Double aDouble = projectSumService.BudgetingBase(money, money1);
                aDouble = (double)Math.round(aDouble*100)/100;
                budgeting.setBudgetingBase(aDouble);
                //预算编制技提
                Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                budgeting.setBudgetingCommission(aDouble1);
            }else{
                //预算编制造价咨询金额
                Double money = projectSumService.wujiangBudgetingMoney(budgeting.getAmountCost());
                money = (double)Math.round(money*100)/100;
                budgeting.setBudgetingCost(money);
                //预算编制标底咨询金额
                Double money1 = projectSumService.wujiangBudgetingMoney(budgeting.getBiddingPriceControl());
                money1 = (double)Math.round(money1*100)/100;
                budgeting.setBudgetingStandard(money1);
                //预算编制咨询费计算基数
                Double aDouble = projectSumService.BudgetingBase(money, money1);
                //如果金额小于3000 则直接返回3000
                if(aDouble<3000){
                    budgeting.setBudgetingBase(3000.00);
                }
                aDouble = (double)Math.round(aDouble*100)/100;
                budgeting.setBudgetingBase(aDouble);
                //预算编制技提
                Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                budgeting.setBudgetingCommission(aDouble1);
            }
        }
        return RestUtil.success(budgetings);
    }

    @RequestMapping(value = "/api/projectCount/LastSettlementReviewChargeList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> LastSettlementReviewChargeList(CostVo2 costVo2){
        List<LastSettlementReview> lastSettlementReviews = projectSumService.lastSettlementReviewChargeList(costVo2);
        for (LastSettlementReview lastSettlementReview : lastSettlementReviews) {
            if(!"4".equals(lastSettlementReview.getDistrict())){
                Double money = projectSumService.anhuiLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                if(money<3000){
                    lastSettlementReview.setReviewNumberCost(3000.00);
                }
                money = (double)Math.round(money*100)/100;
                lastSettlementReview.setReviewNumberCost(money);
                Double aDouble1 = projectSumService.technicalImprovement(money);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                lastSettlementReview.setCommission(aDouble1);
            }else{
                Double money = projectSumService.wujiangLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                money = (double)Math.round(money*100)/100;
                lastSettlementReview.setReviewNumberCost(money);
                Double aDouble1 = projectSumService.technicalImprovement(money);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                lastSettlementReview.setCommission(aDouble1);
            }
        }
        return RestUtil.success(lastSettlementReviews);
    }

    @RequestMapping(value = "/api/projectCount/settlementAuditInformationList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> settlementAuditInformationList(CostVo2 costVo2){
        List<SettlementAuditInformation> settlementAuditInformations = projectSumService.settlementAuditInformationList(costVo2);
        for (SettlementAuditInformation settlementAuditInformation : settlementAuditInformations) {
            if(!"4".equals(settlementAuditInformation.getDistrict())){
                Double money = projectSumService.anhuiSettlementAuditInformationChargeBase(settlementAuditInformation.getSumbitMoney());
                money = (double)Math.round(money*100)/100;
                settlementAuditInformation.setBaseMoney(money);
                Double money1 = projectSumService.anhuiSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                money1 = (double)Math.round(money1*100)/100;
                settlementAuditInformation.setSubtractTheNumberMoney(money1);
            }else{
                Double money = projectSumService.wujiangSettlementAuditInformationChargeBase(settlementAuditInformation.getSumbitMoney());
                money = (double)Math.round(money*100)/100;
                settlementAuditInformation.setBaseMoney(money);
                Double money1 = projectSumService.wujiangSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                money1 = (double)Math.round(money1*100)/100;
                settlementAuditInformation.setSubtractTheNumberMoney(money1);
            }
        }
        return RestUtil.success();
    }
}
