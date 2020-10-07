package net.zlw.cloud.designProject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.mapper.IncomeInfoMapper;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectSumService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @RequestMapping(value = "/api/projectCount/totalRevenue",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> totalRevenue(CostVo2 costVo2){
        BigDecimal bigDecimal = projectSumService.totalRevenue(costVo2);
        return RestUtil.success(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    @RequestMapping(value = "/api/projectCount/totalexpenditure",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> totalexpenditure(CostVo2 costVo2){
        BigDecimal bigDecimal = projectSumService.totalexpenditure(costVo2);
        return RestUtil.success(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

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
    @RequestMapping(value = "/api/projectCount/projectFlow",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>projectFlow(CostVo2 costVo2){
        List<BaseProject> baseProjects = projectSumService.projectFlow(costVo2);
        return RestUtil.success(baseProjects);
    }

    /**
     * 项目统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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
     * 本年项目数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/prjectCensusYear",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object>prjectCensusYear(CostVo2 costVo2){
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
        Integer yearCount = projectSumService.prjectCensusYear(costVo2);
        map.put("yearCount",yearCount);
        return RestUtil.success(map);
    }

    /**
     * 本月项目数量
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
     * 相比上月
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
     * 相比上年
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
     * 总收入构成
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectIncomeCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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
     * 总支出构成
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

    /**
     * 支出条形统计表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/expenditureAnalysis",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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
     * 支出列表
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/BaseProjectExpenditureList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> BaseProjectExpenditureList(CostVo2 costVo2){
        List<BaseProject> baseProjects = projectSumService.BaseProjectExpenditureList(costVo2);
        return RestUtil.success(baseProjects);
    }

    /**
     * 信息统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/BaseProjectInfoCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> BaseProjectInfoCensus(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.BaseProjectInfoCensus(costVo2);
        return RestUtil.success(baseProjectPageInfo.getList());
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
    @RequestMapping(value = "/api/projectCount/designCountCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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
    @RequestMapping(value = "/api/projectCount/projectDesignChangeList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectDesignChangeList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.projectDesignChangeList(costVo2);
        return RestUtil.success(baseProjectPageInfo.getList());
    }

    /**
     * 进度款支付分析
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/progressPaymentList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> progressPaymentList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.progressPaymentList(costVo2);
        return RestUtil.success(baseProjectPageInfo.getList());
    }

    /**
     * 签证变更统计
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectVisaChangeList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectVisaChangeList(CostVo2 costVo2){
        PageInfo<BaseProject> baseProjectPageInfo = projectSumService.projectVisaChangeList(costVo2);
        return RestUtil.success(baseProjectPageInfo.getList());
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
    @RequestMapping(value = "/api/projectCount/projectSettlementCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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
     * 设计部门统计 全局版
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/projectDesginCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectDesginCount(CostVo2 costVo2){
        OneCensus3 oneCensus3 = projectSumService.projectDesginCount(costVo2);
        return RestUtil.success(oneCensus3);
    }

    @RequestMapping(value = "/api/projectCount/projectDesginStatus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> projectDesginStatus(CostVo2 costVo2){
        List<OneCensus3> oneCensus3s = projectSumService.projectDesginStatus(costVo2);
        return RestUtil.success(oneCensus3s);
    }
    @RequestMapping(value = "/api/projectCount/censusList2",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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
    @RequestMapping(value = "/api/projectCount/censusList2MonthCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> censusList2MonthCount(CostVo2 costVo2){
        Integer month = projectSumService.censusList2MonthCount(costVo2);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("month",month);
        return RestUtil.success(map);
    }

    /**
     * 本年任务数量
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/censusList2YearCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> censusList2YearCount(CostVo2 costVo2){
        Integer year = projectSumService.censusList2YearCount(costVo2);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("year",year);
        return RestUtil.success(map);
    }

    /**
     * 同比上年
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/censusList2YearRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> censusList2YearRast(CostVo2 costVo2){
        Integer year = projectSumService.censusList2YearCount(costVo2);
        Integer lastYear = projectSumService.censusList2LastYearCount(costVo2);
        Integer yearRast = projectSumService.prjectCensusRast(year, lastYear);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("yearRast",yearRast);
        return RestUtil.success(map);
    }

    /**
     * 同比上月
     * @param costVo2
     * @return
     */
    @RequestMapping(value = "/api/projectCount/censusList2MonthRast",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> censusList2MonthRast(CostVo2 costVo2){
        Integer month = projectSumService.censusList2MonthCount(costVo2);
        Integer lastmonth = projectSumService.censusList2lastMonthCount(costVo2);
        Integer monthRast = projectSumService.prjectCensusRast(month, lastmonth);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("monthRast",monthRast);
        return RestUtil.success(map);
    }

    @RequestMapping(value = "/api/projectCount/desiginMoneyCensus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desiginMoneyCensus(CostVo2 costVo2){
        OneCensus5 oneCensus5 = projectSumService.desiginMoneyCensus(costVo2);
        Integer amount = oneCensus5.getAnhuiAnount()+oneCensus5.getWujiangAmount();
        Integer notamount = oneCensus5.getNotAmount() - amount;
        String josn =
                "[" +
                        "{\"value1\":"+amount+",\"name1\":\"已到账金额\"}," +
                        "{\"value1\":"+notamount+",name1:\"未到账金额'\"}," +
                        "]";
        JSONArray objects = JSON.parseArray(josn);
        return RestUtil.success(objects);
    }
    @RequestMapping(value = "/api/projectCount/desiginoutsource",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
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

    @RequestMapping(value = "/api/projectCount/desginCensusList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desginCensusList(CostVo2 costVo2){
        PageInfo<DesignInfo> designInfoPageInfo = projectSumService.desginCensusList(costVo2);
        return RestUtil.success(designInfoPageInfo.getList());
    }

    @RequestMapping(value = "/api/projectCount/desginCensusListByDesigner",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> desginCensusListByDesigner(CostVo2 costVo2){
        PageInfo<DesignInfo> designInfoPageInfo = projectSumService.desginCensusList(costVo2);
        return RestUtil.success(designInfoPageInfo.getList());
    }
}
