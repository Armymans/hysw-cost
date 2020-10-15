package net.zlw.cloud.statisticAnalysis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectService;
import net.zlw.cloud.designProject.service.ProjectSumService;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.statisticAnalysis.model.*;
import net.zlw.cloud.statisticAnalysis.service.StatusticAnalysisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticAnalysisController {
    @Resource
    private StatusticAnalysisService statusticAnalysisService;
    @Resource
    private ProjectSumService projectSumService;

    //造价绩效统计
    @RequestMapping(value = "/statisticAnalysis/findAnalysis",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAnalysis(pageVo pageVo){
        StatisticAnalysis analysis = statusticAnalysisService.findAnalysis(pageVo);
        return RestUtil.success(analysis);
    }
    //绩效计提汇总
    @RequestMapping(value = "/statisticAnalysis/performanceAccrualAndSummary",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> PerformanceAccrualAndSummary(pageVo pageVo){
       String summary =  statusticAnalysisService.performanceAccrualAndSummary(pageVo);
       return RestUtil.success(JSONArray.parseArray(summary));
    }
    //绩效计提汇总-列表数据
    @RequestMapping(value = "/statisticAnalysis/performanceAccrualAndSummaryList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> performanceAccrualAndSummaryList(pageVo pageVo){
        PerformanceAccrualAndSummaryList performanceAccrualAndSummaryList = statusticAnalysisService.performanceAccrualAndSummaryList(pageVo);
        return RestUtil.success(performanceAccrualAndSummaryList);
    }
    //员工绩效分析
    @RequestMapping(value = "/statisticAnalysis/EmployeePerformanceAnalysis",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
     public Map<String,Object> EmployeePerformanceAnalysis(EmployeeVo employeeVo){
        ReturnEmployeePerformance employeePerformance =  statusticAnalysisService.EmployeePerformanceAnalysis(employeeVo);
       return RestUtil.success(employeePerformance);
    }
    @RequestMapping(value = "/statisticAnalysis/EmployeePerformanceAnalysisPicture",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> EmployeePerformanceAnalysisPicture(){
        Map<String, Object> stringObjectMap = EmployeePerformanceAnalysis(new EmployeeVo("","","","",""));
        ReturnEmployeePerformance data = (ReturnEmployeePerformance)stringObjectMap.get("data");
        JSONArray picture = data.getPicture();
        return RestUtil.success(picture);

    }

    //员工绩效分析列表
    @RequestMapping(value = "/statisticAnalysis/EmployeePerformanceAnalysisList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> EmployeePerformanceAnalysisList(EmployeeVo employeeVo){
       List<OneCensus10> list =  statusticAnalysisService.EmployeePerformanceAnalysisList(employeeVo);
       return RestUtil.success(list);
    }

    //员工绩效统计  预算编制
    @RequestMapping(value = "/statisticAnalysis/budgetingList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> budgetingList(EmployeeVo costVo2){
//        getLoginUser().getId();
        List<Budgeting> budgetings = projectSumService.EmployeeBudgetingList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getMemberId());
        for (Budgeting budgeting : budgetings) {
            incomeInfo.setBaseProjectId(budgeting.getId());
            achievementsInfo.setBaseProjectId(budgeting.getId());
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
                incomeInfo.setBudgetMoney(new BigDecimal(aDouble));
                //预算编制技提
                Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                budgeting.setBudgetingCommission(aDouble1);
                achievementsInfo.setBudgetAchievements(aDouble1);
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
                    incomeInfo.setBudgetMoney(new BigDecimal(3000));
                }
                aDouble = (double)Math.round(aDouble*100)/100;
                budgeting.setBudgetingBase(aDouble);
                incomeInfo.setBudgetMoney(new BigDecimal(aDouble));
                //预算编制技提
                Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                budgeting.setBudgetingCommission(aDouble1);
                achievementsInfo.setBudgetAchievements(aDouble1);
            }
            //将信息保存到表中
            projectSumService.addIncomeInfo(incomeInfo);
            projectSumService.addAchievements(achievementsInfo);
        }
        return RestUtil.success(budgetings);
    }
    //员工统计 上家结算编制
    @RequestMapping(value = "/statisticAnalysis/LastSettlementReviewChargeList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> LastSettlementReviewChargeList(EmployeeVo employeeVo){
        List<LastSettlementReview> lastSettlementReviews = projectSumService.EmployeelastSettlementReviewChargeList(employeeVo);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(employeeVo.getMemberId());
        for (LastSettlementReview lastSettlementReview : lastSettlementReviews) {
            //保存id
            incomeInfo.setBaseProjectId(lastSettlementReview.getId());
            achievementsInfo.setBaseProjectId(lastSettlementReview.getId());
            if(!"4".equals(lastSettlementReview.getDistrict())){
                Double money = projectSumService.anhuiLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                if(money<3000){
                    lastSettlementReview.setReviewNumberCost(3000.00);
                    incomeInfo.setUpsubmitMoney(new BigDecimal(3000));
                }
                //咨询费
                money = (double)Math.round(money*100)/100;
                lastSettlementReview.setReviewNumberCost(money);
                incomeInfo.setUpsubmitMoney(new BigDecimal(money));

                //计提
                Double aDouble1 = projectSumService.technicalImprovement(money);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                lastSettlementReview.setCommission(aDouble1);
                achievementsInfo.setUpsubmitAchievements(aDouble1);
            }else{
                //咨询费
                Double money = projectSumService.wujiangLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                money = (double)Math.round(money*100)/100;
                lastSettlementReview.setReviewNumberCost(money);
                incomeInfo.setUpsubmitMoney(new BigDecimal(money));

                //计提
                Double aDouble1 = projectSumService.technicalImprovement(money);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                lastSettlementReview.setCommission(aDouble1);
                achievementsInfo.setUpsubmitAchievements(aDouble1);
            }
            //将信息保存到表中
            projectSumService.addIncomeInfo(incomeInfo);
            projectSumService.addAchievements(achievementsInfo);
        }
        return RestUtil.success(lastSettlementReviews);
    }
    //员工统计 下家结算编制
    @RequestMapping(value = "/statisticAnalysis/settlementAuditInformationList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> settlementAuditInformationList(EmployeeVo costVo2){
        List<SettlementAuditInformation> settlementAuditInformations = projectSumService.EmployeesettlementAuditInformationList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getMemberId());
        for (SettlementAuditInformation settlementAuditInformation : settlementAuditInformations) {
            incomeInfo.setBaseProjectId(settlementAuditInformation.getId());
            achievementsInfo.setBaseProjectId(settlementAuditInformation.getId());
            if(!"4".equals(settlementAuditInformation.getDistrict())){
                //计算基本费
                Double money = projectSumService.anhuiSettlementAuditInformationChargeBase(settlementAuditInformation.getSumbitMoney());
                money = (double)Math.round(money*100)/100;
                settlementAuditInformation.setBaseMoney(money);
                //计算核检费
                Double money1 = projectSumService.anhuiSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                money1 = (double)Math.round(money1*100)/100;
                settlementAuditInformation.setSubtractTheNumberMoney(money1);
                //计算咨询费计算基数
                Double money2 = projectSumService.anhuiSettlementAuditInformationChargeMoney(money, money1);
                //如果送审金额 小于60000 应收咨询费=基本费+核减费
                if(settlementAuditInformation.getSumbitMoney().compareTo(new BigDecimal("60000"))<1){
                    money2 = (double)Math.round(money2*100)/100;
                    settlementAuditInformation.setReviewNumberCost(money2);
                    incomeInfo.setDownsubmitMoney(new BigDecimal(money2));
                }
                //如果送审金额大于60000 应收咨询费小于一千则收一千
                if(settlementAuditInformation.getSumbitMoney().compareTo(new BigDecimal("60000"))==1){
                    if(money2<1000){
                        settlementAuditInformation.setReviewNumberCost(1000.00);
                        incomeInfo.setDownsubmitMoney(new BigDecimal(1000.00));
                    }
                    money2 = (double)Math.round(money2*100)/100;
                    settlementAuditInformation.setReviewNumberCost(money2);
                    incomeInfo.setDownsubmitMoney(new BigDecimal(money2));
                }
                //计提
                Double aDouble = projectSumService.settlementAuditImprovement(money, money1, money2);
                aDouble = (double)Math.round(aDouble*100)/100;
                settlementAuditInformation.setCommission(aDouble);
                achievementsInfo.setDownsubmitAchievements(aDouble);
            }else{
                //计算基本费
                Double money = projectSumService.wujiangSettlementAuditInformationChargeBase(settlementAuditInformation.getSumbitMoney());
                money = (double)Math.round(money*100)/100;
                settlementAuditInformation.setBaseMoney(money);
                //计算核检费
                Double money1 = projectSumService.wujiangSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                money1 = (double)Math.round(money1*100)/100;
                settlementAuditInformation.setSubtractTheNumberMoney(money1);
                //计算咨询费计算基数
                Double money2 = projectSumService.anhuiSettlementAuditInformationChargeMoney(money, money1);
                //送审金额小于50000 应收咨询费=400+核减费
                if(settlementAuditInformation.getSumbitMoney().compareTo(new BigDecimal("50000"))<1){
                    settlementAuditInformation.setReviewNumberCost(money1+400);
                    incomeInfo.setDownsubmitMoney(new BigDecimal(money1+400));
                }
                //送审金额5万至10万之间，送审金额>50000 应收咨询费=800＋核减费，
                if(settlementAuditInformation.getSumbitMoney().compareTo(new BigDecimal("50000"))==1&&
                        settlementAuditInformation.getSumbitMoney().compareTo(new BigDecimal("100000"))==-1){
                    settlementAuditInformation.setReviewNumberCost(money1+800);
                    incomeInfo.setDownsubmitMoney(new BigDecimal(money1+800));
                }
                //送审金额>100000，应收咨询费=基本费＋核减费
                money2 = (double)Math.round(money2*100)/100;
                settlementAuditInformation.setReviewNumberCost(money2);
                incomeInfo.setDownsubmitMoney(new BigDecimal(money2));
                //计提
                Double aDouble = projectSumService.settlementAuditImprovement(money, money1, money2);
                aDouble = (double)Math.round(aDouble*100)/100;
                settlementAuditInformation.setCommission(aDouble);
                achievementsInfo.setDownsubmitAchievements(aDouble);
            }
            projectSumService.addIncomeInfo(incomeInfo);
            projectSumService.addAchievements(achievementsInfo);
        }
        return RestUtil.success(settlementAuditInformations);
    }

    @RequestMapping(value = "/statisticAnalysis/trackList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> trackList(EmployeeVo costVo2){
        List<Budgeting> budgetings = projectSumService.EmployeetrackList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getMemberId());
        for (Budgeting budgeting : budgetings) {
            incomeInfo.setBaseProjectId(budgeting.getId());
            achievementsInfo.setBaseProjectId(budgeting.getId());
            budgeting.setProjectType("安装");
            BigDecimal amountCost = budgeting.getAmountCost();
            if(!"4".equals(budgeting.getDistrict())){
                Double money = projectSumService.anhuiTrackChargeBaseRate(amountCost);
                if(amountCost.compareTo(new BigDecimal("5000000"))<1){
                    money = (double)Math.round(money*100)/100;
                    budgeting.setFiveHundredCost(money);
                    budgeting.setSubtotal(money);
                    incomeInfo.setTruckMoney(new BigDecimal(money));
                }
                if(amountCost.compareTo(new BigDecimal("10000000"))<1){
                    money = (double)Math.round(money*100)/100;
                    budgeting.setAThousandCost(money);
                    budgeting.setSubtotal(money);
                    incomeInfo.setTruckMoney(new BigDecimal(money));
                }
                if(amountCost.compareTo(new BigDecimal("10000000"))==1&&amountCost.compareTo(new BigDecimal("20000000"))<1){
                    money = (double)Math.round(money*100)/100;
                    budgeting.setTwoThousandCost(money);
                    budgeting.setSubtotal(money);
                    incomeInfo.setTruckMoney(new BigDecimal(money));
                }
                Double aDouble = projectSumService.trackImprovement(budgeting.getSubtotal());
                aDouble = (double)Math.round(aDouble*100)/100;
                budgeting.setBudgetingCommission(aDouble);
                achievementsInfo.setTruckAchievements(aDouble);
            }else{
                Double money = projectSumService.wujiangTrackChargeBaseRate(amountCost);
                if(amountCost.compareTo(new BigDecimal("5000000"))<1){
                    money = (double)Math.round(money*100)/100;
                    budgeting.setFiveHundredCost(money);
                    budgeting.setSubtotal(money);
                    incomeInfo.setTruckMoney(new BigDecimal(money));
                }
                if(amountCost.compareTo(new BigDecimal("10000000"))<1){
                    money = (double)Math.round(money*100)/100;
                    budgeting.setAThousandCost(money);
                    budgeting.setSubtotal(money);
                    incomeInfo.setTruckMoney(new BigDecimal(money));
                }
                if(amountCost.compareTo(new BigDecimal("10000000"))==1&&amountCost.compareTo(new BigDecimal("20000000"))<1){
                    money = (double)Math.round(money*100)/100;
                    budgeting.setTwoThousandCost(money);
                    budgeting.setSubtotal(money);
                    incomeInfo.setTruckMoney(new BigDecimal(money));
                }
                Double aDouble = projectSumService.trackImprovement(budgeting.getSubtotal());
                aDouble = (double)Math.round(aDouble*100)/100;
                budgeting.setBudgetingCommission(aDouble);
                achievementsInfo.setTruckAchievements(aDouble);
            }
            projectSumService.addIncomeInfo(incomeInfo);
            projectSumService.addAchievements(achievementsInfo);
        }
        return RestUtil.success(budgetings);
    }



}
