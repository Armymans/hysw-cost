package net.zlw.cloud.designProject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.mapper.AchievementsInfoMapper;
import net.zlw.cloud.designProject.mapper.IncomeInfoMapper;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectSumService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ProjectSumController extends BaseController {
    @Resource
    private ProjectSumService projectSumService;

    /**
     * 项目统计 项目总数
     * @return
     */
    @RequestMapping(value = "/api/projectCount/AllprojectCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer AllprojectCount(CostVo2 costVo2){
        Integer integer = projectSumService.AllprojectCount(costVo2);
        return integer;
    }

    /**
     * 项目统计 待审核总数
     * @return
     */
    @RequestMapping(value = "/api/projectCount/withAuditCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer withAuditCount(CostVo2 costVo2){
        return  projectSumService.withAuditCount(costVo2);
    }

    /**
     * 进行中的项目
     * @return
     */
    @RequestMapping(value = "/api/projectCount/conductCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer conductCount(CostVo2 costVo2){
        return  projectSumService.conductCount(costVo2);
    }

    /**
     * 已完成项目
     * @return
     */
    @RequestMapping(value = "/api/projectCount/completeCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer completeCount(CostVo2 costVo2){
        return  projectSumService.completeCount(costVo2);
    }

    /**
     * 项目统计 项目数量
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectNumber",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectNumber(CostVo2 costVo2){
        Integer integer = projectSumService.AllprojectCount(costVo2);
        Integer integer1 = projectSumService.withAuditCount(costVo2);
        Integer integer2 = projectSumService.conductCount(costVo2);
        Integer integer3 = projectSumService.completeCount(costVo2);

        ProjectNumber projectNumber = new ProjectNumber();
        projectNumber.setTotal(integer);
        projectNumber.setWithAuditCount(integer1);
        projectNumber.setConductCount(integer2);
        projectNumber.setCompleteCount(integer3);

        return RestUtil.success(projectNumber);
    }

    /**
     * 个人绩效分析 = 预算编制列表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/budgetingList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> budgetingList(CostVo2 costVo2){
//        getLoginUser().getId();
        List<Budgeting> budgetings = projectSumService.budgetingList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getId());
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
                System.err.println(money1);
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

    /***
     * 个人绩效分析 = 上家结算编制
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/LastSettlementReviewChargeList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> LastSettlementReviewChargeList(CostVo2 costVo2){
        List<LastSettlementReview> lastSettlementReviews = projectSumService.lastSettlementReviewChargeList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getId());
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

    /**
     * 下家结算编制
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/settlementAuditInformationList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> settlementAuditInformationList(CostVo2 costVo2){
        List<SettlementAuditInformation> settlementAuditInformations = projectSumService.settlementAuditInformationList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getId());
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

    /**
     * 跟踪审计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/trackList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> trackList(CostVo2 costVo2){
        List<Budgeting> budgetings = projectSumService.trackList(costVo2);
        IncomeInfo incomeInfo = new IncomeInfo();
        AchievementsInfo achievementsInfo = new AchievementsInfo();
        achievementsInfo.setMemberId(costVo2.getId());
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

    /**
     * 企业总收支 总收入
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/totalRevenue",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> totalRevenue(CostVo2 costVo2){
        BigDecimal bigDecimal = projectSumService.totalRevenue(costVo2);
        double value = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 总支出
        BigDecimal bigDecimal1 = projectSumService.totalexpenditure(costVo2);
        double v = bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 总利润
        BigDecimal bigDecimal3 = projectSumService.totalRevenue(costVo2);
        BigDecimal bigDecimal2 = projectSumService.totalexpenditure(costVo2);
        BigDecimal subtract = bigDecimal3.subtract(bigDecimal2);
        double value1 = subtract.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        Expenditure expenditure = new Expenditure();

        expenditure.setTotalRevenue(value);
        expenditure.setTotalexpenditure(v);
        expenditure.setTotalprofit(value1);


        return RestUtil.success(expenditure);
    }
    /**
     * 企业总收支 总支出
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/totalexpenditure",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> totalexpenditure(CostVo2 costVo2){
        BigDecimal bigDecimal = projectSumService.totalexpenditure(costVo2);
        bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return RestUtil.success(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 企业总收支 总利润
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/totalprofit",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>totalprofit(CostVo2 costVo2){
        BigDecimal bigDecimal1 = projectSumService.totalRevenue(costVo2);
        BigDecimal bigDecimal2 = projectSumService.totalexpenditure(costVo2);
        BigDecimal subtract = bigDecimal1.subtract(bigDecimal2);
        return RestUtil.success(subtract.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    }



    /**
     * 项目进度统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectFlow",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>projectFlow(CostVo2 costVo2){
        List<BaseProject> baseProjects = projectSumService.projectFlow(costVo2);
        return RestUtil.success(baseProjects);
    }

    /**
     * 项目统计 项目统计图
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>prjectCensus(CostVo2 costVo2){
        List<CostVo3> costVo3s = projectSumService.prjectCensus(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"项目数量\"," +
                        "\"imageAmmount\": [";
        for (CostVo3 costVo3 : costVo3s) {
            json +=
                    "{\"time\": \""+costVo3.getYearTime()+"-"+costVo3.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+costVo3.getTotal()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 项目统计 本年项目数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensusYear",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>prjectCensusYear(CostVo2 costVo2){
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();

//        本年项目数量
        Integer yearCount = projectSumService.prjectCensusYear(costVo2);
//        本月项目数量
        Integer MonthCount = projectSumService.prjectCensusMonth(costVo2);
//        相比上月
        Integer lastMonthCount = projectSumService.lastPrjectCensusMonth(costVo2);
        Integer MonthRast = projectSumService.prjectCensusRast(MonthCount, lastMonthCount);
//        相比上年
        Integer lastyearCount = projectSumService.lastPrjectCensusMonth(costVo2);
        Integer yearRast = projectSumService.prjectCensusRast(yearCount, lastyearCount);

        map.put("yearRast",yearRast);
        map.put("MonthRast",MonthRast);
        map.put("MonthCount",MonthCount);
        map.put("yearCount",yearCount);
        return RestUtil.success(map);
    }

    /**
     * 项目统计 本月项目数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensusMonth",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>prjectCensusMonth(CostVo2 costVo2){
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
        Integer MonthCount = projectSumService.prjectCensusMonth(costVo2);
        map.put("MonthCount",MonthCount);
        return RestUtil.success(map);
    }

    /**
     * 项目统计 相比上月
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensusMonthRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>prjectCensusMonthRast(CostVo2 costVo2){
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
        Integer MonthCount = projectSumService.prjectCensusMonth(costVo2);
        Integer lastMonthCount = projectSumService.lastPrjectCensusMonth(costVo2);
        Integer MonthRast = projectSumService.prjectCensusRast(MonthCount, lastMonthCount);
        map.put("MonthRast",MonthRast);
        return RestUtil.success(map);
    }

    /**
     * 项目统计 相比上年
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensusYearRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>prjectCensusYearRast(CostVo2 costVo2){
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
        Integer lastyearCount = projectSumService.lastPrjectCensusMonth(costVo2);
        Integer yearCount = projectSumService.prjectCensusYear(costVo2);
        Integer yearRast = projectSumService.prjectCensusRast(yearCount, lastyearCount);
        map.put("yearRast",yearRast);
        return RestUtil.success(map);
    }

    /**
     * 企业收支构成 总收入构成
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectIncomeCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>projectIncomeCensus(CostVo2 costVo2){
        OneCensus oneCensus = projectSumService.projectIncomeCensus(costVo2);
        String josn = "";
        if(oneCensus!=null){
            josn =
                    "[" +
                            "{\"value1\":"+oneCensus.getMunicipalPipeline()+",\"name1\":\"市政管道\"}," +
                            "{\"value1\":"+oneCensus.getNetworkReconstruction()+",name1:\"管网改造\"}," +
                            "{\"value1\":"+oneCensus.getNewCommunity()+",name1:\"新建小区\"}," +
                            "{\"value1\":"+oneCensus.getSecondaryWater()+",name1:\"二次供水类型\"}," +
                            "{\"value1\":"+oneCensus.getCommercialHouseholds()+",name1:\"工商户\"}," +
                            "{\"value1\":"+oneCensus.getWaterResidents()+",name1:\"居民装接水\"}," +
                            "{\"value1\":"+oneCensus.getAdministration()+",name1:\"行政事业\"}" +
                            "]";
        }
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }

    /**
     * 企业收支构成 总支出构成
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectExpenditureCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>projectExpenditureCensus(CostVo2 costVo2){
        OneCensus oneCensus = projectSumService.projectExpenditureCensus(costVo2);
        String josn = "";
        if(oneCensus!=null){
            josn =
                    "[" +
                            "{\"value1\":"+oneCensus.getMunicipalPipeline()+",\"name1\":\"市政管道\"}," +
                            "{\"value1\":"+oneCensus.getNetworkReconstruction()+",name1:\"管网改造\"}," +
                            "{\"value1\":"+oneCensus.getNewCommunity()+",name1:\"新建小区\"}," +
                            "{\"value1\":"+oneCensus.getSecondaryWater()+",name1:\"二次供水类型\"}," +
                            "{\"value1\":"+oneCensus.getCommercialHouseholds()+",name1:\"工商户\"}," +
                            "{\"value1\":"+oneCensus.getWaterResidents()+",name1:\"居民装接水\"}," +
                            "{\"value1\":"+oneCensus.getAdministration()+",name1:\"行政事业\"}" +
                            "]";
        }
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }


    @RequestMapping(value = "/api/projectCount/selectProjectExpenditureCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>selectProjectExpenditureCensus(CostVo2 costVo2){
        // 总收入构成
        OneCensus oneCensus = projectSumService.projectIncomeCensus(costVo2);
        String josn = "";
        if(oneCensus!=null){
            josn =
                    "[" +
                            "{\"value1\":"+oneCensus.getMunicipalPipeline()+",\"name1\":\"市政管道\"}," +
                            "{\"value1\":"+oneCensus.getNetworkReconstruction()+",name1:\"管网改造\"}," +
                            "{\"value1\":"+oneCensus.getNewCommunity()+",name1:\"新建小区\"}," +
                            "{\"value1\":"+oneCensus.getSecondaryWater()+",name1:\"二次供水类型\"}," +
                            "{\"value1\":"+oneCensus.getCommercialHouseholds()+",name1:\"工商户\"}," +
                            "{\"value1\":"+oneCensus.getWaterResidents()+",name1:\"居民装接水\"}," +
                            "{\"value1\":"+oneCensus.getAdministration()+",name1:\"行政事业\"}" +
                            "]";
        }
        JSONArray objects = JSON.parseArray(josn);
        // 总支出构成
        OneCensus oneCensus1 = projectSumService.projectExpenditureCensus(costVo2);
        String josn1 = "";
        if(oneCensus!=null){
            josn1 =
                    "[" +
                            "{\"value1\":"+oneCensus1.getMunicipalPipeline()+",\"name1\":\"市政管道\"}," +
                            "{\"value1\":"+oneCensus1.getNetworkReconstruction()+",name1:\"管网改造\"}," +
                            "{\"value1\":"+oneCensus1.getNewCommunity()+",name1:\"新建小区\"}," +
                            "{\"value1\":"+oneCensus1.getSecondaryWater()+",name1:\"二次供水类型\"}," +
                            "{\"value1\":"+oneCensus1.getCommercialHouseholds()+",name1:\"工商户\"}," +
                            "{\"value1\":"+oneCensus1.getWaterResidents()+",name1:\"居民装接水\"}," +
                            "{\"value1\":"+oneCensus1.getAdministration()+",name1:\"行政事业\"}" +
                            "]";
        }
        JSONArray objects1 = JSON.parseArray(josn1);

        HashMap<String, Object> map = new HashMap<>();
        map.put("data11",objects);
        map.put("data12",objects1);

        return RestUtil.success(map);
    }

    /**
     * 企业收支统计 支出条形统计表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/expenditureAnalysis",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>expenditureAnalysis(CostVo2 costVo2){
        List<OneCensus3> oneCensus3s = projectSumService.expenditureAnalysis(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"员工绩效\"," +
                        "\"imageAmmount\": [";
        for (OneCensus3 oneCensus3 : oneCensus3s) {
            json +=
                    "{\"time\": \""+oneCensus3.getYearTime()+"-"+oneCensus3.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus3.getAdvMoney()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);

        json +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"委外支出\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus3 oneCensus3 : oneCensus3s) {
            json += "{\"time\": \""+oneCensus3.getYearTime()+"-"+oneCensus3.getMonthTime()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus3.getOutMoney()+"\"},";
        }
        json = json.substring(0,json.length() -1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 企业收支统计 项目收支情况
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/BaseProjectExpenditureList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> BaseProjectExpenditureList(CostVo2 costVo2){
    public Map<String,Object> BaseProjectExpenditureList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.BaseProjectExpenditureList(costVo2);
        return RestUtil.page(baseProjectPageInfo);
//        return RestUtil.success(baseProjectPageInfo.getList());
    }

    /**
     * 项目统计 项目信息统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/BaseProjectInfoCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> BaseProjectInfoCensus(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.BaseProjectInfoCensus(costVo2);
        return RestUtil.page(baseProjectPageInfo);
    }

    /**
     * 设计项目个数
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/designCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> designCount(CostVo2 costVo2){
        Integer designInfoCount = projectSumService.designInfoCount(costVo2);
        Integer designChangeInfoCount = projectSumService.designChangeInfoCount(costVo2);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("designInfoCount",designInfoCount);
        map.put("designChangeInfoCount",designChangeInfoCount);
        return RestUtil.success(map);
    }

    /**
     *设计变更分析
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/designCountCensus",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> designCountCensus(CostVo2 costVo2){
        Integer designInfoCount = projectSumService.designInfoCount(costVo2);
        Integer designChangeInfoCount = projectSumService.designChangeInfoCount(costVo2);
        String josn =
                "[" +
                        "{\"value1\":"+designInfoCount+",\"name1\":\"未变更项目\"}," +
                        "{\"value1\":"+designChangeInfoCount+",name1:\"变更项目'\"}," +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }

    /**
     *设计变更统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectDesignChangeList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectDesignChangeList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.projectDesignChangeList(costVo2);
        return RestUtil.page(baseProjectPageInfo);
    }

    /**
     * 进度款支付分析
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/progressPaymentList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> progressPaymentList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.progressPaymentList(costVo2);
        return RestUtil.page(baseProjectPageInfo);
    }

    /**
     * 进度款支付信息 个数综合
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/progressPaymentCountAndSum",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> progressPaymentCountAndSum(CostVo2 costVo2){
        Integer count = projectSumService.progressPaymentCount(costVo2);
        Double sum = projectSumService.progressPaymentSum(costVo2);
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("count",count);
        map.put("sum",sum);
        return RestUtil.success(map);
    }

    /**
     * 签证变更统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectVisaChangeList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectVisaChangeList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.projectVisaChangeList(costVo2);
        return RestUtil.page(baseProjectPageInfo);
    }

    /**
     * 签证变更个数统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/VisaChangeMoneyAndCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> VisaChangeMoneyAndCount(CostVo2 costVo2){
        Double VisaChangeCount = projectSumService.VisaChangeCount(costVo2);
        Double VisaChangeMoney = projectSumService.VisaChangeMoney(costVo2);
        ConcurrentHashMap<String, Double> map = new ConcurrentHashMap<>();
        map.put("VisaChangeCount",VisaChangeCount);
        map.put("VisaChangeMoney",VisaChangeMoney);
        return RestUtil.success(map);
    }

    /**
     * 项目结算分析
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectSettlementCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectSettlementCensus(CostVo2 costVo2){
        List<OneCensus4> oneCensus4s = projectSumService.projectSettlementCensus(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"上家结算送审\"," +
                        "\"imageAmmount\": [";
        for (OneCensus4 oneCensus4 : oneCensus4s) {
            json +=
                    "{\"time\": \""+oneCensus4.getYearTime()+"-"+oneCensus4.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus4.getReviewNumber()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);

        json +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"下家结算送审\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus4 oneCensus4 : oneCensus4s){
            json +=
                    "{\"time\": \""+oneCensus4.getYearTime()+"-"+oneCensus4.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus4.getSumbitMoney()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);

        json +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"下家结算审核\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus4 oneCensus4 : oneCensus4s){
            json +=
                    "{\"time\": \""+oneCensus4.getYearTime()+"-"+oneCensus4.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus4.getAuthorizedNumber()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);

        json +=
                "]" +
                        "}, {" +
                         "\"companyName\":\"下家结算核减\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus4 oneCensus4 : oneCensus4s){
            json +=
                    "{\"time\": \""+oneCensus4.getYearTime()+"-"+oneCensus4.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus4.getSubtractTheNumber()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 项目结算分析 个数展示
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectSettlementCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectSettlementCount(CostVo2 costVo2){
        OneCensus4 oneCensus4 = projectSumService.projectSettlementCount(costVo2);
        return RestUtil.success(oneCensus4);
    }
    /**
     * 设计部门任务进度统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectSettlementSum",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectSettlementSum(CostVo2 costVo2){
        OneCensus4 oneCensus4 = projectSumService.projectSettlementSum(costVo2);
        return RestUtil.success(oneCensus4);
    }

    /**
     * 设计部门统计 全局版  设计任务数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectDesginCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectDesginCount(CostVo2 costVo2){
        OneCensus3 oneCensus3 = projectSumService.projectDesginCount(costVo2);
        return RestUtil.success(oneCensus3);
    }

    /***
     * 设计任务统计 -未知
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectDesginStatus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectDesginStatus(CostVo2 costVo2){
        List<OneCensus3> oneCensus3s = projectSumService.projectDesginStatus(costVo2);
        return RestUtil.success(oneCensus3s);
    }

    /***
     * 设计任务统计 -设计任务统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/censusList2",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> censusList2(CostVo2 costVo2){
        //todo getLoginUser().getId()
        List<OneCensus> oneCensuses = projectSumService.censusList2(costVo2);
        String censusList = "[{\"companyName\":\"市政管道\"," +
                "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList +=
                    "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus.getMunicipalPipeline()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"管网改造\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus.getNetworkReconstruction()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"新建小区\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus oneCensus : oneCensuses) {
            censusList +=
                    "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                            "\"truckAmmount\": \"" + oneCensus.getNewCommunity()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"二次供水改造项目\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus oneCensus : oneCensuses) {
            censusList +=
                    "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                            "\"truckAmmount\": \"" + oneCensus.getSecondaryWater()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"工商户\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus.getCommercialHouseholds()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"居民装接水\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus.getWaterResidents()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"行政事业\"," +
                        "\"imageAmmount\": [";
        for (OneCensus oneCensus : oneCensuses) {
            censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus.getAdministration()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=  "]}]";
        JSONArray json = JSON.parseArray(censusList);
        return RestUtil.showJsonSuccess(json);
    }

    /**
     * 本月任务数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/censusList2MonthCount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> censusList2MonthCount(CostVo2 costVo2){
//        本月任务数量
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        Integer month = projectSumService.censusList2MonthCount(costVo2);

//        本年任务数量

        Integer year = projectSumService.censusList2YearCount(costVo2);
//        同比上年
        Integer lastYear = projectSumService.censusList2LastYearCount(costVo2);
        Integer yearRast = projectSumService.prjectCensusRast(year, lastYear);
//        同比上月
        Integer lastmonth = projectSumService.censusList2lastMonthCount(costVo2);
        Integer monthRast = projectSumService.prjectCensusRast(month, lastmonth);
        map.put("monthRast",monthRast);
        map.put("yearRast",yearRast);
        map.put("year",year);
        map.put("month",month);
        return RestUtil.success(map);
    }

//    /**
//     * 本年任务数量
//     * @param costVo2
//     * @return
//     */
//    @RequestMapping(value = "/api/projectCount/censusList2YearCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> censusList2YearCount(CostVo2 costVo2){
//        Integer year = projectSumService.censusList2YearCount(costVo2);
//        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
//        map.put("year",year);
//        return RestUtil.success(map);
//    }
//
//    /**
//     * 同比上年
//     * @param costVo2
//     * @return
//     */
//    @RequestMapping(value = "/api/projectCount/censusList2YearRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> censusList2YearRast(CostVo2 costVo2){
//        Integer year = projectSumService.censusList2YearCount(costVo2);
//        Integer lastYear = projectSumService.censusList2LastYearCount(costVo2);
//        Integer yearRast = projectSumService.prjectCensusRast(year, lastYear);
//        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
//        map.put("yearRast",yearRast);
//        return RestUtil.success(map);
//    }
//
//    /**
//     * 同比上月
//     * @param costVo2
//     * @return
//     */
//    @RequestMapping(value = "/api/projectCount/censusList2MonthRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> censusList2MonthRast(CostVo2 costVo2){
//        Integer month = projectSumService.censusList2MonthCount(costVo2);
//        Integer lastmonth = projectSumService.censusList2lastMonthCount(costVo2);
//        Integer monthRast = projectSumService.prjectCensusRast(month, lastmonth);
//        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
//        map.put("monthRast",monthRast);
//        return RestUtil.success(map);
//    }

    /**
     * 设计任务统计 -设计任务总汇1
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginMoneyCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginMoneyCensus(CostVo2 costVo2){
        OneCensus5 oneCensus5 = projectSumService.desiginMoneyCensus(costVo2);
        Integer amount = oneCensus5.getAnhuiAnount()+oneCensus5.getWujiangAmount();
        Integer notamount = oneCensus5.getNotAmount() - amount;
        String josn =
                "[" +
                        "{\"value1\":"+amount+",\"name1\":\"已到账数目\"}," +
                        "{\"value1\":"+notamount+",name1:\"未到账数目'\"}," +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }

    /**
     * 设计任务统计 -设计任务总汇1 数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginMoneyCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginMoneyCount(CostVo2 costVo2){
        OneCensus5 oneCensus5 = projectSumService.desiginMoneyCensus(costVo2);
        Integer amount = oneCensus5.getAnhuiAnount()+oneCensus5.getWujiangAmount();
        Integer notamount = oneCensus5.getNotAmount() - amount;
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("amount",amount);
        map.put("notamount",notamount);
        return RestUtil.success(map);
    }
    /**
     * 设计任务统计 -设计任务总汇2
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginoutsource",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginoutsource(CostVo2 costVo2){
        OneCensus5 oneCensus5 = projectSumService.desiginoutsource(costVo2);
        Integer outsourceno = oneCensus5.getOutsourceNo();
        Integer outsourceyes = oneCensus5.getOutsourceYes();
        String josn =
                "[" +
                        "{\"value1\":"+outsourceno+",\"name1\":\"内部设计\"}," +
                        "{\"value1\":"+outsourceyes+",name1:\"委外设计'\"}," +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }

    /**
     * 设计任务统计 -设计任务总汇2 数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginoutsourceCount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginoutsourceCount(CostVo2 costVo2){
        OneCensus5 oneCensus5 = projectSumService.desiginoutsource(costVo2);
        Integer outsourceno = oneCensus5.getOutsourceNo();
        Integer outsourceyes = oneCensus5.getOutsourceYes();
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("amount",outsourceno);
        map.put("notamount",outsourceyes);
        return RestUtil.success(map);
    }

    @RequestMapping(value = "/api/projectCount/desiginoutSourceAndCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginoutSourceAndCensus(CostVo2 costVo2){
        OneCensus5 oneCensus5 = projectSumService.desiginMoneyCensus(costVo2);
        Integer amount = oneCensus5.getAnhuiAnount()+oneCensus5.getWujiangAmount();
        Integer notamount = oneCensus5.getNotAmount() - amount;
        String josn =
                "[" +
                        "{\"value1\":"+amount+",\"name1\":\"已到账数目\"}," +
                        "{\"value1\":"+notamount+",name1:\"未到账数目'\"}," +
                        "]";
        JSONArray objects = JSON.parseArray(josn);

        OneCensus5 oneCensus51 = projectSumService.desiginoutsource(costVo2);
        Integer outsourceno = oneCensus51.getOutsourceNo();
        Integer outsourceyes = oneCensus51.getOutsourceYes();
        String josn1 =
                "[" +
                        "{\"value1\":"+outsourceno+",\"name1\":\"内部设计\"}," +
                        "{\"value1\":"+outsourceyes+",name1:\"委外设计'\"}," +
                        "]";
        JSONArray objects1 = JSON.parseArray(josn);
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("objects",objects);
        map.put("objects1",objects1);
        return RestUtil.success(map);
    }


    /**
     * 设计任务统计 -设计任务分析
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desginCensusList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desginCensusList(CostVo2 costVo2){
        PageInfo<DesignInfo> designInfoPageInfo = projectSumService.desginCensusList(costVo2);
        return RestUtil.page(designInfoPageInfo);
    }

    /**
     * 根据设计人查找列表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desginCensusListByDesigner",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desginCensusListByDesigner(CostVo2 costVo2){
        PageInfo<DesignInfo> designInfoPageInfo = projectSumService.desginCensusList(costVo2);
        return RestUtil.success(designInfoPageInfo.getList());
    }

    /**
     * 设计绩效统计图
     * @param costVo2
     * @return
     */
        @RequestMapping(value = "/api/projectCount/desiginAchievementsCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsCensus(CostVo2 costVo2){
        List<OneCensus6> oneCensus6s = projectSumService.desiginAchievementsCensus(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"设计绩效\"," +
                        "\"imageAmmount\": [";
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            json +=
                    "{\"time\": \""+oneCensus6.getYearTime()+"-"+oneCensus6.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus6.getDesginAchievements()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 设计绩效 发放总数
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievements",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievements(CostVo2 costVo2){
        BigDecimal bigDecimal1 = projectSumService.desiginMonthAchievements(costVo2);
        BigDecimal bigDecimal2 = projectSumService.desiginYearAchievements(costVo2);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("monthAchievements",bigDecimal1);
        map.put("yearAchievements",bigDecimal2);
        return RestUtil.success(map);
    }

//    /**
//     * 设计绩效 本年发放总数
//     * @param costVo2
//     * @return
//     */
//    @RequestMapping(value = "/api/projectCount/desiginYearAchievements",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> desiginYearAchievements(CostVo2 costVo2){
//        BigDecimal bigDecimal2 = projectSumService.desiginYearAchievements(costVo2);
//        return RestUtil.success(bigDecimal2);
//    }

    /**
     * 设计同比上个月比例
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginLastMonthAchievementsRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginLastMonthAchievementsRast(CostVo2 costVo2){
        BigDecimal month = projectSumService.desiginMonthAchievements(costVo2);
        BigDecimal lastmonth = projectSumService.desiginLastMonthAchievements(costVo2);
        BigDecimal bigDecimal = projectSumService.desiginCensusRast(month, lastmonth);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("monthAchievementsRast",bigDecimal);
        return RestUtil.success(map);
    }

    /**
     * 设计同比上一年比例
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginLastYearAchievementsRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginLastYearAchievementsRast(CostVo2 costVo2){
        BigDecimal year = projectSumService.desiginYearAchievements(costVo2);
        BigDecimal lastyear = projectSumService.desiginLastYearAchievements(costVo2);
        BigDecimal bigDecimal = projectSumService.desiginCensusRast(year, lastyear);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("yearAchievementsRast",bigDecimal);
        return RestUtil.success(map);
    }

    /**
     * 绩效计提总汇
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginProvisionCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginProvisionCensus(CostVo2 costVo2){
        List<OneCensus6> oneCensus6s = projectSumService.desiginAchievementsCensus(costVo2);

        String censusList = "[{\"companyName\":\"应计提金额\"," +
                "\"imageAmmount\": [";
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            censusList +=
                    "{\"time\": \"" + oneCensus6.getYearTime() + "-" + oneCensus6.getMonthTime() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus6.getDesginAchievements()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"建议计提金额\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            censusList += "{\"time\": \""+oneCensus6.getYearTime()+"-"+oneCensus6.getMonthTime()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus6.getDesginAchievements().multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP)+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList += "]}]";
        JSONArray objects = JSON.parseArray(censusList);
        return RestUtil.success(objects);
    }

    /**
     * 个人月度计提统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievementsOneCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsOneCensus(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){
        }else{
            costVo2.setId("user282");
        }
        List<OneCensus6> oneCensus6s = projectSumService.desiginAchievementsOneCensus(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"月度绩效\"," +
                        "\"imageAmmount\": [";
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            json +=
                    "{\"time\": \""+oneCensus6.getYearTime()+"-"+oneCensus6.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus6.getDesginAchievements()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 本月发放的绩效
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievementsOneCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsOneCount(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){
        }else{
            costVo2.setId("user282");
        }
        BigDecimal bigDecimal = projectSumService.desiginAchievementsOneCount(costVo2);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("monthAchievements",bigDecimal);
        return RestUtil.success(map);
    }

    /**
     * 同比上个月绩效统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievementsOneCountRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsOneCountRast(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){
        }else{
            costVo2.setId("user282");
        }
        BigDecimal mouth = projectSumService.desiginAchievementsOneCount(costVo2);
        BigDecimal lastmouth = projectSumService.desiginLastAchievementsOneCount(costVo2);
        BigDecimal bigDecimal = projectSumService.desiginCensusRast(mouth, lastmouth);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("monthAchievementsRast",bigDecimal);
        return RestUtil.success(map);
    }

    /**
     * 年度绩效计提统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievementsOneCensus2",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsOneCensus2(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){
        }else{
            costVo2.setId("user282");
        }
        List<OneCensus6> oneCensus6s = projectSumService.desiginAchievementsOneCensus2(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"年度绩效\"," +
                        "\"imageAmmount\": [";
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            json +=
                    "{\"time\": \""+oneCensus6.getYearTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus6.getDesginAchievements()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 本年发布的绩效
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievementsOneCount2",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsOneCount2(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){
        }else{
            costVo2.setId("user282");
        }
        BigDecimal bigDecimal = projectSumService.desiginAchievementsOneCount2(costVo2);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("yearAchievements",bigDecimal);
        return RestUtil.success(map);
    }

    /**
     * 同比上年的绩效
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/desiginAchievementsOneCountRast2",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginAchievementsOneCountRast2(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){
        }else{
            costVo2.setId("user282");
        }
        BigDecimal mouth = projectSumService.desiginAchievementsOneCount2(costVo2);
        BigDecimal lastmouth = projectSumService.desiginLastAchievementsOneCount2(costVo2);
        BigDecimal bigDecimal = projectSumService.desiginCensusRast(mouth, lastmouth);
        ConcurrentHashMap<String, BigDecimal> map = new ConcurrentHashMap<>();
        map.put("yearAchievementsRast",bigDecimal);
        return RestUtil.success(map);
    }

    /**
     * 造价任务数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskTotalCount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskTotalCount(CostVo2 costVo2){
        Integer costTaskTotal = projectSumService.costTaskTotal(costVo2);
        Integer costTaskReviewed = projectSumService.costTaskReviewed(costVo2);
        Integer costTaskHandle = projectSumService.costTaskHandle(costVo2);
        Integer costTaskComple = projectSumService.costTaskComple(costVo2);

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("costTaskTotal",costTaskTotal);
        map.put("costTaskReviewed",costTaskReviewed);
        map.put("costTaskHandle",costTaskHandle);
        map.put("costTaskComple",costTaskComple);
        return RestUtil.success(map);
    }

    /**
     * 造价任务统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskCensus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskCensus(CostVo2 costVo2){
        List<OneCensus6> oneCensus6s = projectSumService.costTaskCensus(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"造价任务\"," +
                        "\"imageAmmount\": [";
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            json +=
                    "{\"time\": \""+oneCensus6.getYearTime()+"-"+oneCensus6.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus6.getTotal()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 本月任务数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskMonthTotal",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskMonthTotal(CostVo2 costVo2){
        Integer integer = projectSumService.costTaskMonthTotal(costVo2);
        return RestUtil.success(integer);
    }

    /**
     * 本年任务数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskYearTotal",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskYearTotal(CostVo2 costVo2){
        Integer integer = projectSumService.costTaskYearTotal(costVo2);
        return RestUtil.success(integer);
    }

    /**
     *同比上年
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskYearTotalRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskYearTotalRast(CostVo2 costVo2){
        Integer costTaskYearTotal = projectSumService.costTaskYearTotal(costVo2);
        Integer costTaskLastYearTotal = projectSumService.costTaskLastYearTotal(costVo2);
        Integer integer = projectSumService.prjectCensusRast(costTaskYearTotal, costTaskLastYearTotal);
        return RestUtil.success(integer);
    }

    /**
     * 同比上月
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskMonthTotalRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskMonthTotalRast(CostVo2 costVo2){
        Integer costTaskMonthTotal = projectSumService.costTaskMonthTotal(costVo2);
        Integer costTaskLastMonthTotal = projectSumService.costTaskLastMonthTotal(costVo2);
        Integer integer = projectSumService.prjectCensusRast(costTaskMonthTotal, costTaskLastMonthTotal);
        return RestUtil.success(integer);
    }

    /**
     * 造价任务汇总统计图
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskSummary",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskSummary(CostVo2 costVo2){
        OneCensus7 oneCensus7 = projectSumService.costTaskSummary(costVo2);
        String josn =
                "[" +
                        "{\"value1\":"+oneCensus7.getBudgeting()+",\"name1\":\"预算编制\"}," +
                        "{\"value1\":"+oneCensus7.getLastSettlementReview()+",name1:\"上家结算编制'\"}," +
                        "{\"value1\":"+oneCensus7.getSettlementAuditInformation()+",name1:\"下家结算审核'\"}," +
                        "{\"value1\":"+oneCensus7.getTrackAuditInfo()+",name1:\"跟踪审计'\"}," +
                        "{\"value1\":"+oneCensus7.getVisaChangeInformation()+",name1:\"签证/变更''\"}," +
                        "{\"value1\":"+oneCensus7.getProgressPaymentInformation()+",name1:\"进度款支付'\"}" +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }

    /**
     * 造价任务汇总统计图搜索
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/selectCostTaskSummary",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectCostTaskSummary(CostVo2 costVo2){
        OneCensus7 oneCensus7 = projectSumService.costTaskSummary(costVo2);
        String josn =
                "[" +
                        "{\"value1\":"+oneCensus7.getBudgeting()+",\"name1\":\"预算编制\"}," +
                        "{\"value1\":"+oneCensus7.getLastSettlementReview()+",name1:\"上家结算编制'\"}," +
                        "{\"value1\":"+oneCensus7.getSettlementAuditInformation()+",name1:\"下家结算审核'\"}," +
                        "{\"value1\":"+oneCensus7.getTrackAuditInfo()+",name1:\"跟踪审计'\"}," +
                        "{\"value1\":"+oneCensus7.getVisaChangeInformation()+",name1:\"签证/变更''\"}," +
                        "{\"value1\":"+oneCensus7.getProgressPaymentInformation()+",name1:\"进度款支付'\"}" +
                        "]";
        JSONArray objects = JSON.parseArray(josn);


        // 是否委外
        Integer costTaskOutsourcingCount = projectSumService.costTaskOutsourcingCount(costVo2);
        Integer costTaskNoOutsourcingCount = projectSumService.costTaskNoOutsourcingCount(costVo2);
        String josn1 =
                "[" +
                        "{\"value1\":"+costTaskOutsourcingCount+",\"name1\":\"委外\"}," +
                        "{\"value1\":"+costTaskNoOutsourcingCount+",name1:\"内部'\"}" +
                        "]";
        JSONArray objects1 = JSON.parseArray(josn1);

        OneCensus7 oneCensus = projectSumService.costTaskSummary(costVo2);

        ConcurrentHashMap<String, Integer> map1 = new ConcurrentHashMap<>();
        map1.put("costTaskOutsourcingCount",costTaskOutsourcingCount);
        map1.put("costTaskNoOutsourcingCount",costTaskNoOutsourcingCount);

        HashMap<String, Object> map = new HashMap<>();
        map.put("data11",objects);
        map.put("data12",objects1);
        map.put("data13",oneCensus);
        map.put("data14",map1);

        return RestUtil.success(map);

    }


    /**
     * 造价任务汇总个数
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskSummaryCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskSummaryCount(CostVo2 costVo2){
        OneCensus7 oneCensus7 = projectSumService.costTaskSummary(costVo2);
        return RestUtil.success(oneCensus7);
    }

    /**
     * 造价是否委外统计图
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskOutsourcingCountCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskOutsourcingCountCensus(CostVo2 costVo2){
        Integer costTaskOutsourcingCount = projectSumService.costTaskOutsourcingCount(costVo2);
        Integer costTaskNoOutsourcingCount = projectSumService.costTaskNoOutsourcingCount(costVo2);
        String josn =
                "[" +
                        "{\"value1\":"+costTaskOutsourcingCount+",\"name1\":\"委外\"}," +
                        "{\"value1\":"+costTaskNoOutsourcingCount+",name1:\"内部'\"}" +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }

    /**
     * 造价委外数
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskOutsourcingCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costTaskOutsourcingCount(CostVo2 costVo2){
        Integer costTaskOutsourcingCount = projectSumService.costTaskOutsourcingCount(costVo2);
        Integer costTaskNoOutsourcingCount = projectSumService.costTaskNoOutsourcingCount(costVo2);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("costTaskOutsourcingCount",costTaskOutsourcingCount);
        map.put("costTaskNoOutsourcingCount",costTaskNoOutsourcingCount);
        return RestUtil.success(map);
    }

    @RequestMapping(value = "/api/projectCount/DesginAchievementsList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> DesginAchievementsList(CostVo2 costVo2){
        List<OneCensus8> oneCensus8s = projectSumService.DesginAchievementsList(costVo2);
        Double total1 = 0.0;
        Double total2 = 0.0;
        Double total3 = 0.0;
        for (OneCensus8 oneCensus8 : oneCensus8s) {
            total1 += oneCensus8.getDesginAchievements();
            total2 += oneCensus8.getDesginAchievements2();
            total3 += oneCensus8.getBalance();
        }
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("DesginAchievementsList",oneCensus8s);
        map.put("total1",total1);
        map.put("total2",total2);
        map.put("total3",total3);
        return RestUtil.success(map);
    }

    /**
     * 月度绩效计提统计列表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/DesginMonthAchievementsList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> DesginMonthAchievementsList(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){

        }else{
            costVo2.setId("1");
        }
        PageInfo<OneCensus9> oneCensus9PageInfo = projectSumService.DesginMonthAchievementsList(costVo2);
        return RestUtil.success(oneCensus9PageInfo);
    }

    /**
     * 设计年度绩效表列表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/DesginYearAchievementsList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> DesginYearAchievementsList(CostVo2 costVo2){
        if(costVo2.getId()!=null&&!"".equals(costVo2.getId())){

        }else{
            costVo2.setId("1");
        }
        PageInfo<OneCensus9> oneCensus9PageInfo = projectSumService.DesginYearAchievementsList(costVo2);
        return RestUtil.success(oneCensus9PageInfo);
    }

    /**
     * 造价任务统计列表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/costTaskCensusList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>CostTaskCensusList(CostVo2 costVo2){
        List<OneCensus10> oneCensus10s = projectSumService.costTaskCensusList(costVo2);
        return RestUtil.success(oneCensus10s);
    }

    /**
     * 员工绩效发放统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/MemberAchievementsCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>MemberAchievementsCensus(CostVo2 costVo2){
        List<OneCensus2> oneCensus2s = projectSumService.memberAchievementsCensus(costVo2);
        String json =
                "[{" +
                        "\"companyName\": \"绩效发放数据\"," +
                        "\"imageAmmount\": [";
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            json +=
                    "{\"time\": \""+oneCensus2.getYeartime()+"-"+oneCensus2.getMonthTime()+"\"," +
                            "\"truckAmmount\": \""+oneCensus2.getTotal()+"\"" +
                            "},";
        }
        json = json.substring(0,json.length()-1);
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return RestUtil.success(objects);
    }

    /**
     * 本年员工绩效发放
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/memberAchievementsYearCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> memberAchievementsYearCount(CostVo2 costVo2){
        BigDecimal bigDecimal = projectSumService.memberAchievementsYearCount(costVo2);
        return RestUtil.success(bigDecimal);
    }

    /**
     * 本年员工绩效同比上年
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/memberAchievementsYearRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> memberAchievementsYearRast(CostVo2 costVo2){
        BigDecimal memberAchievementsYearCount = projectSumService.memberAchievementsYearCount(costVo2);
        BigDecimal memberAchievementsLastYearCount = projectSumService.memberAchievementsLastYearCount(costVo2);
        BigDecimal bigDecimal = projectSumService.desiginCensusRast(memberAchievementsYearCount, memberAchievementsLastYearCount);
        return RestUtil.success(bigDecimal);
    }

    /**
     * 本月员工绩效发放
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/memberAchievementsMonthCount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>memberAchievementsMonthCount(CostVo2 costVo2){
        BigDecimal bigDecimal = projectSumService.memberAchievementsMonthCount(costVo2);
        return RestUtil.success(bigDecimal);
    }
    /**
     * 本年员工绩效同比上月
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/memberAchievementsMonthRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>memberAchievementsMonthRast(CostVo2 costVo2){
        BigDecimal memberAchievementsMonthCount = projectSumService.memberAchievementsMonthCount(costVo2);
        BigDecimal memberAchievementsLastMonthCount = projectSumService.memberAchievementsLastMonthCount(costVo2);
        BigDecimal bigDecimal = projectSumService.desiginCensusRast(memberAchievementsMonthCount, memberAchievementsLastMonthCount);
        return RestUtil.success(bigDecimal);
    }

    @RequestMapping(value = "/api/projectCount/MemberAchievementsCensus2",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>MemberAchievementsCensus2(CostVo2 costVo2){
        List<OneCensus4> oneCensus4s = projectSumService.MemberAchievementsCensus2(costVo2);
        String censusList = "[{\"companyName\":\"应计提金额\"," +
                "\"imageAmmount\": [";
        for (OneCensus4 oneCensus2 : oneCensus4s) {
            censusList +=
                    "{\"time\": \"" + oneCensus2.getMemberName() + "\"," +
                            "\"truckAmmount\": \"" + oneCensus2.getTotal()+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"建议计提金额\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus4 oneCensus2 : oneCensus4s) {
            censusList += "{\"time\": \""+oneCensus2.getMemberName()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus2.getTotal().multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP)+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"余额\"," +
                        "\"imageAmmount\": [" ;
        for (OneCensus4 oneCensus2 : oneCensus4s) {
            censusList += "{\"time\": \""+oneCensus2.getMemberName()+"\"," +
                    "\"truckAmmount\": \"" + oneCensus2.getTotal().subtract(oneCensus2.getTotal().multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP))+"\"},";
        }
        censusList = censusList.substring(0,censusList.length() -1);
        censusList += "]}]";
        JSONArray objects = JSON.parseArray(censusList);
        return RestUtil.success(objects);
    }
}
